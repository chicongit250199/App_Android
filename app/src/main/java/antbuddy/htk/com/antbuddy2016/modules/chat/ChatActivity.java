package antbuddy.htk.com.antbuddy2016.modules.chat;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import java.io.File;
import java.io.IOException;
import java.net.URI;

import antbuddy.htk.com.antbuddy2016.R;
import antbuddy.htk.com.antbuddy2016.RealmObjects.RChatMessage;
import antbuddy.htk.com.antbuddy2016.RealmObjects.RObjectManagerOne;
import antbuddy.htk.com.antbuddy2016.RealmObjects.RRoom;
import antbuddy.htk.com.antbuddy2016.RealmObjects.RUser;
import antbuddy.htk.com.antbuddy2016.RealmObjects.RUserMe;
import antbuddy.htk.com.antbuddy2016.adapters.RChatAdapter;
import antbuddy.htk.com.antbuddy2016.api.APIManager;
import antbuddy.htk.com.antbuddy2016.customview.HTKPhoToView;
import antbuddy.htk.com.antbuddy2016.interfaces.HttpRequestReceiver;
import antbuddy.htk.com.antbuddy2016.model.FileAntBuddy;
import antbuddy.htk.com.antbuddy2016.service.AntbuddyService;
import antbuddy.htk.com.antbuddy2016.setting.ABSharedPreference;
import antbuddy.htk.com.antbuddy2016.util.AndroidHelper;
import antbuddy.htk.com.antbuddy2016.util.BroadcastConstant;
import antbuddy.htk.com.antbuddy2016.util.LogHtk;
import antbuddy.htk.com.antbuddy2016.util.NationalTime;
import github.ankushsachdeva.emojicon.EmojiconEditText;
import github.ankushsachdeva.emojicon.EmojiconGridView;
import github.ankushsachdeva.emojicon.EmojiconsPopup;
import github.ankushsachdeva.emojicon.emoji.Emojicon;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.Sort;

/**
 * Created by Micky on 4/1/2016.
 */
public class ChatActivity extends Activity {

    /**
     * Id to identify a camera permission request.
     */
//    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 1990;
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 1990;

    public final static String kKeyRoom = "Key of Room";
    public final static String key_type = "type";
    public final static String key_title = "title";

    // This fields need in a Chat Room
    private String keyRoom = ""; //ex: a667b2a0-d636-11e5-9bc1-25a4e1ac232c, key group hoac key cua user != jid (cua xmpp)
    private boolean isGroup = true; //true: la room ; false: chat 1-1
    private String title;
    private String keyMe = "";

    private boolean isloadedMessages;

    private RObjectManagerOne realmManager;


    private RelativeLayout rootView;
    private LinearLayout navigationBarID;
    private Button btnAttachment;
    private LinearLayout idAttachView;

    private SwipyRefreshLayout  mSwipyRefreshLayout;
    private TextView            tv_title;
    private RelativeLayout      areaBack;

    // Input message
    private LinearLayout cameraView;
    static int TAKE_PIC =1;
    Uri outPutfileUri;


    private HTKPhoToView imgPhoto;
    private ImageView imgDelete;
    private ImageView imgEditPhoto;

    private ListView            lv_messages;
    private RChatAdapter        chatAdapter;
    private EmojiconEditText    etTypingMessage;
    private ImageView           imgSendMessage;
    private ImageView           btn_smile;
    private EmojiconsPopup popup;

    //Animations
    private Animation animationFadeIn;

    private BroadcastReceiver loadMoreReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String loadMessage = intent.getStringExtra("loadMessage");
            if (loadMessage.equals("start")) {
                //LogHtk.i(LogHtk.Test1, "-->start");
                isloadedMessages = false;
            }
            if (loadMessage.equals("loaded")) {
                isloadedMessages = false;
                mSwipyRefreshLayout.setRefreshing(false);
                //LogHtk.i(LogHtk.Test1, "-->done");
                int sizeMessages = intent.getIntExtra("sizeMessages", 0);
                chatAdapter.updateAdapter(sizeMessages);
            }

            if (loadMessage.equals("saved")) {
                isloadedMessages = true;
            }

            if (loadMessage.equals("error")) {
                //LogHtk.i(LogHtk.Test1, "-->error");
                mSwipyRefreshLayout.setRefreshing(false);
            }
            if (loadMessage.equals("empty")) {
                //LogHtk.i(LogHtk.Test1, "-->empty");
                mSwipyRefreshLayout.setRefreshing(false);
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

        loadMoreMessages1();

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

    @Override
    public void setTitle(CharSequence title) {
        tv_title.setText(title);
    }

    private void loadingDatabase() {
        if (isGroup) {  // Groups
            realmManager.setChatMessages(realmManager.getRealm().where(RChatMessage.class).equalTo("fromKey", keyRoom).findAll().distinct("id"));
        } else {    // 1-1, myseft
            if (keyMe.equals(keyRoom)) {

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
                        .or()
                        .equalTo("fromKey", ABSharedPreference.get(ABSharedPreference.KEY_XMPP_DOMAIN))
                        .equalTo("senderKey", ABSharedPreference.get(ABSharedPreference.KEY_XMPP_DOMAIN))
                        .equalTo("receiverKey", keyRoom)
                        .findAll().distinct("id"));


//                LogHtk.i(LogHtk.Test1, "keyRoom = " + keyRoom);
//                realmManager.setChatMessages(realmManager.getRealm().where(RChatMessage.class)
//                        .equalTo("org", keyRoom)
//                        .findAll().distinct("id"));


            }
        }

        realmManager.getChatMessages().sort("time", Sort.ASCENDING);
    }

