package antbuddy.htk.com.antbuddy2016.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import org.jivesoftware.smack.packet.Message;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import antbuddy.htk.com.antbuddy2016.util.AndroidHelper;
import antbuddy.htk.com.antbuddy2016.util.NationalTime;

/**
 * Created by Micky on 4/1/2016.
 */
public class ChatMessage implements Parcelable {
    public static String TAG = "ChatMessage";
    private String idMessage;
    private String senderJid;
    private String senderId;
    private String senderName;
    private String body; // ban dang lam gi do?
    private String fromId; // id of user or room
    private String receiverJid;
    private String receiverId;
    private String receiverName; // PianoCover MucKho
    private boolean isModified; // true
    private String type; // chat or groupchat
    private String subtype; // adduser or remove
    private String s_datetime; // 08:46 AM
    private String expandBody;

    /***************************/
    private String file_name;           //google_play_services.zip
    private int file_size;		   //16223844
    private String file_Url;        //http://s3-ap-southeast-1.amazonaws.com/hipchat/google_play_services1415098114018_google_play_services.zip
    private String file_mimeType;       //application/octet-stream
    private String file_thumbnailUrl;


    /***************************/
    private final static  String key_org = "org";
    private final static String key_senderKey = "senderKey";
    private final static String key_body = "body";
    private final static String key_fromKey = "fromKey";
    private final static String key_receiverKey = "receiverKey";
    private final static String key_id = "id";
    private final static String key_v = "__v";
    private final static String key_isModified = "isModified";
    private final static String key_subtype = "subtype";
    private final static String key_type = "type";
    private final static String key_time = "time";
    private final static String key_file = "file";


    public final static String info_lastTime = "info_lastTime";


    /*******API***************/
    private String org;
    private String senderKey;
    private String fromKey;
    private String receiverKey;
    private FileAntBuddy fileAntBuddy;

    /***************************/
    public enum TYPE {
        chat, groupchat
    }

    public ChatMessage() {
    }

    public ChatMessage(String idMessage, String senderJid, String senderId, String senderName, String receiverJid, String receiverId, String receiverName, String fromId, boolean isModified, String body, String type, String subtype, long datetime) {
        this.idMessage = idMessage;
        this.senderJid = senderJid;
        this.senderId = senderId;
        this.senderName = senderName;
        this.receiverJid = receiverJid;
        this.receiverId = receiverId;
        this.receiverName = receiverName;
        this.fromId = fromId;
        this.isModified = isModified;
        this.body = body;
        this.type = type;
        this.subtype = subtype;
        this.s_datetime = "" +datetime;
    }

    public ChatMessage(String senderName, String receiverName, boolean isModified, String body, long datetime) {
        this.senderName = senderName;
        this.receiverName = receiverName;
        this.isModified = isModified;
        this.body = body;
        this.s_datetime = "" + datetime;
    }

    public String getSenderKey() {
        return senderKey;
    }

    public String getFromKey() {
        return fromKey;
    }

