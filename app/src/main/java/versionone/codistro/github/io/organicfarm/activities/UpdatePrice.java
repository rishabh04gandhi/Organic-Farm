package versionone.codistro.github.io.organicfarm.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.style.UpdateAppearance;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import versionone.codistro.github.io.organicfarm.R;
import versionone.codistro.github.io.organicfarm.utilityclasses.Price;

public class UpdatePrice extends AppCompatActivity {

    private EditText cowMilkPriceEditText, buffaloMilkPriceEditText;
    private Button updateButton;
    private DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_price);
        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setTitle("Update Price");

        //checking if internet is connected
        if(!isInternetConnected(UpdatePrice.this)){
            //AlertDialog to show no internet the action
            AlertDialog.Builder alert = new AlertDialog.Builder(UpdatePrice.this);
            alert.setMessage(R.string.no_internet);
            alert.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent in = new Intent(UpdatePrice.this,AdminPanel.class);
                    startActivity(in);
                }
            });
            alert.show();
        }

        cowMilkPriceEditText = (EditText) findViewById(R.id.cow_milk_price);
        buffaloMilkPriceEditText = (EditText) findViewById(R.id.buffalo_milk_price);
        updateButton = (Button) findViewById(R.id.update_price);

        reference = FirebaseDatabase.getInstance().getReference("Price");

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Query query = ref.child("Price");

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Price p = dataSnapshot.getValue(Price.class);
                cowMilkPriceEditText.setText(Double.valueOf(p.getCowMilkPrice()).toString());
                buffaloMilkPriceEditText.setText(Double.valueOf(p.getBuffaloMilkPrice()).toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //AlertDialog to confirm the action
                AlertDialog.Builder alert = new AlertDialog.Builder(UpdatePrice.this);
                alert.setTitle("Confirm Action");
                alert.setMessage("Are you sure?");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //calling utility function updates price in database
                        updatePrice();
                    }
                });
                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(UpdatePrice.this,"Action Cancelled",Toast.LENGTH_SHORT).show();
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

    private void updatePrice() {
        String cowMilkPrice = cowMilkPriceEditText.getText().toString().trim();
        String buffaloMilkPrice = buffaloMilkPriceEditText.getText().toString().trim();

        if(!TextUtils.isEmpty(cowMilkPrice) && !TextUtils.isEmpty(buffaloMilkPrice)){
            Double cow = Double.parseDouble(cowMilkPrice);
            Double buffalo = Double.parseDouble(buffaloMilkPrice);

            Price price = new Price(cow,buffalo);
            reference.setValue(price);
            Toast.makeText(getApplicationContext(),"Price Updated",Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getApplicationContext(),"Invalid Price",Toast.LENGTH_SHORT).show();
        }
    }
}
