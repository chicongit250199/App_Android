package antbuddy.htk.com.antbuddy2016.modules.center.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;

import java.util.ArrayList;

import antbuddy.htk.com.antbuddy2016.R;
import antbuddy.htk.com.antbuddy2016.adapters.ListRecentsAdapter;
import antbuddy.htk.com.antbuddy2016.model.RecentData;
import antbuddy.htk.com.antbuddy2016.modules.chat.ChatActivity;
import antbuddy.htk.com.antbuddy2016.util.AndroidHelper;

/**
 * Created by thanhnguyen on 30/03/2016.
 */
public class RecentFragment extends Fragment {
    
    private ExpandableListView list_recent;
    
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recent, container, false);
        list_recent = (ExpandableListView) rootView.findViewById(R.id.list_recent);
        ArrayList<String> groupNames = new ArrayList<>();
        groupNames.add("GROUPS");
        groupNames.add("MEMBERS");

        ArrayList<ArrayList<RecentData>> recentsData = new ArrayList<>();

        ArrayList<RecentData> recentData = new ArrayList<>();
        recentData.add(new RecentData());
        recentData.add(new RecentData());
        recentsData.add( recentData );

        recentData = new ArrayList<>();
        recentData.add(new RecentData());
        recentData.add(new RecentData());
        recentsData.add( recentData );

        recentData = new ArrayList<>();
        recentData.add(new RecentData());
        recentData.add(new RecentData());
        recentsData.add(recentData);

        ListRecentsAdapter listRecentsAdapter = new ListRecentsAdapter(getContext(),groupNames, recentsData);
        list_recent.setAdapter(listRecentsAdapter);
        list_recent.expandGroup(1);
        list_recent.expandGroup(0);

        //lock header
        list_recent.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
            }
        });

        list_recent.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                AndroidHelper.gotoActivity(getActivity(), ChatActivity.class);
                return true;
            }
        });
        return rootView;
    }
}
