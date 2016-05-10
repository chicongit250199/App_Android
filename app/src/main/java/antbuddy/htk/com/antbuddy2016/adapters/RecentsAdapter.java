package antbuddy.htk.com.antbuddy2016.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import antbuddy.htk.com.antbuddy2016.R;
import antbuddy.htk.com.antbuddy2016.RealmObjects.RObjectManagerOne;
import antbuddy.htk.com.antbuddy2016.RealmObjects.ROpeningChatRoom;
import antbuddy.htk.com.antbuddy2016.RealmObjects.RRoom;
import antbuddy.htk.com.antbuddy2016.RealmObjects.RUser;
import antbuddy.htk.com.antbuddy2016.util.LogHtk;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by Micky on 3/31/2016.
 */
public class RecentsAdapter extends BaseExpandableListAdapter {
    private final int GROUPS_POSITION = 0;
    private final int MEMBERS_POSITION = 1;
    private Context context;
    private List<String> parents;
    private List<List<ROpeningChatRoom>> childers;
    private LayoutInflater inflater;
    private RObjectManagerOne realmManager;

    public RecentsAdapter(Context context,
                          ArrayList<String> parents,
                          List<List<ROpeningChatRoom>> childers, RObjectManagerOne realmManager) {
        this.context      = context;
        this.parents      = parents;
        this.childers     = childers;
        this.realmManager = realmManager;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getGroupCount() {
        if (parents != null) {
            return parents.size();
        } else {
            return 0;
        }
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        List<ROpeningChatRoom> parent = childers.get(groupPosition);
        if (parent != null) {
            return parent.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getGroup(int groupPosition) {
        return parents.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childers.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }
    
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View v;
        if (convertView != null)
            v = convertView;
        else
            v = inflater.inflate(R.layout.parent_view, parent, false);
        String gt = (String) getGroup(groupPosition);
        TextView tv_title = (TextView) v.findViewById(R.id.tv_title);
        if (tv_title != null)
            tv_title.setText(parents.get(groupPosition));
        return v;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View rowView;
        final Holder holder;

        if (convertView == null ){
            holder = new Holder();
            rowView = inflater.inflate(R.layout.row_member, parent, false);
            holder.imgAvatar = (ImageView) rowView.findViewById(R.id.imgAvatar);
            holder.tv_user_name = (TextView) rowView.findViewById(R.id.tv_user_name);
            holder.imgXMPPStatus = (ImageView) rowView.findViewById(R.id.imgXMPPStatus);
            rowView.setTag(holder);
        } else {
            rowView = convertView;
            holder = (Holder) rowView.getTag();
        }

        final ROpeningChatRoom openingChatroom = (ROpeningChatRoom) getChild(groupPosition, childPosition);
        ViewGroup.LayoutParams layoutParams = holder.imgAvatar.getLayoutParams();
        if (groupPosition == 0) {   // List rooms
            holder.imgXMPPStatus.setVisibility(View.GONE);
            layoutParams.width = 60;
            layoutParams.height = 60;
            holder.imgAvatar.setLayoutParams(layoutParams);
            for (RRoom room : realmManager.getRooms()) {
                if (room.getKey().equals(openingChatroom.getChatRoomKey())) {
                    if (room.getIsPublic()) {
                        Glide.with(context)
                                .load(R.drawable.ic_global)
                                .placeholder(R.drawable.ic_avatar_defaul)
                                .error(R.drawable.ic_avatar_defaul)
                                .into(holder.imgAvatar);
                    } else {
                        Glide.with(context)
                                .load(R.drawable.ic_lock)
                                .placeholder(R.drawable.ic_avatar_defaul)
                                .error(R.drawable.ic_avatar_defaul)
                                .into(holder.imgAvatar);
                    }
                    holder.tv_user_name.setText(room.getName());
                    break;
                }
            }
        } else {    // List users
            layoutParams.width = 160;
            layoutParams.height = 160;
            holder.imgAvatar.setLayoutParams(layoutParams);
            //holder.imgXMPPStatus.setVisibility(View.VISIBLE);
            for (RUser user : realmManager.getUsers()) {
                if (user.getKey().equals(openingChatroom.getChatRoomKey())) {
                    Glide.with(context)
                            .load(user.getAvatar())
                            .override(60, 60)
                            .placeholder(R.drawable.ic_avatar_defaul)
                            .error(R.drawable.ic_avatar_defaul)
                            .bitmapTransform(new CropCircleTransformation(context))
                            .into(holder.imgAvatar);

                    holder.tv_user_name.setText(user.getName());

                    // Check XMPP status
                    showXmppStatus(holder.imgXMPPStatus, user.getXmppStatus());
                    break;
                }
            }
        }
        return rowView;
    }

    public class Holder {
        public ImageView imgAvatar;
        public TextView tv_user_name;
        public ImageView imgXMPPStatus;
    }

    public boolean hasStableIds() {
        return true;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private void showXmppStatus(ImageView imageView, String statusStr) {
        imageView.setVisibility(View.VISIBLE);
        if (statusStr!= null && statusStr.length() > 0) {
            if (statusStr.equals(RUser.XMPPStatus.offline.toString())) {
                int id = context.getResources().getIdentifier("xmppstatus_offline", "drawable", context.getPackageName());
                imageView.setImageResource(id);
            } else if (statusStr.equals(RUser.XMPPStatus.online.toString())){
                int id = context.getResources().getIdentifier("xmppstatus_online", "drawable", context.getPackageName());
                imageView.setImageResource(id);
            } else if (statusStr.equals(RUser.XMPPStatus.away.toString())){
                int id = context.getResources().getIdentifier("xmppstatus_away", "drawable", context.getPackageName());
                imageView.setImageResource(id);
            } else if (statusStr.equals(RUser.XMPPStatus.dnd.toString())){
                int id = context.getResources().getIdentifier("xmppstatus_dnd", "drawable", context.getPackageName());
                imageView.setImageResource(id);
            }
        }
    }
}
