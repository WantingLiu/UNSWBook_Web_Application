package DAOs;
import java.util.List;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;


public class PostDAOImpl extends BasicDAO<User, ObjectId> implements PostDAO {
 
	public PostDAOImpl(Class<User> entityClass, Datastore ds) {
		super(entityClass, ds);
	}
	
	public void createPost(int id, String username, String poster, String datetime, String content, String pic) {
		Query<User> query = createQuery().field("username").equal(username);		
		Post post = new Post(poster, datetime, content, id, pic);
		UpdateOperations ops = createUpdateOperations().push("wall", post);
		update(query, ops);
	}
	
	public void likePost(int id, String username, String liker) {
		Query<User> query = createQuery().field("username").equal(username);
		UpdateOperations ops = createUpdateOperations().push("wall."+(id-1)+".likes", liker);
		update(query, ops);
	}
	
	public void unlikePost(int id, String username, String liker) {
		Query<User> query = createQuery().field("username").equal(username);
		UpdateOperations ops = createUpdateOperations().removeAll("wall."+(id-1)+".likes", liker);
		update(query, ops);
	}
	
}