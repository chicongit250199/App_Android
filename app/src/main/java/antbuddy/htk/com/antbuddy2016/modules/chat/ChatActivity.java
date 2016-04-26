package antbuddy.htk.com.antbuddy2016.modules.chat;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import antbuddy.htk.com.antbuddy2016.R;
import antbuddy.htk.com.antbuddy2016.RealmObjects.RChatMessage;
import antbuddy.htk.com.antbuddy2016.RealmObjects.RObjectManagerOne;
import antbuddy.htk.com.antbuddy2016.RealmObjects.RRoom;
import antbuddy.htk.com.antbuddy2016.RealmObjects.RUser;
import antbuddy.htk.com.antbuddy2016.RealmObjects.RUserMe;
import antbuddy.htk.com.antbuddy2016.adapters.RChatAdapter;
import antbuddy.htk.com.antbuddy2016.service.AntbuddyService;
import antbuddy.htk.com.antbuddy2016.util.AndroidHelper;
import antbuddy.htk.com.antbuddy2016.util.BroadcastConstant;
import antbuddy.htk.com.antbuddy2016.util.LogHtk;
import github.ankushsachdeva.emojicon.EmojiconEditText;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.Sort;

/**
 * Created by Micky on 4/1/2016.
 */
public class ChatActivity extends Activity implements View.OnClickListener {
    public final static String kKeyRoom = "Key of Room";
    public final static String key_type = "type";
    public final static String key_title = "title";

    // This fields need in a Chat Room
    private String keyRoom = ""; //ex: a667b2a0-d636-11e5-9bc1-25a4e1ac232c, key group hoac key cua user != jid (cua xmpp)
    private boolean isGroup = true; //true: la room ; false: chat 1-1
    private String title;
    private String keyMe = "";

    private Boolean isFister = true;

    private ListView            lv_messages;

    private RObjectManagerOne realmManager;

    private RChatAdapter        chatAdapter;
//    private RealmResults<RChatMessage> chatMessages;


    private SwipyRefreshLayout  mSwipyRefreshLayout;
    private TextView            tv_title;
    private RelativeLayout      areaBack;
    private EmojiconEditText    etTypingMessage;
    private ImageView           imgSendMessage;

    private boolean isloadedMessages;

    private Realm realm;

//    public static AntbuddyService mIRemoteService = AntbuddyService.mAntbuddyService;
//    private boolean mBound;
//    private final ServiceConnection mConnection = new ServiceConnection() {
//        public void onServiceConnected(ComponentName className, IBinder service) {
//            AntbuddyService.LocalBinder binder = (AntbuddyService.LocalBinder) service;
//            mIRemoteService = binder.getService();
//            mBound = true;
//            LogHtk.d(LogHtk.SERVICE_TAG, "CenterActivity/onServiceConnected");
//        }
//
//        public void onServiceDisconnected(ComponentName className) {
//            mIRemoteService = null;
//            mBound = false;
//            LogHtk.e(LogHtk.SERVICE_TAG, "CenterActivity/onServiceDisconnected");
//        }
//    };

    private BroadcastReceiver loadMoreReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String loadMessage = intent.getStringExtra("loadMessage");
            if (loadMessage.equals("start")) {
//                LogHtk.i(LogHtk.Test1, "-->start");
                isloadedMessages = false;
            }
            if (loadMessage.equals("loaded")) {
                isloadedMessages = false;
                mSwipyRefreshLayout.setRefreshing(false);
//                LogHtk.i(LogHtk.Test1, "-->done");
                int sizeMessages = intent.getIntExtra("sizeMessages", 0);
                chatAdapter.updateAdapter(sizeMessages);
            }

            if (loadMessage.equals("saved")) {
                isloadedMessages = true;
            }

            if (loadMessage.equals("error")) {
//                LogHtk.i(LogHtk.Test1, "-->error");
                mSwipyRefreshLayout.setRefreshing(false);
            }
            if (loadMessage.equals("empty")) {
//                LogHtk.i(LogHtk.Test1, "-->empty");
                mSwipyRefreshLayout.setRefreshing(false);
            }
        }
    };

    /**
     * chatReceiver broadcast. Receive chat from xmpp
     */
    private final BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                String idMessage = intent.getStringExtra("idMessage");
