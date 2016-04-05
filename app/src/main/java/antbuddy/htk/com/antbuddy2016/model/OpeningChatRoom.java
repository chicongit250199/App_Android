package antbuddy.htk.com.antbuddy2016.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import antbuddy.htk.com.antbuddy2016.util.AndroidHelper;

/**
 * Created by thanhnguyen on 04/04/2016.
 */
public class OpeningChatRoom {
    private final static String key_chatRoomKey = "chatRoomKey";
    private final static String key__id = "_id";
    private final static String key_lastTimeReadMessage = "lastTimeReadMessage";
    private final static String key_headNavigatorStatus = "headNavigatorStatus";
    private final static String key_lastReadMessage = "lastReadMessage";
    private final static String key_isUnread = "isUnread";
    private final static String key_isFavorite = "isFavorite";
    private final static String key_isMuc = "isMuc";
    private String chatRoomKey;
    private String _id;
    private double lastTimeReadMessage;
    private String headNavigatorStatus;
    private String lastReadMessage;
    private Boolean isUnread;
    private Boolean isFavorite;
    private Boolean isMuc;

    public OpeningChatRoom(JSONObject json) {
        chatRoomKey = AndroidHelper.getString(json, key_chatRoomKey, null);
        _id = AndroidHelper.getString(json, key__id, null);
        lastTimeReadMessage = AndroidHelper.getInt(json, key_lastTimeReadMessage, 0);
        headNavigatorStatus = AndroidHelper.getString(json, key_headNavigatorStatus, null);
        lastReadMessage = AndroidHelper.getString(json, key_lastReadMessage, null);
        isUnread = AndroidHelper.getBoolean(json, key_isUnread, false);
        isFavorite = AndroidHelper.getBoolean(json, key_isFavorite, false);
        isMuc = AndroidHelper.getBoolean(json, key_isMuc, false);
    }

    public static ArrayList<OpeningChatRoom> parse(JSONArray jsonArray) {
        ArrayList<OpeningChatRoom> openingChatrooms = new ArrayList<>();
        for(int i=0; i<jsonArray.length(); i++){
            try {
                JSONObject json = jsonArray.getJSONObject(i);
                OpeningChatRoom openingChatroom = new OpeningChatRoom(json);
                openingChatrooms.add(openingChatroom);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return openingChatrooms;
    }

    public Boolean getIsMuc() {
        return isMuc;
    }

    public String getChatRoomKey() {
        return chatRoomKey;
    }


}
