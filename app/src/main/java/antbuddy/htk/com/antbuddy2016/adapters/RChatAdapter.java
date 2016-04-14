package antbuddy.htk.com.antbuddy2016.adapters;

import android.content.Context;
import android.graphics.Rect;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.Collections;
import java.util.List;

import antbuddy.htk.com.antbuddy2016.GsonObjects.GChatMassage;
import antbuddy.htk.com.antbuddy2016.GsonObjects.GFileAntBuddy;
import antbuddy.htk.com.antbuddy2016.R;
import antbuddy.htk.com.antbuddy2016.RealmObjects.RChatMassage;
import antbuddy.htk.com.antbuddy2016.RealmObjects.RFileAntBuddy;
import antbuddy.htk.com.antbuddy2016.model.ChatMessage;
import antbuddy.htk.com.antbuddy2016.model.ObjectManager;
import antbuddy.htk.com.antbuddy2016.model.User;
import antbuddy.htk.com.antbuddy2016.model.UserMe;
import antbuddy.htk.com.antbuddy2016.util.LogHtk;
import antbuddy.htk.com.antbuddy2016.util.NationalTime;
import github.ankushsachdeva.emojicon.EmojiconTextView;
import io.realm.Realm;
import io.realm.RealmBaseAdapter;
import io.realm.RealmResults;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by thanhnguyen on 12/04/2016.
 */
public class RChatAdapter extends RealmBaseAdapter<RChatMassage> {
    private final Context ctx;
    private final ListView mListView;
//    private ArrayList<RChatMassage> mMessages;
    RealmResults<RChatMassage> realmResults;
    private String keyMe;
    Realm realm;

    private String before = "";  //last time : set bang time hien tai neu null
    private Boolean isTheFisrtTimeLoadMessage;

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

