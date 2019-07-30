package servlets;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import DAOs.User;
import DAOs.UserDAO;
import DAOs.UserReport;
import DAOs.UserReportDAO;
import DAOs.UserReportDAOImpl;
import helpers.Search;

@WebServlet("/userHistory")
public class UserHistoryServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		UserDAO userDAO = (UserDAO) req.getSession(false).getAttribute("userDAO");
		UserReportDAO userReportDAO = (UserReportDAOImpl) req.getSession(false).getAttribute("userReportDAO");
		User current_user = userDAO.getByUsername((String) req.getSession(false).getAttribute("username"));
		String username = req.getParameter("username");
		if (current_user.getAdmin().equals("true")) {
			List<UserReport> userHistories = userReportDAO.getAllHistories(username);
			req.setAttribute("userHistories", userHistories);
			req.setAttribute("username", username);
			req.getRequestDispatcher("/user_history.jsp").forward(req, resp);
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		UserDAO userDAO = (UserDAO) req.getSession(false).getAttribute("userDAO");
		UserReportDAO userReportDAO = (UserReportDAOImpl) req.getSession(false).getAttribute("userReportDAO");
		User current_user = userDAO.getByUsername((String) req.getSession(false).getAttribute("username"));
		String username = req.getParameter("username");

		if (current_user.getAdmin().equals("true")) {
			List<UserReport> allUserHistories = userReportDAO.getAllHistories(username);
			
			ArrayList<String> values = new ArrayList<String>();
			String[] form_fields = new String[] { "type", "from", "to" };
			for (int i = 0; i < form_fields.length; i++) {
				String value = req.getParameter(form_fields[i]);
				if (value == null) {
					value = "";
				}
				values.add(value);
			}

			List<UserReport> userHistories = new ArrayList<UserReport>();
			try {
				userHistories = Search.userReportsFilter(allUserHistories, values);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			req.setAttribute("username", username);
			req.setAttribute("userHistories", userHistories);
			req.getRequestDispatcher("/user_history.jsp").forward(req, resp);
		}
	}
}