    public String getSubtype() {
        return subtype;
    }

//    public static List<ChatMessage> parseListHistoryMessages(JSONArray arrayJsonObjects, List<User> listUser) throws JSONException {
//        List<ChatMessage> listChatMessageHistory = new ArrayList<ChatMessage>();
//
//        for (int i = 0; i < arrayJsonObjects.length(); i++) {
//            JSONObject elementObject = arrayJsonObjects.getJSONObject(i);
//
//            ChatMessage chatMessage = new ChatMessage();
//            chatMessage.setId(elementObject.getString("id"));
//            chatMessage.setSenderId(elementObject.getString("senderId"));
//            chatMessage.setSenderJid(elementObject.getString("senderId").toString() + "@htklabs.com");
//            chatMessage.setSenderName("");
//            chatMessage.setFromId(elementObject.getString("fromId"));
//            for (User user : listUser) {
//                if (user.get_id().equals(chatMessage.getSenderId())) {
//                    chatMessage.setSenderName(user.getName());
//                    break;
//                }
//            }
//
//            chatMessage.setBody(elementObject.getString("body"));
//
//            if (elementObject.has("expandBody")) {
//                chatMessage.setExpandBody(elementObject.getString("expandBody").toString());
//            }
//
//            if (elementObject.has("file") == false) {
//                chatMessage.setFileAntBuddy(null);
//            } else {
//                try {
//                    JSONArray fileObject = elementObject.getJSONArray("file");
//                    chatMessage.setFileAntBuddy(new FileAntBuddy(fileObject.getJSONObject(0).getString("name"), fileObject.getJSONObject(0).getInt("size"), fileObject.getJSONObject(0).getString("fileUrl"), fileObject.getJSONObject(0).getString("mimeType"), fileObject.getJSONObject(0).getString("thumbnailUrl")));
//                } catch (JSONException e) {
//                    // TODO: file can be return object
//                    try {
//                        JSONObject fileObject = elementObject.getJSONObject("file");
//                        chatMessage.setFileAntBuddy(new FileAntBuddy(fileObject.getString("name"), fileObject.getInt("size"), fileObject.getString("fileUrl"), fileObject.getString("mimeType"), fileObject.getString("thumbnailUrl")));
//                    } catch (Exception e1) {
//                        e1.printStackTrace();
//                    }
//                }
//            }
//
//            if(elementObject.has("receiverId")) {
//                chatMessage.setReceiverId(elementObject.getString("receiverId"));
//                chatMessage.setReceiverJid(elementObject.getString("receiverId").toString() + "@htklabs.com");
//                chatMessage.setReceiverName(""); // process later
//            }
//
//            chatMessage.setModified(elementObject.getBoolean("isModified"));
//            chatMessage.setType(elementObject.getString("type"));
//            if(chatMessage.getType().equals("chat")) {
//                chatMessage.setFromId(null);
//            }
//            chatMessage.setSubType(elementObject.getString("subtype"));
//
//            String utcTime = elementObject.getString("time");
//            chatMessage.setDatetime(NationalTime.convertUTCTimeToCurrentTimeDateInLong(utcTime));
//
//            listChatMessageHistory.add(0,chatMessage);
//        }
//        return listChatMessageHistory;
//    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel outParcel, int paramInt) {
        outParcel.writeString(idMessage);
        outParcel.writeString(org);
        outParcel.writeString(senderKey);
        outParcel.writeString(body);
        outParcel.writeString(fromKey);
        outParcel.writeString(receiverKey);
        outParcel.writeByte((byte) (isModified ? 1 : 0));
        outParcel.writeString(subtype);
        outParcel.writeString(type);
        outParcel.writeString(s_datetime);
        outParcel.writeParcelable(fileAntBuddy, paramInt);
    }

    public static final Creator<ChatMessage> CREATOR = new Creator<ChatMessage>() {

        @Override
        public ChatMessage createFromParcel(Parcel in) {
            return new ChatMessage(in);
        }

        @Override
        public ChatMessage[] newArray(int size) {
            return new ChatMessage[size];
        }
    };






    public String getId() {
        return idMessage;
    }

    public void setId(String idMessage) {
        this.idMessage = idMessage;
    }

    // gets and sets function
    public String getSenderJid() {
        return senderJid;
    }

    public void setSenderJid(String senderJid) {
        this.senderJid = senderJid;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getFromId() {
        return fromId;
    }

    public void setFromId(String fromId) {
        this.fromId = fromId;
    }

    public void setReceiverJid(String receiverJid) {
        this.receiverJid = receiverJid;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public boolean isModified() {
        return isModified;
    }

    public void setModified(boolean isModified) {
        this.isModified = isModified;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubType() {
        return subtype;
    }

    public String setSubType(String subtype) {
        return this.subtype = subtype;
    }

    public String getDatetime() {
        return s_datetime;
    }

    public String getReceiverKey() {
        return receiverKey;
    }

    public String getExpandBody() {
        return expandBody;
    }

    private static Pattern maskGitlab  = Pattern.compile("\\[([^\\|]+)\\|+([^\\|]+)\\](.*)");

    public FileAntBuddy getFileAntBuddy() {
        return fileAntBuddy;
    }

    public void setExpandBody(String expandBody)
    {
        if(expandBody == null) return;
        Matcher matcher = maskGitlab.matcher(expandBody);
        if(matcher.find())
        {
            Matcher m = maskGitlab.matcher(expandBody);
            while (m.find()) {
                if(TextUtils.isEmpty(this.expandBody))
                    this.expandBody = "<a href=\"" +m.group(1)+"\">"+m.group(2)+"</a>"+m.group(3);
                else
                    this.expandBody =this.expandBody+"<br></br><a href=\""+m.group(1)+"\">"+m.group(2)+"</a>"+m.group(3);
            }
        }
        else
            this.expandBody = expandBody;
    }

    public static ArrayList<ChatMessage> parseArray(JSONArray jsonArray, HashMap<String,String> info) {
        if (info == null) {
            info = new HashMap<>();
        }
        String lastTime = "";
        ArrayList<ChatMessage> messages = new ArrayList<>();
        for(int i=0; i<jsonArray.length(); i++){
            try {
                JSONObject json = jsonArray.getJSONObject(i);
                String idMessage = AndroidHelper.getString(json, key_id, null);
                String s_datetime =  AndroidHelper.getString(json, key_time, null);
                if(!TextUtils.isEmpty(idMessage)) {
                    ChatMessage chatMessage = new ChatMessage(json);
                    messages.add(chatMessage);
                    //get last time in list
                    lastTime = s_datetime;

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Collections.sort(messages, new MessageComparator());
        info.put(info_lastTime, lastTime);
        return messages;
    }

    public ChatMessage(JSONObject json) {
        idMessage = AndroidHelper.getString(json, key_id, null);
        org = AndroidHelper.getString(json, key_org, null);
        senderKey = AndroidHelper.getString(json, key_senderKey, null);
        body = AndroidHelper.getString(json, key_body, null);
        fromKey = AndroidHelper.getString(json, key_fromKey, null);
        receiverKey = AndroidHelper.getString(json, key_receiverKey, null);
        isModified = AndroidHelper.getBoolean(json, key_isModified, false);
        subtype = AndroidHelper.getString(json, key_subtype, null);
        type = AndroidHelper.getString(json, key_type, null);
        s_datetime =  AndroidHelper.getString(json, key_time, null);
        if (json.has(key_file)) {
            try {
                fileAntBuddy = new FileAntBuddy(json.getJSONObject(key_file));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public ChatMessage(Parcel in) {
        idMessage = in.readString();
        org = in.readString();
        senderKey = in.readString();
        body = in.readString();
        fromKey = in.readString();
        receiverKey = in.readString();
        isModified = in.readByte() != 0;
        subtype = in.readString();
        type = in.readString();
        s_datetime = in.readString();
        fileAntBuddy = (FileAntBuddy)in.readParcelable(FileAntBuddy.class.getClassLoader());
    }
    public ChatMessage(Message message) {
        idMessage = message.getPacketID();
        Matcher m = Pattern.compile("([^_]+)_([^_]+)(@[^//]+)/*").matcher(message.getTo());
        while (m.find()) {
            org = m.group(2);
            receiverKey = m.group(1);
        }

        String params[] = message.getFrom().split("/");
        UserMe userMe = ObjectManager.getInstance().getUserMe();
        if (params[0].endsWith(userMe.getChatMucDomain())){
            fromKey =  params[0].split("_")[0];
            senderKey = params[1].split("_")[0];
        } else {
            senderKey = fromKey =  params[0].split("_")[0];
        }

        if (senderKey.endsWith(receiverKey) && !TextUtils.isEmpty(message.getWith())) {
            fromKey = message.getWith();
        }

        if(message.getFile() != null) {
            fileAntBuddy = new FileAntBuddy(message.getFile());
            body = message.getFile().getName() + " " + message.getFile().getSize() + " KB";
        } else {
            try {
                body = new String( message.getBody().getBytes(), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                body = message.getBody();
                e.printStackTrace();
            }
        }

        type = message.getType().toString();

        if(message.getSubtype() != null) {
            subtype = message.getSubtype().toString();
        } else {
            subtype = null;
        }
        s_datetime = NationalTime.getLocalTimeToUTCTime();
        isModified = message.getExtension("replace", "urn:xmpp:message-correct:0") != null;
    }

    public ChatMessage(String receiverKey, String body, Boolean isMuc) {
        UserMe userMe = ObjectManager.getInstance().getUserMe();
        if (userMe == null) {
            return;
        }

        UserMe.Org currentOrg = userMe.getCurrentOrg();
        if (currentOrg == null) {
            return;
        }
        type = (isMuc? "groupchat": "chat");
        org = currentOrg.getOrgKey();
        senderKey = userMe.getKey();
        this.receiverKey = receiverKey;
        if (isMuc) {
            fromKey = receiverKey;
        } else {
            fromKey = senderKey;
        }
        this.body = body;
    }

    public static class MessageComparator implements Comparator<ChatMessage>
    {
        public int compare(ChatMessage left, ChatMessage right) {
            return left.getDatetime().compareTo(right.getDatetime());
        }
    }
}
