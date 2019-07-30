package servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import DAOs.*;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import helpers.FileUpload;
import helpers.StreamToString;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;

import com.google.gson.Gson;

import helpers.URLtoLink;
import helpers.ExtractAPI;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.*;

@MultipartConfig
@WebServlet("/profile")
public class ProfileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		UserDAO userDAO = (UserDAO) req.getSession(false).getAttribute("userDAO");
		String username;
		User current_user = userDAO.getByUsername((String) req.getSession(false).getAttribute("username"));
		if (req.getParameter("username") != null) {
			username = req.getParameter("username");
		} else {
			username = current_user.getUsername();
		}
		User user = userDAO.getByUsername(username);
		req.setAttribute("user", user);
		req.setAttribute("current_user", current_user);
		req.getRequestDispatcher("/profile.jsp").forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String poster = (String) req.getSession(false).getAttribute("username");
		UserDAO userDAO = (UserDAO) req.getSession(false).getAttribute("userDAO");
		PostDAO postDAO = (PostDAO) req.getSession(false).getAttribute("postDAO");
		UserReportDAO userReportDAO = (UserReportDAOImpl) req.getSession(false).getAttribute("userReportDAO");
		boolean ajax = "XMLHttpRequest".equals(req.getHeader("X-Requested-With"));
		if (ajax) {
			if (req.getParameter("user_details") == null) {
				String username = req.getParameter("username");
				User current_user = userDAO.getByUsername((String) req.getSession(false).getAttribute("username"));
				if (req.getParameter("username") == null) {
					username = current_user.getUsername();
				}
				if (current_user.getUsername() == username || current_user.getFriends().contains(username)) {
					User user = userDAO.getByUsername(username);
					List<Post> posts = user.getAllPosts();
					posts.sort(Post.DateTimeComparator);
					Gson gson = new Gson();
					String json = gson.toJson(posts);
					resp.getWriter().write(json);
				}
			} else {
				String username = req.getParameter("username");
				User user = userDAO.getByUsername(username);
				Gson gson = new Gson();
				String json = gson.toJson(user);
				resp.getWriter().write(json);
			}
		} else {
			FileUpload fileUpload = new FileUpload();

			List formItems = null;
			try {
				formItems = fileUpload.getUpload().parseRequest(req);
			} catch (FileUploadException e) {
				e.printStackTrace();
			}

			String content = "";
			String username = "";

			Iterator iter = formItems.iterator();

			FileItem fileItem = null;

			while (iter.hasNext()) {
				FileItem item = (FileItem) iter.next();
				// processes only fields that are not form fields
				if (!item.isFormField()) {
					fileItem = item;
				} else {
					if (item.getFieldName().equals("content")){
						content = StreamToString.getStringFromInputStream(item.getInputStream());
					}
					if (item.getFieldName().equals("username")){
						username = StreamToString.getStringFromInputStream(item.getInputStream());
					}
				}
			}
			String url = null;
			if (fileItem.getSize() != 0){
				Cloudinary cloudinary = (Cloudinary) getServletContext().getAttribute("cloudinary");

				Map<String,Object> result = cloudinary.uploader().upload(fileItem.get(), ObjectUtils.emptyMap());

				url = (String)result.get("secure_url");
			}
			String datetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
			
			User user = userDAO.getByUsername(username);
			int id = user.getAllPosts().size() + 1;
			String finalContent = URLtoLink.urlToLink(content);
			String bullyWords= (String) getServletContext().getAttribute("bullyWords");
			String bully = ExtractAPI.extractBully(bullyWords,content);
			System.out.println("bully:"+bully);
			postDAO.createPost(id, username, poster, datetime, finalContent, url);
			UserReport userReport = new UserReport(poster, datetime, "post", username);
			userReportDAO.save(userReport);
			if(bully != null && !bully.equals("")){
				userReport = new UserReport(poster, datetime, "bullyPost", null);
				userReportDAO.save(userReport);
				NotificationDAO notificationDAO = (NotificationDAOImpl) req.getSession(false).getAttribute("notificationDAO");
				Notification notification = new Notification("harry", poster, "bullyPost", "false", datetime);
				notificationDAO.save(notification);
				// send email to admin (placeholder email for now)				
				String email = "avril.liuwanting@gmail.com";
				String postType = "post";
				String path = "/email?emailType=bullyPost"
							+ "&email=" + email
							+ "&postType=" + postType
							+ "&poster=" + poster
							+ "&wall=" + username;
				System.out.println("you've been a bad boy. These words relate to bullying:"+bully);
				req.getRequestDispatcher(path).forward(req, resp);
			}
		}
	}
}