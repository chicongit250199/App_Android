package antbuddy.htk.com.antbuddy2016.modules.login.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import antbuddy.htk.com.antbuddy2016.R;
import antbuddy.htk.com.antbuddy2016.modules.login.activities.DomainActivity;
import antbuddy.htk.com.antbuddy2016.objects.Domain;

/**
 * Created by thanhnguyen on 29/03/2016.
 */
public class DomainAdapter extends ArrayAdapter<Domain> {
    DomainActivity context = null;
    ArrayList<Domain> myArray = null;

    public DomainAdapter(DomainActivity context, ArrayList<Domain> arr){
        super(context, R.layout.item_domain);
        this.context = context;
        this.myArray = arr;
    }

    @Override
    public int getCount() {
        return myArray.size();
    }

    @Override
    public Domain getItem(int position) {
        return myArray.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Holder holder;
        final View rowView;
        LayoutInflater vi;

        if (convertView == null) {
            holder = new Holder();
            vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = vi.inflate(R.layout.item_domain, null);

            holder.name_TextView = (TextView) rowView.findViewById(R.id.name_TextView);
            rowView.setTag(holder);
        } else {
            rowView = convertView;
            holder = (Holder) rowView.getTag();
        }

        Domain domainItem = myArray.get(position);
        holder.name_TextView.setText(domainItem.getName());
        return rowView;
    }

    public class Holder {
        public TextView name_TextView;
    }
}

