package antbuddy.htk.com.antbuddy2016.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.squareup.picasso.Picasso;

import java.util.List;

import antbuddy.htk.com.antbuddy2016.R;
import antbuddy.htk.com.antbuddy2016.model.User;
import antbuddy.htk.com.antbuddy2016.util.RoundedTransformation;

/**
 * Created by Micky on 4/1/2016.
 */
public class UserAdapter extends ArrayAdapter<User> {
    private final Context ctx;
    private final ListView mGridView;
    private List<User> listUsers;
    
    public UserAdapter(Context context, ListView listView, List<User> listUsers) {
        super(context, R.layout.row_member);
        this.ctx = context;
        mGridView = listView;
        this.listUsers = listUsers;
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
            rowView.setTag(holder);
        } else {
            rowView = convertView;
            holder = (Holder) rowView.getTag();
        }

        Picasso.with(ctx).load("https://abs1.antbuddy.com/antbuddy-bucket/1455784435927_avatar.png").
                resize(60, 60).
//                error(R.drawable.empty_avatar).
                transform(new RoundedTransformation(5, 5)).
                into(holder.imgAvatar);

        return rowView;
    }

    public class Holder {
        public ImageView imgAvatar;
    }

    @Override
    public int getCount() {
        return listUsers.size();
    }

}
