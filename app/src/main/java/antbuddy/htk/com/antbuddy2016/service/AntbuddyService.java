package antbuddy.htk.com.antbuddy2016.service;

import android.app.Activity;
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
		LogHtk.d(LogHtk.SERVICE_TAG, "------------------>onCreate SERVICE<----------------");
		super.onCreate();

		mAntbuddyService = AntbuddyService.this;

	}

	@Override
	public IBinder onBind(Intent intent) {
		LogHtk.i(LogHtk.Test2, "Service onBind!");
		LogHtk.d(LogHtk.SERVICE_TAG, "------------------>onBind SERVICE<----------------");
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
