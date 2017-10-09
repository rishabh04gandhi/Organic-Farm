package versionone.codistro.github.io.organicfarm.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import versionone.codistro.github.io.organicfarm.R;
import versionone.codistro.github.io.organicfarm.utilityclasses.ParentClass;

public class AdminPanel extends AppCompatActivity {

    private Button register;
    private Button updatePriceButton;
    private Button viewDetailButton;
    private Button generateQrButton;
    public static final String FILE = "auth";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);
        ActionBar bar = getSupportActionBar();
        bar.setTitle("Admin Panel");

        register = (Button) findViewById(R.id.register);
        updatePriceButton = (Button) findViewById(R.id.update_price);
        viewDetailButton = (Button) findViewById(R.id.view_details);
        generateQrButton = (Button) findViewById(R.id.generate);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminPanel.this,RegisterCustomer.class));
            }
        });

        updatePriceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminPanel.this,UpdatePrice.class));
            }
        });

        viewDetailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(AdminPanel.this,CustomersList.class);
                ParentClass.parent = "details";
                startActivity(in);
            }
        });

        generateQrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(AdminPanel.this,CustomersList.class);
                ParentClass.parent = "generateQr";
                startActivity(in);
            }
        });

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
        Intent in = new Intent(AdminPanel.this,LogIn.class);
        startActivity(in);
        return true;
    }
}
