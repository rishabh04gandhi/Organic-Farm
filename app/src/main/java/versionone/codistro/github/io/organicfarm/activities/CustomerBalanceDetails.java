package versionone.codistro.github.io.organicfarm.activities;

import android.app.DatePickerDialog;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

import versionone.codistro.github.io.organicfarm.R;
import versionone.codistro.github.io.organicfarm.customadapter.SalesCustomAdapter;
import versionone.codistro.github.io.organicfarm.utilityclasses.Balance;
import versionone.codistro.github.io.organicfarm.utilityclasses.Sales;

public class CustomerBalanceDetails extends AppCompatActivity {

    private EditText fromEditText, toEditText;
    private TextView customerNameTextView, balanceTextView;
    private Button detailsButton;
    private Long fromTimeStamp, toTimeStamp;
    private DatabaseReference referenceSales,referenceBalance;
    private String id, name;
    private Sales sales;
    private ListView customersListView;
    private ArrayList<Sales> salesArrayList;
    private SalesCustomAdapter adapter;
    private Balance balance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_balance_details);
        final ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setTitle("Customer Balance Details");

        fromEditText = (EditText) findViewById(R.id.from);
        toEditText = (EditText) findViewById(R.id.to);
        customerNameTextView = (TextView) findViewById(R.id.customer_name);
        customersListView = (ListView) findViewById(R.id.customers_list);
        detailsButton = (Button) findViewById(R.id.details);
        balanceTextView = (TextView) findViewById(R.id.balance);
        salesArrayList = new ArrayList<>();
        adapter = new SalesCustomAdapter(CustomerBalanceDetails.this,salesArrayList);
        customersListView.setAdapter(adapter);
        id = CustomersList.customerId;
        name = CustomersList.customerName;

        //setting default dates to views
        defaultDates();

        customerNameTextView.setText(name);

        referenceBalance = FirebaseDatabase.getInstance().getReference("Balance");

        Query queryBalance = referenceBalance.orderByChild("id").equalTo(id);
        queryBalance.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren())
                    balance = data.getValue(Balance.class);
                if(balance != null) {
                    balanceTextView.setText("Balance: "+balance.getBalance().toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        fromEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initiateDatePicker(v);
            }
        });

        toEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initiateDatePicker(v);
            }
        });


        detailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displaySales();

            }
        });




    }

    private void displaySales() {
        salesArrayList.clear();
        adapter.notifyDataSetChanged();
        referenceSales = FirebaseDatabase.getInstance().getReference("Sales");

        Query query = referenceSales.orderByChild("id").equalTo(id);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    sales = data.getValue(Sales.class);
                    if (sales.getDate() >= fromTimeStamp && sales.getDate() <= toTimeStamp) {
                        salesArrayList.add(sales);
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void defaultDates() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.set(year,month,day,0,0,0);
        fromTimeStamp = calendar.getTimeInMillis();
        toTimeStamp = calendar.getTimeInMillis();
        //fromEditText.setText(Long.valueOf(fromTimeStamp).toString());
        //toEditText.setText(Long.valueOf(toTimeStamp).toString());
        month = month + 1;
        fromEditText.setText(day + "-" + month + "-" + year);
        toEditText.setText(day+"-"+month+"-"+year);
    }

    private void initiateDatePicker(final View v) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog date = new DatePickerDialog(CustomerBalanceDetails.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                calendar.set(year,month,Calendar.DAY_OF_MONTH,00,00,00);
                if(v.getId() == R.id.from) {
                    month = month + 1;
                    fromEditText.setText(dayOfMonth + "-" + month + "-" + year);
                    fromTimeStamp = calendar.getTimeInMillis();
                    //fromEditText.setText(Long.valueOf(fromTimeStamp).toString());
                }
                else{
                    month = month + 1;
                    toEditText.setText(dayOfMonth+"-"+month+"-"+year);
                    toTimeStamp = calendar.getTimeInMillis();
                }

            }
        },year,month,day);
        date.show();
    }
}
