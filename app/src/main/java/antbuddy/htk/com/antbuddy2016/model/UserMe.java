package antbuddy.htk.com.antbuddy2016.model;

import android.text.TextUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import antbuddy.htk.com.antbuddy2016.util.LogHtk;
import github.ankushsachdeva.emojicon.EmojiconHandler;

/**
 * Created by thanhnguyen on 01/04/2016.
 */
public class UserMe {

    //    private final static String key__id = "_id";
//    private final static String key_avatar = "avatar";
//    private final static String key_username = "username";
//    private final static String key_key = "key";
//    private final static String key_provider = "provider";
//    private final static String key_email = "email";
//    private final static String key_name = "name";
//    private final static String key_nonce = "nonce";
//    private final static String key_modified = "modified";
//    private final static String key_active = "active";
//    private final static String key_tourStep = "tourStep";
//    private final static String key_orgs = "orgs";
//    private final static String key_forceChangePassw = "forceChangePassw";
//    private final static String key_created = "created";
//    private final static String key_chatDomain = "chatDomain";
//    private final static String key_chatMucDomain = "chatMucDomain";
//    private final static String key_chatUrl = "chatUrl";
//    private final static String key_currentOrg = "currentOrg";
//    private final static String key_chatToken = "chatToken";
    private String nonce;
    private String modified;
    private Boolean active;
    private int tourStep;
    private ArrayList<Org> orgs;
    private Boolean forceChangePassw;
    private String created;
    private CurrentOrg currentOrg;


    private String key;
    private String chatToken; // 53893e8c497468d3453f6d5a:27f00feeeb03a35e5dd0b44f6ae7c9bebc43a045880c92d0d03e7b5bb05da02e06628b1c07f3a3f3
    private String chatMucDomain; // conference.htklabs.com
    private String chatDomain; // htklabs.com
    private String _id; // 53893e8c497468d3453f6d5a
    private String name; // Thanh Nguyen
    private String email; // thanh.nguyen@htklabs.com
    private String avatar = null; // avatar url
    private String username; // thanh.nguyen@htklabs.com
    private String provider; // google
    private String chatUrl;

    public String getChatUrl() {
        return chatUrl;
    }

    public void setChatUrl(String chatUrl) {
        this.chatUrl = chatUrl;
    }

    public List<OpeningChatRoom> getOpeningChatrooms() {
        if (currentOrg == null) {
            LogHtk.i(LogHtk.UserMe, "Error! currentOrg is null in UserMe!");
            return null;
        }
        String key = currentOrg.getKey();
        for (Org org : orgs) {
            if (org.getOrgKey().equals(key)) {
                return org.getOpeningChatrooms();
            }
        }
        return null;
    }

    public String getKey() {
        return key;
    }

    public Org getFullCurrentOrg() {
        String key_org = currentOrg.getKey();
        for (Org org : orgs) {
            if (org.getOrgKey().equals(key_org)) {
                return org;
            }
        }

        return null;
    }

