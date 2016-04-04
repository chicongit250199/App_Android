package antbuddy.htk.com.antbuddy2016.model;

/**
 * Created by thanhnguyen on 04/04/2016.
 */

// Example
//    {
//        "_id": "56c536387480fcb75f115556",
//            "name": "HTK INC",
//            "domain": "happy",
//            "key": "475a400a-292b-440c-981a-57af0b3f9a2c",
//            "isDefaultLogo": false,
//            "logo": "https://abs1.antbuddy.com/antbuddy-bucket/1455783222587_logov3_100x100.png",
//            "anttel": {
//        "realm": "happy.anttel-pro.ab-kz-02.antbuddy.com"
//    },
//        "id": "56c536387480fcb75f115556"
//    },

public class Organization {

    String name;
    String domain;
    String key;
    String logo;
    boolean isDefaultLogo;

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

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public boolean isDefaultLogo() {
        return isDefaultLogo;
    }

    public void setIsDefaultLogo(boolean isDefaultLogo) {
        this.isDefaultLogo = isDefaultLogo;
    }
}
