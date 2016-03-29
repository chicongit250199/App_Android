package antbuddy.htk.com.antbuddy2016.util;

import android.app.Activity;
import android.app.Service;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by thanhnguyen on 29/03/2016.
 */
public class AndroidHelper {

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }
}
