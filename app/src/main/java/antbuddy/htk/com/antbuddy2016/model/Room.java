package antbuddy.htk.com.antbuddy2016.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import antbuddy.htk.com.antbuddy2016.api.ParseJson;
import antbuddy.htk.com.antbuddy2016.util.JSONKey;

public class Room {

	private String createdBy;
	private String org;
	private String key;
	private List<User> users;
	private int countFiles;
	private int	countMessages;
	private boolean isPublic;
	private String status;
	private String pinMessage;
	private String topic;
	private String name;
	private String created;
	private boolean isFavorite;

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getOrg() {
		return org;
	}

	public void setOrg(String org) {
		this.org = org;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public int getCountFiles() {
		return countFiles;
	}

	public void setCountFiles(int countFiles) {
		this.countFiles = countFiles;
	}

	public int getCountMessages() {
		return countMessages;
	}

	public void setCountMessages(int countMessages) {
		this.countMessages = countMessages;
	}

	public boolean isPublic() {
		return isPublic;
	}

	public void setIsPublic(boolean isPublic) {
		this.isPublic = isPublic;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPinMessage() {
		return pinMessage;
	}

	public void setPinMessage(String pinMessage) {
		this.pinMessage = pinMessage;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public boolean isFavorite() {
		return isFavorite;
	}

	public void setIsFavorite(boolean isFavorite) {
		this.isFavorite = isFavorite;
	}

	public static Room parse(JSONObject object) {
		Room room = new Room();
		try {
			room.setCreatedBy(object.getString(JSONKey.createdBy));
		} catch (JSONException e) {
			room.setCreatedBy("");
		}

		try {
			room.setOrg(object.getString(JSONKey.org));
		} catch (JSONException e) {
			room.setOrg("");
		}

		try {
			room.setKey(object.getString(JSONKey.key));
		} catch (JSONException e) {
			room.setKey("");
		}

		try {
			room.setCountFiles(object.getInt(JSONKey.countFiles));
		} catch (JSONException e) {
			room.setCountFiles(0);
		}

		try {
			room.setCountMessages(object.getInt(JSONKey.countMessages));
		} catch (JSONException e) {
			room.setCountMessages(0);
		}

		try {
			room.setIsPublic(object.getBoolean(JSONKey.isPublic));
		} catch (JSONException e) {
			room.setIsPublic(false);
		}

		try {
			room.setStatus(object.getString(JSONKey.status));
		} catch (JSONException e) {
			room.setStatus("");
		}

		try {
			room.setPinMessage(object.getString(JSONKey.pinMessage));
		} catch (JSONException e) {
			room.setPinMessage("");
		}

		try {
			room.setTopic(object.getString(JSONKey.topic));
		} catch (JSONException e) {
			room.setTopic("");
		}

		try {
			room.setName(object.getString(JSONKey.name));
		} catch (JSONException e) {
			room.setName("");
		}

		try {
			room.setCreated(object.getString(JSONKey.created));
		} catch (JSONException e) {
			room.setCreated("");
		}

		try {
			room.setIsFavorite(object.getBoolean(JSONKey.isFavorite));
		} catch (JSONException e) {
			room.setIsFavorite(false);
		}

		JSONArray userArray = null;
		try {
			userArray = object.getJSONArray(JSONKey.users);
		} catch (JSONException e) {
			userArray = new JSONArray();
		}

		List<User> listUsers = new ArrayList<>();
		for (int i = 0; i < userArray.length(); i++) {
			try {
				JSONObject rowObject = (JSONObject) userArray.get(i);
				User user = new User();

				try {
					user.setUser(rowObject.getString(JSONKey.user));
					user.setKey(rowObject.getString(JSONKey.user));
				} catch (JSONException e) {
					user.setUser("");
					user.setKey("");
				}

				try {
					user.setRole(rowObject.getString(JSONKey.role));
				} catch (JSONException e) {
					user.setRole("");
				}

				// Check user exist
				if (user.getKey().length() > 0) {
					listUsers.add(user);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		room.setUsers(listUsers);
		if (room.getKey().length() > 0) {
			return room;
		} else {
			return null;
		}
	}
}