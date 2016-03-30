package antbuddy.htk.com.antbuddy2016.api;

/**
 * Created by thanhnguyen on 30/03/2016.
 */
public class LoginAPI {

    public static void login(String email, String password, HttpRequestReceiver receiver) {
        Request.login(email, password, receiver);
    }
}
