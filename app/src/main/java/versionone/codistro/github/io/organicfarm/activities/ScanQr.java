package versionone.codistro.github.io.organicfarm.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import versionone.codistro.github.io.organicfarm.R;
import versionone.codistro.github.io.organicfarm.utilityclasses.ParentClass;

public class ScanQr extends AppCompatActivity {

    private Button scanButton;
    public static final String FILE = "auth";
    public static final String USERNAME = "username";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qr);
        ActionBar bar = getSupportActionBar();
        bar.setTitle("Scan QrCode");

        scanButton = (Button) findViewById(R.id.scan);

        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(ScanQr.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences auth = getSharedPreferences(FILE,MODE_PRIVATE);
        if(auth.getString(USERNAME,null) == null) {
            Intent in = new Intent(ScanQr.this, LogIn.class);
            startActivity(in);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(result != null){
            if(result.getContents() == null)
                Toast.makeText(getApplicationContext(),"Something Went Wrong",Toast.LENGTH_SHORT).show();
            else {
                if(extractData(result.getContents())) {
                    Intent in = new Intent(ScanQr.this, SelectMilkType.class);
                    startActivity(in);
                }
                else{
                    Toast.makeText(getApplicationContext(),"Invalid Qr code",Toast.LENGTH_SHORT).show();
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data);

    }

    //extracting data from QR and starting new Activity
    private boolean extractData(String contents) {
            String[] parts = contents.split(",");
            if(parts.length != 2)
                return false;
            ParentClass.id = parts[0];
            ParentClass.name = parts[1];
            if(ParentClass.id.charAt(0) == '-')
                return true;
            else
                return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.logout,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        SharedPreferences auth = getSharedPreferences(FILE,MODE_PRIVATE);
        SharedPreferences.Editor editor = auth.edit();
        editor.clear();
        editor.apply();
        Intent in = new Intent(ScanQr.this,LogIn.class);
        startActivity(in);
        return true;
    }
}
