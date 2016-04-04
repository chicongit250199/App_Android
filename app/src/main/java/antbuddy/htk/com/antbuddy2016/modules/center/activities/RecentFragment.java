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
import antbuddy.htk.com.antbuddy2016.model.UserMe;
import antbuddy.htk.com.antbuddy2016.modules.chat.ChatActivity;
import antbuddy.htk.com.antbuddy2016.util.AndroidHelper;

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

//        ObjectManager.getInstance().getUserMe(new ObjectManager.OnListenerUserMe() {
//            @Override
//            public void onResponse(UserMe userMe) {
//                if (userMe != null && userMe.getOpeningChatrooms() != null) {
//                    for (UserMe.OpeningChatroom openingChatroom : userMe.getOpeningChatrooms()) {
//                        if (openingChatroom.getIsMuc()) {
//                            recentsData.get(0).add(openingChatroom);
//                        } else {
//                            recentsData.get(1).add(openingChatroom);
//                        }
//                    }
//                }
//            }
//        });

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

    protected void updateUI() {

        ObjectManager.getInstance().getUserMe(new ObjectManager.OnListenerUserMe() {
            @Override
            public void onResponse(UserMe userMe) {
                if (userMe.getOpeningChatrooms() != null) {
                    recentsData.get(ChatType.Group.getValue()).addAll(UserMe.getChatsOpening(userMe, true));
                    recentsData.get(ChatType.OneToOne.getValue()).addAll(UserMe.getChatsOpening(userMe, false));
                    listRecentsAdapter.notifyDataSetChanged();
                }
            }
        });
    }
}
