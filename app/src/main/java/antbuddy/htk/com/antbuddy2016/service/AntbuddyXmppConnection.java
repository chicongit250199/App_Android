package antbuddy.htk.com.antbuddy2016.service;

import android.app.NotificationManager;
import android.content.Context;
import android.provider.Settings;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.AntBuddyFile;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Message.Type;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smackx.provider.AdHocCommandDataProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import antbuddy.htk.com.antbuddy2016.RealmObjects.RChatMessage;
import antbuddy.htk.com.antbuddy2016.RealmObjects.RObjectManagerBackGround;
import antbuddy.htk.com.antbuddy2016.RealmObjects.RObjectManagerOne;
import antbuddy.htk.com.antbuddy2016.RealmObjects.ROrg;
import antbuddy.htk.com.antbuddy2016.RealmObjects.RRoom;
import antbuddy.htk.com.antbuddy2016.RealmObjects.RUser;
import antbuddy.htk.com.antbuddy2016.RealmObjects.RUserMe;
import antbuddy.htk.com.antbuddy2016.api.APIManager;
import antbuddy.htk.com.antbuddy2016.interfaces.XMPPReceiver;
import antbuddy.htk.com.antbuddy2016.model.ChatMessage;
import antbuddy.htk.com.antbuddy2016.setting.ABSharedPreference;
import antbuddy.htk.com.antbuddy2016.setting.ABXMPPConfig;
import antbuddy.htk.com.antbuddy2016.util.AndroidHelper;
import antbuddy.htk.com.antbuddy2016.util.LogHtk;
import io.realm.RealmResults;

public class AntbuddyXmppConnection {

    // Used when connect to XMPP Server successful or unsuccessful.
    public static final String SERVICE_START_SUCCESS = "XMPP_START_SUCCESS";
    public static final String SERVICE_START_ERROR = "XMPP_START_ERROR";
    public static final String SERVICE_ALREADY_START = "XMPP_ALREADY_START";
    public static final String SERVICE_ALREADY_CONNECTED_AND_AUTHEN = "XMPP_ALREADY_CONNECTED_AND_AUTHEN";
    public static final String SERVICE_ALREADY_LOGGED = "XMPP_ALREADY_LOGGED";

    private static Context mContext;

    private XMPPConnection xmppConnection;
    private ConnectionListener mConnectionListener;
    private PacketListener chatListener;
    private PacketListener groupChatListener;
    private PacketListener deleteListener;
    private PacketListener presenceListener;


    private RChatMessage chatMessageWillBeSent;

    /**
     * Notification
     */
    private NotificationManager myNotificationManager;
    private Map<String, Integer> notificationInfo = new HashMap<String, Integer>(); // include notificationID and number messages

    protected static AntbuddyXmppConnection instance;

    synchronized public static AntbuddyXmppConnection getInstance() {
        if (instance == null) {
            instance = new AntbuddyXmppConnection();
            LogHtk.i(LogHtk.XMPP_TAG, "Created a new XMPP connection!");
        }
        return instance;
    }

    public void connectXMPP(RUserMe me) {
        if (me != null) {
            String[] accountXMPP = me.getChatToken().split(":");
            String username = accountXMPP[0];
            String password = accountXMPP[1];

            Pattern p = Pattern.compile(".*\\/\\/([^:^\\/]+).*");
            Matcher m = p.matcher(me.getChatUrl());
            String hostXMPP = "";
            if (m.matches()) {
                hostXMPP = m.group(1);
            }

            int portXMPP = 5222;    // Default
            String domainXMPP = me.getChatDomain();

            if (hostXMPP.length() > 0 && username.length() > 0 && password.length() > 0 && domainXMPP.length() > 0) {
                ABSharedPreference.save(ABSharedPreference.KEY_XMPP_HOST, hostXMPP);
                ABSharedPreference.save(ABSharedPreference.KEY_XMPP_PORT, portXMPP);
                ABSharedPreference.save(ABSharedPreference.KEY_XMPP_DOMAIN, domainXMPP);
                ABSharedPreference.save(ABSharedPreference.KEY_XMPP_USERNAME, username);
                ABSharedPreference.save(ABSharedPreference.KEY_XMPP_PASSWORD, password);

                // LOGIN XMPP
                connectXMPP(AntbuddyService.getInstance().getApplicationContext(), new XMPPReceiver() {
                    @Override
                    public void onSuccess(String result) {
                        LogHtk.d(LogHtk.XMPP_TAG, "-->Connect XMPP:  " + result);
                    }

                    @Override
                    public void onError(String error) {
                        LogHtk.e(LogHtk.XMPP_TAG, "-->Connect XMPP " + error);
                    }
                });
            }
        } else {
            new Exception("Cannot connect XMPP! Cause Userme is NULL!").printStackTrace();
        }
    }

