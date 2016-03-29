package antbuddy.htk.com.antbuddy2016.module.login.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import antbuddy.htk.com.antbuddy2016.R;
import antbuddy.htk.com.antbuddy2016.api.HttpRequestReceiver;
import antbuddy.htk.com.antbuddy2016.api.Request;
import antbuddy.htk.com.antbuddy2016.util.AndroidHelper;

/**
 * Created by thanhnguyen on 28/03/2016.
 */
public class LoginActivity extends Activity {

    private Button accept_login_Button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        accept_login_Button = (Button) findViewById(R.id.accept_login_Button);
        accept_login_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("DaiThanh", "Click here!");

                AndroidHelper.hideSoftKeyboard(LoginActivity.this);

                //antbuddytesting1@gmail.com/111qqq111
                Thread thread = new Thread(new Runnable(){
                    @Override
                    public void run() {
                        try {
                            // Request
                            Request.login("antbuddytesting1@gmail.com", "111qqq111", new HttpRequestReceiver() {
                                @Override
                                public void onSuccess(String result) {
                                    Log.d("DaiThanh", "Thanh Cong: " + result);
                                }

                                @Override
                                public void onError(String error) {
                                    Log.d("DaiThanh", "That bai: " + error);
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                thread.start();


//                Intent myIntent = new Intent(LoginActivity.this, DomainActivity.class);
//                startActivity(myIntent);
            }
        });
    }

//    /**
//     *
//     */
//    private void bindService() {
//        AntbuddyApplication context = AntbuddyApplication.getInstance();
//        if (context != null && context.getService() == null) {
//            Intent it = new Intent();
//            it.setAction("com.htk.antbuddy.services.AntbuddyService.BIND");
//            // binding to remote service
//            context.bindService(it, context.getServiceConnection(), Service.BIND_AUTO_CREATE);
//        } else if (Request.getCookie() != null) { // Case: if user has already logined, you need loading data throw cookie
//
//            Thread login = new Thread(new Runnable() {
//
//                @Override
//                public void run() {
//                    try {
//                        String result = AntbuddyApplication.getInstance().getService().login(Request.getCookie());
//                        if (result.equals(AntbuddyXmppConnection.SERVICE_ALREADY_START)) {
//                            moveToMainActivity();
//                        }
//                    } catch (RemoteException e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
//            login.start();
//        }
//    }

}
