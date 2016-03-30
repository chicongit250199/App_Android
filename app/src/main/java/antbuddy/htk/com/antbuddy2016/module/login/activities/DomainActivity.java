package antbuddy.htk.com.antbuddy2016.module.login.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

import antbuddy.htk.com.antbuddy2016.R;
import antbuddy.htk.com.antbuddy2016.module.login.adapter.DomainAdapter;
import antbuddy.htk.com.antbuddy2016.objects.Domain;

/**
 * Created by thanhnguyen on 29/03/2016.
 */
public class DomainActivity extends Activity {

    ArrayList<Domain> domainList;
    DomainAdapter domainAdapter;
    ListView domainListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_domain);

        // Init
        domainList = new ArrayList<Domain>();
        domainList.add(new Domain("htkinc"));
        domainList.add(new Domain("htk-dn"));
        domainList.add(new Domain("htk-hcm"));

        domainAdapter = new DomainAdapter(this, R.layout.item_domain, domainList);
        domainListView = (ListView) findViewById(R.id.domain_ListView);

        domainListView.setAdapter(domainAdapter);
    }
}
