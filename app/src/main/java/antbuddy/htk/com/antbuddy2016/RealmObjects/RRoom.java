package antbuddy.htk.com.antbuddy2016.RealmObjects;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by thanhnguyen on 15/04/2016.
 */
public class RRoom extends RealmObject {
    private String _id;
    private String createdBy;
    private String org;
    private String key;
    //private String anttel;
    private RealmList<RUserInRoom> users;
    private int countFiles;
    private int countMessages;
    private Boolean isPublic;
    private String status;
    private String pinMessage;
    private String topic;
    private String name;
    private String created;
    private Boolean isFavorite;

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

    public RealmList<RUserInRoom> getUsers() {
        return users;
    }

    public void setUsers(RealmList<RUserInRoom> users) {
        this.users = users;
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

    public static class RUserInRoom extends RealmObject {
        private String user;
        private String _id;
        private int role;

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public int getRole() {
            return role;
        }

        public void setRole(int role) {
            this.role = role;
        }
    }

}
