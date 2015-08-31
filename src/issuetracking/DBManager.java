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
                persistanceTicket.setSprint(tupdate.getSprint());
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
	
	public List<Component> getComponents(){
                TypedQuery<Component> query = em.createQuery("SELECT c FROM Component c", Component.class);
		List<Component> components = query.getResultList();
		return components;
	}
	
	public Component getComponentById(String compid){
		return em.find(Component.class, compid);
	}
	
	public void saveComponent(Component toPersist){
            em.persist(toPersist);
	}
	
	public void updateComponent(Component c){
		em.merge(c);
                em.persist(c);
	}
	
	public void deleteComponent(Component c){
		em.merge(c);
                em.remove(c);
	}
	
	public List<Component> getComponentsByTicket(int tid){
            Ticket ticket = getTicketById(tid);
            return ticket.getComponents();
	}
	
	public List<Ticket> getTicketsByComponent(String compid){
            Component comp = getComponentById(compid);
            return comp.getTickets();
	}
	
	public void removeTCRelation(Ticket t1, Component c){
		em.merge(t1);
                t1.removeComponent(c);
	}
	
	public void removeTCRelation(Component c){
            em.merge(c);
            c.clearTickets();
	}
	
	public void removeTCRelation(Ticket t1){
            em.merge(t1);
		t1.clearComponents();		
	}

public int saveComment(Comment comment1){
    em.persist(comment1);
    return comment1.getCid();
}

public void deleteComment(Comment c){
    c = em.merge(c);
    em.remove(c);
}

public void updateComment(Comment c){
	c = em.merge(c);
        em.persist(c);
}
	
	public Comment getCommentById(int comment_id){
            return em.find(Comment.class, comment_id);
	}


////////////////////////////////////////////////////////////////////

public List<Sprint> getSprints() {
    TypedQuery<Sprint> query = em.createQuery("SELECT s FROM Sprint s", Sprint.class);
	List<Sprint> sprints = query.getResultList();
	return sprints;
}

public Sprint getSprintById(int sprintid){
    return em.find(Sprint.class, sprintid);
}


public void saveSprint(Sprint sprint1){
	em.persist(sprint1);
}

	public void deleteSprint(Sprint s) {
		s=em.merge(s);
                em.remove(s);
	}
	
		
	
	public void updateSprint(Sprint supdate) {
		supdate = em.merge(supdate);
                em.persist(supdate);
	}
	
	public Sprint getActiveSprint() {
		TypedQuery<Sprint> query = em.createQuery("SELECT s FROM Sprint s WHERE s.active LIKE true", Sprint.class);
                List<Sprint> activeSprints = query.getResultList();
                if(activeSprints.size()==1)
                    return activeSprints.get(0);
                else
                    return null;
	}
	
	
}