package com.Final_year_App.android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class CreateAccount extends AppCompatActivity {

Button Signup;

TextView Login;
 EditText name , Email, Contact , Password, ConfirmPassword;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";



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
                        else{
                            System.out.println("signup successfully");
                            Toast.makeText(CreateAccount.this,"signup Successfully",Toast.LENGTH_SHORT).show();
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
