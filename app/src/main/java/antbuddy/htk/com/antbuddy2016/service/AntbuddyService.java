package antbuddy.htk.com.antbuddy2016.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;

import antbuddy.htk.com.antbuddy2016.interfaces.XMPPReceiver;
import antbuddy.htk.com.antbuddy2016.model.ChatMessage;
import antbuddy.htk.com.antbuddy2016.util.LogHtk;

public class AntbuddyService extends Service {

    private static AntbuddyXmppConnection mXmppConnection = null;
    public static AntbuddyService mAntbuddyService = null;

    private final IBinder serviceBinder = new LocalBinder();
    public class LocalBinder extends Binder {
        public AntbuddyService getService() {
            mAntbuddyService = AntbuddyService.this;
            return mAntbuddyService;
        }
    }

	private SharedPreferences prefWrapper;
//	private ReceiverBroadCast mReceiver;

	@Override
	public void onCreate() {
		LogHtk.d(LogHtk.SERVICE_TAG, "------------------>onCreate SERVICE<----------------");
		super.onCreate();

		mAntbuddyService = AntbuddyService.this;

		//prefWrapper = getSharedPreferences(SettingAB.SHARED_SETTING, Context.MODE_MULTI_PROCESS);
		//AntbuddyConfig.COOKIE = prefWrapper.getString(SHARED_TOKEN, "");

//		LogHtk.i(TAG, "Get Cookie from SharedPreferences: " + ((AntbuddyConfig.COOKIE == null)? "NULL":AntbuddyConfig.COOKIE));
//		if (!TextUtils.isEmpty(AntbuddyConfig.COOKIE)) {
//			new Thread(new Runnable() {
//				@Override
//				public void run() {
//					try {
//						String result = login(AntbuddyConfig.COOKIE);
//						if (result.equals(AntbuddyXmppConnection.SERVICE_START_SUCCESS) || result.equals(AntbuddyXmppConnection.SERVICE_ALREADY_START)) {
//							String result1 = loadingRoom();
//							if (result1.equals("Success")) {
//								LogHtk.i(TAG, "AntbuddyService/onCreate:");
//								saveService();
//							} else {
//								LogHtk.i(TAG, result1);
//							}
//						}
//					} catch (RemoteException e) {
//						e.printStackTrace();
//					}
//				}
//			}).start();
//		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		LogHtk.d(LogHtk.SERVICE_TAG, "------------------>onBind SERVICE<----------------");
		return serviceBinder;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		LogHtk.d(LogHtk.SERVICE_TAG, "------------------>onStartCommand SERVICE<----------------");
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
		super.onDestroy();
	}

	public void loginXMPP(String usernameXMPP, String passwordXMPP) {
		mXmppConnection = AntbuddyXmppConnection.getInstance();
		mXmppConnection.connectXMPP(AntbuddyService.this, usernameXMPP, passwordXMPP, new XMPPReceiver() {
			@Override
			public void onSuccess(String result) {
				LogHtk.d(LogHtk.XMPP_TAG, "OnSuccess/ result = " + result);
			}

			@Override
			public void onError(String error) {
				LogHtk.e(LogHtk.XMPP_TAG, "OnSuccess/ error = " + error);
			}
		});




//		try {
//			LogHtk.i(TAG, "Cookie at AntbuddyService/login() set agian:");
//			AntbuddyConfig.setCOOKIE(cookie);
//			mAntbuddyService = AntbuddyService.this;
//			mXmppConnection = AntbuddyXmppConnection.getInstance();
//			UserInfo userInfo = mXmppConnection.getmUserInfo();
//			if (userInfo == null) {
//				String result = Request.connect("/api/users/me");
//				//Log.i("SERVICE", "String /api/users/me response: " + result);
//				userInfo = UserInfo.parseUser(new JSONObject(result));
//				mXmppConnection.setmUserInfo(userInfo);
//			}
//			return mXmppConnection.connect(AntbuddyService.this, userInfo.getXmppUsername(), userInfo.getXmppPassword());
//		} catch (JSONException e) {
//			e.printStackTrace();
//			return "Error";
//		} catch (Exception e) {
//			e.printStackTrace();
//			return "Error";
//		}

	}

	public void sendMessageOut(ChatMessage chatMessage) {
		mXmppConnection = AntbuddyXmppConnection.getInstance();
		mXmppConnection.sendMessageOut(chatMessage);
	}

	public void resetXMPP() {
		mXmppConnection = AntbuddyXmppConnection.getInstance();
		mXmppConnection.disconnect();
	}

//	public String logout() throws RemoteException {
//		AntbuddyConfig.COOKIE = null;
//		saveSevice();
//		AntbuddyXmppConnection.getInstance().disconnect();
//		return AntbuddyConfig.LOGOUT_SUCCESS;
//	}
}
