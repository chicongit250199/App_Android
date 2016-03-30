package antbuddy.htk.com.antbuddy2016.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.IBinder;

/**
 * Created by thanhnguyen on 30/03/2016.
 */
public class LocalService extends Service {
    private static final String DOMAIN = "xmpp.jp";
    private static final String USERNAME = "khushi";
    private static final String PASSWORD = "password";
    public static ConnectivityManager cm;
    //public static MyXMPP xmpp;
    public static boolean ServerchatCreated = false;
    String text = "";


    @Override
    public IBinder onBind(final Intent intent) {
        return new LocalBinder<LocalService>(this);
    }

    //public Chat chat;

    @Override
    public void onCreate() {
        super.onCreate();
        cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        //xmpp = MyXMPP.getInstance(LocalService.this, DOMAIN, USERNAME, PASSWORD);
        //xmpp.connect("onCreate");
    }

    @Override
    public int onStartCommand(final Intent intent, final int flags,
                              final int startId) {
        return Service.START_NOT_STICKY;
    }

    @Override
    public boolean onUnbind(final Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //xmpp.connection.disconnect();
    }

    public static boolean isNetworkConnected() {
        return cm.getActiveNetworkInfo() != null;
    }
}
