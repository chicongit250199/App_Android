package antbuddy.htk.com.antbuddy2016.modules.center.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import java.util.List;

import antbuddy.htk.com.antbuddy2016.R;
import antbuddy.htk.com.antbuddy2016.adapters.GroupAdapter;
import antbuddy.htk.com.antbuddy2016.model.ObjectManager;
import antbuddy.htk.com.antbuddy2016.model.Room;
import antbuddy.htk.com.antbuddy2016.modules.chat.ChatActivity;
import antbuddy.htk.com.antbuddy2016.util.AndroidHelper;
import antbuddy.htk.com.antbuddy2016.util.LogHtk;

/**
 * Created by thanhnguyen on 30/03/2016.
 */
public class GroupsFragment extends Fragment {

    private GridView groupsView;
    private Button refresh_Groups_Button;
    private TextView empty_groups_TextView;

    GroupAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_groups, container, false);
        initViews(rootView);

        groupsView=(GridView)rootView.findViewById(R.id.gridView);
        adapter = new GroupAdapter(getContext(), groupsView, ObjectManager.getInstance().getListRooms());
        groupsView.setAdapter(adapter);

        groupsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Room room = ObjectManager.getInstance().getListRooms().get(position);
                Bundle args = new Bundle();
                args.putString(ChatActivity.key_key, room.getKey());
                args.putBoolean(ChatActivity.key_type, true);
                args.putString(ChatActivity.key_title, room.getName());
                AndroidHelper.gotoActivity(getActivity(), ChatActivity.class, args);
            }
        });

        ObjectManager.getInstance().setOnListenerRoom(this.getClass(), new ObjectManager.OnListenerGroup() {
            @Override
            public void onResponse(List<Room> listRooms) {
                adapter.notifyDataSetChanged();
                checkRooms();
            }
        });


        return rootView;
    }

    protected void initViews(View rootView) {
        refresh_Groups_Button = (Button) rootView.findViewById(R.id.refresh_Groups_Button);
        refresh_Groups_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkRooms();
            }
        });

        empty_groups_TextView = (TextView) rootView.findViewById(R.id.empty_groups_TextView);
    }

    private void checkRooms() {
        List<Room> listRooms = ObjectManager.getInstance().getListRooms();
        if (listRooms.size() == 0) {
            // show ra giao dien Empty
            empty_groups_TextView.setVisibility(View.VISIBLE);

        } if (listRooms.size() > 0) {
            empty_groups_TextView.setVisibility(View.GONE);
            adapter.setListRooms(ObjectManager.getInstance().getListRooms());
            adapter.notifyDataSetChanged();
        }
        refresh_Groups_Button.setVisibility(View.GONE);

//        if (ObjectManager.getInstance().isGroupsNeedToReload()) {
//            refresh_Groups_Button.setVisibility(View.VISIBLE);
//        }
    }
}
