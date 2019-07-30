package DAOs;

import java.util.List;

import org.bson.types.ObjectId;
import org.mongodb.morphia.dao.DAO;

public interface UserReportDAO extends DAO<UserReport, ObjectId>{
	public List<UserReport> getAllHistories(String username);
	public List<UserReport> getHistoriesByType(String username, String[] types, String[] values);
}
