package DAOs;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity("notifications")
public class Notification {
	@Id
	private ObjectId objectId;
	private String toUser;
	private String fromUser;
	private String type;
	private String read;
	private String datetime;
	
	public Notification(){}
	
	public Notification(String toUser, String fromUser, String type, String read, String datetime) {
		super();
		this.setToUser(toUser);
		this.setFromUser(fromUser);
		this.setType(type);
		this.setRead(read);
		this.setDatetime(datetime);
	}

	public ObjectId getObjectId() {
		return objectId;
	}
 
	public void setObjectId(ObjectId objectId) {
		this.objectId = objectId;
	}
	
	public String getToUser() {
		return toUser;
	}

	public void setToUser(String toUser) {
		this.toUser = toUser;
	}

	public String getFromUser() {
		return fromUser;
	}

	public void setFromUser(String fromUser) {
		this.fromUser = fromUser;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getRead() {
		return read;
	}

	public void setRead(String read) {
		this.read = read;
	}

	public String getDatetime() {
		return datetime;
	}

	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}
}
