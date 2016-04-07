package antbuddy.htk.com.antbuddy2016.interfaces;

import org.json.JSONArray;

/**
 * Created by thanhnguyen on 29/03/2016.
 */

public interface HttpRequestReceiver<T> {
    //public void onBegin();
    public void onSuccess(T object);
    public void onError(String error);
}
