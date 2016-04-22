package antbuddy.htk.com.antbuddy2016.service;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.os.Looper;

import java.util.EmptyStackException;
import java.util.List;

import antbuddy.htk.com.antbuddy2016.GsonObjects.GChatMassage;
import antbuddy.htk.com.antbuddy2016.GsonObjects.GFileAntBuddy;
import antbuddy.htk.com.antbuddy2016.RealmObjects.RChatMessage;
import antbuddy.htk.com.antbuddy2016.RealmObjects.RFileAntBuddy;
import antbuddy.htk.com.antbuddy2016.RealmObjects.RObjectManager;
import antbuddy.htk.com.antbuddy2016.RealmObjects.RObjectManagerBackGround;
import antbuddy.htk.com.antbuddy2016.RealmObjects.RObjectManagerOne;
import antbuddy.htk.com.antbuddy2016.RealmObjects.RUserMe;
import antbuddy.htk.com.antbuddy2016.api.APIManager;
import antbuddy.htk.com.antbuddy2016.interfaces.HttpRequestReceiver;
import antbuddy.htk.com.antbuddy2016.interfaces.XMPPReceiver;
import antbuddy.htk.com.antbuddy2016.model.ChatMessage;
import antbuddy.htk.com.antbuddy2016.model.Room;
import antbuddy.htk.com.antbuddy2016.model.User;
import antbuddy.htk.com.antbuddy2016.model.UserMe;
import antbuddy.htk.com.antbuddy2016.util.BroadcastConstant;
import antbuddy.htk.com.antbuddy2016.util.LogHtk;

public class AntbuddyService extends Service {

    private static AntbuddyXmppConnection mXmppConnection;
    public static AntbuddyService mAntbuddyService;

    private final IBinder serviceBinder = new LocalBinder();
    public class LocalBinder extends Binder {
        public AntbuddyService getService() {
            mAntbuddyService = AntbuddyService.this;
			LogHtk.i(LogHtk.Test2, "LocalBinder return service!");
            return mAntbuddyService;
        }
    }

	public synchronized static AntbuddyService getInstance() {
		if (mAntbuddyService == null) {
			AntbuddyApplication.getInstance().startService(new Intent(AntbuddyApplication.getInstance().getApplicationContext(), AntbuddyService.class));
		}
		return mAntbuddyService;
	}

	private SharedPreferences prefWrapper;
//	private ReceiverBroadCast mReceiver;

	@Override
	public void onCreate() {
		LogHtk.i(LogHtk.Test2, "Service onCreate!");
		super.onCreate();

		mAntbuddyService = AntbuddyService.this;
	}

	@Override
	public IBinder onBind(Intent intent) {
		LogHtk.i(LogHtk.Test2, "Service onBind!");
		return serviceBinder;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		LogHtk.d(LogHtk.SERVICE_TAG, "------------------>onStartCommand SERVICE<----------------");

		loginXMPP();
		return START_STICKY;
	}

	/**
	 * Save token when app kill
	 */
	private void saveService() {
		SharedPreferences.Editor editor = prefWrapper.edit();
		//editor.putString(SHARED_TOKEN, AntbuddyConfig.COOKIE);
		editor.commit();
		//LogHtk.i(LogHtk.SERVICE_TAG, "Cookie saved in SharedPreferences: " + ((AntbuddyConfig.COOKIE == null) ? "NULL" : AntbuddyConfig.COOKIE));
	}

	@Override
	public void onTaskRemoved(Intent rootIntent) {
		LogHtk.d(LogHtk.SERVICE_TAG, "------------------>onTaskRemoved SERVICE<----------------");
		//saveService();
		super.onTaskRemoved(rootIntent);
		//stop
	}

	@Override
	public void onDestroy() {
		LogHtk.e(LogHtk.SERVICE_TAG, "------------------>onDestroy SERVICE<----------------");

		startService(new Intent(this, AntbuddyService.class));
	}

//	public boolean isXMPPConnected() throws Exception {
//		if (AntbuddyXmppConnection.getInstance() == null) {
//			throw new Exception();
//		} else {
//			return mXmppConnection.getConnection().isConnected();
//		}
//	}

