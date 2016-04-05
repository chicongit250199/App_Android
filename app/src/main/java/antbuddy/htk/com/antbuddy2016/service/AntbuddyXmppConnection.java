package antbuddy.htk.com.antbuddy2016.service;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.util.Log;

import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionCreationListener;
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
import java.util.regex.Pattern;

import antbuddy.htk.com.antbuddy2016.api.LoginAPI;
import antbuddy.htk.com.antbuddy2016.interfaces.XMPPReceiver;
import antbuddy.htk.com.antbuddy2016.model.ChatMessage;
import antbuddy.htk.com.antbuddy2016.model.ObjectManager;
import antbuddy.htk.com.antbuddy2016.model.Room;
import antbuddy.htk.com.antbuddy2016.model.UserMe;
import antbuddy.htk.com.antbuddy2016.objects.XMPPMessage;
import antbuddy.htk.com.antbuddy2016.util.BroadcastConstant;
import antbuddy.htk.com.antbuddy2016.util.Constants;
import antbuddy.htk.com.antbuddy2016.util.LogHtk;

public class AntbuddyXmppConnection {
	public static final String TAG = "AntbuddyXmppConnection";

	public static final String HOST_XMPP = "dev.antbuddy.com"; // wss://chat.htk.me/chatserver
	public static final String HOST_GETCOOKIE = "https://" + HOST_XMPP;
    public static final String HOST_SERVER_NODEJS = "https://" + HOST_XMPP;

	// Doman when OneToOneRoom
	public static final String DOMAIN = "htklabs.com";

	// Domain when Group Room
	public static final String DOMAINGROUP = "conference.htklabs.com";
	public static final int PORT = 5222; // Default: 5222

	// Used when connect to XMPP Server successful or unsuccessful.
	public static final String SERVICE_START_SUCCESS = "XMPP_START_SUCCESS";
	public static final String SERVICE_START_ERROR = "XMPP_START_ERROR";
	public static final String SERVICE_ALREADY_START = "XMPP_ALREADY_START";

	//private static Map<String, List<ChatMessage>> historyMessages;
	private static Context mContext;

	private static AntbuddyXmppConnection instance;

	synchronized public static AntbuddyXmppConnection getInstance() {
		if (instance == null) {
			instance = new AntbuddyXmppConnection();
			//historyMessages = new HashMap<String, List<ChatMessage>>();
		}
		return instance;
	}

//	synchronized public static boolean isInstance() {
//		return instance != null;
//	}

    /**
     * Notification
     */
    private NotificationManager myNotificationManager;
    private boolean isPaused = false;
    private Map<String, Integer> notificationInfo = new HashMap<String, Integer>(); // include notificationID and number messages

	/**
	 * mConnection: should be accessed only from _xmpp thread or from a thread where this variable is already guaranteed to be non null (for example, from the thread where smack listeners are
	 * invoked). null checks on this variable in non _xmpp thread are unsafe and meaningless.
	 */
	private XMPPConnection xmppConnection;

	//AntbuddyService antbuddyService;

	private ConnectionListener mConnectionListener;
	private PacketListener chatListener;
	private PacketListener groupChatListener;
    private PacketListener deleteListener;
	private PacketListener presenceListener;

	SharedPreferences sharedSetting;

//	//Information about User
//	private UserInfo mUserInfo;
//
//	//List all of rooms user can joint it.
//	private List<Room> listRoom;
//
//	//List all of users user can chat with them.
//	private List<User> listUser;
//
//	private OpeningChatRoom mCurrentRouter;
//
//	public OpeningChatRoom getmCurrentRouter() {
//		return mCurrentRouter;
//	}
//
//	public void setmCurrentRouter(OpeningChatRoom mCurrentRouter) {
//		this.mCurrentRouter = mCurrentRouter;
//	}
//
//	public UserInfo getmUserInfo() {
//		return mUserInfo;
//	}
//
//	public void setmUserInfo(UserInfo mUserInfo) {
//		this.mUserInfo = mUserInfo;
//	}
//
//	/**
//	 * @return the listRoom
//	 */
//	public List<Room> getListRoom() {
//		return listRoom;
//	}
//
//	/**
//	 * @param listRoom the listRoom to set
//	 */
//	public void setListRoom(List<Room> listRoom) {
//		this.listRoom = listRoom;
//	}
//
//	/**
//	 * @return the listUser
//	 */
//	public List<User> getListUser() {
//		return listUser;
//	}
//
//	/**
//	 * @param listUser the listUser to set
//	 */
//	public void setListUser(List<User> listUser) {
//		this.listUser = listUser;
//	}

