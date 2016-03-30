package antbuddy.htk.com.antbuddy2016.module.login.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayList;

import antbuddy.htk.com.antbuddy2016.R;
import antbuddy.htk.com.antbuddy2016.api.HttpRequestReceiver;
import antbuddy.htk.com.antbuddy2016.api.LoginAPI;
import antbuddy.htk.com.antbuddy2016.module.login.adapter.DomainAdapter;
import antbuddy.htk.com.antbuddy2016.objects.Domain;
import antbuddy.htk.com.antbuddy2016.util.AndroidHelper;

/**
 * Created by thanhnguyen on 29/03/2016.
 */
public class DomainActivity extends Activity {

    ArrayList<Domain> domainList;
    DomainAdapter domainAdapter;
    ListView domainListView;

    ProgressBar progressBar_Domain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_domain);

        progressBar_Domain = (ProgressBar) findViewById(R.id.progressBar_Domain);

        // Init
        domainList = new ArrayList<Domain>();
        domainList.add(new Domain("htkinc"));
        domainList.add(new Domain("htk-dn"));
        domainList.add(new Domain("htk-hcm"));

        domainAdapter = new DomainAdapter(this, R.layout.item_domain, domainList);
        domainListView = (ListView) findViewById(R.id.domain_ListView);

        domainListView.setAdapter(domainAdapter);


        // Request Data to show List organizations
        requestAPIToGetOrganizations();

    }

    private void requestAPIToGetOrganizations() {
        //antbuddytesting1@gmail.com/111qqq111
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    // Request
                    AndroidHelper.showProgressBar(DomainActivity.this, progressBar_Domain);
                    LoginAPI.GETOrganizations(new HttpRequestReceiver() {
                        @Override
                        public void onSuccess(String result) {
                            Log.d("DaiThanh", "requestAPIToGetOrganizations success!");
                            AndroidHelper.hideProgressBar(DomainActivity.this, progressBar_Domain);
                        }

                        @Override
                        public void onError(String error) {
                            Log.d("DaiThanh", "requestAPIToGetOrganizations ERROR!");
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
