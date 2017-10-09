package versionone.codistro.github.io.organicfarm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by RAJAN on 04-10-2017.
 */

public class CustomersListAdapter extends ArrayAdapter<Customer> {

    public CustomersListAdapter(Context context, ArrayList<Customer> customers) {
        super(context,0, customers);
    }


    @Override
    public View getView(int position,View convertView,ViewGroup parent) {
        Customer customer = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.customer_list_item,parent,false);
        }

        TextView name = (TextView) convertView.findViewById(R.id.list_name);
        TextView mobile = (TextView) convertView.findViewById(R.id.list_mobile);

        name.setText(customer.getName());
        mobile.setText(customer.getMobile());

        return convertView;
    }
}
