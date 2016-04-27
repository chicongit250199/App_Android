package antbuddy.htk.com.antbuddy2016.service;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.content.ComponentName;
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
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Message.Type;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smackx.provider.AdHocCommandDataProvider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import antbuddy.htk.com.antbuddy2016.RealmObjects.RChatMessage;
import antbuddy.htk.com.antbuddy2016.RealmObjects.RObjectManagerBackGround;
import antbuddy.htk.com.antbuddy2016.RealmObjects.RObjectManagerOne;
import antbuddy.htk.com.antbuddy2016.RealmObjects.ROrg;
import antbuddy.htk.com.antbuddy2016.RealmObjects.RRoom;
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
//				Intent intent = new Intent(BroadcastConstant.BROAD_CAST_CONNECTION_STATUS);
//				intent.putExtra(AntbuddyConstant.CONNECTION_STATUS, AntbuddyConstant.CONNECTION_STATUS_SUCCESS_MESSAGE);
//				mContext.sendBroadcast(intent);
                LogHtk.i(LogHtk.XMPP_TAG, "Successfully reconnected to the XMPP server.");
                //sendPresenceOutFromOpeningRooms();
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
                Presence presence = (Presence) packet;
                //LogHtk.i(LogHtk.XMPP_TAG, "IN/PRESENCE: " + presence.toXML());
            }
        };
        xmppConnection.addPacketListener(presenceListener, presenceFilter);
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

        if (message.getBody() != null && message.getBody().length() > 0 && !message.getFrom().equals(domainXMPP)) {

            new Thread(new Runnable() {
                @Override
                public void run() {
                    RObjectManagerBackGround realmBG = new RObjectManagerBackGround();
                    RChatMessage chatMessage = new RChatMessage(message, realmBG.getUserMeFromDB());
                    LogHtk.d(LogHtk.XMPP_TAG, "					ChatMessage" + chatMessage.toString());
                    realmBG.saveMessage(chatMessage);
                    realmBG.closeRealm();
                }
            }).start();
        }
    }

    public void messageOUT(final RChatMessage chatMessage) {
        if (xmppConnection == null || !xmppConnection.isConnected()) {
            LogHtk.e(LogHtk.ErrorHTK, "ERROR! XMPPConnection is null or do not connect! --> Try to connect XMPP ... ");
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
        Message msg = new Message(receiverJid, type);
        msg.setPacketID(id);
        msg.setBody(chatMessage.getBody());
        msg.setWith(chatMessage.getReceiverKey());
//		LogHtk.i(LogHtk.Test1, "XMPP message out: " + msg.toXML());
        xmppConnection.sendPacket(msg);
        APIManager.newMessageToHistory(chatMessage, id);
        //fix not update in other device
        if (chatMessage.getType().equals(ChatMessage.TYPE.chat.toString()) && !chatMessage.getReceiverKey().equals(userMe.getKey())) {
            String mReceiverJid = String.format("%s_%s@%s", userMe.getKey(), orgKey, ABSharedPreference.get(ABSharedPreference.KEY_XMPP_DOMAIN));
            msg.setTo(mReceiverJid);
            xmppConnection.sendPacket(msg);
        }

        realmManager.closeRealm();
    }

//    public void sendToMyself(XMPPMessage messageChatting, String with) {
//        // for  1-1: Need to send a message to myself
//        Message msg = new Message(mUserInfo.get_id() + "@" + DOMAIN, Message.Type.chat);
//        msg.setBody(messageChatting.getBody());
//        if(messageChatting.getFileAntBuddy() != null) {
//            FileAntBuddy file = messageChatting.getFileAntBuddy();
//            msg.setFile(new AntBuddyFile(file.getName(), file.getSize(), file.getFileUrl(), file.getMimeType(), file.getThumbnailUrl()));
//        }
//
//        msg.setWith(with);
//        mConnection.sendPacket(msg);
//    }

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

//		setmUserInfo(null);
//		setListRoom(null);
//		setListUser(null);
//        setmCurrentRouter(null);
//        reduceHistory(true);
//
//        // cancel all notification
//        if(myNotificationManager != null) {
//            myNotificationManager.cancelAll();
//        }
    }

    private static Pattern maskNameUser = Pattern.compile("@[a-zA-Z]+");
