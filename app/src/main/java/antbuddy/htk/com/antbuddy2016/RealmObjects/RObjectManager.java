package antbuddy.htk.com.antbuddy2016.RealmObjects;

import android.os.SystemClock;

import java.util.ArrayList;
import java.util.List;

import antbuddy.htk.com.antbuddy2016.model.OpeningChatRoom;
import antbuddy.htk.com.antbuddy2016.model.Room;
import antbuddy.htk.com.antbuddy2016.model.User;
import antbuddy.htk.com.antbuddy2016.model.UserMe;
import antbuddy.htk.com.antbuddy2016.service.AntbuddyService;
import antbuddy.htk.com.antbuddy2016.util.LogHtk;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by thanhnguyen on 14/04/2016.
 */
public class RObjectManager {

    private Realm realm;

    private static RObjectManager realmObject;

    private RUserMe userme;
    private RealmResults<RUser> users;
    private RealmResults<RRoom> rooms;

    private RealmChangeListener userMeListener;
    private RealmChangeListener usersListener;
    private RealmChangeListener roomsListener;

    private RObjectManager() {

    }

    public static RObjectManager getInstance() {
        if (realmObject == null) {
            realmObject = new RObjectManager();
        }
        return realmObject;
    }

    // ASSIGN
    public void assignRealm(Realm realm) {
        this.realm = realm;
    }

    public void assignUserMe(RUserMe userme, RealmChangeListener userMeListener) {
        this.userme = userme;
        this.userMeListener = userMeListener;

        if (this.userme == null) {
//            realm.beginTransaction();
//            RUserMe usermeDefault = realm.createObject(RUserMe.class);
//            usermeDefault.set_id("9999");
//            realm.copyToRealmOrUpdate(usermeDefault);
//            realm.commitTransaction();
//
//            this.userme = getUserMeFromDB();
//            if (this.userme != null) {
//                this.userme.addChangeListener(this.userMeListener);
//            }
        } else {
            this.userme.addChangeListener(this.userMeListener);
        }
    }

    public void assignUsers(RealmResults<RUser> users, RealmChangeListener usersListener) {
        this.users = users;
        this.usersListener = usersListener;

        if (this.users == null) {
//            realm.beginTransaction();
//            RUser userDefault = realm.createObject(RUser.class);
//            userDefault.setKey("8888");
//            realm.copyToRealmOrUpdate(userDefault);
//            realm.commitTransaction();
//
//            this.users = getUsersFromDB();
//            if (this.users != null) {
//                this.users.addChangeListener(this.usersListener);
//            }
        } else {
            this.users.addChangeListener(this.usersListener);
        }
    }

    public void assignRooms(RealmResults<RRoom> rooms, RealmChangeListener roomsListener) {
        this.rooms = rooms;
        this.roomsListener = roomsListener;

        if (this.rooms == null) {
//            realm.beginTransaction();
//            RRoom roomDefault = realm.createObject(RRoom.class);
//            roomDefault.setKey("7777");
//            realm.copyToRealmOrUpdate(roomDefault);
//            realm.commitTransaction();
//
//            this.rooms = getRoomsFromDB();
//            if (this.rooms != null) {
//                this.rooms.addChangeListener(this.roomsListener);
//            }
        } else {
            this.rooms.addChangeListener(this.roomsListener);
        }
    }
    // --------------End Assign------------------


    // Working with CACHE
    public RUserMe getUserMeFromCache() {
        if (userme == null) {
            //LogHtk.i(LogHtk.Test3, "--> getUserme when null!");
            //AntbuddyService.getInstance().loading_UserMe_Users_Rooms();
            AntbuddyService.getInstance().loadUserMe();
        }
        return userme;
    }

    public void setUsermeIntoCache(RUserMe userme) {
        this.userme = userme;
    }

