package antbuddy.htk.com.antbuddy2016.service;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import antbuddy.htk.com.antbuddy2016.RealmObjects.RObjectManager;
import antbuddy.htk.com.antbuddy2016.RealmObjects.RRoom;
import antbuddy.htk.com.antbuddy2016.RealmObjects.RUser;
import antbuddy.htk.com.antbuddy2016.RealmObjects.RUserMe;
import antbuddy.htk.com.antbuddy2016.api.API;
import antbuddy.htk.com.antbuddy2016.api.APIManager;
import antbuddy.htk.com.antbuddy2016.interfaces.XMPPReceiver;
import antbuddy.htk.com.antbuddy2016.setting.ABSharedPreference;
import antbuddy.htk.com.antbuddy2016.util.AndroidHelper;
import antbuddy.htk.com.antbuddy2016.util.LogHtk;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

public class AntbuddyApplication extends Application {
	public static final String TAG = AntbuddyApplication.class.getSimpleName();

	private static AntbuddyApplication mInstance;

	private Retrofit retrofit;
	private API apiService;
	private RealmConfiguration realmConfig;

	@Override
	public void onCreate() {
        super.onCreate();
		mInstance = this;

		// Create Android Local Service
		AntbuddyService.getInstance();

		createAPIService();

		realmConfig = new RealmConfiguration.Builder(this)
											.schemaVersion(7)
											.name("antbuddy7.realm")
											.build();
		//Realm.deleteRealm(realmConfig);
		Realm.setDefaultConfiguration(realmConfig);
	}

	public void deleteRealm() {
		//Realm.getDefaultInstance().c
		Realm.deleteRealm(realmConfig);
		Realm.setDefaultConfiguration(realmConfig);
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
        LogHtk.e(LogHtk.WarningHTK, "Application onLowMemory");
	}

	public static synchronized AntbuddyApplication getInstance() {
		return mInstance;
	}

	public API getApiService() {
		if (apiService == null) {
			apiService = createAPIService();
		}

		String domain = ABSharedPreference.get(ABSharedPreference.KEY_DOMAIN);
		if (domain.length() > 0) {
			apiService = restartAPIServiceWithDomain(ABSharedPreference.get(ABSharedPreference.KEY_DOMAIN));
		} else {
			LogHtk.e(TAG, "Domain is not Exist");
		}

		return apiService;
	}

	public void resetApiService() {
		apiService = null;
	}

	private API restartAPIServiceWithDomain(String domain) {
		String URLWithDomain = String.format(API.BASE_URL_WITH_DOMAIN, domain);
		retrofit = new Retrofit.Builder()
				.baseUrl(URLWithDomain)
				.addConverterFactory(GsonConverterFactory.create())
				.build();
		apiService = retrofit.create(API.class);
		return apiService;
	}
}
