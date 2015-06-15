package issuetracking;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

public class DBManager {

	public static void main(String[] args) {
	}

	private static DBManager DBManager1;

	private static Map<Integer, Ticket> ticketsMap = new HashMap<Integer, Ticket>();
	private static Map<String, User> usersMap = new HashMap<String, User>();

	private DBManager() {
	}

	public static DBManager getInstance() {
		if (DBManager.DBManager1 == null) {
			DBManager.DBManager1 = new DBManager();
		}
		return DBManager.DBManager1;
	}

	public int getNextId() {
		int i = 1;
		for (; i < 10000; i++) {
			if (!ticketsMap.keySet().contains(i))
				break;
		}
		return i;
	}
	
	public void loadTickets() {
		ticketsMap.clear();
		try {
			// Holen
			// 1. get conn
			Connection myConn = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/issuetracking_db",
					"glassfishadmin", "chucknorris42");
			// 2. create statement
			Statement myStmt = myConn.createStatement();
			Statement myStmt2 = myConn.createStatement();
			// 3. execute sql query
			ResultSet resultBugs = myStmt
					.executeQuery("select * from tickets, ticket_bugs WHERE "
							+ "tickets.id = ticket_bugs.id "
							+ "order by tickets.id;");
			ResultSet resultFeatures = myStmt2
					.executeQuery("select * from tickets, ticket_features WHERE "
							+ "tickets.id = ticket_features.id "
							+ "order by tickets.id;"
							);
			// 4. Process results
			while (resultBugs.next()) {
				TicketBug t1 = new TicketBug();
				t1.setId(resultBugs.getInt("id"));
				t1.setTitle(resultBugs.getString("title"));
				t1.setDescription(resultBugs.getString("description"));
				Date aDate = resultBugs.getDate("creation_date");
				t1.setDate(aDate);
				t1.setAuthor(resultBugs.getString("author"));
				t1.setResponsible_user(resultBugs.getString("responsible_user"));
				t1.setType(resultBugs.getString("type"));
				t1.setState(resultBugs.getString("state"));
				ticketsMap.put(t1.getId(), t1);
			}
			while (resultFeatures.next()) {
				TicketFeature t1 = new TicketFeature();
				t1.setId(resultFeatures.getInt("id"));
				t1.setTitle(resultFeatures.getString("title"));
				t1.setDescription(resultFeatures.getString("description"));
				Date aDate = resultFeatures.getDate("creation_date");
				t1.setDate(aDate);
				t1.setAuthor(resultFeatures.getString("author"));
				t1.setResponsible_user(resultFeatures
						.getString("responsible_user"));
				t1.setType(resultFeatures.getString("type"));
				t1.setState(resultFeatures.getString("state"));

				t1.setEstimated_time(resultFeatures.getString("estimated_time"));
				ticketsMap.put(t1.getId(), t1);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	};
	
	public List<Ticket> getTickets() {
		loadTickets();
		List<Ticket> tickets = new LinkedList<Ticket>(ticketsMap.values());
		return tickets;
	}

	public Ticket getTicketById(int i) {
		loadTickets();
		return ticketsMap.get(i);
	}
	
	public List<Ticket> getTicketsByState(String state) {
		loadTickets();
		List<Ticket> tickets = new LinkedList<Ticket>();
		for(Ticket t : ticketsMap.values()){
			if(t.getState().equals(state)){
				tickets.add(t);
			}
		}
		return tickets;
	}

	public void saveTicket(Ticket t1) {
		try {
			// Einfügen
			// 1. get conn
			Connection myConn = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/issuetracking_db",
					"glassfishadmin", "chucknorris42");
			// 2. create statement
			Statement myStmt = myConn.createStatement();
			// 3. Execute SQL query
			
			
			String sql = "insert into tickets "
					+ " (id, title, description, creation_date, author, responsible_user, type, state)"
					+ " values("+ t1.getId()+ ", "
					+ "'"+ t1.getTitle()+ "', "
					+ "'"+ t1.getDescription()+ "' ,"
					+ "'"+ t1.getDateAsString()+ "', "
					+ "'"+ t1.getAuthor()+ "' ,"
					+ "'"+ t1.getResponsible_user()+ "', "
					+ "'"+ t1.getType()+ "' ," + "'" + t1.getState() + "' " + ");";
			myStmt.executeUpdate(sql);

			if (t1 instanceof TicketBug) {
				TicketBug tbug = (TicketBug) t1;

				// 2. create statement
				Statement myStmt2 = myConn.createStatement();
				String sql2 = "insert into ticket_bugs " + " (id)" + " values("
						+ tbug.getId() + " " + ");";

				myStmt2.executeUpdate(sql2);
			}

			if (t1 instanceof TicketFeature) {
				TicketFeature tfeature = (TicketFeature) t1;

				// 2. create statement
				Statement myStmt2 = myConn.createStatement();
				String sql2 = "insert into ticket_features "
						+ " (id, estimated_time)" + " values("
						+ tfeature.getId() + ", " + "'" + tfeature.getEstimated_time()
						+ "' " + ");";

				myStmt2.executeUpdate(sql2);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		loadTickets();
	}

	public void updateTicket(Ticket tupdate) {
		loadTickets();
		Ticket t1 = DBManager1.getTicketById(tupdate.getId());

		deleteTicket(t1);
		saveTicket(tupdate);
		loadTickets();

	}

	public void deleteTicket(Ticket t1) {
		try {
			// Löschen
			// 1. get conn
			Connection myConn = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/issuetracking_db",
					"glassfishadmin", "chucknorris42");
			// 2. create statement
			Statement myStmt = myConn.createStatement();
			// 3. Execute SQL query
			String sql = "delete from tickets " + "where id=" + t1.getId()
					+ ";";

			myStmt.executeUpdate(sql);

			if (t1.getType().equals("bug"))
				deleteBugPart(t1);
			if (t1.getType().equals("feature"))
				deleteFeaturePart(t1);

		} catch (Exception e) {
			e.printStackTrace();
		}
		loadTickets();
	}

	public void deleteFeaturePart(Ticket t1) {
		try {
			// Löschen
			// 1. get conn
			Connection myConn = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/issuetracking_db",
					"glassfishadmin", "chucknorris42");
			// 2. create statement
			Statement myStmt = myConn.createStatement();
			// 3. Execute SQL query
			String sql = "delete from ticket_features " + "where id="
					+ t1.getId() + ";";

			myStmt.executeUpdate(sql);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void deleteBugPart(Ticket t1) {
		try {
			// Löschen
			// 1. get conn
			Connection myConn = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/issuetracking_db",
					"glassfishadmin", "chucknorris42");
			// 2. create statement
			Statement myStmt = myConn.createStatement();
			// 3. Execute SQL query
			String sql = "delete from ticket_bugs " + "where id=" + t1.getId()
					+ ";";

			myStmt.executeUpdate(sql);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void loadUsers() {
		usersMap.clear();

		try {
			// Holen
			// 1. get conn
			Connection myConn = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/issuetracking_db",
					"glassfishadmin", "chucknorris42");
			// 2. create statement
			Statement myStmt = myConn.createStatement();
			// 3. execute sql query
			ResultSet myRs = myStmt.executeQuery("select * from users order by userid");
			// 4. Process results
			while (myRs.next()) {
				User u1 = new User();
				u1.setUserid(myRs.getString("userid"));
				u1.setPassword(myRs.getString("password"));
				usersMap.put(u1.getUserid(), u1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	};


	public List<User> getUsers() {
		loadUsers();
		List<User> users = new LinkedList<User>(usersMap.values());
		return users;
	}

	public User getUserByUserid(String userid) {
		loadUsers();
		return usersMap.get(userid);
	}
	
	public void registerUser(String userid, String password) {
		try {
			// Einfügen
			// 1. get conn
			Connection myConn = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/issuetracking_db",
					"glassfishadmin", "chucknorris42");
			// 2. create statement
			Statement myStmt = myConn.createStatement();
			// 3. Execute SQL query
			String sql = "insert into users " + " (userid, password)"
					+ " values('" + userid + "', '" + password + "');";

			myStmt.executeUpdate(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		loadUsers();
	}

	public void updateUser(User u1) {
		try {
			// Updaten
			// 1. get conn
			Connection myConn = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/issuetracking_db",
					"glassfishadmin", "chucknorris42");
			// 2. create statement
			Statement myStmt = myConn.createStatement();
			// 3. Execute SQL query
			String sql = "update users " + "set password='" + u1.getPassword()
					+ "' " + "where userid='" + u1.getUserid() + "';";

			myStmt.executeUpdate(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		loadUsers();
	}

	public boolean containsUser(String userid) {
		loadUsers();
		try {
			// Holen
			// 1. get conn
			Connection myConn = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/issuetracking_db",
					"glassfishadmin", "chucknorris42");
			// 2. create statement
			Statement myStmt = myConn.createStatement();
			// 3. execute sql query
			ResultSet myRs = myStmt
					.executeQuery("select * from users where userid = '"
							+ userid + "' ;");

			if (myRs.next()) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	};

	public void deleteUser(User u1) {
		try {
			// Löschen
			// 1. get conn
			Connection myConn = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/issuetracking_db",
					"glassfishadmin", "chucknorris42");
			// 2. create statement
			Statement myStmt = myConn.createStatement();
			// 3. Execute SQL query
			String sql = "delete from users " + "where userid = '"
					+ u1.getUserid() + "' ;";

			myStmt.executeUpdate(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		loadUsers();
	}
	
	public boolean checkLogin(String userid, String password) {
		try {
			// Holen
			// 1. get conn
			Connection myConn = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/issuetracking_db",
					"glassfishadmin", "chucknorris42");
			// 2. create statement
			Statement myStmt = myConn.createStatement();
			// 3. execute sql query
			ResultSet myRs = myStmt
					.executeQuery("select password from users where userid = '"
							+ userid + "';");
			String s1;
			if (myRs.next()) {
				s1 = myRs.getString(1);
				if (s1.equals(password))
					return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	};


}