    public UserMe.CurrentOrg getCurrentOrg() {
        return currentOrg;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public int getTourStep() {
        return tourStep;
    }

    public void setTourStep(int tourStep) {
        this.tourStep = tourStep;
    }

    public ArrayList<Org> getOrgs() {
        return orgs;
    }

    public void setOrgs(ArrayList<Org> orgs) {
        this.orgs = orgs;
    }

    public Boolean getForceChangePassw() {
        return forceChangePassw;
    }

    public void setForceChangePassw(Boolean forceChangePassw) {
        this.forceChangePassw = forceChangePassw;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public void setCurrentOrg(CurrentOrg currentOrg) {
        this.currentOrg = currentOrg;
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
        if (!TextUtils.isEmpty(name)) {
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

//    public static UserMe parse(JSONObject object) throws JSONException {
//        UserMe me = new UserMe();
//        me.setKey(object.getString(JSONKey.key));
//        me.setName(object.getString(JSONKey.name));
//        me.setEmail(object.getString(JSONKey.email));
//        me.setUsername(object.getString(JSONKey.username));
//        me.setAvatar(object.getString(JSONKey.avatar));
//        return me;
//    }

//    public UserMe(JSONObject json){
//        new Exception().printStackTrace();
//        _id = AndroidHelper.getString(json, key__id, null);
//        avatar = AndroidHelper.getString(json, key_avatar, null);
//        username = AndroidHelper.getString(json, key_username, null);
//        key = AndroidHelper.getString(json, key_key, null);
//        provider = AndroidHelper.getString(json, key_provider, null);
//        email = AndroidHelper.getString(json, key_email, null);
//        name = AndroidHelper.getString(json, key_name, null);
//        nonce = AndroidHelper.getString(json, key_nonce, null);
//        modified = AndroidHelper.getString(json, key_modified, null);
//        active = AndroidHelper.getBoolean(json, key_active, false);
//        tourStep = AndroidHelper.getInt(json, key_tourStep, 0);
//        if (json.has(key_orgs)) {
//            try {
//                orgs = Org.parse(json.getJSONArray(key_orgs));
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//        forceChangePassw = AndroidHelper.getBoolean(json, key_forceChangePassw, false);
//        created = AndroidHelper.getString(json, key_created, null);
//        chatDomain = AndroidHelper.getString(json, key_chatDomain, null);
//        chatMucDomain = AndroidHelper.getString(json, key_chatMucDomain, null);
//        chatUrl = AndroidHelper.getString(json, key_chatUrl, null);
//        if (json.has(key_currentOrg)) {
//            try {
//                currentOrg = new CurrentOrg(json.getJSONObject(key_currentOrg));
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//        chatToken = AndroidHelper.getString(json, key_chatToken, null);
//    }

    public static class Org {
        private String orgKey;
        private String role;
        private String _id;
        private ArrayList<OpeningChatRoom> openingChatrooms = null;

        public String getOrgKey() {
            return orgKey;
        }

        public void setOrgKey(String orgKey) {
            this.orgKey = orgKey;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public ArrayList<OpeningChatRoom> getOpeningChatrooms() {
            return openingChatrooms;
        }

        public void setOpeningChatrooms(ArrayList<OpeningChatRoom> openingChatrooms) {
            this.openingChatrooms = openingChatrooms;
        }
    }

    public class CurrentOrg {
        private String _id;
        private String name;
        private String domain;
        private String key;
        private String createdBy;
        private Boolean isDefaultLogo;
        private Boolean allowSelfRegister;
        private String logo;
        private String status;
        private String modified;
        private String created;

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
        }

        public String getDomain() {
            return domain;
        }

        public void setDomain(String domain) {
            this.domain = domain;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getCreatedBy() {
            return createdBy;
        }

        public void setCreatedBy(String createdBy) {
            this.createdBy = createdBy;
        }

        public Boolean getIsDefaultLogo() {
            return isDefaultLogo;
        }

        public void setIsDefaultLogo(Boolean isDefaultLogo) {
            this.isDefaultLogo = isDefaultLogo;
        }

        public Boolean getAllowSelfRegister() {
            return allowSelfRegister;
        }

        public void setAllowSelfRegister(Boolean allowSelfRegister) {
            this.allowSelfRegister = allowSelfRegister;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getModified() {
            return modified;
        }

        public void setModified(String modified) {
            this.modified = modified;
        }

        public String getCreated() {
            return created;
        }

        public void setCreated(String created) {
            this.created = created;
        }

        public String getKey() {
            return key;
        }
    }

    public static List<OpeningChatRoom> getChatsOpening(UserMe me, boolean isGroup) {
        List<OpeningChatRoom> listChatsOpening = new ArrayList<>();
        if (me != null && me.getOpeningChatrooms() != null) {
            for (OpeningChatRoom openingChatroom : me.getOpeningChatrooms()) {
                if (isGroup) { // Room
                    if (openingChatroom.getIsMuc()) {
                        listChatsOpening.add(openingChatroom);
                    }
                } else {
                    if (!openingChatroom.getIsMuc()) {
                        listChatsOpening.add(openingChatroom);
                    }
                }
            }
        }
        return listChatsOpening;
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