	/**
	 * should be called from _xmpp thread only
	 */
	public XMPPConnection getConnection() {
		return xmppConnection;
	}

	/**
	 * set connectionsenderId
	 * @param connection
	 */
	public void setConnection(XMPPConnection connection) {
		xmppConnection = connection;
	}

	public void connectXMPP(final Context context, final String username, final String pass , final XMPPReceiver receiver) {
		if (xmppConnection != null) {
			receiver.onSuccess(SERVICE_ALREADY_START);
			return;
		}

		mContext = context;
		ConnectionConfiguration connConfig = new ConnectionConfiguration(Constants.HOST_XMPP, Constants.PORT_XMPP, Constants.DOMAIN_XMPP);
		XMPPConnection connection = new XMPPConnection(connConfig);
		try {
			connection.connect();
		} catch (XMPPException ex) {
            ex.printStackTrace();
			xmppConnection = null;
			receiver.onError("Failed to connect to " + connection.getHost());
			return;
		}

		ProviderManager pm = ProviderManager.getInstance();
		pm.addIQProvider("command", "http://jabber.org/protocol/commands", new AdHocCommandDataProvider());

		try {
			SASLAuthentication.supportSASLMechanism("PLAIN", 0);
            String android_id = Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
            connection.login(username, pass, android_id);

			// After connect successful
			// set connectionXmpp
			xmppConnection = connection;
			xmppConnection.setConnected(true);

			// Register: MessageListener, ConnectionListener, PresenceListener
			addMessageListener();
			addConnectionListener();
			addPresenceListener();

			// send presence out to Server XMPP
			sendPresenceOutFromOpeningRooms();

			receiver.onSuccess(SERVICE_START_SUCCESS);
		} catch (XMPPException ex) {
            ex.printStackTrace();
			xmppConnection = null;
			receiver.onError(SERVICE_START_ERROR);
		} catch (Exception e) {
			e.printStackTrace();
			xmppConnection = null;
			receiver.onError(SERVICE_START_ERROR);
		}
	}

	/**
	 * Add message listener
	 */
	private void addMessageListener() {
		PacketFilter messageFilter = new MessageTypeFilter(Type.chat);
		chatListener = new PacketListener() {
			public void processPacket(Packet packet) {
				processMessageReceived(packet);
			}
		};

		PacketFilter groupChatFilter = new MessageTypeFilter(Type.groupchat);
		groupChatListener = new PacketListener() {
			public void processPacket(Packet packet) {
				processMessageReceived(packet);
			}
		};

        PacketFilter messageDelete = new MessageTypeFilter(Type.event);
        deleteListener = new PacketListener() {
            public void processPacket(Packet packet) {
                processMessageReceived(packet);
            }
        };
        // register packet listener
		xmppConnection.addPacketListener(chatListener, messageFilter);
		xmppConnection.addPacketListener(deleteListener, messageDelete);
		xmppConnection.addPacketListener(groupChatListener, groupChatFilter);
	}


//	public void setAntbuddyService(AntbuddyService antbuddyService) {
//		this.antbuddyService = antbuddyService;
//	}

