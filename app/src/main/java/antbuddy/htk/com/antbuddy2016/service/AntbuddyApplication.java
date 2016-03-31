package antbuddy.htk.com.antbuddy2016.service;

import android.app.Application;

import antbuddy.htk.com.antbuddy2016.util.LogHtk;

public class AntbuddyApplication extends Application {
	public static final String TAG = AntbuddyApplication.class.getSimpleName();

	@Override
	public void onCreate() {
        super.onCreate();
        //ReportHandler.install(this, "thanh.nguyen@htklabs.com");
		LogHtk.e(TAG, "Created AntbuddyApplication!");
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
        LogHtk.e(TAG, "onLowMemory");
	}
}
