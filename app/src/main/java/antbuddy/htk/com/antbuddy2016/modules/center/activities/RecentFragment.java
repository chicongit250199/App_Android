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


    private RelativeLayout backgroundTry;
    private LinearLayout backgroundViews;
    private ProgressBar prb_Loading;
    private Button btnTry;
    private ProgressBar prb_LoadingFisrt;
    
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recent, container, false);

        initViews(rootView);
        viewsListener();
        updateUI();
        return rootView;
    }

    private void initViews(View root) {
        backgroundTry     = (RelativeLayout) root.findViewById(R.id.backgroundTry);
        backgroundViews   = (LinearLayout) root.findViewById(R.id.backgroundViews);
        btnTry            = (Button) root.findViewById(R.id.btnTry);
        prb_Loading       = (ProgressBar) root.findViewById(R.id.prb_Loading);
        prb_LoadingFisrt  = (ProgressBar) root.findViewById(R.id.prb_LoadingFisrt);
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

    protected void updateUI() {
        LogHtk.i(LogHtk.RecentFragment, "updateUI");
        prb_LoadingFisrt.setVisibility(View.VISIBLE);
        // LOGIN XMPP
        //UserMe
        ObjectManager.getInstance().getUserMe(new ObjectManager.OnObjectManagerListener<UserMe>() {
            @Override
            public void onSuccess(final UserMe me) {
                LogHtk.i(LogHtk.Test1, "Userme = " + me.toString());
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
                LogHtk.e(LogHtk.Test1, "rooms = " + users.toString());
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
                LogHtk.e(LogHtk.Test1, "rooms = " + rooms.toString());

                UserMe me = ObjectManager.getInstance().getUserMe();
                CenterActivity.connectXMPP(me);
                backgroundTry.setVisibility(View.GONE);
                prb_Loading.setVisibility(View.GONE);
                btnTry.setVisibility(View.VISIBLE);
                backgroundViews.setVisibility(View.VISIBLE);
                prb_LoadingFisrt.setVisibility(View.GONE);
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
