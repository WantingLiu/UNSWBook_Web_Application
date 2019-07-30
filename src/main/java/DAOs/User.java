package DAOs;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
 
@Entity("users")
public class User {
 
	@Id
	private ObjectId objectId;
	private String username;
	private String firstName;
	private String lastName;
	private String password;
	private String birthDate;
	private String gender;
	private String email;
	private String confirmed;
	private String admin;
	private List<Post> wall;
	private List<String> likes;
	private List<Comment> comments;
	private List<String> friends;
	private Boolean banned;
	private String pic = "https://pbs.twimg.com/profile_images/898295311893880832/bCps4HFV_400x400.jpg";

	/**
	 * keep an empty constructor so that morphia 
	 * can recreate this entity when you want to 
	 * fetch it from the database
	 */
	public User(){}
	
	
	/**
	 * full constructor 
	 * (without objectId, we let morphia generate this one for us) 
	 * 
	 * @param username
	 * @param firstName
	 * @param lastName
	 * @param password
	 * @param date
	 * @param gender
	 * @param email
	 * @param confirmed
	 */
	public User(String username, String firstName, String lastName, String password, String date, String gender, String email, String confirmed, String pic) {
		super();
		this.username = username;
		this.firstName = firstName;
		this.lastName = lastName;
		this.password = password;
		this.birthDate = date;
		this.gender = gender;
		this.email = email;
		this.confirmed = confirmed;
		this.setAdmin("false");
		this.wall = new ArrayList<Post>();
		this.setLikes(new ArrayList<String>());
		this.setComments(new ArrayList<Comment>());
		this.friends = new ArrayList<String>();
		this.banned = false;
		this.pic = pic;
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
	
	public String getFirstName() {
		return firstName;
	}
 
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
 
	public String getLastName() {
		return lastName;
	}
 
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPassword() {
		return password;
	}
 
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getBirthDate() {
		return birthDate;
	}
 
	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}
 
	public String getGender() {
		return gender;
	}
 
	public void setGender(String gender) {
		this.gender = gender;
	}
	
	public String getEmail() {
		return email;
	}
 
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getConfirmed() {
		return confirmed;
	}
 
	public void setConfirmed(String confirmed) {
		this.confirmed = confirmed;
	}
	
	public List<Post> getAllPosts() {
		if (wall != null) {
			return wall;
		} else {
			return Collections.emptyList();
		}
	}
 
	public void addPost(Post post) {
		this.wall.add(post);
	}

	public List<String> getFriends() {
		if (friends != null) {
			return friends;
		} else {
			return Collections.emptyList();
		}
	}

	public void setFriends(List<String> friends) {
		this.friends = friends;
	}


	public List<String> getLikes() {
		return likes;
	}


	public void setLikes(List<String> likes) {
		this.likes = likes;
	}


	public List<Comment> getComments() {
		return comments;
	}


	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}


	public String getAdmin() {
		return admin;
	}


	public void setAdmin(String admin) {
		this.admin = admin;
	}


	public Boolean getBanned() {
		if (banned != null) {
			return banned;
		} else {
			return false;
		}
	}


	public void setBanned(Boolean banned) {
		this.banned = banned;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}
}