package antbuddy.htk.com.antbuddy2016.modules.center.activities;

import android.os.Bundle;
import android.os.HandlerThread;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import antbuddy.htk.com.antbuddy2016.R;
import antbuddy.htk.com.antbuddy2016.RealmObjects.RObjectManager;
import antbuddy.htk.com.antbuddy2016.RealmObjects.ROpeningChatRoom;
import antbuddy.htk.com.antbuddy2016.RealmObjects.RRoom;
import antbuddy.htk.com.antbuddy2016.RealmObjects.RUser;
import antbuddy.htk.com.antbuddy2016.RealmObjects.RUserMe;
import antbuddy.htk.com.antbuddy2016.adapters.RecentsAdapter;
import antbuddy.htk.com.antbuddy2016.api.APIManager;
import antbuddy.htk.com.antbuddy2016.interfaces.HttpRequestReceiver;
import antbuddy.htk.com.antbuddy2016.model.OpeningChatRoom;
import antbuddy.htk.com.antbuddy2016.model.Room;
import antbuddy.htk.com.antbuddy2016.model.User;
import antbuddy.htk.com.antbuddy2016.model.UserMe;
import antbuddy.htk.com.antbuddy2016.modules.chat.ChatActivity;
import antbuddy.htk.com.antbuddy2016.service.AntbuddyApplication;
import antbuddy.htk.com.antbuddy2016.service.AntbuddyService;
import antbuddy.htk.com.antbuddy2016.util.AndroidHelper;
import antbuddy.htk.com.antbuddy2016.util.LogHtk;
import io.realm.RealmResults;

/**
 * Created by thanhnguyen on 30/03/2016.
 */
public class RecentFragment extends Fragment {

    List<List<ROpeningChatRoom>> recentsData;
    private ExpandableListView list_recent;
    private RecentsAdapter recentsAdapter;

    private RelativeLayout backgroundTry;
    private LinearLayout backgroundViews;
    private ProgressBar prb_Loading;
    private Button btnTry;
    private ProgressBar prb_LoadingFisrt;

    public enum ChatType {
        Group(0), OneToOne(1);
        private final int value;
        private ChatType(int value) {
            this.value = value;
        }
        public int getValue() {
            return value;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LogHtk.i(LogHtk.Test3, "RecentFragment onCreate!");

        View rootView = inflater.inflate(R.layout.fragment_recent, container, false);
        list_recent = (ExpandableListView) rootView.findViewById(R.id.list_recent);
        ArrayList<String> groupNames = new ArrayList<>();
        groupNames.add("GROUPS");
        groupNames.add("MEMBERS");

        recentsData = new ArrayList<>();
        recentsData.add(new ArrayList<ROpeningChatRoom>());
        recentsData.add(new ArrayList<ROpeningChatRoom>());

        recentsAdapter = new RecentsAdapter(getContext(),groupNames, recentsData);
        list_recent.setAdapter(recentsAdapter);
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
                Bundle args = new Bundle();
                if (groupPosition == 0) {
                    ROpeningChatRoom openingChatRoom = (ROpeningChatRoom) recentsAdapter.getChild(groupPosition, childPosition);
                    RRoom room = RObjectManager.findRoom(openingChatRoom.getChatRoomKey());
                    args.putString(ChatActivity.kKeyRoom, room.getKey());
                    args.putBoolean(ChatActivity.key_type, true);
                    args.putString(ChatActivity.key_title, room.getName());
                } else {
                    ROpeningChatRoom openingChatRoom = (ROpeningChatRoom) recentsAdapter.getChild(groupPosition, childPosition);
                    RUser user = RObjectManager.findUser(openingChatRoom.getChatRoomKey());
                    args.putString(ChatActivity.kKeyRoom, user.getKey());
                    args.putBoolean(ChatActivity.key_type, false);
                    args.putString(ChatActivity.key_title, user.getName());
                }
                AndroidHelper.gotoActivity(getActivity(), ChatActivity.class, args);
                return true;
            }
        });

        initViews(rootView);
        viewsListener();

        // Service created at Application, but we need to check it again
