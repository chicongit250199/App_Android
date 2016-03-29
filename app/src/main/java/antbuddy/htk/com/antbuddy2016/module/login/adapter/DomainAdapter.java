package antbuddy.htk.com.antbuddy2016.module.login.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

import antbuddy.htk.com.antbuddy2016.module.login.activities.DomainActivity;
import antbuddy.htk.com.antbuddy2016.objects.Domain;

/**
 * Created by thanhnguyen on 29/03/2016.
 */
public class DomainAdapter extends ArrayAdapter<Domain> {
    DomainActivity context=null;
    ArrayList<Domain> myArray=null;
    int layoutId;

    public DomainAdapter(DomainActivity context, int layoutId, ArrayList<Domain> arr){
        super(context, layoutId, arr);
        this.context=context;
        this.layoutId=layoutId;
        this.myArray=arr;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        convertView=inflater.inflate(layoutId, null);

        if(myArray.size()>0 && position>=0)
        {
//            //dòng lệnh lấy TextView ra để hiển thị Mã và tên lên
//            final TextView txtdisplay=(TextView)
//                    convertView.findViewById(R.id.txtitem);
//            //lấy ra nhân viên thứ position
//            final Employee emp=myArray.get(position);
//            //đưa thông tin lên TextView
//            //emp.toString() sẽ trả về Id và Name
//            txtdisplay.setText(emp.toString());
//            //lấy ImageView ra để thiết lập hình ảnh cho đúng
//            final ImageView imgitem=(ImageView)
//                    convertView.findViewById(R.id.imgitem);
//            //nếu là Nữ thì lấy hình con gái
//            if(emp.isGender())
//                imgitem.setImageResource(R.drawable.girlicon);
//            else//nếu là Nam thì lấy hình con trai
//                imgitem.setImageResource(R.drawable.boyicon );
        }
        return convertView;
    }
}
