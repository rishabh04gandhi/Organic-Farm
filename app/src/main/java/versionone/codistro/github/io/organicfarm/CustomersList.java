package versionone.codistro.github.io.organicfarm;

import android.*;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import static com.google.zxing.integration.android.IntentIntegrator.REQUEST_CODE;

public class CustomersList extends AppCompatActivity {

    private ListView customersListView;
    private ArrayList<Customer> customersList;
    private ArrayList<String> customerIds;
    private DatabaseReference reference;
    public static String customerId;
    public static String customerName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customers_list);
        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setTitle("Select Customer");

        customersListView = (ListView)findViewById(R.id.customers_list);
        customersList = new ArrayList<>();
        customerIds = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Customers");

        //Asking for permission for sdfVersion > 23
        if (Build.VERSION.SDK_INT >= 23) {
            ActivityCompat.requestPermissions(CustomersList.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
        }

        final CustomersListAdapter adapter = new CustomersListAdapter(CustomersList.this,customersList);
        customersListView.setAdapter(adapter);

        customersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Customer c = customersList.get(position);
                customerId = c.getId();
                customerName = c.getName();
                Intent in = getIntent();
                String parentActivity = ParentClass.parent;
                if(parentActivity.equals("details")){
                    startActivity(new Intent(CustomersList.this,CustomerBalanceDetails.class));
                }else {

                    generateQr(c);
                }
            }
        });

        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Customer c = dataSnapshot.getValue(Customer.class);
                customersList.add(c);
                customerIds.add(c.getId());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Customer c = dataSnapshot.getValue(Customer.class);
                String key = c.getId();
                int indexOfKey = customerIds.indexOf(key);
                customersList.set(indexOfKey,c);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Customer c = dataSnapshot.getValue(Customer.class);
                String key = c.getId();
                int indexOfKey = customerIds.indexOf(key);
                customersList.remove(indexOfKey);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    //genrating
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void generateQr(Customer c) {
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        String info = c.getId()+","+c.getName();
        BitMatrix bitMatrix = null;
        try {
            bitMatrix = multiFormatWriter.encode(info, BarcodeFormat.QR_CODE, 400, 400);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            saveToDownloads(bitmap,c);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    //method to save the Qr generate in Downloads folder
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void saveToDownloads(Bitmap bitmap,Customer c) {
        FileOutputStream fos = null;
        String fileName = c.getName()+"_"+c.getMobile()+".jpg";
        try {
            if (ActivityCompat.checkSelfPermission(CustomersList.this,android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName);
                fos = new FileOutputStream(file);
                // Use the compress method on the BitMap object to write image to the OutputStream
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                Toast.makeText(getApplicationContext(),"Saved to downloads folder", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
