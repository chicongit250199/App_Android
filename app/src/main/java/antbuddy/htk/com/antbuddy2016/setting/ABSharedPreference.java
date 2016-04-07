package antbuddy.htk.com.antbuddy2016.setting;

import android.content.Context;
import android.content.SharedPreferences;

import antbuddy.htk.com.antbuddy2016.service.AntbuddyApplication;

/**
 * Created by thanhnguyen on 07/04/2016.
 */
public class ABSharedPreference {

    private static final String NAME_AB_SHARED_PREFERENCE = "NAME_AB_SHARED_PREFERENCE";

    private static final String KEY_EMAIL = "KEY_EMAIL";
    private static final String KEY_PASSWORD = "KEY_PASSWORD";
    private static final String KEY_TOKEN = "KEY_TOKEN";
    private static final String KEY_DOMAIN = "KEY_DOMAIN";


    private static final String KEY_XMPP_HOST = "XMPP_HOST";

    private static final String KEY_REMEMBER_PASSWORD = "KEY_REMEMBER_PASSWORD";


    public static ABAccountConfig getAccoungConfig() {
        ABAccountConfig accountConfig = new ABAccountConfig();
        SharedPreferences sharedSetting = AntbuddyApplication.getInstance().getApplicationContext().getSharedPreferences(NAME_AB_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        accountConfig.setEmail(sharedSetting.getString(KEY_EMAIL, ""));
        accountConfig.setPassword(sharedSetting.getString(KEY_PASSWORD, ""));
        accountConfig.setToken(sharedSetting.getString(KEY_TOKEN, ""));
        accountConfig.setDomain(sharedSetting.getString(KEY_DOMAIN, ""));
        return accountConfig;
    }

    public static void saveABAcountConfig(String email, String password, String token, String domain) {
        SharedPreferences sharedSetting = AntbuddyApplication.getInstance().getApplicationContext().getSharedPreferences(NAME_AB_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedSetting.edit();
        edit.putString(KEY_EMAIL, email);
        edit.putString(KEY_PASSWORD, password);
        edit.putString(KEY_TOKEN, token);
        edit.putString(KEY_DOMAIN, domain);
        edit.apply();
    }

    public static void saveEmailAndPass(String email, String password) {
        SharedPreferences sharedSetting = AntbuddyApplication.getInstance().getApplicationContext().getSharedPreferences(NAME_AB_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedSetting.edit();
        edit.putString(KEY_EMAIL, email);
        edit.putString(KEY_PASSWORD, password);
        edit.apply();
    }

    public static void saveToken(String token) {
        SharedPreferences sharedSetting = AntbuddyApplication.getInstance().getApplicationContext().getSharedPreferences(NAME_AB_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedSetting.edit();
        edit.putString(KEY_TOKEN, token);
        edit.apply();
    }

    public static void saveDomain(String domain) {
        SharedPreferences sharedSetting = AntbuddyApplication.getInstance().getApplicationContext().getSharedPreferences(NAME_AB_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedSetting.edit();
        edit.putString(KEY_DOMAIN, domain);
        edit.apply();
    }

    public static void saveRememberPass(boolean isSave) {
        SharedPreferences sharedSetting = AntbuddyApplication.getInstance().getApplicationContext().getSharedPreferences(NAME_AB_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedSetting.edit();
        edit.putBoolean(KEY_REMEMBER_PASSWORD, isSave);
        edit.apply();
    }

    public static boolean getRememberPass() {
        SharedPreferences sharedSetting = AntbuddyApplication.getInstance().getApplicationContext().getSharedPreferences(NAME_AB_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        return sharedSetting.getBoolean(KEY_REMEMBER_PASSWORD, false);
    }
}
