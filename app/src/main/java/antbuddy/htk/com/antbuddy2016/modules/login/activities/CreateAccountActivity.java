package antbuddy.htk.com.antbuddy2016.modules.login.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import antbuddy.htk.com.antbuddy2016.R;
import antbuddy.htk.com.antbuddy2016.util.AndroidHelper;

/**
 * Created by thanhnguyen on 28/03/2016.
 */
public class CreateAccountActivity extends Activity {

    private ProgressBar progressBar_CreateAccount;
    private Button accept_CreateAccount_Button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_createaccount);
        initViews();

        AndroidHelper.warningInternetConnection(this);
    }

    private void initViews() {
        progressBar_CreateAccount = (ProgressBar) findViewById(R.id.progressBar_CreateAccount);
        accept_CreateAccount_Button = (Button) findViewById(R.id.accept_CreateAccount_Button);
        accept_CreateAccount_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AndroidHelper.showToast("This feature will be available soon!", CreateAccountActivity.this);
                requestAPI();
            }
        });
    }

    private void requestAPI() {
        if (!AndroidHelper.isInternetAvailable(getApplicationContext())) {
            AndroidHelper.showToast("No network connection available!", CreateAccountActivity.this);
            return;
        }
    }
}
