package antbuddy.htk.com.antbuddy2016.modules.login.activities;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import antbuddy.htk.com.antbuddy2016.R;

/**
 * Created by thanhnguyen on 28/03/2016.
 */
public class ItemFragment extends Fragment {

    private static final String EXTRA_MESSAGE = "mes";

    public static final ItemFragment newInstance(int walkThroughNumber){
        ItemFragment f = new ItemFragment();
        Bundle bdl = new Bundle(1);
        bdl.putInt(EXTRA_MESSAGE, walkThroughNumber);
        f.setArguments(bdl);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int value = getArguments().getInt(EXTRA_MESSAGE);
        View v = inflater.inflate(R.layout.item, container, false);

        ImageView img = (ImageView)v.findViewById(R.id.img);
        switch (value) {
            case 1:
                Glide.with(getContext())
                        .load(R.drawable.walkthrough_1).override(60, 60)
                        .into(img);
                break;
            case 2:
                Glide.with(getContext())
                        .load(R.drawable.walkthrough_2).override(60, 60)
                        .into(img);
                break;
            case 3:
                Glide.with(getContext())
                        .load(R.drawable.walkthrough_3).override(60, 60)
                        .into(img);
                break;
            case 4:
                Glide.with(getContext())
                        .load(R.drawable.walkthrough_4).override(60, 60)
                        .into(img);
                break;

            default:
                break;
        }
        return v;
    }
}
