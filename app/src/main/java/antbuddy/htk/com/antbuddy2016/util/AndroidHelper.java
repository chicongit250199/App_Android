package antbuddy.htk.com.antbuddy2016.util;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import antbuddy.htk.com.antbuddy2016.R;

/**
 * Created by thanhnguyen on 29/03/2016.
 */
public class AndroidHelper {

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if(activity.getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
            clearFocus(activity.getCurrentFocus());
        }
    }

    public static void showProgressBar(Activity activity, final ProgressBar progressBar) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.VISIBLE);
            }
        });
    }

    public static void hideProgressBar(Activity activity, final ProgressBar progressBar) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    public static void setEnabledWithView(Activity activity, final View view, final boolean enabled) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                view.setEnabled(enabled);
            }
        });
    }

    public static void showToast(final String message, final Activity activity) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity.getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static boolean isInternetAvailable(Context context) {
        ConnectivityManager conMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo i = conMgr.getActiveNetworkInfo();
        if (i == null)
            return false;
        if (!i.isConnected())
            return false;
        if (!i.isAvailable())
            return false;
        return true;
    }

    public static void alertDialogShow(Context context, String message, DialogInterface.OnClickListener okClickListener, DialogInterface.OnClickListener cancelClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyAlertDialogStyle);
        builder.setTitle("Antbuddy");
        builder.setMessage(message);
        builder.setPositiveButton("OK", okClickListener);
        builder.setNegativeButton("Cancel", cancelClickListener);
        builder.show();
    }

    public static boolean warningInternetConnection(Activity activity) {
        if (!AndroidHelper.isInternetAvailable(activity.getApplicationContext())) {
            AndroidHelper.showToast("No network connection available!", activity);
            return false;
        }
        return true;
    }

    //goto activity
    public static void gotoActivity(Activity currentActivity, Activity activity, Class<?> cls,
                             boolean isFinish, Bundle bundle) {
        gotoActivity(currentActivity, cls, isFinish, bundle, false);
    }

    public static void gotoActivity(Activity currentActivity, Class<?> cls,
                              boolean isFinish, Bundle bundle, Boolean isTop) {
        Intent riIntent = new Intent(currentActivity, cls);
        if (isTop) {
            riIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        }
        if (bundle != null)
            riIntent.putExtras(bundle);
        currentActivity.startActivity(riIntent);
        if (isFinish)
            currentActivity.finish();
        // activity.overridePendingTransition(R.anim.in_from_right,
        // R.anim.out_to_left);
    }

    public static void gotoActivity(Activity currentActivity, Class<?> cls,
                             boolean isFinish, Boolean isTop) {
        gotoActivity(currentActivity, cls, isFinish, null, isTop);
    }

    public static void gotoActivity(Activity currentActivity, Class<?> cls,
                             boolean isFinish) {
        gotoActivity(currentActivity, cls, isFinish, null, false);
    }

    public static void gotoActivity(Activity currentActivity, Class<?> cls) {
        gotoActivity(currentActivity, cls, false);
    }

    public static void gotoActivity(Activity currentActivity, Class<?> cls,
                              Bundle bundle) {
        gotoActivity(currentActivity, cls, false, bundle, false);
    }

    //clear forcus
    public static void clearFocus(View view)
    {
        if (view instanceof EditText) {
            view.clearFocus();
        }
        if (view instanceof ViewGroup) {

            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {

                View innerView = ((ViewGroup) view).getChildAt(i);

                clearFocus(innerView);
            }
        }
    }

    //parse json data
    public static String getString(JSONObject json, String key, String sDefault) {
        try {
            return json.getString(key);
        } catch (JSONException e) {
            return sDefault;
        }
    }

    public static Boolean getBoolean(JSONObject json, String key, Boolean sDefault) {
        try {
            return json.getBoolean(key);
        } catch (JSONException e) {
            return sDefault;
        }
    }
    public static int getInt(JSONObject json, String key, int iDefault) {
        try {
            return json.getInt(key);
        } catch (JSONException e) {
            return iDefault;
        }
    }
    public static String renID() {
        String md5OfTime = md5("" + NationalTime.getlongTime());
        Matcher m = Pattern.compile("(.{5})(.{4})(.{4})(.{4})(.{12})").matcher(md5OfTime);
        while (m.find()) {
            return String.format("%s-%s-%s-%s-%s", m.group(1), m.group(2), m.group(3), m.group(4), m.group(5));
        }
        return md5OfTime;
    }

    public static final String md5(final String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++) {
                String h = Integer.toHexString(0xFF & messageDigest[i]);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
