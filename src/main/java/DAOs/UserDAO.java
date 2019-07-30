package DAOs;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.bson.types.ObjectId;
import org.mongodb.morphia.dao.DAO;
import org.mongodb.morphia.query.Query;


public interface UserDAO extends DAO<User, ObjectId>{
 
	public User getByUsername(String username);
	public List<User> getAllUsers();
	public List<User> getByFirstName(String firstname);
	public List<User> getByLastName(String lastname);
	public List<User> getByGender(String gender);
	public List<User> getByFields(String[] fields, ArrayList<String> values);
	public void AddFriend(String username, String friend);
	public void DeleteFriend(String username, String friend);
	public void updateFields(String username, ArrayList<String> values);
	public void banUser(String username);
	public void unbanUser(String username);
}