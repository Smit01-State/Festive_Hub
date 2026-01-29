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


        db = openOrCreateDatabase("finalApp", MODE_PRIVATE, null);
        String createTable = "CREATE TABLE  IF NOT EXISTS FUSER(USERID INTEGER PRIMARY KEY AUTOINCREMENT ,NAME VARCHAR(50),EMAIL VARCHAR(20),CONTACTS BIGINT(10),PASSWORD VARCHAR(10),GENDER ENUM,CITY VARCHAR(10))";
        db.execSQL(createTable);


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

                            String SelectQuery = "SELECT * FROM FUSER WHERE (EMAIL='" + Email.getText().toString() + "' or CONTACTS = '" + Email.getText().toString() + "' ) and PASSWORD = '" + Password.getText().toString() + "' ";
                            Cursor cursor = db.rawQuery(SelectQuery, null);

                            if (cursor.getCount() > 0) {



                       /* System.out.println("login successfully");
                        Log.d("login","successfully login");
                        Log.e("login","successfully login");
                        Log.w("login","successfully login");*/

                                while (cursor.moveToNext()) {

                                    String sUserID = cursor.getString(0);
                                    String sName = cursor.getString(1);
                                    String sEmail = cursor.getString(2);
                                    String sContacts = cursor.getString(3);
                                    String sPassword = cursor.getString(4);
                                    String sGender = cursor.getString(5);
                                    String sCity = cursor.getString(6);

                                    sp.edit().putString(ConstantSP.USERID, sUserID).commit();
                                    sp.edit().putString(ConstantSP.NAME, sName).commit();
                                    sp.edit().putString(ConstantSP.EMAIL, sEmail).commit();
                                    sp.edit().putString(ConstantSP.CONTACTS, sContacts).commit();
                                    sp.edit().putString(ConstantSP.PASSWORD, sPassword).commit();
                                    sp.edit().putString(ConstantSP.GENDER, sGender).commit();
                                    sp.edit().putString(ConstantSP.CITY, sCity).commit();

                                }
                                Toast.makeText(MainActivity.this, "login successfully", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(MainActivity.this, Dashboard.class);
                                startActivity(intent);

                            } else {

                                Toast.makeText(MainActivity.this, "credential invalid!", Toast.LENGTH_SHORT).show();

                            }


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

