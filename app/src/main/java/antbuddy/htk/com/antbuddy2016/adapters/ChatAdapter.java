package antbuddy.htk.com.antbuddy2016.adapters;

import android.content.Context;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collections;

import antbuddy.htk.com.antbuddy2016.R;
import antbuddy.htk.com.antbuddy2016.model.ChatMessage;
import antbuddy.htk.com.antbuddy2016.model.ObjectManager;
import antbuddy.htk.com.antbuddy2016.model.User;
import github.ankushsachdeva.emojicon.EmojiconTextView;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by Micky on 4/1/2016.
 */
public class ChatAdapter extends ArrayAdapter<ChatMessage> {
    private final Context ctx;
    private final ListView mListView;
    private ArrayList<ChatMessage> mMessages = new ArrayList<>();
    private String keyMe = ObjectManager.getInstance().getUserMe().getKey();

    public ChatAdapter(Context context, ListView listView) {
        super(context, R.layout.row_chat_message);
        this.ctx = context;
//        sizeAvatar = getContext().getResources().getDimensionPixelSize(R.dimen.dp60);
        mListView = listView;
//        bitmapIconDefault = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.empty_avatar);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Holder holder;
        final View rowView;
        LayoutInflater vi;
        if (convertView == null) {
            holder = new Holder();
            vi = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = vi.inflate(R.layout.row_chat_message, null);
            holder.ll_message_right = (LinearLayout) rowView.findViewById(R.id.ll_message_right);
            holder.ll_message_left = (LinearLayout) rowView.findViewById(R.id.ll_message_left);
            holder.textMessage_left = (EmojiconTextView) rowView.findViewById(R.id.textMessage_left);
            holder.textMessage_right = (EmojiconTextView) rowView.findViewById(R.id.textMessage_right);
            holder.imgAvatarLeft = (ImageView) rowView.findViewById(R.id.imgAvatarLeft);
            holder.imgAvatarRight = (ImageView) rowView.findViewById(R.id.imgAvatarRight);
            rowView.setTag(holder);
        } else {
            rowView = convertView;
            holder = (Holder) rowView.getTag();
        }

        ChatMessage message = mMessages.get(position);
        //set data to GUI

        String senderKey = message.getSenderKey();
        Log.i("Hoa debug", "ChatMessage:ChatMessage:  => senderKey " + senderKey);
        Log.i("Hoa debug", "ChatMessage:ChatMessage:  => message.getSubtype() " + message.getSubtype());
        boolean isMe = TextUtils.isEmpty(message.getSubtype()) && senderKey.equals(keyMe);

        if (isMe) {
            holder.ll_message_left.setVisibility(View.GONE);
            holder.ll_message_right.setVisibility(View.VISIBLE);
            holder.textMessage_right.setText(message.getBody());
        } else {
            holder.ll_message_left.setVisibility(View.VISIBLE);
            holder.ll_message_right.setVisibility(View.GONE);
            holder.textMessage_left.setText(message.getBody());
        }

        if (!TextUtils.isEmpty(message.getSubtype()) && message.getSubtype().equals("bot-gitlab")) {
            Glide.with(ctx)
                    .load(R.drawable.gitlab_icon)
                    .override(60, 60)
                    .bitmapTransform(new CropCircleTransformation(ctx))
                    .into(isMe ? holder.imgAvatarRight : holder.imgAvatarLeft);
        } else {
            String url_avatar = "";
            User user;
            if (!senderKey.isEmpty()) {
                user = ObjectManager.getInstance().findUsers(senderKey);
                if (user != null) {
                    url_avatar = user.getAvatar();
                }
            }

            Glide.with(ctx)
                    .load(url_avatar)
                    .override(60, 60)
                    .bitmapTransform(new CropCircleTransformation(ctx))
                    .into(isMe ? holder.imgAvatarRight : holder.imgAvatarLeft);
        }


        return rowView;
    }

    public class Holder {
        public EmojiconTextView textMessage_left;
        public EmojiconTextView textMessage_right;
        public ImageView imgAvatarRight;
        public ImageView imgAvatarLeft;
        public LinearLayout ll_message_right;
        public LinearLayout ll_message_left;
    }

    @Override
    public int getCount() {
        return mMessages.size();
    }

    /****/
    public void addMessages(ArrayList<ChatMessage> messages) {
        addMessages(messages, true);
    }

    public void addMessages(ArrayList<ChatMessage> messages, boolean isGotoBottom) {
        Rect corners = new Rect();
        if (isGotoBottom == false) {
            View view = mListView.getChildAt(0);
            mListView.getLocalVisibleRect(corners);
        }
        //update data in view
        mMessages.addAll(0, messages);
        notifyDataSetChanged();

        if (isGotoBottom == false) {
            mListView.setSelected(true);
            mListView.setSelectionFromTop(messages.size(), corners.top);
        } else {
            mListView.setSelected(true);
            mListView.setSelection(mMessages.size());
        }
    }

    public void addMessage(ChatMessage message, boolean isGotoBottom) {
        Rect corners = new Rect();
        int topItem = 0;
        if (isGotoBottom == false) {
            topItem = mListView.getFirstVisiblePosition();
            View view = mListView.getChildAt(topItem);
            mListView.getLocalVisibleRect(corners);
        }
        //update data in view
        if (message.isModified()) {
            boolean isFinded = false;
            for (ChatMessage chatMessage : mMessages) {
                if (chatMessage.getId().equals(message.getId())) {
                    chatMessage.setBody(message.getBody());
                    chatMessage.setModified(true);
                    isFinded = true;
                    break;
                }
            }
            if (!isFinded) {
                mMessages.add(message);
            }
        } else {
            mMessages.add(message);
        }
        Collections.sort(mMessages, new ChatMessage.MessageComparator());
        notifyDataSetChanged();

        if (isGotoBottom == false) {
            mListView.setSelected(true);
            mListView.setSelectionFromTop(topItem, corners.top);
        } else {
            mListView.setSelected(true);
            mListView.setSelection(mMessages.size());
        }
    }
}
