package antbuddy.htk.com.antbuddy2016.RealmObjects;


import org.jivesoftware.smack.packet.AntBuddyFile;
import org.jivesoftware.smack.packet.Message;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import antbuddy.htk.com.antbuddy2016.model.FileAntBuddy;
import antbuddy.htk.com.antbuddy2016.setting.ABSharedPreference;
import antbuddy.htk.com.antbuddy2016.util.LogHtk;
import antbuddy.htk.com.antbuddy2016.util.NationalTime;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by thanhnguyen on 12/04/2016.
 */

//{
//        "_id": "570b4b57bacf54267ff6649e",
//        "org": "56c536387480fcb75f115556",
//        "senderKey": "b90d4680-9d8e-11e5-a30e-75f0dda6207b",
//        "body": "http://www.ratatype.com/",
//        "fromKey": "fa787770-d61e-11e5-b8c2-f576aa388022",
//        "receiverKey": "",
//        "id": "b90d4680-9d8e-11e5-a30e-75f0dda6207bbe45753b-5c9a-67ca-4754-5262b60159de",
//        "__v": 0,
//        "oembed": {
//                    "provider_url": "http://www.ratatype.com",
//                    "description": "Learn to type faster with Ratatype. Take touch typing lessons, practice your keyboarding skills online, take a typing test and get typing speed certificate for free.",
//                    "title": "Ratatype - Online Typing Tutor and Typing Lessons",
//                    "thumbnail_width": 200,
//                    "url": "http://www.ratatype.com/",
//                    "thumbnail_url": "http://www.ratatype.com/static/i/open-graph-logo.png",
//                    "version": "1.0",
//                    "provider_name": "Ratatype",
//                    "type": "link",
//                    "thumbnail_height": 200,
//                    "author_name": "",
//                    "author_url": "",
//                    "html": ""
//                       },
//        "isModified": false,
//        "subtype": "",
//        "type": "groupchat",
//        "time": "2016-04-11T06:59:35.412Z"
//        }

//    Chat Message have file
//{
//        "_id": "570c9ed8c66345d430e4b2a4",
//        "org": "56c536387480fcb75f115556",
//        "senderKey": "756651f0-9196-11e5-a569-fdc7cdc19515",
//        "body": "File uploaded: https://abs1.antbuddy.com/antbuddy-bucket/1460444887543_device20160412140855.png",
//        "file": {
//        "name": "device-2016-04-12-140855.png",
//        "size": 225369,
//        "fileUrl": "https://abs1.antbuddy.com/antbuddy-bucket/1460444887543_device20160412140855.png",
//        "mimeType": "image/png",
//        "thumbnailUrl": "https://abs1.antbuddy.com/antbuddy-bucket/thumb_1460444887543_device20160412140855.png",
//        "thumbnailWidth": 128,
//        "thumbnailHeight": 228
//        },
//        "bonusImg": null,
//        "fromKey": "a667b2a0-d636-11e5-9bc1-25a4e1ac232c",
//        "receiverKey": "",
//        "id": "756651f0-9196-11e5-a569-fdc7cdc1951591b3fcda-b3f8-2e00-0e42-b590db57ed8f",
//        "__v": 0,
//        "isModified": false,
//        "subtype": "",
//        "type": "groupchat",
//        "time": "2016-04-12T07:08:08.969Z"
//        }

    // Chat message have Boss
//{
//        "_id": "570cb608c66345d430e4b381",
//        "org": "56c536387480fcb75f115556",
//        "body": "[antbuddy-ios:gossip_module] 1 new commit by Long Vo Hoang",
//        "fromKey": "a667b2a0-d636-11e5-9bc1-25a4e1ac232c",
//        "id": "6f352e60-84ed-11e5-b53a-2d9474960e70218d6eb0-008b-11e6-b557-efb90d40e819",
//        "receiverKey": null,
//        "senderKey": "6f352e60-84ed-11e5-b53a-2d9474960e70",
//        "expandBody": "[https://repo.htk.me/htk-inc/antbuddy-ios/commit/38dd1a9fa1ee202f11a75c8c7482b271f92136d5|38dd1a]: Implement message button in call - Trung Tran\n",
//        "__v": 0,
//        "isModified": false,
//        "subtype": "bot-gitlab",
//        "type": "groupchat",
//        "time": "2016-04-12T08:47:04.475Z"
//        },

