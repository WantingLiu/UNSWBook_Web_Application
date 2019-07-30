package DAOs;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity("user_reports")
public class UserReport {
	@Id
	private ObjectId objectId;
	private String username;
	private String datetime;
	private String type;
	private String username_opt;
	
	public UserReport() {}
	
	public UserReport(String username, String datetime, String type, String username_opt) {
		super();
		this.setUsername(username);
		this.setDatetime(datetime);
		this.setType(type);
		this.setUsername_opt(username_opt);
	}

	public ObjectId getObjectId() {
		return objectId;
	}
 
	public void setObjectId(ObjectId objectId) {
		this.objectId = objectId;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getDatetime() {
		return datetime;
	}

	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUsername_opt() {
		return username_opt;
	}

	public void setUsername_opt(String username_opt) {
		this.username_opt = username_opt;
	}
}
