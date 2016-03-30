package antbuddy.htk.com.antbuddy2016.module.login.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import antbuddy.htk.com.antbuddy2016.R;
import antbuddy.htk.com.antbuddy2016.module.login.activities.DomainActivity;
import antbuddy.htk.com.antbuddy2016.objects.Domain;
import antbuddy.htk.com.antbuddy2016.util.LogHtk;

/**
 * Created by thanhnguyen on 29/03/2016.
 */
public class DomainAdapter extends ArrayAdapter<Domain> {
    DomainActivity context = null;
    ArrayList<Domain> myArray = null;
    int layoutId;

    public DomainAdapter(DomainActivity context, int layoutId, ArrayList<Domain> arr){
        super(context, layoutId, arr);
        this.context = context;
        this.layoutId = layoutId;
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
        LayoutInflater inflater = context.getLayoutInflater();
        convertView = inflater.inflate(layoutId, null);

        if (myArray.size() > 0 && position >= 0) {
            Domain domainItem = myArray.get(position);
            TextView name_TextView = (TextView) convertView.findViewById(R.id.name_TextView);
            name_TextView.setText(domainItem.getName());
        }
        return convertView;
    }
}
