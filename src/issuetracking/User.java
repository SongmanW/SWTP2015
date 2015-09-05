package issuetracking;

import java.util.HashMap;
import java.util.Map;

public class User {

	private String userid;
	private String password;

	public User(String userid, String password) {
		this.userid = userid;
		this.password = password;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public static Map<String, String> validateUserRegistration(
			String useridinput, String passwordinput, DBManager DBManager1) {
		Map<String, String> errorMsg = new HashMap<String, String>();
		if (useridinput == null || useridinput.trim().equals(""))
			errorMsg.put("useridinput", "This field should not be empty!");
		if (DBManager1.containsUser(useridinput))
			errorMsg.put("useridinput", "The username is already in use!");
		if (passwordinput == null || passwordinput.trim().equals(""))
			errorMsg.put("passwordinput", "This field should not be empty!");
		return errorMsg;
	}

	public static Map<String, String> validateUserLogin(String useridinput,
			String passwordinput) {
		Map<String, String> errorMsg = new HashMap<String, String>();
		if (useridinput == null || useridinput.trim().equals(""))
			errorMsg.put("useridinput", "This field should not be empty!");
		if (passwordinput == null || passwordinput.trim().equals(""))
			errorMsg.put("passwordinput", "This field should not be empty!");
		return errorMsg;
	}

	public static Map<String, String> validateUserChange(String passwordinput) {
		Map<String, String> errorMsg = new HashMap<String, String>();
		if (passwordinput == null || passwordinput.trim().equals(""))
			errorMsg.put("passwordinput", "This field should not be empty!");
		return errorMsg;
	}

}
