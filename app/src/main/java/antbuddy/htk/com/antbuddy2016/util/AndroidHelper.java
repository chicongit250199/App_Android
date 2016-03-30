package antbuddy.htk.com.antbuddy2016.util;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import antbuddy.htk.com.antbuddy2016.R;

/**
 * Created by thanhnguyen on 29/03/2016.
 */
public class AndroidHelper {

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
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
}
