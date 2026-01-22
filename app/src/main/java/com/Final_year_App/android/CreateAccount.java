package com.Final_year_App.android;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class CreateAccount extends AppCompatActivity {

Button Signup;

TextView Login;
EditText name , Email, Contact , Password, ConfirmPassword;
String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
RadioGroup gender;
CheckBox terms;
String sgender;

Spinner spinner;

ArrayList<String> cityArray;

String scity = "";

SQLiteDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_account);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        name = findViewById(R.id.Create_name);
        Email = findViewById(R.id.Create_Email);
        Contact = findViewById(R.id.Create_contact);
        Password = findViewById(R.id.Create_password);
        ConfirmPassword = findViewById(R.id.Create_Confirm_password);
        Signup = findViewById(R.id.Create_button);
        Login = findViewById(R.id.Create_login);
        terms = findViewById(R.id.Create_terms);
        gender = findViewById(R.id.Create_radioGroup);
        spinner = findViewById(R.id.Create_Spinner);

        cityArray = new ArrayList<>();
        cityArray.add("ahemdabad");
        cityArray.add("vadodra");
        cityArray.add("surat");
        cityArray.add("rajkot");
        cityArray.add(0,"Select City");


        db = openOrCreateDatabase("finalApp",MODE_PRIVATE,null);
        String createTable = "CREATE TABLE  IF NOT EXISTS FUSER(USERID INTEGER PRIMARY KEY AUTOINCREMENT ,NAME VARCHAR(50),EMAIL VARCHAR(20),CONTACTS BIGINT(10),PASSWORD VARCHAR(10),GENDER ENUM,CITY VARCHAR(10))";
        db.execSQL(createTable);



        ArrayAdapter adapter = new ArrayAdapter(CreateAccount.this, android.R.layout.simple_list_item_1,cityArray);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_checked);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    scity = "";
                }
                else {
                    //sCity = cityArray[i];
                    scity = cityArray.get(position);
                    Toast.makeText(CreateAccount.this, scity, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


       gender.setOnCheckedChangeListener(
               new RadioGroup.OnCheckedChangeListener() {
                   @Override
                   public void onCheckedChanged(@NonNull RadioGroup group, int checkedId) {
                       RadioButton radiobutton = findViewById(checkedId);
                       sgender = radiobutton.getText().toString();
                       Toast.makeText(CreateAccount.this, sgender, Toast.LENGTH_SHORT).show();
                   }
               }
       );

        Signup.setOnClickListener(
                new View.OnClickListener(){

                    @Override
                    public void onClick(View v) {

                        if(name.getText().toString().trim().equals("")){
                            name.setError("Enter your name");}
                        else if(Email.getText().toString().trim().equals("")){
                                Email.setError(" Email Required ");
                        }
                        else if (!Email.getText().toString().trim().matches(emailPattern)) {
                                Email.setError("valid Email Id required");
                        }
                        else if (Contact.getText().toString().trim().length()<10) {
                            Contact.setError("valid contact number required");
                            
                        } else if (Password.getText().toString().trim().equals("")) {
                            Password.setError("password required");
                        }
                        else if (Password.getText().toString().trim().length()<6) {
                            Password.setError("valid Length(minimum 6) required");
                        }
                        else if (ConfirmPassword.getText().toString().trim().equals("")) {
                        ConfirmPassword.setError("password required");
                        }
                        else if (ConfirmPassword.getText().toString().trim().length()<6) {
                        ConfirmPassword.setError("valid Length(minimum 6) required");
                        }
                        else if (!ConfirmPassword.getText().toString().matches(Password.getText().toString().trim())) {
                            ConfirmPassword.setError("password not match");
                        }
                        else if(gender.getCheckedRadioButtonId() == -1){
                            Toast.makeText(CreateAccount.this, "Please Select Gender", Toast.LENGTH_SHORT).show();
                        }
                        else if(scity == ""){
                            Toast.makeText(CreateAccount.this, "Please Select City", Toast.LENGTH_SHORT).show();
                        }
                        else if(!terms.isChecked()){
                            Toast.makeText(CreateAccount.this, "Please Accpet Terms & Conditions", Toast.LENGTH_SHORT).show();
                        }
                        else{

                            String InsertQuery = "INSERT INTO FUSER (NAME,EMAIL,CONTACTS,PASSWORD,GENDER,CITY) values('"+name.getText().toString()+"','"+Email.getText().toString()+"','"+Contact.getText().toString()+"','"+Password.getText().toString()+"','"+sgender+"','"+scity+"')";
                            db.execSQL(InsertQuery);

                            System.out.println("signup successfully");
                            Toast.makeText(CreateAccount.this,"signup Successfully",Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(CreateAccount.this, MainActivity.class);
                            startActivity(intent);

                            onBackPressed();
                        }

                    }
                }
        );

        Login.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(CreateAccount.this,MainActivity.class);
                        startActivity(intent);
                    }
                }
        );



    }
}
