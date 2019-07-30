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
import com.google.gson.Gson;

import helpers.ExtractAPI;
import helpers.FileUpload;
import helpers.StreamToString;
import helpers.URLtoLink;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.lang3.tuple.*;

@MultipartConfig
@WebServlet("/home")
public class HomeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	req.getRequestDispatcher("/home.jsp").forward(req,resp);
    }
	
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		boolean ajax = "XMLHttpRequest".equals(req.getHeader("X-Requested-With"));
		if (ajax) {
			String username = (String) req.getSession(false).getAttribute("username");
			UserDAO userDAO = (UserDAO) req.getSession(false).getAttribute("userDAO");
			User current_user = userDAO.getByUsername(username);
			List<String> friends = current_user.getFriends();
			List<Pair<Post, String>> posts = new ArrayList<Pair<Post, String>>();
			for (String friend : friends) {
				for (Post post : userDAO.getByUsername(friend).getAllPosts()) {
					Pair<Post, String> pair = Pair.of(post, friend);
					posts.add(pair);
				}
			}
			posts.sort(Post.DateTimeComparatorPair);
			Gson gson = new Gson();
			String json = gson.toJson(posts);
			resp.getWriter().write(json);
		} else {
    		FileUpload fileUpload = new FileUpload();
    		List formItems = null;
			try {
				formItems = fileUpload.getUpload().parseRequest(req);
			} catch (FileUploadException e) {
				e.printStackTrace();
			}

			String content = null;
			Iterator iter = formItems.iterator();

			FileItem fileItem = null;

			while (iter.hasNext()) {
				FileItem item = (FileItem) iter.next();
				if (!item.isFormField()) {
					fileItem = item;
				} else {
					if (item.getFieldName().equals("content")){
						content = StreamToString.getStringFromInputStream(item.getInputStream());
					}
				}
			}
			String url = null;
			if (fileItem.getSize() != 0){
				Cloudinary cloudinary = (Cloudinary) getServletContext().getAttribute("cloudinary");
				Map<String,Object> result = cloudinary.uploader().upload(fileItem.get(), ObjectUtils.emptyMap());
	
				url = (String)result.get("secure_url");
			}
	
			String poster = (String) req.getSession(false).getAttribute("username");
			UserDAO userDAO = (UserDAO) req.getSession(false).getAttribute("userDAO");
			PostDAO postDAO = (PostDAO) req.getSession(false).getAttribute("postDAO");
			UserReportDAO userReportDAO = (UserReportDAOImpl) req.getSession(false).getAttribute("userReportDAO");

			String datetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
			String finalContent = URLtoLink.urlToLink(content);

			User current_user = userDAO.getByUsername(poster);
			int id = current_user.getAllPosts().size() + 1;
			String bullyWords= (String) getServletContext().getAttribute("bullyWords");
			String bully = ExtractAPI.extractBully(bullyWords,content);
			System.out.println("bully:"+bully);
			postDAO.createPost(id, poster, poster, datetime, finalContent, url);
			UserReport userReport = new UserReport(poster, datetime, "post", poster);
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
							+ "&wall=" + poster;
				System.out.println("you've been a bad boy. These words relate to bullying:"+bully);
				req.getRequestDispatcher(path).forward(req, resp);
			}
			//req.getRequestDispatcher("home").forward(req,resp);
    	}
	}

}