	/**
	 * part packet to message object and later broadcast it to activity
	 * al
	 * @param packet
	 */
	private void processMessageReceived(Packet packet) {

		Message message = (Message) packet;
		LogHtk.d(LogHtk.XMPP_TAG, "XMPP Message Received: " + message.getBody());

		if (message.getBody() != null) {
			ChatMessage chatMessage = new ChatMessage(message);
			Intent intent = new Intent(BroadcastConstant.BROAD_CAST_RECEIVER_CHAT);
			intent.putExtra(BroadcastConstant.MESSAGE_RECEIVE, chatMessage);
			mContext.sendBroadcast(intent);
		}

//        boolean isDeleteMessage = false;
//		if (message.getBody() != null) {
//
//            LogHtk.i(TAG, "<==== MESSAGE RECEIVED: " + message.toXML());
//            // convert message from XMPP to message object in Java
//			ChatMessage chatMessage = ChatMessage.messageToChatMessage(message, getListUser(), mUserInfo);
//			if(message.getExpandBody() != null)
//				chatMessage.setExpandBody(message.getExpandBody());
//            // analyse message object
//            if(message.getExtension("replace", "urn:xmpp:message-correct:0") != null) {
//                chatMessage.setModified(true);
//            }
//
//            if(message.getSubtype() != null ) {
//                if (message.getSubtype().equals("delete-message")) {
//                    chatMessage.setId(chatMessage.getBody());
//                    isDeleteMessage = true;
//                }
//            }
//
//			String roomId = AntbuddyUtil.getRoomId(chatMessage, mUserInfo.get_id());
//            if(!isDeleteMessage) {
//				addMessageToHistory(roomId, chatMessage);
//            } else {
//				removeMessageToHistory(roomId, chatMessage);
//            }
//
//            // sent message object to Activity
//			Intent intent = new Intent(BroadcastConstant.BROAD_CAST_RECEIVER_CHAT);
//			intent.putExtra(AntbuddyConstant.MESSAGE_RECEIVE, chatMessage);
//			mContext.sendBroadcast(intent);
//
//            if(!isForeground("com.htk.antbuddy") || getmCurrentRouter() == null || !getmCurrentRouter().getChatRoomId().equals(roomId)) {
//				displayNotification(chatMessage, roomId, mContext);
//            }
//		}
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
				System.out.println("reconnectionSuccessful");
				sendPresenceOutFromOpeningRooms();
			}

			@Override
			public void reconnectionFailed(Exception e) {
//				Intent intent = new Intent(BroadcastConstant.BROAD_CAST_CONNECTION_STATUS);
//				intent.putExtra(AntbuddyConstant.CONNECTION_STATUS, AntbuddyConstant.CONNECTION_STATUS_FAIL_MESSAGE);
//				mContext.sendBroadcast(intent);
//				System.out.println("reconnectionFailed");
			}

			@Override
			public void reconnectingIn(int seconds) {
//				Intent intent = new Intent(BroadcastConstant.BROAD_CAST_CONNECTION_STATUS);
//				intent.putExtra(AntbuddyConstant.CONNECTION_STATUS, AntbuddyConstant.CONNECTION_STATUS_RECONNECTING_MESSAGE + seconds + " seconds");
//				mContext.sendBroadcast(intent);
//				System.out.println("reconnectingIn " + seconds);
			}

			@Override
			public void connectionClosedOnError(Exception e) {
				System.out.println("connectionClosedOnError");
			}

