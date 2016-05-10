package antbuddy.htk.com.antbuddy2016.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import antbuddy.htk.com.antbuddy2016.R;
import antbuddy.htk.com.antbuddy2016.RealmObjects.RUser;
import antbuddy.htk.com.antbuddy2016.util.LogHtk;
import io.realm.RealmResults;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by Micky on 4/1/2016.
 */
public class UserAdapter extends ArrayAdapter<RUser> {
    private final Context ctx;
    private final ListView mGridView;
    private RealmResults<RUser> users;
//    private RealmResults<RUser> listFilterUsers;

    public UserAdapter(Context context, ListView listView, RealmResults<RUser> listUsers) {
        super(context, R.layout.row_member);
        this.ctx = context;
        mGridView = listView;
        this.users = listUsers;
//        filter("", new RealmResults<RUser>());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Holder holder;
        final View rowView;
        LayoutInflater vi;

        if (convertView == null) {
            holder = new Holder();
            vi = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = vi.inflate(R.layout.row_member, null);
            holder.imgAvatar = (ImageView)rowView.findViewById(R.id.imgAvatar);
            holder.imgXMPPStatus = (ImageView)rowView.findViewById(R.id.imgXMPPStatus);
            holder.tv_user_name = (TextView)rowView.findViewById(R.id.tv_user_name);
            rowView.setTag(holder);
        } else {
            rowView = convertView;
            holder = (Holder) rowView.getTag();
        }

        RUser user = users.get(position);
        Glide.with(ctx)
                .load(user.getAvatar())
                .override(60, 60)
                .bitmapTransform(new CropCircleTransformation(ctx))
                .placeholder(R.drawable.ic_avatar_defaul)
                .error(R.drawable.ic_avatar_defaul)
                .into(holder.imgAvatar);
        holder.tv_user_name.setText(user.getName());

        showXmppStatus(holder.imgXMPPStatus, user.getXmppStatus());
        return rowView;
    }

    public class Holder {
        public ImageView imgAvatar;
        public ImageView imgXMPPStatus;
        public TextView tv_user_name;
    }

    @Override
    public int getCount() {
        return users.size();
    }

    // Filter Class
    public void filter(String charText, RealmResults<RUser> listUsers) {
//        charText = charText.toLowerCase(Locale.getDefault());
//        listFilterUsers.clear();
//        if (charText.length() == 0) {
//            listFilterUsers.addAll(listUsers);
//        }
//        else
//        {
//            for (User user : listUsers)
//            {
//                if (user.getName().toLowerCase(Locale.getDefault()).contains(charText))
//                {
//                    listFilterUsers.add(user);
//                }
//            }
//        }
//        notifyDataSetChanged();
    }

    @Override
    public RUser getItem(int position) {
        return users.get(position);
    }

    private void showXmppStatus(ImageView imageView, String statusStr) {
        imageView.setVisibility(View.VISIBLE);
        if (statusStr!= null && statusStr.length() > 0) {

            if (statusStr.equals(RUser.XMPPStatus.offline.toString())) {
                int id = ctx.getResources().getIdentifier("xmppstatus_offline", "drawable", ctx.getPackageName());
                imageView.setImageResource(id);
            } else if (statusStr.equals(RUser.XMPPStatus.online.toString())){
                int id = ctx.getResources().getIdentifier("xmppstatus_online", "drawable", ctx.getPackageName());
                imageView.setImageResource(id);
            } else if (statusStr.equals(RUser.XMPPStatus.away.toString())){
                int id = ctx.getResources().getIdentifier("xmppstatus_away", "drawable", ctx.getPackageName());
                imageView.setImageResource(id);
            } else if (statusStr.equals(RUser.XMPPStatus.dnd.toString())){
                int id = ctx.getResources().getIdentifier("xmppstatus_dnd", "drawable", ctx.getPackageName());
                imageView.setImageResource(id);
            }
        }
    }
}
