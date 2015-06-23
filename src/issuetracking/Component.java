package issuetracking;

import java.util.HashMap;
import java.util.Map;

public class Component {
	protected String compid;
	protected String description;
	
	public Component() {
	}

	public String getCompid() {
		return compid;
	}

	public void setCompid(String compid) {
		this.compid = compid;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public Map<String, String> validate() {
		Map<String, String> errorMsg = new HashMap<String, String>();
		if (compid == null || compid.trim().equals(""))
			errorMsg.put("id", "This field should not be empty!");
		if (description == null || description.trim().equals(""))
			errorMsg.put("description", "This field should not be empty!");

		return errorMsg;
	}
}
