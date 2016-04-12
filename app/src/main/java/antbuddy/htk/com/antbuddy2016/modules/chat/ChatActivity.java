package antbuddy.htk.com.antbuddy2016.modules.chat;

import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.bumptech.glide.Glide;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import antbuddy.htk.com.antbuddy2016.R;
import antbuddy.htk.com.antbuddy2016.adapters.ChatAdapter;
import antbuddy.htk.com.antbuddy2016.api.APIManager;
import antbuddy.htk.com.antbuddy2016.interfaces.HttpRequestReceiver;
import antbuddy.htk.com.antbuddy2016.model.ChatMessage;
import antbuddy.htk.com.antbuddy2016.model.ObjectManager;
import antbuddy.htk.com.antbuddy2016.model.UserMe;
import antbuddy.htk.com.antbuddy2016.service.AntbuddyApplication;
import antbuddy.htk.com.antbuddy2016.service.AntbuddyService;
import antbuddy.htk.com.antbuddy2016.util.AndroidHelper;
import antbuddy.htk.com.antbuddy2016.util.BroadcastConstant;
import antbuddy.htk.com.antbuddy2016.util.Constants;
import antbuddy.htk.com.antbuddy2016.util.LogHtk;
import antbuddy.htk.com.antbuddy2016.util.NationalTime;
import github.ankushsachdeva.emojicon.EmojiconEditText;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by Micky on 4/1/2016.
 */
public class ChatActivity extends Activity implements View.OnClickListener {
    public final static String key_key = "key";
    public final static String key_type = "type";
    public final static String key_title = "title";

    private String keyReceiver = ""; //ex: a667b2a0-d636-11e5-9bc1-25a4e1ac232c, key group hoac key cua user != jid (cua xmpp)
    private String before = NationalTime.getLocalTimeToUTCTime(); //last time : set bang time hien tai neu null
    private boolean isRoom = true; //true: la room ; false: chat 1-1
    private String title;

    private Boolean isFister = true;

    private ListView            lv_messages;
    private ChatAdapter         mChatAdapter;
    private SwipyRefreshLayout  mSwipyRefreshLayout;
    private TextView            tv_title;
    private RelativeLayout      areaBack;
    private EmojiconEditText    etTypingMessage;
    private ImageView           imgSendMessage;



