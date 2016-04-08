package antbuddy.htk.com.antbuddy2016.modules.center.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import antbuddy.htk.com.antbuddy2016.R;
import antbuddy.htk.com.antbuddy2016.api.APIManager;
import antbuddy.htk.com.antbuddy2016.model.ObjectManager;
import antbuddy.htk.com.antbuddy2016.model.UserMe;
import antbuddy.htk.com.antbuddy2016.util.AndroidHelper;
import antbuddy.htk.com.antbuddy2016.util.LogHtk;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by thanhnguyen on 30/03/2016.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener {
    
    private ImageView imgAvatar;
    private TextView tv_user_name;
    private LinearLayout ll_user;

    private RelativeLayout backgroundTry;
    private LinearLayout backgroundViews;
    private ProgressBar prb_Loading;
    private Button btnTry;
    
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        imgAvatar    = (ImageView) rootView.findViewById(R.id.imgAvatar);
        tv_user_name = (TextView) rootView.findViewById(R.id.tv_user_name);
//        ll_user      = (LinearLayout) rootView.findViewById(R.id.ll_user);
//        ll_user      = (LinearLayout) rootView.findViewById(R.id.ll_swap);
//        ll_user.setOnClickListener(this);

        initViews(rootView);

        updateUI();
        viewsListener();
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

    private void updateUI() {
        ObjectManager.getInstance().getUserMe(new ObjectManager.OnObjectManagerListener<UserMe>() {
            @Override
            public void onSuccess(UserMe me) {
                Glide.with(getContext())
                        .load(me.getAvatar())
                        .override(100, 100)
                        .bitmapTransform(new CropCircleTransformation(getContext()))
                        .placeholder(R.drawable.ic_avatar_defaul)
                        .error(R.drawable.ic_avatar_defaul)
                        .into(imgAvatar);

                tv_user_name.setText(me.getUsername());

                backgroundTry.setVisibility(View.GONE);
                prb_Loading.setVisibility(View.GONE);
                btnTry.setVisibility(View.VISIBLE);
                backgroundViews.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError(String error) {
                APIManager.showToastWithCode(error, getActivity());
                if (!AndroidHelper.isInternetAvailable(getContext())) {
                    backgroundTry.setVisibility(View.VISIBLE);
                    prb_Loading.setVisibility(View.GONE);
                    btnTry.setVisibility(View.VISIBLE);
                    backgroundViews.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v == ll_user) {

        }
    }
}
