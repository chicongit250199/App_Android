package antbuddy.htk.com.antbuddy2016.model;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import antbuddy.htk.com.antbuddy2016.api.API;
import antbuddy.htk.com.antbuddy2016.api.LoginAPI;
import antbuddy.htk.com.antbuddy2016.interfaces.HttpRequestReceiver;
import antbuddy.htk.com.antbuddy2016.service.AntbuddyApplication;
import antbuddy.htk.com.antbuddy2016.util.Constants;
import antbuddy.htk.com.antbuddy2016.util.LogHtk;

/**
 * Created by thanhnguyen on 01/04/2016.
 */

public class ObjectManager {
    private List<Room> listRooms = new ArrayList<>();
    private List<User> listUsers = new ArrayList<>();
    private UserMe userMe;

    private HashMap<String, OnListenerUser> mListenerUser = new HashMap<>();
    private HashMap<String, OnListenerGroup> mListenerRoom = new HashMap<>();

    private static ObjectManager objectManager;

    private ObjectManager() {
        listRooms  = new ArrayList<>();
    }

    public static ObjectManager getInstance() {
        if (objectManager == null) {
            objectManager = new ObjectManager();
        }
        return objectManager;
    }

    public void clear() {
        if (userMe != null) {
            userMe.getOpeningChatrooms().clear();
        }

        if (listRooms != null) {
            listRooms.clear();
        }

        if (listUsers != null) {
            listUsers.clear();
        }
    }

    public UserMe getUserMe() {
        return userMe;
    }

    public List<Room> getListRooms() {
        return listRooms;
    }

    public List<User> getListUsers() {
        return listUsers;
    }

    public User findUsers(String senderKey) {
        for (User user : listUsers) {
            if (user.getKey().equals(senderKey)) {
                return user;
            }
        }
        return null;
    }

    public Room findRoom(String senderKey) {
        for (Room room : listRooms) {
            if (room.getKey().equals(senderKey)) {
                return room;
            }
        }
        return null;
    }


    public void setOnListenerUser(Class<?> cls, OnListenerUser listener) {
        mListenerUser.put(cls.getName(), listener);
        if (listUsers.size() == 0) {

            LoginAPI.GETUsers(new HttpRequestReceiver<List<User>>() {
                @Override
                public void onSuccess(List<User> _listUsers) {
                    listUsers.addAll(_listUsers);
                    for (String key: mListenerUser.keySet()) {
                        if(mListenerUser.get(key) != null) {
                            mListenerUser.get(key).onResponse(listUsers);
                        }
                    }
                }

                @Override
                public void onError(String error) {
                    LogHtk.d(LogHtk.API_TAG, "222 " + error.toString());
                }
            });

        }else {
            LogHtk.d(LogHtk.API_TAG, "333 ");
            listener.onResponse(listUsers);
        }
    }

    public void removeOnListenerUser(Class<?> cls) {
        mListenerUser.remove(cls.getName());
    }


    public void setOnListenerRoom(Class<?> cls, OnListenerGroup onListenerGroup) {
        mListenerRoom.put(cls.getName(), onListenerGroup);
        if (listRooms.size() == 0) {
            String API_USERS_URL = "https://" + Constants.domain + ".antbuddy.com/api/rooms/";
            JsonArrayRequest req = new JsonArrayRequest(API_USERS_URL,
                    new Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            listRooms.addAll(Room.parseArray(response));
                            for (String key: mListenerRoom.keySet()) {
                                if(mListenerRoom.get(key) != null) {
                                    mListenerRoom.get(key).onResponse(listRooms);
                                }
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                        }
                    }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("authorization", Constants.token);
                    return params;
                }
            };
            AntbuddyApplication.getInstance().addToRequestQueue(req);
        }else {
            onListenerGroup.onResponse(listRooms);
        }
    }
    public void removeOnListenerRoom(Class<?> cls) {
        mListenerRoom.remove(cls.getName());
    }

    public void getUserMe(final OnListenerUserMe onListenerUserMe) {
        if (userMe == null) {
            String API_USER_ME_URL = "https://" + Constants.domain + ".antbuddy.com/api/me/";

            LoginAPI.GETUserMe(new HttpRequestReceiver<UserMe>() {
                @Override
                public void onSuccess(UserMe me) {
                    LogHtk.d(LogHtk.API_TAG, "Error 11");
                    if (onListenerUserMe != null) {
                        onListenerUserMe.onResponse(me);
                    }
                }

                @Override
                public void onError(String error) {
                    LogHtk.d(LogHtk.API_TAG, "Error 12");
                }
            });
        } else {
            onListenerUserMe.onResponse(userMe);
        }
    }

    public void parseUserMe(JSONObject response) {
        userMe = new UserMe(response);
    }


    public interface OnListenerUser {
        public void onResponse(List<User> listUsers);
    }

    public interface OnListenerGroup {
        public void onResponse(List<Room> listRooms);
    }

    public interface OnListenerUserMe {
        public void onResponse(UserMe userMe);
    }

    public void setListRooms(List<Room> listRooms) {
        this.listRooms = listRooms;
    }

    public void setListUsers(List<User> listUsers) {
        this.listUsers = listUsers;
    }

    public void setUserMe(UserMe userMe) {
        this.userMe = userMe;
    }

    public HashMap<String, OnListenerUser> getmListenerUser() {
        return mListenerUser;
    }

    public void setmListenerUser(HashMap<String, OnListenerUser> mListenerUser) {
        this.mListenerUser = mListenerUser;
    }

    public HashMap<String, OnListenerGroup> getmListenerRoom() {
        return mListenerRoom;
    }

    public void setmListenerRoom(HashMap<String, OnListenerGroup> mListenerRoom) {
        this.mListenerRoom = mListenerRoom;
    }

    public static ObjectManager getObjectManager() {
        return objectManager;
    }

    public static void setObjectManager(ObjectManager objectManager) {
        ObjectManager.objectManager = objectManager;
    }
}
