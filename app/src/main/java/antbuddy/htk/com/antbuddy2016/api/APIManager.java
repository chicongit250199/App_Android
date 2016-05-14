package antbuddy.htk.com.antbuddy2016.api;

import android.app.Activity;

import java.util.List;

import antbuddy.htk.com.antbuddy2016.GsonObjects.GChatMassage;
import antbuddy.htk.com.antbuddy2016.GsonObjects.GFileAntBuddy;
import antbuddy.htk.com.antbuddy2016.RealmObjects.RChatMessage;
import antbuddy.htk.com.antbuddy2016.interfaces.HttpRequestReceiver;
import antbuddy.htk.com.antbuddy2016.model.NewAccount;
import antbuddy.htk.com.antbuddy2016.model.Organization;
import antbuddy.htk.com.antbuddy2016.model.OrganizationExist;
import antbuddy.htk.com.antbuddy2016.model.Room;
import antbuddy.htk.com.antbuddy2016.model.Token;
import antbuddy.htk.com.antbuddy2016.model.User;
import antbuddy.htk.com.antbuddy2016.model.UserMe;
import antbuddy.htk.com.antbuddy2016.service.AntbuddyApplication;
import antbuddy.htk.com.antbuddy2016.setting.ABSharedPreference;
import antbuddy.htk.com.antbuddy2016.util.AndroidHelper;
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
                    LogHtk.e(LogHtk.ErrorHTK, "APIManager/GETLogin/ ERROR! " + response.message());
                    receiver.onError(response.code() + "");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                receiver.onError(t.toString());
            }
        });
    }

    public static void POSTCheckOrganizationExist(final String domainName, final HttpRequestReceiver<OrganizationExist> receiver) {
        Call<OrganizationExist> call = AntbuddyApplication.getInstance().getApiService().POSTCheckOrganizationExist(domainName);
        call.enqueue(new Callback<OrganizationExist>() {
            @Override
            public void onResponse(Response<OrganizationExist> response) {
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

    public static void POSTCreateNewAccount(final String userName,
                                            final String fullName,
                                            final String email,
                                            final String password,
                                            final String organization,
                                            final String domain,
                                            final HttpRequestReceiver<NewAccount> receiver) {
        Call<NewAccount> call = AntbuddyApplication.getInstance().getApiService().POSTCreateNewAccount(userName, fullName, email, password, organization, domain);
        call.enqueue(new Callback<NewAccount>() {
            @Override
            public void onResponse(Response<NewAccount> response) {
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
        Call<List<Organization>> call = AntbuddyApplication.getInstance().getApiService().GETOrganizations(ABSharedPreference.getAccountConfig().getToken());
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


//    public static void GETMessages(String time, String chatRoomKey, String typeChat, final HttpRequestReceiver<List<ChatMessage>> receiver) {
//        String token = ABSharedPreference.getAccountConfig().getToken();
//        Call<List<ChatMessage>> call = AntbuddyApplication.getInstance().getApiService().GETMessages(token, time, chatRoomKey, typeChat);
//        call.enqueue(new Callback<List<ChatMessage>>() {
//            @Override
//            public void onResponse(Response<List<ChatMessage>> response) {
//                if (response.body() != null) {
//                    receiver.onSuccess(response.body());
//                } else {
//                    receiver.onError(response.code() + "");
//                }
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//                receiver.onError(t.toString());
//            }
//        });
//    }

    public static void GETMessages(String time, String chatRoomKey, String typeChat, final HttpRequestReceiver<List<GChatMassage>> receiver) {
        String token = ABSharedPreference.getAccountConfig().getToken();
        Call<List<GChatMassage>> call = AntbuddyApplication.getInstance().getApiService().GETMessages(token, time, chatRoomKey, typeChat);
        call.enqueue(new Callback<List<GChatMassage>>() {
            @Override
            public void onResponse(Response<List<GChatMassage>> response) {
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
        Call<UserMe> call = AntbuddyApplication.getInstance().getApiService().GETUserProfile(ABSharedPreference.getAccountConfig().getToken());
        call.enqueue(new Callback<UserMe>() {
            @Override
            public void onResponse(Response<UserMe> response) {
                if (response.body() != null) {
                    receiver.onSuccess(response.body());
                } else {
                    LogHtk.e(LogHtk.ErrorHTK, "APIManager/GETUserMe/ ERROR! " + response.message());
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
        Call<List<User>> call = AntbuddyApplication.getInstance().getApiService().GETUsers(ABSharedPreference.getAccountConfig().getToken());
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
        Call<List<Room>> call = AntbuddyApplication.getInstance().getApiService().GETRooms(ABSharedPreference.getAccountConfig().getToken());
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

    public static void POSTSaveMessage(RChatMessage chatMessage, final HttpRequestReceiver<GChatMassage> receiver) {
        GChatMassage message = new GChatMassage();
        message.setBody(chatMessage.getBody());
        message.setFromKey(chatMessage.getFromKey());
        message.setReceiverKey(chatMessage.getReceiverKey());
        message.setSenderKey(chatMessage.getSenderKey());
        message.setSubtype(chatMessage.getSubtype());
        message.setType(chatMessage.getType());
        message.setId(chatMessage.getId());
        message.setTime(chatMessage.getTime());

        if (chatMessage.getFileAntBuddy() != null) {
            GFileAntBuddy file = new GFileAntBuddy();
            file.setSize(chatMessage.getFileAntBuddy().getSize());
            file.setName(chatMessage.getFileAntBuddy().getName());
            file.setMimeType(chatMessage.getFileAntBuddy().getMimeType());
            file.setFileUrl(chatMessage.getFileAntBuddy().getFileUrl());
            file.setThumbnailHeight(chatMessage.getFileAntBuddy().getThumbnailHeight());
            file.setThumbnailWidth(chatMessage.getFileAntBuddy().getThumbnailWidth());
            file.setThumbnailUrl(chatMessage.getFileAntBuddy().getThumbnailUrl());
            message.setFileAntBuddy(file);

            message.setBody("File uploaded: " + file.getFileUrl());
        }

        LogHtk.i(LogHtk.Test1, "--> message---- ");
        LogHtk.i(LogHtk.Test1, "Body = " + message.getBody());
        LogHtk.i(LogHtk.Test1, "FromKey = " + message.getFromKey());
        LogHtk.i(LogHtk.Test1, "ReceiverKey = " + message.getReceiverKey());
        LogHtk.i(LogHtk.Test1, "SenderKey = " + message.getSenderKey());
        LogHtk.i(LogHtk.Test1, "ID = " + message.getId());
        LogHtk.i(LogHtk.Test1, "Type = " + message.getType());
        LogHtk.i(LogHtk.Test1, "Subtype = " + message.getSubtype());
        LogHtk.i(LogHtk.Test1, "Time = " + message.getTime());

        LogHtk.i(LogHtk.Test1, "ABSharedPreference.getAccountConfig().getToken() = " + ABSharedPreference.getAccountConfig().getToken());
        Call<GChatMassage> call = AntbuddyApplication.getInstance().getApiService().newMessageToHistory(ABSharedPreference.getAccountConfig().getToken(), message);
        call.enqueue(new Callback<GChatMassage>() {
            @Override
            public void onResponse(Response<GChatMassage> response) {
                LogHtk.i(LogHtk.Test1, "errorBody = " + response.errorBody());
                LogHtk.i(LogHtk.Test1, "message = " + response.message());
                LogHtk.i(LogHtk.Test1, "Raw = " + response.raw());

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
}
