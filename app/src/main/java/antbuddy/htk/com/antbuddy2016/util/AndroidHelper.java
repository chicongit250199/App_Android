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

    public static void warningInternetConnection(Activity activity) {
        if (!AndroidHelper.isInternetAvailable(activity.getApplicationContext())) {
            AndroidHelper.showToast("No network connection available!", activity);
        }
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
}
