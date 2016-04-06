package antbuddy.htk.com.antbuddy2016.modules.center.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.List;

import antbuddy.htk.com.antbuddy2016.R;
import antbuddy.htk.com.antbuddy2016.adapters.ListRecentsAdapter;
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
    private ListRecentsAdapter listRecentsAdapter;

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

        listRecentsAdapter = new ListRecentsAdapter(getContext(),groupNames, recentsData);
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
                Bundle args = new Bundle();
                if (groupPosition == 0) {
                    OpeningChatRoom openingChatRoom = (OpeningChatRoom) listRecentsAdapter.getChild(groupPosition, childPosition);
                    Room room = ObjectManager.getInstance().findRoom(openingChatRoom.getChatRoomKey());
                    args.putString(ChatActivity.key_key, room.getKey());
                    args.putBoolean(ChatActivity.key_type, true);
                    args.putString(ChatActivity.key_title, room.getName());
                } else {
                    OpeningChatRoom openingChatRoom = (OpeningChatRoom) listRecentsAdapter.getChild(groupPosition, childPosition);
                    User user = ObjectManager.getInstance().findUsers(openingChatRoom.getChatRoomKey());
                    args.putString(ChatActivity.key_key, user.getKey());
                    args.putBoolean(ChatActivity.key_type, false);
                    args.putString(ChatActivity.key_title, user.getName());
                }
                AndroidHelper.gotoActivity(getActivity(), ChatActivity.class, args);
                return true;
            }
        });

        updateUI();
        return rootView;
    }

    @Override
    public void onResume() {
        LogHtk.d(LogHtk.Test1, "onResume");
        super.onResume();
    }

    protected void updateUI() {
        ObjectManager.getInstance().getUserMe(new ObjectManager.OnObjectManagerListener<UserMe>() {
            @Override
            public void onSuccess(UserMe me) {
                if (me.getOpeningChatrooms() != null) {
                    recentsData.get(ChatType.Group.getValue()).addAll(UserMe.getChatsOpening(me, true));
                    recentsData.get(ChatType.OneToOne.getValue()).addAll(UserMe.getChatsOpening(me, false));
                    listRecentsAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(String error) {
                LogHtk.d(LogHtk.Test1, "error = " + error);
            }
        });
    }
}
