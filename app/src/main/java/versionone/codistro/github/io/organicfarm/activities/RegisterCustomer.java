package versionone.codistro.github.io.organicfarm.activities;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import versionone.codistro.github.io.organicfarm.R;
import versionone.codistro.github.io.organicfarm.utilityclasses.Customer;

public class RegisterCustomer extends AppCompatActivity {

    private EditText nameEditText, mobileEditText;
    private Button registerButton;
    private DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_customer);
        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setTitle("Register Customer");

        //registering views
        nameEditText = (EditText)findViewById(R.id.customer_name);
        mobileEditText = (EditText)findViewById(R.id.customer_mobile);
        registerButton = (Button)findViewById(R.id.register_button);


        //getting reference to customers in firebase database
        reference = FirebaseDatabase.getInstance().getReference("Customers");

        //adding click listener to button
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //calling utility function that registers cutomers in database
                registerCustomer();
            }
        });

    }

    private void registerCustomer() {

        //getting data from text fields
        String name = nameEditText.getText().toString().trim();
        String mobile = mobileEditText.getText().toString().trim();

        if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(mobile)){
            String id = reference.push().getKey().toString();
            Customer customer = new Customer(id,name,mobile);
            reference.child(id).setValue(customer);
            Toast.makeText(RegisterCustomer.this,"Customer Registered",Toast.LENGTH_SHORT).show();

            //setting text field empty
            nameEditText.setText("");
            mobileEditText.setText("");
        }else {
            Toast.makeText(RegisterCustomer.this,"Invalid Data",Toast.LENGTH_SHORT).show();
        }
    }
}