    private void initViews() {
        animationFadeIn = AnimationUtils.loadAnimation(this, R.anim.fadein);

        navigationBarID = (LinearLayout) findViewById(R.id.navigationBarID);
        idAttachView = (LinearLayout) findViewById(R.id.idAttachView);
        idAttachView.setVisibility(View.GONE);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, navigationBarID.getLayoutParams().height, 0, 0);
        idAttachView.setLayoutParams(layoutParams);

        btnAttachment = (Button) findViewById(R.id.btnAttachment);



        cameraView = (LinearLayout) findViewById(R.id.cameraView);
        imgPhoto = (HTKPhoToView) findViewById(R.id.imgPhoto);
        imgPhoto.setVisibility(View.GONE);


        imgDelete = (ImageView) findViewById(R.id.imgDelete);
        imgDelete.setVisibility(View.GONE);

        imgEditPhoto = (ImageView) findViewById(R.id.imgEditPhoto);
        imgEditPhoto.setVisibility(View.GONE);


        rootView = (RelativeLayout) findViewById(R.id.root_view);
        tv_title = (TextView) findViewById(R.id.tv_title);
        setTitle(title);
        etTypingMessage = (EmojiconEditText) findViewById(R.id.text_send);
        lv_messages = (ListView) findViewById(R.id.lv_messages);
        mSwipyRefreshLayout = (SwipyRefreshLayout) findViewById(R.id.swipyrefreshlayout);

