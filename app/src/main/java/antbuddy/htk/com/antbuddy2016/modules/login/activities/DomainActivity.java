package antbuddy.htk.com.antbuddy2016.modules.login.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import antbuddy.htk.com.antbuddy2016.R;
import antbuddy.htk.com.antbuddy2016.api.APIManager;
import antbuddy.htk.com.antbuddy2016.interfaces.HttpRequestReceiver;
import antbuddy.htk.com.antbuddy2016.model.Organization;
import antbuddy.htk.com.antbuddy2016.modules.center.activities.CenterActivity;
import antbuddy.htk.com.antbuddy2016.modules.login.adapter.DomainAdapter;
import antbuddy.htk.com.antbuddy2016.service.AntbuddyApplication;
import antbuddy.htk.com.antbuddy2016.setting.ABSharedPreference;
import antbuddy.htk.com.antbuddy2016.util.AndroidHelper;
import antbuddy.htk.com.antbuddy2016.util.LogHtk;

/**
 * Created by thanhnguyen on 29/03/2016.
 */
public class DomainActivity extends Activity {

    public static final String TAG_THISCLASS = "DomainActivity";
    List<Organization> domainList;
    DomainAdapter domainAdapter;
    ListView domainListView;
    ProgressBar progressBar_Domain;
    Button btnRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_domain);
        ABSharedPreference.triggerCurrentScreen(ABSharedPreference.CURRENTSCREEN.DOMAIN_ACTIVITY);
        ABSharedPreference.save(ABSharedPreference.KEY_IS_LOGIN, false);

        initViews();
        viewsListener();

        // Request Data to show List organizations
        requestAPIToGetOrganizations();
    }

    private void initViews() {
        progressBar_Domain = (ProgressBar) findViewById(R.id.progressBar_Domain);
        domainList = new ArrayList<>();
        domainAdapter = new DomainAdapter(this, domainList);
        domainListView = (ListView) findViewById(R.id.domain_ListView);
        domainListView.setAdapter(domainAdapter);
        btnRefresh = (Button) findViewById(R.id.btnRefresh);
    }

    private void viewsListener() {
        domainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String domain = domainList.get(position).getDomain();
                if (domain.length() > 0) {
                    ABSharedPreference.save(ABSharedPreference.KEY_DOMAIN, domain);
                    Intent myIntent = new Intent(DomainActivity.this, CenterActivity.class);
                    startActivity(myIntent);
                    finish();
                }
            }
        });

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnRefresh.setVisibility(View.GONE);
                requestAPIToGetOrganizations();
            }
        });
    }

    @Override
    public void onBackPressed() {
        AndroidHelper.alertDialogShow(this, "Do you want to sign out?", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                LoReActivity.resetAccountInSharedPreferences();
                LoReActivity.resetXMPP();
                Intent myIntent = new Intent(DomainActivity.this, LoginActivity.class);
                startActivity(myIntent);
                finish();
            }
        }, null);
    }

    private void requestAPIToGetOrganizations() {
        if (!AndroidHelper.isInternetAvailable(getApplicationContext())) {
            Toast.makeText(this, "No network connection available", Toast.LENGTH_SHORT).show();
            btnRefresh.setVisibility(View.VISIBLE);
            return;
        }

        if (ABSharedPreference.getAccountConfig().getToken().length() > 0) {
            AndroidHelper.showProgressBar(DomainActivity.this, progressBar_Domain);

            APIManager.GETOrganizations(new HttpRequestReceiver<List<Organization>>() {
                @Override
                public void onSuccess(List<Organization> listOrgs) {
                    LogHtk.d(TAG_THISCLASS, "Domain response !: " + listOrgs.size());
                    domainList.clear();
                    domainList.addAll(listOrgs);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            domainAdapter.notifyDataSetChanged();
                            }
                        });
                    AndroidHelper.hideProgressBar(DomainActivity.this, progressBar_Domain);
                }

                @Override
                public void onError(String error) {
                    btnRefresh.setVisibility(View.VISIBLE);
                    AndroidHelper.hideProgressBar(DomainActivity.this, progressBar_Domain);
                    APIManager.showToastWithCode(error, DomainActivity.this);
                }
            });
        }
    }
}
