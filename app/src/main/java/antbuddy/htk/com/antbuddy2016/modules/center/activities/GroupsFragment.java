package antbuddy.htk.com.antbuddy2016.modules.center.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import antbuddy.htk.com.antbuddy2016.R;
import antbuddy.htk.com.antbuddy2016.adapters.GroupAdapter;

/**
 * Created by thanhnguyen on 30/03/2016.
 */
public class GroupsFragment extends Fragment {

    private GridView groupsView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_groups, container, false);
        groupsView=(GridView)rootView.findViewById(R.id.gridView);
        GroupAdapter adapter = new GroupAdapter(getContext(), groupsView);
        groupsView.setAdapter(adapter);
        return rootView;
    }
}