	public void loadUserMe() {
		APIManager.GETUserMe(new HttpRequestReceiver<UserMe>() {
			@Override
			public void onSuccess(final UserMe me) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						RObjectManagerOne realmManager = new RObjectManagerOne();
						realmManager.saveUserMeOrUpdate(me);
						realmManager.closeRealm();
					}
				});
			}

			@Override
			public void onError(String error) {
				LogHtk.e(LogHtk.ErrorHTK, "Error! Can not load UserMe from server!");
			}
		});
	}

	// API request
	public void loading_UserMe_Users_Rooms() {
		APIManager.GETUserMe(new HttpRequestReceiver<UserMe>() {
			@Override        // Main Thread
			public void onSuccess(final UserMe me) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						RObjectManagerOne realmManager = new RObjectManagerOne();
						realmManager.saveUserMeOrUpdate(me);
						realmManager.closeRealm();
						loadUsers();
					}
				}).start();
			}

			@Override        // Main Thread
			public void onError(String error) {
				Intent intent = new Intent(BroadcastConstant.CENTER_LOADING_DATA_SUCEESS);
				intent.putExtra("loadingResult", error);
				getApplicationContext().sendBroadcast(intent);
			}
		});
	}

	public void loadUsers() {
		APIManager.GETUsers(new HttpRequestReceiver<List<User>>() {
			@Override
			public void onSuccess(final List<User> users) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						RObjectManagerOne realmManager = new RObjectManagerOne();
						realmManager.saveUsersOrUpdate(users);
						realmManager.closeRealm();
						loadRooms();
					}
				}).start();
			}

			@Override
			public void onError(String error) {
				LogHtk.e(LogHtk.ErrorHTK, "Error! Can not load Users from server!");
				Intent intent = new Intent(BroadcastConstant.CENTER_LOADING_DATA_SUCEESS);
				intent.putExtra("loadingResult", "noUsers");
				getApplicationContext().sendBroadcast(intent);
			}
		});
	}

	private void loadRooms() {
		APIManager.GETGroups(new HttpRequestReceiver<List<Room>>() {
			@Override
			public void onSuccess(final List<Room> rooms) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						RObjectManagerOne realmManager = new RObjectManagerOne();
						realmManager.saveRoomsOrUpdate(rooms);
						realmManager.closeRealm();
						Intent intent = new Intent(BroadcastConstant.CENTER_LOADING_DATA_SUCEESS);
						intent.putExtra("loadingResult", "yes");
						getApplicationContext().sendBroadcast(intent);
					}
				}).start();
			}

			@Override
			public void onError(String error) {
				LogHtk.e(LogHtk.ErrorHTK, "Error! Can not load Rooms from server!");
				Intent intent = new Intent(BroadcastConstant.CENTER_LOADING_DATA_SUCEESS);
				intent.putExtra("loadingResult", "noRooms");
				getApplicationContext().sendBroadcast(intent);
			}
		});
	}

	public AntbuddyXmppConnection getXMPPConnection() {
		return mXmppConnection;
	}

	public void loginXMPP() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				LogHtk.i(LogHtk.SERVICE_TAG, "------------------>loginXMPP<----------------");
				RObjectManagerBackGround rObjectManagerBG = new RObjectManagerBackGround();
				mXmppConnection = AntbuddyXmppConnection.getInstance();
				mXmppConnection.connectXMPP(rObjectManagerBG.getUserMeFromDB());
				rObjectManagerBG.closeRealm();
			}
		}).start();


