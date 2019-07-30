package DAOs;
import java.util.List;

import org.bson.types.ObjectId;
import org.mongodb.morphia.dao.DAO;
import org.mongodb.morphia.query.Query;


public interface CommentDAO extends DAO<User, ObjectId>{
 
	public void createComment(String username, int id, String commenter, String datetime, String content);
	
}