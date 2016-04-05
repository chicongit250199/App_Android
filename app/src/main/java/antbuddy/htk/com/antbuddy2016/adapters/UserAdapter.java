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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import antbuddy.htk.com.antbuddy2016.R;
import antbuddy.htk.com.antbuddy2016.model.User;
import antbuddy.htk.com.antbuddy2016.util.RoundedTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by Micky on 4/1/2016.
 */
public class UserAdapter extends ArrayAdapter<User> {
    private final Context ctx;
    private final ListView mGridView;
    private ArrayList<User> listFilterUsers = new ArrayList<>();

    public UserAdapter(Context context, ListView listView, List<User> listUsers) {
        super(context, R.layout.row_member);
        this.ctx = context;
        mGridView = listView;
        filter("", new ArrayList<User>());
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
            holder.tv_user_name = (TextView)rowView.findViewById(R.id.tv_user_name);
            rowView.setTag(holder);
        } else {
            rowView = convertView;
            holder = (Holder) rowView.getTag();
        }

        User user = listFilterUsers.get(position);
        Glide.with(ctx).load(user.getAvatar()).override(60, 60).
                bitmapTransform(new CropCircleTransformation(ctx))
                .into(holder.imgAvatar);
        holder.tv_user_name.setText(user.getName());
        return rowView;
    }

    public class Holder {
        public ImageView imgAvatar;
        public TextView tv_user_name;
    }

    @Override
    public int getCount() {
        return listFilterUsers.size();
    }

    // Filter Class
    public void filter(String charText, List<User> listUsers) {
        charText = charText.toLowerCase(Locale.getDefault());
        listFilterUsers.clear();
        if (charText.length() == 0) {
            listFilterUsers.addAll(listUsers);
        }
        else
        {
            for (User user : listUsers)
            {
                if (user.getName().toLowerCase(Locale.getDefault()).contains(charText))
                {
                    listFilterUsers.add(user);
                }
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public User getItem(int position) {
        return listFilterUsers.get(position);
    }
}