//                LogHtk.i(LogHtk.Test1, "Nhan Message = " + idMessage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupRealmOne();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            keyRoom = bundle.getString(kKeyRoom);
            isGroup = bundle.getBoolean(key_type);
            title = bundle.getString(key_title);
            keyMe = realmManager.getUserme().getKey();
        }
        setContentView(R.layout.activity_chat);
        registerReceiver(loadMoreReceiver, new IntentFilter(BroadcastConstant.LOAD_MORE_CHATMESSAGE));
        isloadedMessages = false;

        loadingDatabase();

        initViews();
        viewsListener();
        updateUI();

        loadMoreMessages1();
//        loadMoreMessages();
        registerReceiver(messageReceiver, new IntentFilter(BroadcastConstant.BROAD_CAST_RECEIVER_CHAT));

//        boolean isConnectService = connectServiceInAndroid();
//        if (!isConnectService) {
//            LogHtk.i(LogHtk.SERVICE_TAG, "CenterActivity can not get service object in android!");
//        }
    }

    private void setupRealmOne() {
        realmManager = new RObjectManagerOne();
        realmManager.setUserme(realmManager.getRealm().where(RUserMe.class).findFirst());
        realmManager.setUsers(realmManager.getRealm().where(RUser.class).findAll());
        realmManager.setRooms(realmManager.getRealm().where(RRoom.class).findAll());

        realmManager.addUserMeListener(new RealmChangeListener() {
            @Override
            public void onChange() {

            }
        });

        realmManager.addUsersListener(new RealmChangeListener() {
            @Override
            public void onChange() {

            }
        });

        realmManager.addRoomsListener(new RealmChangeListener() {
            @Override
            public void onChange() {

            }
        });
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(messageReceiver);
        unregisterReceiver(loadMoreReceiver);

        if (realmManager != null) {
            realmManager.removeUserMeListener();
            realmManager.removeRoomsListener();
            realmManager.removeUsersListener();
            realmManager.removeChatMessagesListener();
            realmManager.closeRealm();
        }

        super.onDestroy();
    }

