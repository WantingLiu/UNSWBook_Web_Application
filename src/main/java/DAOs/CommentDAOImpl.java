package DAOs;
import java.util.List;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;


public class CommentDAOImpl extends BasicDAO<User, ObjectId> implements CommentDAO {
 
	public CommentDAOImpl(Class<User> entityClass, Datastore ds) {
		super(entityClass, ds);
	}
	
	public void createComment(String username, int id, String commenter, String datetime, String content) {
		Query<User> query = createQuery().field("username").equal(username);
		Comment comment = new Comment(datetime, content, commenter);
		UpdateOperations ops = createUpdateOperations().push("wall."+(id-1)+".comments", comment);
		update(query, ops);
		
	}

}