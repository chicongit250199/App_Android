package antbuddy.htk.com.antbuddy2016.util;

import android.util.Log;

import antbuddy.htk.com.antbuddy2016.modules.center.activities.CenterActivity;
import antbuddy.htk.com.antbuddy2016.modules.login.activities.DomainActivity;
import antbuddy.htk.com.antbuddy2016.service.AntbuddyApplication;

/**
 * Created by htk on 16/04/2015.
 */
public class LogHtk {
    public static String SERVICE_TAG = "AntbuddyService";

    private static String[] tags = {
            DomainActivity.TAG_THISCLASS,
            CenterActivity.TAG_THISCLASS,
            AntbuddyApplication.TAG,
            "asdf",
            SERVICE_TAG,
//           "Hoa debug",
//            "Thanh",
//            SettingsFragment.TAG_THISCLASS,
//            AntbuddyService.TAG,
//            MainActivity.TAG,
//            ChatFragment.TAG_THISCLASS,
//            LoginActivity.TAG_THISCLASS,
//            FileAntBuddy.TAG_THISCLASS,
//            AntbuddyConfig.TAG_THISCLASS,
//            AntbuddyApplication.TAG,
//            LobbyFragment.TAG,
//            RoomsFragment.TAG,
//            AntbuddyXmppConnection.TAG,
//            ChatMessage.TAG,
    };

    private static boolean isShow = true;

    public static void setIsShow(boolean isShow) {
        LogHtk.isShow = isShow;
    }

    private static boolean isExist(String tag)
    {
        for (String _tag : tags)
        {
            if(tag.equalsIgnoreCase(_tag))
                return true;
        }
        return false;
    }

    public static void i(String classNameTag, String text) {
        // check classNameTag in list string to hide
        if (!LogHtk.isShow) return;
        if(isExist(classNameTag))
            Log.i("___" + classNameTag, text);
    }

    public static void d(String classNameTag, String text) {
        if (!LogHtk.isShow) return;
        if(isExist(classNameTag))
            Log.d("___" + classNameTag, text);
    }

    public static void e(String classNameTag, String text) {
        if (!LogHtk.isShow) return;
        if(isExist(classNameTag))
            Log.e("___" + classNameTag, text);
    }
}
