package antbuddy.htk.com.antbuddy2016.service;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import antbuddy.htk.com.antbuddy2016.RealmObjects.RObjectManager;
import antbuddy.htk.com.antbuddy2016.RealmObjects.RRoom;
import antbuddy.htk.com.antbuddy2016.RealmObjects.RUser;
import antbuddy.htk.com.antbuddy2016.RealmObjects.RUserMe;
import antbuddy.htk.com.antbuddy2016.api.API;
import antbuddy.htk.com.antbuddy2016.api.APIManager;
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

	private RequestQueue mRequestQueue;
	private static AntbuddyApplication mInstance;

	private Retrofit retrofit;
	private API apiService;
	private RealmConfiguration realmConfig;



//	private RUserMe userme;
//	private RealmResults<RUser> users;
//	private RealmResults<RRoom> rooms;


	@Override
	public void onCreate() {
        super.onCreate();
		LogHtk.e(TAG, "Created AntbuddyApplication!");
		mInstance = this;

		// Create Android Local Service
		AntbuddyService.getInstance();

		createAPIService();

		//AndroidHelper.showLogSizeDevice(getApplicationContext());
		realmConfig = new RealmConfiguration.Builder(this)
											.schemaVersion(5)
											.name("demo5.realm")
											.build();
//		Realm.deleteRealm(realmConfig);
		Realm.setDefaultConfiguration(realmConfig);

//		userme = RObjectManager.getInstance().getUserMe();
//		users = RObjectManager.getUsers();
//		rooms = RObjectManager.getRooms();
	}

	private API createAPIService() {
		retrofit = new Retrofit.Builder()
				.baseUrl(API.BASE_URL)			//"https://antbuddy.com"
				.addConverterFactory(GsonConverterFactory.create())
				.build();
		apiService = retrofit.create(API.class);
		return apiService;
	}

	public void deleteRealm() {
		Realm.deleteRealm(realmConfig);
	}

	public void closeRealm() {
		Realm.getDefaultInstance().close();
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
//
//	public RUserMe getUserme() {
//		return userme;
//	}
//
//	public void setUserme(RUserMe userme) {
//		this.userme = userme;
//	}
//
//	public boolean isUserMeExist() {
//		if (userme != null && userme.getKey().length() > 0) {
//			return true;
//		} else {
//			return false;
//		}
//	}
//
//	public RealmResults<RUser> getUsers() {
//		return users;
//	}
//
//	public void setUsers(RealmResults<RUser> users) {
//		this.users = users;
//	}
//
//	public boolean isUsersExist() {
//		if (users != null && users.size() > 0) {
//			return true;
//		} else {
//			return false;
//		}
//	}
//
//	public RealmResults<RRoom> getRooms() {
//		return rooms;
//	}
//
//	public void setRooms(RealmResults<RRoom> rooms) {
//		this.rooms = rooms;
//	}
//
//	public boolean isRoomsExist() {
//		if (rooms != null && rooms.size() > 0) {
//			return true;
//		} else {
//			return false;
//		}
//	}
}