//	public boolean isCanShowNotification(XMPPMessage message, Context context)
//	{
//        // check gitlab message
//        if(message.getSenderName() == null && message.getBody().contains("new commit")) {
//            return true;
//        }
//
//		boolean iamIn = sharedSetting.getBoolean(SettingAB.A_MESSAGE_IS_SENT_TO_A_PRIVATE_ROOM_IAM_IN, true);
//		if (iamIn && message.getType().equalsIgnoreCase(ChatMessage.TYPE.groupchat.name()) && AntbuddyUtil.isPrivateRoom(message.getFromId(), getListRoom())) {
//			return true;
//		}
//
//		boolean privateMessage = sharedSetting.getBoolean(SettingAB.CHOISE_PRIVATE_MESSAGE, true);
//		if(privateMessage && message.getType().equalsIgnoreCase(ChatMessage.TYPE.chat.toString()))
//			return true;
//
//		boolean mentionInRoom = sharedSetting.getBoolean(SettingAB.CHOISE_MENTIIONNED_IN_ROOM, true);
//		if(mentionInRoom) {
//			String chat_text = message.getBody();
//			Matcher matcher = maskNameUser.matcher(chat_text);
//			while (matcher.find()) {
//				int k = matcher.start();
//				int m = matcher.end();
//				String subtext = chat_text.substring(k, m);
//				if (subtext.equalsIgnoreCase(EmojiconHandler.myName) || subtext.equalsIgnoreCase("@all"))
//					return true;
//			}
//		}
//		return false;
//	}

//    /**
//     * Display notification with sound when receiver message
//     *
//     * @param message
//     * @param context
//     */
//    private void displayNotification(XMPPMessage message, String roomId, Context context) {
//		if(isCanShowNotification(message, context) == false) {
//			return;
//		}
//
//        LogHtk.i(TAG, "displayNotification="+message.getBody());
//        String text = message.getBody();
//        String senderName = "gitlab BOT";
//
//        if(message.getSenderName() != null) {
//            senderName = message.getSenderName();
//        }
//        String title = senderName;
//        String ticker = senderName + " : " + message.getBody();
//
//        int num_message = 0;
//        if (notificationInfo.size() > 0 && notificationInfo.containsKey(roomId)) {
//            num_message = notificationInfo.get(roomId);
//            notificationInfo.put(roomId, ++num_message);
//        } else {
//            notificationInfo.put(roomId, ++num_message);
//        }
//
//        // check private chat or group chat
//        if (message.getType().equals(ChatMessage.TYPE.groupchat.name())) {
//            for(Room room : getListRoom()) {
////                if(room.get_id().equals(message.getSenderId())) {
//                if(room.get_id().equals(message.getFromId())) {
//                    title = room.getName();
//                    break;
//                }
//            }
//            text = ticker;
//        }
//		boolean isShowPopup = sharedSetting.getBoolean(SettingAB.SHOWING_A_POPUP, true);
//		if (isShowPopup) {
//
//			// Invoking the default notification service
//			NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
//
//			mBuilder.setWhen(System.currentTimeMillis());
//			mBuilder.setContentTitle(title);
//			mBuilder.setContentText(text);
//			mBuilder.setTicker(ticker);
//			mBuilder.setSmallIcon(R.drawable.ic_launcher);
//			mBuilder.setAutoCancel(true);
//			if (sharedSetting.getBoolean(SettingAB.PLAYING_A_SOUND, true)) {
//					mBuilder.setDefaults(Notification.DEFAULT_SOUND);
//			}
//
//
//			// Increase notification number every time a new notification arrives
//			mBuilder.setNumber(num_message);
//			if (num_message > 1) {
//				mBuilder.setContentText(num_message + " new messages");
//			}
//
//			Intent resultIntent = new Intent(context, MainActivity.class);
//			resultIntent.putExtra("roomJid", roomId);
//			resultIntent.setAction(String.valueOf(roomId));
//			resultIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//
//			PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0,
//					resultIntent, 0);
//
//			// start the activity when the user clicks the notification text
//			mBuilder.setContentIntent(resultPendingIntent);
//
//			myNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//
//			int position = 0;
//			for (int i = 0; i < getmUserInfo().getListRoomsOpeningChat().size(); i++) {
//				if (getmUserInfo().getListRoomsOpeningChat().get(i).getChatRoomId().equals(roomId)) {
//					position = i;
//					break;
//				}
//			}
//
//			// pass the Notification object to the system
//			myNotificationManager.notify(position, mBuilder.build());
//		}
//		else
//		{
//			if (sharedSetting.getBoolean(SettingAB.PLAYING_A_SOUND, true)) {
//				try {
//					Uri notification = getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//					Ringtone r = RingtoneManager.getRingtone(context, notification);
//					r.play();
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		}
//    }

    /**
     * Reset notification count with id
     *
     * @param jid
     */
    public void resetNotificationCount(String jid) {
        if (notificationInfo.containsKey(jid)) {
            notificationInfo.put(jid, 0);
        }
    }

    /**
     * Check package is foreground
     *
     * @param myPackage
     * @return
     */
    public boolean isForeground(String myPackage) {
        ActivityManager manager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTaskInfo = manager.getRunningTasks(1);

        ComponentName componentInfo = runningTaskInfo.get(0).topActivity;
        if (componentInfo.getPackageName().equals(myPackage)) return true;
        return false;
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