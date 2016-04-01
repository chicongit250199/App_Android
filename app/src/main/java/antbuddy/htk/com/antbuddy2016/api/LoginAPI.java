package antbuddy.htk.com.antbuddy2016.api;

import android.content.Context;

import antbuddy.htk.com.antbuddy2016.interfaces.HttpRequestReceiver;

/**
 * Created by thanhnguyen on 30/03/2016.
 */
public class LoginAPI {

    public static void POSTLogin(String email, String password, HttpRequestReceiver receiver, Context context) {
        Request.POSTLogin(email, password, receiver, context);
    }

    public static void GETOrganizations(HttpRequestReceiver receiver) {
        Request.GETOrganizations(receiver);
    }

    public static void GETOrganizationUserProfile(HttpRequestReceiver receiver) {
        Request.GETOrganizationUserProfile(receiver);
    }

    public static void POSTCheckExistOrganizations(HttpRequestReceiver receiver) {
        //Request.POSTCheckExistOrganizations(receiver);
    }

//    // Check su ton tai Domain
//    https://antbuddy.com/api/organizations/checkexist POST
//    POST /api/organizations/checkexist
//    Posting Data:
//    {
//        name: "htk inc"
//    }
}
