package versionone.codistro.github.io.organicfarm;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AdminPanel extends AppCompatActivity {

    private Button register;
    private Button updatePriceButton;
    private Button viewDetailButton;
    private Button generateQrButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);
        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);
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
}
