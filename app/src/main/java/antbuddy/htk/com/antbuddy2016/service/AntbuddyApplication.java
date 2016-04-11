package antbuddy.htk.com.antbuddy2016.service;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import antbuddy.htk.com.antbuddy2016.api.API;
import antbuddy.htk.com.antbuddy2016.api.APIManager;
import antbuddy.htk.com.antbuddy2016.setting.ABSharedPreference;
import antbuddy.htk.com.antbuddy2016.util.AndroidHelper;
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

		createAPIService();

		//AndroidHelper.showLogSizeDevice(getApplicationContext());
	}

	private API createAPIService() {
		retrofit = new Retrofit.Builder()
				.baseUrl(API.BASE_URL)			//"https://antbuddy.com"
				.addConverterFactory(GsonConverterFactory.create())
				.build();
		apiService = retrofit.create(API.class);
		return apiService;
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
		if (apiService == null) {
			boolean isDomainExist = ABSharedPreference.getBoolean(ABSharedPreference.KEY_IS_DOMAIN_EXIST);
			if (isDomainExist) {
				apiService = restartAPIServiceWithDomain(ABSharedPreference.get(ABSharedPreference.KEY_DOMAIN));
			} else {
				apiService = createAPIService();
			}
		}

		return apiService;
	}

	public void resetApiService() {
		apiService = null;
	}

	public API restartAPIServiceWithDomain(String domain) {
		String URLWithDomain = String.format(API.BASE_URL_WITH_DOMAIN, domain);
		retrofit = new Retrofit.Builder()
				.baseUrl(URLWithDomain)
				.addConverterFactory(GsonConverterFactory.create())
				.build();
		apiService = retrofit.create(API.class);
		return apiService;
	}
}