public class RChatMessage extends RealmObject {

    @PrimaryKey
    private String id;

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
    private String time; // 08:46 AM
    private String expandBody;
    private String org;
    private String senderKey;
    private String fromKey;
    private String receiverKey;
    private RFileAntBuddy fileAntBuddy;

    public RChatMessage() {
        super();
    }

    public RChatMessage(final Message message) {
        id = message.getPacketID();

        // XMPP Message Info
        String to         = message.getTo();
        String from       = message.getFrom();
        String with       = message.getWith();
        String mBody      = message.getBody();
        String subType    = message.getSubtype();
        String mType      = message.getType().toString();
        AntBuddyFile file = message.getFile();

        // Prepare
        Matcher m = Pattern.compile("([^_]+)_([^_]+)(@[^//]+)/*").matcher(to);
        String group1 = "", group2 = "";
        while (m.find()) {
            group1 = m.group(1);
            group2 = m.group(2);
        }
        String fromSplit[] = from.split("/");
        if (from.contains("/")) {
            fromSplit = from.split("/");
        }

        // Group
        if (mType != null  && mType.equals("groupchat")) {
            // to (XMPP message) ~ receiverKey_org (ChatMessage)
            receiverKey = group1;
            org = group2;
            fromKey   = fromSplit[0].split("_")[0];

            if (fromSplit.length > 1) {
                senderKey = fromSplit[1].split("_")[0];
            }

        } else {    //  1-1
            org = group2;
            receiverKey = with; // with ~ receiverKey
            senderKey = fromKey =  fromSplit[0].split("_")[0];

            LogHtk.i(LogHtk.Test1, "1");
            // Myself
            if (group1.equals(with) && fromKey.equals(ABSharedPreference.get(ABSharedPreference.KEY_XMPP_DOMAIN))) {
                LogHtk.i(LogHtk.Test1, "2");
                senderKey = with;
                fromKey = with;
                receiverKey = with;
            }

            LogHtk.i(LogHtk.Test1, "3");
            // 1-1
            if (fromKey.equals(ABSharedPreference.get(ABSharedPreference.KEY_XMPP_DOMAIN))) {
                LogHtk.i(LogHtk.Test1, "4");
                if (!group1.equals(with)) {
                    LogHtk.i(LogHtk.Test1, "5");
                    senderKey = group1;
                    fromKey = group1;
                    receiverKey = with;
                }

            } else {
                LogHtk.i(LogHtk.Test1, "6");
                if (!fromKey.equals(group1) && !fromKey.equals(with)) {
                    LogHtk.i(LogHtk.Test1, "7");
                    receiverKey = with;
                    senderKey = fromKey;
                }
            }
            LogHtk.i(LogHtk.Test1, "8");
        }

        // Common
        if(file != null) {
            fileAntBuddy = new RFileAntBuddy(file);
            body = file.getName() + " " + file.getSize() + " KB";
        } else {
            try {
                body = new String( mBody.getBytes(), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                body = mBody;
                e.printStackTrace();
            }
        }

        type = message.getType().toString();

        if(subType != null) {
            subtype = subType;
        } else {
            subtype = null;
        }
        time = NationalTime.getLocalTimeToUTCTime();
        isModified = message.getExtension("replace", "urn:xmpp:message-correct:0") != null;
    }

    public RChatMessage(final String _receiverKey, final String _body, final Boolean isMuc, RUserMe userMe) {
        ROrg currentOrg = userMe.getFullCurrentOrg();
        if (currentOrg == null) {
            LogHtk.e(LogHtk.ChatMessage, "Warning! Current Org is not exist!");
            return;
        }
        type = (isMuc? "groupchat": "chat");
        org = currentOrg.getOrgKey();
        senderKey = userMe.getKey();
        receiverKey = _receiverKey;
        if (isMuc) {
            fromKey = receiverKey;
        } else {
            fromKey = senderKey;
        }
        body = _body;
    }

    public RChatMessage(final String _receiverKey, final String _body, final Boolean isMuc, final FileAntBuddy fileAntBuddy, RUserMe userMe) {
        ROrg currentOrg = userMe.getFullCurrentOrg();
        if (currentOrg == null) {
            LogHtk.e(LogHtk.ChatMessage, "Warning! Current Org is not exist!");
            return;
        }
        type = (isMuc? "groupchat": "chat");
        org = currentOrg.getOrgKey();
        senderKey = userMe.getKey();
        receiverKey = _receiverKey;
        if (isMuc) {
            fromKey = receiverKey;
        } else {
            fromKey = senderKey;
        }
        body = _body;

        this.fileAntBuddy = new RFileAntBuddy();
        this.fileAntBuddy.setThumbnailHeight(fileAntBuddy.getThumbnailHeight());
        this.fileAntBuddy.setThumbnailWidth(fileAntBuddy.getThumbnailWidth());
        this.fileAntBuddy.setFileUrl(fileAntBuddy.getFileUrl());
        this.fileAntBuddy.setThumbnailUrl(fileAntBuddy.getThumbnailUrl());
        this.fileAntBuddy.setMimeType(fileAntBuddy.getMimeType());
        this.fileAntBuddy.setName(fileAntBuddy.getName());
        this.fileAntBuddy.setSize(fileAntBuddy.getSize());
    }

    public String getOrg() {
        return org;
    }

    public void setOrg(String org) {
        this.org = org;
    }

    public String getFromKey() {
        return fromKey;
    }

    public void setFromKey(String fromKey) {
        this.fromKey = fromKey;
    }

    public String getReceiverKey() {
        return receiverKey;
    }

    public void setReceiverKey(String receiverKey) {
        this.receiverKey = receiverKey;
    }

    public RFileAntBuddy getFileAntBuddy() {
        return fileAntBuddy;
    }

    public void setFileAntBuddy(RFileAntBuddy fileAntBuddy) {
        this.fileAntBuddy = fileAntBuddy;
    }

    public String getSenderKey() {
        return senderKey;
    }

    public void setSenderKey(String senderKey) {
        this.senderKey = senderKey;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public void setIsModified(boolean isModified) {
        this.isModified = isModified;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubtype() {
        return subtype;
    }

    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getExpandBody() {
        return expandBody;
    }

    public void setExpandBody(String expandBody) {
        this.expandBody = expandBody;
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

    public void showLog() {
        LogHtk.i(LogHtk.Object, "----RChatMessage----");
        LogHtk.i(LogHtk.Object, "id = " + id);
        LogHtk.i(LogHtk.Object, "senderJid = " + senderJid);
        LogHtk.i(LogHtk.Object, "senderId = " + senderId);
        LogHtk.i(LogHtk.Object, "senderName = " + senderName);
        LogHtk.i(LogHtk.Object, "body = " + body);
        LogHtk.i(LogHtk.Object, "fromId = " + fromId);
        LogHtk.i(LogHtk.Object, "receiverJid = " + receiverJid);
        LogHtk.i(LogHtk.Object, "receiverId = " + receiverId);
        LogHtk.i(LogHtk.Object, "receiverName = " + receiverName);
        LogHtk.i(LogHtk.Object, "isModified = " + isModified);
        LogHtk.i(LogHtk.Object, "type = " + type);
        LogHtk.i(LogHtk.Object, "subtype = " + subtype);
        LogHtk.i(LogHtk.Object, "time = " + time);
        LogHtk.i(LogHtk.Object, "expandBody = " + expandBody);
        LogHtk.i(LogHtk.Object, "org = " + org);
        LogHtk.i(LogHtk.Object, "senderKey = " + senderKey);
        LogHtk.i(LogHtk.Object, "fromKey = " + fromKey);
        LogHtk.i(LogHtk.Object, "receiverKey = " + receiverKey);
        LogHtk.i(LogHtk.Object, "fileAntBuddy = " + fileAntBuddy);
        LogHtk.i(LogHtk.Object, "----End----");
    }
}
