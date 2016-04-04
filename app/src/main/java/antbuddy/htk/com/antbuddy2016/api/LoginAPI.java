package antbuddy.htk.com.antbuddy2016.api;

import android.content.Context;

import java.util.List;

import antbuddy.htk.com.antbuddy2016.interfaces.HttpRequestReceiver;
import antbuddy.htk.com.antbuddy2016.model.Room;
import antbuddy.htk.com.antbuddy2016.model.User;
import antbuddy.htk.com.antbuddy2016.model.UserMe;
import antbuddy.htk.com.antbuddy2016.service.AntbuddyApplication;
import antbuddy.htk.com.antbuddy2016.util.Constants;
import antbuddy.htk.com.antbuddy2016.util.LogHtk;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;

/**
 * Created by thanhnguyen on 30/03/2016.
 */
public class LoginAPI {

    public static void POSTLogin(String email, String password, HttpRequestReceiver receiver) {
        ABRequest.POSTLogin(email, password, receiver);
    }

    public static void GETOrganizations(HttpRequestReceiver receiver) {
        ABRequest.GETOrganizations(receiver);
    }

    public static void GETUserProfile(HttpRequestReceiver receiver) {
        ABRequest.GETOrganizationUserProfile(receiver);
    }

//    public static void GETListUsers(HttpRequestReceiver receiver) {
//        ABRequest.GETListUsers(receiver);
//    }

//    public static void GETListGroups(HttpRequestReceiver receiver) {
//        ABRequest.GETListGroups(receiver);
//    }

    // Create new account
    public static void POSTCheckExistOrganizations(HttpRequestReceiver receiver) {
        //ABRequest.POSTCheckExistOrganizations(receiver);
    }


    public static void GETUserMe(final HttpRequestReceiver receiver) {
        Call<UserMe> call = AntbuddyApplication.getInstance().getApiService().GETUserProfile(Constants.token);
        call.enqueue(new Callback<UserMe>() {
            @Override
            public void onResponse(Response<UserMe> response) {
                receiver.onSuccess(response.body());
            }

            @Override
            public void onFailure(Throwable t) {
                receiver.onError(t.toString());
            }
        });
    }

    public static void GETUsers(final HttpRequestReceiver receiver) {
        Call<List<User>> call = AntbuddyApplication.getInstance().getApiService().GETUsers(Constants.token);
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Response<List<User>> response) {
                receiver.onSuccess(response.body());
            }

            @Override
            public void onFailure(Throwable t) {
                receiver.onError(t.toString());
            }
        });
    }

    public static void GETGroups(final HttpRequestReceiver receiver) {
        Call<List<Room>> call = AntbuddyApplication.getInstance().getApiService().GETRooms(Constants.token);
        call.enqueue(new Callback<List<Room>>() {
            @Override
            public void onResponse(Response<List<Room>> response) {
                receiver.onSuccess(response.body());
            }

            @Override
            public void onFailure(Throwable t) {
                receiver.onError(t.toString());
            }
        });
    }







//    // Check su ton tai
//    https://antbuddy.com/api/organizations/checkexist POST
//    POST /api/organizations/checkexist
//    Posting Data:
//    {
//        name: "htk inc"
//    }
}
