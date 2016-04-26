package antbuddy.htk.com.antbuddy2016.model;

/**
 * Created by thanhnguyen on 04/04/2016.
 */
public class OpeningChatRoom {
    private String chatRoomKey;
    private String _id;
    private double lastTimeReadMessage;
    private String headNavigatorStatus;
    private String lastReadMessage;
    private Boolean isUnread;
    private Boolean isFavorite;
    private Boolean isMuc;

//    public OpeningChatRoom(JSONObject json) {
//        chatRoomKey = AndroidHelper.getString(json, key_chatRoomKey, null);
//        _id = AndroidHelper.getString(json, key__id, null);
//        lastTimeReadMessage = AndroidHelper.getInt(json, key_lastTimeReadMessage, 0);
//        headNavigatorStatus = AndroidHelper.getString(json, key_headNavigatorStatus, null);
//        lastReadMessage = AndroidHelper.getString(json, key_lastReadMessage, null);
//        isUnread = AndroidHelper.getBoolean(json, key_isUnread, false);
//        isFavorite = AndroidHelper.getBoolean(json, key_isFavorite, false);
//        isMuc = AndroidHelper.getBoolean(json, key_isMuc, false);
//    }

    public void setChatRoomKey(String chatRoomKey) {
        this.chatRoomKey = chatRoomKey;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public double getLastTimeReadMessage() {
        return lastTimeReadMessage;
    }

    public void setLastTimeReadMessage(double lastTimeReadMessage) {
        this.lastTimeReadMessage = lastTimeReadMessage;
    }

    public String getHeadNavigatorStatus() {
        return headNavigatorStatus;
    }

    public void setHeadNavigatorStatus(String headNavigatorStatus) {
        this.headNavigatorStatus = headNavigatorStatus;
    }

    public String getLastReadMessage() {
        return lastReadMessage;
    }

    public void setLastReadMessage(String lastReadMessage) {
        this.lastReadMessage = lastReadMessage;
    }

    public Boolean getIsUnread() {
        return isUnread;
    }

    public void setIsUnread(Boolean isUnread) {
        this.isUnread = isUnread;
    }

    public Boolean getIsFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(Boolean isFavorite) {
        this.isFavorite = isFavorite;
    }

    public void setIsMuc(Boolean isMuc) {
        this.isMuc = isMuc;
    }

//    public static ArrayList<OpeningChatRoom> parse(JSONArray jsonArray) {
//        ArrayList<OpeningChatRoom> openingChatrooms = new ArrayList<>();
//        for(int i=0; i<jsonArray.length(); i++){
//            try {
//                JSONObject json = jsonArray.getJSONObject(i);
//                OpeningChatRoom openingChatroom = new OpeningChatRoom(json);
//                openingChatrooms.add(openingChatroom);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//        return openingChatrooms;
//    }

    public Boolean getIsMuc() {
        return isMuc;
    }

    public String getChatRoomKey() {
        return chatRoomKey;
    }


}
