package antbuddy.htk.com.antbuddy2016.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import antbuddy.htk.com.antbuddy2016.R;
import antbuddy.htk.com.antbuddy2016.model.Room;

/**
 * Created by Micky on 4/1/2016.
 */
public class GroupAdapter  extends ArrayAdapter<Room> {
    
    private final Context ctx;
    private final GridView mGridView;
    
    public GroupAdapter(Context context, GridView gridView) {
        super(context, R.layout.grid_item);
        this.ctx = context;
        mGridView = gridView;
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
            rowView.setTag(holder);
        } else {
            rowView = convertView;
            holder = (Holder) rowView.getTag();
        }
        return rowView;
    }

    public class Holder {
    }

    @Override
    public int getCount() {
        return 10;
    }

}