			@Override
			public void connectionClosed() {
//				Intent intent = new Intent(BroadcastConstant.BROAD_CAST_CONNECTION_STATUS);
//				intent.putExtra(AntbuddyConstant.CONNECTION_STATUS, AntbuddyConstant.CONNECTION_STATUS_CLOSED_MESSAGE);
//				mContext.sendBroadcast(intent);
//				System.out.println("connectionClosed");
			}
		};
		xmppConnection.addConnectionListener(mConnectionListener);
	}

	/**
	 * Add presence listener
	 */
	private void addPresenceListener() {
		PacketTypeFilter presenceFilter = new PacketTypeFilter(Presence.class);
		presenceListener = new PacketListener() {
			public void processPacket(Packet packet) {
				Presence presence = (Presence) packet;
                LogHtk.i(LogHtk.XMPP_TAG, "IN/PRESENCE: " + presence.toXML());


//				Intent intent = new Intent(BroadcastConstant.BROAD_CAST_RECEIVER_PRESENCE);
//				intent.putExtra(AntbuddyConstant.PRESENCE_FROM, presence.getFrom());
//				intent.putExtra(AntbuddyConstant.PRESENCE_TYPE, presence.getType());
//				intent.putExtra(AntbuddyConstant.PRESENCE_MODE, presence.getMode());
//				mContext.sendBroadcast(intent);
			}
		};
		xmppConnection.addPacketListener(presenceListener, presenceFilter);
	}

	public void sendMessageOut(ChatMessage chatMessage) {
		if(!xmppConnection.isConnected()) return;
		Message msg = null;
		UserMe userMe = ObjectManager.getInstance().getUserMe();
		if (userMe == null) {
			return;
		}

		UserMe.Org currentOrg = userMe.getCurrentOrg();
		if (currentOrg == null) {
			return;
		}
		String orgKey = currentOrg.getOrgKey();

		final String userMeKey = userMe.getKey();
		final String chatMucDomain = userMe.getChatMucDomain();
		String receiverJid;
		Message.Type type;
		if(chatMessage.getType().equals("groupchat")) {
			receiverJid = String.format("%s_%s@%s", chatMessage.getReceiverKey(), orgKey, chatMucDomain);
			LogHtk.i(LogHtk.Test1, "receiverJid =" + receiverJid);
			type = Message.Type.groupchat;
		} else {
			receiverJid = String.format("%s_%s@%s", chatMessage.getReceiverKey(), orgKey, Constants.DOMAIN_XMPP);
			type = Message.Type.chat;
		}

		msg = new Message(receiverJid, type);
		msg.setBody(chatMessage.getBody());
		msg.setWith(chatMessage.getReceiverKey());
		xmppConnection.sendPacket(msg);

		LoginAPI.newMessageToHistory(chatMessage);
		//fix not update in other device
		if (chatMessage.getType().equals(ChatMessage.TYPE.chat.toString()) && !chatMessage.getReceiverKey().equals(userMe.getKey())) {
			String mReceiverJid = String.format("%s_%s@%s", userMe.getKey(), orgKey, Constants.DOMAIN_XMPP);
			msg.setTo(mReceiverJid);
			xmppConnection.sendPacket(msg);
		}

	}

	/**
	 * Send message out
	 * @param messageChatting
	 */
	public void sendMessageOut(XMPPMessage messageChatting) {
//		if(!mConnection.isConnected()) return;
//		Message msg = null;
//
//        // GROUP
//		if (messageChatting.getType().equals(ChatMessage.TYPE.groupchat.toString())) {
//			msg = new Message(messageChatting.getReceiverJid(), Message.Type.groupchat);
//		}addConnectionCreationListener
//
//        // 1-1
//        if (messageChatting.getType().equals(ChatMessage.TYPE.chat.toString())) {
//			msg = new Message(messageChatting.getReceiverJid(), Message.Type.chat);
//            sendToMyself(messageChatting, messageChatting.getReceiverId());
//		}
//
//        if(messageChatting.getFileAntBuddy() != null) {
//            FileAntBuddy file = messageChatting.getFileAntBuddy();
//            msg.setFile(new AntBuddyFile(file.getName(), file.getSize(), file.getFileUrl(), file.getMimeType(), file.getThumbnailUrl()));
//            msg.setBody(file.getName());
//        }
//        else {
//            msg.setBody(messageChatting.getBody());
//        }
//
//		mConnection.sendPacket(msg);
//        LogHtk.i(TAG, "====> MESSAGE SENT OUT: " + msg.toXML());
//
//		try {
//            String result = Request.requestURLSendMessageOut(messageChatting);
//            if (result.equals(Request.RESPONSE_RESULT.ERROR_REQUEST+"")) {
//                Toast.makeText(mContext, "Timeout send message out!", Toast.LENGTH_SHORT).show();
//            }
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}

    public void sendToMyself(XMPPMessage messageChatting, String with) {
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
    }

	/*
	 * In CHATGROUP case:
	 * we must send Presence assigned to notification GroupChatRoom. Then you can send Message to Room.
	 */
	private void sendPresenceOutFromOpeningRooms() {
		ObjectManager.getInstance().setOnListenerRoom(this.getClass(), new ObjectManager.OnListenerGroup() {
			@Override
			public void onResponse(List<Room> listRooms) {
				for (Room room : listRooms) {
					Presence presence = new Presence(org.jivesoftware.smack.packet.Presence.Type.available);
					presence.setTo(room.getKey() + "_475a400a-292b-440c-981a-57af0b3f9a2c" + "@conference.antbuddy.com/756651f0-9196-11e5-a569-fdc7cdc19515_475a400a-292b-440c-981a-57af0b3f9a2c");
					if (xmppConnection != null) {
						xmppConnection.sendPacket(presence);
						Log.i("Hoa debug", "AntbuddyXmppConnection:sendPresenceOutFromOpeningRooms: presence.toXML() = " + presence.toXML());
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						LogHtk.i(TAG, "Out/GROUP_PRESENCE: " + presence.toXML());
					}
				}
			}
		});
//
	}

	/**
	 * Add ChatMessage to history
	 * @param sendToJid
	 * @param chatMessage
	 */
	private void addMessageToHistory(String sendToJid, XMPPMessage chatMessage) {
//		// if historyMessages contians key
//		List<ChatMessage> chatlist = historyMessages.get(sendToJid);
//		if(chatlist == null) {
//			chatlist = new ArrayList<>();
//			historyMessages.put(sendToJid, chatlist);
//		}
//        if(chatMessage.isModified()){
//            for (ChatMessage mHistory : chatlist) {
//                if(chatMessage.getId().equals(mHistory.getId())){
//                    mHistory.setBody(chatMessage.getBody());
//                    mHistory.setModified(true);
//                }
//            }
//        } else {
//            boolean isFind = false;
//            for (ChatMessage mHistory : chatlist) {
//                if(chatMessage.getId().equals(mHistory.getId())){
//                    isFind = true;
//                    break;
//                }
//            }
//            if(!isFind)
//                chatlist.add(chatMessage);
//        }
	}

    private void removeMessageToHistory(String sendToJid, XMPPMessage chatMessage) {
//        // if historyMessages contians key
//        List<ChatMessage> chatlist = historyMessages.get(sendToJid);
//        if(chatlist == null) {
//            chatlist = new ArrayList<>();
//            historyMessages.put(sendToJid, chatlist);
//        }
//        for (ChatMessage mHistory : chatlist) {
//            if(chatMessage.getId().equals(mHistory.getId()) || chatMessage.getBody().equals(mHistory.getId())){
//               chatlist.remove(mHistory);
////                mHistory.setModified(true);
//            }
//        }
    }

	/*
	This function for load more
	 */
	private List<XMPPMessage> addMessagesToHistoryWithJid(String jid, List<XMPPMessage> chatMessages) {
//		// if historyMessages contians key
//		List<ChatMessage> chatlist = historyMessages.get(jid);
//		if(chatlist == null) {
//			chatlist = new ArrayList<>();
//			historyMessages.put(jid, chatlist);
//		}
//		chatlist.addAll(0, chatMessages);
//		return chatlist;
		return null;
	}

    /*
	This function for load more
	 */
    private List<XMPPMessage> removeMessagesFromHistoryWithJid(String jid, List<XMPPMessage> chatMessages) {
//        // if historyMessages contians key
//        List<ChatMessage> chatlist = historyMessages.get(jid);
//        if(chatlist == null) {
//            chatlist = new ArrayList<>();
//            historyMessages.put(jid, chatlist);
//        }
//        chatlist.addAll(0, chatMessages);
//        return chatlist;
		return null;
    }

	// _jIdRouter : jid ko co @
	public List<XMPPMessage> getListHistoryMessages(String idRouter) {
//		return historyMessages.get(idRouter);
		return null;
	}

    // _jIdRouter : jid ko co @
    public void clearHistoryMessages(String idRouter) {
//        if(historyMessages.containsKey(idRouter))
//            historyMessages.remove(idRouter);
    }

    /**
     * Get more messages from server
     * @param idRouter
     * @param dateInString
     * @param type
     * @param limit
     * @return
     */
	private List<XMPPMessage> loadHistoryFromServer(String idRouter, String dateInString, String type, String limit) { // jidRouter can chatRoomId Or UserIdOneToOne

//		String urlFull = "/api/messages?chatRoom=" + idRouter + "&limit=" + limit + "&type=" + type; // chat or groupchat
//
//		if(dateInString != null) {
//            urlFull += "&before=" + dateInString + "%2B00:00";
//		}
//		List<ChatMessage> list = null;
//		try {
//			String result = Request.connect(urlFull);
//            if(TextUtils.isEmpty(result) || result.equals(Request.RESPONSE_RESULT.ERROR_REQUEST.toString())) {
//                list = new ArrayList<>();
//                android.os.Handler handler = new android.os.Handler(Looper.getMainLooper());
//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(mContext, "This task take more long time", Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//            } else if(result.equals("[]")) {
//                android.os.Handler handler = new android.os.Handler(Looper.getMainLooper());
//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(mContext, "Don't have anymore message", Toast.LENGTH_SHORT).show();
//                    }
//                });
//                list = new ArrayList<>();
//            } else {
//				list = ChatMessage.parseListHistoryMessages(new JSONArray(result), listUser);
//			}
//			return list;
//		} catch (JSONException e) {
//            e.printStackTrace();
//        } catch (Exception e) {
//            e.printStackTrace();
//		}
//		return list;
		return null;
	}

    /**
     * Get more messages from server
     * @param router
     * @return
     */
