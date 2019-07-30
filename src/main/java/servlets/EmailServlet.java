package servlets;

import java.io.IOException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bson.types.ObjectId;

import DAOs.Notification;
import DAOs.NotificationDAO;

/**
 * Given email type parameter (register or friend request), sends the appropriate type of email
 *  - common parameters: emailType and email (recipient)
 * 	- if email type == registration, then construct email out of get parameter "username", and "uuid", and forward user to login page
 * 	- if email type == add friend request, then construct email out of get parameter "toUser", "toUserEmail" (don't forward user)
 *  - if email type == user made a "bully" post, then construct email out of get parameter "" and forward user back to home page
 *  	TODO: ^ forwarding back to home somewhat hacky... might not need to do this if we use websockets instead of polling
 *  	consider a PostServlet that can be used for both profile.jsp and home.jsp, then call this via ajax (notification_handler.js)
 */
@WebServlet("/email")
public class EmailServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// get handle on relevant DAOs
		NotificationDAO notificationDAO = (NotificationDAO) req.getSession(false).getAttribute("notificationDAO");
		
		// grab common parameters sent from RegisterServlet
		String emailType = req.getParameter("emailType");
		String email = req.getParameter("email");
		
		// initialize sender email
		final String mail_username = "unswbook.noreply@gmail.com";
		final String mail_password = "comp9321";
		
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.ssl.trust", "smtp.gmail.com"); // NOTE: needed due to operating system or browser-specific issues
		
		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(mail_username, mail_password);
			}
		});
		
		// set message contents
		String messageSubject = "";
		String messageText = "";
		// grab different params based on type of email
		if (emailType.equals("friendReq")) {
			// get handle on friend request that was clicked (using notificationId from notifications.jspi)
			String notificationIdAsString = req.getParameter("notificationId");
			ObjectId notificationId = new ObjectId(notificationIdAsString);
			Notification notification = notificationDAO.get(notificationId);
			// get the sender and receiver of friend request
		    String sender = notification.getFromUser();
		    String receiver = notification.getToUser();
			messageSubject = "Friend Request";
			messageText = "Dear " + receiver + ", " + "\n\n Use the below link to accept friend request from " + sender
					+ "\n http://localhost:8080/unswbook/friendRequest?notificationId=" + notificationIdAsString;
					// NOTE: get rid of "unswbook" depending on where you stored your project
		} else if (emailType.equals("confirmRegistration")) {
			// grab username (email recipient) and uuid
			String username = req.getParameter("username");
			String uuid = req.getParameter("uuid");
			messageSubject = "Welcome to UNSWBook";
			messageText = "Dear " + username + ", " + "\n\n Use the below link to activate your UNSWBook account!"
					+ "\n http://localhost:8080/unswbook/RegisterServlet?confirm=" + uuid;
		} else if (emailType.equals("bullyPost")) {
			messageSubject = "Bully Post";
			messageText = "Dear admin, user with username '";
			// separate cases where bully poster made a new post or commented on a post
			if (req.getParameter("postType").equals("post")) {
				String poster = req.getParameter("poster");
				String wall = req.getParameter("wall");
				messageText += poster + "' has made a heinous post on " + wall + "'s wall.";
			} else {
				String commenter = req.getParameter("commenter");
				String poster = req.getParameter("poster");
				String wall = req.getParameter("wall");
				messageText += commenter + "' has made a heinous comment on " + poster + "'s post on " + wall + "'s wall.";
			}
			messageText += "\n\n Use the below link to log in to unswbook and smite this fool!"
						+ "\n http://localhost:8080/unswbook/SuperSecretAdminLoginPage";
		}
		
		try {
			// initialize email sender and receiver
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("unswbook.noreply@gmail.com"));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
			// initialize email contents
			message.setSubject(messageSubject);
			message.setText(messageText);
			// send email
			Transport.send(message);
			// if user clicks "add friend" in search results, don't redirect
			// TODO: for now, making a bullyPost (from home) redirects user to home page 
			// consider some subtext like "you've made a bad post - prepare to get banned" under the text submission area in profile.jsp or home.jsp
			if (emailType.equals("confirmRegistration")) {
				req.getRequestDispatcher("/login").forward(req, resp);
			} else if (emailType.equals("bullyPost")) {
				req.getRequestDispatcher("/home").forward(req, resp);
			}
			return;
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
}
