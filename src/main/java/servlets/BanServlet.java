package servlets;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;  
import javax.servlet.http.HttpServletRequest;  
import javax.servlet.http.HttpServletResponse;  

import DAOs.MorphiaService;
import DAOs.User;
import DAOs.UserDAO;
import DAOs.UserDAOImpl;
import DAOs.UserReport;
import DAOs.UserReportDAO;
import DAOs.UserReportDAOImpl;  

@WebServlet("/ban")
public class BanServlet extends HttpServlet {  
	private static final long serialVersionUID = 1L;
	@Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		MorphiaService morphiaService = (MorphiaService) getServletContext().getAttribute("morphiaService");
		UserDAO userDAO = new UserDAOImpl(User.class, morphiaService.getDatastore());
		UserReportDAO userReportDAO = (UserReportDAOImpl) req.getSession(false).getAttribute("userReportDAO");
		String username = req.getParameter("username");
		String type = req.getParameter("type");
		String datetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		if (type.equals("ban")) {
			userDAO.banUser(username);
			UserReport userReport = new UserReport(username, datetime, "ban", "admin");
			userReportDAO.save(userReport);
		} else {
			userDAO.unbanUser(username);
			UserReport userReport = new UserReport(username, datetime, "unban", "admin");
			userReportDAO.save(userReport);
		}
	}
}  