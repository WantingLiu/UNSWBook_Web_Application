package servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import helpers.RestExtract;

@WebServlet("/extract")
public class ExtractServlet extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String content = req.getParameter("content");
        System.out.println("input:"+content);
        String type = req.getParameter("type");
        System.out.println(type);
        RestExtract re=new RestExtract();
        String result = "";
        switch (type){
            case "1":
                result=re.ExtractFeaturesRest("http://d2dcrc.cse.unsw.edu.au:9091/ExtractionAPI-0.0.1-SNAPSHOT/rest/keyword","sentence",content);
                break;
            case "2":
                result=re.ExtractFeaturesRest("http://d2dcrc.cse.unsw.edu.au:9091/ExtractionAPI-0.0.1-SNAPSHOT/rest/entity/person","ent",content);
                break;
            case "3":
                result=re.ExtractFeaturesRest("http://d2dcrc.cse.unsw.edu.au:9091/ExtractionAPI-0.0.1-SNAPSHOT/rest/entity/organization","ent",content);
                break;
            case "4":
                result=re.ExtractFeaturesRest("http://d2dcrc.cse.unsw.edu.au:9091/ExtractionAPI-0.0.1-SNAPSHOT/rest/entity/continent","ent",content);
                break;
        }
        System.out.println("re:"+result);
        resp.setContentType("text/html;charset=UTF-8");
        resp.getWriter().write(result);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
