package org.orz;

import org.orz.util.*;
import org.orz.data.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Servlet implementation class HelloServlet
 */
@WebServlet("/APIServlet")
public class APIServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public APIServlet() { }

    @SuppressWarnings("deprecation")
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String[] paths = request.getRequestURI().split("/");

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String url = "http://arnetminer.org/services";

        String api = null;
        if (paths[2].equalsIgnoreCase("api")) {
            if (paths.length > 3) {
                api = paths[3];
            }
        } else {
            for (int i = 0; i < paths.length; i++) {
                if (paths[i].equalsIgnoreCase("api")) {
                    if (i < paths.length - 1) {
                        api = paths[i+1];
                    }
                }
            }
        }
        if (null == api) {
            response.getWriter().write("{\"err\":-1, \"msg\":\"cannot get api\"}");
            return;
        }

        try {
            if (api.equalsIgnoreCase("SearchConf")) {
                int startyear = Integer.parseInt(request.getParameter("start_year"));
                int endyear = Integer.parseInt(request.getParameter("end_year"));
                String detail = request.getParameter("detail");
                int result[] = new int[endyear - startyear + 1];
                int detail_info[][] = new int[endyear - startyear + 1][100];

                url = url + "/search-publication?";
                String q = request.getParameter("conf");
                url = url + "q=" + q + "&u=tangwb06&start=1&num=100";
                String rs = HttpTest.sendURL(url);
                response.getWriter().write(rs);
                if (rs != null) return;
                JSONObject tmp = new JSONObject(rs);
                JSONArray papers = tmp.getJSONArray("Results");
                for (int i = 0; i < papers.length(); i++) {
                    if (papers.getJSONObject(i).has("Jconfname")) {
                        String confname = (papers.getJSONObject(i)).getString("Jconfname");
                        if (confname.contains(q)) {
                            JSONObject cur_paper = papers.getJSONObject(i);
                            if (cur_paper.has("Pubyear")) {
                                int cur_year = cur_paper.getInt("Pubyear");
                                if (cur_year >= startyear && cur_year <= endyear) {
                                    if (cur_paper.has("Citedby")) {
                                        result[cur_year - startyear] += cur_paper.getInt("Citedby");
                                        detail_info[cur_year - startyear][i] = 1;
                                    }
                                }
                            }
                        }
                    }
                }
                JSONArray result_array = new JSONArray();
                for (int i = 0; i < endyear - startyear + 1; i++) {
                    if (detail.equals("true")) {
                        JSONArray detail_papers = new JSONArray();
                        int lable = 0;
                        for (int j = 0; j < papers.length(); j++) {
                            if (detail_info[i][j] == 1) {
                                detail_papers.put(lable,
                                        papers.getJSONObject(j));
                                lable++;
                            }
                        }
                        result_array.put(i,
                                         new JSONObject().put("detail", detail_papers)
                                                         .put("number", result[i])
                                                         .put("year", i + startyear));
                    } else {
                        result_array.put(
                                i,
                                new JSONObject().put("number", result[i]).put(
                                        "year", i + startyear));
                    }
                }
                new JSONObject().put("err", 0)
                                .put("result", result_array)
                                .write(response.getWriter());
            } else if (api.equalsIgnoreCase("person")) {
                int startyear = Integer.parseInt(request.getParameter("start_year"));
                int endyear = Integer.parseInt(request.getParameter("end_year"));
                int num = endyear - startyear + 1;
                
                int []pubNum = new int[num];
                int []citeNum = new int[num];

                String q = request.getParameter("q");
                url = url + "/person/" + URLEncoder.encode(q) + "?u=tangwb06&o=ttf";
                String tmp_string = HttpTest.sendURL(url);
                JSONArray tmp = new JSONArray(tmp_string);
                JSONObject person = null;
                if (tmp.length() > 0) {
                    person = tmp.getJSONObject(0);
                } else {
                    new JSONObject().put("err", -100)
                                    .put("msg", "no result")
                                    .write(response.getWriter());
                    return;
                }

                JSONArray papers = person.getJSONArray("PubList");
                for (int i = 0; i < papers.length(); i++) {
                    JSONObject cur_paper = papers.getJSONObject(i);
                    if (cur_paper.has("Pubyear")) {
                        int pubyear = cur_paper.getInt("Pubyear");
                        if (pubyear <= endyear && pubyear >= startyear) {
                            pubNum[pubyear - startyear]++;
                            if (cur_paper.has("Citedby")) {
                                citeNum[pubyear - startyear] += cur_paper.getInt("Citedby");
                            }
                        }
                    }
                }
                JSONArray result = new JSONArray();
                result.put(new JSONObject().put("name", "Publication Number")
                                           .put("data", new JSONArray(pubNum))
                );
                result.put(new JSONObject().put("name", "Citation Number")
                                           .put("data", new JSONArray(citeNum))
                );

                new JSONObject().put("err", 0)
                                .put("data", result)
                                .put("name", person.getString("Name"))
                                .put("photo", person.getString("PictureUrl"))
                                .write(response.getWriter());
            } else {
                new JSONObject().put("err", -3)
                                .put("msg", "no such api: " + api)
                                .write(response.getWriter());
            }

        } catch (JSONException e) {
            response.getWriter().write("{\"err\":-2,\"msg\":\"json exception\",\"desc\":\"" + Stringer.escapeStringForJson(e.getMessage()) + "\",\"req_url\":\"" + Stringer.escapeStringForJson(url) + "\"}");
            e.printStackTrace();
        }
        response.getWriter().flush();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

}
