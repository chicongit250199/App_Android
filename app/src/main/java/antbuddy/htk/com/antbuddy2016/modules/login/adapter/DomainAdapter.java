package antbuddy.htk.com.antbuddy2016.modules.login.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import antbuddy.htk.com.antbuddy2016.R;
import antbuddy.htk.com.antbuddy2016.model.Organization;
import antbuddy.htk.com.antbuddy2016.modules.login.activities.DomainActivity;

/**
 * Created by thanhnguyen on 29/03/2016.
 */
public class DomainAdapter extends ArrayAdapter<Organization> {
    DomainActivity context = null;
    List<Organization> listOrganizations = null;

    public DomainAdapter(DomainActivity context, List<Organization> listOrganizations){
        super(context, R.layout.item_domain);
        this.context = context;
        this.listOrganizations = listOrganizations;
    }

    @Override
    public int getCount() {
        return listOrganizations.size();
    }

    @Override
    public Organization getItem(int position) {
        return listOrganizations.get(position);
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

        Organization orgItem = listOrganizations.get(position);
        holder.name_TextView.setText(orgItem.getName());
        return rowView;
    }

    public class Holder {
        public TextView name_TextView;
    }
}