    public RChatAdapter(Context context,  ListView mListView, Realm realm, RealmResults<RChatMassage> realmResults, boolean automaticUpdate) {
        super(context, realmResults, automaticUpdate);
        this.realmResults = realmResults;

        this.mListView = mListView;
        this.realm = realm;
        this.ctx = context;
//        mListView = listView;
        ObjectManager.getInstance().getUserMe(new ObjectManager.OnObjectManagerListener<UserMe>() {
            @Override
            public void onSuccess(UserMe me) {
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
            holder.ll_message_right       = (LinearLayout) rowView.findViewById(R.id.ll_message_right);
            holder.ll_message_left        = (LinearLayout) rowView.findViewById(R.id.ll_message_left);
            holder.textMessage_left       = (EmojiconTextView) rowView.findViewById(R.id.textMessage_left);
            holder.textMessage_right      = (EmojiconTextView) rowView.findViewById(R.id.textMessage_right);
            holder.imgAvatarLeft          = (ImageView) rowView.findViewById(R.id.imgAvatarLeft);
            holder.imgAvatarRight         = (ImageView) rowView.findViewById(R.id.imgAvatarRight);
            holder.imgMessage_left        = (ImageView) rowView.findViewById(R.id.imgMessage_left);
            holder.imgMessage_right       = (ImageView) rowView.findViewById(R.id.imgMessage_right);
            holder.rl_message_image_right = (RelativeLayout) rowView.findViewById(R.id.rl_message_image_right);
            holder.rl_message_image_left  = (RelativeLayout) rowView.findViewById(R.id.rl_message_image_left);

            rowView.setTag(holder);
        } else {
            rowView = convertView;
            holder = (Holder) rowView.getTag();
        }

        RChatMassage message = realmResults.get(position);
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
            holder.rl_message_image_right.setVisibility(!isFile ? View.GONE : View.VISIBLE);
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

    @Override
    public int getCount() {
        return realmResults.size();
    }

    public void addMessages(List<RChatMassage> messages, boolean isGotoBottom) {
        Rect corners = new Rect();
        if (isGotoBottom == false) {
            View view = mListView.getChildAt(0);
            mListView.getLocalVisibleRect(corners);
        }
        //update data in view
        realmResults.addAll(0, messages);
        notifyDataSetChanged();

        if (isGotoBottom == false) {
            mListView.setSelected(true);
            mListView.setSelectionFromTop(messages.size(), corners.top);
        } else {
            mListView.setSelected(true);
            mListView.setSelection(realmResults.size());
        }
    }

    public void saveMessagesIntoDB(List<GChatMassage> messages) {
        LogHtk.i(LogHtk.Test1, "--------saveMessagesIntoDB---------");
        LogHtk.i(LogHtk.Test1, "realmResults = " + realmResults.size());
        for (int i = 0; i < messages.size(); i++) {
            GChatMassage mess = messages.get(i);

            RChatMassage messageSaved = new RChatMassage();
            messageSaved.setId(mess.getId());
            messageSaved.setSenderJid(mess.getSenderJid());
            messageSaved.setSenderId(mess.getSenderId());
            messageSaved.setSenderName(mess.getSenderName());
            messageSaved.setBody(mess.getBody());
            messageSaved.setFromId(mess.getFromId());
            messageSaved.setReceiverJid(mess.getReceiverJid());
            messageSaved.setReceiverId(mess.getReceiverId());
            messageSaved.setReceiverName(mess.getReceiverName());
            messageSaved.setIsModified(mess.isModified());
            messageSaved.setType(mess.getType());
            messageSaved.setSubtype(mess.getSubtype());
            messageSaved.setTime(mess.getTime());
            messageSaved.setExpandBody(mess.getExpandBody());
            messageSaved.setOrg(mess.getOrg());
            messageSaved.setSenderKey(mess.getSenderKey());
            messageSaved.setFromKey(mess.getFromKey());
            messageSaved.setReceiverKey(mess.getReceiverKey());

            GFileAntBuddy gFile = mess.getFileAntBuddy();
            if (gFile != null) {
                RFileAntBuddy rFile = new RFileAntBuddy();
                rFile.setSize(gFile.getSize());
                rFile.setFileUrl(gFile.getFileUrl());
                rFile.setMimeType(gFile.getMimeType());
                rFile.setThumbnailUrl(gFile.getThumbnailUrl());
                rFile.setThumbnailWidth(gFile.getThumbnailWidth());
                rFile.setThumbnailHeight(gFile.getThumbnailHeight());
                messageSaved.setFileAntBuddy(rFile);
            }

            realm.beginTransaction();
            realm.copyToRealmOrUpdate(messageSaved);
            realm.commitTransaction();
        }
        LogHtk.i(LogHtk.Test1, "realmResults = " + realmResults.size());
    }

    public void saveMessageIntoDB(RChatMassage messages) {
        realm.beginTransaction();
        RChatMassage messageSaved = realm.createObject(RChatMassage.class);
        messageSaved.setSenderJid(messages.getSenderJid());
        messageSaved.setSenderId(messages.getSenderId());
        messageSaved.setSenderName(messages.getSenderName());
        messageSaved.setBody(messages.getBody());
        messageSaved.setFromId(messages.getFromId());
        messageSaved.setReceiverJid(messages.getReceiverJid());
        messageSaved.setReceiverId(messages.getReceiverId());
        messageSaved.setReceiverName(messages.getReceiverName());
        messageSaved.setIsModified(messages.isModified());
        messageSaved.setType(messages.getType());
        messageSaved.setSubtype(messages.getSubtype());
        messageSaved.setTime(messages.getTime());
        messageSaved.setExpandBody(messages.getExpandBody());
        messageSaved.setOrg(messages.getOrg());
        messageSaved.setSenderKey(messages.getSenderKey());
        messageSaved.setFromKey(messages.getFromKey());
        messageSaved.setReceiverKey(messages.getReceiverKey());
        messageSaved.setFileAntBuddy(messages.getFileAntBuddy());

        realm.copyToRealmOrUpdate(messageSaved);
        realm.commitTransaction();
    }

    public void addMessage(RChatMassage message, boolean isGotoBottom) {
//        Rect corners = new Rect();
//        int topItem = 0;
//        if (isGotoBottom == false) {
//            topItem = mListView.getFirstVisiblePosition();
//            View view = mListView.getChildAt(topItem);
//            mListView.getLocalVisibleRect(corners);
//        }
//        //update data in view
//        if (message.isModified()) {
//            boolean isFinded = false;
//            for (RChatMassage chatMessage : realmResults) {
//                if (chatMessage.getId().equals(message.getId())) {
//                    chatMessage.setBody(message.getBody());
//                    chatMessage.setIsModified(true);
//                    isFinded = true;
//                    break;
//                }
//            }
//            if (!isFinded) {
//                realmResults.add(message);
//            }
//        } else {
//
//            // Add into db
//            realm.beginTransaction();
//            RChatMassage messageSaved = realm.createObject(RChatMassage.class);
//            messageSaved = message;
//            realm.commitTransaction();
//        }
//
//        //Collections.sort(mMessages, new ChatMessage.MessageComparator());
//        notifyDataSetChanged();
//
//        if (isGotoBottom == false) {
//            mListView.setSelected(true);
//            mListView.setSelectionFromTop(topItem, corners.top);
//        } else {
//            mListView.setSelected(true);
//            mListView.setSelection(realmResults.size());
//        }
    }

    public String getBefore() {
        if (before.length() == 0) {
            LogHtk.i(LogHtk.Test1, "before 1");
            before = NationalTime.getLocalTimeToUTCTime();
            isTheFisrtTimeLoadMessage = true;
        } else {
            LogHtk.i(LogHtk.Test1, "before 2");
            isTheFisrtTimeLoadMessage = false;
        }
        LogHtk.i(LogHtk.Test1, "before: " + before);
        return before;
    }

    public void setBefore(String before) {
        this.before = before;
    }

    public void updateBefore() {
        this.before = realmResults.last().getTime();
    }

    public void updateAdapter() {
        if (isTheFisrtTimeLoadMessage) {    // the first time
            LogHtk.i(LogHtk.Test1, "updateAdapter 1");
            mListView.setSelected(true);
            mListView.setSelection(realmResults.size());
        } else {        // Load more
            LogHtk.i(LogHtk.Test1, "updateAdapter 2");
            Rect corners = new Rect();
            mListView.getLocalVisibleRect(corners);
            mListView.setSelectionFromTop(50, corners.top);
        }
        this.before = realmResults.first().getTime();
    }
}
