package issuetracking;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
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

    private static DBManager DBManager1;

    private static Map<String, User> usersMap = new HashMap<String, User>();
    private static Map<Integer, PictureFile> pictureMap = new HashMap<Integer, PictureFile>();

    public static String encryptPassword(String clearText) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        String text = clearText;
        md.update(text.getBytes("UTF-8")); // Change this to "UTF-16" if needed
        byte[] digest = md.digest();
        BigInteger bigInt = new BigInteger(1, digest);
        String encrypted = bigInt.toString(16);
        return encrypted;
    }

    /**
     * @return the folder that contains the uploaded files
     */
    public static String getFilesPath() {
        String path = "C:\\Users\\Simon\\Desktop\\test";
        //TODO
        return path;
    }

    /**
     * Sets the folder that contains the uploaded files
     *
     * @param path the path to the folder
     */
    public static void setFilesPath(String path) {
        //TODO
    }
    @Resource(mappedName = "jdbc/issuetracking/Datasource")
    private DataSource ds;
    @PersistenceContext(unitName = "SWTP2015PU")
    private EntityManager em;

    public DBManager() {
    }

    private Connection getConnection() throws SQLException {
        Connection myConn = ds.getConnection();
        return myConn;
    }

    public int getNextPictureId() {
        loadPictures();
        int i = 1;
        for (; i < 10000; i++) {
            if (!pictureMap.keySet().contains(i)) {
                break;
            }
        }
        return i;
    }

    /**
     * Gibt die Tickets als Liste zurück
     *
     * @return
     */
    public List<Ticket> getTickets() {
        TypedQuery<Ticket> query = em.createQuery("SELECT t FROM Ticket t", Ticket.class);
        List<Ticket> tickets = query.getResultList();
        return tickets;
    }

    /**
     * Gibt ein Ticket mit der entsprechenden ID zurück
     *
     * @param i ID des Tickets
     * @return
     */
    public Ticket getTicketById(int i) {
        Ticket toReturn = em.find(Ticket.class, i);
        em.refresh(toReturn);
        return toReturn;
    }

    /**
     * Gibt alle Tickets des entsprechenden Zustands des entsprechenden Sprints
     * zurück
     *
     * @param state
     * @param sprintid
     * @return
     */
    public List<Ticket> getTicketsByState(String state, int sprintid) {
        List<Ticket> ticketList = getTickets();
        List<Ticket> ticketsBySprintid = new LinkedList<Ticket>();
        List<Ticket> ticketsByBoth = new LinkedList<Ticket>();
        
        // when sprintid is -2, return tickets of all sprints (for
        // alltickets.jsp)
        if (sprintid != -2) {
            for (Ticket t : ticketList) {
                if (t.getSprintid() == sprintid) {
                    ticketsBySprintid.add(t);
                }
            }
        } else {
            for (Ticket t : ticketList) {
                ticketsBySprintid.add(t);
            }
        }

        if (!state.equals("beliebig")) {
            for (Ticket t : ticketsBySprintid) {
                if (t.getStatus().equals(state)) {
                    ticketsByBoth.add(t);
                }
            }
        } else {
            for (Ticket t : ticketsBySprintid) {
                ticketsByBoth.add(t);
            }
        }

        return ticketsByBoth;
    }

    /**
     * Speichert das Ticket in der Datenbank
     *
     * @param t1
     */
    public Integer saveTicket(Ticket t1) {
        em.persist(t1);
        return t1.getId();
    }

    ;

	/**
     * Ändert das Ticket in der Datenbank
     *
     * @param tupdate
     */
    public void updateTicket(Ticket tupdate) {
        em.merge(tupdate);
        em.flush();
    }

    /**
     * Entfernt das Ticket aus der Datenbank
     *
     * @param t1
     */
    public void deleteTicket(Ticket t1) {
        t1 = em.merge(t1);
        em.remove(t1);
        em.flush();
    }

    /**
     * Läd doe Nutzer aus der Datenbank
     */
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
                User u1 = new User(myRs.getString("USERID"), myRs.getString("PASSWORD"));

                usersMap.put(u1.getUserid(), u1);
            }

            try {
                if (myStmt != null) {
                    myStmt.close();
                }
            } catch (Exception ex) {/* nothing to do*/

            };
            try {
                if (myConn != null) {
                    myConn.close();
                }
            } catch (Exception ex) {/* nothing to do*/

            };
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Gibt die Nutzer zurück
     * @return
     */
    public List<User> getUsers() {
        loadUsers();
        List<User> users = new LinkedList<User>(usersMap.values());
        return users;
    }

    /**
     * Gibt den Nutzer mit der entsprechenden ID aus
     *
     * @param userid
     * @return
     */
    public User getUserByUserid(String userid) {
        loadUsers();
        return usersMap.get(userid);
    }

    /**
     * Registriert einen neuen Nutzer
     *
     * @param userid
     * @param password
     */
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

            String addGroupQuery = "insert into USERS_GROUPS (GROUPID, USERID) values ('user', '"
                    + userid + "');";
            myStmt.executeUpdate(addGroupQuery);
            try {
                if (myStmt != null) {
                    myStmt.close();
                }
            } catch (Exception ex) {/* nothing to do*/

            };
            try {
                if (myConn != null) {
                    myConn.close();
                }
            } catch (Exception ex) {/* nothing to do*/

            };
        } catch (Exception e) {
            System.out.println("Exception");
            e.printStackTrace();
        }
        loadUsers();
    }

    /**
     * Ändert einen Nutzer
     *
     * @param u1
     */
    public void updateUser(User u1) {
        try {
            // Updaten
            // 1. get conn
            Connection myConn = getConnection();
            // 2. create statement
            Statement myStmt = myConn.createStatement();
            // 3. Execute SQL query
            String sql = "update USERS " + "set PASSWORD='"
                    + encryptPassword(u1.getPassword()) + "' "
                    + "where USERID='" + u1.getUserid() + "';";

            myStmt.executeUpdate(sql);
            try {
                if (myStmt != null) {
                    myStmt.close();
                }
            } catch (Exception ex) {/* nothing to do*/

            };
            try {
                if (myConn != null) {
                    myConn.close();
                }
            } catch (Exception ex) {/* nothing to do*/

            };
        } catch (Exception e) {
            e.printStackTrace();
        }
        loadUsers();
    }

    /**
     * Prüft ob ein Nutzer in der Datenbank enthalten ist
     *
     * @param userid
     * @return
     */
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
            try {
                if (myStmt != null) {
                    myStmt.close();
                }
            } catch (Exception ex) {/* nothing to do*/

            };
            try {
                if (myConn != null) {
                    myConn.close();
                }
            } catch (Exception ex) {/* nothing to do*/

            };
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Löscht einen Nutzer
     *
     * @param u1
     */
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
            try {
                if (myStmt != null) {
                    myStmt.close();
                }
            } catch (Exception ex) {/* nothing to do*/

            };
            try {
                if (myConn != null) {
                    myConn.close();
                }
            } catch (Exception ex) {/* nothing to do*/

            };
        } catch (Exception e) {
            e.printStackTrace();
        }
        loadUsers();
    }

    /**
     * Gibt alle Komponenten zurück
     *
     * @return
     */
    public List<Component> getComponents() {
        TypedQuery<Component> query = em.createQuery("SELECT c FROM Component c", Component.class);
        List<Component> components = query.getResultList();
        return components;
    }

    /**
     * Gibt die Komponente mit der entsprechenden ID zurück
     *
     * @param compid
     * @return
     */
    public Component getComponentById(Integer compid) {
        return em.find(Component.class, compid);
    }

    /**
     * Speichert eine Komponente in die Datenbank
     *
     * @param compid
     * @param description
     */
    public void saveComponent(Component toPersist) {
        em.persist(toPersist);
    }

    /**
     * Ändert eine Komponente in der Datenbank
     *
     * @param c
     */
    public void updateComponent(Component c) {
        em.merge(c);
    }

    /**
     * Löscht eine Komponente aus der Datenbank
     *
     * @param c
     */
    public void deleteComponent(Component c) {
        c = em.merge(c);
        em.remove(c);
    }

    /**
     * Gibt die Komponente mit der entsprechenden ID zurück
     *
     * @param tid
     * @return
     */
    public List<Component> getComponentsByTicket(int tid) {
        Ticket ticket = getTicketById(tid);
        return ticket.getComponents();
    }

    /**
     * Gibt eine Liste mit allen Tickets aus, welche die Komponente besitzen
     *
     * @param compid
     * @return
     */
    public List<Ticket> getTicketsByComponent(Integer compid) {
        Component comp = getComponentById(compid);
        return comp.getTickets();
    }

    public void removeTCRelation(Ticket t1, Component c) {
        em.merge(t1);
        t1.removeComponent(c);
    }

    /**
     * Löscht eine TCRelation aus der Datenbank
     *
     * @param c
     */
    public void removeTCRelation(Component c) {
        em.merge(c);
        c.clearTickets();
    }

    /**
     * Löscht eine TCRelation aus der Datenbank
     *
     * @param t1
     */
    public void removeTCRelation(Ticket t1) {
        em.merge(t1);
        t1.clearComponents();
    }

    /**
     * Speichert einen Kommentar in der Datenbank
     *
     * @param comment1
     */
    public int saveComment(Comment comment1) {
        comment1.setTicket(em.merge(comment1.getTicket()));
        em.persist(comment1);
        em.flush();
        em.refresh(comment1.getTicket());
        return comment1.getCid();
    }

    /**
     * Löscht einen Kommentar in der Datenbank
     *
     * @param c
     */
    public void deleteComment(Comment c) {
        c = em.merge(c);
        em.remove(c);
    }

    /**
     * Löscht alle Kommentare des Tickets
     *
     * @param t1
     */
    public void removeComments(Ticket t1) {
        t1 = em.merge(t1);
        List<Comment> commentList = t1.getComments();
        for (Comment c : commentList) {
            em.remove(c);
        }
        t1.clearComponents();
        em.refresh(t1);
    }

