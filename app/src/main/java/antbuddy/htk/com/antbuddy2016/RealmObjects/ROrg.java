package antbuddy.htk.com.antbuddy2016.RealmObjects;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by thanhnguyen on 14/04/2016.
 */

public class ROrg extends RealmObject {
    private String orgKey;
    private String role;
    private String _id;
    private RealmList<ROpeningChatRoom> openingChatrooms;

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

    public RealmList<ROpeningChatRoom> getOpeningChatrooms() {
        return openingChatrooms;
    }

    public void setOpeningChatrooms(RealmList<ROpeningChatRoom> openingChatrooms) {
        this.openingChatrooms = openingChatrooms;
    }
}
