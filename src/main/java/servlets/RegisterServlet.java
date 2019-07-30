package servlets;

import com.cloudinary.utils.ObjectUtils;
import com.mongodb.*;

import DAOs.MorphiaService;
import DAOs.User;
import DAOs.UserDAO;
import DAOs.UserDAOImpl;
import DAOs.UserReport;
import DAOs.UserReportDAO;
import DAOs.UserReportDAOImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;

import helpers.FileUpload;
import helpers.StreamToString;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;

import java.util.*;

import com.cloudinary.*;

/**
 * doPost: registers the user as unconfirmed, then forwards parameters to EmailServlet.java
 * doGet: receives registration confirmation (when user clicks on url in their email)
 */
@MultipartConfig
@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    		MorphiaService morphiaService = (MorphiaService) getServletContext().getAttribute("morphiaService");
		UserDAO userDAO = new UserDAOImpl(User.class, morphiaService.getDatastore());
		UserReportDAO userReportDAO = new UserReportDAOImpl(UserReport.class, morphiaService.getDatastore());
		String uuid = UUID.randomUUID().toString();

		// configures upload settings
		String username = null;
		String first_name = null;
		String last_name = null;
		String password = null;
		String gender = null;
		String email = null;
		String date = null;

		FileUpload fileUpload = new FileUpload();
		
		List formItems = null;
		try {
			formItems = fileUpload.getUpload().parseRequest(req);
		} catch (FileUploadException e) {
			e.printStackTrace();
		}

		Iterator iter = formItems.iterator();

		FileItem fileItem = null;

		while (iter.hasNext()) {
		FileItem item = (FileItem) iter.next();
		// processes only fields that are not form fields
			if (!item.isFormField()) {
				fileItem = item;
			} else {
                switch (item.getFieldName()){
					case "username":
						username = StreamToString.getStringFromInputStream(item.getInputStream());break;
					case "first_name":
						first_name = StreamToString.getStringFromInputStream(item.getInputStream());break;
					case "last_name":
						last_name = StreamToString.getStringFromInputStream(item.getInputStream());break;
					case "password":
						password = StreamToString.getStringFromInputStream(item.getInputStream());break;
					case "gender":
						gender = StreamToString.getStringFromInputStream(item.getInputStream());break;
					case "email":
						email = StreamToString.getStringFromInputStream(item.getInputStream());break;
					case "date":
						date = StreamToString.getStringFromInputStream(item.getInputStream());break;

                }
			}
		}

		if (userDAO.getByUsername(username) == null) {
			String url = null;
			if (fileItem.getSize() != 0){
				Cloudinary cloudinary = (Cloudinary) getServletContext().getAttribute("cloudinary");
				Map<String,Object> result = cloudinary.uploader().upload(fileItem.get(), ObjectUtils.emptyMap());

				url = (String)result.get("secure_url");
			}
			User user = new User(username, first_name, last_name, password, date, gender, email, uuid, url);
			userDAO.save(user);
			String datetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
			UserReport userReport = new UserReport(username, datetime, "register", "");
			userReportDAO.save(userReport);

			// pass parameters to EmailServlet
			String path = "/email?emailType=confirmRegistration" + 
						"&email=" + email +
						"&username=" + username +
						"&uuid=" + uuid;
			req.getRequestDispatcher(path).forward(req, resp);
		} else {
			req.setAttribute("failure", "username");
			req.getRequestDispatcher("/register.jsp").forward(req, resp);
			return;
		}
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String confirm = req.getParameter("confirm");

//		MongoClient mongoClient = new MongoClient("localhost", 27017);
		MongoClientURI uri  = new MongoClientURI(System.getenv("MONGODB_URI"));
		MongoClient mongoClient = new MongoClient(uri);
		DB mongoDatabase = mongoClient.getDB("unswbook");
		DBCollection mongoCollection = mongoDatabase.getCollection("users");
		DBCursor cursor = mongoCollection.find();

		while (cursor.hasNext()) {
			DBObject current = cursor.next();
			if (current.get("confirmed").equals(confirm)) {

				// Update
				BasicDBObject query = new BasicDBObject();
				query.put("confirmed", confirm);

				BasicDBObject newDocument = new BasicDBObject();
				newDocument.put("confirmed", "true");

				BasicDBObject updateObj = new BasicDBObject();
				updateObj.put("$set", newDocument);

				mongoCollection.update(query, updateObj);

				mongoClient.close();
				req.getRequestDispatcher("/login").forward(req, resp);
				return;

			}
		}
		mongoClient.close();

        req.getRequestDispatcher("register.jsp").forward(req,resp);
    }
}

