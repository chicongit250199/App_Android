package antbuddy.htk.com.antbuddy2016.RealmObjects;

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
public class RObjectManagerOne {

    private Realm realm;

    private RUserMe userme;
    private RealmResults<RUser> users;
    private RUser user;

    private RealmResults<RRoom> rooms;

    private RealmResults<RChatMessage> chatMessages;

    private RealmChangeListener userMeListener;
    private RealmChangeListener usersListener;
    private RealmChangeListener roomsListener;
    private RealmChangeListener chatMessagesListener;

    public RObjectManagerOne() {
        realm = Realm.getDefaultInstance();
    }

    // Working with CACHE
    public RUserMe getUserMeFromCache() {
        if (userme == null) {
            //LogHtk.i(LogHtk.Test3, "--> getUserme when null!");
            //AntbuddyService.getInstance().loading_UserMe_Users_Rooms();
            AntbuddyService.getInstance().loadUserMe();
        }
        return userme;
    }

    public RUserMe getUserMeFromDB() {
//        userme = realm.where(RUserMe.class).findFirst();
        return realm.where(RUserMe.class).findFirst();
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
        AntbuddyService service = AntbuddyService.getInstance();

        if (service != null) {
            new Exception("+++++++++++ShowLog++++++++++>>>>>>>>loading_UserMe_Users_Rooms").printStackTrace();
//            LogHtk.i(LogHtk.API_TAG, "+++++++++++++++++++++>>>>>>>>loading_UserMe_Users_Rooms");
            AntbuddyService.getInstance().loading_UserMe_Users_Rooms();
        } else {
            LogHtk.e(LogHtk.ErrorHTK, "RObjectManagerOne/ Service is still null!");
        }
    }


    public RealmResults<RUser> getUsersFromDB() {
        return realm.where(RUser.class).findAll();
    }

    public static RUser findUser(String userKey, Realm realm) {
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

    public static RRoom findRoom(String roomKey, Realm realm) {
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
        if (userme != null && userMeListener != null) {
            userme.addChangeListener(userMeListener);
        } else {
            new Exception("----test--- RObjectManagerOne/ UserMe is null or userMeListener is null! ").printStackTrace();
            LogHtk.e(LogHtk.WarningHTK, "RObjectManagerOne/ UserMe is null or userMeListener is null!");
        }
//
//        LogHtk.i(LogHtk.Test2, "END saveUserMeOrUpdate");
    }

    public void closeRealm() {
        realm.close();
    }

    public void saveUserOrUpdate(RUser user) {
        if (user != null) {
            realm.beginTransaction();
            realm.copyToRealmOrUpdate(user);
            realm.commitTransaction();
        } else {
            LogHtk.e(LogHtk.Realm, "Warning! User is null!");
        }
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
                //realmUser.setXmppStatus(RUser.XMPPStatus.offline.toString());
                RUser userFound = realm.where(RUser.class).equalTo("key", user.getKey()).findFirst();
                if (userFound != null) {
                    realmUser.setXmppStatus(userFound.getXmppStatus());
                }

                realm.beginTransaction();
                realm.copyToRealmOrUpdate(realmUser);
                realm.commitTransaction();

                // Check and Update Notification
                // Update data in Cache
                this.users = getUsersFromDB();
                if (this.users != null && users.size() > 0 && usersListener != null) {
                    this.users.addChangeListener(usersListener);
                } else {
                    new Exception("----test--- RObjectManagerOne/ Users is null or userMeListener is null! ").printStackTrace();
                    LogHtk.e(LogHtk.WarningHTK, "RObjectManagerOne/ Users is null or usersListener is null!");
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
                if (this.rooms != null && rooms.size() > 0  && roomsListener != null) {
                    this.rooms.addChangeListener(roomsListener);
                } else {
                    new Exception("----test--- RObjectManagerOne/ Rooms is null or userMeListener is null! ").printStackTrace();
                    LogHtk.e(LogHtk.WarningHTK, "RObjectManagerOne/ Rooms is null or roomsListener is null!");
                }
            }
        } else {
            LogHtk.e(LogHtk.Realm, "Warning! Rooms is null or size = 0!");
        }
    }

    public void addUserMeListener(RealmChangeListener listener) {
        userMeListener = listener;
        if (userme != null) {
            userme.addChangeListener(userMeListener);
        } else {
            LogHtk.i(LogHtk.ErrorHTK, "RObjectManagerOne/ Userme is null!");
        }
    }

    public void addUsersListener(RealmChangeListener listener) {
        usersListener = listener;
        if (users != null) {
            users.addChangeListener(usersListener);
        } else {
            LogHtk.i(LogHtk.ErrorHTK, "RObjectManagerOne/ Users is null!");
        }
    }

    public void addRoomsListener(RealmChangeListener listener) {
        roomsListener = listener;
        if (rooms != null) {
            rooms.addChangeListener(roomsListener);
        } else {
            LogHtk.i(LogHtk.ErrorHTK, "RObjectManagerOne/ Rooms is null!");
        }
    }

    public void addChatMessagesListener(RealmChangeListener listener) {
        chatMessagesListener = listener;
        if (chatMessages != null) {
            chatMessages.addChangeListener(chatMessagesListener);
        } else {
            LogHtk.i(LogHtk.ErrorHTK, "RObjectManagerOne/ ChatMessages is null!");
        }
    }

    public void removeUserMeListener() {
        if (userme != null) {
            userme.removeChangeListener(userMeListener);
        }
    }

    public void removeUsersListener() {
        if (users != null) {
            users.removeChangeListener(usersListener);
        }
    }

    public void removeRoomsListener() {
        if (rooms != null) {
            rooms.removeChangeListener(roomsListener);
        }
    }

    public void removeChatMessagesListener() {
        if (chatMessages != null) {
            chatMessages.removeChangeListener(chatMessagesListener);
        }
    }

    public Realm getRealm() {
        return realm;
    }

    public void setUser(RUser user) {
        this.user = user;
    }

    public RUser getUser() {
        return user;
    }

    public void setRealm(Realm realm) {
        this.realm = realm;
    }

    public RUserMe getUserme() {
        return userme;
    }

    public void setUserme(RUserMe userme) {
        this.userme = userme;
    }

    public RealmResults<RUser> getUsers() {
        return users;
    }

    public void setUsers(RealmResults<RUser> users) {
        this.users = users;
    }

    public RealmResults<RRoom> getRooms() {
        return rooms;
    }

    public void setRooms(RealmResults<RRoom> rooms) {
        this.rooms = rooms;
    }

    public RealmResults<RChatMessage> getChatMessages() {
        return chatMessages;
    }

    public void setChatMessages(RealmResults<RChatMessage> chatMessages) {
        this.chatMessages = chatMessages;
    }

    public void setAllUsersOffline() {
        users = realm.where(RUser.class).findAll();
        if (users != null && users.size() >0 ) {
            for (int i = 0 ; i < users.size() ; i ++) {
                realm.beginTransaction();
                RUser user = users.get(i);
                user.setXmppStatus(RUser.XMPPStatus.offline.toString());
                realm.copyToRealmOrUpdate(user);
                realm.commitTransaction();
            }
        }
    }
}