//	public List<XMPPMessage> getMoreListHistoryMessages(final RouterInfo router) {
//		List<ChatMessage> currentList = getListHistoryMessages(router.get_id());
//		List<ChatMessage> resultList = null;
//		long dateInLongLastMessage = 0;
//
//		try {
//		if(currentList == null || currentList.size() == 0) {
//			dateInLongLastMessage = System.currentTimeMillis();
//		} else {
//			ChatMessage topMessage = currentList.get(0);
//			dateInLongLastMessage = topMessage.getDatetime();
//
//		}
//
//		String dateInStringLastMessage = NationalTime.convertDateInLongtoDate(dateInLongLastMessage);
//		String localTimeToUTCTime = NationalTime.convertLocalTimeToUTCTime(dateInStringLastMessage);
//		resultList = loadHistoryFromServer(router.get_id(), localTimeToUTCTime, router.getIsMuc()?"groupchat":"chat", "10");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//        //Collections.reverse(resultList);
//        List<ChatMessage> loopList = new ArrayList<>();
//        loopList.addAll(0, resultList);
//
//        if(currentList != null && currentList.size() != 0) {
//            for (ChatMessage mCurrent : currentList) {
//                for (ChatMessage mHistory : loopList) {
//                    if (mCurrent.getId().equals(mHistory.getId())) {
//                        resultList.remove(mHistory);
//                    }
//                }
//            }
//        }
//        addMessagesToHistoryWithJid(router.get_id(), resultList);
//		return resultList;
//	}

    /**
     * Handle disconnect
     */
	public void disconnect() {

		if (xmppConnection != null) {
			xmppConnection.setConnected(false);

			if (mConnectionListener != null) {
				xmppConnection.removeConnectionListener(mConnectionListener);
			}

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

    /**
     * Clear message when out app
     * @param isFullClean : true -> clear all
     *                    : false -> clear chat jid have more 10 messages
     */
	public void reduceHistory(boolean isFullClean) {
//		for(Entry<String, List<ChatMessage>> entry : historyMessages.entrySet()) {
//		    String key = entry.getKey();
//		    List<ChatMessage> value = entry.getValue();
//
//		    if(isFullClean) {
//		    	value.clear();
//		    } else if(value.size() > 10) {
//		    	value.clear();
//		    }
//		}
	}
	private static Pattern maskNameUser = Pattern.compile("@[a-zA-Z]+");
	public boolean isCanShowNotification(XMPPMessage message, Context context)
	{
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
		return false;
	}
    /**
     * Display notification with sound when receiver message
     *
     * @param message
     * @param context
     */
    private void displayNotification(XMPPMessage message, String roomId, Context context) {
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
    }

    /**
     * Reset notification count with id
     * @param jid
     */
    public void resetNotificationCount(String jid) {
        if(notificationInfo.containsKey(jid)) {
            notificationInfo.put(jid, 0);
        }
    }

    /**
     * Check package is foreground
     * @param myPackage
     * @return
     */
    public boolean isForeground(String myPackage){
        ActivityManager manager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List< ActivityManager.RunningTaskInfo > runningTaskInfo = manager.getRunningTasks(1);

        ComponentName componentInfo = runningTaskInfo.get(0).topActivity;
        if(componentInfo.getPackageName().equals(myPackage)) return true;
        return false;
    }

}