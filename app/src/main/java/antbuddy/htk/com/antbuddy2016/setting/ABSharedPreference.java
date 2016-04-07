package antbuddy.htk.com.antbuddy2016.setting;

import android.content.Context;
import android.content.SharedPreferences;

import antbuddy.htk.com.antbuddy2016.service.AntbuddyApplication;

/**
 * Created by thanhnguyen on 07/04/2016.
 */
public class ABSharedPreference {

    private static final String NAME_AB_SHARED_PREFERENCE = "NAME_AB_SHARED_PREFERENCE";

    // Auth
    public static final String KEY_EMAIL    = "KEY_EMAIL";
    public static final String KEY_PASSWORD = "KEY_PASSWORD";
    public static final String KEY_TOKEN    = "KEY_TOKEN";
    public static final String KEY_DOMAIN   = "KEY_DOMAIN";

    // XMPP
    public static final String KEY_XMPP_HOST     = "XMPP_HOST";
    public static final String KEY_XMPP_PORT     = "KEY_XMPP_PORT";
    public static final String KEY_XMPP_DOMAIN   = "KEY_XMPP_DOMAIN";
    public static final String KEY_XMPP_USERNAME = "KEY_XMPP_USERNAME";
    public static final String KEY_XMPP_PASSWORD = "KEY_XMPP_PASSWORD";

    // Setting
    public static final String KEY_REMEMBER_PASSWORD = "KEY_REMEMBER_PASSWORD";
    public static final String KEY_IS_LOGIN = "KEY_IS_LOGIN";

    public static ABAccountConfig getAccountConfig() {
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

    public static void saveXMPPConfig(String host, int port, String domain, String username, String password) {
        SharedPreferences sharedSetting = AntbuddyApplication.getInstance().getApplicationContext().getSharedPreferences(NAME_AB_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedSetting.edit();
        edit.putString(KEY_XMPP_HOST, host);
        edit.putInt(KEY_XMPP_PORT, port);
        edit.putString(KEY_XMPP_DOMAIN, domain);
        edit.putString(KEY_XMPP_USERNAME, username);
        edit.putString(KEY_XMPP_PASSWORD, password);
        edit.apply();
    }

    public static ABXMPPConfig getXMPPConfig() {
        ABXMPPConfig xmppConfig = new ABXMPPConfig();
        SharedPreferences sharedSetting = AntbuddyApplication.getInstance().getApplicationContext().getSharedPreferences(NAME_AB_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        xmppConfig.setHOST_XMPP(sharedSetting.getString(KEY_XMPP_HOST, ""));
        xmppConfig.setPORT_XMPP(sharedSetting.getInt(KEY_XMPP_PORT, 0));
        xmppConfig.setDOMAIN_XMPP(sharedSetting.getString(KEY_XMPP_DOMAIN, ""));
        xmppConfig.setUSERNAME_XMPP(sharedSetting.getString(KEY_XMPP_USERNAME, ""));
        xmppConfig.setPASSWORD_XMPP(sharedSetting.getString(KEY_XMPP_PASSWORD, ""));
        return xmppConfig;
    }

    public static void save(String key, String value) {
        SharedPreferences sharedSetting = AntbuddyApplication.getInstance().getApplicationContext().getSharedPreferences(NAME_AB_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedSetting.edit();
        edit.putString(key, value);
        edit.apply();
    }

    public static void save(String key, int value) {
        SharedPreferences sharedSetting = AntbuddyApplication.getInstance().getApplicationContext().getSharedPreferences(NAME_AB_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedSetting.edit();
        edit.putInt(key, value);
        edit.apply();
    }

    public static void save(String key, boolean value) {
        SharedPreferences sharedSetting = AntbuddyApplication.getInstance().getApplicationContext().getSharedPreferences(NAME_AB_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedSetting.edit();
        edit.putBoolean(key, value);
        edit.apply();
    }

    public static String get(String key) {
        SharedPreferences sharedSetting = AntbuddyApplication.getInstance().getApplicationContext().getSharedPreferences(NAME_AB_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        return sharedSetting.getString(key, "");
    }

    public static boolean getBoolean(String key) {
        SharedPreferences sharedSetting = AntbuddyApplication.getInstance().getApplicationContext().getSharedPreferences(NAME_AB_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        return sharedSetting.getBoolean(key, false);
    }

    public static int getInt(String key) {
        SharedPreferences sharedSetting = AntbuddyApplication.getInstance().getApplicationContext().getSharedPreferences(NAME_AB_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        return sharedSetting.getInt(key, 0);
    }

    public static void resetXMPP() {
        SharedPreferences sharedSetting = AntbuddyApplication.getInstance().getApplicationContext().getSharedPreferences(NAME_AB_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedSetting.edit();
        edit.putString(KEY_XMPP_HOST, "");
        edit.putInt(KEY_XMPP_PORT, 0);
        edit.putString(KEY_XMPP_DOMAIN, "");
        edit.putString(KEY_XMPP_USERNAME, "");
        edit.putString(KEY_XMPP_PASSWORD, "");
        edit.apply();
    }

    public static void resetAccount() {
        resetXMPP();
        SharedPreferences sharedSetting = AntbuddyApplication.getInstance().getApplicationContext().getSharedPreferences(NAME_AB_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedSetting.edit();
        edit.putString(KEY_PASSWORD, "");
        edit.putString(KEY_TOKEN, "");
        edit.putString(KEY_DOMAIN, "");
        edit.apply();
    }
}
