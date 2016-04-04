package antbuddy.htk.com.antbuddy2016.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import antbuddy.htk.com.antbuddy2016.R;
import antbuddy.htk.com.antbuddy2016.model.RecentData;
import antbuddy.htk.com.antbuddy2016.util.RoundedTransformation;

/**
 * Created by Micky on 3/31/2016.
 */
public class ListRecentsAdapter extends BaseExpandableListAdapter {
    private  final int GROUPS_POSITION = 0;
    private  final int MEMBERS_POSITION = 1;
    private Context context;
    private ArrayList<String> parents;
    private ArrayList<ArrayList<RecentData>> childers;
    private LayoutInflater inflater;

    public ListRecentsAdapter(Context context,
                              ArrayList<String> parents,
                              ArrayList<ArrayList<RecentData>> childers) {
        this.context = context;
        this.parents = parents;
        this.childers = childers;
        inflater = LayoutInflater.from( context );
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
        ArrayList<RecentData> parent = childers.get(groupPosition);
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
        View v = null;
        if( convertView != null )
            v = convertView;
        else
            v = inflater.inflate(R.layout.parent_view, parent, false);
        String gt = (String)getGroup(groupPosition);
        TextView tv_title = (TextView)v.findViewById(R.id.tv_title);
        if( tv_title != null )
            tv_title.setText(parents.get(groupPosition));
        return v;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View rowView = null;
        final Holder holder;

        if( convertView == null && groupPosition == GROUPS_POSITION) {
            holder = new Holder();
            rowView = inflater.inflate(R.layout.row_member, parent, false);
            holder.imgAvatar = (ImageView)rowView.findViewById(R.id.imgAvatar);
            rowView.setTag(holder);
        } else if( convertView == null && groupPosition == MEMBERS_POSITION) {
            holder = new Holder();
            rowView = inflater.inflate(R.layout.row_member, parent, false);
            holder.imgAvatar = (ImageView)rowView.findViewById(R.id.imgAvatar);
            rowView.setTag(holder);
        } else {
            rowView = convertView;
            holder = (Holder) rowView.getTag();
        }

//        Picasso.with(context).load("https://abs1.antbuddy.com/antbuddy-bucket/1455784435927_avatar.png").
//                resize(60, 60).
////                error(R.drawable.empty_avatar).
//        transform(new RoundedTransformation(5, 5)).
//                into(holder.imgAvatar);

        return rowView;
    }

    public class Holder {
        public ImageView imgAvatar;
    }

    public boolean hasStableIds() {
        return true;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
