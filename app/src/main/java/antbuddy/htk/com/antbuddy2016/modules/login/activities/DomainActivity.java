package antbuddy.htk.com.antbuddy2016.modules.login.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

import antbuddy.htk.com.antbuddy2016.R;
import antbuddy.htk.com.antbuddy2016.interfaces.HttpRequestReceiver;
import antbuddy.htk.com.antbuddy2016.api.LoginAPI;
import antbuddy.htk.com.antbuddy2016.api.ParseJson;
import antbuddy.htk.com.antbuddy2016.modules.center.activities.CenterActivity;
import antbuddy.htk.com.antbuddy2016.modules.login.adapter.DomainAdapter;
import antbuddy.htk.com.antbuddy2016.objects.Domain;
import antbuddy.htk.com.antbuddy2016.util.AndroidHelper;
import antbuddy.htk.com.antbuddy2016.util.Constants;
import antbuddy.htk.com.antbuddy2016.util.LogHtk;

/**
 * Created by thanhnguyen on 29/03/2016.
 */
public class DomainActivity extends Activity {

    public static final String TAG_THISCLASS = "DomainActivity";

    ArrayList<Domain> domainList;
    DomainAdapter domainAdapter;
    ListView domainListView;

    ProgressBar progressBar_Domain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_domain);

        progressBar_Domain = (ProgressBar) findViewById(R.id.progressBar_Domain);
        domainList = new ArrayList<Domain>();
        domainAdapter = new DomainAdapter(this, domainList);
        domainListView = (ListView) findViewById(R.id.domain_ListView);
        domainListView.setAdapter(domainAdapter);

        // Request Data to show List organizations
        requestAPIToGetOrganizations();

        domainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Constants.domain = domainList.get(position).getDomain();
                Intent myIntent = new Intent(DomainActivity.this, CenterActivity.class);
                startActivity(myIntent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        AndroidHelper.alertDialogShow(this, "Do you want to sign out?", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent myIntent = new Intent(DomainActivity.this, LoginActivity.class);
                startActivity(myIntent);
                finish();
            }
        }, null);
    }

    private void requestAPIToGetOrganizations() {
        if (!AndroidHelper.isInternetAvailable(getApplicationContext())) {
            Toast.makeText(this, "No network connection available", Toast.LENGTH_SHORT).show();
            return;
        }

        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    AndroidHelper.showProgressBar(DomainActivity.this, progressBar_Domain);
                    LoginAPI.GETOrganizations(new HttpRequestReceiver() {
                        @Override
                        public void onSuccess(String result) {
                            LogHtk.d(TAG_THISCLASS, "requestAPIToGetOrganizations success!: " + result);

                            domainList.clear();
                            domainList.addAll(ParseJson.parseToListDomains(result));
                            LogHtk.d(TAG_THISCLASS, "domainList.count: " + domainList.size());
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
                            AndroidHelper.hideProgressBar(DomainActivity.this, progressBar_Domain);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
}
