package issuetracking;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.sql.DataSource;

@Stateless
@LocalBean
public class DBManager {
    
    @Resource(mappedName = "jdbc/issuetracking/Datasource")
    private DataSource ds;
    
    @PersistenceContext(unitName="SWTP2015PU")
    private EntityManager em;

	private static DBManager DBManager1;

	private static Map<String, User> usersMap = new HashMap<String, User>();
	private static Map<String, Component> componentsMap = new HashMap<String, Component>();
	private static Map<String, List<Integer>> tcRelationMap = new HashMap<String, List<Integer>>();
	private static Map<Integer, Comment> commentsMap = new HashMap<Integer, Comment>();
	private static Map<Integer, Sprint> sprintsMap = new HashMap<Integer, Sprint>(); 
        
        public DBManager() {
        }
        
	public static DBManager getInstance() {
		if (DBManager.DBManager1 == null) {
			DBManager.DBManager1 = new DBManager();
		}
		return DBManager.DBManager1;
	}

        private Connection getConnection() throws SQLException{
            			Connection myConn = ds.getConnection();
                                return myConn;
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

	public List<Ticket> getTickets() {
                TypedQuery<Ticket> query = em.createQuery("SELECT t FROM Ticket t", Ticket.class);
		List<Ticket> tickets = query.getResultList();
		return tickets;
	}

	public Ticket getTicketById(int i) {
		return em.find(Ticket.class, i);
	}
	
	public List<Ticket> getTicketsByState(String state, int sprintid) {
		List<Ticket> ticketList = getTickets();
		List<Ticket> ticketsBySprintid = new LinkedList<Ticket>();
		List<Ticket> ticketsByBoth = new LinkedList<Ticket>();
		
		//when sprintid is -2, return tickets of all sprints (for alltickets.jsp)
		if(sprintid!=-2){
		for(Ticket t : ticketList){
			if(t.getSprintid()==sprintid){
				ticketsBySprintid.add(t);
			}
		}}
		else 
		for(Ticket t : ticketList){
			ticketsBySprintid.add(t);
		}
		
		
		
		if(!state.equals("beliebig")){
		for(Ticket t : ticketsBySprintid){
			if(t.getStatus().equals(state)){
				ticketsByBoth.add(t);
			}}
		}
		else 
			for(Ticket t : ticketsBySprintid){
				ticketsByBoth.add(t);
			}
		
		
		
		return ticketsByBoth;
	}

	public Integer saveTicket(Ticket t1) {
            em.persist(t1);
            return t1.getId();
	}

	public void updateTicket(Ticket tupdate) {
		Ticket persistanceTicket = em.find(Ticket.class, tupdate.getId());
                persistanceTicket.setAuthor(tupdate.getAuthor());
                persistanceTicket.setCreation_date(tupdate.getCreation_date());
                persistanceTicket.setDescription(tupdate.getDescription());
                persistanceTicket.setEstimated_time(tupdate.getEstimated_time());
                persistanceTicket.setResponsible_user(tupdate.getResponsible_user());
                persistanceTicket.setSprintid(tupdate.getSprintid());
                persistanceTicket.setStatus(tupdate.getStatus());
                persistanceTicket.setTitle(tupdate.getTitle());
                persistanceTicket.setType(tupdate.getType());
                em.persist(persistanceTicket);
	}

	public void deleteTicket(Ticket t1) {
            t1 = em.merge(t1);
            em.remove(t1);
	}

	public void loadUsers() {
		usersMap.clear();

		try {
			// Holen
			// 1. get conn
			Connection myConn = getConnection();
			// 2. create statement
			Statement myStmt = myConn.createStatement();
			// 3. execute sql query
			ResultSet myRs = myStmt.executeQuery("select * from USERS order by USERID");
			// 4. Process results
			while (myRs.next()) {
				User u1 = new User(myRs.getString("USERID"),myRs.getString("PASSWORD"));
				
				usersMap.put(u1.getUserid(), u1);
			}
			
			try { if( myStmt != null ) myStmt.close(); } catch( Exception ex ) {/* nothing to do*/};
			try { if( myConn != null ) myConn.close(); } catch( Exception ex ) {/* nothing to do*/};
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
			// Einfoegen
			// 1. get conn
			Connection myConn = getConnection();
			// 2. create statement
			Statement myStmt = myConn.createStatement();
			// 3. Execute SQL query
                        //TODO catch exceptions
                        String encryptedPassword = encryptPassword(password);
			String addUserQuery = "insert into USERS " + " (USERID, PASSWORD)"
					+ " values('" + userid + "', '" + encryptedPassword + "');";
                        
			myStmt.executeUpdate(addUserQuery);
                        
                        String addGroupQuery = "insert into USERS_GROUPS (GROUPID, USERID) values ('user', '" + userid + "');";
                        myStmt.executeUpdate(addGroupQuery);
		} catch (Exception e) {
                    System.out.println("Exception");
			e.printStackTrace();
		}
		loadUsers();
	}
        
        public static String encryptPassword(String clearText) throws UnsupportedEncodingException, NoSuchAlgorithmException{
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            String text = clearText;
            md.update(text.getBytes("UTF-8")); // Change this to "UTF-16" if needed
            byte[] digest = md.digest();
            BigInteger bigInt = new BigInteger(1, digest);
            String encrypted = bigInt.toString(16);
            return encrypted;
        }

	public void updateUser(User u1) {
		try {
			// Updaten
			// 1. get conn
			Connection myConn = getConnection();
			// 2. create statement
			Statement myStmt = myConn.createStatement();
			// 3. Execute SQL query
			String sql = "update USERS " + "set PASSWORD='" + encryptPassword(u1.getPassword())
					+ "' " + "where USERID='" + u1.getUserid() + "';";

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
			Connection myConn = getConnection();
			// 2. create statement
			Statement myStmt = myConn.createStatement();
			// 3. execute sql query
			ResultSet myRs = myStmt
					.executeQuery("select * from USERS where USERID = '"
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
			// Loeschen
			// 1. get conn
			Connection myConn = getConnection();
			// 2. create statement
			Statement myStmt = myConn.createStatement();
			// 3. Execute SQL query
			String sql = "delete from USERS " + "where USERID = '"
					+ u1.getUserid() + "' ;";

			myStmt.executeUpdate(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		loadUsers();
	}

	public void loadComponents(){
		componentsMap.clear();
		try {
			// Holen
			// 1. get conn
			Connection myConn = getConnection();
			// 2. create statement
			Statement myStmt = myConn.createStatement();
			// 3. execute sql query
			ResultSet myRs = myStmt.executeQuery("select * from components order by compid");
			// 4. Process results
			while (myRs.next()) {
				Component c1 = new Component(myRs.getString("compid"), myRs.getString("description"));
				
				componentsMap.put(c1.getCompid(), c1);
			}
			try { if( myStmt != null ) myStmt.close(); } catch( Exception ex ) {/* nothing to do*/};
			try { if( myConn != null ) myConn.close(); } catch( Exception ex ) {/* nothing to do*/};
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
			// Einfoegen
			// 1. get conn
			Connection myConn = getConnection();
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
			Connection myConn = getConnection();
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
			// Loeschen
			// 1. get conn
			Connection myConn = getConnection();
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
	
	public void loadTCRelation(){
		tcRelationMap.clear();
		try {
			// Holen
			// 1. get conn
			Connection myConn = getConnection();
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
			try { if( myStmt != null ) myStmt.close(); } catch( Exception ex ) {/* nothing to do*/};
			try { if( myConn != null ) myConn.close(); } catch( Exception ex ) {/* nothing to do*/};
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
			// Einfuegen
			// 1. get conn
			Connection myConn = getConnection();
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
			// Loeschen
			// 1. get conn
			Connection myConn = getConnection();
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
			// Loeschen
			// 1. get conn
			Connection myConn = getConnection();
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
			// Loeschen
			// 1. get conn
			Connection myConn = getConnection();
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







public void loadComments(){
	commentsMap.clear();
	try {
		// Holen
		// 1. get conn
		Connection myConn = getConnection();
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
		try { if( myStmt != null ) myStmt.close(); } catch( Exception ex ) {/* nothing to do*/};
		try { if( myConn != null ) myConn.close(); } catch( Exception ex ) {/* nothing to do*/};
	} catch (Exception e) {
		e.printStackTrace();
		}

	}

public void saveComment(Comment comment1){
	try {
		// Einfuegen
		// 1. get conn
		Connection myConn = getConnection();
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
		// Loeschen
		// 1. get conn
		Connection myConn = getConnection();
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
		Connection myConn = getConnection();
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


////////////////////////////////////////////////////////////////////

public void loadSprints() {
	sprintsMap.clear();

	try {
		// Holen
		// 1. get conn
		Connection myConn = getConnection();
		// 2. create statement
		Statement myStmt = myConn.createStatement();
		// 3. execute sql query
		ResultSet myRs = myStmt.executeQuery("select * from sprints order by sprintid;");
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
		try { if( myStmt != null ) myStmt.close(); } catch( Exception ex ) {/* nothing to do*/};
		try { if( myConn != null ) myConn.close(); } catch( Exception ex ) {/* nothing to do*/};
	} catch (Exception e) {
		e.printStackTrace();
	}
};


public List<Sprint> getSprints() {
	loadSprints();
	List<Sprint> sprints = new LinkedList<Sprint>(sprintsMap.values());
	return sprints;
}

public Sprint getSprintById(int sprintid){
	loadSprints();
	Sprint sprint1= sprintsMap.get(sprintid);
	return sprint1;
}


public void saveSprint(Sprint sprint1){
	try {
		// Einfuegen
		// 1. get conn
		Connection myConn = getConnection();
		// 2. create statement
		Statement myStmt = myConn.createStatement();
		// 3. Execute SQL query
		
		String sql = "insert into sprints " + " (sprintid ,title, start_date, end_date, active)"
				+ " values(" + sprint1.sprintid + ", '" + sprint1.title + "', '" + sprint1.getStartDateAsStringForDatabase() + "', '" + sprint1.getEndDateAsStringForDatabase() + "', '" + (sprint1.active ? "1" : "0") + "');";
		myStmt.executeUpdate(sql);
		try { if( myStmt != null ) myStmt.close(); } catch( Exception ex ) {/* nothing to do*/};
		try { if( myConn != null ) myConn.close(); } catch( Exception ex ) {/* nothing to do*/};
	} catch (Exception e) {
		e.printStackTrace();
	}
	loadSprints();
}

	public void deleteSprint(Sprint s) {
		try {
			List<Ticket> openTs= DBManager1.getTicketsByState("open",s.sprintid);//,Integer.parseInt(request.getParameter("sprintid"))));
			List<Ticket> in_progressTs= DBManager1.getTicketsByState("in_progress",s.sprintid);//,Integer.parseInt(request.getParameter("sprintid"))));
			List<Ticket> testTs= DBManager1.getTicketsByState("test",s.sprintid);//,Integer.parseInt(request.getParameter("sprintid"))));
//			if (!(openTs.isEmpty() && in_progressTs.isEmpty() && testTs.isEmpty()))
//					 {
//				System.out.println("Deletion not possible. There are still not closed Tickets which belong to this Sprint");
//			}else{
			// L�schen
			// 1. get conn
			Connection myConn = getConnection();
			// 2. create statement
			Statement myStmt = myConn.createStatement();
			// 3. Execute SQL query
			String sql = "delete from sprints " + "where sprintid = '"
					+ s.getSprintid() + "' ;";

			myStmt.executeUpdate(sql);
			try { if( myStmt != null ) myStmt.close(); } catch( Exception ex ) {/* nothing to do*/};
			try { if( myConn != null ) myConn.close(); } catch( Exception ex ) {/* nothing to do*/};
		} catch (Exception e) {
			e.printStackTrace();
		}
		loadSprints();	
	}
	
		
	
	public void updateSprint(Sprint supdate) {
		loadSprints();
		Sprint s1 = DBManager1.getSprintById(supdate.getSprintid());

		deleteSprint(s1);
		saveSprint(supdate);
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
	
	
}