    synchronized private void connectXMPP(final Context context, final XMPPReceiver receiver) {
        if (xmppConnection != null && xmppConnection.isConnected() && xmppConnection.isAuthenticated()) {
            receiver.onSuccess(SERVICE_ALREADY_CONNECTED_AND_AUTHEN);
            return;
        }

        mContext = context;
        ABXMPPConfig config = getABXMPPConfig();
        ConnectionConfiguration connConfig = new ConnectionConfiguration(config.getHOST_XMPP(), config.getPORT_XMPP(), config.getDOMAIN_XMPP());

        if (xmppConnection == null) {
            LogHtk.i(LogHtk.Test1, "XmppConnection is created!");
            xmppConnection = new XMPPConnection(connConfig);
        }

        try {
            xmppConnection.connect();
        } catch (XMPPException e) {
            e.printStackTrace();
            String result = e.getMessage();
            if (result.contains("Already logged in to server.")) {
                receiver.onSuccess(SERVICE_ALREADY_LOGGED);
            } else {
//                xmppConnection = null;
                receiver.onError(e.getMessage());
            }
            return;
        }

        ProviderManager pm = ProviderManager.getInstance();
        pm.addIQProvider("command", "http://jabber.org/protocol/commands", new AdHocCommandDataProvider());

        try {
            SASLAuthentication.supportSASLMechanism("PLAIN", 0);
            String android_id = Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
            xmppConnection.login(config.getUSERNAME_XMPP(), config.getPASSWORD_XMPP(), android_id);

            // After connect successful
            // set connectionXmpp
            xmppConnection.setConnected(true);

            // Register: MessageListener, ConnectionListener, PresenceListener
            addMessageListener();
            addConnectionListener();
            addPresenceListener();

            // send presence out to Server XMPP
            sendPresenceOutFromOpeningRooms();

            receiver.onSuccess(SERVICE_START_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            String result = e.getMessage();
            if (result.contains("Already logged in to server.")) {
                receiver.onSuccess(SERVICE_ALREADY_LOGGED);
            } else {
                receiver.onError(result);
            }
        }
    }

    /**
     * Add message listener
     */
    private void addMessageListener() {
        PacketFilter messageFilter = new MessageTypeFilter(Type.chat);
        chatListener = new PacketListener() {
            public void processPacket(Packet packet) {
                messageIN(packet);
            }
        };

        PacketFilter groupChatFilter = new MessageTypeFilter(Type.groupchat);
        groupChatListener = new PacketListener() {
            public void processPacket(Packet packet) {
                messageIN(packet);
            }
        };

        PacketFilter messageDelete = new MessageTypeFilter(Type.event);
        deleteListener = new PacketListener() {
            public void processPacket(Packet packet) {
                messageIN(packet);
            }
        };
        // register packet listener
        xmppConnection.addPacketListener(chatListener, messageFilter);
        xmppConnection.addPacketListener(deleteListener, messageDelete);
        xmppConnection.addPacketListener(groupChatListener, groupChatFilter);
    }

    /**
     * Add connection listener
     */
    private void addConnectionListener() {
        mConnectionListener = new ConnectionListener() {

            @Override
            public void reconnectionSuccessful() {
                LogHtk.i(LogHtk.XMPP_TAG, "Successfully reconnected to the XMPP server.");
            }

            @Override
            public void reconnectionFailed(Exception e) {
                LogHtk.i(LogHtk.XMPP_TAG, "Failed to reconnect to the XMPP server.");
                e.printStackTrace();
            }

            @Override
            public void reconnectingIn(int seconds) {
                LogHtk.i(LogHtk.XMPP_TAG, "Reconnecting in " + seconds + " seconds.");
            }

            @Override
            public void connectionClosedOnError(Exception e) {
                LogHtk.i(LogHtk.XMPP_TAG, "Connection to XMPP server was lost.");
                e.printStackTrace();
            }

            @Override
            public void connectionClosed() {
                LogHtk.i(LogHtk.XMPP_TAG, "XMPP connection was closed.");
            }
        };
        try {
            xmppConnection.addConnectionListener(mConnectionListener);
        } catch (IllegalStateException ex) {
            LogHtk.i(LogHtk.XMPP_TAG, "loi Gi day: " + ex.toString());
        }

    }

    /**
     * Add presence listener
     */
    private void addPresenceListener() {
        PacketTypeFilter presenceFilter = new PacketTypeFilter(Presence.class);
        presenceListener = new PacketListener() {
            public void processPacket(Packet packet) {
                presenceIN(packet);
            }
        };
        xmppConnection.addPacketListener(presenceListener, presenceFilter);
    }

    private void presenceIN(Packet packet) {
        Presence presence = (Presence) packet;
        LogHtk.i(LogHtk.XMPP_TAG, "IN/PRESENCE: " + presence.toXML());

        // Update status on RUser
//        <presence to="4e499d80-1408-11e6-8d18-91c528c9c04c_69fbb50b-7822-485e-9960-ee0cb869e848@antbuddy.com/7b815c22a8c0f6fa"
//        from="4e499d80-1408-11e6-8d18-91c528c9c04c_69fbb50b-7822-485e-9960-ee0cb869e848@antbuddy.com/19241909081462593894776630">
//        <status>undefined</status
//                ><show>away</show>
//        </presence>



//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                RObjectManagerOne realm = new RObjectManagerOne();
//                realm.setUsers(realm.getRealm().where(RUser.class).equalTo("key", ).findAll());
//
////                realm.setUsers();
//
////                //RChatMessage _chatMessage = new RChatMessage(message, realmBG.getUserMeFromDB());
////                LogHtk.d(LogHtk.XMPP_TAG, "					ChatMessage" + _chatMessage.toString());
////                realmBG.saveMessage(_chatMessage);
////
////                // Update time
////                if (chatMessageWillBeSent != null) {
////                    chatMessageWillBeSent.setTime(message.getTimestamp());
////                    APIManager.POSTSaveMessage(chatMessageWillBeSent);
////                }
//
//                realm.closeRealm();
//            }
//        }).start();


    }

    /**
     * part packet to message object and later broadcast it to activity
     * al
     *
     * @param packet
     */
    private void messageIN(Packet packet) {

        final Message message = (Message) packet;
        LogHtk.d(LogHtk.XMPP_TAG, "												-->Message from XMPP: " + message.toXML());

        String domainXMPP = ABSharedPreference.get(ABSharedPreference.KEY_XMPP_DOMAIN);
        LogHtk.d(LogHtk.XMPP_TAG, "--> domainXMPP = " + domainXMPP);
        LogHtk.d(LogHtk.XMPP_TAG, "--> message.getBody() = " + message.getBody());
        LogHtk.d(LogHtk.XMPP_TAG, "--> message.getBody().length() = " + message.getBody().length());
        LogHtk.d(LogHtk.XMPP_TAG, "-->!message.getFrom().equals(domainXMPP) = " + !message.getFrom().equals(domainXMPP));
        LogHtk.d(LogHtk.XMPP_TAG, "--> message.getFrom() = " + message.getFrom());

        if (message.getBody() != null && message.getBody().length() > 0) {

            new Thread(new Runnable() {
                @Override
                public void run() {
                    RObjectManagerBackGround realmBG = new RObjectManagerBackGround();
                    RChatMessage _chatMessage = new RChatMessage(message, realmBG.getUserMeFromDB());
                    LogHtk.d(LogHtk.XMPP_TAG, "					ChatMessage" + _chatMessage.toString());
                    realmBG.saveMessage(_chatMessage);

                    // Update time
                    if (chatMessageWillBeSent != null) {
                        chatMessageWillBeSent.setTime(message.getTimestamp());
                        APIManager.POSTSaveMessage(chatMessageWillBeSent);
                    }

                    realmBG.closeRealm();
                }
            }).start();
        }
    }

    public void messageOUT(final RChatMessage chatMessage) {

        if (xmppConnection == null || !xmppConnection.isConnected()) {
            //LogHtk.e(LogHtk.ErrorHTK, "ERROR! XMPPConnection is null or do not connect! --> Try to connect XMPP ... ");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    RObjectManagerOne realmManager = new RObjectManagerOne();
                    realmManager.setUserme(realmManager.getRealm().where(RUserMe.class).findFirst());
                    connectXMPP(realmManager.getUserme());
                    realmManager.closeRealm();
                }
            }).start();

