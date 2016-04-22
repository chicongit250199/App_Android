package antbuddy.htk.com.antbuddy2016.modules.login.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import antbuddy.htk.com.antbuddy2016.R;
import antbuddy.htk.com.antbuddy2016.RealmObjects.RObjectManager;
import antbuddy.htk.com.antbuddy2016.RealmObjects.RObjectManagerOne;
import antbuddy.htk.com.antbuddy2016.RealmObjects.RRoom;
import antbuddy.htk.com.antbuddy2016.RealmObjects.RUser;
import antbuddy.htk.com.antbuddy2016.RealmObjects.RUserMe;
import antbuddy.htk.com.antbuddy2016.modules.center.activities.CenterActivity;
import antbuddy.htk.com.antbuddy2016.modules.center.activities.RecentFragment;
import antbuddy.htk.com.antbuddy2016.service.AntbuddyApplication;
import antbuddy.htk.com.antbuddy2016.setting.ABSharedPreference;
import antbuddy.htk.com.antbuddy2016.util.AndroidHelper;
import antbuddy.htk.com.antbuddy2016.util.BroadcastConstant;
import antbuddy.htk.com.antbuddy2016.util.LogHtk;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * Created by thanhnguyen on 19/04/2016.
 */
public class LoadingActivity extends Activity {

    private RObjectManagerOne realmManager;

    private BroadcastReceiver loadingReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            try {
                String result = intent.getStringExtra("loadingResult");

                if (result.contains("yes")) {
                    AndroidHelper.gotoActivity(LoadingActivity.this, CenterActivity.class, true);
                    unregisterReceiver(loadingReceiver);
                }

                if (result.contains("No address associated with hostname")) {
                    if (realmManager.getUserme().isValid() && realmManager.getUsers().isValid() && realmManager.getRooms().isValid()) {
                        AndroidHelper.gotoActivity(LoadingActivity.this, CenterActivity.class, true);
                        unregisterReceiver(loadingReceiver);
                    }
                }

                if (result.contains("noRooms")) {
                    AndroidHelper.gotoActivity(LoadingActivity.this, DomainActivity.class, true);
                    unregisterReceiver(loadingReceiver);
                }

                if (result.contains("noUsers")) {
                    AndroidHelper.gotoActivity(LoadingActivity.this, DomainActivity.class, true);
                    unregisterReceiver(loadingReceiver);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        registerReceiver(loadingReceiver, new IntentFilter(BroadcastConstant.CENTER_LOADING_DATA_SUCEESS));
        setupRealmOne();
    }

    private void setupRealmOne() {
        realmManager = new RObjectManagerOne();
        realmManager.setUserme(realmManager.getRealm().where(RUserMe.class).findFirst());
        realmManager.setUsers(realmManager.getRealm().where(RUser.class).findAll());
        realmManager.setRooms(realmManager.getRealm().where(RRoom.class).findAll());

        realmManager.addUserMeListener(new RealmChangeListener() {
            @Override
            public void onChange() {

            }
        });

        realmManager.addUsersListener(new RealmChangeListener() {
            @Override
            public void onChange() {

            }
        });

        realmManager.addRoomsListener(new RealmChangeListener() {
            @Override
            public void onChange() {

            }
        });

        realmManager.loading_UserMe_Users_Rooms();
    }

    @Override
    protected void onDestroy() {
        try {
            if (loadingReceiver != null) {
                this.unregisterReceiver(loadingReceiver);
            }
        } catch (IllegalArgumentException e) {
            LogHtk.i(LogHtk.ErrorHTK, "Already unregisterReceiver!");
            loadingReceiver = null;
        }

        realmManager.closeRealm();
        super.onDestroy();
    }
}
