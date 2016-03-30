package antbuddy.htk.com.antbuddy2016.objects;

/**
 * Created by thanhnguyen on 29/03/2016.
 */
public class Domain {
    private String name;
    private String domain;

    public Domain(String domain, String name) {
        this.domain = domain;
        this.name = name;
    }

    public Domain(String name) {
        this.name = name;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
