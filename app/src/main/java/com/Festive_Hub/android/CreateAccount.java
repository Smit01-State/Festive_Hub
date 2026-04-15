package com.Festive_Hub.android;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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


import com.Festive_Hub.android.Hash.MD5Hash;
import com.Festive_Hub.android.network.ApiClient;

import java.util.ArrayList;

public class CreateAccount extends AppCompatActivity {



Button Signup;
TextView Login;
EditText name , email, contact , password, ConfirmPassword;
String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
RadioGroup gender;
CheckBox terms;
String sgender;

Spinner spinner;

ArrayList<String> cityArray;

String scity = "";

SQLiteDatabase db;
    ApiClient apiClient;


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
        email = findViewById(R.id.Create_Email);
        contact = findViewById(R.id.Create_contact);
        password = findViewById(R.id.Create_password);
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

        apiClient = new ApiClient(this);
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
                        String Name = name.getText().toString().trim();
                        String Email = email.getText().toString().trim();
                        String Contact = contact.getText().toString().trim();
                        String Password = password.getText().toString().trim();

                        String HashPassword = MD5Hash.md5Hash(Password);
                        Log.d("hash", "onClick: "+HashPassword);



                        if(Name.equals("")){
                            name.setError("Enter your name");}
                        else if(Email.equals("")){
                                email.setError(" Email Required ");
                        }
                        else if (!Email.matches(emailPattern)) {
                                email.setError("valid Email Id required");
                        }
                        else if (Contact.length()<10) {
                            contact.setError("valid contact number required");
                            
                        } else if (Password.equals("")) {
                            password.setError("password required");
                        }
                        else if (Password.length()<6) {
                            password.setError("valid Length(minimum 6) required");
                        }
                        else if (ConfirmPassword.getText().toString().trim().equals("")) {
                        ConfirmPassword.setError("password required");
                        }
                        else if (ConfirmPassword.getText().toString().trim().length()<6) {
                        ConfirmPassword.setError("valid Length(minimum 6) required");
                        }
                        else if (!ConfirmPassword.getText().toString().matches(Password)) {
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

                           /* String InsertQuery = "INSERT INTO FUSER (NAME,EMAIL,CONTACTS,PASSWORD,GENDER,CITY) values('"+Name+"','"+Email+"','"+Contact+"','"+ HashPassword +"','"+sgender+"','"+scity+"')";
                            db.execSQL(InsertQuery);*/
                            apiClient.registerUser( Name, Email, Contact, HashPassword ,sgender,scity,callback);
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

   ApiClient.ApiCallback callback = new ApiClient.ApiCallback() {
        @Override
        public void onSuccess(String message) {
            Toast.makeText(CreateAccount.this, message, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(String error) {
            Log.e("ERROR", "onError: "+error);
            Toast.makeText(CreateAccount.this, error, Toast.LENGTH_SHORT).show();
        }
    };
}
