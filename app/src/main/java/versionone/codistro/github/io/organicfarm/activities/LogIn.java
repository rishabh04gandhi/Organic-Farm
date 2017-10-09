package versionone.codistro.github.io.organicfarm.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import versionone.codistro.github.io.organicfarm.R;

public class LogIn extends AppCompatActivity {

    private Button logInButton;
    private EditText userNameEditText, passwordEditText;
    public static final String FILE = "auth";
    public static final String USERNAME = "username";
    public static final String ADMIN_USERNAME = "admin";
    public static final String ADMIN_PASSWORD = "567#admin";
    public static final String DELIVERY_USERNAME = "delivery";
    public static final String DELIVERY_PASSWORD = "user#123";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_task);
        userNameEditText = (EditText) findViewById(R.id.username);
        passwordEditText = (EditText) findViewById(R.id.password);
        logInButton = (Button) findViewById(R.id.login);

        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authenticate();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences auth = getSharedPreferences(FILE,MODE_PRIVATE);
        if(auth.getString(USERNAME,null) != null) {
            if (auth.getString(USERNAME, null).equals(ADMIN_USERNAME)) {
                Intent in = new Intent(LogIn.this, AdminPanel.class);
                startActivity(in);
                finish();
            } else if (auth.getString(USERNAME, null).equals(DELIVERY_USERNAME)) {
                Intent in = new Intent(LogIn.this, ScanQr.class);
                startActivity(in);
                finish();
            }
        }
    }

    private void authenticate() {
        String username = userNameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString();

        if(username.equals(ADMIN_USERNAME) && password.equals(ADMIN_PASSWORD)){
            SharedPreferences auth = getSharedPreferences(FILE,MODE_PRIVATE);
            SharedPreferences.Editor editor = auth.edit();
            editor.putString(USERNAME,username);
            editor.apply();
            Intent in = new Intent(LogIn.this,AdminPanel.class);
            startActivity(in);
        }

        else if(username.equals(DELIVERY_USERNAME) && password.equals(DELIVERY_PASSWORD)){
            SharedPreferences auth = getSharedPreferences(FILE,MODE_PRIVATE);
            SharedPreferences.Editor editor = auth.edit();
            editor.putString(USERNAME,username);
            editor.apply();
            Intent in = new Intent(LogIn.this,ScanQr.class);
            startActivity(in);
        }
        else{
            Toast.makeText(LogIn.this,"Invalid Username or Password",Toast.LENGTH_SHORT).show();
        }
    }
}
