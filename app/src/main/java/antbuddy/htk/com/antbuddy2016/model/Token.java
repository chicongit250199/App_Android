package antbuddy.htk.com.antbuddy2016.model;

/**
 * Created by thanhnguyen on 02/04/2016.
 */
public class Token {
    String token;
    String expires;
    String message;

    public Token() {
        token = "";
        expires = "";
        message = "";
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getExpires() {
        return expires;
    }

    public void setExpires(String expires) {
        this.expires = expires;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
