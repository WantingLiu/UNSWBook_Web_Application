package servlets;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bson.types.ObjectId;

import DAOs.MorphiaService;
import DAOs.Notification;
import DAOs.NotificationDAO;
import DAOs.NotificationDAOImpl;
import DAOs.User;
import DAOs.UserDAO;
import DAOs.UserDAOImpl;
import DAOs.UserReport;
import DAOs.UserReportDAO;
import DAOs.UserReportDAOImpl;

/**
 * doGet:
 * 	- confirms friend requests sent via email (given notificationId in email url)
 *	- creates new accetpReq notification
 *	- deletes corresponding friendReq notification
 * doPost: handles friend requests accepted through the app itself (i.e. via notification.jsp)
 */

@WebServlet("/friendRequest")
public class FriendRequestServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {		
		MorphiaService morphiaService = (MorphiaService) getServletContext().getAttribute("morphiaService");
		UserDAO userDAO = new UserDAOImpl(User.class, morphiaService.getDatastore());
		NotificationDAO notificationDAO = new NotificationDAOImpl(Notification.class, morphiaService.getDatastore());
		
		// get handle on friend request that was clicked (using notificationId from notifications.jspi)
		ObjectId notificationId = new ObjectId(req.getParameter("notificationId"));
		Notification notification = notificationDAO.get(notificationId);
		
		// if notification exists, then repeat methods in doPost;
		// otherwise (i.e. was already accepted through the browser itself) do nothing
		if (notification != null) {
			// get the sender and receiver of friend request
			String sender = notification.getFromUser();
			String receiver = notification.getToUser();
			// append to both users' friend lists
			userDAO.AddFriend(sender, receiver);
			userDAO.AddFriend(receiver, sender);
			// create new notification
			String dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
			Notification reqAccepted = new Notification(sender, receiver, "reqAccepted", "false", dateTime);
			notificationDAO.save(reqAccepted);
			// remove accepted friend request from database
			notificationDAO.deleteById(notificationId);
		}
		
		// forward user to login/index
		req.getRequestDispatcher("/login").forward(req, resp);
	}
	
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	// get handle on relevant DAOs
    		//MorphiaService morphiaService = new MorphiaService();
		UserDAO userDAO = (UserDAO) req.getSession(false).getAttribute("userDAO");
		NotificationDAO notificationDAO = (NotificationDAO) req.getSession(false).getAttribute("notificationDAO");
		UserReportDAO userReportDAO = (UserReportDAOImpl) req.getSession(false).getAttribute("userReportDAO");
		String datetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		// get handle on friend request that was clicked (using notificationId from notifications.jspi)
		ObjectId notificationId = new ObjectId(req.getParameter("notificationId"));
		Notification notification = notificationDAO.get(notificationId);
		
		// get the sender and receiver of friend request
        String sender = notification.getFromUser();
        String receiver = notification.getToUser();
      
        // append to both users' friend lists
        userDAO.AddFriend(sender, receiver);
        userDAO.AddFriend(receiver, sender);
        
        // remove accepted friend request from database
        notificationDAO.deleteById(notificationId);
        
        // write to response to allow NotificationServlet to send "_ accepted your friend request" to sender (see notification.js) 
        resp.getWriter().write(sender);
        
        UserReport userReport = new UserReport(receiver, datetime, "acceptReq", sender);
		userReportDAO.save(userReport);
    }
}