//		mXmppConnection = AntbuddyXmppConnection.getInstance();
//		mXmppConnection.connectXMPP(AntbuddyService.this, new XMPPReceiver() {
//			@Override
//			public void onSuccess(String result) {
//				LogHtk.d(LogHtk.XMPP_TAG, "Login and Connect XMPP:  " + result);
//			}
//
//			@Override
//			public void onError(String error) {
//				LogHtk.e(LogHtk.XMPP_TAG, "Login and Connect XMPP " + error);
//			}
//		});
	}

	public void sendMessageOut(RChatMessage chatMessage) {
		mXmppConnection = AntbuddyXmppConnection.getInstance();
		mXmppConnection.messageOUT(chatMessage);
	}

	public void resetXMPP() {
		mXmppConnection = AntbuddyXmppConnection.getInstance();
		mXmppConnection.disconnect();
		AntbuddyXmppConnection.instance = null;
	}

	public void loadMessage(String before, String keyRoom, boolean isGroup) {
		Intent intent = new Intent(BroadcastConstant.LOAD_MORE_CHATMESSAGE);
		intent.putExtra("loadMessage", "start");
		getApplicationContext().sendBroadcast(intent);

		APIManager.GETMessages1(before, keyRoom, (isGroup ? "groupchat" : "chat"), new HttpRequestReceiver<List<GChatMassage>>() {
			@Override
			public void onSuccess(final List<GChatMassage> listMessages) {
				LogHtk.i(LogHtk.Test1, "----loadMoreMessages1---");
				LogHtk.i(LogHtk.Test1, "listMessages: " + listMessages.size());

				if (listMessages.size() > 0) {
					LogHtk.i(LogHtk.Test1, "----111---");
					Intent intent = new Intent(BroadcastConstant.LOAD_MORE_CHATMESSAGE);
					intent.putExtra("loadMessage", "loaded");
					intent.putExtra("sizeMessages", listMessages.size());
					getApplicationContext().sendBroadcast(intent);
					LogHtk.i(LogHtk.Test1, "----222---");
					new Thread(new Runnable() {
						@Override
						public void run() {
							RObjectManagerBackGround realmBG = new RObjectManagerBackGround();

							for (int i = 0; i < listMessages.size(); i++) {
								GChatMassage mess = listMessages.get(i);

								RChatMessage messageSaved = new RChatMessage();
								messageSaved.setId(mess.getId());
								messageSaved.setSenderJid(mess.getSenderJid());
								messageSaved.setSenderId(mess.getSenderId());
								messageSaved.setSenderName(mess.getSenderName());
								messageSaved.setBody(mess.getBody());
								messageSaved.setFromId(mess.getFromId());
								messageSaved.setReceiverJid(mess.getReceiverJid());
								messageSaved.setReceiverId(mess.getReceiverId());
								messageSaved.setReceiverName(mess.getReceiverName());
								messageSaved.setIsModified(mess.isModified());
								messageSaved.setType(mess.getType());
								messageSaved.setSubtype(mess.getSubtype());
								messageSaved.setTime(mess.getTime());
								messageSaved.setExpandBody(mess.getExpandBody());
								messageSaved.setOrg(mess.getOrg());
								messageSaved.setSenderKey(mess.getSenderKey());
								messageSaved.setFromKey(mess.getFromKey());
								messageSaved.setReceiverKey(mess.getReceiverKey());

								GFileAntBuddy gFile = mess.getFileAntBuddy();
								if (gFile != null) {
									RFileAntBuddy rFile = new RFileAntBuddy();
									rFile.setSize(gFile.getSize());
									rFile.setFileUrl(gFile.getFileUrl());
									rFile.setMimeType(gFile.getMimeType());
									rFile.setThumbnailUrl(gFile.getThumbnailUrl());
									rFile.setThumbnailWidth(gFile.getThumbnailWidth());
									rFile.setThumbnailHeight(gFile.getThumbnailHeight());
									messageSaved.setFileAntBuddy(rFile);
								}

								realmBG.saveMessage(messageSaved);
							}
							realmBG.closeRealm();
							Intent intent = new Intent(BroadcastConstant.LOAD_MORE_CHATMESSAGE);
							intent.putExtra("loadMessage", "saved");
							intent.putExtra("sizeMessages", listMessages.size());
							getApplicationContext().sendBroadcast(intent);
							LogHtk.i(LogHtk.Test1, "----444---");
						}
					}).start();
				} else {
					Intent intent = new Intent(BroadcastConstant.LOAD_MORE_CHATMESSAGE);
					intent.putExtra("loadMessage", "empty");
					intent.putExtra("sizeMessages", listMessages.size());
					getApplicationContext().sendBroadcast(intent);
				}

				//mSwipyRefreshLayout.setRefreshing(false);
			}

			@Override
			public void onError(String error) {
				LogHtk.i(LogHtk.ChatActivity, "-->Error");
				//mSwipyRefreshLayout.setRefreshing(false);
				Intent intent = new Intent(BroadcastConstant.LOAD_MORE_CHATMESSAGE);
				intent.putExtra("loadMessage", "error");
				getApplicationContext().sendBroadcast(intent);
			}
		});
	}

//	public String logout() throws RemoteException {
//		AntbuddyConfig.COOKIE = null;
//		saveSevice();
//		AntbuddyXmppConnection.getInstance().disconnect();
//		return AntbuddyConfig.LOGOUT_SUCCESS;
//	}
}