    public boolean isUserMeExistInCache() {
        if (userme != null && userme.getKey().length() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public RealmResults<RUser> getUsersFromCache() {
        if (users == null) {
            //AntbuddyService.getInstance().loading_UserMe_Users_Rooms();
        }
        return users;
    }

    public void setUsersInCache(RealmResults<RUser> users) {
        this.users = users;
    }

    public boolean isUsersExistInCache() {
        if (users != null && users.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public RealmResults<RRoom> getRoomsFromCache() {
        if (rooms == null) {
            //AntbuddyService.getInstance().loading_UserMe_Users_Rooms();
        }
        return rooms;
    }

    public void setRoomsInCache(RealmResults<RRoom> rooms) {
        this.rooms = rooms;
    }

    public boolean isRoomsExistInCache() {
        if (rooms != null && rooms.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public RUserMe getUserMeFromDB() {
        userme = realm.where(RUserMe.class).findFirst();
        return userme;
    }

    public static boolean isUserMeExist() {
        Realm realm = Realm.getDefaultInstance();
        RUserMe rUserMe = realm.where(RUserMe.class).findFirst();
        if (rUserMe != null && rUserMe.getKey().length() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public void loading_UserMe_Users_Rooms() {
        AntbuddyService.getInstance().loading_UserMe_Users_Rooms();
//        AntbuddyService.getInstance().loadUsers();
    }


    public RealmResults<RUser> getUsersFromDB() {
        return realm.where(RUser.class).findAll();
    }

    public static RUser findUser(String userKey) {
        Realm realm = Realm.getDefaultInstance();
        RUser user = realm.where(RUser.class).equalTo("key", userKey).findFirst();
        return user;
    }

    public static boolean isUsersExist() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<RUser> realmUsers = realm.where(RUser.class).findAll();
        if (realmUsers != null &&  realmUsers.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public RealmResults<RRoom> getRoomsFromDB() {
        return realm.where(RRoom.class).findAll();
    }

    public static RRoom findRoom(String roomKey) {
        Realm realm = Realm.getDefaultInstance();
        RRoom room = realm.where(RRoom.class).equalTo("key", roomKey).findFirst();
        return room;
    }

    public static boolean isRoomsExist() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<RRoom> realmRooms = realm.where(RRoom.class).findAll();
        if (realmRooms != null && realmRooms.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    /*
    This function will parse Gson object to Realm Object, and Realm Object will be save or update into DB
     */
    public void saveUserMeOrUpdate(UserMe me) {
        RUserMe rMe = new RUserMe();
        rMe.setKey(me.getKey());
        rMe.setChatToken(me.getChatToken());
        rMe.setChatMucDomain(me.getChatMucDomain());
        rMe.setChatDomain(me.getChatDomain());
        rMe.set_id(me.get_id());
        rMe.setName(me.getName());
        rMe.setEmail(me.getEmail());
        rMe.setAvatar(me.getAvatar());
        rMe.setUsername(me.getUsername());
        rMe.setProvider(me.getProvider());
        rMe.setChatUrl(me.getChatUrl());

        rMe.setModified(me.getModified());
        rMe.setActive(me.getActive());
        rMe.setTourStep(me.getTourStep());

        List<UserMe.Org> orgs = me.getOrgs();
        RealmList<ROrg> rOrgs = new RealmList<>();
        if (orgs != null) {
            for (int i = 0 ; i < orgs.size() ; i ++) {
                UserMe.Org org = orgs.get(i);
                ROrg realmOrg = new ROrg();
                realmOrg.set_id(org.get_id());
                realmOrg.setOrgKey(org.getOrgKey());
                realmOrg.setRole(org.getRole());

                // GET OPENING CHAT ROOM
                List<OpeningChatRoom> openingChatRooms = org.getOpeningChatrooms();
                RealmList<ROpeningChatRoom> realmOpeningChatRooms = new RealmList<>();
                if (openingChatRooms != null) {
                    for (OpeningChatRoom open : openingChatRooms) {
                        ROpeningChatRoom realmOpen = new ROpeningChatRoom();
                        realmOpen.set_id(open.get_id());
                        realmOpen.setChatRoomKey(open.getChatRoomKey());
                        realmOpen.setLastTimeReadMessage(open.getLastTimeReadMessage());
                        realmOpen.setHeadNavigatorStatus(open.getHeadNavigatorStatus());
                        realmOpen.setLastReadMessage(open.getLastReadMessage());
                        realmOpen.setIsUnread(open.getIsUnread());
                        realmOpen.setIsFavorite(open.getIsFavorite());
                        realmOpen.setIsMuc(open.getIsMuc());
                        realmOpeningChatRooms.add(realmOpen);
                    }
                    realmOrg.setOpeningChatrooms(realmOpeningChatRooms);
                }

                rOrgs.add(realmOrg);
            }
            rMe.setOrgs(rOrgs);
        }

        rMe.setForceChangePassw(me.getForceChangePassw());
        rMe.setCreated(me.getCreated());

        UserMe.CurrentOrg currentOrg = me.getCurrentOrg();
        RCurrentOrg realmCurrentOrg = new RCurrentOrg();
        if (currentOrg != null) {
            realmCurrentOrg.setName(currentOrg.getName());
            realmCurrentOrg.setDomain(currentOrg.getDomain());
            realmCurrentOrg.setKey(currentOrg.getKey());
            realmCurrentOrg.setCreatedBy(currentOrg.getCreatedBy());
            realmCurrentOrg.setIsDefaultLogo(currentOrg.getIsDefaultLogo());
            realmCurrentOrg.setAllowSelfRegister(currentOrg.getAllowSelfRegister());
            realmCurrentOrg.setLogo(currentOrg.getLogo());
            realmCurrentOrg.setStatus(currentOrg.getStatus());
            realmCurrentOrg.setModified(currentOrg.getModified());
            realmCurrentOrg.setCreated(currentOrg.getCreated());
            rMe.setCurrentOrg(realmCurrentOrg);
        }

        realm.beginTransaction();
        realm.copyToRealmOrUpdate(rMe);
        realm.commitTransaction();

        // Update data in Cache
        userme = getUserMeFromDB();
        if (userme != null) {
            LogHtk.i(LogHtk.Test3, "Register Userme success!: " + userme.toString());
            userme.addChangeListener(userMeListener);
        } else {
            LogHtk.e(LogHtk.Test3, "Get UserMe from db is null!");
        }
//
//        LogHtk.i(LogHtk.Test2, "END saveUserMeOrUpdate");
    }

    public void saveUsersOrUpdate(List<User> users) {
        if (users != null && users.size() > 0) {
            for (User user : users) {
                RUser realmUser = new RUser();
                realmUser.setAvatar(user.getAvatar());
                realmUser.setUsername(user.getUsername());
                realmUser.setKey(user.getKey());
                realmUser.setName(user.getName());
                realmUser.setEmail(user.getEmail());
                realmUser.setNonce(user.getNonce());
                realmUser.setBio(user.getBio());
                realmUser.setPhone(user.getPhone());
                realmUser.setRole(user.getRole());
                realmUser.setActive(user.isActive());
                realmUser.setIsFavorite(user.isFavorite());

                realm.beginTransaction();
                realm.copyToRealmOrUpdate(realmUser);
                realm.commitTransaction();

                // Check and Update Notification
                // Update data in Cache
                this.users = getUsersFromDB();
                if (this.users != null && users.size() > 0) {
                    this.users.addChangeListener(usersListener);
                } else {
                    LogHtk.e(LogHtk.Test3, "Users null!");
                }
            }
        } else {
            LogHtk.e(LogHtk.Realm, "Warning! Users is null or size == 0!");
        }
    }

    public void saveRoomsOrUpdate(List<Room> rooms) {
        if (rooms != null && rooms.size() > 0) {
            for (Room room : rooms) {
                RRoom realmRoom = new RRoom();
                realmRoom.set_id(room.get_id());
                realmRoom.setCreatedBy(room.getKey_createdBy());
                realmRoom.setOrg(room.getOrg());
                realmRoom.setKey(room.getKey());

                List<Room.UserInRoom> usersInRoom = room.getUsers();
                RealmList<RUsersInRoom> realmUsersInRoom = new RealmList<>();
                if (usersInRoom != null && usersInRoom.size() > 0) {
                    for (Room.UserInRoom userRoom: usersInRoom) {
                        RUsersInRoom realUserInRoom = new RUsersInRoom();
                        realUserInRoom.set_id(userRoom.get_id());
                        realUserInRoom.setRole(userRoom.getRole());
                        realUserInRoom.setUser(userRoom.getUser());

                        realmUsersInRoom.add(realUserInRoom);
                    }
                    realmRoom.setUsers(realmUsersInRoom);
                } else {
                    LogHtk.e(LogHtk.Realm, "Warning! Users in Room is null or size = 0!");
                }

                realmRoom.setCountFiles(room.getCountFiles());
                realmRoom.setCountMessages(room.getCountMessages());
                realmRoom.setIsPublic(room.getIsPublic());
                realmRoom.setStatus(room.getStatus());
                realmRoom.setPinMessage(room.getPinMessage());
                realmRoom.setTopic(room.getTopic());
                realmRoom.setName(room.getName());
                realmRoom.setCreated(room.getCreated());
                realmRoom.setIsFavorite(room.getIsFavorite());

                realm.beginTransaction();
                realm.copyToRealmOrUpdate(realmRoom);
                realm.commitTransaction();

                // Check and Update Notification
                this.rooms = getRoomsFromDB();
                if (this.rooms != null && rooms.size() > 0) {
                    this.rooms.addChangeListener(roomsListener);
                } else {
                    LogHtk.e(LogHtk.Test3, "Rooms null!");
                }
            }
        } else {
            LogHtk.e(LogHtk.Realm, "Warning! Rooms is null or size = 0!");
        }
    }
}
