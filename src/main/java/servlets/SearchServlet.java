package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import DAOs.MorphiaService;
import DAOs.Notification;
import DAOs.NotificationDAO;
import DAOs.NotificationDAOImpl;
import DAOs.User;
import DAOs.UserDAO;
import DAOs.UserDAOImpl;
import helpers.*;

@WebServlet("/search")
public class SearchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		UserDAO userDAO = (UserDAO) req.getSession(false).getAttribute("userDAO");
		NotificationDAO notificationDAO = (NotificationDAO) req.getSession(false).getAttribute("notificationDAO");
		String username = (String) req.getSession(false).getAttribute("username");
		List<User> users = Search.searchAllUser(userDAO);
		List<String> users_requests_sent = Search.search_users_requests_sent(username, users, notificationDAO);
		req.setAttribute("users", users);
		req.setAttribute("users_requests_sent", users_requests_sent);
		req.getRequestDispatcher("/search.jsp").forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// get handle on relevant DAOs
		UserDAO userDAO = (UserDAO) req.getSession(false).getAttribute("userDAO");
		NotificationDAO notificationDAO = (NotificationDAO) req.getSession(false).getAttribute("notificationDAO");
		// populate search results
		String username = (String) req.getSession(false).getAttribute("username");
		User current_user = userDAO.getByUsername(username);
		
		List<User> users;
		if (req.getParameter("advanced_search") != null) {
			ArrayList<String> values = new ArrayList<String>();
			String[] form_fields = new String[] { "first_name", "last_name", "gender", "date" };
			for (int i = 0; i < form_fields.length; i++) {
				String value = req.getParameter(form_fields[i]);
				if (value == null) {
					value = "";
				}
				values.add(value);
			}
			users = Search.advanceSearch(values, userDAO);
		} else {
			String search = req.getParameter("search");
			users = Search.search(search, userDAO);
		}

		
		List<String> users_requests_sent = Search.search_users_requests_sent(username,users, notificationDAO);

		req.setAttribute("users", users);
		req.setAttribute("current_user", current_user);
		req.setAttribute("users_requests_sent", users_requests_sent);
		req.getRequestDispatcher("/search.jsp").forward(req, resp);
	}
}