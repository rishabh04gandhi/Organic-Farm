package versionone.codistro.github.io.organicfarm.activities;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import versionone.codistro.github.io.organicfarm.R;
import versionone.codistro.github.io.organicfarm.utilityclasses.Balance;

public class CustomerBalanceDetails extends AppCompatActivity {

    private TextView customerNameTextView, balanceTextView, totalTextView;
    private DatabaseReference reference;
    private String id, name;
    private Balance balance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_balance_details);
        final ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setTitle("Customer Balance Details");

        customerNameTextView = (TextView) findViewById(R.id.customer_name);
        balanceTextView = (TextView) findViewById(R.id.balance);
        totalTextView = (TextView) findViewById(R.id.total);

        reference = FirebaseDatabase.getInstance().getReference("Balance");

        id = CustomersList.customerId;
        name = CustomersList.customerName;

        customerNameTextView.setText(name);
        balanceTextView.setText("0");
        totalTextView.setText("0");
        Query query = reference.orderByChild("id").equalTo(id);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren())
                    balance = data.getValue(Balance.class);
                if(balance != null){
                    Double b = balance.getBalance();
                    Double t = balance.getTotal();
                    balanceTextView.setText("Rs. "+b.toString());
                    totalTextView.setText(t.toString()+" Kg");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}
