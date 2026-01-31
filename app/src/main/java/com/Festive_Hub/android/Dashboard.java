package com.Festive_Hub.android;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Dashboard extends AppCompatActivity {

    Button profile,log0ut,delete,Category;

    TextView title;
    SharedPreferences sp; // session

    SQLiteDatabase db;



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

        profile = findViewById(R.id.Dashboard_profile);
        log0ut = findViewById(R.id.Dashboard_logOut);
        delete = findViewById(R.id.Dashboard_Delete);
        title = findViewById(R.id.Dashboard_title);
        Category = findViewById(R.id.Dashboard_Category);

        sp = getSharedPreferences(ConstantSP.PREF, MODE_PRIVATE);

        db = openOrCreateDatabase("finalApp",MODE_PRIVATE,null);
        String createTable = "CREATE TABLE  IF NOT EXISTS FUSER(USERID INTEGER PRIMARY KEY AUTOINCREMENT ,NAME VARCHAR(50),EMAIL VARCHAR(20),CONTACTS BIGINT(10),PASSWORD VARCHAR(10),GENDER ENUM,CITY VARCHAR(10))";
        db.execSQL(createTable);



        title.setText("Welcome, " + sp.getString(ConstantSP.NAME, ""));

        Category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this, CategoryActivity.class);
                startActivity(intent);
            }
        });



       profile.setOnClickListener(
               new View.OnClickListener(){

                   @Override
                   public void onClick(View v) {
                        Intent intent = new Intent(Dashboard.this, ProfileActivity.class);
                        startActivity(intent);

                   }
               }
       );

        log0ut.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(Dashboard.this);
                        builder.setTitle("log Out");
                        builder.setMessage("are you sure you want to log out? ");
                        builder.setIcon(R.mipmap.app_icon);

                        builder.setCancelable(false);

                        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dologOut();
                            }
                        });
                        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        builder.setNeutralButton("rate us", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Toast.makeText(Dashboard.this, "Rate us 5*", Toast.LENGTH_SHORT).show();

                            }
                        });

                        builder.show();



                        //
                    }
                }
        );
        delete.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(Dashboard.this);
                        builder.setTitle("Delete your Account");
                        builder.setMessage("are you sure you want to Delete Account? ");
                        builder.setIcon(R.mipmap.app_icon);

                        builder.setCancelable(false);//

                        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                String DeleteQuery = "DELETE FROM FUSER WHERE USERID = '"+sp.getString(ConstantSP.USERID,"")+"'";
                                    db.execSQL(DeleteQuery);
                                Toast.makeText(Dashboard.this, "Account Delete successfully", Toast.LENGTH_SHORT).show();
                                dologOut();
                            }
                        });
                        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

                        builder.show();


                    }
                }
        );

    }

    private void dologOut() {
        sp.edit().clear().commit();

        Intent intent = new Intent(Dashboard.this, MainActivity.class);
        startActivity(intent);
        finish();

    }
}

