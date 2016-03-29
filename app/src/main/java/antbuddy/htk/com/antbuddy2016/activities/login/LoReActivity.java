package antbuddy.htk.com.antbuddy2016.activities.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import antbuddy.htk.com.antbuddy2016.activities.R;
import antbuddy.htk.com.antbuddy2016.util.Constants;

/**
 * Created by thanhnguyen on 29/03/2016.
 */
public class LoReActivity extends Activity {

    // Buttons
    private Button login_LoRe_Button;
    private Button createNewAccount_LoRe_Button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_loreactivity);

        initViews();

        getBunldeAndProcess();

        // Button listner
        login_LoRe_Button.setOnClickListener(welcomeListener);
        createNewAccount_LoRe_Button.setOnClickListener(welcomeListener);
    }

    void initViews() {
        login_LoRe_Button = (Button) findViewById(R.id.login_LoRe_Button);
        createNewAccount_LoRe_Button = (Button) findViewById(R.id.createNewAccount_LoRe_Button);
    }

    void getBunldeAndProcess() {
        // Get bundle from WalkThroughActivity
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int typeValue = extras.getInt(Constants.LOGIN_TYPE);
            Log.d("DaiThanh", "Gia tri =" + typeValue);

            if (typeValue == 1) {
                Intent myIntent = new Intent(LoReActivity.this, CreateAccountActivity.class);
                startActivity(myIntent);
            } else if (typeValue == 2) {
                Intent myIntent = new Intent(LoReActivity.this, LoginActivity.class);
                startActivity(myIntent);
            }
        }
    }

    View.OnClickListener welcomeListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.createNewAccount_LoRe_Button:
                    Intent signUpIntent = new Intent(LoReActivity.this, CreateAccountActivity.class);
                    startActivity(signUpIntent);
                    break;

                case R.id.login_LoRe_Button:
                    Intent loginIntent = new Intent(LoReActivity.this, LoginActivity.class);
                    startActivity(loginIntent);
                    break;

                default:
                    break;
            }
        }
    };
}
