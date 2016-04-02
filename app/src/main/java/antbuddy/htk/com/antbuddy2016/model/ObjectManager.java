package antbuddy.htk.com.antbuddy2016.model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import antbuddy.htk.com.antbuddy2016.api.ParseJson;
import antbuddy.htk.com.antbuddy2016.objects.Domain;
import antbuddy.htk.com.antbuddy2016.util.JSONKey;

/**
 * Created by thanhnguyen on 01/04/2016.
 */

public class ObjectManager {
    // Check all data loaded
    private boolean isUserMeLoaded;
    private boolean isUsersLoaded;
    private boolean isGroupsLoaded;
    private boolean groupsNeedToReload;

    private List<Room> listRooms;
    private List<User> listUsers;
    private UserMe userMe;

    private static ObjectManager objectManager;

    private ObjectManager() {
        isUserMeLoaded = false;
        isUsersLoaded = false;
        isGroupsLoaded = false;

        userMe = new UserMe();
        listRooms  = new ArrayList<>();
        listUsers  = new ArrayList<>();
    }

    public static ObjectManager getInstance() {
        if (objectManager == null) {
            objectManager = new ObjectManager();
        }
        return objectManager;
    }

    public void clear() {
        isUserMeLoaded = false;
        isUsersLoaded = false;
        isGroupsLoaded = false;
        userMe = new UserMe();
        listRooms.clear();
        listUsers.clear();
    }

    public UserMe getUserMe() {
        return userMe;
    }

    public void setUserMe(UserMe userMe) {
        isUserMeLoaded = true;
        this.userMe = userMe;
    }

    public List<Room> getListRooms() {
        return listRooms;
    }

    public void setListRooms(List<Room> listRooms) {
        isGroupsLoaded = true;
        this.listRooms = listRooms;
    }

    public List<User> getListUsers() {
        return listUsers;
    }

    public void setListUsers(List<User> listUsers) {
        isUsersLoaded = true;
        this.listUsers = listUsers;
    }

    public boolean isUserMeLoaded() {
        return isUserMeLoaded;
    }

    public void setIsUserMeLoaded(boolean isUserMeLoaded) {
        this.isUserMeLoaded = isUserMeLoaded;
    }

    public boolean isUsersLoaded() {
        return isUsersLoaded;
    }

    public void setIsUsersLoaded(boolean isUsersLoaded) {
        this.isUsersLoaded = isUsersLoaded;
    }

    public boolean isGroupsLoaded() {
        return isGroupsLoaded;
    }

    public void setIsGroupsLoaded(boolean isGroupsLoaded) {
        this.isGroupsLoaded = isGroupsLoaded;
    }

    public boolean isGroupsNeedToReload() {
        return groupsNeedToReload;
    }

    public void parseUserMe(JSONObject jsonObject) {
        try {
            userMe = UserMe.parse(jsonObject);
            isUserMeLoaded = true;
        } catch (JSONException e) {
            e.printStackTrace();
            isUserMeLoaded = false;
        }
    }

    public void parseUsers(JSONArray jsonArray) {
        listUsers.clear();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject element = null;
            try {
                element = (JSONObject) jsonArray.get(i);
                User user = User.parse(element);
                if (user != null) {
                    listUsers.add(user);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (listUsers.size() > 0) {
            isUsersLoaded = true;
        } else {
            isUsersLoaded = false;
        }
    }

    public void parseRooms(JSONArray jsonArray) {
        listRooms.clear();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject element = null;
            try {
                element = (JSONObject) jsonArray.get(i);
                Room room = Room.parse(element);
                if (room != null) {
                    listRooms.add(room);
                }

                groupsNeedToReload = false;
            } catch (JSONException e) {
                e.printStackTrace();
                groupsNeedToReload = true;
            }
        }

        if (listRooms.size() >= 0) {
            isGroupsLoaded = true;
        } else {
            isGroupsLoaded = false;
        }
    }
}
