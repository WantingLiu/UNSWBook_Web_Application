package servlets;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import DAOs.Notification;
import DAOs.NotificationDAO;
import DAOs.NotificationDAOImpl;
import DAOs.UserReport;
import DAOs.UserReportDAO;
import DAOs.UserReportDAOImpl;

@WebServlet("/notification")
public class NotificationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		NotificationDAO notificationDAO = (NotificationDAOImpl) req.getSession(false).getAttribute("notificationDAO");
		String current_user = (String) req.getSession(false).getAttribute("username");
		List<Notification> notifications = notificationDAO.getNotificationsByUser(current_user);
		req.setAttribute("notifications", notifications);
		req.getRequestDispatcher("/notifications.jsp").forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		NotificationDAO notificationDAO = (NotificationDAOImpl) req.getSession(false).getAttribute("notificationDAO");
		UserReportDAO userReportDAO = (UserReportDAOImpl) req.getSession(false).getAttribute("userReportDAO");
		String fromUser = (String) req.getSession(false).getAttribute("username");
		String type = req.getParameter("type");
		boolean ajax = "XMLHttpRequest".equals(req.getHeader("X-Requested-With"));
		if (ajax) {
			if (type.equals("polling")) {
				List<Notification> notifications = notificationDAO.getNewNotificationsByUser(fromUser);
				Gson gson = new Gson();
				String json = gson.toJson(notifications);
				resp.getWriter().write(json);
			} else {
				if (type.equals("read")) {
					String username = req.getParameter("username");
					notificationDAO.markAllAsRead(username);
				} else {
					String toUser = req.getParameter("toUser");
					String datetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
	
					Notification notification = new Notification(toUser, fromUser, type, "false", datetime);
					notificationDAO.save(notification);
					
					// write to response to allow EmailServlet to get a handle on the notificationId (see notification_handler.js) 
					resp.getWriter().write(notification.getObjectId().toString());
					
					UserReport userReport;
					if (type.equals("friendReq")) {
						userReport = new UserReport(fromUser, datetime, type, toUser);
					} else {
						userReport = new UserReport(toUser, datetime, type, fromUser);
					}
					userReportDAO.save(userReport);
				}
			}
		}
	}
}