            return;
        }

        RObjectManagerOne realmManager = new RObjectManagerOne();
        realmManager.setUserme(realmManager.getRealm().where(RUserMe.class).findFirst());
        RUserMe userMe = realmManager.getUserme();
        if (userMe == null) {
            LogHtk.e(LogHtk.ErrorHTK, "ERROR! Cannot send message out because Userme is Null!");
            realmManager.closeRealm();
            return;
        }

        ROrg currentOrg = userMe.getFullCurrentOrg();
        if (currentOrg == null) {
            LogHtk.e(LogHtk.API_TAG, "Warning! Oragnization is null in UserMe: ");
            realmManager.closeRealm();
            return;
        }
        String orgKey = currentOrg.getOrgKey();
        final String chatMucDomain = userMe.getChatMucDomain();
        String receiverJid;
        Message.Type type;
        if (chatMessage.getType().equals("groupchat")) {
            receiverJid = String.format("%s_%s@%s", chatMessage.getReceiverKey(), orgKey, chatMucDomain);
            type = Message.Type.groupchat;
        } else {
            receiverJid = String.format("%s_%s@%s", chatMessage.getReceiverKey(), orgKey, ABSharedPreference.get(ABSharedPreference.KEY_XMPP_DOMAIN));
            type = Message.Type.chat;
        }

        String id = chatMessage.getFromKey() + AndroidHelper.renID();
        chatMessage.setId(id);
        this.chatMessageWillBeSent = chatMessage;

        Message msg = new Message(receiverJid, type);
        msg.setPacketID(id);
        msg.setBody(chatMessage.getBody());
        msg.setWith(chatMessage.getReceiverKey());
        if (chatMessage.getFileAntBuddy() != null) {
            AntBuddyFile file = new AntBuddyFile(chatMessage.getFileAntBuddy().getName(),
                    chatMessage.getFileAntBuddy().getSize(), chatMessage.getFileAntBuddy().getFileUrl(),
                    chatMessage.getFileAntBuddy().getMimeType(), chatMessage.getFileAntBuddy().getThumbnailUrl());
            msg.setFile(file);

            msg.setBody("File uploaded: " + chatMessage.getFileAntBuddy().getFileUrl());
        }
        xmppConnection.sendPacket(msg);
        realmManager.closeRealm();
    }

    Thread openingRoom;

    /*
     * In CHATGROUP case:
     * we must send Presence assigned to notification GroupChatRoom. Then you can send Message to Room.
     */
    private void sendPresenceOutFromOpeningRooms() {
        if (openingRoom == null || !openingRoom.isAlive()) {
            openingRoom = new Thread(new Runnable() {
                @Override
                public void run() {
                    RObjectManagerBackGround realmBG = new RObjectManagerBackGround();
                    RealmResults<RRoom> rooms = realmBG.getRoomsFromDB();
                    RUserMe userMe = realmBG.getUserMeFromDB();
                    if (userMe == null) {
                        realmBG.closeRealm();
                        return;
                    }

                    for (RRoom room : rooms) {
                        RUserMe me = userMe;
                        String key_org = me.getFullCurrentOrg().getOrgKey();
                        String key_me = me.getKey();
                        Presence presence = new Presence(org.jivesoftware.smack.packet.Presence.Type.available);
                        presence.setTo(room.getKey() + "_" + key_org + "@conference.antbuddy.com/" + key_me + "_" + key_org);
                        if (xmppConnection != null && xmppConnection.isConnected()) {
                            xmppConnection.sendPacket(presence);
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            //LogHtk.i(LogHtk.XMPP_TAG, "Out/GROUP_PRESENCE: " + presence.toXML());
                        } else {
                            //LogHtk.i(LogHtk.XMPP_TAG, "Out/GROUP_PRESENCE: " + presence.toXML());
                        }
                    }
                    realmBG.closeRealm();
                }
            });
            openingRoom.start();
        }
    }

    /**
     * Handle disconnect
     */
    public void disconnectXMPP() {
        if (xmppConnection != null) {
            LogHtk.i(LogHtk.Test1, "disconnect XMPP");
			xmppConnection.setConnected(false);

            if (chatListener != null) {
                xmppConnection.removePacketListener(chatListener);
            }

            if (deleteListener != null) {
                xmppConnection.removePacketListener(deleteListener);
            }

            if (groupChatListener != null) {
                xmppConnection.removePacketListener(groupChatListener);
            }

            if (presenceListener != null) {
                xmppConnection.removePacketListener(presenceListener);
            }

            if (mConnectionListener != null) {
                xmppConnection.removeConnectionListener(mConnectionListener);
            }

            xmppConnection.disconnect();
            xmppConnection = null;
        }
    }

    private ABXMPPConfig getABXMPPConfig() {
        return ABSharedPreference.getXMPPConfig();
    }

    /**
     * set connectionsenderId
     *
     * @param connection
     */
    public void setConnection(XMPPConnection connection) {
        xmppConnection = connection;
    }
}