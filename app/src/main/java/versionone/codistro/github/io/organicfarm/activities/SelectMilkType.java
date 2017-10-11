package versionone.codistro.github.io.organicfarm.activities;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import versionone.codistro.github.io.organicfarm.R;

public class SelectMilkType extends AppCompatActivity {

    private Button cow, buffalo;
    private String id,name,type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_milk_type);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Select Milk Type");
        actionBar.setDisplayHomeAsUpEnabled(true);

        cow = (Button)findViewById(R.id.cow);
        buffalo = (Button)findViewById(R.id.buffalo);
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        name = intent.getStringExtra("name");

        buffalo.setVisibility(View.GONE);
        cow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigate(v);
            }
        });

        buffalo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigate(v);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(),"Back Disabled",Toast.LENGTH_SHORT).show();
    }

    public void navigate(View v){
        Intent in = new Intent(SelectMilkType.this,SaleDetails.class);
        if(v.getId() == R.id.cow)
            in.putExtra("type",R.string.cow);
        else
            in.putExtra("type",R.string.buffalo);
        startActivity(in);
    }
}
