package antbuddy.htk.com.antbuddy2016.modules.center.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SearchView;

import java.util.List;

import antbuddy.htk.com.antbuddy2016.R;
import antbuddy.htk.com.antbuddy2016.RealmObjects.RObjectManager;
import antbuddy.htk.com.antbuddy2016.RealmObjects.RUser;
import antbuddy.htk.com.antbuddy2016.adapters.UserAdapter;
import antbuddy.htk.com.antbuddy2016.api.APIManager;
import antbuddy.htk.com.antbuddy2016.interfaces.HttpRequestReceiver;
import antbuddy.htk.com.antbuddy2016.model.User;
import antbuddy.htk.com.antbuddy2016.modules.chat.ChatActivity;
import antbuddy.htk.com.antbuddy2016.util.AndroidHelper;
import antbuddy.htk.com.antbuddy2016.util.LogHtk;

/**
 * Created by thanhnguyen on 30/03/2016.
 */
public class MembersFragment extends Fragment {

    private ListView lv_member;
    private UserAdapter mUserAdapter;

    private RelativeLayout backgroundTry;
    private LinearLayout backgroundViews;
    private ProgressBar prb_Loading;
    private Button btnTry;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_members, container, false);
        lv_member = (ListView)rootView.findViewById(R.id.lv_member);

        if (mUserAdapter == null) {
            mUserAdapter = new UserAdapter(getContext(), lv_member, RObjectManager.getUsers());
        }
        lv_member.setAdapter(mUserAdapter);
        lv_member.setDividerHeight(0);

        lv_member.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RUser user = mUserAdapter.getItem(position);
                Bundle args = new Bundle();
                args.putString(ChatActivity.kKeyRoom, user.getKey());
                args.putBoolean(ChatActivity.key_type, false);
                args.putString(ChatActivity.key_title, user.getName());
                AndroidHelper.gotoActivity(getActivity(), ChatActivity.class, args);
            }
        });

        initViews(rootView);
        viewsListener();

        loading_Users();

        return rootView;
    }

    private void initViews(View root) {
        backgroundTry = (RelativeLayout) root.findViewById(R.id.backgroundTry);
        backgroundViews = (LinearLayout) root.findViewById(R.id.backgroundViews);
        btnTry = (Button) root.findViewById(R.id.btnTry);
        prb_Loading = (ProgressBar) root.findViewById(R.id.prb_Loading);
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
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    protected void loading_Users() {
        if (AndroidHelper.isInternetAvailable(getActivity().getApplicationContext())) {
            APIManager.GETUsers(new HttpRequestReceiver<List<User>>() {
                @Override
                public void onSuccess(List<User> users) {
//                    mUserAdapter.filter("", users);
                    RObjectManager.saveUsersOrUpdate(users);
                    updateUI();
                }

                @Override
                public void onError(String error) {
                    APIManager.showToastWithCode(error, getActivity());
                    processUIWhenNoConnection();
                }
            });
        } else if (RObjectManager.isUserMeExist() && RObjectManager.isUsersExist() && RObjectManager.isRoomsExist()) {
            updateUI();
        } else {    // No connection and No data in DB
            processUIWhenNoConnection();
        }
    }

    private void updateUI(){
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
