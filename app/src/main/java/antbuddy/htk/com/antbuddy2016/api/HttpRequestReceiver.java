package antbuddy.htk.com.antbuddy2016.api;

/**
 * Created by thanhnguyen on 29/03/2016.
 */

public interface HttpRequestReceiver {
    public void onSuccess(String result);
    public void onError(String error);
}
