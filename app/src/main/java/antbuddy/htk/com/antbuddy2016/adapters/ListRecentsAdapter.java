package antbuddy.htk.com.antbuddy2016.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import antbuddy.htk.com.antbuddy2016.R;
import antbuddy.htk.com.antbuddy2016.model.ObjectManager;
import antbuddy.htk.com.antbuddy2016.model.OpeningChatRoom;
import antbuddy.htk.com.antbuddy2016.model.Room;
import antbuddy.htk.com.antbuddy2016.model.User;
import antbuddy.htk.com.antbuddy2016.model.UserMe;
import antbuddy.htk.com.antbuddy2016.util.LogHtk;
import antbuddy.htk.com.antbuddy2016.util.RoundedTransformation;
import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by Micky on 3/31/2016.
 */
public class ListRecentsAdapter extends BaseExpandableListAdapter {
    private final int GROUPS_POSITION = 0;
    private final int MEMBERS_POSITION = 1;
    private Context context;
    private List<String> parents;
    private List<List<OpeningChatRoom>> childers;
    private LayoutInflater inflater;

    public ListRecentsAdapter(Context context,
                              ArrayList<String> parents,
                              List<List<OpeningChatRoom>> childers) {
        this.context = context;
        this.parents = parents;
        this.childers = childers;
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
        List<OpeningChatRoom> parent = childers.get(groupPosition);
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
            holder.ic_status = (ImageView) rowView.findViewById(R.id.ic_status);
            rowView.setTag(holder);
        } else {
            rowView = convertView;
            holder = (Holder) rowView.getTag();
        }

        final OpeningChatRoom openingChatroom = (OpeningChatRoom) getChild(groupPosition, childPosition);
        if (groupPosition == 0) {
            holder.ic_status.setVisibility(View.GONE);
            for (Room room : ObjectManager.getInstance().getListRooms()) {
                if (room.getKey().equals(openingChatroom.getChatRoomKey())) {
                    if (room.getIsPublic()) {
                        Glide.with(context).load(R.drawable.ic_global).override(30, 30).into(holder.imgAvatar);
                    } else {
                        Glide.with(context).load(R.drawable.ic_lock).override(30, 30).into(holder.imgAvatar);
                    }
                    holder.tv_user_name.setText(room.getName());
                    break;
                }
            }
        } else {
            holder.ic_status.setVisibility(View.VISIBLE);
            for (User user : ObjectManager.getInstance().getListUsers()) {
                if (user.getKey().equals(openingChatroom.getChatRoomKey())) {
                    LogHtk.d(LogHtk.Test1, "user = " + user.getUsername());
//                    Picasso.with(context).load(user.getAvatar()).
//                            resize(60, 60).error(R.drawable.ic_avatar_defaul).transform(new RoundedTransformation(5, 5)).
//                            into(holder.imgAvatar);

                    Glide.with(context)
                            .load(user.getAvatar())
                            .override(60, 60)
                            .placeholder(R.drawable.ic_avatar_defaul)
                            .bitmapTransform(new CropCircleTransformation(context))
                            .into(holder.imgAvatar);

                    holder.tv_user_name.setText(user.getName());
                    break;
                }
            }
        }
        return rowView;
    }

    public class Holder {
        public ImageView imgAvatar;
        public TextView tv_user_name;
        public ImageView ic_status;
    }

    public boolean hasStableIds() {
        return true;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
