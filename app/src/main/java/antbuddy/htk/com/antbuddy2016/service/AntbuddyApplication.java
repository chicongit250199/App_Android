package antbuddy.htk.com.antbuddy2016.service;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import antbuddy.htk.com.antbuddy2016.api.ABRequest;
import antbuddy.htk.com.antbuddy2016.api.API;
import antbuddy.htk.com.antbuddy2016.api.APIManager;
import antbuddy.htk.com.antbuddy2016.setting.ABSharedPreference;
import antbuddy.htk.com.antbuddy2016.util.AndroidHelper;
import antbuddy.htk.com.antbuddy2016.util.Constants;
import antbuddy.htk.com.antbuddy2016.util.LogHtk;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

public class AntbuddyApplication extends Application {
	public static final String TAG = AntbuddyApplication.class.getSimpleName();

	private RequestQueue mRequestQueue;
	private static AntbuddyApplication mInstance;

	private Retrofit retrofit;
	private API apiService;

	@Override
	public void onCreate() {
        super.onCreate();
		mInstance = this;
		LogHtk.e(TAG, "Created AntbuddyApplication!");

		retrofit = new Retrofit.Builder()
				.baseUrl(APIManager.BASE_URL)			//"https://antbuddy.com"
				.addConverterFactory(GsonConverterFactory.create())
				.build();
		apiService = retrofit.create(API.class);

		AndroidHelper.showLogSizeDevice(getApplicationContext());
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
        LogHtk.e(TAG, "onLowMemory");
	}

	public static synchronized AntbuddyApplication getInstance() {
		return mInstance;
	}

	public API getApiService() {
		return apiService;
	}

	public API restartAPIServiceWithDomain(String domain) {

		final String URLWithDomain = "https://" + ABSharedPreference.getAccoungConfig().getDomain() + ".antbuddy.com";
		retrofit = new Retrofit.Builder()
				.baseUrl(URLWithDomain)			//"https://antbuddy.com"
				.addConverterFactory(GsonConverterFactory.create())
				.build();
		apiService = retrofit.create(API.class);
		return apiService;
	}

	public RequestQueue getRequestQueue() {
		if (mRequestQueue == null) {
			mRequestQueue = Volley.newRequestQueue(getApplicationContext());
		}

		return mRequestQueue;
	}

	public <T> void addToRequestQueue(Request<T> req, String tag) {
		req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
		getRequestQueue().add(req);
	}

	public <T> void addToRequestQueue(Request<T> req) {
		req.setTag(TAG);
		getRequestQueue().add(req);
	}

	public void cancelPendingRequests(Object tag) {
		if (mRequestQueue != null) {
			mRequestQueue.cancelAll(tag);
		}
	}
}
