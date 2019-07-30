package DAOs;

import java.util.List;
import java.util.regex.Pattern;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Query;

public class UserReportDAOImpl extends BasicDAO<UserReport, ObjectId> implements UserReportDAO {

	public UserReportDAOImpl(Class<UserReport> entityClass, Datastore ds) {
		super(entityClass, ds);
	}

	@Override
	public List<UserReport> getAllHistories(String username) {
		Query<UserReport> query = createQuery().field("username").equal(username);
		return query.asList();
	}

	@Override
	public List<UserReport> getHistoriesByType(String username, String[] types, String[] values) {
		Query<UserReport> query = createQuery();
		for (int i = 0; i < types.length; i++) {
			query.field(types[i]).equal(values[i]);
		}
		return query.asList();
	}

}
