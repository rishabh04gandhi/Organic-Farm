package versionone.codistro.github.io.organicfarm;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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

    public void navigate(View v){
        Intent in = new Intent(SelectMilkType.this,SaleDetails.class);
        if(v.getId() == R.id.cow)
            in.putExtra("type","cow");
        else
            in.putExtra("type","buffalo");
        startActivity(in);
    }
}
