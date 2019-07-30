package DAOs;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import com.sun.org.apache.xerces.internal.impl.xpath.regex.Match;
import jdk.nashorn.internal.runtime.regexp.joni.Regex;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.mapping.Mapper;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.mongodb.morphia.query.UpdateOpsImpl;

public class UserDAOImpl extends BasicDAO<User, ObjectId> implements UserDAO {

	public UserDAOImpl(Class<User> entityClass, Datastore ds) {
		super(entityClass, ds);
	}

	public User getByUsername(String username) {
		Query<User> query = createQuery().field("username").equal(username);
		return query.get();
	}

	public List<User> getAllUsers() {
		Query<User> query = createQuery();
		return query.asList();
	}

	public List<User> getByFirstName(String firstname) {
		Pattern pattern = Pattern.compile(firstname, Pattern.CASE_INSENSITIVE);
		Query<User> query = createQuery().field("firstName").equal(pattern);
		return query.asList();
	}

	public List<User> getByLastName(String lastname) {
		Pattern pattern = Pattern.compile(lastname, Pattern.CASE_INSENSITIVE);
		Query<User> query = createQuery().field("lastName").equal(pattern);
		return query.asList();
	}

	public List<User> getByGender(String gender) {
		Query<User> query = createQuery().field("gender").equal(gender);
		return query.asList();
	}

	public List<User> getByFields(String[] fields, ArrayList<String> values) {
		Pattern pattern;
		Query<User> query = createQuery();
		for (int i = 0; i < fields.length; i++) {
			pattern = Pattern.compile(values.get(i).replaceAll("\\s+", ""), Pattern.CASE_INSENSITIVE);
			query.field(fields[i]).equal(pattern);
		}
		return query.asList();
	}

	public void AddFriend(String username, String friend) {
		Query<User> query = createQuery().field("username").equal(username);
		UpdateOperations ops = createUpdateOperations().push("friends", friend);
		update(query, ops);
	}
	
	public void DeleteFriend(String username, String friend) {
		Query<User> query = createQuery().field("username").equal(username);
		UpdateOperations ops = createUpdateOperations().removeAll("friends", friend);
		update(query, ops);
	}
	

	public void updateFields(String username, ArrayList<String> values) {
		UpdateOpsImpl updateOps = new UpdateOpsImpl(User.class, new Mapper());
		String[] fields = new String[] { "password", "firstName", "lastName", "birthDate", "gender", "email" , "pic"};
		Query<User> query = createQuery().field("username").equal(username);
		for (int i = 0; i < fields.length; i++) {
			if (!values.get(i).equals("")) {
				updateOps.set(fields[i], values.get(i));
				update(query, updateOps);
			}
		}
	}

	public void banUser(String username) {
		Query<User> query = createQuery().field("username").equal(username);
		UpdateOperations ops = createUpdateOperations().set("banned", true);
		update(query, ops);
	}

	public void unbanUser(String username) {
		Query<User> query = createQuery().field("username").equal(username);
		UpdateOperations ops = createUpdateOperations().set("banned", false);
		update(query, ops);
	}

}