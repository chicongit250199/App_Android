package antbuddy.htk.com.antbuddy2016.adapters;

import android.content.Context;
import android.graphics.Rect;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import antbuddy.htk.com.antbuddy2016.R;
import antbuddy.htk.com.antbuddy2016.RealmObjects.RUserMe;
import antbuddy.htk.com.antbuddy2016.model.ChatMessage;
import antbuddy.htk.com.antbuddy2016.model.ObjectManager;
import antbuddy.htk.com.antbuddy2016.model.User;
import antbuddy.htk.com.antbuddy2016.model.UserMe;
import antbuddy.htk.com.antbuddy2016.util.LogHtk;
import github.ankushsachdeva.emojicon.EmojiconTextView;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by Micky on 4/1/2016.
 */
public class ChatAdapter extends ArrayAdapter<ChatMessage> {
    private final Context ctx;
    private final ListView mListView;
    private ArrayList<ChatMessage> mMessages;
    private String keyMe;

    public ChatAdapter(Context context, ListView listView) {
        super(context, R.layout.row_chat_message);
        this.ctx = context;
        mListView = listView;
        mMessages = new ArrayList<>();
        ObjectManager.getInstance().getUserMe(new ObjectManager.OnObjectManagerListener<RUserMe>() {
            @Override
            public void onSuccess(RUserMe me) {
                keyMe = me.getKey();
            }

            @Override
            public void onError(String error) {
                LogHtk.e(LogHtk.API_TAG, "Error at ChatAdapter : " + error);
            }
        });
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
            holder.imgMessage_left = (ImageView) rowView.findViewById(R.id.imgMessage_left);
            holder.imgMessage_right = (ImageView) rowView.findViewById(R.id.imgMessage_right);
            holder.rl_message_image_right = (RelativeLayout) rowView.findViewById(R.id.rl_message_image_right);
            holder.rl_message_image_left = (RelativeLayout) rowView.findViewById(R.id.rl_message_image_left);

            rowView.setTag(holder);
        } else {
            rowView = convertView;
            holder = (Holder) rowView.getTag();
        }

        ChatMessage message = mMessages.get(position);
        String body = message.getBody();

        // DECODE
        body = Html.fromHtml(body).toString();

        //set data to GUI
        String senderKey = message.getSenderKey();
        boolean isMe = (TextUtils.isEmpty(message.getSubtype()) || message.getSubtype().equals("null")) && senderKey.equals(keyMe);
        Boolean isFile = message.getFileAntBuddy() != null && message.getFileAntBuddy().getMimeType().startsWith("image");

        if (isMe) {
            holder.ll_message_left.setVisibility(View.GONE);
            holder.ll_message_right.setVisibility(View.VISIBLE);

            holder.textMessage_right.setVisibility(isFile?View.GONE:View.VISIBLE);
            holder.rl_message_image_right.setVisibility(!isFile?View.GONE:View.VISIBLE);
            if (!isFile) {
                holder.textMessage_right.setText(body);
            } else {
                Glide.with(ctx)
                        .load(message.getFileAntBuddy().getThumbnailUrl())
                        .into( holder.imgMessage_right);
            }
        } else {
            holder.ll_message_left.setVisibility(View.VISIBLE);
            holder.ll_message_right.setVisibility(View.GONE);

            holder.textMessage_left.setVisibility(isFile?View.GONE:View.VISIBLE);
            holder.rl_message_image_left.setVisibility(!isFile?View.GONE:View.VISIBLE);
            if (!isFile) {
                holder.textMessage_left.setText(body);
            } else {
                Glide.with(ctx)
                        .load(message.getFileAntBuddy().getThumbnailUrl())
                        .into( holder.imgMessage_left);
            }
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
                user = ObjectManager.getInstance().findUser(senderKey);
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
        public ImageView imgMessage_left;
        public ImageView imgMessage_right;
        public RelativeLayout rl_message_image_right;
        public RelativeLayout rl_message_image_left;
    }

    @Override
    public int getCount() {
        return mMessages.size();
    }

    /****/
    public void addMessages(ArrayList<ChatMessage> messages) {
        addMessages(messages, true);
    }

    public void addMessages(List<ChatMessage> messages, boolean isGotoBottom) {
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
