package antbuddy.htk.com.antbuddy2016.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Room {

    private static final String TAG_THISCLASS = "Room";
    private String _id;
	private String name;
	private String topic;
	private String pinMessage;
	private String status;
	private boolean isPublic;
	private String createdBy;
	private long countMessages;
	private long countFiles;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the topic
	 */
	public String getTopic() {
		return topic;
	}

	/**
	 * @param topic
	 *            the topic to set
	 */
	public void setTopic(String topic) {
		this.topic = topic;
	}

	/**
	 * @return the pinMessage
	 */
	public String getPinMessage() {
		return pinMessage;
	}

	/**
	 * @param pinMessage
	 *            the pinMessage to set
	 */
	public void setPinMessage(String pinMessage) {
		this.pinMessage = pinMessage;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the isPublic
	 */
	public boolean isPublic() {
		return isPublic;
	}

	/**
	 * @param isPublic
	 *            the isPublic to set
	 */
	public void setPublic(boolean isPublic) {
		this.isPublic = isPublic;
	}

	/**
	 * @return the createdBy
	 */
	public String getCreatedBy() {
		return createdBy;
	}

	/**
	 * @param createdBy
	 *            the createdBy to set
	 */
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 * @return the _id
	 */
	public String get_id() {
		return _id;
	}

	/**
	 * @param _id
	 *            the _id to set
	 */
	public void set_id(String _id) {
		this._id = _id;
	}

	/**
	 * @return the countMessages
	 */
	public long getCountMessages() {
		return countMessages;
	}

	/**
	 * @param countMessages
	 *            the countMessages to set
	 */
	public void setCountMessages(long countMessages) {
		this.countMessages = countMessages;
	}

	/**
	 * @return the countFiles
	 */
	public long getCountFiles() {
		return countFiles;
	}

	/**
	 * @param countFiles
	 *            the countFiles to set
	 */
	public void setCountFiles(long countFiles) {
		this.countFiles = countFiles;
	}

	public static Room parse(JSONObject object) throws JSONException {
		Room room = new Room();
		room.setName(object.getString("name"));
		room.setTopic(object.getString("topic"));
		room.setPinMessage(object.getString("pinMessage"));
		room.setStatus(object.getString("status"));
		room.setPublic(object.getBoolean("isPublic"));
		room.setCountMessages(object.getLong("countMessages"));
		room.setCountFiles(object.getLong("countFiles"));
		room.set_id(object.getString("_id"));
		room.setCreatedBy(object.getString("createdBy"));

		JSONArray userArray = object.getJSONArray("users");
		for (int i = 0; i < userArray.length(); i++) {
			try {

                JSONObject rowObject = (JSONObject) userArray.get(i);
                String id = rowObject.getString("_id");

                String t = rowObject.getString("role");
                int role;
                if (t.equals("null")) {
                    role = -1;
                } else {
                    role = rowObject.getInt("role");
                }
                String user = rowObject.getString("user");

                // save list user in room into database
                //DatabaseManager.getInstance().addUserToRoom(userRoom);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return room;
	}
}