//        if (AntbuddyService.getInstance() != null) {
//            AntbuddyService.getInstance().resetXMPP();
//        } else {
//            LogHtk.e(LogHtk.RecentFragment, "Warning! Android Local Service is null at recentFragment!");
//        }

        //loading_UserMe_Users_Rooms();

        updateUI();
        return rootView;
    }

    private void initViews(View root) {
        backgroundTry = (RelativeLayout) root.findViewById(R.id.backgroundTry);
        backgroundViews = (LinearLayout) root.findViewById(R.id.backgroundViews);
        btnTry = (Button) root.findViewById(R.id.btnTry);
        prb_Loading = (ProgressBar) root.findViewById(R.id.prb_Loading);
        prb_LoadingFisrt = (ProgressBar) root.findViewById(R.id.prb_LoadingFisrt);
    }

    private void viewsListener() {
        btnTry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CenterActivity.mIRemoteService.resetXMPP();

                prb_Loading.setVisibility(View.VISIBLE);
                btnTry.setVisibility(View.GONE);
                if (!AndroidHelper.isInternetAvailable(getContext())) {
                    AndroidHelper.warningInternetConnection(getActivity());
                    prb_Loading.setVisibility(View.GONE);
                    btnTry.setVisibility(View.VISIBLE);
                } else {
                    CenterActivity.mIRemoteService.resetXMPP();
                    updateUI();
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    protected void loading_UserMe_Users_Rooms() {
        Boolean isdataLoadedFromDB = false;
        AntbuddyApplication application = AntbuddyApplication.getInstance();
//        if (application.isUserMeExist() && application.isUsersExist() && application.isRoomsExist()) {
//            updateUI();
//            isdataLoadedFromDB = true;
//        }

        if (AndroidHelper.isInternetAvailable(getActivity().getApplicationContext())) {
            APIManager.GETUserMe(new HttpRequestReceiver<UserMe>() {
                @Override
                public void onSuccess(UserMe me) {
                    RObjectManager.getInstance().saveUserMeOrUpdate(me);
//                    AntbuddyApplication.getInstance().setUserme(RObjectManager.getInstance().getUserMe());
                    loadUsers();
                }

                @Override
                public void onError(String error) {
                    LogHtk.e(LogHtk.RecentFragment, "Error! Can not load UserMe from server!");
                    APIManager.showToastWithCode(error, getActivity());
                    processUIWhenError();
                }
            });
        } else if (!isdataLoadedFromDB) {    // No connection and No data in DB
            processUIWhenError();
        }
    }

    private void loadUsers() {
        APIManager.GETUsers(new HttpRequestReceiver<List<User>>() {
            @Override
            public void onSuccess(List<User> users) {
//                RObjectManager.saveUsersOrUpdate(users);
//                AntbuddyApplication.getInstance().setUsers(RObjectManager.getUsers());
                loadRooms();
            }

            @Override
            public void onError(String error) {
                LogHtk.e(LogHtk.RecentFragment, "Error! Can not load Users from server!");
                APIManager.showToastWithCode(error, getActivity());
                processUIWhenError();
            }
        });
    }

    private void loadRooms() {
        APIManager.GETGroups(new HttpRequestReceiver<List<Room>>() {
            @Override
            public void onSuccess(List<Room> rooms) {
//                RObjectManager.saveRoomsOrUpdate(rooms);
//                AntbuddyApplication.getInstance().setRooms(RObjectManager.getRooms());
                updateUI();
            }

            @Override
            public void onError(String error) {
                LogHtk.e(LogHtk.RecentFragment, "Error! Can not load Rooms from server!");
                APIManager.showToastWithCode(error, getActivity());
                processUIWhenError();

            }
        });
    }

    protected void updateUI() {
        RUserMe me = RObjectManager.getInstance().getUserMeFromCache();

        if (me == null) {
            backgroundTry.setVisibility(View.VISIBLE);
            prb_Loading.setVisibility(View.GONE);
            btnTry.setVisibility(View.VISIBLE);
            backgroundViews.setVisibility(View.GONE);
            prb_LoadingFisrt.setVisibility(View.GONE);
        } else {
            LogHtk.i(LogHtk.Test3, "Recent: " + me.toString());
            if (me.getOpeningChatrooms() != null) {
                recentsData.get(ChatType.Group.getValue()).clear();
                recentsData.get(ChatType.OneToOne.getValue()).clear();
                recentsData.get(ChatType.Group.getValue()).addAll(RUserMe.getChatsOpening(me, true));
                recentsData.get(ChatType.OneToOne.getValue()).addAll(RUserMe.getChatsOpening(me, false));

                recentsAdapter.notifyDataSetChanged();
                backgroundTry.setVisibility(View.GONE);
                prb_Loading.setVisibility(View.GONE);
                btnTry.setVisibility(View.VISIBLE);
                backgroundViews.setVisibility(View.VISIBLE);
                prb_LoadingFisrt.setVisibility(View.GONE);
            } else {
                LogHtk.i(LogHtk.Test3, "getOpeningChatrooms");
            }

//            CenterActivity.connectXMPP(me);
//            try {
//                if (!AntbuddyService.getInstance().isXMPPConnected()) {
//
//                } else {
//
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//                LogHtk.e(LogHtk.RecentFragment, "Warning! XMPPConnection is null at RecentFragment!");
//            }
        }
    }

    private void processUIWhenError() {
        if (!AndroidHelper.isInternetAvailable(getContext())) {
            backgroundTry.setVisibility(View.VISIBLE);
            prb_Loading.setVisibility(View.GONE);
            btnTry.setVisibility(View.VISIBLE);
            backgroundViews.setVisibility(View.GONE);
            prb_LoadingFisrt.setVisibility(View.GONE);
        }
    }
}