    public static AntbuddyService mIRemoteService = AntbuddyService.mAntbuddyService;
    private boolean mBound;
    private final ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            AntbuddyService.LocalBinder binder = (AntbuddyService.LocalBinder) service;
            mIRemoteService = binder.getService();
            mBound = true;
            LogHtk.d(LogHtk.SERVICE_TAG, "CenterActivity/onServiceConnected");
        }

        public void onServiceDisconnected(ComponentName className) {
            mIRemoteService = null;
            mBound = false;
            LogHtk.e(LogHtk.SERVICE_TAG, "CenterActivity/onServiceDisconnected");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            keyReceiver = bundle.getString(key_key);
            isRoom = bundle.getBoolean(key_type);
            title = bundle.getString(key_title);
        }
        setContentView(R.layout.activity_chat);

        initViews();
        viewsListener();
        updateUI();

        loadMoreMessages();

        registerReceiver(messageReceiver, new IntentFilter(BroadcastConstant.BROAD_CAST_RECEIVER_CHAT));

        boolean isConnectService = connectServiceInAndroid();
        if (!isConnectService) {
            LogHtk.i(LogHtk.SERVICE_TAG, "CenterActivity can not get service object in android!");
        }
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(messageReceiver);
        super.onDestroy();
    }

    private boolean connectServiceInAndroid() {
        if(AntbuddyService.mAntbuddyService == null) {
            Intent intent = new Intent(this, AntbuddyService.class);
            bindService(intent, mConnection, Service.BIND_AUTO_CREATE);
        } else {
            mIRemoteService = AntbuddyService.mAntbuddyService;
        }
        return mIRemoteService != null;
    }

    @Override
    public void setTitle(CharSequence title) {
        tv_title.setText(title);
    }

    private void initViews() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        setTitle(title);
        etTypingMessage = (EmojiconEditText) findViewById(R.id.text_send);
        lv_messages = (ListView) findViewById(R.id.lv_messages);
        mSwipyRefreshLayout = (SwipyRefreshLayout) findViewById(R.id.swipyrefreshlayout);
        if (mChatAdapter == null) {
            mChatAdapter = new ChatAdapter(this, lv_messages);
        }
        lv_messages.setAdapter(mChatAdapter);
        lv_messages.setDividerHeight(0);
        lv_messages.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_DISABLED);

        mSwipyRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                loadMoreMessages();
            }
        });

        etTypingMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        LogHtk.i(LogHtk.ChatActivity, "");
                        lv_messages.setSelection(lv_messages.getCount() - 1);

                        //
                    }
                }, 300);
            }
        });

        etTypingMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                LogHtk.i(LogHtk.Test1, "beforeTextChanged");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                LogHtk.i(LogHtk.Test1, "onTextChanged");
            }

            @Override
            public void afterTextChanged(Editable s) {
                LogHtk.i(LogHtk.Test1, "afterTextChanged");
            }
        });

        lv_messages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        areaBack = (RelativeLayout) findViewById(R.id.areaBack);
        areaBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        imgSendMessage = (ImageView) findViewById(R.id.imgSendMessage);
    }

    private void viewsListener() {
        imgSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!AndroidHelper.warningInternetConnection(ChatActivity.this)) {
                    return;
                }

                // Send message
                String text_body = etTypingMessage.getText().toString().trim();
                LogHtk.i(LogHtk.ChatActivity, "text_body =" + text_body);
                if (!TextUtils.isEmpty(text_body)) {
                    LogHtk.i(LogHtk.ChatActivity, "key =" + keyReceiver);
                    LogHtk.i(LogHtk.ChatActivity, "isRoom =" + isRoom);
                    ChatMessage chatMessage = new ChatMessage(keyReceiver, text_body, isRoom);
                    chatMessage.showLog();
                    mIRemoteService.sendMessageOut(chatMessage);
                    etTypingMessage.setText("");
                }
            }
        });
    }

    private void updateUI() {
//        UserMe me = ObjectManager.getInstance().getUserMe();
//        Glide.with(getApplicationContext())
//                .load(me.getAvatar())
//                .override(50, 50)
//                .placeholder(R.drawable.ic_avatar_defaul)
//                .error(R.drawable.ic_avatar_defaul)
//                .bitmapTransform(new CropCircleTransformation(getApplicationContext()))
//                .into(imgSendMessage);
    }
    
    //goi len webservice lay tin nhan cua room / uses
    public void loadMoreMessages(){
        APIManager.GETMessages(before, keyReceiver, (isRoom ? "groupchat" : "chat"), new HttpRequestReceiver<List<ChatMessage>>() {
            @Override
            public void onSuccess(List<ChatMessage> listMessages) {
                Collections.reverse(listMessages);
                mSwipyRefreshLayout.setRefreshing(false);
                mChatAdapter.addMessages(listMessages, isFister);
                isFister = false;
                if (listMessages.size() > 0) {
                    before = listMessages.get(0).getDatetime();
                }
            }

            @Override
            public void onError(String error) {
                LogHtk.i(LogHtk.ChatActivity, "-->Error");
                mSwipyRefreshLayout.setRefreshing(false);
            }
        });
    }

    /**
     * chatReceiver broadcast. Receive chat from xmpp
     */
    private final BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                ChatMessage chatMessage = intent.getParcelableExtra(BroadcastConstant.MESSAGE_RECEIVE);
                if (keyReceiver.equals(chatMessage.getFromKey())) {
                    mChatAdapter.addMessage(chatMessage, true);
                    isFister = false;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public void onClick(View v) {
//        if(!AndroidHelper.warningInternetConnection(ChatActivity.this)) {
//            return;
//        }
//
//        // Send message
//        String text_body = etTypingMessage.getText().toString().trim();
//        LogHtk.i(LogHtk.ChatActivity, "text_body =" + text_body);
//        if (!TextUtils.isEmpty(text_body)) {
//            LogHtk.i(LogHtk.ChatActivity, "key =" + keyReceiver);
//            LogHtk.i(LogHtk.ChatActivity, "isRoom =" + isRoom);
//            ChatMessage chatMessage = new ChatMessage(keyReceiver, text_body, isRoom);
//            chatMessage.showLog();
//            mIRemoteService.sendMessageOut(chatMessage);
//            etTypingMessage.setText("");
//        }
    }
}