//    private boolean connectServiceInAndroid() {
//        if(AntbuddyService.mAntbuddyService == null) {
//            Intent intent = new Intent(this, AntbuddyService.class);
//            bindService(intent, mConnection, Service.BIND_AUTO_CREATE);
//        } else {
//            mIRemoteService = AntbuddyService.mAntbuddyService;
//        }
//        return mIRemoteService != null;
//    }

    @Override
    public void setTitle(CharSequence title) {
        tv_title.setText(title);
    }

    private void loadingDatabase() {


        if (isGroup) {  // Groups
            realmManager.setChatMessages(realmManager.getRealm().where(RChatMessage.class).equalTo("fromKey", keyRoom).findAll().distinct("id"));
        } else {    // 1-1, myseft
            if (keyMe == keyRoom) {

                realmManager.setChatMessages(realmManager.getRealm().where(RChatMessage.class)
                        .equalTo("fromKey", keyRoom)
                        .equalTo("senderKey", keyRoom)
                        .equalTo("receiverKey", keyRoom)
                        .findAll().distinct("id"));
            } else {   // 1-1
                realmManager.setChatMessages(realmManager.getRealm().where(RChatMessage.class)
                        .equalTo("fromKey", keyMe)
                        .equalTo("senderKey", keyMe)
                        .equalTo("receiverKey", keyRoom)
                        .or()
                        .equalTo("fromKey", keyRoom)
                        .equalTo("senderKey", keyRoom)
                        .equalTo("receiverKey", keyMe)
                        .findAll().distinct("id"));
            }
        }

        realmManager.getChatMessages().sort("time", Sort.ASCENDING);
        realmManager.addChatMessagesListener(new RealmChangeListener() {
            @Override
            public void onChange() {
                lv_messages.setSelection(lv_messages.getCount() - 1);
                if (!isloadedMessages) {
                    lv_messages.setSelection(lv_messages.getCount() - 1);
                } else {

                }
            }
        });



//--------
//        realm = Realm.getDefaultInstance();
//
//        if (isGroup) {  // Groups
//            chatMessages = realm.where(RChatMessage.class).equalTo("fromKey", keyRoom).findAll().distinct("id");
//        } else {    // 1-1, myseft
//            if (keyMe == keyRoom) {
//
//                chatMessages = realm.where(RChatMessage.class)
//                        .equalTo("fromKey", keyRoom)
//                        .equalTo("senderKey", keyRoom)
//                        .equalTo("receiverKey", keyRoom)
//                        .findAll().distinct("id");
//            } else {   // 1-1
//                chatMessages = realm.where(RChatMessage.class)
//                        .equalTo("fromKey", keyMe)
//                        .equalTo("senderKey", keyMe)
//                        .equalTo("receiverKey", keyRoom)
//                        .or()
//                        .equalTo("fromKey", keyRoom)
//                        .equalTo("senderKey", keyRoom)
//                        .equalTo("receiverKey", keyMe)
//                        .findAll().distinct("id");
//            }
//        }
//
//        chatMessages.sort("time", Sort.ASCENDING);
//
//
////        for (int i = 0; i < chatMessages.size() ; i++) {
////            LogHtk.i(LogHtk.Test1, "------------------");
////            LogHtk.i(LogHtk.Test1, "id: " + chatMessages.get(i).getId());
////            LogHtk.i(LogHtk.Test1, "body: " + chatMessages.get(i).getBody());
////        }
//
//        chatMessages.addChangeListener(new RealmChangeListener() {
//            @Override
//            public void onChange() {
//                lv_messages.setSelection(lv_messages.getCount() - 1);
//                if (!isloadedMessages) {
//                    lv_messages.setSelection(lv_messages.getCount() - 1);
//                } else {
//
//                }
//            }
//        });
    }

    private void initViews() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        setTitle(title);
        etTypingMessage = (EmojiconEditText) findViewById(R.id.text_send);
        lv_messages = (ListView) findViewById(R.id.lv_messages);
        mSwipyRefreshLayout = (SwipyRefreshLayout) findViewById(R.id.swipyrefreshlayout);

        if (chatAdapter == null) {
            chatAdapter = new RChatAdapter(this, lv_messages, realm, realmManager.getChatMessages(), keyMe, true);
            chatAdapter.updateRealmResults(realmManager.getChatMessages());
        }

        lv_messages.setAdapter(chatAdapter);
        lv_messages.setSelection(realmManager.getChatMessages().size());

        lv_messages.setDividerHeight(0);
        lv_messages.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_DISABLED);

        mSwipyRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                loadMoreMessages1();
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

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

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
                if (!TextUtils.isEmpty(text_body)) {
                    RUserMe userMe = realmManager.getUserme();
                    if (userMe != null) {
                        RChatMessage chatMessage = new RChatMessage(keyRoom, text_body, isGroup, userMe);
//                    chatMessage.showLog();
                        AntbuddyService.getInstance().sendMessageOut(chatMessage);
                    }

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
//        APIManager.GETMessages(mChatAdapter1.getBefore(), keyReceiver, (isRoom ? "groupchat" : "chat"), new HttpRequestReceiver<List<ChatMessage>>() {
//            @Override
//            public void onSuccess(List<ChatMessage> listMessages) {
//
//                Collections.reverse(listMessages);
//                mSwipyRefreshLayout.setRefreshing(false);
//                mChatAdapter.addMessages(listMessages, isFister);
//
//                isFister = false;
//                if (listMessages.size() > 0) {
//                    before = listMessages.get(0).getDatetime();
//                }
//            }
//
//            @Override
//            public void onError(String error) {
//                LogHtk.i(LogHtk.ChatActivity, "-->Error");
//                mSwipyRefreshLayout.setRefreshing(false);
//            }
//        });
    }

    public void loadMoreMessages1(){
        // Check internet
        if(!AndroidHelper.warningInternetConnection(ChatActivity.this)) {
            mSwipyRefreshLayout.setRefreshing(false);
            return;
        }

        AntbuddyService.getInstance().loadMessage(chatAdapter.getBefore(), keyRoom, isGroup);

//        APIManager.GETMessages1(chatAdapter.getBefore(), keyRoom, (isGroup ? "groupchat" : "chat"), new HttpRequestReceiver<List<GChatMassage>>() {
//            @Override
//            public void onSuccess(List<GChatMassage> listMessages) {
//                LogHtk.i(LogHtk.Test1, "----loadMoreMessages1---");
//                LogHtk.i(LogHtk.Test1, "listMessages: " + listMessages.size());
//
//                if (listMessages.size() > 0) {
//                    chatAdapter.saveMessagesIntoDB(listMessages);
//                    chatAdapter.updateAdapter(listMessages.size());
//                }
//
//                mSwipyRefreshLayout.setRefreshing(false);
//            }
//
//            @Override
//            public void onError(String error) {
//                LogHtk.i(LogHtk.ChatActivity, "-->Error");
//                mSwipyRefreshLayout.setRefreshing(false);
//            }
//        });
    }

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
