package com.Festive_Hub.android;


import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
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
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {
    
    Button Edit,Update;

    TextView Login;
    EditText name , Email, Contact , Password, ConfirmPassword;
    CardView ConfirmPasswordCard;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    RadioGroup gender;
    RadioButton male,female;
    CheckBox terms;
    String sgender;

    Spinner spinner;

    ArrayList<String> cityArray;

    String scity = "";

    SQLiteDatabase db;

    SharedPreferences sp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        name = findViewById(R.id.Profile_name);
        Email = findViewById(R.id.Profile_Email);
        Contact = findViewById(R.id.Profile_contact);
        Password = findViewById(R.id.Profile_password);
        ConfirmPassword = findViewById(R.id.Profile_Confirm_password);
        ConfirmPasswordCard = findViewById(R.id.Profile_Confirm_password_card);

        Edit = findViewById(R.id.Profile_Edit_button);
        Update = findViewById(R.id.Profile_Update_button);

        gender = findViewById(R.id.Profile_radioGroup);
        male = findViewById(R.id.Profile_radio_male);
        female = findViewById(R.id.Profile_radio_female);
        spinner = findViewById(R.id.Profile_Spinner);

        cityArray = new ArrayList<>();
        cityArray.add("ahemdabad");
        cityArray.add("vadodra");
        cityArray.add("surat");
        cityArray.add("rajkot");
        cityArray.add(0,"Select City");



        sp = getSharedPreferences(ConstantSP.PREF,MODE_PRIVATE);
        db = openOrCreateDatabase("finalApp",MODE_PRIVATE,null);
        String ProfileTable = "CREATE TABLE  IF NOT EXISTS FUSER(USERID INTEGER PRIMARY KEY AUTOINCREMENT ,NAME VARCHAR(50),EMAIL VARCHAR(20),CONTACTS BIGINT(10),PASSWORD VARCHAR(10),GENDER ENUM,CITY VARCHAR(10))";
        db.execSQL(ProfileTable);



        ArrayAdapter adapter = new ArrayAdapter(ProfileActivity.this, android.R.layout.simple_list_item_1,cityArray);
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
                    Toast.makeText(ProfileActivity.this, scity, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(ProfileActivity.this, sgender, Toast.LENGTH_SHORT).show();
                    }
                }
        );

        Update.setOnClickListener(
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
                            Toast.makeText(ProfileActivity.this, "Please Select Gender", Toast.LENGTH_SHORT).show();
                        }
                        else if(scity == ""){
                            Toast.makeText(ProfileActivity.this, "Please Select City", Toast.LENGTH_SHORT).show();
                        }

                        else{

                            String UpdateQuery = "UPDATE FUSER " +
                                    "SET NAME='"+name.getText().toString()+"'," +
                                    "EMAIL='"+Email.getText().toString()+"'," +
                                    "CONTACTS='"+Contact.getText().toString()+"'," +
                                    "PASSWORD='"+Password.getText().toString()+"'," +
                                    "GENDER='"+sgender+"',CITY='"+scity+"' WHERE USERID = '"+sp.getString(ConstantSP.USERID,"")+"' ";
                            db.execSQL(UpdateQuery);

                            sp.edit().putString(ConstantSP.NAME,name.getText().toString()).commit();
                            sp.edit().putString(ConstantSP.EMAIL,Email.getText().toString()).commit();
                            sp.edit().putString(ConstantSP.CONTACTS,Contact.getText().toString()).commit();
                            sp.edit().putString(ConstantSP.PASSWORD,Password.getText().toString()).commit();
                            sp.edit().putString(ConstantSP.GENDER,sgender).commit();
                            sp.edit().putString(ConstantSP.CITY,scity).commit();

                            System.out.println("signup successfully");
                            Toast.makeText(ProfileActivity.this,"profile update Successfully",Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(ProfileActivity.this, Dashboard.class);
                            startActivity(intent);


                            onBackPressed();
                        }

                    }
                }
        );
        Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setData(true);
            }
        });
        setData(false);

    }

    private void setData(boolean b) {


        if(b){
            ConfirmPasswordCard.setVisibility(View.VISIBLE);
            Edit.setVisibility(View.GONE);
            Update.setVisibility(View.VISIBLE);

        }
        else {

            ConfirmPasswordCard.setVisibility(View.GONE);
            Edit.setVisibility(View.VISIBLE);
            Update.setVisibility(View.GONE);
        }

        name.setEnabled(b);
        Email.setEnabled(b);
        Contact.setEnabled(b);
        Password.setEnabled(b);
        male.setEnabled(b);
        female.setEnabled(b);
        spinner.setEnabled(b);


        name.setText(sp.getString(ConstantSP.NAME,""));
        Email.setText(sp.getString(ConstantSP.EMAIL,""));
        Contact.setText(sp.getString(ConstantSP.CONTACTS,""));
        Password.setText(sp.getString(ConstantSP.PASSWORD,""));
        ConfirmPassword.setText(sp.getString(ConstantSP.PASSWORD,""));

        sgender = sp.getString(ConstantSP.GENDER,"");
        if (sgender.equalsIgnoreCase("male")){

            male.setChecked(true);
            female.setChecked(false);

        } else if (sgender.equalsIgnoreCase("female")) {

            male.setChecked(false);
            female.setChecked(true);

        }
        else {

            male.setChecked(false);
            female.setChecked(false);
        }

        scity = sp.getString(ConstantSP.CITY,"");
        int iCityPosition = 0;

        for(int i= 0;i<=cityArray.size();i++){
            if(scity.equalsIgnoreCase(cityArray.get(i))){
                iCityPosition = i;
                break;
            }
        }
        spinner.setSelection(iCityPosition);


    }
}