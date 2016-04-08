package antbuddy.htk.com.antbuddy2016.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Micky on 4/8/2016.
 */
public class CommonReceiver extends BroadcastReceiver {
    public void onReceive(Context paramContext, Intent paramIntent)
    {
        paramContext.startService(new Intent(paramContext, AntbuddyService.class));
    }
}