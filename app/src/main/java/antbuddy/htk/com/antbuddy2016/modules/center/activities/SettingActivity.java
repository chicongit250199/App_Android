package antbuddy.htk.com.antbuddy2016.modules.center.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import antbuddy.htk.com.antbuddy2016.R;
import antbuddy.htk.com.antbuddy2016.RealmObjects.RObjectManagerOne;
import antbuddy.htk.com.antbuddy2016.RealmObjects.RUserMe;
import antbuddy.htk.com.antbuddy2016.api.APIManager;
import antbuddy.htk.com.antbuddy2016.interfaces.HttpRequestReceiver;
import antbuddy.htk.com.antbuddy2016.model.UserMe;
import antbuddy.htk.com.antbuddy2016.modules.login.activities.DomainActivity;
import antbuddy.htk.com.antbuddy2016.modules.login.activities.LoginActivity;
import antbuddy.htk.com.antbuddy2016.service.AntbuddyApplication;
import antbuddy.htk.com.antbuddy2016.service.AntbuddyService;
import antbuddy.htk.com.antbuddy2016.setting.ABSharedPreference;
import antbuddy.htk.com.antbuddy2016.util.AndroidHelper;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by thanhnguyen on 09/04/2016.
 */
public class SettingActivity extends Activity {
    private ImageView imgAvatar;
    private TextView tv_user_name;
    private LinearLayout ll_user;

    private Button btnSwithCompany;
    private Button btnSetting;
    private Button btnSignOut;

    private RelativeLayout areaBack;

    private RelativeLayout backgroundTry;
    private LinearLayout backgroundViews;
    private ProgressBar prb_Loading;
    private Button btnTry;

    private RObjectManagerOne realmManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        imgAvatar    = (ImageView) findViewById(R.id.imgAvatar);
        tv_user_name = (TextView) findViewById(R.id.tv_user_name);

        realmManager = new RObjectManagerOne();
        realmManager.setUserme(realmManager.getRealm().where(RUserMe.class).findFirst());

        initViews();

//        loading_UserMe();
        updateUI(realmManager.getUserme());
        viewsListener();
    }

    @Override
    protected void onDestroy() {
        realmManager.closeRealm();
        super.onDestroy();
    }

    private void initViews() {
        btnSignOut = (Button) findViewById(R.id.btnSignOut);
        btnSetting = (Button) findViewById(R.id.btnSetting);
        btnSwithCompany = (Button) findViewById(R.id.btnSwithCompany);

        backgroundTry = (RelativeLayout) findViewById(R.id.backgroundTry);
        backgroundViews = (LinearLayout) findViewById(R.id.backgroundViews);
        btnTry = (Button) findViewById(R.id.btnTry);
        prb_Loading = (ProgressBar) findViewById(R.id.prb_Loading);
        areaBack = (RelativeLayout) findViewById(R.id.areaBack);
    }

    private void viewsListener() {
        btnTry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prb_Loading.setVisibility(View.VISIBLE);
                btnTry.setVisibility(View.GONE);
                if (!AndroidHelper.isInternetAvailable(getApplicationContext())) {
                    AndroidHelper.warningInternetConnection(SettingActivity.this);
                    prb_Loading.setVisibility(View.GONE);
                    btnTry.setVisibility(View.VISIBLE);
                } else {
                    loading_UserMe();
                }
            }
        });

        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AndroidHelper.showToast("This feature will be available soon!", SettingActivity.this);
            }
        });
        btnSwithCompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AndroidHelper.alertDialogShow(SettingActivity.this, "Do you want to switch company?", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ABSharedPreference.resetXMPP();

                        //RObjectManager.getInstance().closeRealm();
                        // Reset Object Manager

                        //AntbuddyApplication.getInstance().closeRealm();
                        AntbuddyApplication.getInstance().resetApiService();

//                        ObjectManager.getInstance().clear();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                AntbuddyService.getInstance().resetXMPP();
                            }
                        }).start();

                        AndroidHelper.gotoActivity(SettingActivity.this, DomainActivity.class, true, true);
                    }
                }, null);
            }
        });
        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AndroidHelper.alertDialogShow(SettingActivity.this, "Do you want to sign out?", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        ObjectManager.getInstance().clear();
//                        RObjectManager.getInstance().closeRealm();

                        //AntbuddyApplication.getInstance().closeRealm();
                        //AntbuddyApplication.getInstance().deleteRealm();
                        ABSharedPreference.resetAccountInSharedPreferences();
                        ABSharedPreference.resetXMPP();
                        AntbuddyService.getInstance().resetXMPP();
                        AntbuddyApplication.getInstance().resetApiService();
                        AndroidHelper.gotoActivity(SettingActivity.this, LoginActivity.class, true, true);

                    }
                }, null);
            }
        });
        areaBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void loading_UserMe() {
        Boolean isdataLoadedFromDB = false;
        final AntbuddyApplication application = AntbuddyApplication.getInstance();
//        if (application.isUserMeExist()) {
//            updateUI(application.getUserme());
//            isdataLoadedFromDB = true;
//        }

        if (AndroidHelper.isInternetAvailable(getApplicationContext())) {
            APIManager.GETUserMe(new HttpRequestReceiver<UserMe>() {
                @Override
                public void onSuccess(UserMe me) {
                    realmManager.saveUserMeOrUpdate(me);
//                    application.setUserme(RObjectManager.getInstance().getUserMe());
//                    updateUI(application.getUserme());
                }

                @Override
                public void onError(String error) {
                    APIManager.showToastWithCode(error, SettingActivity.this);
                    updateUIWhenNoInternet();
                }
            });
        } else if (!isdataLoadedFromDB) {    // No connection and No data in DB
            updateUIWhenNoInternet();
        }
    }

    private void updateUI(RUserMe me) {
        Glide.with(SettingActivity.this)
                .load(me.getAvatar())
                .override(100, 100)
                .bitmapTransform(new CropCircleTransformation(SettingActivity.this))
                .placeholder(R.drawable.ic_avatar_defaul)
                .error(R.drawable.ic_avatar_defaul)
                .into(imgAvatar);

        tv_user_name.setText(me.getUsername());

        backgroundTry.setVisibility(View.GONE);
        prb_Loading.setVisibility(View.GONE);
        btnTry.setVisibility(View.VISIBLE);
        backgroundViews.setVisibility(View.VISIBLE);
    }

    private void updateUIWhenNoInternet() {
        if (!AndroidHelper.isInternetAvailable(getApplicationContext())) {
            backgroundTry.setVisibility(View.VISIBLE);
            prb_Loading.setVisibility(View.GONE);
            btnTry.setVisibility(View.VISIBLE);
            backgroundViews.setVisibility(View.GONE);
        }
    }
}
