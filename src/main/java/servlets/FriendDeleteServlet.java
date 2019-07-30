package servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import DAOs.User;
import DAOs.UserDAO;

/**
 * Servlet implementation class FriendDeleteServlet
 */
@WebServlet("/friendDelete")
public class FriendDeleteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FriendDeleteServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		UserDAO userDAO = (UserDAO) req.getSession(false).getAttribute("userDAO");	
		String username = (String) req.getSession(false).getAttribute("username");
		String friendName = req.getParameter("friendname");
		String delete = req.getParameter("deleteFriend");
		if (delete != null) {
			userDAO.DeleteFriend(username, friendName);
			userDAO.DeleteFriend(friendName, username);
		}		
		User current_user = userDAO.getByUsername((String) req.getSession(false).getAttribute("username"));
		req.setAttribute("user", current_user);
		resp.sendRedirect("profile");
	}

}
