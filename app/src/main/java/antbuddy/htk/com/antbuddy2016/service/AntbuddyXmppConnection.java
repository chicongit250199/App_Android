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
import org.jivesoftware.smack.filter.IQTypeFilter;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.AntBuddyFile;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Message.Type;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smackx.provider.AdHocCommandDataProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import antbuddy.htk.com.antbuddy2016.GsonObjects.GChatMassage;
import antbuddy.htk.com.antbuddy2016.RealmObjects.RChatMessage;
import antbuddy.htk.com.antbuddy2016.RealmObjects.RObjectManagerBackGround;
import antbuddy.htk.com.antbuddy2016.RealmObjects.RObjectManagerOne;
import antbuddy.htk.com.antbuddy2016.RealmObjects.ROrg;
import antbuddy.htk.com.antbuddy2016.RealmObjects.RRoom;
import antbuddy.htk.com.antbuddy2016.RealmObjects.RUser;
import antbuddy.htk.com.antbuddy2016.RealmObjects.RUserMe;
import antbuddy.htk.com.antbuddy2016.api.APIManager;
import antbuddy.htk.com.antbuddy2016.interfaces.HttpRequestReceiver;
import antbuddy.htk.com.antbuddy2016.interfaces.XMPPReceiver;
import antbuddy.htk.com.antbuddy2016.setting.ABSharedPreference;
import antbuddy.htk.com.antbuddy2016.setting.ABXMPPConfig;
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

    private List<String> messageIDs;

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
            LogHtk.i(LogHtk.Info, "XmppConnection is created!");
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
            ABSharedPreference.save(ABSharedPreference.KEY_XMPP_RESOURCE, android_id);
            xmppConnection.login(config.getUSERNAME_XMPP(), config.getPASSWORD_XMPP(), android_id);

            // After connect successful
            // set connectionXmpp
            xmppConnection.setConnected(true);

            // Register: MessageListener, ConnectionListener, PresenceListener
            addMessageListener();
            addConnectionListener();
            addPresenceListener();

            // Add packet Listener
            PacketFilter getFilter = new IQTypeFilter(IQ.Type.ERROR);
            xmppConnection.addPacketListener(new PacketListener() {
                public void processPacket(Packet packet) {
                    LogHtk.i(LogHtk.Test1, "===========> 111");
                    String from = packet.getFrom();
                    String to = packet.getTo();

                    final IQ request = new IQ() {
                        @Override
                        public String getChildElementXML() {
                            return toXML();
                        }
                    };
                    request.setFrom(from);
                    request.setTo(to);

                    final IQ result = IQ.createResultIQ(request);
                    LogHtk.i(LogHtk.Test1, "Reply: " + result.toXML());
                    xmppConnection.sendPacket(result);
                }
            }, getFilter);

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
            public void processPacket(final Packet packet) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        presenceIN(packet);
                    }
                }).start();

            }
        };
        xmppConnection.addPacketListener(presenceListener, presenceFilter);
    }

    synchronized private void presenceIN(Packet packet) {
        LogHtk.i(LogHtk.XMPPPresence, "=================================presenceIN ===================================");
        RObjectManagerOne realm = new RObjectManagerOne();

        Presence presence = (Presence) packet;
        LogHtk.i(LogHtk.XMPPPresence, presence.toXML());

        // FROM
        final String senderID = presence.getFrom().split("_")[0];
        realm.setUser(realm.getRealm().where(RUser.class).equalTo("key", senderID).findFirst());

        // TYPE
        final Presence.Type typeXMPP = presence.getType();

        // STATUS
        Presence.Mode xmppMode = presence.getMode();

        if (senderID.length() > 0 && typeXMPP != null && xmppMode == null) {
            LogHtk.i(LogHtk.XMPPPresence, "--> Process online, offline");

            RUser user = realm.getUser();
            if (user != null) {
                LogHtk.i(LogHtk.XMPPPresence, "senderID = " + senderID + " with name: " + user.getName());

                if (typeXMPP.toString().equals(Presence.Type.unavailable.toString())) {
                    LogHtk.i(LogHtk.XMPPPresence, "- unavailable");
                    realm.getRealm().beginTransaction();
                    user.setXmppStatus(RUser.XMPPStatus.offline.toString());
                    realm.getRealm().copyToRealmOrUpdate(user);
                    realm.getRealm().commitTransaction();
                }

                if (typeXMPP.toString().equals(Presence.Type.available.toString())) {
                    LogHtk.i(LogHtk.XMPPPresence, "- available");
                    realm.getRealm().beginTransaction();
                    user.setXmppStatus(RUser.XMPPStatus.online.toString());
                    realm.getRealm().copyToRealmOrUpdate(user);
                    realm.getRealm().commitTransaction();
                }
            } else {
                LogHtk.e(LogHtk.XMPPPresence, "User is null!");
            }
        }

        // Process away, dnd
        if (xmppMode != null  && xmppMode.toString().length() > 0) {
            LogHtk.i(LogHtk.XMPPPresence, "--> Process away, dnd");
            RUser user = realm.getUser();
            if (user != null) {
                LogHtk.i(LogHtk.XMPPPresence, "senderID = " + senderID + " with name: " + user.getName());
                LogHtk.i(LogHtk.XMPPPresence, "- " + xmppMode.toString());
                realm.getRealm().beginTransaction();
                user.setXmppStatus(xmppMode.toString());
                realm.getRealm().copyToRealmOrUpdate(user);
                realm.getRealm().commitTransaction();
            } else {
                LogHtk.e(LogHtk.XMPPPresence, "User is null!");
            }
        }

        realm.closeRealm();
    }

    /**
     * part packet to message object and later broadcast it to activity
     * al
     *
     * @param packet
     */
    private void messageIN(Packet packet) {
        final Message message = (Message) packet;
//        LogHtk.i(LogHtk.XMPP_TAG, "====================XMPP Message IN===============================");
//        LogHtk.i(LogHtk.XMPP_TAG, message.toXML());

        if (message.getTimestamp().length() > 0 && message.getBody() != null && message.getBody().length() > 0) {

            Type type = message.getType();
            if (type != null) {
                if ((type.toString().equals("groupchat") && message.getAck() != null)
                        || (type.toString().equals("chat") && message.getAck() != null)
                        || (type.toString().equals("chat") && message.getAck() == null && message.getFrom().equals(ABSharedPreference.get(ABSharedPreference.KEY_XMPP_DOMAIN)))) {

                    LogHtk.i(LogHtk.XMPP_TAG, "====================XMPP Message IN===============================");
                    LogHtk.i(LogHtk.XMPP_TAG, message.toXML());

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            RObjectManagerBackGround realmBG = new RObjectManagerBackGround();
                            RChatMessage _chatMessage = new RChatMessage(message);

                            LogHtk.i(LogHtk.XMPP_TAG, " -> ChatMessage: " + _chatMessage.toString());
                            _chatMessage.setTime(message.getTimestamp());
                            realmBG.saveMessage(_chatMessage);

                            String from = message.getFrom();
                            String xmppResource = ABSharedPreference.get(ABSharedPreference.KEY_XMPP_RESOURCE);
//                            String xmppDomain = ABSharedPreference.get(ABSharedPreference.KEY_XMPP_DOMAIN);
                            if ((from != null && from.contains(xmppResource)) || (messageIDs != null && messageIDs.contains(_chatMessage.getId()))) {
                                LogHtk.i(LogHtk.Test1, "XMPP Resource : " + xmppResource);
                                LogHtk.i(LogHtk.Test1, "-->IN messageIDs: " + messageIDs.size());
                                messageIDs.remove(_chatMessage.getId());
                                APIManager.POSTSaveMessage(_chatMessage, new HttpRequestReceiver<GChatMassage>() {
                                    @Override
                                    public void onSuccess(GChatMassage chatmessage) {
                                        LogHtk.i(LogHtk.XMPP_TAG, " -> Message saved: " + chatmessage.toString());
                                    }

                                    @Override
                                    public void onError(String error) {
                                        LogHtk.e(LogHtk.XMPP_TAG, "ERROR! Can not save message! " + error);
                                    }
                                });
                            } else {
                                LogHtk.i(LogHtk.Test1, "No need save message data! Because it'd saved on web client!");
                            }

                            realmBG.closeRealm();
                        }
                    }).start();
                } else {
                    LogHtk.e(LogHtk.ErrorHTK, "Warning! XMPP message in not be show!");
                }
            } else {
                LogHtk.e(LogHtk.ErrorHTK, "Error! Mode is null!");
            }

        } else {
            LogHtk.e(LogHtk.ErrorHTK, "XMPP message don't have timestamp field!");
        }
    }

    public void messageOUT(final RChatMessage chatMessage) {
        //LogHtk.i(LogHtk.XMPP_TAG, "===========================XMPP Message OUT===================================");
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

        Message msg = new Message(receiverJid, type);
        msg.setPacketID(chatMessage.getId());
        msg.setBody(chatMessage.getBody());
        msg.setWith(chatMessage.getReceiverKey());
        if (chatMessage.getFileAntBuddy() != null) {
            AntBuddyFile file = new AntBuddyFile(chatMessage.getFileAntBuddy().getName(),
                    chatMessage.getFileAntBuddy().getSize(), chatMessage.getFileAntBuddy().getFileUrl(),
                    chatMessage.getFileAntBuddy().getMimeType(), chatMessage.getFileAntBuddy().getThumbnailUrl());
            msg.setFile(file);

            msg.setBody("File uploaded: " + chatMessage.getFileAntBuddy().getFileUrl());
        }

        LogHtk.i(LogHtk.XMPP_TAG, msg.toXML());
        if (messageIDs == null) {
            messageIDs = new ArrayList<>();
        }
        messageIDs.add(chatMessage.getId());
        LogHtk.i(LogHtk.Test1, "-->OUT messageIDs: " + messageIDs.size());
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
            LogHtk.i(LogHtk.XMPP_TAG, "disconnect XMPP");
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