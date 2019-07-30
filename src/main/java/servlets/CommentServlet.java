package servlets;
import java.io.IOException;  
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;  
import javax.servlet.http.HttpServletRequest;  
import javax.servlet.http.HttpServletResponse;  

import DAOs.CommentDAO;
import DAOs.CommentDAOImpl;
import DAOs.Notification;
import DAOs.NotificationDAO;
import DAOs.NotificationDAOImpl;
import DAOs.UserReport;
import DAOs.UserReportDAO;
import DAOs.UserReportDAOImpl;
import helpers.ExtractAPI;
import helpers.URLtoLink;

@WebServlet("/comment")
public class CommentServlet extends HttpServlet {  
	private static final long serialVersionUID = 1L;
	@Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		CommentDAO commentDAO = (CommentDAOImpl) req.getSession(false).getAttribute("commentDAO");
		NotificationDAO notificationDAO = (NotificationDAOImpl) req.getSession(false).getAttribute("notificationDAO");
		UserReportDAO userReportDAO = (UserReportDAOImpl) req.getSession(false).getAttribute("userReportDAO");
		String username = req.getParameter("username");
		System.out.println("username123: " + username);
		int id = Integer.parseInt(req.getParameter("id"));
		String commenter = (String) req.getSession(false).getAttribute("username");
		String poster = req.getParameter("poster");
		String datetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String comment = req.getParameter("comment");
        String finalComment = URLtoLink.urlToLink(comment);
        // create comment, and create user report for comment
        commentDAO.createComment(username, id, commenter, datetime, finalComment);
        Notification notification = new Notification(poster, commenter, "comment", "false", datetime);
        notificationDAO.save(notification);
        UserReport userReport = new UserReport(commenter, datetime, "comment", poster);
		userReportDAO.save(userReport);

		String bullyWords= (String) getServletContext().getAttribute("bullyWords");
		String bully = ExtractAPI.extractBully(bullyWords,comment);
		System.out.println("bully:"+bully);
        // if there are bully words in the comment, then create notification (to admin) and add to user report
		if (bully != null && !bully.equals("")) {
			userReport = new UserReport(commenter, datetime, "bullyPost", null);
			userReportDAO.save(userReport);
			Notification bullyNotification = new Notification("harry", commenter, "bullyPost", "false", datetime);
			notificationDAO.save(bullyNotification);
			// send email to admin (placeholder email for now)

			String email = "avril.liuwanting@gmail.com";
			String postType = "comment";
			String path = "/email?emailType=bullyPost"
						+ "&email=" + email
						+ "&postType=" + postType
						+ "&commenter=" + commenter
						+ "&poster=" + poster
						+ "&wall=" + username;
			System.out.println("you've been a bad boy. These words relate to bullying:"+bully);
			req.getRequestDispatcher(path).forward(req, resp);
		}
	}
}  