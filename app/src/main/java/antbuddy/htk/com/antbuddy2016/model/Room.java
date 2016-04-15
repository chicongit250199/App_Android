package antbuddy.htk.com.antbuddy2016.model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import antbuddy.htk.com.antbuddy2016.util.AndroidHelper;

public class Room {
//	private final static String key__id = "_id";
//	private final static String key_createdBy = "createdBy";
//	private final static String key_org = "org";
//	private final static String key_key = "key";
//	private final static String key_anttel = "anttel";
//	private final static String key_dialplanId = "dialplanId";
//	private final static String key_confId = "confId";
//	private final static String key_anttelStep = "anttelStep";
//	private final static String key_users = "users";
//	private final static String key_countFiles = "countFiles";
//	private final static String key_countMessages = "countMessages";
//	private final static String key_isPublic = "isPublic";
//	private final static String key_status = "status";
//	private final static String key_pinMessage = "pinMessage";
//	private final static String key_topic = "topic";
//	private final static String key_name = "name";
//	private final static String key_created = "created";
//	private final static String key_isFavorite = "isFavorite";

	private String _id;
	private String createdBy;
	private String org;
	private String key;
	//private String anttel;
	private List<UserInRoom> users;
	private int countFiles;
	private int countMessages;
	private Boolean isPublic;
	private String status;
	private String pinMessage;
	private String topic;
	private String name;
	private String created;
	private Boolean isFavorite;

	public String get_id() {
		return _id;
	}

	public String getKey_createdBy() {
		return createdBy;
	}

	public String getOrg() {
		return org;
	}

	public String getKey() {
		return key;
	}

	public List<UserInRoom> getUsers() {
		return users;
	}

	public int getCountFiles() {
		return countFiles;
	}

	public int getCountMessages() {
		return countMessages;
	}

	public String getName() {
		return name;
	}

	public Boolean getIsPublic() {
		return isPublic;
	}

	public String getStatus() {
		return status;
	}

	public String getPinMessage() {
		return pinMessage;
	}

	public String getTopic() {
		return topic;
	}

	public String getCreated() {
		return created;
	}

	public Boolean getIsFavorite() {
		return isFavorite;
	}

//	public static ArrayList<Room> parseArray(JSONArray jsonArray) {
//		String lastTime = "";
//		ArrayList<Room> rooms = new ArrayList<>();
//		for(int i=0; i<jsonArray.length(); i++){
//			try {
//				JSONObject json = jsonArray.getJSONObject(i);
//				String idMessage = AndroidHelper.getString(json, key_key, null);
//				if(!idMessage.isEmpty()) {
//					Room room = new Room(json);
//					rooms.add(room);
//				}
//			} catch (JSONException e) {
//				e.printStackTrace();
//			}
//		}
//		return rooms;
//	}

//	public Room(JSONObject json) {
//		_id = AndroidHelper.getString(json, key__id, null);
//		createdBy = AndroidHelper.getString(json, key_createdBy, null);
//		org = AndroidHelper.getString(json, key_org, null);
//		key = AndroidHelper.getString(json, key_key, null);
//		if (json.has(key_users)){
//			try {
//				users = UserInRoom.parseArray(json.getJSONArray(key_users));
//			} catch (JSONException e) {
//				e.printStackTrace();
//			}
//		}
//		countFiles = AndroidHelper.getInt(json, key_countFiles, 0);
//		countMessages = AndroidHelper.getInt(json, key_countMessages, 0);
//		isPublic = AndroidHelper.getBoolean(json, key_isPublic, false);
//		status = AndroidHelper.getString(json, key_status, null);
//		pinMessage = AndroidHelper.getString(json, key_pinMessage, null);
//		topic = AndroidHelper.getString(json, key_topic, null);
//		name = AndroidHelper.getString(json, key_name, null);
//		created = AndroidHelper.getString(json, key_created, null);
//		isFavorite = AndroidHelper.getBoolean(json, key_isFavorite, false);
//	}

	public static class UserInRoom {
//		private final static String key_user = "user";
//		private final static String key__id = "_id";
//		private final static String key_role = "role";
		private String user;
		private String _id;
		private int role;

		public String getUser() {
			return user;
		}

		public void setUser(String user) {
			this.user = user;
		}

		public String get_id() {
			return _id;
		}

		public void set_id(String _id) {
			this._id = _id;
		}

		public int getRole() {
			return role;
		}

		public void setRole(int role) {
			this.role = role;
		}

		//		public static ArrayList<UserInRoom> parseArray(JSONArray jsonArray) {
//			ArrayList<UserInRoom> usersInRoom = new ArrayList<>();
//			for(int i=0; i<jsonArray.length(); i++){
//				try {
//					JSONObject json = jsonArray.getJSONObject(i);
//					String key = AndroidHelper.getString(json, key_user, null);
//					if(!key.isEmpty()) {
//						UserInRoom userInRoom = new UserInRoom(json);
//						usersInRoom.add(userInRoom);
//					}
//				} catch (JSONException e) {
//					e.printStackTrace();
//				}
//			}
//			return usersInRoom;
//		}
//
//		public UserInRoom(JSONObject json) {
//			user = AndroidHelper.getString(json, key_user, null);
//			_id = AndroidHelper.getString(json, key__id, null);
//			role = AndroidHelper.getInt(json, key__id, 0);
//		}
	}

	public String toString() {
		StringBuilder result = new StringBuilder();
		String newLine = System.getProperty("line.separator");

		result.append( this.getClass().getName() );
		result.append( " Object {" );
		result.append(newLine);

		//determine fields declared in this class only (no fields of superclass)
		Field[] fields = this.getClass().getDeclaredFields();

		//print field names paired with their values
		for ( Field field : fields  ) {
			result.append("  ");
			try {
				result.append( field.getName() );
				result.append(": ");
				//requires access to private field:
				result.append( field.get(this) );
			} catch ( IllegalAccessException ex ) {
				System.out.println(ex);
			}
			result.append(newLine);
		}
		result.append("}");

		return result.toString();
	}
}