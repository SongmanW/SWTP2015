package issuetracking;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

public class DBManager {
	
	public static void main(String[] args){
		System.out.println("ok");
	}
	
	private static DBManager instance;
	
	private static Map<Integer, Ticket> ticketsMap = new HashMap<Integer, Ticket>();
	private DBManager () {}
	
	public static DBManager getInstance () {
	    if (DBManager.instance == null) {
	    	DBManager.instance = new DBManager();
	    }
	    return DBManager.instance;
	}
	
	public int getNextId () {
		int i=1;
		for(;i<10000;i++){
			if(!ticketsMap.keySet().contains(i))break;
		}
	    return i;
	}
	
	
	public void loadTickets(){
		ticketsMap.clear();
		try {
			//Holen
			//1. get conn
			Connection myConn= DriverManager.getConnection("jdbc:mysql://localhost:3306/issuetracking_db","glassfishadmin","chucknorris42");
			//2. create statement
			Statement myStmt=myConn.createStatement();
			//3. execute sql query
			ResultSet myRs=myStmt.executeQuery("select * from tickets");
			//4. Process results
			while(myRs.next()){
				Ticket t1=new Ticket();
				t1.setId(myRs.getInt("id"));
				t1.setTitle(myRs.getString("title"));
				t1.setDescription(myRs.getString("description"));
				ticketsMap.put(t1.getId(),t1);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		//System.out.println(""+ticketsMap.size()+" Tickets wurden geholt");
	};
	
	
	public List<Ticket> getTickets(){
		List<Ticket> tickets=new LinkedList<Ticket>(ticketsMap.values());
		return tickets;
	}
	
	public Ticket getTicketById(int i){
		return ticketsMap.get(i);	
	}

	public void saveTicket(Ticket t1){
		try {
			//Einfügen
			//1. get conn
			Connection myConn= DriverManager.getConnection("jdbc:mysql://localhost:3306/issuetracking_db","glassfishadmin","chucknorris42");
			//2. create statement
			Statement myStmt=myConn.createStatement();
			//3. Execute SQL query
			String sql="insert into tickets "
					+" (id, title, description)"
					+" values("+t1.getId()+", '"+t1.getTitle()+"', '"+t1.getDescription()+"');";
			
			myStmt.executeUpdate(sql);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	public void updateTicket(Ticket t1){
		try {
			//Updaten
			//1. get conn
			Connection myConn= DriverManager.getConnection("jdbc:mysql://localhost:3306/issuetracking_db","glassfishadmin","chucknorris42");
			//2. create statement
			Statement myStmt=myConn.createStatement();
			//3. Execute SQL query
			String sql="update tickets "
					+"set title='"+t1.getTitle()+"', description='"+t1.getDescription()+"' "
					+"where id="+t1.getId()+";";
			
			myStmt.executeUpdate(sql);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void deleteTicket(Ticket t1){
		try {
			//Löschen
			//1. get conn
			Connection myConn= DriverManager.getConnection("jdbc:mysql://localhost:3306/issuetracking_db","glassfishadmin","chucknorris42");
			//2. create statement
			Statement myStmt=myConn.createStatement();
			//3. Execute SQL query
			String sql="delete from tickets "
					+"where id="+t1.getId()+";";
			
			myStmt.executeUpdate(sql);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
		
	
	public void registerUser(String userid,String password){
		try {
			//Einfügen
			//1. get conn
			Connection myConn= DriverManager.getConnection("jdbc:mysql://localhost:3306/issuetracking_db","glassfishadmin","chucknorris42");
			//2. create statement
			Statement myStmt=myConn.createStatement();
			//3. Execute SQL query
			String sql="insert into users "
					+" (userid, password)"
					+" values('"+userid+"', '"+password+"');";
			
			myStmt.executeUpdate(sql);
		}
		catch(Exception e){
			e.printStackTrace();
		}
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
					.executeQuery("select password from users where userid = '" + userid + "';");
			/*
			 * SELECT `nachname` , `vorname` FROM testadressen WHERE vorname =
			 * 'Fischer'
			 */;
			String s1;
			if(myRs.next()){ 
				s1 = myRs.getString(1);
				if(s1.equals(password))return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	};
	
	public boolean containsUser(String userid) {
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
					.executeQuery("select * from users where userid = '" + userid + "';");

			if(myRs.next()){ 
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	};

	/*
	public void deleteUser(String userid, String password){
		try {
			//Löschen
			//1. get conn
			Connection myConn= DriverManager.getConnection("jdbc:mysql://localhost:3306/issuetracking_db","glassfishadmin","chucknorris42");
			//2. create statement
			Statement myStmt=myConn.createStatement();
			//3. Execute SQL query
			String sql="delete from users "
					+"where userid="+userid+";";
			
			myStmt.executeUpdate(sql);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}*/
	
}