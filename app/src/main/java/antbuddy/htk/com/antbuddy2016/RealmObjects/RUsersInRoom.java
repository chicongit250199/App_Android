package antbuddy.htk.com.antbuddy2016.RealmObjects;

import io.realm.RealmObject;

/**
 * Created by thanhnguyen on 15/04/2016.
 */
public class RUsersInRoom extends RealmObject {
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