/**
     * Ändert einen Kommentar
     *
     * @param c
     */
    public void updateComment(Comment c) {
        c = em.merge(c);
        em.flush();
    }

    public Comment getCommentById(int comment_id) {
        return em.find(Comment.class, comment_id);
    }

////////////////////////////////////////////////////////////////////

    /**
     * Gibt alle Sprints zurück
     *
     * @return
     */
    public List<Sprint> getSprints() {
        TypedQuery<Sprint> query = em.createQuery("SELECT s FROM Sprint s", Sprint.class);
        List<Sprint> sprints = query.getResultList();
        return sprints;
    }

    /**
     * Gibt den entsprechenden Sprint zurück
     *
     * @param sprintid
     * @return
     */
    public Sprint getSprintById(int sprintid) {
        return em.find(Sprint.class, sprintid);
    }

    /**
     * Speichert den Sprint in der Datenbank
     *
     * @param sprint1
     */
    public void saveSprint(Sprint sprint1) {
        em.persist(sprint1);
    }

    /**
     * Löscht den Sprint in der Datenbank
     *
     * @param s
     */
    public void deleteSprint(Sprint s) {
        s = em.merge(s);
        em.remove(s);
    }

    /**
     * Ändert den Sprint in der Datenbank
     *
     * @param supdate
     */
    public void updateSprint(Sprint supdate) {
        supdate = em.merge(supdate);
        em.persist(supdate);
    }

    /**
     * Gibt den aktiven Sprint aus
     *
     * @return
     */
    public Sprint getActiveSprint() {
        TypedQuery<Sprint> query = em.createQuery("SELECT s FROM Sprint s WHERE s.active LIKE true", Sprint.class);
        List<Sprint> activeSprints = query.getResultList();
        if (activeSprints.size() == 1) {
            return activeSprints.get(0);
        } else {
            return null;
        }
    }

    /**
     * Gibt die Anzahl der Tickets aus
     *
     * @return
     */
    public int TicketCount() {
        int count = 0;
        try {
            // Holen
            // 1. get conn
            Connection myConn = getConnection();
            // 2. create statement
            Statement myStmt = myConn.createStatement();
            // 3. execute sql query
            ResultSet result = myStmt
                    .executeQuery("select COUNT(*) from tickets");
            // 4. Process results
            result.next();
            count = result.getInt(1);
            try {
                if (myStmt != null) {
                    myStmt.close();
                }
            } catch (Exception ex) {/* nothing to do*/

            };
            try {
                if (myConn != null) {
                    myConn.close();
                }
            } catch (Exception ex) {/* nothing to do*/

            };

        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    /**
     * Gibt die Anzahl der erledigten Tickets aus
     *
     * @return
     */
    public int closedTicketCount() {
        int count = 0;
        try {
            // Holen
            // 1. get conn
            Connection myConn = getConnection();
            // 2. create statement
            Statement myStmt = myConn.createStatement();
            // 3. execute sql query
            ResultSet result = myStmt
                    .executeQuery("select COUNT(*) from tickets where type = 'closed'");
            // 4. Process results
            result.next();
            count = result.getInt(1);
            try {
                if (myStmt != null) {
                    myStmt.close();
                }
            } catch (Exception ex) {/* nothing to do*/

            };
            try {
                if (myConn != null) {
                    myConn.close();
                }
            } catch (Exception ex) {/* nothing to do*/

            };

        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    /**
     * Gibt die Anzahl der Kommentare aus
     *
     * @return
     */
    public int commentCount() {
        int count = 0;
        try {
            // Holen
            // 1. get conn
            Connection myConn = getConnection();
            // 2. create statement
            Statement myStmt = myConn.createStatement();
            // 3. execute sql query
            ResultSet result = myStmt
                    .executeQuery("select COUNT(*) from comments");
            // 4. Process results
            result.next();
            count = result.getInt(1);
            try {
                if (myStmt != null) {
                    myStmt.close();
                }
            } catch (Exception ex) {/* nothing to do*/

            };
            try {
                if (myConn != null) {
                    myConn.close();
                }
            } catch (Exception ex) {/* nothing to do*/

            };

        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    /**
     * Gibt die Anzahl der Komponenten aus
     *
     * @return
     */
    public int componentCount() {
        int count = 0;
        try {
            // Holen
            // 1. get conn
            Connection myConn = getConnection();
            // 2. create statement
            Statement myStmt = myConn.createStatement();
            // 3. execute sql query
            ResultSet result = myStmt
                    .executeQuery("select COUNT(*) from components");
            // 4. Process results
            result.next();
            count = result.getInt(1);
            try {
                if (myStmt != null) {
                    myStmt.close();
                }
            } catch (Exception ex) {/* nothing to do*/

            };
            try {
                if (myConn != null) {
                    myConn.close();
                }
            } catch (Exception ex) {/* nothing to do*/

            };

        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    /**
     * Gibt die Anzahl der Nutzer aus
     *
     * @return
     */
    public int userCount() {
        int count = 0;
        try {
            // Holen
            // 1. get conn
            Connection myConn = getConnection();
            // 2. create statement
            Statement myStmt = myConn.createStatement();
            // 3. execute sql query
            ResultSet result = myStmt
                    .executeQuery("select COUNT(*) from users");
            // 4. Process results
            result.next();
            count = result.getInt(1);
            try {
                if (myStmt != null) {
                    myStmt.close();
                }
            } catch (Exception ex) {/* nothing to do*/

            };
            try {
                if (myConn != null) {
                    myConn.close();
                }
            } catch (Exception ex) {/* nothing to do*/

            };

        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    ;
    
    /**
     * Gibt die Anzahl der Sprints aus
     *
     * @return
     */
    public int sprintCount() {
        int count = 0;
        try {
            // Holen
            // 1. get conn
            Connection myConn = getConnection();
            // 2. create statement
            Statement myStmt = myConn.createStatement();
            // 3. execute sql query
            ResultSet result = myStmt
                    .executeQuery("select COUNT(*) from sprints");
            // 4. Process results
            result.next();
            count = result.getInt(1);
            try {
                if (myStmt != null) {
                    myStmt.close();
                }
            } catch (Exception ex) {/* nothing to do*/

            };
            try {
                if (myConn != null) {
                    myConn.close();
                }
            } catch (Exception ex) {/* nothing to do*/

            };

        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }
    
    // ///////////////////////////
    
    /**
     * Loads all PictureFiles stored in the database into pictureMap
     */
    public void loadPictures() {
        pictureMap.clear();

        try {
            // Holen
            // 1. get conn
            Connection myConn = getConnection();
            // 2. create statement
            Statement myStmt = myConn.createStatement();
            // 3. execute sql query
            ResultSet myRs = myStmt
                    .executeQuery("select * from pictures order by pictureid");
            // 4. Process results
            while (myRs.next()) {
                java.util.Date date1 = null;
                Timestamp timestamp1 = myRs.getTimestamp("upload_date");
                if (timestamp1 != null) {
                    date1 = new java.util.Date(timestamp1.getTime());
                }
                PictureFile p1 = new PictureFile(myRs.getInt("pictureid"),
                        myRs.getInt("ticketid"), date1,
                        myRs.getString("uploader"),
                        myRs.getString("type"));
                ;
                pictureMap.put(p1.getPictureId(), p1);
            }
            try {
                if (myStmt != null) {
                    myStmt.close();
                }
            } catch (Exception ex) {/* nothing to do */

            }
            try {
                if (myConn != null) {
                    myConn.close();
                }
            } catch (Exception ex) {/* nothing to do */

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Saves a PictureFile in the database
     * @param p1 the PictureFile to save
     */
    public void savePicture(PictureFile p1) {
        try {
            // Einfuegen
            // 1. get conn
            Connection myConn = getConnection();
            // 2. create statement
            Statement myStmt = myConn.createStatement();
            // 3. Execute SQL query

            String sql = "insert into pictures"
                    + " (pictureid ,ticketid, upload_date, uploader, type)"
                    + " values(" + p1.getPictureId() + ", " + p1.getTicketId()
                    + ", '" + p1.getUploadDateAsStringForDatabase() + "', '"
                    + p1.getUploader() + "', '" + p1.getType() + "');";
            myStmt.executeUpdate(sql);
            try {
                if (myStmt != null) {
                    myStmt.close();
                }
            } catch (Exception ex) {/* nothing to do */

            }
            try {
                if (myConn != null) {
                    myConn.close();
                }
            } catch (Exception ex) {/* nothing to do */

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        loadPictures();
    }

    /**
     * Returns the PictureFile with the matching id
     *
     * @param id the id of the picture
     * @return the PictureFile with the matching id
     */
    public PictureFile getPictureById(int id) {
        loadPictures();
        return pictureMap.get(id);
    }

    /**
     * @return All PictureFiles stored in the Database
     */
    public List<PictureFile> getPictures() {
        loadPictures();
        LinkedList<PictureFile> pics = new LinkedList<PictureFile>(pictureMap.values());
        return pics;
    }

    /**
     * Returns all PictureFiles attached to a specified ticket
     *
     * @param ticketid the id of the ticket
     * @return all PictureFiles with matching ticketid
     */
    public List<PictureFile> getPicturesByTicket(int ticketid) {
        List<PictureFile> pics = getPictures();
        List<PictureFile> result = new LinkedList<PictureFile>();
        for (int i = 0; i < pics.size(); i++) {
            if (pics.get(i).getTicketId() == ticketid) {
                result.add(pics.get(i));
            }
        }
        return result;
    }

    /**
     * Deletes a PictureFile from the database and the file system
     *
     * @param p the PictureFile to delete
     */
    public void deletePicture(PictureFile p) {
        try {
            // 1. get conn
            Connection myConn = getConnection();
            // 2. create statement
            Statement myStmt = myConn.createStatement();
            // 3. Execute SQL query
            String sql = "delete from pictures " + "where pictureid = '"
                    + p.getPictureId() + "' ;";

            myStmt.executeUpdate(sql);
            p.delete();
            try {
                if (myStmt != null) {
                    myStmt.close();
                }
            } catch (Exception ex) {/* nothing to do */

            }
            try {
                if (myConn != null) {
                    myConn.close();
                }
            } catch (Exception ex) {/* nothing to do */

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        loadPictures();
    }

    public void deletePictureByTicketId(int id) {
        try {
            List<PictureFile> pics = getPicturesByTicket(id);
            // 1. get conn
            Connection myConn = getConnection();
            // 2. create statement
            Statement myStmt = myConn.createStatement();
            // 3. Execute SQL query
            String sql = "delete from pictures " + "where ticketid = '"
                    + id + "' ;";

            myStmt.executeUpdate(sql);
            PictureFile p = null;
            while (pics.size() > 0) {
                p = pics.get(0);
                pics.remove(0);
                p.delete();
            }
            try {
                if (myStmt != null) {
                    myStmt.close();
                }
            } catch (Exception ex) {/* nothing to do */

            }

            try {
                if (myConn != null) {
                    myConn.close();
                }
            } catch (Exception ex) {/* nothing to do */

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        loadPictures();
    }
}
