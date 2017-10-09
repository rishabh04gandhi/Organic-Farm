package versionone.codistro.github.io.organicfarm.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import versionone.codistro.github.io.organicfarm.R;
import versionone.codistro.github.io.organicfarm.utilityclasses.Balance;
import versionone.codistro.github.io.organicfarm.utilityclasses.ParentClass;
import versionone.codistro.github.io.organicfarm.utilityclasses.Price;
import versionone.codistro.github.io.organicfarm.utilityclasses.Sales;

public class SaleDetails extends AppCompatActivity {

    private String id, name, milkType;
    private TextView nameTextView, milkTypeTextView;
    private EditText quantityEditText;
    private Button deliverButton;
    private Price price;
    private Balance balance;
    private DatabaseReference referenceSales, referencePrice, referenceBalance;
    private Query queryPrice, queryBalance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale_details);
        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setTitle("Enter Quantity");

        Intent in = getIntent();
        id = ParentClass.id;
        name = ParentClass.name;
        milkType = in.getStringExtra("type");

        nameTextView = (TextView) findViewById(R.id.customer_name);
        quantityEditText = (EditText) findViewById(R.id.quantity);
        deliverButton = (Button) findViewById(R.id.deliver);
        milkTypeTextView = (TextView) findViewById(R.id.milk_type);

        referenceSales = FirebaseDatabase.getInstance().getReference("Sales");
        referencePrice = FirebaseDatabase.getInstance().getReference();
        referenceBalance = FirebaseDatabase.getInstance().getReference("Balance");

        queryPrice = referencePrice.child("Price");

        queryPrice.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                price = dataSnapshot.getValue(Price.class);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        queryBalance = referenceBalance.orderByChild("id").equalTo(id);
        queryBalance.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren())
                    balance = data.getValue(Balance.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        nameTextView.setText(name);
        milkTypeTextView.setText(milkType);
        deliverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //AlertDialog to confirm the action
                AlertDialog.Builder alert = new AlertDialog.Builder(SaleDetails.this);
                alert.setTitle("Confirm Action");
                alert.setMessage("Are you sure?");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //calling utility function that updates the sales in database
                        deliverMilk();
                    }
                });
                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(SaleDetails.this,"Action Cancelled",Toast.LENGTH_SHORT).show();
                    }
                });
                alert.show();

            }
        });
    }

    private void deliverMilk() {

        queryBalance = referenceBalance.orderByChild("id").equalTo(id);
        queryBalance.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren())
                    balance = data.getValue(Balance.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        String quantity = quantityEditText.getText().toString().trim();
        if(TextUtils.isEmpty(quantity))
            Toast.makeText(getApplicationContext(),"Invalid Quantity",Toast.LENGTH_SHORT).show();
        else {
            Double q = Double.parseDouble(quantity);
            String newId = referenceSales.push().getKey();
            Sales s = new Sales(id,q,milkType);
            double priceRupee = 0;

            if(milkType.equals("cow"))
                priceRupee = price.getCowMilkPrice();
            else
                priceRupee = price.getBuffaloMilkPrice();

            if(balance != null) {
                double oldBalance = balance.getBalance();
                double oldTotal = balance.getTotal();

                double newBalance = oldBalance + (q * priceRupee);
                double newTotal = oldTotal + q;

                Balance balance1 = new Balance(id,newBalance,newTotal);
                referenceBalance.child(id).setValue(balance1);

                referenceSales.child(newId).setValue(s);
                Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();
            }else{
                Balance balance1 = new Balance(id,q * priceRupee,q);
                referenceBalance.child(id).setValue(balance1);

                referenceSales.child(newId).setValue(s);
                Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
