package antbuddy.htk.com.antbuddy2016.service;

import android.os.Binder;

import java.lang.ref.WeakReference;

/**
 * Created by thanhnguyen on 30/03/2016.
 */
public class LocalBinder<S> extends Binder {
    private final WeakReference<S> mService;

    public LocalBinder(final S service) {
        mService = new WeakReference<S>(service);
    }

    public S getService() {
        return mService.get();
    }

}
