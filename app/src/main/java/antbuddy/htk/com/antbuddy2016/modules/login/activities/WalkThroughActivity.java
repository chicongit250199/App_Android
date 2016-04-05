package antbuddy.htk.com.antbuddy2016.modules.login.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;

import antbuddy.htk.com.antbuddy2016.R;

import antbuddy.htk.com.antbuddy2016.modules.login.adapter.WalkThroughAdapder;
import antbuddy.htk.com.antbuddy2016.util.Constants;
import antbuddy.htk.com.antbuddy2016.util.LogHtk;

/**
 * Created by thanhnguyen on 28/03/2016.
 */
public class WalkThroughActivity extends AppCompatActivity {

    private ViewPager paper;
    private List<Fragment> listData;

    private RadioButton radioButton;
    private RadioButton radioButton2;
    private RadioButton radioButton3;
    private RadioButton radioButton4;
    private RadioGroup radioGroup;

    // Buttons
    private Button signUpFree_Button;
    private Button login_Button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_walkthrough);

        initViews();

        listData = new ArrayList<>();
        listData = new ArrayList<>();

        listData.add(new WalkThroughFragment1());
        listData.add(new WalkThroughFragment2());
        listData.add(new WalkThroughFragment3());
        listData.add(new WalkThroughFragment4());

        WalkThroughAdapder adapder = new WalkThroughAdapder(getSupportFragmentManager(), listData);
        paper = (ViewPager) findViewById(R.id.viewpager);


        radioGroup = (RadioGroup)findViewById(R.id.radiogroup);
        radioButton = (RadioButton) findViewById(R.id.radioButton);
        radioButton2 = (RadioButton) findViewById(R.id.radioButton2);
        radioButton3 = (RadioButton) findViewById(R.id.radioButton3);
        radioButton4 = (RadioButton) findViewById(R.id.radioButton4);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == radioButton.getId()) {
                    LogHtk.d(LogHtk.Test2, "-->Xuc 1");
                }

                if (checkedId == radioButton2.getId()) {
                    LogHtk.d(LogHtk.Test2, "-->Xuc 2");
                }

                if (checkedId == radioButton3.getId()) {
                    LogHtk.d(LogHtk.Test2, "-->Xuc 3");
                }

                if (checkedId == radioButton4.getId()) {
                    LogHtk.d(LogHtk.Test2, "-->Xuc 4");
                }
            }
        });


        paper.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                LogHtk.i(LogHtk.Test2, "----?onPageSelected + " + position);
                switch (position) {
                    case 0:
                        radioButton.setChecked(true);
                        break;
                    case 1:
                        radioButton2.setChecked(true);
                        break;
                    case 2:
                        radioButton3.setChecked(true);
                        break;
                    case 3:
                        radioButton4.setChecked(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                LogHtk.i(LogHtk.Test2, "onPageScrollStateChanged");
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
