package antbuddy.htk.com.antbuddy2016.api;

import android.app.Activity;

import java.util.HashMap;
import java.util.List;

import antbuddy.htk.com.antbuddy2016.interfaces.HttpRequestReceiver;
import antbuddy.htk.com.antbuddy2016.model.ChatMessage;
import antbuddy.htk.com.antbuddy2016.model.Organization;
import antbuddy.htk.com.antbuddy2016.model.Room;
import antbuddy.htk.com.antbuddy2016.model.Token;
import antbuddy.htk.com.antbuddy2016.model.User;
import antbuddy.htk.com.antbuddy2016.model.UserMe;
import antbuddy.htk.com.antbuddy2016.service.AntbuddyApplication;
import antbuddy.htk.com.antbuddy2016.setting.ABSharedPreference;
import antbuddy.htk.com.antbuddy2016.util.AndroidHelper;
import antbuddy.htk.com.antbuddy2016.util.Constants;
import antbuddy.htk.com.antbuddy2016.util.LogHtk;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;

/**
 * Created by thanhnguyen on 06/04/2016.
 */
public class APIManager {

    public static void showToastWithCode(String code, Activity activity) {
        int codeInt;
        try {
            codeInt = Integer.parseInt(code);
            AndroidHelper.showToast("Request error with code: " + codeInt, activity);
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
            codeInt = -1;
            AndroidHelper.showToast("Warning: " + code, activity);
        }
    }

    public static void GETLogin(String email, String password, final HttpRequestReceiver<Token> receiver) {
        Call<Token> call = AntbuddyApplication.getInstance().getApiService().GETLogin(email, password);
        call.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Response<Token> response) {
                if (response.body() != null) {
                    receiver.onSuccess(response.body());
                } else {
                    receiver.onError(response.code() + "");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                receiver.onError(t.toString());
            }
        });
    }

    public static void GETOrganizations(final HttpRequestReceiver<List<Organization>> receiver) {
        Call<List<Organization>> call = AntbuddyApplication.getInstance().getApiService().GETOrganizations(ABSharedPreference.getAccoungConfig().getToken());
        call.enqueue(new Callback<List<Organization>>() {
            @Override
            public void onResponse(Response<List<Organization>> response) {
                if (response.body() != null) {
                    receiver.onSuccess(response.body());
                } else {
                    receiver.onError(response.code() + "");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                receiver.onError(t.toString());
            }
        });
    }


    public static void GETMessages(String time, String chatRoomKey, String typeChat, final HttpRequestReceiver<List<ChatMessage>> receiver) {
        String token = ABSharedPreference.getAccoungConfig().getToken();
        Call<List<ChatMessage>> call = AntbuddyApplication.getInstance().getApiService().GETMessages(token, time, chatRoomKey, typeChat);
        call.enqueue(new Callback<List<ChatMessage>>() {
            @Override
            public void onResponse(Response<List<ChatMessage>> response) {
                if (response.body() != null) {
                    receiver.onSuccess(response.body());
                } else {
                    receiver.onError(response.code() + "");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                receiver.onError(t.toString());
            }
        });
    }

    public static void GETUserMe(final HttpRequestReceiver<UserMe> receiver) {
        Call<UserMe> call = AntbuddyApplication.getInstance().getApiService().GETUserProfile(ABSharedPreference.getAccoungConfig().getToken());
        call.enqueue(new Callback<UserMe>() {
            @Override
            public void onResponse(Response<UserMe> response) {
                if (response.body() != null) {
                    receiver.onSuccess(response.body());
                } else {
                    receiver.onError(response.code() + "");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                receiver.onError(t.toString());
            }
        });
    }

    public static void GETUsers(final HttpRequestReceiver<List<User>> receiver) {
        Call<List<User>> call = AntbuddyApplication.getInstance().getApiService().GETUsers(ABSharedPreference.getAccoungConfig().getToken());
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Response<List<User>> response) {
                if (response.body() != null) {
                    receiver.onSuccess(response.body());
                } else {
                    receiver.onError(response.code() + "");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                receiver.onError(t.toString());
            }
        });
    }

    public static void GETGroups(final HttpRequestReceiver<List<Room>> receiver) {
        Call<List<Room>> call = AntbuddyApplication.getInstance().getApiService().GETRooms(ABSharedPreference.getAccoungConfig().getToken());
        call.enqueue(new Callback<List<Room>>() {
            @Override
            public void onResponse(Response<List<Room>> response) {
                if (response.body() != null) {
                    receiver.onSuccess(response.body());
                } else {
                    receiver.onError(response.code() + "");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                receiver.onError(t.toString());
            }
        });
    }

    public static void newMessageToHistory(ChatMessage chatMessage) {
        String id = chatMessage.getFromKey()+ AndroidHelper.renID();
        HashMap<String, Object> body = new HashMap<>();
        body.put("body", chatMessage.getBody());
        body.put("fromKey", chatMessage.getFromKey());
        body.put("receiverKey", chatMessage.getReceiverKey());
        body.put("senderKey", chatMessage.getSenderKey());
        body.put("subtype", chatMessage.getSubType());
        body.put("type", chatMessage.getType());
        body.put("id", id);

        Call<ChatMessage> call = AntbuddyApplication.getInstance().getApiService().newMessageToHistory(ABSharedPreference.getAccoungConfig().getToken(), body);
        call.enqueue(new Callback<ChatMessage>() {
            @Override
            public void onResponse(Response<ChatMessage> response) {
            }

            @Override
            public void onFailure(Throwable t) {
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
