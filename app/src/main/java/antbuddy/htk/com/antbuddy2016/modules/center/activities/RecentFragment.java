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
import antbuddy.htk.com.antbuddy2016.adapters.RecentsAdapter;
import antbuddy.htk.com.antbuddy2016.api.APIManager;
import antbuddy.htk.com.antbuddy2016.model.ObjectManager;
import antbuddy.htk.com.antbuddy2016.model.OpeningChatRoom;
import antbuddy.htk.com.antbuddy2016.model.Room;
import antbuddy.htk.com.antbuddy2016.model.User;
import antbuddy.htk.com.antbuddy2016.model.UserMe;
import antbuddy.htk.com.antbuddy2016.modules.chat.ChatActivity;
import antbuddy.htk.com.antbuddy2016.util.AndroidHelper;
import antbuddy.htk.com.antbuddy2016.util.LogHtk;

/**
 * Created by thanhnguyen on 30/03/2016.
 */
public class RecentFragment extends Fragment {

    List<List<OpeningChatRoom>> recentsData;
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

        View rootView = inflater.inflate(R.layout.fragment_recent, container, false);
        list_recent = (ExpandableListView) rootView.findViewById(R.id.list_recent);
        ArrayList<String> groupNames = new ArrayList<>();
        groupNames.add("GROUPS");
        groupNames.add("MEMBERS");

        recentsData = new ArrayList<>();
        recentsData.add(new ArrayList<OpeningChatRoom>());
        recentsData.add(new ArrayList<OpeningChatRoom>());

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
                    OpeningChatRoom openingChatRoom = (OpeningChatRoom) recentsAdapter.getChild(groupPosition, childPosition);
                    Room room = ObjectManager.getInstance().findRoom(openingChatRoom.getChatRoomKey());
                    args.putString(ChatActivity.key_key, room.getKey());
                    args.putBoolean(ChatActivity.key_type, true);
                    args.putString(ChatActivity.key_title, room.getName());
                } else {
                    OpeningChatRoom openingChatRoom = (OpeningChatRoom) recentsAdapter.getChild(groupPosition, childPosition);
                    User user = ObjectManager.getInstance().findUser(openingChatRoom.getChatRoomKey());
                    args.putString(ChatActivity.key_key, user.getKey());
                    args.putBoolean(ChatActivity.key_type, false);
                    args.putString(ChatActivity.key_title, user.getName());
                }
                AndroidHelper.gotoActivity(getActivity(), ChatActivity.class, args);
                return true;
            }
        });

        initViews(rootView);
        viewsListener();
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
                    updateUI();
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    protected void updateUI() {
        LogHtk.i(LogHtk.RecentFragment, "updateUI");
        prb_LoadingFisrt.setVisibility(View.VISIBLE);
        // LOGIN XMPP
        //UserMe
        ObjectManager.getInstance().getUserMe(new ObjectManager.OnObjectManagerListener<UserMe>() {
            @Override
            public void onSuccess(final UserMe me) {
                loadUsers();
            }

            @Override
            public void onError(String error) {
                LogHtk.e(LogHtk.RecentFragment, "error = " + error);
                processUIWhenError(error);
            }
        });
    }

    private void loadUsers() {
        ObjectManager.getInstance().setOnListenerUsers(null, new ObjectManager.OnObjectManagerListener<List<User>>() {
            @Override
            public void onSuccess(List<User> users) {
                loadRooms();
            }

            @Override
            public void onError(String error) {
                LogHtk.e(LogHtk.RecentFragment, "ERROR to loading users : " + error);
                processUIWhenError(error);
            }
        });
    }

    private void loadRooms() {
        ObjectManager.getInstance().setOnListenerRooms(null, new ObjectManager.OnObjectManagerListener<List<Room>>() {
            @Override
            public void onSuccess(List<Room> rooms) {
                CenterActivity.mIRemoteService.resetXMPP();

                UserMe me = ObjectManager.getInstance().getUserMe();
                if (me == null) {
                    LogHtk.e(LogHtk.RecentFragment, "UserMe is null!");
                    backgroundTry.setVisibility(View.VISIBLE);
                    prb_Loading.setVisibility(View.GONE);
                    btnTry.setVisibility(View.VISIBLE);
                    backgroundViews.setVisibility(View.GONE);
                    prb_LoadingFisrt.setVisibility(View.GONE);
                } else {
                    if (me.getOpeningChatrooms() != null) {
                        recentsData.get(ChatType.Group.getValue()).clear();
                        recentsData.get(ChatType.OneToOne.getValue()).clear();
                        recentsData.get(ChatType.Group.getValue()).addAll(UserMe.getChatsOpening(me, true));
                        recentsData.get(ChatType.OneToOne.getValue()).addAll(UserMe.getChatsOpening(me, false));
                        recentsAdapter.notifyDataSetChanged();
                        backgroundTry.setVisibility(View.GONE);
                        prb_Loading.setVisibility(View.GONE);
                        btnTry.setVisibility(View.VISIBLE);
                        backgroundViews.setVisibility(View.VISIBLE);
                        prb_LoadingFisrt.setVisibility(View.GONE);
                    }

                    CenterActivity.connectXMPP(me);
                }
            }

            @Override
            public void onError(String error) {
                LogHtk.e(LogHtk.RecentFragment, "error = " + error);
                APIManager.showToastWithCode(error, getActivity());
                processUIWhenError(error);
            }
        });
    }

    private void processUIWhenError(final String error) {
        LogHtk.e(LogHtk.RecentFragment, "error = " + error);
        APIManager.showToastWithCode(error, getActivity());
        if (!AndroidHelper.isInternetAvailable(getContext())) {
            backgroundTry.setVisibility(View.VISIBLE);
            prb_Loading.setVisibility(View.GONE);
            btnTry.setVisibility(View.VISIBLE);
            backgroundViews.setVisibility(View.GONE);
            prb_LoadingFisrt.setVisibility(View.GONE);
        }
    }
}
