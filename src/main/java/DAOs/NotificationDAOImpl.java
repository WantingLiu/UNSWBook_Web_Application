package DAOs;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.bson.BSONObject;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;

import com.mongodb.DBObject;

public class NotificationDAOImpl extends BasicDAO<Notification, ObjectId> implements NotificationDAO {

	public NotificationDAOImpl(Class<Notification> entityClass, Datastore ds) {
		super(entityClass, ds);
	}


	public List<Notification> getNotificationsByUser(String username) {
		Query<Notification> query = createQuery().field("toUser").equal(username);
		return query.asList();
	}
	
	// same functionality as UserDAO's getByFields: given fields and their respective values, returns matching notifications
	public List<Notification> getNotificationsByFields(String[] fields, String[] values) {
		Pattern pattern;
		Query<Notification> query = createQuery();
		for (int i = 0; i < fields.length; i++) {
			pattern = Pattern.compile(values[i].replaceAll("\\s+", ""), Pattern.CASE_INSENSITIVE);
			query.field(fields[i]).equal(pattern);
		}
		return query.asList();
	}
	
	public List<Notification> getNewNotificationsByUser(String username) {
		Query<Notification> query = createQuery().field("toUser").equal(username).field("read").equal("false");
		return query.asList();
	}
	
	public void markAllAsRead(String username) {
		Query<Notification> query = createQuery().field("toUser").equal(username);
		UpdateOperations ops = createUpdateOperations().set("read", "true");
		update(query, ops);
	}
}
