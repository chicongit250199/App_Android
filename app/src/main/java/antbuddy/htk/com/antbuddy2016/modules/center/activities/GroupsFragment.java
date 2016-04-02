package antbuddy.htk.com.antbuddy2016.modules.center.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;

import antbuddy.htk.com.antbuddy2016.R;
import antbuddy.htk.com.antbuddy2016.adapters.GroupAdapter;
import antbuddy.htk.com.antbuddy2016.model.ObjectManager;

/**
 * Created by thanhnguyen on 30/03/2016.
 */
public class GroupsFragment extends Fragment {

    private GridView groupsView;
    private Button refresh_Groups_Button;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_groups, container, false);
        initViews(rootView);

        groupsView=(GridView)rootView.findViewById(R.id.gridView);
        GroupAdapter adapter = new GroupAdapter(getContext(), groupsView, ObjectManager.getInstance().getListRooms());
        groupsView.setAdapter(adapter);

        if (!ObjectManager.getInstance().isGroupsLoaded()) {
            refresh_Groups_Button.setVisibility(View.VISIBLE);
        } else {
            adapter.setListRooms(ObjectManager.getInstance().getListRooms());
            adapter.notifyDataSetChanged();
        }

        return rootView;
    }

    protected void initViews(View rootView) {
        refresh_Groups_Button = (Button) rootView.findViewById(R.id.refresh_Groups_Button);
    }
}
