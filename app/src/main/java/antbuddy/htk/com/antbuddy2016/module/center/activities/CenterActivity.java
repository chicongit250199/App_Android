package antbuddy.htk.com.antbuddy2016.module.center.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.util.AttributeSet;
import android.view.View;

import antbuddy.htk.com.antbuddy2016.R;

/**
 * Created by thanhnguyen on 29/03/2016.
 */
public class CenterActivity extends FragmentActivity {

    private FragmentTabHost mTabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_center);

        //tabHost = new FragmentTabHost(getActivity());
        if (mTabHost == null) {
            mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);

            // Create the tabs in main_fragment.xml
            mTabHost.setup(CenterActivity.this, getSupportFragmentManager(), R.id.realtabcontent);

            mTabHost.addTab(mTabHost.newTabSpec("RecentTab").setIndicator("Recent"), RecentFragment.class, null);
            mTabHost.addTab(mTabHost.newTabSpec("MembersTab").setIndicator("Members"), MembersFragment.class, null);
            mTabHost.addTab(mTabHost.newTabSpec("GroupsTab").setIndicator("Groups"), GroupsFragment.class, null);
            mTabHost.addTab(mTabHost.newTabSpec("ProfileTab").setIndicator("Profile"), ProfileFragment.class, null);
            mTabHost.setCurrentTab(0);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
