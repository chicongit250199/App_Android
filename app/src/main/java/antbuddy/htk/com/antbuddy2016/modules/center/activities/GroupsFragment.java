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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import antbuddy.htk.com.antbuddy2016.R;
import antbuddy.htk.com.antbuddy2016.adapters.GroupAdapter;
import antbuddy.htk.com.antbuddy2016.api.APIManager;
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

    GroupAdapter adapter;

    private RelativeLayout backgroundTry;
    private LinearLayout backgroundViews;
    private ProgressBar prb_Loading;
    private Button btnTry;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_groups, container, false);
        initViews(rootView);
        viewsListener();

        updateUI();
        return rootView;
    }

    protected void initViews(View rootView) {
        backgroundTry = (RelativeLayout) rootView.findViewById(R.id.backgroundTry);
        backgroundViews = (LinearLayout) rootView.findViewById(R.id.backgroundViews);
        btnTry = (Button) rootView.findViewById(R.id.btnTry);
        prb_Loading = (ProgressBar) rootView.findViewById(R.id.prb_Loading);
        groupsView=(GridView)rootView.findViewById(R.id.gridView);
        adapter = new GroupAdapter(getContext(), groupsView, ObjectManager.getInstance().getListRooms());
        groupsView.setAdapter(adapter);
    }

    private void viewsListener() {
        btnTry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prb_Loading.setVisibility(View.VISIBLE);
                btnTry.setVisibility(View.GONE);
                if (!AndroidHelper.isInternetAvailable(getContext())) {
                    AndroidHelper.warningInternetConnection(getActivity());
                    prb_Loading.setVisibility(View.GONE);
                    btnTry.setVisibility(View.VISIBLE);
                } else {
                    updateUI();
                }
            }
        });

        groupsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Room room = ObjectManager.getInstance().getListRooms().get(position);
                Bundle args = new Bundle();
                args.putString(ChatActivity.kKeyRoom, room.getKey());
                args.putBoolean(ChatActivity.key_type, true);
                args.putString(ChatActivity.key_title, room.getName());
                AndroidHelper.gotoActivity(getActivity(), ChatActivity.class, args);
            }
        });
    }

    protected void updateUI() {
        ObjectManager.getInstance().setOnListenerRooms(this.getClass(), new ObjectManager.OnObjectManagerListener<List<Room>>() {
            @Override
            public void onSuccess(List<Room> rooms) {
                LogHtk.i(LogHtk.GroupsFragment, "Size of rooms = " + rooms.size());
                adapter.notifyDataSetChanged();

                if (rooms.size() == 0) {
                    // TODO show ra giao dien Empty

                } if (rooms.size() > 0) {
                    adapter.setListRooms(rooms);
                    adapter.notifyDataSetChanged();
                }

                backgroundTry.setVisibility(View.GONE);
                prb_Loading.setVisibility(View.GONE);
                btnTry.setVisibility(View.VISIBLE);
                backgroundViews.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError(String error) {
                LogHtk.e(LogHtk.GroupsFragment, "ERROR! = " + error);
                APIManager.showToastWithCode(error, getActivity());
                if (!AndroidHelper.isInternetAvailable(getContext())) {
                    backgroundTry.setVisibility(View.VISIBLE);
                    prb_Loading.setVisibility(View.GONE);
                    btnTry.setVisibility(View.VISIBLE);
                    backgroundViews.setVisibility(View.GONE);
                }
            }
        });
    }
}
