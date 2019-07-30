package helpers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import DAOs.Notification;
import DAOs.NotificationDAO;
import DAOs.User;
import DAOs.UserDAO;
import DAOs.UserReport;

public class Search {

	public Search() {
		super();
	}

	public static List<User> search(String search, UserDAO userDAO) {
		List<User> tempUsers = new ArrayList<User>();
		List<User> users = new ArrayList<User>();
		if (search == "") {
			return null;
		} else {
			String[] _values = search.split(" ");
			int num = _values.length;
			switch (num) {
			case 1:
				tempUsers = userDAO.getByFirstName(search);
				tempUsers.addAll(userDAO.getByLastName(search));
				break;
			case 2:
				ArrayList<String> values = new ArrayList<String>();
				for (String value : _values) {
					values.add(value);
				}
				String[] fields = new String[] { "firstName", "lastName" };
				tempUsers = userDAO.getByFields(fields, values);
				break;
			default:
				tempUsers = null;
				break;
			}

		}
		users = extractUser(tempUsers);
		return users;
	}

	public static List<User> advanceSearch(ArrayList<String> values, UserDAO userDAO) {
		List<User> tempUsers = new ArrayList<User>();
		List<User> users = new ArrayList<User>();

		String[] fields = new String[] { "firstName", "lastName", "gender", "birthDate" };

		tempUsers = userDAO.getByFields(fields, values);
		users = extractUser(tempUsers);
		return users;
	}

	public static List<User> searchAllUser(UserDAO userDAO) {
		List<User> allUsers = new ArrayList<User>();
		List<User> users = new ArrayList<User>();
		allUsers = userDAO.getAllUsers();
		users = extractUser(allUsers);
		return users;
	}

	private static List<User> extractUser(List<User> inputUser) {
		List<User> users = new ArrayList<User>();
		if (inputUser != null) {
			for (User u : inputUser) {
				if (u.getAdmin().equals("false")) {
					users.add(u);
				}
			}
		}
		return users;
	}

	public static List<String> search_users_requests_sent(String username, List<User> users, NotificationDAO notificationDAO) {
		List<String> users_requests_sent = new ArrayList<String>();
		String fields[] = { "fromUser", "type" };
		String values[] = { username, "friendReq" };
		List<Notification> currentUserSentFriendReqs = notificationDAO.getNotificationsByFields(fields, values);
		if (users != null) {
			for (User u : users) {
				for (Notification n : currentUserSentFriendReqs) {
					if (n.getToUser().equals(u.getUsername())) {
						users_requests_sent.add(u.getUsername());
					}
				}
			}
		}
		return users_requests_sent;
	}

	public static List<UserReport> userReportsFilter(List<UserReport> allHistories, ArrayList<String> values)
			throws ParseException {
		List<UserReport> result = new ArrayList<UserReport>();
		String type = values.get(0);
		String startDate = values.get(1);
		String endDate = values.get(2);
		SimpleDateFormat sdfs = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
		SimpleDateFormat sdfe = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
		SimpleDateFormat sdff = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date start = null;
		Date end = null;
		if (!startDate.equals("")) {
			startDate += " 00:00:00" ;
			start = sdfs.parse(startDate);
		}
		
		if (!endDate.equals("")) {
			endDate += " 23:59:59";
			end = sdfe.parse(endDate);
		}

		if (allHistories != null) {
			for (UserReport u : allHistories) {
				Date dateTime = sdff.parse(u.getDatetime());
				System.out.println("this" + u.getDatetime());
				System.out.println("this" + dateTime);
				if (type.equals("all")) {
					if (isInBetween(start, end, dateTime))
						result.add(u);

				} else if (u.getType().equals(type)) {
					if (isInBetween(start, end, dateTime))
						result.add(u);
				}

			}
		}
		return result;

	}

	private static boolean isInBetween(Date start, Date end, Date dateTime) {
		boolean result = true;
		if(start == null && end == null)return result;
		if (start != null && (dateTime.before(start))) {
			result = false;
		} else if (end != null && (dateTime.after(end))) {
			result = false;
		}
		return result;
	}
}
