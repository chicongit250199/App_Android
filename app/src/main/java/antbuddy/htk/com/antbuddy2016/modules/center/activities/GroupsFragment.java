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
import antbuddy.htk.com.antbuddy2016.RealmObjects.RObjectManager;
import antbuddy.htk.com.antbuddy2016.RealmObjects.RRoom;
import antbuddy.htk.com.antbuddy2016.adapters.GroupAdapter;
import antbuddy.htk.com.antbuddy2016.api.APIManager;
import antbuddy.htk.com.antbuddy2016.interfaces.HttpRequestReceiver;
import antbuddy.htk.com.antbuddy2016.model.Room;
import antbuddy.htk.com.antbuddy2016.model.UserMe;
import antbuddy.htk.com.antbuddy2016.modules.chat.ChatActivity;
import antbuddy.htk.com.antbuddy2016.service.AntbuddyApplication;
import antbuddy.htk.com.antbuddy2016.util.AndroidHelper;
import antbuddy.htk.com.antbuddy2016.util.LogHtk;
import io.realm.RealmResults;

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

//        loading_Groups();
        updateUI();
        return rootView;
    }

    protected void initViews(View rootView) {
        backgroundTry = (RelativeLayout) rootView.findViewById(R.id.backgroundTry);
        backgroundViews = (LinearLayout) rootView.findViewById(R.id.backgroundViews);
        btnTry = (Button) rootView.findViewById(R.id.btnTry);
        prb_Loading = (ProgressBar) rootView.findViewById(R.id.prb_Loading);
        groupsView=(GridView)rootView.findViewById(R.id.gridView);
        adapter = new GroupAdapter(getContext(), groupsView, RObjectManager.getInstance().getRoomsFromCache());
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
                RRoom room = RObjectManager.getInstance().getRoomsFromCache().get(position);
                Bundle args = new Bundle();
                args.putString(ChatActivity.kKeyRoom, room.getKey());
                args.putBoolean(ChatActivity.key_type, true);
                args.putString(ChatActivity.key_title, room.getName());
                AndroidHelper.gotoActivity(getActivity(), ChatActivity.class, args);
            }
        });
    }

    protected void loading_Groups() {
        Boolean isdataLoadedFromDB = false;
        AntbuddyApplication application = AntbuddyApplication.getInstance();
//        if (application.isUserMeExist() && application.isUsersExist() && application.isRoomsExist()) {
//            updateUI();
//            isdataLoadedFromDB = true;
//        }
//        if (RObjectManager.isUserMeExist() && RObjectManager.isUsersExist() && RObjectManager.isRoomsExist()) {
//            updateUI();
//        }

        if (AndroidHelper.isInternetAvailable(getActivity().getApplicationContext())) {
//            APIManager.GETGroups(new HttpRequestReceiver<List<Room>>() {
//                @Override
//                public void onSuccess(List<Room> rooms) {
//                    RObjectManager.saveRoomsOrUpdate(rooms);
//                    AntbuddyApplication.getInstance().setRooms(RObjectManager.getRooms());
//                    updateUI();
//                }
//
//                @Override
//                public void onError(String error) {
//                    processUIWhenNoConnection();
//                }
//            });
        } else if (!isdataLoadedFromDB) {    // No connection and No data in DB
            processUIWhenNoConnection();
        }
    }

    private void updateUI() {
        RealmResults<RRoom> rooms = RObjectManager.getInstance().getRoomsFromCache();

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

    private void processUIWhenNoConnection() {
        if (!AndroidHelper.isInternetAvailable(getContext())) {
            backgroundTry.setVisibility(View.VISIBLE);
            prb_Loading.setVisibility(View.GONE);
            btnTry.setVisibility(View.VISIBLE);
            backgroundViews.setVisibility(View.GONE);
        }
    }
}
