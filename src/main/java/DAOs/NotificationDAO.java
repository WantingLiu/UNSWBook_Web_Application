package DAOs;

import java.util.ArrayList;
import java.util.List;

import org.bson.BSONObject;
import org.bson.types.ObjectId;
import org.mongodb.morphia.dao.DAO;

public interface NotificationDAO extends DAO<Notification, ObjectId>{
	public List<Notification> getNotificationsByUser(String username);
	public List<Notification> getNotificationsByFields(String[] fields, String[] values);
	public List<Notification> getNewNotificationsByUser(String username);
	public void markAllAsRead(String username);
}
