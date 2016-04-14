package antbuddy.htk.com.antbuddy2016.RealmObjects;

import io.realm.RealmObject;

/**
 * Created by thanhnguyen on 14/04/2016.
 */
public class RCurrentOrg extends RealmObject {
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

    public String getKey() {
        return key;
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
}
