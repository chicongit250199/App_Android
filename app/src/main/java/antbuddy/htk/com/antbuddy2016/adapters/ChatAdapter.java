package antbuddy.htk.com.antbuddy2016.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import antbuddy.htk.com.antbuddy2016.R;
import antbuddy.htk.com.antbuddy2016.model.ChatMessage;

/**
 * Created by Micky on 4/1/2016.
 */
public class ChatAdapter extends ArrayAdapter<ChatMessage> {
    private final Context ctx;
    private final ListView mListView;

    public ChatAdapter(Context context, ListView listView) {
        super(context, R.layout.row_chat_message);
        this.ctx = context;
//        sizeAvatar = getContext().getResources().getDimensionPixelSize(R.dimen.dp60);
        mListView = listView;
//        bitmapIconDefault = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.empty_avatar);
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
