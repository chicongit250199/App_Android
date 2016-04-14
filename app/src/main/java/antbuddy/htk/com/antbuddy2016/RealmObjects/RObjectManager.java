package antbuddy.htk.com.antbuddy2016.RealmObjects;

import java.util.ArrayList;
import java.util.List;

import antbuddy.htk.com.antbuddy2016.model.OpeningChatRoom;
import antbuddy.htk.com.antbuddy2016.model.User;
import antbuddy.htk.com.antbuddy2016.model.UserMe;
import antbuddy.htk.com.antbuddy2016.service.AntbuddyApplication;
import antbuddy.htk.com.antbuddy2016.util.LogHtk;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by thanhnguyen on 14/04/2016.
 */
public class RObjectManager {

    public static RUserMe getUserMe() {
        Realm realm = Realm.getDefaultInstance();
        RUserMe rUserMe = realm.where(RUserMe.class).findFirst();
        return rUserMe;
    }

    /*
    This function will parse Gson object to Realm Object, and Realm Object will be save or update into DB
     */
    public static void saveUserMeOrUpdate(UserMe me) {
        Realm realm = Realm.getDefaultInstance();
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
    }

    public static void saveUserOrUpdate(User user) {

    }

    public static void saveRoomOrUpdate(User user) {

    }


}
