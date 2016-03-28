package antbuddy.htk.com.antbuddy2016.activities.login;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import antbuddy.htk.com.antbuddy2016.activities.R;

/**
 * Created by thanhnguyen on 28/03/2016.
 */
public class ItemFragment extends Fragment {

    private static final String EXTRA_MESSAGE = "mes";

    public static final ItemFragment newInstance(String message){
        ItemFragment f = new ItemFragment();
        Bundle bdl = new Bundle(1);
        bdl.putString(EXTRA_MESSAGE, message);
        f.setArguments(bdl);
        return f;
    }

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        String message = getArguments().getString(EXTRA_MESSAGE);
        View v = inflater.inflate(R.layout.item, container, false);
        TextView messageTextView = (TextView)v.findViewById(R.id.textView);
        messageTextView.setText(message);
        return v;
    }
}
