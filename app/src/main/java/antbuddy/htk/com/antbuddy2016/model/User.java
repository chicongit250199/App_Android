package antbuddy.htk.com.antbuddy2016.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import antbuddy.htk.com.antbuddy2016.util.AndroidHelper;
import antbuddy.htk.com.antbuddy2016.util.Constants;

public class User {
	private static final String key_avatar = "avatar";
	private static final String key_username = "username";
	private static final String key_key = "key";
	private static final String key_email = "email";
	private static final String key_name = "name";
	private static final String key_bio = "bio";
	private static final String key_nonce = "nonce";
	private static final String key_phone = "phone";
	private static final String key_role = "role";
	private static final String key_active = "active";
	private static final String key_isFavorite = "isFavorite";
	
	
	private String avatar;
	private String username;
	private String key;
	private String name;
	private String email;
	private String nonce;
	private String bio;
	private String phone;
	private String role;
	private boolean active;
	private boolean isFavorite;

	private String user; 	// The same "key", This field used in list users of Room

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getAvatar() {
		if (!avatar.startsWith("http")) {
			avatar = "https://" + Constants.domain + ".antbuddy.com/"+avatar;
		}
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNonce() {
		return nonce;
	}

	public void setNonce(String nonce) {
		this.nonce = nonce;
	}

	public String getBio() {
		return bio;
	}

	public void setBio(String bio) {
		this.bio = bio;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean isFavorite() {
		return isFavorite;
	}

	public void setIsFavorite(boolean isFavorite) {
		this.isFavorite = isFavorite;
	}

	public static ArrayList<User> parseArray(JSONArray jsonArray) {
		String lastTime = "";
		ArrayList<User> users = new ArrayList<>();
		for(int i=0; i<jsonArray.length(); i++){
			try {
				JSONObject json = jsonArray.getJSONObject(i);
				String idMessage = AndroidHelper.getString(json, key_key, null);
				if(!idMessage.isEmpty()) {
					User user = new User(json);
					users.add(user);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return users;
	}


	public User(JSONObject json) {
		avatar = AndroidHelper.getString(json, key_avatar, null);
		//fix avatar defaul
		if (!avatar.startsWith("http")) {
			avatar = "https://" + Constants.domain + ".antbuddy.com/"+avatar;
		}
		username = AndroidHelper.getString(json, key_username, null);
		key = AndroidHelper.getString(json, key_key, null);
		name = AndroidHelper.getString(json, key_name, null);
		email = AndroidHelper.getString(json, key_email, null);
		bio = AndroidHelper.getString(json, key_bio, null);
		nonce = AndroidHelper.getString(json, key_nonce, null);
		phone = AndroidHelper.getString(json, key_phone, null);
		role = AndroidHelper.getString(json, key_role, null);
		active = AndroidHelper.getBoolean(json, key_active, false);
		isFavorite =  AndroidHelper.getBoolean(json, key_isFavorite, false);
	}
}
