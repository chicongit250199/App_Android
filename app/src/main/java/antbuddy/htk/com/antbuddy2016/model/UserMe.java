package antbuddy.htk.com.antbuddy2016.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import antbuddy.htk.com.antbuddy2016.util.JSONKey;
import github.ankushsachdeva.emojicon.EmojiconHandler;

/**
 * Created by thanhnguyen on 01/04/2016.
 */
public class UserMe {
    private String key;
    private String chatToken; // 53893e8c497468d3453f6d5a:27f00feeeb03a35e5dd0b44f6ae7c9bebc43a045880c92d0d03e7b5bb05da02e06628b1c07f3a3f3
    private String xmppUsername;
    private String xmppPassword;
    private String chatMucDomain; // conference.htklabs.com
    private String chatDomain; // htklabs.com
    private String _id; // 53893e8c497468d3453f6d5a
    private String name; // Thanh Nguyen
    private String email; // thanh.nguyen@htklabs.com
    private String avatar = null; // avatar url
    private String username; // thanh.nguyen@htklabs.com
    private String provider; // google
    //private List<OpeningChatRoom> listRoomsOpeningChat;

//    public UserInfo() {
//    }
//
//    public UserInfo(Parcel in) {
//        readFromParcel(in);
//    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getChatToken() {
        return chatToken;
    }

    public void setChatToken(String chatToken) {
        this.chatToken = chatToken;
    }

    public String getXmppUsername() {
        return xmppUsername;
    }

    public void setXmppUsername(String xmppUsername) {
        this.xmppUsername = xmppUsername;
    }

    public String getXmppPassword() {
        return xmppPassword;
    }

    public void setXmppPassword(String xmppPassword) {
        this.xmppPassword = xmppPassword;
    }

    public String getChatMucDomain() {
        return chatMucDomain;
    }

    public void setChatMucDomain(String chatMucDomain) {
        this.chatMucDomain = chatMucDomain;
    }

    public String getChatDomain() {
        return chatDomain;
    }

    public void setChatDomain(String chatDomain) {
        this.chatDomain = chatDomain;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        if(!TextUtils.isEmpty(name)) {
            EmojiconHandler.myName = "@" + name.replace(" ", "");
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

//    public List<OpeningChatRoom> getListRoomsOpeningChat() {
//        return listRoomsOpeningChat;
//    }
//
//    public void setListRoomsOpeningChat(List<OpeningChatRoom> listRoomsOpeningChat) {
//        this.listRoomsOpeningChat = listRoomsOpeningChat;
//    }
//
//    public static UserInfo parseUser(JSONObject object) throws JSONException {
//        UserInfo userInfo = new UserInfo();
//        userInfo.setChatToken(object.getString("chatToken"));
//        List<String> xmppAccount = Arrays.asList(Pattern.compile(":").split(userInfo.getChatToken()));
//        userInfo.setXmppUsername(xmppAccount.get(0));
//        userInfo.setXmppPassword(xmppAccount.get(1));
//        userInfo.setChatMucDomain(object.getString("chatMucDomain"));
//        userInfo.setChatDomain(object.getString("chatDomain"));
//        userInfo.set_id(object.getString("_id"));
//        userInfo.setName(object.getString("name"));
//        userInfo.setEmail(object.getString("email"));
//        if(object.has("avatar")) {
//            String s_avatar = object.getString("avatar").replaceAll(" ", "%20");
//            userInfo.setAvatar(s_avatar);
//        }
//        userInfo.setUsername(object.getString("username"));
//        userInfo.setProvider(object.getString("google"));
//
//        List<OpeningChatRoom> openingChatRoom = new ArrayList<OpeningChatRoom>();
//        JSONArray openingChatroomArray = object.getJSONArray("openingChatrooms");
//        for (int i = 0; i < openingChatroomArray.length(); i++) {
//            try {
//                OpeningChatRoom chatRoom = OpeningChatRoom.parse((JSONObject) openingChatroomArray.get(i));
//                openingChatRoom.add(chatRoom);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        userInfo.setListRoomsOpeningChat(openingChatRoom);
//        return userInfo;
//    }
//
//    @Override
//    public int describeContents() {
//        // TODO Auto-generated method stub
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel outParcel, int flags) {
//        outParcel.writeString(chatToken);
//        outParcel.writeString(xmppUsername);
//        outParcel.writeString(xmppPassword);
//        outParcel.writeString(chatMucDomain);
//        outParcel.writeString(chatDomain);
//        outParcel.writeString(_id);
//        outParcel.writeString(name);
//        outParcel.writeString(email);
//        outParcel.writeString(avatar);
//        outParcel.writeString(username);
//        outParcel.writeString(provider);
//        outParcel.writeTypedList(listRoomsOpeningChat);
//    }
//
//    private void readFromParcel(Parcel in) {
//        chatToken = in.readString();
//        xmppUsername = in.readString();
//        xmppPassword = in.readString();
//        chatMucDomain = in.readString();
//        chatDomain = in.readString();
//        _id = in.readString();
//        name = in.readString();
//        email = in.readString();
//        avatar = in.readString();
//        username = in.readString();
//        provider = in.readString();
//        if(listRoomsOpeningChat == null) {
//            listRoomsOpeningChat = new ArrayList<OpeningChatRoom>();
//        }
//        in.readTypedList(listRoomsOpeningChat, OpeningChatRoom.CREATOR);
//    }
//
//    public static final Parcelable.Creator<UserInfo> CREATOR = new Parcelable.Creator<UserInfo>() {
//
//        @Override
//        public UserInfo createFromParcel(Parcel in) {
//            return new UserInfo(in);
//        }
//
//        @Override
//        public UserInfo[] newArray(int size) {
//            return new UserInfo[size];
//        }
//    };

    public static UserMe parse(JSONObject object) throws JSONException {
        UserMe me = new UserMe();
        me.setKey(object.getString(JSONKey.key));
        me.setName(object.getString(JSONKey.name));
        me.setEmail(object.getString(JSONKey.email));
        me.setUsername(object.getString(JSONKey.username));
        me.setAvatar(object.getString(JSONKey.avatar));
        return me;
    }
}
