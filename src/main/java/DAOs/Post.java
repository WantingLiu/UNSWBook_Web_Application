package DAOs;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

public class Post {
	private int id;
	private String poster;
	private String datetime;
	private String content;
	private List<String> likes;
	private List<Comment> comments;
	private String pic = null;

	public Post(){}


	public Post(String poster, String datetime, String content, int id, String pic) {
		super();
		this.id = id;
		this.poster = poster;
		this.datetime = datetime;
		this.content = content;
		this.likes = new ArrayList<String>();
		this.comments = new ArrayList<Comment>();
		this.pic = pic;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getPoster() {
		return poster;
	}

	public void setPoster(String poster) {
		this.poster = poster;
	}
	
	public String getDatetime() {
		return datetime;
	}

	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}
	
	public String getContent() {
		return content;
	}
 
	public void setContent(String content) {
		this.content = content;
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

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public boolean alreadyLiked(String username) {
		if (likes != null) {
			return likes.contains(username);
		} else {
			return false;
		}
	}

	public static Comparator<Post> DateTimeComparator = new Comparator<Post>() {
		public int compare(Post p1, Post p2) {
			String date1 = p1.getDatetime();
			String date2 = p2.getDatetime();
			return date2.compareTo(date1);
		}
	};
	
	public static Comparator<Pair<Post, String>> DateTimeComparatorPair = new Comparator<Pair<Post, String>>() {
		public int compare(Pair<Post, String> o1, Pair<Post, String> o2) {
			String date1 = o1.getLeft().getDatetime();
			String date2 = o2.getLeft().getDatetime();
			return date2.compareTo(date1);
		}
	};
}

