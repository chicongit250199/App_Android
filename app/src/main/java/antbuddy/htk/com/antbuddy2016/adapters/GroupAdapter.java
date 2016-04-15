package antbuddy.htk.com.antbuddy2016.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import java.util.List;

import antbuddy.htk.com.antbuddy2016.R;
import antbuddy.htk.com.antbuddy2016.RealmObjects.RRoom;
import antbuddy.htk.com.antbuddy2016.model.Room;
import antbuddy.htk.com.antbuddy2016.util.LogHtk;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by Micky on 4/1/2016.
 */
public class GroupAdapter  extends ArrayAdapter<Room> {
    
    private final Context ctx;
    private final GridView roomGridView;
    private RealmResults<RRoom> rooms;

    public GroupAdapter(Context context, GridView roomGridView, RealmResults<RRoom> listRooms) {
        super(context, R.layout.grid_item);
        this.ctx = context;
        this.roomGridView = roomGridView;
        this.rooms = listRooms;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Holder holder;
        final View rowView;
        LayoutInflater vi;

        if (convertView == null) {
            holder = new Holder();
            vi = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = vi.inflate(R.layout.grid_item, null);

            holder.tv_NameRoom = (TextView) rowView.findViewById(R.id.tv_NameRoom);

            rowView.setTag(holder);
        } else {
            rowView = convertView;
            holder = (Holder) rowView.getTag();
        }

        holder.tv_NameRoom.setText(rooms.get(position).getName());

        return rowView;
    }

    public class Holder {
        TextView tv_NameRoom;
    }

    @Override
    public int getCount() {
        return rooms.size();
    }

    public RealmResults<RRoom> getListRooms() {
        return rooms;
    }

    public void setListRooms(RealmResults<RRoom> listRooms) {
        this.rooms = listRooms;
    }

}
