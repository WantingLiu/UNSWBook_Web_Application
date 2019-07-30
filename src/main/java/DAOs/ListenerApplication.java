package DAOs;


import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.apache.commons.io.IOUtils;

public class ListenerApplication implements ServletContextListener {

	public void contextInitialized(ServletContextEvent servletContextEvent) {
		MorphiaService morphiaService = new MorphiaService();
		Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
				"cloud_name","unswbook",
				"api_key", "136521779173293",
				"api_secret","Idcffiof5KJsj9t3DTU-vuGYd_E"
		));

		try {
			String bullyWords = IOUtils.toString(new URL( "http://res.cloudinary.com/unswbook/raw/upload/v1508211382/bullyWords_g3ailf.txt" ).openStream());
			servletContextEvent.getServletContext().setAttribute("bullyWords", bullyWords);
		} catch (IOException e) {
			e.printStackTrace();
		}
		servletContextEvent.getServletContext().setAttribute("morphiaService", morphiaService);
		servletContextEvent.getServletContext().setAttribute("cloudinary", cloudinary);

	}

	public void contextDestroyed(ServletContextEvent servletContextEvent) {

	}
}
