package antbuddy.htk.com.antbuddy2016.RealmObjects;

import io.realm.RealmObject;

/**
 * Created by thanhnguyen on 14/04/2016.
 */
public class ROpeningChatRoom extends RealmObject {
    private String chatRoomKey;
    private String _id;
    private double lastTimeReadMessage;
    private String headNavigatorStatus;
    private String lastReadMessage;
    private Boolean isUnread;
    private Boolean isFavorite;
    private Boolean isMuc;

    public String getChatRoomKey() {
        return chatRoomKey;
    }

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

    public Boolean getIsMuc() {
        return isMuc;
    }

    public void setIsMuc(Boolean isMuc) {
        this.isMuc = isMuc;
    }
}
