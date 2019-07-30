package servlets;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;  
import javax.servlet.http.HttpServletRequest;  
import javax.servlet.http.HttpServletResponse;  
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;

import DAOs.User;
import DAOs.UserDAO;  

@WebServlet("/graph")
public class GraphServlet extends HttpServlet {  
	private static final long serialVersionUID = 1L;
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {  
		req.getRequestDispatcher("/graph.jsp").forward(req, resp);
    }
	
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		UserDAO userDAO = (UserDAO) req.getSession(false).getAttribute("userDAO");
		List<User> users = userDAO.getAllUsers();
		Gson gson = new Gson();
		String json = gson.toJson(users);
		resp.getWriter().write(json);
	}
}  