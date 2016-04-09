package antbuddy.htk.com.antbuddy2016.modules.center.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
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
import antbuddy.htk.com.antbuddy2016.api.APIManager;
import antbuddy.htk.com.antbuddy2016.model.ObjectManager;
import antbuddy.htk.com.antbuddy2016.model.UserMe;
import antbuddy.htk.com.antbuddy2016.modules.login.activities.DomainActivity;
import antbuddy.htk.com.antbuddy2016.modules.login.activities.LoginActivity;
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

    private RelativeLayout backgroundTry;
    private LinearLayout backgroundViews;
    private ProgressBar prb_Loading;
    private Button btnTry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        imgAvatar    = (ImageView) findViewById(R.id.imgAvatar);
        tv_user_name = (TextView) findViewById(R.id.tv_user_name);

        initViews();

        updateUI();
        viewsListener();
    }

    private void initViews() {
        btnSignOut = (Button) findViewById(R.id.btnSignOut);
        btnSetting = (Button) findViewById(R.id.btnSetting);
        btnSwithCompany = (Button) findViewById(R.id.btnSwithCompany);

        backgroundTry = (RelativeLayout) findViewById(R.id.backgroundTry);
        backgroundViews = (LinearLayout) findViewById(R.id.backgroundViews);
        btnTry = (Button) findViewById(R.id.btnTry);
        prb_Loading = (ProgressBar) findViewById(R.id.prb_Loading);
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
                    updateUI();
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

                        // Reset Object Manager
                        ObjectManager.getInstance().clear();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                CenterActivity.mIRemoteService.resetXMPP();
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
                        ABSharedPreference.resetAccountInSharedPreferences();
                        ABSharedPreference.resetXMPP();
                        AndroidHelper.gotoActivity(SettingActivity.this, LoginActivity.class, true, true);
                    }
                }, null);
            }
        });
    }

    private void updateUI() {
        ObjectManager.getInstance().getUserMe(new ObjectManager.OnObjectManagerListener<UserMe>() {
            @Override
            public void onSuccess(UserMe me) {
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

            @Override
            public void onError(String error) {
                APIManager.showToastWithCode(error, SettingActivity.this);
                if (!AndroidHelper.isInternetAvailable(getApplicationContext())) {
                    backgroundTry.setVisibility(View.VISIBLE);
                    prb_Loading.setVisibility(View.GONE);
                    btnTry.setVisibility(View.VISIBLE);
                    backgroundViews.setVisibility(View.GONE);
                }
            }
        });
    }
}
