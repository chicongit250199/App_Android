package antbuddy.htk.com.antbuddy2016.modules.center.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import antbuddy.htk.com.antbuddy2016.R;
import antbuddy.htk.com.antbuddy2016.model.ObjectManager;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by thanhnguyen on 30/03/2016.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener {
    
    private ImageView imgAvatar;
    private TextView tv_user_name;
    private LinearLayout ll_user;
    
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        imgAvatar = (ImageView) rootView.findViewById(R.id.imgAvatar);
        Glide.with(getContext())
                .load(ObjectManager.getInstance().getUserMe().getAvatar())
                .override(100, 100)
                .bitmapTransform(new CropCircleTransformation(getContext()))
                .placeholder(R.drawable.ic_avatar_defaul)
                .error(R.drawable.ic_avatar_defaul)
                .into(imgAvatar);
        tv_user_name = (TextView) rootView.findViewById(R.id.tv_user_name);
        tv_user_name.setText(ObjectManager.getInstance().getUserMe().getUsername());
        ll_user = (LinearLayout) rootView.findViewById(R.id.ll_user);
        ll_user.setOnClickListener(this);
        ll_user = (LinearLayout) rootView.findViewById(R.id.ll_swap);
        ll_user.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onClick(View v) {
        if(v == ll_user) {

        }
    }
}
