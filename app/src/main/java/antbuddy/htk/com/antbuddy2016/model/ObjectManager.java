//package antbuddy.htk.com.antbuddy2016.model;
//
//import com.android.volley.AuthFailureError;
//import com.android.volley.Response;
//import com.android.volley.Response.Listener;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.JsonArrayRequest;
//
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import antbuddy.htk.com.antbuddy2016.RealmObjects.RObjectManager;
//import antbuddy.htk.com.antbuddy2016.RealmObjects.RUserMe;
//import antbuddy.htk.com.antbuddy2016.api.APIManager;
//import antbuddy.htk.com.antbuddy2016.interfaces.HttpRequestReceiver;
//import antbuddy.htk.com.antbuddy2016.service.AntbuddyApplication;
//import antbuddy.htk.com.antbuddy2016.setting.ABSharedPreference;
//import antbuddy.htk.com.antbuddy2016.util.LogHtk;
//import io.realm.Realm;
//
///**
// * Created by thanhnguyen on 01/04/2016.
// */
//
//public class ObjectManager {
//    private List<Room> listRooms = new ArrayList<>();
//    private List<User> listUsers = new ArrayList<>();
//    private RUserMe rUserMe;
//
//    private HashMap<String, OnObjectManagerListener> mListenerUser = new HashMap<>();
//    private HashMap<String, OnObjectManagerListener> mListenerRoom = new HashMap<>();
//
//    private static ObjectManager objectManager;
//
//    private ObjectManager() {
//        listRooms  = new ArrayList<>();
//    }
//
//    public static ObjectManager getInstance() {
//        if (objectManager == null) {
//            objectManager = new ObjectManager();
//        }
//        return objectManager;
//    }
//
//    public void clear() {
//        rUserMe = null;
//        if (listRooms != null) {
//            listRooms.clear();
//        }
//        if (listUsers != null) {
//            listUsers.clear();
//        }
//    }
//
//    public RUserMe getUserMe() {
//        LogHtk.i(LogHtk.Test2, "--->Goi vao day ");
//        if (rUserMe == null) {
//            LogHtk.i(LogHtk.Test2, "--->Null roi troi oi ");
//        } else {
//            LogHtk.i(LogHtk.Test2, "--->Khong null ha ");
//        }
//        return rUserMe;
//    }
//
//    synchronized  public void getUserMe(final OnObjectManagerListener<RUserMe> listener) {
//        rUserMe = RObjectManager.getUserMe();
//
//        if (rUserMe == null) {
//            LogHtk.i(LogHtk.Test2, "--->getUserMe/ User is null");
//            APIManager.GETUserMe(new HttpRequestReceiver<UserMe>() {
//                @Override
//                public void onSuccess(UserMe me) {
//                    RObjectManager.saveUserMeOrUpdate(me);
//                    rUserMe = RObjectManager.getUserMe();
//                    if (listener != null) {
//                        listener.onSuccess(rUserMe);
//                    }
//                }
//
//                @Override
//                public void onError(String error) {
//                    listener.onError(error);
//                }
//            });
//        } else {
//            LogHtk.i(LogHtk.Test2, "-->getUserMe/ Khac null");
//            listener.onSuccess(rUserMe);
//        }
//    }
//
//    public List<Room> getListRooms() {
//        return listRooms;
//    }
//
//    public List<User> getListUsers() {
//        return listUsers;
//    }
//
//    public User findUser(String senderKey) {
//        for (User user : listUsers) {
//            if (user.getKey().equals(senderKey)) {
//                return user;
//            }
//        }
//        return null;
//    }
//
//    public Room findRoom(String senderKey) {
//        for (Room room : listRooms) {
//            if (room.getKey().equals(senderKey)) {
//                return room;
//            }
//        }
//        return null;
//    }
//
//    public void setOnListenerUsers(final Class<?> keyRegister, final OnObjectManagerListener listener) {
//        if (keyRegister != null) {
//            mListenerUser.put(keyRegister.getName(), listener);
//        }
//        if (listUsers == null) {
//            listUsers = new ArrayList<>();
//        }
//        if (listUsers.size() == 0) {
//
//            APIManager.GETUsers(new HttpRequestReceiver<List<User>>() {
//                @Override
//                public void onSuccess(List<User> _listUsers) {
//                    listUsers.clear();
//                    listUsers.addAll(_listUsers);
//                    if (keyRegister == null) {
//                        listener.onSuccess(listUsers);
//                    }
//                    for (String key : mListenerUser.keySet()) {
//                        if (mListenerUser.get(key) != null) {
//                            mListenerUser.get(key).onSuccess(listUsers);
//                        }
//                    }
//                }
//
//                @Override
//                public void onError(String error) {
//                    listener.onError(error);
//                }
//            });
//
//        } else {
//            listener.onSuccess(listUsers);
//        }
//    }
//
//    public void removeOnListenerUsers(Class<?> cls) {
//        mListenerUser.remove(cls.getName());
//    }
//
//    synchronized public void setOnListenerRooms(final Class<?> keyRegister, final OnObjectManagerListener onListenerGroup) {
//        if (keyRegister != null) {
//            mListenerRoom.put(keyRegister.getName(), onListenerGroup);
//        }
//        if (listRooms == null) {
//            listRooms = new ArrayList<>();
//        }
//        if (listRooms.size() == 0) {
//
//            APIManager.GETGroups(new HttpRequestReceiver<List<Room>>() {
//                @Override
//                public void onSuccess(List<Room> rooms) {
//                    listRooms.clear();
//                    listRooms.addAll(rooms);
//                    if (keyRegister == null) {
//                        onListenerGroup.onSuccess(listRooms);
//                    }
//                    for (String key : mListenerRoom.keySet()) {
//                        if (mListenerRoom.get(key) != null) {
//                            mListenerRoom.get(key).onSuccess(listRooms);
//                        }
//                    }
//                }
//
//                @Override
//                public void onError(String error) {
//                    onListenerGroup.onError(error);
//                }
//            });
//        }else {
//            onListenerGroup.onSuccess(listRooms);
//        }
//    }
//
//    public interface OnObjectManagerListener<T> {
//        void onSuccess(T object);
//        void onError(String error);
//    }
//
//    public void setListRooms(List<Room> listRooms) {
//        this.listRooms = listRooms;
//    }
//}
