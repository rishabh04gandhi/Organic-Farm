package versionone.codistro.github.io.organicfarm.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.IntegerRes;
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

import java.util.concurrent.atomic.DoubleAccumulator;

import versionone.codistro.github.io.organicfarm.R;
import versionone.codistro.github.io.organicfarm.utilityclasses.Balance;
import versionone.codistro.github.io.organicfarm.utilityclasses.ParentClass;
import versionone.codistro.github.io.organicfarm.utilityclasses.Price;
import versionone.codistro.github.io.organicfarm.utilityclasses.Sales;

public class SaleDetails extends AppCompatActivity {

    private String id, name, milkType;
    private TextView nameTextView, milkTypeTextView;
    private EditText quantityEditText, paidEditText;
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
        bar.setTitle(R.string.enter_quntity);

        //getting info from previos activity about type of milk
        Intent in = getIntent();
        id = ParentClass.id;
        name = ParentClass.name;
        milkType = in.getStringExtra("type");


        //linking the views
        nameTextView = (TextView) findViewById(R.id.customer_name);
        quantityEditText = (EditText) findViewById(R.id.quantity);
        deliverButton = (Button) findViewById(R.id.deliver);
        milkTypeTextView = (TextView) findViewById(R.id.milk_type);
        paidEditText = (EditText) findViewById(R.id.paid_rupee);

        //getting database references
        referenceSales = FirebaseDatabase.getInstance().getReference("Sales");
        referencePrice = FirebaseDatabase.getInstance().getReference();
        referenceBalance = FirebaseDatabase.getInstance().getReference("Balance");

        //getting the price of the milk from the database
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

        //getting previous balance of the customer from the database
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

        //setting name and milk type to the views
        nameTextView.setText(name);
        milkTypeTextView.setText(milkType);

        //on clik for the delivery button
        deliverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //AlertDialog to confirm the action
                AlertDialog.Builder alert = new AlertDialog.Builder(SaleDetails.this);
                alert.setTitle(R.string.confirm);
                alert.setMessage("Are you sure?");
                alert.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(isInternetConnected(SaleDetails.this)) {
                            //calling utility function that updates the sales in database
                            deliverMilk();
                        }
                        else{
                            Toast.makeText(SaleDetails.this,R.string.no_internet,Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                alert.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(SaleDetails.this,R.string.action_cancelled,Toast.LENGTH_SHORT).show();
                    }
                });
                alert.show();

            }
        });
    }

    public static boolean isInternetConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    private void deliverMilk() {

        //grtting the quantity and paid rupee from the edit text
        String quantity = quantityEditText.getText().toString().trim();
        String paid = paidEditText.getText().toString().trim();

        if(TextUtils.isEmpty(quantity) || Double.valueOf(quantity) == 0)// if quantity is empty block update
            Toast.makeText(getApplicationContext(),R.string.invalid_quantity,Toast.LENGTH_SHORT).show();
        else {
            Double newBalance = null;
            Double p = null;
            Double q = Double.parseDouble(quantity);
            String newId = referenceSales.push().getKey();
            if(!TextUtils.isEmpty(paid))
                p = Double.valueOf(paid);
            else
                p = 0d;

            Sales s = new Sales(id,q,p,milkType);
            double priceRupee = 0;

            if(milkType.equals("cow"))
                priceRupee = price.getCowMilkPrice();
            else
                priceRupee = price.getBuffaloMilkPrice();

            if(balance != null) {
                double oldBalance = balance.getBalance();
                double oldTotal = balance.getTotal();

                newBalance = oldBalance + (q * priceRupee);

                //checking any money paid at delivery
                if(!TextUtils.isEmpty(paid)){

                    newBalance = newBalance - (Double.valueOf(paid));
                }

                double newTotal = oldTotal + q;

                Balance balance1 = new Balance(id,newBalance,newTotal);
                referenceBalance.child(id).setValue(balance1);

                referenceSales.child(newId).setValue(s);
                Toast.makeText(getApplicationContext(),R.string.updated, Toast.LENGTH_SHORT).show();
                Intent in = new Intent(SaleDetails.this,ScanQr.class);
                startActivity(in);
            }else{ //If no previous balance found update fresh
                newBalance = q * priceRupee;
                //checking any money paid at delivery
                if(!TextUtils.isEmpty(paid)){
                    newBalance = newBalance - (Double.valueOf(paid));
                }
                Balance balance1 = new Balance(id,newBalance,q);
                referenceBalance.child(id).setValue(balance1);

                referenceSales.child(newId).setValue(s);
                Toast.makeText(getApplicationContext(),R.string.updated, Toast.LENGTH_SHORT).show();
                Intent in = new Intent(SaleDetails.this,ScanQr.class);
                startActivity(in);
            }
        }
    }
}
