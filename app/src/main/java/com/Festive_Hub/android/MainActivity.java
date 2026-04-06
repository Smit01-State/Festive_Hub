package com.Festive_Hub.android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.Festive_Hub.android.network.LoginApiClient;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    Button login;

    EditText Email, Password;
    TextView CreateAccount;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    SQLiteDatabase db;

    SharedPreferences sp;


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
        // constant
        sp = getSharedPreferences(ConstantSP.PREF, MODE_PRIVATE);


        Email = findViewById(R.id.main_Email);
        Password = findViewById(R.id.main_Password);
        login = findViewById(R.id.main_button);
        CreateAccount = findViewById(R.id.main_createAccount);


        login.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (Email.getText().toString().trim().equals("")) {
                            Email.setError(" Email Required ");
                        } /*else if (!Email.getText().toString().trim().matches(emailPattern)) {
                    Email.setError("valid Email Id required");}*/ else if (Password.getText().toString().trim().equals("")) {
                            Password.setError("password required");
                        } else if (Password.getText().toString().trim().length() < 6) {
                            Password.setError("valid Length(minimum 6) required");
                        } else {

                            // ── Get values from fields ────────────────
                            String email    = Email.getText().toString().trim();
                            String password = Password.getText().toString().trim();

                            if (email.equals("admin@gmail.com") && password.equals("123abc")) {
                                Toast.makeText(MainActivity.this, "Admin Login Successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(MainActivity.this, EventManagerActivity.class);
                                startActivity(intent);
                                finish();
                                return;
                            }

                            // ── Call API instead of SQLite ────────────
                            LoginApiClient loginApiClient = new LoginApiClient(MainActivity.this);

                            loginApiClient.loginUser(email, password, new LoginApiClient.LoginCallback() {

                                @Override
                                public void onSuccess(JSONObject userData) throws JSONException {

                                    // ── Save to SharedPreferences ─────
                                    // same sp.edit() you were doing before


                                    sp.edit().putString(ConstantSP.USERID, userData.getString("userid")).apply();
                                    sp.edit().putString(ConstantSP.NAME, userData.getString("name")).apply();
                                    sp.edit().putString(ConstantSP.EMAIL, userData.getString("email")).apply();
                                    sp.edit().putString(ConstantSP.CONTACTS, userData.getString("contact")).apply();
                                    sp.edit().putString(ConstantSP.PASSWORD, userData.getString("password")).apply();
                                    sp.edit().putString(ConstantSP.GENDER, userData.getString("gender")).apply();
                                    sp.edit().putString(ConstantSP.CITY, userData.getString("city")).apply();

                                    Toast.makeText(MainActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();

                                    // ── Go to Dashboard ───────────────
                                    Intent intent = new Intent(MainActivity.this, Dashboard.class);
                                    startActivity(intent);
                                    finish();
                                }

                                @Override
                                public void onError(String error) {
                                    Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
                                }
                            });

                        }

                    }
                });

        CreateAccount.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, CreateAccount.class);
                        startActivity(intent);
                    }
                }
        );


    }
}

