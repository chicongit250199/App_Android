package antbuddy.htk.com.antbuddy2016.RealmObjects;

import io.realm.RealmObject;

/**
 * Created by thanhnguyen on 14/04/2016.
 */

//"_id": "56c536387480fcb75f115556",
//        "name": "HTK INC",
//        "domain": "happy",
//        "key": "475a400a-292b-440c-981a-57af0b3f9a2c",
//        "createdBy": "56c536387480fcb75f115555",
//        "__v": 0,
//        "bonus": {
//        "roomKey": "",
//        "budget": 100
//        },
//        "anttel": {
//        "orgId": "103de9b5191815c3628e0a53510b0039",
//        "active": true,
//        "realm": "happy.anttel-pro.ab-kz-02.antbuddy.com"
//        },
//        "isDefaultLogo": false,
//        "allowSelfRegister": false,
//        "logo": "https://abs1.antbuddy.com/antbuddy-bucket/1455783222587_logov3_100x100.png",
//        "status": "active",
//        "modified": "2016-02-18T03:10:48.128Z",
//        "created": "2016-02-18T03:10:48.128Z",
//        "id": "56c536387480fcb75f115556"

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
