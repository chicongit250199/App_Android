package antbuddy.htk.com.antbuddy2016.RealmObjects;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by thanhnguyen on 15/04/2016.
 */
public class RRoom extends RealmObject {

    @PrimaryKey
    private String key;

    private String _id;
    private String createdBy;
    private String org;
    //private String anttel;
    private RealmList<RUsersInRoom> users;
    private int countFiles;
    private int countMessages;
    private Boolean isPublic;
    private String status;
    private String pinMessage;
    private String topic;
    private String name;
    private String created;
    private Boolean isFavorite;

    public RealmList<RUsersInRoom> getUsers() {
        return users;
    }

    public void setUsers(RealmList<RUsersInRoom> users) {
        this.users = users;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getOrg() {
        return org;
    }

    public void setOrg(String org) {
        this.org = org;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getCountFiles() {
        return countFiles;
    }

    public void setCountFiles(int countFiles) {
        this.countFiles = countFiles;
    }

    public int getCountMessages() {
        return countMessages;
    }

    public void setCountMessages(int countMessages) {
        this.countMessages = countMessages;
    }

    public Boolean getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPinMessage() {
        return pinMessage;
    }

    public void setPinMessage(String pinMessage) {
        this.pinMessage = pinMessage;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public Boolean getIsFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(Boolean isFavorite) {
        this.isFavorite = isFavorite;
    }
}
