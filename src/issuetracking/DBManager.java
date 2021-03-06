package issuetracking;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DBManager {

	public static void main(String[] args) {
	}

	private static DBManager DBManager1;

	private static Map<Integer, Ticket> ticketsMap = new HashMap<Integer, Ticket>();
	private static Map<String, User> usersMap = new HashMap<String, User>();
	private static Map<String, Component> componentsMap = new HashMap<String, Component>();
	private static Map<String, List<Integer>> tcRelationMap = new HashMap<String, List<Integer>>();
	private static Map<Integer, Comment> commentsMap = new HashMap<Integer, Comment>();
	private static Map<Integer, Sprint> sprintsMap = new HashMap<Integer, Sprint>(); 

	private DBManager() {
	}

	public static DBManager getInstance() {
		if (DBManager.DBManager1 == null) {
			DBManager.DBManager1 = new DBManager();
		}
		return DBManager.DBManager1;
	}
	
/////////////////////////////////////////// ID Methods ////////////////////////////////////////////////////

	public int getNextTicketId() {
		loadTickets();
		int i = 1;
		for (; i < 10000; i++) {
			if (!ticketsMap.keySet().contains(i))
				break;
		}
		return i;
	}
	
	public int getNextSprintId() {
		loadSprints();
		int i = 1;
		for (; i < 10000; i++) {
			if (!sprintsMap.keySet().contains(i))
				break;
		}
		return i;
	}
	
	public int getNextCommentId() {
		loadComments();
		int i = 1;
		for (; i < 10000; i++) {
			if (!commentsMap.keySet().contains(i))
				break;
		}
		return i;
	}

/////////////////////////////////////////// Ticket Methods ////////////////////////////////////////////////////
	
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
				TicketBug t1 = new TicketBug(resultBugs.getInt("id"), resultBugs.getInt("sprintid"),resultBugs.getString("title")
						, resultBugs.getString("description"),  resultBugs.getDate("creation_date"), resultBugs.getString("author")
						, resultBugs.getString("responsible_user"),resultBugs.getString("type") ,resultBugs.getString("state")
						);
				ticketsMap.put(t1.getId(), t1);
			}
			while (resultFeatures.next()) {
			
			
				TicketFeature t1 = new TicketFeature(resultFeatures.getInt("id"), resultFeatures.getInt("sprintid"),resultFeatures.getString("title")
						, resultFeatures.getString("description"),  resultFeatures.getDate("creation_date"), resultFeatures.getString("author")
						, resultFeatures.getString("responsible_user"),resultFeatures.getString("type") ,resultFeatures.getString("state")
						, resultFeatures.getString("estimated_time")
						);
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
	
	public List<Ticket> getTicketsByState(String state, int sprintid) {
		loadTickets();
		List<Ticket> ticketsBySprintid = new LinkedList<Ticket>();
		List<Ticket> ticketsByBoth = new LinkedList<Ticket>();
		
		//when sprintid is -2, return tickets of all sprints (for alltickets.jsp)
		if(sprintid!=-2){
		for(Ticket t : ticketsMap.values()){
			if(t.getSprintid()==sprintid){
				ticketsBySprintid.add(t);
			}
		}}
		else 
		for(Ticket t : ticketsMap.values()){
			ticketsBySprintid.add(t);
		}
		
		
		
		if(!state.equals("beliebig")){
		for(Ticket t : ticketsBySprintid){
			if(t.getState().equals(state)){
				ticketsByBoth.add(t);
			}}
		}
		else 
			for(Ticket t : ticketsBySprintid){
				ticketsByBoth.add(t);
			}
		
		
		
		return ticketsByBoth;
	}

	public void saveTicket(Ticket t1) {
		try {
			// Einf�gen
			// 1. get conn
			Connection myConn = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/issuetracking_db",
					"glassfishadmin", "chucknorris42");
			// 2. create statement
			Statement myStmt = myConn.createStatement();
			// 3. Execute SQL query
			
			
			String sql = "insert into tickets "
					+ " (id, sprintid, title, description, creation_date, author, responsible_user, type, state)"
					+ " values("+ t1.getId()+ ", "
					+ "'"+ t1.getSprintid()+ "', "
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
			// L�schen
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
			// L�schen
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
			// L�schen
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

/////////////////////////////////////////// User Methods ////////////////////////////////////////////////////

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
				User u1 = new User(myRs.getString("userid"),myRs.getString("password"));
				
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
			// Einf�gen
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
			// L�schen
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

/////////////////////////////////////////// Component Methods ////////////////////////////////////////////////////

	public void loadComponents(){
		componentsMap.clear();
		try {
			// Holen
			// 1. get conn
			Connection myConn = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/issuetracking_db",
					"glassfishadmin", "chucknorris42");
			// 2. create statement
			Statement myStmt = myConn.createStatement();
			// 3. execute sql query
			ResultSet myRs = myStmt.executeQuery("select * from components order by compid");
			// 4. Process results
			while (myRs.next()) {
				Component c1 = new Component(myRs.getString("compid"), myRs.getString("description"));
				
				componentsMap.put(c1.getCompid(), c1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public List<Component> getComponents(){
		loadComponents();
		List<Component> components = new LinkedList<Component>(componentsMap.values());
		return components;
	}
	
	public Component getComponentById(String compid){
		loadComponents();
		return componentsMap.get(compid);
	}
	
	public void saveComponent(String compid, String description){
		try {
			// Einf�gen
			// 1. get conn
			Connection myConn = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/issuetracking_db",
					"glassfishadmin", "chucknorris42");
			// 2. create statement
			Statement myStmt = myConn.createStatement();
			// 3. Execute SQL query
			String sql = "insert into components " + " (compid, description)"
					+ " values('" + compid + "', '" + description + "');";

			myStmt.executeUpdate(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		loadComponents();
	}
	
	public void updateComponent(Component c){
		try {
			// Updaten
			// 1. get conn
			Connection myConn = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/issuetracking_db",
					"glassfishadmin", "chucknorris42");
			// 2. create statement
			Statement myStmt = myConn.createStatement();
			// 3. Execute SQL query
			String sql = "update components " + "set description='" + c.getDescription()
					+ "' " + "where compid='" + c.getCompid() + "';";

			myStmt.executeUpdate(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		loadComponents();
	}
	
	public void deleteComponent(Component c){
		try {
			// L�schen
			// 1. get conn
			Connection myConn = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/issuetracking_db",
					"glassfishadmin", "chucknorris42");
			// 2. create statement
			Statement myStmt = myConn.createStatement();
			// 3. Execute SQL query
			String sql = "delete from components " + "where compid = '"
					+ c.getCompid() + "' ;";

			myStmt.executeUpdate(sql);		
		} catch (Exception e) {
			e.printStackTrace();
		}
		loadComponents();
		
		removeTCRelation(c);
	}

/////////////////////////////////////////// Ticket_Component_Relation Methods ////////////////////////////////////////////////////
	
	public void loadTCRelation(){
		tcRelationMap.clear();
		try {
			// Holen
			// 1. get conn
			Connection myConn = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/issuetracking_db",
					"glassfishadmin", "chucknorris42");
			// 2. create statement
			Statement myStmt = myConn.createStatement();
			// 3. execute sql query
			ResultSet myRs = myStmt.executeQuery("select * from tcrelation order by tid");
			// 4. Process results
			String compidinput;
			int tidinput;
			while (myRs.next()) {
				compidinput = myRs.getString("compid");
				tidinput = myRs.getInt("tid");
				if(!tcRelationMap.containsKey(compidinput)){
					tcRelationMap.put(compidinput, new LinkedList<Integer>());
				}
				tcRelationMap.get(compidinput).add(tidinput);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public List<Component> getComponentsByTicket(int tid){
		loadComponents();
		loadTCRelation();
		List<Component> list = new LinkedList<Component>();
		for(String cid : tcRelationMap.keySet()){
			if(tcRelationMap.get(cid).contains(tid)) list.add(getComponentById(cid));
		}
		return list;
	}
	
	public List<Ticket> getTicketsByComponent(String compid){
		loadTCRelation();
		List<Ticket> list = new LinkedList<Ticket>();
		for(Integer tid : tcRelationMap.get(compid)){
			list.add(getTicketById(tid));
		}
		return list;
	}
	
	
	
	public void saveTCRelation(Ticket t1, Component c){
		try {
			// Einf�gen
			// 1. get conn
			Connection myConn = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/issuetracking_db",
					"glassfishadmin", "chucknorris42");
			// 2. create statement
			Statement myStmt = myConn.createStatement();
			// 3. Execute SQL query
			String sql = "insert into tcrelation " + " (tid, compid)"
					+ " values('" + t1.getId() + "', '" + c.getCompid() + "');";

			myStmt.executeUpdate(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		loadTCRelation();
	}
	
	public void updateTCRelation(Ticket t1, List<Component> clist){
		removeTCRelation(t1);
		for(Component c : clist){
			saveTCRelation(t1, c);
		}
	}
	
	
	
	public void removeTCRelation(Ticket t1, Component c){
		try {
			// L�schen
			// 1. get conn
			Connection myConn = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/issuetracking_db",
					"glassfishadmin", "chucknorris42");
			// 2. create statement
			Statement myStmt = myConn.createStatement();
			// 3. Execute SQL query
			String sql = "delete from tcrelation " + "where compid = '"
					+ c.getCompid() + "' and tid = '" + t1.getId() + "' ;";

			myStmt.executeUpdate(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		loadTCRelation();
		
	}
	
	public void removeTCRelation(Component c){
		try {
			// L�schen
			// 1. get conn
			Connection myConn = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/issuetracking_db",
					"glassfishadmin", "chucknorris42");
			// 2. create statement
			Statement myStmt = myConn.createStatement();
			// 3. Execute SQL query
			String sql = "delete from tcrelation " + "where compid = '"
					+ c.getCompid() + "';";

			myStmt.executeUpdate(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		loadTCRelation();
	}
	
	public void removeTCRelation(Ticket t1){
		try {
			// L�schen
			// 1. get conn
			Connection myConn = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/issuetracking_db",
					"glassfishadmin", "chucknorris42");
			// 2. create statement
			Statement myStmt = myConn.createStatement();
			// 3. Execute SQL query
			String sql = "delete from tcrelation " + "where tid = '"
					+ t1.getId() + "';";

			myStmt.executeUpdate(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		loadTCRelation();
				
	}

/////////////////////////////////////// Comment Methods ////////////////////////////////////////////////////
	
	public void loadComments(){
		commentsMap.clear();
		try {
			// Holen
			// 1. get conn
			Connection myConn = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/issuetracking_db",
					"glassfishadmin", "chucknorris42");
			// 2. create statement
			Statement myStmt = myConn.createStatement();
			// 3. execute sql query
			ResultSet myRs = myStmt.executeQuery("select * from comments order by cid");
			// 4. Process results
			while (myRs.next()) {
		
				java.util.Date date= null;
				Timestamp timestamp = myRs.getTimestamp("creation_date");
				if (timestamp != null)
				    date = new java.util.Date(timestamp.getTime());
				
				Comment c1 = new Comment(myRs.getInt("cid"), myRs.getInt("tid"), date, myRs.getString("author"), myRs.getString("message"));
				commentsMap.put(c1.getCid(), c1);
			}
		} catch (Exception e) {
			e.printStackTrace();
			}
	
		}
	
	public void saveComment(Comment comment1){
		try {
			// Einf�gen
			// 1. get conn
			Connection myConn = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/issuetracking_db",
					"glassfishadmin", "chucknorris42");
			// 2. create statement
			Statement myStmt = myConn.createStatement();
			// 3. Execute SQL query
			
			String sql = "insert into comments " + " (cid ,tid, creation_date, author, message)"
					+ " values(" + comment1.cid + ", " + comment1.tid + ", '" + comment1.getDateAsStringForDatabase() + "', '" + comment1.getAuthor() + "', '" + comment1.message + "');";
	
			myStmt.executeUpdate(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		loadComments();
	}
	
	public void deleteComment(Comment c){
		try {
			// L�schen
			// 1. get conn
			Connection myConn = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/issuetracking_db",
					"glassfishadmin", "chucknorris42");
			// 2. create statement
			Statement myStmt = myConn.createStatement();
			// 3. Execute SQL query
			String sql = "delete from comments " + "where cid = "
					+ c.getCid() + " ;";
	
			myStmt.executeUpdate(sql);		
		} catch (Exception e) {
			e.printStackTrace();
		}
		loadComments();
	}
	
	public void updateComment(Comment c){
		try {
			// Updaten
			// 1. get conn
			Connection myConn = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/issuetracking_db",
					"glassfishadmin", "chucknorris42");
			// 2. create statement
			Statement myStmt = myConn.createStatement();
			// 3. Execute SQL query
			String sql = "update comments " + "set message='" + c.getMessage()
					+ "' " + "where cid='" + c.getCid() + "';";
	
			myStmt.executeUpdate(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		loadComments();
	}

	public List<Comment> getCommentsByTicket(int tid) {
		loadComments();
		List<Comment> list = new LinkedList<Comment>();
		for (int cidkey : commentsMap.keySet()) {
			if (commentsMap.get(cidkey).getTid() == tid){
				list.add(commentsMap.get(cidkey));
				}
		}
		return list;
	}
	
	public Comment getCommentById(int comment_id){
		loadComments();
		return commentsMap.get(comment_id);
	}


/////////////////////////////////////////// Sprint Methods ////////////////////////////////////////////////////

	public void loadSprints() {
		sprintsMap.clear();
	
		try {
			// Holen
			// 1. get conn
			Connection myConn = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/issuetracking_db",
					"glassfishadmin", "chucknorris42");
			// 2. create statement
			Statement myStmt = myConn.createStatement();
			// 3. execute sql query
			ResultSet myRs = myStmt.executeQuery("select * from sprints order by sprintid");
			// 4. Process results
			while (myRs.next()) {
				java.util.Date date1= null;
				Timestamp timestamp1 = myRs.getTimestamp("start_date");
				if (timestamp1 != null)
				    date1 = new java.util.Date(timestamp1.getTime());
				java.util.Date date2= null;
				Timestamp timestamp2 = myRs.getTimestamp("end_date");
				if (timestamp2 != null)
				    date2 = new java.util.Date(timestamp2.getTime());
				
				Sprint s1 = new Sprint(myRs.getInt("sprintid"),myRs.getString("title"), date1, date2, myRs.getBoolean("active"));;
				sprintsMap.put(s1.getSprintid(), s1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	};
	
	
	public List<Sprint> getSprints() {
		loadSprints();
		List<Sprint> sprints = new LinkedList<Sprint>(sprintsMap.values());
		return sprints;
	}
	
	public void saveSprint(Sprint sprint1){
		try {
			// Einf�gen
			// 1. get conn
			Connection myConn = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/issuetracking_db",
					"glassfishadmin", "chucknorris42");
			// 2. create statement
			Statement myStmt = myConn.createStatement();
			// 3. Execute SQL query
			
			String sql = "insert into sprints " + " (sprintid ,title, start_date, end_date, active)"
					+ " values(" + sprint1.sprintid + ", '" + sprint1.title + "', '" + sprint1.getStartDateAsStringForDatabase() + "', '" + sprint1.getEndDateAsStringForDatabase() + "', '" + (sprint1.active ? "1" : "0") + "');";
			myStmt.executeUpdate(sql);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		loadSprints();
	}
	
	public Sprint getActiveSprint() {
		List<Sprint> sprints = getSprints();
		Sprint sprint = null;
		int count = 0;
		for(Sprint s: sprints){
			if(s.isActive()){
				count++;
				sprint = s;
			}
		}
		if(count == 1){
			return sprint;
		}
		return null;
	}
	
	public Sprint getSprintById(int sprintid){
		loadSprints();
		return sprintsMap.get(sprintid);
	}
	
	public void updateSprint(Sprint s){
		String active = s.isActive() ? "1" : "0";
		try {
			// Updaten
			// 1. get conn
			Connection myConn = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/issuetracking_db",
					"glassfishadmin", "chucknorris42");
			// 2. create statement
			Statement myStmt = myConn.createStatement();
			// 3. Execute SQL query
			String sql = "update sprints " + "set active='" + active + "' " + "where sprintid='" + s.getSprintid() + "';";
	
			myStmt.executeUpdate(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		loadSprints();
	}

}