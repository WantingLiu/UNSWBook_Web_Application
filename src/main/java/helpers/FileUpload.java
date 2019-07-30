package helpers;

import java.io.File;

import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class FileUpload {

	private static final String UPLOAD_DIRECTORY = "/Users/harryhuang/Documents/unswbook_uploads/profile_pics";
	private static final int THRESHOLD_SIZE = 1024 * 1024 * 3; // 3MB
	private static final int MAX_FILE_SIZE = 1024 * 1024 * 40; // 40MB
	private static final int MAX_REQUEST_SIZE = 1024 * 1024 * 50; // 50MB
	private DiskFileItemFactory factory;
	private ServletFileUpload upload;
	
	public FileUpload() {	
		setFactory();
		setUpload();
	}

	public DiskFileItemFactory getFactory() {
		return factory;
	}

	private void setFactory() {
		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setSizeThreshold(THRESHOLD_SIZE);
		factory.setRepository(new File(System.getProperty("java.io.tmpdir")));
		this.factory = factory;
	}

	public ServletFileUpload getUpload() {
		return upload;
	}

	private void setUpload() {
		ServletFileUpload upload = new ServletFileUpload(this.factory);
		upload.setFileSizeMax(MAX_FILE_SIZE);
		upload.setSizeMax(MAX_REQUEST_SIZE);
		this.upload = upload;
	}
	

	
}
