package antbuddy.htk.com.antbuddy2016.modules.center.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import antbuddy.htk.com.antbuddy2016.R;
import antbuddy.htk.com.antbuddy2016.adapters.UserAdapter;

/**
 * Created by thanhnguyen on 30/03/2016.
 */
public class MembersFragment extends Fragment {

    private ListView lv_member;
    private UserAdapter mUserAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_members, container, false);
        lv_member = (ListView)rootView.findViewById(R.id.lv_member);
        if (mUserAdapter == null) {
            mUserAdapter = new UserAdapter(getContext(), lv_member);
        }
        lv_member.setAdapter(mUserAdapter);
        lv_member.setDividerHeight(0);
        return rootView;
    }
}
