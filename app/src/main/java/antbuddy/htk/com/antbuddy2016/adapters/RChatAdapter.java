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

import com.bumptech.glide.Glide;

import antbuddy.htk.com.antbuddy2016.R;
import antbuddy.htk.com.antbuddy2016.RealmObjects.RChatMessage;
import antbuddy.htk.com.antbuddy2016.RealmObjects.RObjectManagerOne;
import antbuddy.htk.com.antbuddy2016.RealmObjects.RUser;
import antbuddy.htk.com.antbuddy2016.setting.ABSharedPreference;
import antbuddy.htk.com.antbuddy2016.util.NationalTime;
import github.ankushsachdeva.emojicon.EmojiconTextView;
import io.realm.Realm;
import io.realm.RealmBaseAdapter;
import io.realm.RealmResults;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by thanhnguyen on 12/04/2016.
 */
public class RChatAdapter extends RealmBaseAdapter<RChatMessage> {
    private final Context ctx;
    private final ListView mListView;
//    private ArrayList<RChatMassage> mMessages;
    RealmResults<RChatMessage> realmResults;
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

    public RChatAdapter(Context context,  ListView mListView, Realm realm, RealmResults<RChatMessage> realmResults, String keyMe, boolean automaticUpdate) {
        super(context, realmResults, automaticUpdate);
        this.realmResults = realmResults;

        this.mListView = mListView;
        this.realm = realm;
        this.ctx = context;
        this.keyMe = keyMe;
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

        RChatMessage message = realmResults.get(position);
        String body = message.getBody();

        // DECODE
        body = Html.fromHtml(body).toString();

        //set data to GUI
        String senderKey = message.getSenderKey();
        boolean isMe = (TextUtils.isEmpty(message.getSubtype()) || message.getSubtype().equals("null")) && senderKey.equals(keyMe);
        Boolean isFile = message.getFileAntBuddy() != null && message.getFileAntBuddy().getMimeType().startsWith("image");

//        if (message.getFromKey().equals(ABSharedPreference.get(ABSharedPreference.KEY_XMPP_DOMAIN))) {
//            isMe = true;
//        }

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
            RUser user;
            if (!senderKey.isEmpty()) {
                user = RObjectManagerOne.findUser(senderKey, this.realm);
                if (user != null) {
                    url_avatar = user.getAvatar();
                }
            }

//            if (message.getFromKey().equals(ABSharedPreference.get(ABSharedPreference.KEY_XMPP_DOMAIN))) {
//                user = RObjectManagerOne.findUser(keyMe, this.realm);
//
//                if (user != null) {
//                    url_avatar = user.getAvatar();
//                }
//            }

            Glide.with(ctx)
                    .load(url_avatar)
                    .override(60, 60)
                    .bitmapTransform(new CropCircleTransformation(ctx))
                    .into(isMe ? holder.imgAvatarRight : holder.imgAvatarLeft);
        }

        return rowView;
    }

//    public void addMessages(List<RChatMessage> messages, boolean isGotoBottom) {
//        Rect corners = new Rect();
//        if (!isGotoBottom) {
//            View view = mListView.getChildAt(0);
//            mListView.getLocalVisibleRect(corners);
//        }
//        //update data in view
//        realmResults.addAll(0, messages);
//        notifyDataSetChanged();
//
//        if (!isGotoBottom) {
//            mListView.setSelected(true);
//            mListView.setSelectionFromTop(messages.size(), corners.top);
//        } else {
//            mListView.setSelected(true);
//            mListView.setSelection(realmResults.size());
//        }
//    }

//    public void saveMessageIntoDB(RChatMessage messages) {
//        realm.beginTransaction();
//        RChatMessage messageSaved = realm.createObject(RChatMessage.class);
//        messageSaved.setSenderJid(messages.getSenderJid());
//        messageSaved.setSenderId(messages.getSenderId());
//        messageSaved.setSenderName(messages.getSenderName());
//        messageSaved.setBody(messages.getBody());
//        messageSaved.setFromId(messages.getFromId());
//        messageSaved.setReceiverJid(messages.getReceiverJid());
//        messageSaved.setReceiverId(messages.getReceiverId());
//        messageSaved.setReceiverName(messages.getReceiverName());
//        messageSaved.setIsModified(messages.isModified());
//        messageSaved.setType(messages.getType());
//        messageSaved.setSubtype(messages.getSubtype());
//        messageSaved.setTime(messages.getTime());
//        messageSaved.setExpandBody(messages.getExpandBody());
//        messageSaved.setOrg(messages.getOrg());
//        messageSaved.setSenderKey(messages.getSenderKey());
//        messageSaved.setFromKey(messages.getFromKey());
//        messageSaved.setReceiverKey(messages.getReceiverKey());
//        messageSaved.setFileAntBuddy(messages.getFileAntBuddy());
//
//        realm.copyToRealmOrUpdate(messageSaved);
//        realm.commitTransaction();
//    }

    public void addMessage(RChatMessage message, boolean isGotoBottom) {
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
            before = NationalTime.getLocalTimeToUTCTime();
            isTheFisrtTimeLoadMessage = true;
        } else {
            isTheFisrtTimeLoadMessage = false;
        }
        return before;
    }

    public void updateAdapter(int sizeFromTop) {
        if (isTheFisrtTimeLoadMessage) {    // the first time
            mListView.setSelected(true);
            mListView.setSelection(realmResults.size());
        } else {        // Load more
            Rect corners = new Rect();
            mListView.getLocalVisibleRect(corners);
            mListView.setSelectionFromTop(sizeFromTop, corners.top);
        }

        if (realmResults != null && realmResults.size() > 0) {
            before = realmResults.first().getTime();
        } else {
            before = NationalTime.getLocalTimeToUTCTime();
        }

    }
}
