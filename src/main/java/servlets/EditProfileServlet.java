package servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import helpers.FileUpload;
import helpers.StreamToString;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;

import DAOs.User;
import DAOs.UserDAO;

@MultipartConfig
@WebServlet("/edit_profile")
public class EditProfileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public EditProfileServlet() {
		super();
	}

	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.getRequestDispatcher("/edit_profile.jsp").forward(req, resp);
		
	}
	
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		FileUpload fileUpload = new FileUpload();	
		List formItems = null;
		try {
			formItems = fileUpload.getUpload().parseRequest(req);
		} catch (FileUploadException e) {
			e.printStackTrace();
		}

		Iterator iter = formItems.iterator();
		FileItem fileItem = null;
		String[] form_fields = new String[6];
		while (iter.hasNext()) {
			FileItem item = (FileItem) iter.next();
			// processes only fields that are not form fields
			if (!item.isFormField()) {
				fileItem = item;
			} else {
				switch (item.getFieldName()){
					case "first_name":
						form_fields[1]=StreamToString.getStringFromInputStream(item.getInputStream());break;
					case "last_name":
						form_fields[2]=StreamToString.getStringFromInputStream(item.getInputStream());break;
					case "password":
						form_fields[0]=StreamToString.getStringFromInputStream(item.getInputStream());break;
					case "gender":
						form_fields[4]=StreamToString.getStringFromInputStream(item.getInputStream());break;
					case "email":
						form_fields[5]=StreamToString.getStringFromInputStream(item.getInputStream());break;
					case "date":
						form_fields[3]=StreamToString.getStringFromInputStream(item.getInputStream());break;
				}
			}
		}

		// Checks valid filesize for upload
		String url = "";
		if (fileItem.getSize() != 0){
			Cloudinary cloudinary = (Cloudinary) getServletContext().getAttribute("cloudinary");
			Map<String,Object> result = cloudinary.uploader().upload(fileItem.get(), ObjectUtils.emptyMap());
			url = (String)result.get("secure_url");
		}
		UserDAO userDAO = (UserDAO) req.getSession(false).getAttribute("userDAO");		
		String username = (String) req.getSession(false).getAttribute("username");	
		ArrayList<String> values = new ArrayList<String>();
		for (int i = 0; i < form_fields.length; i++) {
			String value = form_fields[i];
			if (value == null) {
				value = "";
			}
			values.add(value);
		}
		values.add(url);
		userDAO.updateFields(username, values);

		User current_user = userDAO.getByUsername((String) req.getSession(false).getAttribute("username"));
		req.setAttribute("user", current_user);
		resp.sendRedirect("profile");

	}

}
