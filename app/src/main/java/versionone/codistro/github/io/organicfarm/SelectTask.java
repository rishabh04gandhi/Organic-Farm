package versionone.codistro.github.io.organicfarm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SelectTask extends AppCompatActivity {

    private Button admin, deliver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_task);

        admin = (Button) findViewById(R.id.admin);
        deliver = (Button) findViewById(R.id.deliver);

        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SelectTask.this,AdminPanel.class));
            }
        });

        deliver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(SelectTask.this,ScanQr.class);
                startActivity(in);
            }
        });
    }
}
