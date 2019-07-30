package servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import DAOs.CommentDAO;
import DAOs.CommentDAOImpl;
import DAOs.MorphiaService;
import DAOs.Notification;
import DAOs.NotificationDAO;
import DAOs.NotificationDAOImpl;
import DAOs.PostDAO;
import DAOs.PostDAOImpl;
import DAOs.User;
import DAOs.UserDAO;
import DAOs.UserDAOImpl;
import DAOs.UserReport;
import DAOs.UserReportDAO;
import DAOs.UserReportDAOImpl;

import java.io.IOException;


@MultipartConfig
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getSession().setAttribute("admin_page_used",false);
		req.getRequestDispatcher("/index.jsp").forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		MorphiaService morphiaService = (MorphiaService) getServletContext().getAttribute("morphiaService");
		UserDAO userDAO = new UserDAOImpl(User.class, morphiaService.getDatastore());
		String username = req.getParameter("username");
		User user = userDAO.getByUsername(username);
		try {
			if (username == null || !user.getPassword().equals(req.getParameter("password")) || !user.getConfirmed().equals("true")) {
				req.setAttribute("failure", "password");
				if ((Boolean) req.getSession().getAttribute("admin_page_used")) {
					req.getRequestDispatcher("/admin_login.jsp").forward(req, resp);
					return;
				}
				req.getRequestDispatcher("/index.jsp").forward(req, resp);
				return;
			} else if (user.getBanned()) {
				req.getRequestDispatcher("/ban.jsp").forward(req, resp);
				return;
			} else if (user.getAdmin().equals("true") && !(Boolean) req.getSession().getAttribute("admin_page_used")) {
				req.setAttribute("failure", "password");
				req.getRequestDispatcher("/index.jsp").forward(req, resp);
				return;
			} else if (user.getAdmin().equals("false") && (Boolean) req.getSession().getAttribute("admin_page_used")) {
				req.setAttribute("failure", "password");
				req.getRequestDispatcher("/admin_login.jsp").forward(req, resp);
				return;
			}
			PostDAO postDAO = new PostDAOImpl(User.class, morphiaService.getDatastore());
			CommentDAO commentDAO = new CommentDAOImpl(User.class, morphiaService.getDatastore());
			NotificationDAO notificationDAO = new NotificationDAOImpl(Notification.class, morphiaService.getDatastore());
			UserReportDAO userReportDAO = new UserReportDAOImpl(UserReport.class, morphiaService.getDatastore());
			HttpSession session = req.getSession();
			session.setMaxInactiveInterval(20*60);
			session.setAttribute("username", req.getParameter("username"));
			session.setAttribute("current_user", userDAO.getByUsername(username));
			session.setAttribute("userDAO", userDAO);
			session.setAttribute("postDAO", postDAO);
			session.setAttribute("commentDAO", commentDAO);
			session.setAttribute("notificationDAO", notificationDAO);
			session.setAttribute("userReportDAO", userReportDAO);
			resp.sendRedirect("home");
		} catch (Exception e) {
			req.setAttribute("failure", false);
			req.getRequestDispatcher("/index.jsp").forward(req, resp);
		}
		

	}
}