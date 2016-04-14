package antbuddy.htk.com.antbuddy2016.modules.center.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import antbuddy.htk.com.antbuddy2016.R;
import antbuddy.htk.com.antbuddy2016.RealmObjects.ROpeningChatRoom;
import antbuddy.htk.com.antbuddy2016.RealmObjects.RUserMe;
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
 * Created by thanhnguyen on 09/04/2016.
 */
public class OpeningRoomActivity extends Activity{

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_openingroom);

        list_recent = (ExpandableListView) findViewById(R.id.list_recent);
        ArrayList<String> groupNames = new ArrayList<>();
        groupNames.add("GROUPS");
        groupNames.add("MEMBERS");

        recentsData = new ArrayList<>();
        recentsData.add(new ArrayList<ROpeningChatRoom>());
        recentsData.add(new ArrayList<ROpeningChatRoom>());

        recentsAdapter = new RecentsAdapter(getApplicationContext(),groupNames, recentsData);
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
                    args.putString(ChatActivity.kKeyRoom, room.getKey());
                    args.putBoolean(ChatActivity.key_type, true);
                    args.putString(ChatActivity.key_title, room.getName());
                } else {
                    OpeningChatRoom openingChatRoom = (OpeningChatRoom) recentsAdapter.getChild(groupPosition, childPosition);
                    User user = ObjectManager.getInstance().findUser(openingChatRoom.getChatRoomKey());
                    args.putString(ChatActivity.kKeyRoom, user.getKey());
                    args.putBoolean(ChatActivity.key_type, false);
                    args.putString(ChatActivity.key_title, user.getName());
                }
                AndroidHelper.gotoActivity(OpeningRoomActivity.this, ChatActivity.class, args);
                return true;
            }
        });

        initViews();
        viewsListener();
        updateUI();
    }

    private void initViews() {
        backgroundTry = (RelativeLayout) findViewById(R.id.backgroundTry);
        backgroundViews = (LinearLayout) findViewById(R.id.backgroundViews);
        btnTry = (Button) findViewById(R.id.btnTry);
        prb_Loading = (ProgressBar) findViewById(R.id.prb_Loading);
        prb_LoadingFisrt = (ProgressBar) findViewById(R.id.prb_LoadingFisrt);
    }

    private void viewsListener() {
        btnTry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CenterActivity.mIRemoteService.resetXMPP();

                prb_Loading.setVisibility(View.VISIBLE);
                btnTry.setVisibility(View.GONE);
                if (!AndroidHelper.isInternetAvailable(getApplicationContext())) {
                    AndroidHelper.warningInternetConnection(OpeningRoomActivity.this);
                    prb_Loading.setVisibility(View.GONE);
                    btnTry.setVisibility(View.VISIBLE);
                } else {
                    updateUI();
                }
            }
        });
    }

    protected void updateUI() {
        LogHtk.i(LogHtk.RecentFragment, "updateUI");
        prb_LoadingFisrt.setVisibility(View.VISIBLE);
        // LOGIN XMPP
        //UserMe
        ObjectManager.getInstance().getUserMe(new ObjectManager.OnObjectManagerListener<RUserMe>() {
            @Override
            public void onSuccess(final RUserMe me) {
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
        ObjectManager.getInstance().setOnListenerUsers(RecentFragment.class, new ObjectManager.OnObjectManagerListener<List<User>>() {
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
        ObjectManager.getInstance().setOnListenerRooms(RecentFragment.class, new ObjectManager.OnObjectManagerListener<List<Room>>() {
            @Override
            public void onSuccess(List<Room> rooms) {
                RUserMe me = ObjectManager.getInstance().getUserMe();
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
                }

                CenterActivity.connectXMPP(me);
            }

            @Override
            public void onError(String error) {
                LogHtk.e(LogHtk.RecentFragment, "error = " + error);
                APIManager.showToastWithCode(error, OpeningRoomActivity.this);
                processUIWhenError(error);
            }
        });
    }

    private void processUIWhenError(final String error) {
        LogHtk.e(LogHtk.RecentFragment, "error = " + error);
        APIManager.showToastWithCode(error, OpeningRoomActivity.this);
        if (!AndroidHelper.isInternetAvailable(getApplicationContext())) {
            backgroundTry.setVisibility(View.VISIBLE);
            prb_Loading.setVisibility(View.GONE);
            btnTry.setVisibility(View.VISIBLE);
            backgroundViews.setVisibility(View.GONE);
            prb_LoadingFisrt.setVisibility(View.GONE);
        }
    }
}
