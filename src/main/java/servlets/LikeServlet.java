package servlets;
import java.io.IOException;  
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;  
import javax.servlet.http.HttpServletRequest;  
import javax.servlet.http.HttpServletResponse;  

import DAOs.Notification;
import DAOs.NotificationDAO;
import DAOs.NotificationDAOImpl;
import DAOs.PostDAO;
import DAOs.UserReport;
import DAOs.UserReportDAO;
import DAOs.UserReportDAOImpl;  

@WebServlet("/like")
public class LikeServlet extends HttpServlet {  
	private static final long serialVersionUID = 1L;
	@Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		PostDAO postDAO = (PostDAO) req.getSession(false).getAttribute("postDAO");
		NotificationDAO notificationDAO = (NotificationDAOImpl) req.getSession(false).getAttribute("notificationDAO");
		UserReportDAO userReportDAO = (UserReportDAOImpl) req.getSession(false).getAttribute("userReportDAO");
		String username = req.getParameter("username");
		String poster = req.getParameter("poster");
		int id = Integer.parseInt(req.getParameter("post_id"));
		String liker = (String) req.getSession(false).getAttribute("username");
		String datetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		if (req.getParameter("unlike").equals("true")) {
			postDAO.unlikePost(id, username, liker);
			UserReport userReport = new UserReport(liker, datetime, "unlike", poster);
			userReportDAO.save(userReport);
		} else {
			postDAO.likePost(id, username, liker);
			Notification notification = new Notification(poster, liker, "like", "false", datetime);
	        notificationDAO.save(notification);
	        UserReport userReport = new UserReport(liker, datetime, "like", poster);
			userReportDAO.save(userReport);
		}
	}
}  