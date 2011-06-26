package org.orz;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.HttpURLConnection;
import java.net.URLEncoder;

import org.json.JSONObject;
import org.json.JSONArray;


public class HttpTest {

    public static String sendURL(String urlStr) {
    	System.out.println("send url : " + urlStr);
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
        return new String(result);
    }
}
