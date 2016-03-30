package antbuddy.htk.com.antbuddy2016.api;

/**
 * Created by thanhnguyen on 30/03/2016.
 */
public class LoginAPI {

    public static void POSTLogin(String email, String password, HttpRequestReceiver receiver) {
        Request.POSTLogin(email, password, receiver);
    }

    public static void GETOrganizations(HttpRequestReceiver receiver) {
        Request.GETOrganizations(receiver);
    }
}
