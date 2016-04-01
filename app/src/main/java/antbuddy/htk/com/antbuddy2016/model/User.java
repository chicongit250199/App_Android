package antbuddy.htk.com.antbuddy2016.model;

import org.json.JSONException;
import org.json.JSONObject;

public class User {
	private String _id;  //"5370848992cf74b9043cc24e"
	private String name;  //"ThanhNguyen"
	private boolean active;  //true

	/**
	 * @return the _id
	 */
	public String get_id() {
		return _id;
	}
	/**
	 * @param _id the _id to set
	 */
	public void set_id(String _id) {
		this._id = _id;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the active
	 */
	public Boolean getActive() {
		return active;
	}
	/**
	 * @param active the active to set
	 */
	public void setActive(Boolean active) {
		this.active = active;
	}

	public static User parse(JSONObject object) throws JSONException {
		User user = new User();
		user.set_id(object.getString("_id"));
		user.setName(object.getString("name"));
		user.setActive(object.getBoolean("active"));
		return user;
	}
}
