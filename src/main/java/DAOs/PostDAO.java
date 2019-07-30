package DAOs;
import java.util.List;

import org.bson.types.ObjectId;
import org.mongodb.morphia.dao.DAO;
import org.mongodb.morphia.query.Query;


public interface PostDAO extends DAO<User, ObjectId>{
 
	public void createPost(int id, String username, String poster, String datetime, String content, String pic);
	public void likePost(int id, String username, String liker);
	public void unlikePost(int id, String username, String liker);
	
}