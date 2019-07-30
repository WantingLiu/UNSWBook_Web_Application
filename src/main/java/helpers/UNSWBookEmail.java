package helpers;

import java.util.Properties;

import javax.mail.PasswordAuthentication;
import javax.mail.Session;

public class UNSWBookEmail {

	private Session session;

	public UNSWBookEmail() {

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.ssl.trust", "smtp.gmail.com"); // NOTE: needed due to operating system or browser-specific
															// issues
		setSession(props);
	}

	public Session getSession() {
		return session;
	}

	private void setSession(Properties props) {
		this.session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				String mail_username = "unswbook.noreply@gmail.com";
				String mail_password = "comp9321";
				return new PasswordAuthentication(mail_username, mail_password);
			}
		});
	}

	
	
}
