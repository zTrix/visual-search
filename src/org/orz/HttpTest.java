package org.orz;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.HttpURLConnection;
import java.net.URLEncoder;

import org.json.JSONObject;
import org.json.JSONArray;

import org.orz.util.*;

public class HttpTest {

    public static String sendURL(String urlStr) {
    	System.out.println("send url : " + urlStr);
        
        String bin_path = new HttpTest().getClass().getClassLoader().getResource("/").getPath();
        File root = new File(bin_path);

        String [] rs = null;
        File dir = null;
        try {
            int qu = urlStr.indexOf('?');
            String filePath;
            if (qu > 31) {
                filePath = urlStr.substring(31, qu);
            } else {
                filePath = urlStr.substring(31);
            }
            rs = filePath.split("/");
            if (rs.length < 2) {
                rs = new String[2];
                rs[0] = "search-publication";
                int left = urlStr.indexOf("q=") + 2;
                int right = urlStr.indexOf("&u=");
                ZLog.info("left  = " + left);
                ZLog.info("right = " + right);
                rs[1] = urlStr.substring(left, right);
            }
            ZLog.info("rs[0] = " + rs[0]);
            ZLog.info("rs[1] = " + rs[1]);
            dir = new File(root, rs[0]);
            if (!dir.exists()) {
                dir.mkdir();
            } else {
                File f = new File(dir, rs[1]);
                if (f.exists()) {   // cache hit
                    FileInputStream in = new FileInputStream(f);
                    byte[] json = new byte[in.available()];
                    in.read(json);
                    return new String(json);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        URL url;
        String result = new String();
        String tmp = new String();
        try {
            url = new URL(urlStr);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(true);

            InputStream in = urlConnection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(in));
            
            while((tmp = rd.readLine()) != null) {
                result = result + tmp;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // cache
        try {
            if (rs != null && rs.length >= 2 && dir != null) {
                File f = new File(dir, rs[1]);
                BufferedWriter out = new BufferedWriter(
                                     new OutputStreamWriter(
                                     new FileOutputStream(f)));
                out.write(result);
                out.close();
            } else {
                if (rs == null) {
                    ZLog.err("write cache failed, rs null");
                }
                if (rs.length < 2) {
                    ZLog.err("write cache failed, rs length < 2");
                }
                if (dir == null) {
                    ZLog.err("write cache failed, dir = null");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
