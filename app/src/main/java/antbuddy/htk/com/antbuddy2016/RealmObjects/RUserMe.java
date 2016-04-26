package antbuddy.htk.com.antbuddy2016.RealmObjects;

import java.util.ArrayList;
import java.util.List;

import antbuddy.htk.com.antbuddy2016.util.LogHtk;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by thanhnguyen on 14/04/2016.
 */
public class RUserMe extends RealmObject {
    @PrimaryKey
    private String key;

    private String chatToken; // 53893e8c497468d3453f6d5a:27f00feeeb03a35e5dd0b44f6ae7c9bebc43a045880c92d0d03e7b5bb05da02e06628b1c07f3a3f3
    private String chatMucDomain; // conference.htklabs.com
    private String chatDomain; // htklabs.com
    private String _id; // 53893e8c497468d3453f6d5a
    private String name; // Thanh Nguyen
    private String email; // thanh.nguyen@htklabs.com
    private String avatar; // avatar url
    private String username; // thanh.nguyen@htklabs.com
    private String provider; // google
    private String chatUrl;
    private String nonce;
    private String modified;
    private Boolean active;
    private int tourStep;
    private RealmList<ROrg> orgs;
    private Boolean forceChangePassw;
    private String created;
    private RCurrentOrg currentOrg;

    public ROrg getFullCurrentOrg() {
        String key_org = currentOrg.getKey();
        for (ROrg org : orgs) {
            if (org.getOrgKey().equals(key_org)) {
                return org;
            }
        }

        return null;
    }

    public List<ROpeningChatRoom> getOpeningChatrooms() {
        if (currentOrg == null) {
            LogHtk.i(LogHtk.UserMe, "Error! currentOrg is null in UserMe!");
            return null;
        }

        String key = currentOrg.getKey();
        for (ROrg org : orgs) {
            if (org.getOrgKey().equals(key)) {
                return org.getOpeningChatrooms();
            }
        }
        return null;
    }

    public static List<ROpeningChatRoom> getChatsOpening(RUserMe me, boolean isGroup) {
        List<ROpeningChatRoom> listChatsOpening = new ArrayList<>();
        if (me != null && me.getOpeningChatrooms() != null) {
            for (ROpeningChatRoom openingChatroom : me.getOpeningChatrooms()) {
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
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
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

    public String getChatUrl() {
        return chatUrl;
    }

    public void setChatUrl(String chatUrl) {
        this.chatUrl = chatUrl;
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

    public List<ROrg> getOrgs() {
        return orgs;
    }

    public void setOrgs(RealmList<ROrg> orgs) {
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

    public RCurrentOrg getCurrentOrg() {
        return currentOrg;
    }

    public void setCurrentOrg(RCurrentOrg currentOrg) {
        this.currentOrg = currentOrg;
    }
}
