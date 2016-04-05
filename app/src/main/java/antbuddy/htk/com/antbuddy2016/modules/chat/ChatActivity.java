package antbuddy.htk.com.antbuddy2016.modules.chat;

import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import antbuddy.htk.com.antbuddy2016.R;
import antbuddy.htk.com.antbuddy2016.adapters.ChatAdapter;
import antbuddy.htk.com.antbuddy2016.model.ChatMessage;
import antbuddy.htk.com.antbuddy2016.service.AntbuddyApplication;
import antbuddy.htk.com.antbuddy2016.service.AntbuddyService;
import antbuddy.htk.com.antbuddy2016.util.AndroidHelper;
import antbuddy.htk.com.antbuddy2016.util.BroadcastConstant;
import antbuddy.htk.com.antbuddy2016.util.Constants;
import antbuddy.htk.com.antbuddy2016.util.LogHtk;
import antbuddy.htk.com.antbuddy2016.util.NationalTime;
import github.ankushsachdeva.emojicon.EmojiconEditText;

/**
 * Created by Micky on 4/1/2016.
 */
public class ChatActivity extends Activity implements View.OnClickListener {
    public final static String key_key = "key";
    public final static String key_type = "type";
    public final static String key_title = "title";

    private String key = "a667b2a0-d636-11e5-9bc1-25a4e1ac232c"; //key group hoac key cua user != jid (cua xmpp)
    private String before = NationalTime.getLocalTimeToUTCTime(); //last time : set bang time hien tai neu null
    private boolean type = true; //true: la room ; false: chat 1-1
    private String title;

    private Boolean isFister = true;

    private ListView lv_messages;
    private ChatAdapter mChatAdapter;
    private SwipyRefreshLayout mSwipyRefreshLayout;
    private TextView tv_title;

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
    private EmojiconEditText text_send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            key = bundle.getString(key_key);
            type = bundle.getBoolean(key_type);
            title = bundle.getString(key_title);
        }
        setContentView(R.layout.activity_chat);
        tv_title = (TextView) findViewById(R.id.tv_title);
        text_send = (EmojiconEditText) findViewById(R.id.text_send);
        initView();
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
    
    private void initView() {
        setTitle(title);
        lv_messages = (ListView) findViewById(R.id.lv_messages);
        mSwipyRefreshLayout = (SwipyRefreshLayout) findViewById(R.id.swipyrefreshlayout);
        // chat addapter and list event
        if (mChatAdapter == null) {
            mChatAdapter = new ChatAdapter(this, lv_messages);
        }

        lv_messages.setAdapter(mChatAdapter);
        lv_messages.setDividerHeight(0);
        lv_messages.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                Log.i("Hoa debug","ChatActivity:onScrollStateChanged: 1 = "+1);
                if (lv_messages.getFirstVisiblePosition() == 0 && scrollState == SCROLL_STATE_IDLE) {
                    Log.i("Hoa debug", "ChatActivity:onScrollStateChanged: 2 = " + 2);
//                    loadMoreMessages();
                    if (lv_messages.getChildAt(0) != null && lv_messages.getChildAt(0).getTop() == 0) {
                        Log.i("Hoa debug","ChatActivity:onScrollStateChanged: 3 = "+3);
                        loadMoreMessages();
                        //loadMoreMessages();
//                        mSwipyRefreshLayout.setRefreshing(true);
//                        loadMoreMessages();
                    }
                } else {
                    mSwipyRefreshLayout.setRefreshing(false);
                    Log.i("Hoa debug","ChatActivity:onScrollStateChanged: 4 = "+4);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
        lv_messages.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_DISABLED);

        lv_messages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
//
//                if (imm.isAcceptingText()) {
////                    AntbuddyUtil.hideSoftKeyboard(getActivity());
//                } else {
//                }
            }
        });
        loadMoreMessages();
        AndroidHelper.hideSoftKeyboard(this);
    }
    
    //goi len webservice lay tin nhan cua room / uses
    public void loadMoreMessages(){
        String sFormatUrl ="https://%s.antbuddy.com/api/messages?before=%s&chatRoom=%s&limit=50&type=%s";
        final String API_MESSAGES_URL = String.format(sFormatUrl, Constants.domain, before, key, (type ? "groupchat" : "chat"));
        Log.i("Hoa debug","ChatActivity:loadMoreMessages: API_MESSAGES_URL = "+API_MESSAGES_URL);
        mSwipyRefreshLayout.setRefreshing(true);
        JsonArrayRequest req = new JsonArrayRequest(API_MESSAGES_URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i("Hoa debug", "ChatActivity:onResponse: thanh cong = "+API_MESSAGES_URL);
                        mSwipyRefreshLayout.setRefreshing(false);
                        HashMap<String,String> infoLoad = new HashMap<>();
                        ArrayList<ChatMessage> messages = ChatMessage.parseArray(response, infoLoad);
                        mChatAdapter.addMessages(messages,isFister);
                        isFister = false;
                        before = infoLoad.get(ChatMessage.info_lastTime);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("Hoa debug", "ChatActivity:onResponse: error = "+API_MESSAGES_URL);
                        mSwipyRefreshLayout.setRefreshing(false);
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("authorization", Constants.token);
                return params;
            }
        };
        AntbuddyApplication.getInstance().addToRequestQueue(req);
    }

    /**
     * chatReceiver broadcast. Receive chat from xmpp
     */
    private final BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("Hoa debug","ChatActivity:onReceive:  = messageReceiver");
            try {
                ChatMessage chatMessage = intent.getParcelableExtra(BroadcastConstant.MESSAGE_RECEIVE);
                Log.i("Hoa debug","ChatActivity:onReceive:  = chatMessage "+chatMessage.getBody());
                if (key.equals(chatMessage.getFromKey())) {
                    mChatAdapter.addMessage(chatMessage, isFister);
                    isFister = false;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public void onClick(View v) {
        String text_body = text_send.getText().toString();
        if (!TextUtils.isEmpty(text_body)) {
            ChatMessage chatMessage = new ChatMessage(key, text_body, type);
            mIRemoteService.sendMessageOut(chatMessage);
            text_send.setText("");
        }
    }
}
