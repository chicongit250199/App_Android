package antbuddy.htk.com.antbuddy2016.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        outParcel.writeString(senderJid);
        outParcel.writeString(senderId);
        outParcel.writeString(senderName);
        outParcel.writeString(receiverJid);
        outParcel.writeString(receiverId);
        outParcel.writeString(receiverName);
        outParcel.writeByte((byte) (isModified ? 1 : 0));
        outParcel.writeString(body);
        outParcel.writeString(fromId);
        outParcel.writeString(type);
        outParcel.writeString(subtype);
        outParcel.writeLong(getDatetime());
        outParcel.writeString(expandBody);
        outParcel.writeParcelable(getFileAntBuddy(), paramInt);
    }

    public static final Creator<ChatMessage> CREATOR = new Creator<ChatMessage>() {

        @Override
        public ChatMessage createFromParcel(Parcel in) {
            String idMessage = in.readString();
            String jidSender = in.readString();
            String idSender = in.readString();
            String sender = in.readString();
            String jidReceiver = in.readString();
            String idReceiver = in.readString();
            String receiver = in.readString();
            Boolean isSender = in.readByte() != 0;
            String message = in.readString();
            String fromId = in.readString();
            String type = in.readString();
            String subtype = in.readString();
            long datetime = in.readLong();
            String expandBody = in.readString();
            FileAntBuddy fileAntBuddy = (FileAntBuddy)in.readParcelable(FileAntBuddy.class.getClassLoader());

            ChatMessage chatMessage = new ChatMessage(idMessage, jidSender, idSender, sender, jidReceiver, idReceiver, receiver, fromId, isSender, message, type, subtype, datetime);
            chatMessage.setExpandBody(expandBody);
            chatMessage.setFileAntBuddy(fileAntBuddy);
            return chatMessage;
        }

        @Override
        public ChatMessage[] newArray(int size) {
            return new ChatMessage[size];
        }
    };


    /*
    public static ChatMessage messageToChatMessage(Message message, List<User> listUser, UserInfo mUserInfo) {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setId(message.getPacketID());
        String senderId = Pattern.compile("@").split(message.getFrom())[0];
        chatMessage.setSenderId(senderId);
        chatMessage.setSenderJid(message.getFrom());

        if(message.getWith() != null && senderId.equals(mUserInfo.get_id())) {
            chatMessage.setSenderName(mUserInfo.getName());
            chatMessage.setReceiverJid(message.getWith() + "@" + AntbuddyXmppConnection.DOMAIN);
            chatMessage.setReceiverId(message.getWith());

            for (User user : listUser) {
                if (user.get_id().equals(message.getWith())) {
                    chatMessage.setReceiverName(user.getName());
                    break;
                }
            }

        } else {
            String from = senderId;
            if(message.getType() == Message.Type.groupchat) {
                from = message.getFrom().split("/")[1];
                //chatMessage.setFromId(from);
                chatMessage.setSenderId(from);
                chatMessage.setFromId(senderId);
            }

            for (User user : listUser) {
                if (user.get_id().equals(from)) {
                    chatMessage.setSenderName(user.getName());
                    break;
                }
            }

            chatMessage.setReceiverJid(message.getTo());
            chatMessage.setReceiverId(Pattern.compile("@").split(message.getTo())[0]);
            chatMessage.setReceiverName(mUserInfo.getName());
        }
        if(message.getFile() != null) {
            chatMessage.setFileAntBuddy(new FileAntBuddy(message.getFile().getName(), message.getFile().getSize(), message.getFile().getFileUrl(), message.getFile().getMimeType(), message.getFile().getThumbnailUrl()));
            chatMessage.setBody(message.getFile().getName() + " " + message.getFile().getSize() + " KB");
        } else {
            chatMessage.setBody(message.getBody());
        }
        // server time delay 4 min from client
        chatMessage.setDatetime(NationalTime.getlongTime());
        chatMessage.setType(message.getType().toString());
        if(message.getSubtype() != null) {
            chatMessage.setSubType(message.getSubtype().toString());
        } else {
            chatMessage.setSubType("null");
        }
        return chatMessage;
    }
    */

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

    public FileAntBuddy getFileAntBuddy() {
        FileAntBuddy fileAntBuddy = null;

        if(!TextUtils.isEmpty(file_name))
            fileAntBuddy = new FileAntBuddy(file_name,file_size,file_Url,file_mimeType,file_thumbnailUrl);

        return fileAntBuddy;
    }

    public void setFileAntBuddy(FileAntBuddy fileAntBuddy) {
        if(fileAntBuddy == null)
            return;
        this.file_name = fileAntBuddy.getName();
        this.file_size = fileAntBuddy.getSize();
        this.file_Url = fileAntBuddy.getFileUrl();
        this.file_mimeType = fileAntBuddy.getMimeType();
        this.file_thumbnailUrl = fileAntBuddy.getThumbnailUrl();
    }

    public String getFromId() {
        return fromId;
    }

    public void setFromId(String fromId) {
        this.fromId = fromId;
    }

    public String getReceiverJid() {
        return receiverJid;
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

    public long getDatetime() {
        return Long.parseLong(s_datetime);
    }

    public void setDatetime(long datetime) {
        this.s_datetime = ""+datetime;
    }

    public String getExpandBody() {
        return expandBody;
    }

    private static Pattern maskGitlab  = Pattern.compile("\\[([^\\|]+)\\|+([^\\|]+)\\](.*)");

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

}
