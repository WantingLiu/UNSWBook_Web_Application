package DAOs;
import java.util.ArrayList;

public class Comment {
	private String datetime;
	private String content;
	private String commenter;
	
	public Comment(){}
	
	public Comment(String datetime, String content, String commenter) {
		super();
		this.datetime = datetime;
		this.content = content;
		this.commenter = commenter;
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

	public String getCommenter() {
		return commenter;
	}
	public void setCommenter(String commenter) {
		this.commenter = commenter;
	}
}