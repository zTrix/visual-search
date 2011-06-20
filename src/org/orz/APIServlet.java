package org.orz;

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

    /**
     * Default constructor.
     */
    public APIServlet() {
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    @SuppressWarnings("deprecation")
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        String[] paths = request.getRequestURI().split("/");

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String url = "http://arnetminer.org/services/";
        int startyear = Integer.parseInt(request.getParameter("startyear"));
        int endyear = Integer.parseInt(request.getParameter("endyear"));
        String detail = request.getParameter("detail");
        int result[] = new int[endyear - startyear + 1];
        int detail_info[][] = new int[endyear - startyear + 1][5000];

        try {

            if (paths[2].equalsIgnoreCase("SearchConf")) {
                url = url + "search-publication?";
                String q = request.getParameter("conf");
                url = url + "q=" + q + "&u=oyster&start=1&num=5000";

                JSONObject tmp = new JSONObject(HttpTest.sendURL(url));
                JSONArray papers = tmp.getJSONArray("Results");
                for (int i = 0; i < papers.length(); i++) {
                    if (papers.getJSONObject(i).has("Jconfname")) {
                        String confname = (papers.getJSONObject(i))
                                .getString("Jconfname");
                        if (confname.contains(q)) {
                            JSONObject cur_paper = papers.getJSONObject(i);
                            if (cur_paper.has("Pubyear")) {
                                int cur_year = cur_paper.getInt("Pubyear");
                                if (cur_year >= startyear
                                        && cur_year <= endyear) {
                                    if (cur_paper.has("Citedby")) {
                                        result[cur_year - startyear] += cur_paper
                                                .getInt("Citedby");
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
                        result_array.put(
                                i,
                                new JSONObject().put("detail", detail_papers)
                                        .put("number", result[i])
                                        .put("year", i + startyear));
                    } else
                        result_array.put(
                                i,
                                new JSONObject().put("number", result[i]).put(
                                        "year", i + startyear));
                }

                PrintWriter out = response.getWriter();
                result_array.write(out);
            } else if (paths[2].equalsIgnoreCase("SearchExpert")) {
                url = url + "person/";
                String q = request.getParameter("name");
                url = url + URLEncoder.encode(q) + "?u=oyster&o=ttf";
                String tmp_string = HttpTest.sendURL(url);

                int start = tmp_string.indexOf("[");
                int end = tmp_string.lastIndexOf("]");
                String tmp_str = tmp_string.substring(start + 1, end);
                JSONObject tmp = new JSONObject(tmp_str);
                ;

                JSONArray papers = tmp.getJSONArray("PubList");
                for (int i = 0; i < papers.length(); i++) {
                    if (papers.getJSONObject(i).has("Pubyear")) {
                        JSONObject cur_paper = papers.getJSONObject(i);
                        int pubyear = cur_paper.getInt("Pubyear");
                        if (pubyear >= startyear && pubyear <= endyear) {
                            if (cur_paper.has("Citedby")) {
                                result[pubyear - startyear] += cur_paper
                                        .getInt("Citedby");
                                detail_info[pubyear - startyear][i] = 1;
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
                        result_array.put(
                                i,
                                new JSONObject().put("detail", detail_papers)
                                        .put("number", result[i])
                                        .put("year", i + startyear));
                    } else
                        result_array.put(
                                i,
                                new JSONObject().put("number", result[i]).put(
                                        "year", i + startyear));
                }

                PrintWriter out = response.getWriter();
                result_array.write(out);
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        doGet(request, response);
    }

}
