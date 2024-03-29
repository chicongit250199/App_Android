package antbuddy.htk.com.antbuddy2016.myReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import antbuddy.htk.com.antbuddy2016.RealmObjects.RObjectManagerOne;
import antbuddy.htk.com.antbuddy2016.service.AntbuddyService;
import antbuddy.htk.com.antbuddy2016.util.LogHtk;

/**
 * Created by thanhnguyen on 23/04/2016.
 */
public class NetworkChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {

        ConnectivityManager conMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMan.getActiveNetworkInfo();
        if (netInfo != null && (netInfo.getType() == ConnectivityManager.TYPE_WIFI || netInfo.getType() == ConnectivityManager.TYPE_MOBILE)) {
            LogHtk.i(LogHtk.Info, "Have network!");
            if (AntbuddyService.getInstance() != null && !AntbuddyService.getInstance().isConnecting) {
                AntbuddyService.getInstance().loginXMPP();
            }
        } else {
            LogHtk.i(LogHtk.Info, "No network!");
            RObjectManagerOne realmManager = new RObjectManagerOne();
            realmManager.setAllUsersOffline();
            realmManager.closeRealm();

            AntbuddyService.getInstance().resetXMPP();
        }


//        if(checkInternet(context))
//        {
//            Toast.makeText(context, "Network Available Do operations", Toast.LENGTH_LONG).show();
//            LogHtk.i(LogHtk.Test1, "Co network!");
//        } else {
//            LogHtk.i(LogHtk.Test1, "KHONG Co network!");
//            AntbuddyService.getInstance().resetXMPP();
//        }

//        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
//        NetworkInfo mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
//
//        boolean isConnected = wifi != null && wifi.isConnectedOrConnecting() ||
//                mobile != null && mobile.isConnectedOrConnecting();
//        if (isConnected) {
//            Log.d("Network Available ", "YES");
//        } else {
//            Log.d("Network Available ", "NO");
//        }

    }


    boolean checkInternet(Context context) {
        ServiceManager serviceManager = new ServiceManager(context);
        if (serviceManager.isNetworkAvailable()) {
            return true;
        } else {
            return false;
        }
    }

}
