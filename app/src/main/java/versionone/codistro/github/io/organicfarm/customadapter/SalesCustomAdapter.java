package versionone.codistro.github.io.organicfarm.customadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import versionone.codistro.github.io.organicfarm.R;
import versionone.codistro.github.io.organicfarm.utilityclasses.Customer;
import versionone.codistro.github.io.organicfarm.utilityclasses.Sales;

/**
 * Created by RAJAN on 10-10-2017.
 */

public class SalesCustomAdapter extends ArrayAdapter<Sales> {

    public SalesCustomAdapter(Context context, ArrayList<Sales> sales) {
        super(context,0, sales);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Sales sales = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.sales_list_item,parent,false);
        }

        TextView date = (TextView) convertView.findViewById(R.id.date);
        TextView quantity = (TextView) convertView.findViewById(R.id.quantity);
        TextView paid = (TextView) convertView.findViewById(R.id.paid);
        TextView type = (TextView) convertView.findViewById(R.id.type);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(sales.getDate());
        SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy");
        String formatted = format1.format(calendar.getTime());

        date.setText(formatted);
        quantity.setText(Double.valueOf(sales.getQuantity()).toString()+"L");
        paid.setText("Paid:"+Double.valueOf(sales.getPaid()).toString());
        type.setText(sales.getType());

        return convertView;
    }
}
