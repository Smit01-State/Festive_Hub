package com.Final_year_App.android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Dashboard extends AppCompatActivity {

    Button BackButton;

    TextView title;
    SharedPreferences sp;
    // session


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dashboard);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        BackButton = findViewById(R.id.backButton);
        title = findViewById(R.id.Dashboard_title);

        sp = getSharedPreferences(ConstantSP.PREF, MODE_PRIVATE);

        title.setText("Welcome " + sp.getString(ConstantSP.NAME, ""));

        BackButton.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {


                        Intent intent = new Intent(Dashboard.this, MainActivity.class);
                        startActivity(intent);
                    }
                }
        );

    }
}

