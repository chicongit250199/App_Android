package antbuddy.htk.com.antbuddy2016.interfaces;

/**
 * Created by thanhnguyen on 29/03/2016.
 */

public interface HttpRequestReceiver {
    //public void onBegin();
    public void onSuccess(String response);
    public void onError(String error);
    //public void onFinish();
}
