package antbuddy.htk.com.antbuddy2016.modules.chat;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import antbuddy.htk.com.antbuddy2016.R;
import antbuddy.htk.com.antbuddy2016.adapters.ChatAdapter;

/**
 * Created by Micky on 4/1/2016.
 */
public class ChatActivity extends Activity {
    private ListView lv_messages;
    private ChatAdapter mChatAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initView();
    }
    
    private void initView() {
        lv_messages = (ListView) findViewById(R.id.lv_messages);
        // chat addapter and list event
        if (mChatAdapter == null) {
            mChatAdapter = new ChatAdapter(this, lv_messages);
        }

        lv_messages.setAdapter(mChatAdapter);
        lv_messages.setDividerHeight(0);
        lv_messages.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (lv_messages.getFirstVisiblePosition() == 0 && scrollState == SCROLL_STATE_IDLE) {
                    if (lv_messages.getChildAt(0) != null && lv_messages.getChildAt(0).getTop() == 0) {
                        //loadMoreMessages();
//                        mSwipyRefreshLayout.setRefreshing(true);
//                        loadMoreMessages();
                    }
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
    }
    
    //goi len webservice lay tin nhan cua room / uses
    public void loadMessages(){

    }
}