        if (chatAdapter == null) {
            chatAdapter = new RChatAdapter(this, lv_messages, realmManager.getRealm(), realmManager.getChatMessages(), keyMe, true);
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

        // EMOJICONs
        btn_smile = (ImageView) findViewById(R.id.btn_smile);
        popup = new EmojiconsPopup(rootView, this);

        //Will automatically set size according to the soft keyboard size
        popup.setSizeForSoftKeyboard();

        //If the emoji popup is dismissed, change emojiButton to smiley icon
        popup.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                changeEmojiKeyboardIcon(btn_smile, R.drawable.ab_smile);
            }
        });

        //If the text keyboard closes, also dismiss the emoji popup
        popup.setOnSoftKeyboardOpenCloseListener(new EmojiconsPopup.OnSoftKeyboardOpenCloseListener() {

            @Override
            public void onKeyboardOpen(int keyBoardHeight) {
                popup.dismiss();
            }

            @Override
            public void onKeyboardClose() {

            }
        });

        //On emoji clicked, add it to edittext
        popup.setOnEmojiconClickedListener(new EmojiconGridView.OnEmojiconClickedListener() {

            @Override
            public void onEmojiconClicked(Emojicon emojicon) {
                if (etTypingMessage == null || emojicon == null) {
                    return;
                }

                int start = etTypingMessage.getSelectionStart();
                int end = etTypingMessage.getSelectionEnd();
                if (start < 0) {
                    etTypingMessage.append(emojicon.getEmoji());
                } else {
                    etTypingMessage.getText().replace(Math.min(start, end),
                            Math.max(start, end), emojicon.getEmoji(), 0,
                            emojicon.getEmoji().length());
                }
            }
        });

        //On backspace clicked, emulate the KEYCODE_DEL key event
        popup.setOnEmojiconBackspaceClickedListener(new EmojiconsPopup.OnEmojiconBackspaceClickedListener() {

            @Override
            public void onEmojiconBackspaceClicked(View v) {
                KeyEvent event = new KeyEvent(
                        0, 0, 0, KeyEvent.KEYCODE_DEL, 0, 0, 0, 0, KeyEvent.KEYCODE_ENDCALL);
                etTypingMessage.dispatchKeyEvent(event);
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

        Rect r = new Rect();
        rootView.getWindowVisibleDisplayFrame(r);
    }

    public int pxToDp(int px) {
        DisplayMetrics displayMetrics = getApplicationContext().getResources().getDisplayMetrics();
        int dp = Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return dp;
    }

    private void viewsListener() {
        btnAttachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (idAttachView.getVisibility() == View.VISIBLE) {
                    idAttachView.setVisibility(View.GONE);
                } else {
                    //TODO: WARNING when run this line! Skipped 34 frames!  The application may be doing too much work on its main thread
                    idAttachView.setVisibility(View.VISIBLE);
//                    idAttachView.startAnimation(animationFadeIn);
                }
            }
        });

        cameraView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Here, thisActivity is the current activity
                if (ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {

                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(ChatActivity.this,
                            Manifest.permission.CAMERA)) {

                        // Show an expanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.


                    } else {
                        // No explanation needed, we can request the permission.

                        ActivityCompat.requestPermissions(ChatActivity.this,
                                new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                MY_PERMISSIONS_REQUEST_CAMERA);

                        // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                        // app-defined int constant. The callback method gets the
                        // result of the request.
                    }
                } else {
                    openCamera();
                }
            }
        });

        imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgPhoto.setVisibility(View.GONE);
                imgDelete.setVisibility(View.GONE);
                imgEditPhoto.setVisibility(View.GONE);
            }
        });

        imgEditPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        // To toggle between text keyboard and emoji keyboard keyboard(Popup)
        btn_smile.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//
                if (popup.isShowing()) {
                    // Show keyboard
                    etTypingMessage.setFocusableInTouchMode(true);
                    etTypingMessage.requestFocus();
                    final InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.showSoftInput(etTypingMessage, InputMethodManager.SHOW_IMPLICIT);
                    changeEmojiKeyboardIcon(btn_smile, R.drawable.ab_smile);
                } else {
                    if (popup.isKeyBoardOpen()) {
                        // Dismiss Keyboard
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(rootView.getWindowToken(), 0);
                    }

                    popup.showPopup();
                    changeEmojiKeyboardIcon(btn_smile, R.drawable.ab_keyboard);
                }
            }
        });

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
                        AntbuddyService.getInstance().sendMessageOut(chatMessage);
                    }

                    etTypingMessage.setText("");
                }

                if (imgPhoto.getVisibility() == View.VISIBLE) {

                    File file = new File(outPutfileUri.getPath());

                    AntbuddyService.getInstance().uploadFile(file, new HttpRequestReceiver<FileAntBuddy>() {
                        @Override
                        public void onSuccess(final FileAntBuddy fileAntBuddy) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    RUserMe userMe = realmManager.getUserme();
                                    if (userMe != null) {
                                        RChatMessage chatMessage = new RChatMessage(keyRoom, "", isGroup, fileAntBuddy, userMe);
                                        AntbuddyService.getInstance().sendMessageOut(chatMessage);

                                        imgPhoto.setVisibility(View.GONE);
                                        imgDelete.setVisibility(View.GONE);
                                        imgEditPhoto.setVisibility(View.GONE);
                                    }
                                }
                            });
                        }

                        @Override
                        public void onError(String error) {
                            LogHtk.e(LogHtk.ErrorHTK, "error = " + error);
                        }
                    });
                }
            }
        });

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
    }

    public void loadMoreMessages1(){
        // Check internet
        if(!AndroidHelper.warningInternetConnection(ChatActivity.this)) {
            mSwipyRefreshLayout.setRefreshing(false);
            return;
        }

        AntbuddyService.getInstance().loadMessage(chatAdapter.getBefore(), keyRoom, isGroup);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TAKE_PIC && resultCode == RESULT_OK) {
            // Show image in Edit Text
            imgPhoto.setVisibility(View.VISIBLE);

//            LogHtk.i(LogHtk.Test1, "URI: " + outPutfileUri.toString());
            new Exception("PATH : " + outPutfileUri.getPath()).printStackTrace();
            Glide.with(getApplicationContext())
                    .load(new File(outPutfileUri.getPath()))
                    .into(imgPhoto);
            imgDelete.setVisibility(View.VISIBLE);
//            imgEditPhoto.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        if (popup.isShowing()) {
            popup.dismiss();
            return;
        }

        if (imgPhoto.getVisibility() == View.VISIBLE) {
            imgDelete.setVisibility(View.GONE);
            imgPhoto.setVisibility(View.GONE);
            etTypingMessage.setText("");
            return;
        }

        super.onBackPressed();
    }

    private static File getOutputMediaFile(){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "AntBuddyPhoto");

        if (!mediaStorageDir.exists()){
            if (!mediaStorageDir.mkdirs()){
                return null;
            }
        }

        Long tsLong = System.currentTimeMillis()/1000;
        String ts = tsLong.toString();

        return new File(mediaStorageDir.getPath() + File.separator +
                "Ant"+ ts + ".jpg");
    }

    private void openCamera() {
        Intent intent= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //File file = new File(Environment.getExternalStorageDirectory(), "Antbuddy_"+ NationalTime.getLocalTimeToUTCTime() + ".jpg");

        Long tsLong = System.currentTimeMillis()/1000;
        String ts = tsLong.toString();

        File file = getOutputMediaFile();

        if (file != null) {
            outPutfileUri = Uri.fromFile(file);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outPutfileUri);
            startActivityForResult(intent, TAKE_PIC);
        } else {
            LogHtk.e(LogHtk.ErrorHTK, "ChatActivity/ Error! Can not create photo file!");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    openCamera();
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    private void changeEmojiKeyboardIcon(ImageView iconToBeChanged, int drawableResourceId){
        iconToBeChanged.setBackgroundResource(drawableResourceId);

    }
}
