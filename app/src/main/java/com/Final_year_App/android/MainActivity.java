package com.Final_year_App.android;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    Button login;
    EditText Email, Password;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        Email = findViewById(R.id.main_Email);
        Password = findViewById(R.id.main_Password);

        login = findViewById(R.id.main_button);
        login.setOnClickListener(
                new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                if(Email.getText().toString().trim().equals("")){
                    Email.setError(" Email Required ");
                } else if (!Email.getText().toString().trim().matches(emailPattern)) {
                    Email.setError("valid Email Id required");
                } else if (Password.getText().toString().trim().equals("")) {
                    Password.setError("password required");
                } else if (Password.getText().toString().trim().length()<6){
                    Password.setError("valid Length(minimum 6) required");
                } else{
                    System.out.println("login successfully");
                    Log.d("login","successfully login");
                    Log.e("login","successfully login");
                    Log.w("login","successfully login");

                    Toast.makeText(MainActivity.this,"login successfully",Toast.LENGTH_SHORT).show();
                    Snackbar.make(v, "login successfully",Snackbar.LENGTH_LONG).show();

                }

            }
        }
        );

    }
}

