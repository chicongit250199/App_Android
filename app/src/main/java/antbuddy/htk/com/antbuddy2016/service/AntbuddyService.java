package antbuddy.htk.com.antbuddy2016.service;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;

import java.util.EmptyStackException;
import java.util.List;

import antbuddy.htk.com.antbuddy2016.RealmObjects.RObjectManager;
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

		new Thread(new Runnable() {
			@Override
			public void run() {
				loginXMPP();
			}
		}).start();
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

	public boolean isXMPPConnected() throws Exception {
		if (AntbuddyXmppConnection.getInstance() == null) {
			throw new Exception();
		} else {
			return mXmppConnection.getConnection().isConnected();
		}
	}

	public void loadUserMe() {
		APIManager.GETUserMe(new HttpRequestReceiver<UserMe>() {
			@Override
			public void onSuccess(UserMe me) {
				LogHtk.i(LogHtk.Test3, "Userme loaded at Service!");
				RObjectManager.getInstance().saveUserMeOrUpdate(me);
			}

			@Override
			public void onError(String error) {
				LogHtk.e(LogHtk.SERVICE_TAG, "Error! Can not load UserMe from server!");
			}
		});
	}

	// API request
	public void loading_UserMe_Users_Rooms() {
		APIManager.GETUserMe(new HttpRequestReceiver<UserMe>() {
			@Override
			public void onSuccess(UserMe me) {
				LogHtk.i(LogHtk.Test3, "Userme loaded at Service!");
				RObjectManager.getInstance().saveUserMeOrUpdate(me);
				loadUsers();
			}

			@Override
			public void onError(String error) {
				LogHtk.e(LogHtk.SERVICE_TAG, "Error! Can not load UserMe from server!");
				Intent intent = new Intent(BroadcastConstant.CENTER_LOADING_DATA_SUCEESS);
				intent.putExtra("loadingResult", "noUserMe");
				getApplicationContext().sendBroadcast(intent);
			}
		});
	}

	public void loadUsers() {
		LogHtk.i(LogHtk.Test3, "Service loadUsers!");
		APIManager.GETUsers(new HttpRequestReceiver<List<User>>() {
			@Override
			public void onSuccess(List<User> users) {
				RObjectManager.getInstance().saveUsersOrUpdate(users);
				LogHtk.i(LogHtk.Test3, "Service loadUsers!");
				loadRooms();
			}

			@Override
			public void onError(String error) {
				LogHtk.e(LogHtk.SERVICE_TAG, "Error! Can not load Users from server!");
				Intent intent = new Intent(BroadcastConstant.CENTER_LOADING_DATA_SUCEESS);
				intent.putExtra("loadingResult", "noUsers");
				getApplicationContext().sendBroadcast(intent);
			}
		});
	}

	private void loadRooms() {
		LogHtk.i(LogHtk.Test3, "Service loadRooms!");
		APIManager.GETGroups(new HttpRequestReceiver<List<Room>>() {
			@Override
			public void onSuccess(List<Room> rooms) {
				RObjectManager.getInstance().saveRoomsOrUpdate(rooms);

				Intent intent = new Intent(BroadcastConstant.CENTER_LOADING_DATA_SUCEESS);
				intent.putExtra("loadingResult", "yes");
				getApplicationContext().sendBroadcast(intent);
			}

			@Override
			public void onError(String error) {
				LogHtk.e(LogHtk.SERVICE_TAG, "Error! Can not load Rooms from server!");
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
		LogHtk.i(LogHtk.SERVICE_TAG, "------------------>loginXMPP<----------------");
		mXmppConnection = AntbuddyXmppConnection.getInstance();
		mXmppConnection.connectXMPP(AntbuddyService.this, new XMPPReceiver() {
			@Override
			public void onSuccess(String result) {
				LogHtk.d(LogHtk.XMPP_TAG, "Login and Connect XMPP:  " + result);
			}

			@Override
			public void onError(String error) {
				LogHtk.e(LogHtk.XMPP_TAG, "Login and Connect XMPP " + error);
			}
		});
	}

	public void sendMessageOut(ChatMessage chatMessage) {
		mXmppConnection = AntbuddyXmppConnection.getInstance();
		mXmppConnection.sendMessageOut(chatMessage);
	}

	public void resetXMPP() {
		mXmppConnection = AntbuddyXmppConnection.getInstance();
		mXmppConnection.disconnect();
		AntbuddyXmppConnection.instance = null;
	}

//	public String logout() throws RemoteException {
//		AntbuddyConfig.COOKIE = null;
//		saveSevice();
//		AntbuddyXmppConnection.getInstance().disconnect();
//		return AntbuddyConfig.LOGOUT_SUCCESS;
//	}
}
