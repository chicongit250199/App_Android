package antbuddy.htk.com.antbuddy2016.activities.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import antbuddy.htk.com.antbuddy2016.activities.R;

import antbuddy.htk.com.antbuddy2016.util.Constants;

/**
 * Created by thanhnguyen on 28/03/2016.
 */
public class WalkThroughActivity extends AppCompatActivity {

    private ViewPager paper;
    private List<Fragment> listData;

    // Buttons
    private Button signUpFree_Button;
    private Button login_Button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.welcome_activity);

        initViews();

        listData = new ArrayList<>();
        listData = new ArrayList<>();
        listData.add(new ItemFragment().newInstance("11111111"));
        listData.add(new ItemFragment().newInstance("22222222"));
        listData.add(new ItemFragment().newInstance("33333333"));
        listData.add(new ItemFragment().newInstance("44444444"));

        ViewPagerAdapder adapder = new ViewPagerAdapder(getSupportFragmentManager(), listData);
        paper = (ViewPager) findViewById(R.id.viewpager);

        final RadioGroup radioGroup = (RadioGroup)findViewById(R.id.radiogroup);
        paper.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        radioGroup.check(R.id.radioButton);
                        break;
                    case 1:
                        radioGroup.check(R.id.radioButton2);
                        break;
                    case 2:
                        radioGroup.check(R.id.radioButton3);
                        break;
                    case 3:
                        radioGroup.check(R.id.radioButton4);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        paper.setAdapter(adapder);

        // Button listner
        signUpFree_Button.setOnClickListener(welcomeListener);
        login_Button.setOnClickListener(welcomeListener);
    }

    void initViews() {
        signUpFree_Button = (Button) findViewById(R.id.signUpFree_Button);
        login_Button = (Button) findViewById(R.id.login_Button);
    }

    View.OnClickListener welcomeListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent myIntent = new Intent(WalkThroughActivity.this, LoReActivity.class);
            switch (v.getId()) {
                case R.id.signUpFree_Button:
                    myIntent.putExtra(Constants.LOGIN_TYPE, 1);  // sign up free
                    startActivity(myIntent);
                    finish();
                    break;

                case R.id.login_Button:
                    myIntent.putExtra(Constants.LOGIN_TYPE, 2);  // login
                    startActivity(myIntent);
                    finish();
                    break;

                default:
                    break;
            }
        }
    };
}
