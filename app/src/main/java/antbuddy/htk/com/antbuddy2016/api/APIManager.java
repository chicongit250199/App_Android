package antbuddy.htk.com.antbuddy2016.api;

import java.util.List;

import antbuddy.htk.com.antbuddy2016.interfaces.HttpRequestReceiver;
import antbuddy.htk.com.antbuddy2016.model.ChatMessage;
import antbuddy.htk.com.antbuddy2016.service.AntbuddyApplication;
import antbuddy.htk.com.antbuddy2016.util.Constants;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;

/**
 * Created by thanhnguyen on 06/04/2016.
 */
public class APIManager {

    public static void GETMessages(String time, String chatRoomKey, String typeChat, final HttpRequestReceiver receiver) {
        Call<List<ChatMessage>> call = AntbuddyApplication.getInstance().getApiService().GETMessages(Constants.token, time, chatRoomKey, typeChat);
        call.enqueue(new Callback<List<ChatMessage>>() {
            @Override
            public void onResponse(Response<List<ChatMessage>> response) {
                receiver.onSuccess(response.body());
            }

            @Override
            public void onFailure(Throwable t) {
                receiver.onError(t.toString());
            }
        });
    }
}
