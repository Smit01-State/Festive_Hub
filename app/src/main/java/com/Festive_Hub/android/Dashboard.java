package com.Festive_Hub.android;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;

public class Dashboard extends AppCompatActivity {

    ImageButton profileBtn, wishlistBtn, cartBtn;
    TextView title;
    RecyclerView categoryRecyclerView;

    SharedPreferences sp;
    SQLiteDatabase db;

    // Category data
    int[] idArray = { 1, 2, 3, 4 };
    String[] nameArray = {
            "Religious Festivals",
            "Cultural Festivals",
            "Music & Arts",
            "Food Festivals"
    };
    String[] imageArray = {
            "https://img.freepik.com/free-vector/diwali-festival-background-with-diya-oil-lamp_1017-34084.jpg",
            "https://img.freepik.com/free-vector/holi-festival-background-with-colorful-powder_23-2148842426.jpg",
            "https://img.freepik.com/free-vector/music-event-poster-template-with-abstract-shapes_23-2148293060.jpg",
            "https://img.freepik.com/free-vector/food-festival-poster-template_23-2148530460.jpg"
    };

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

        // Views
        profileBtn = findViewById(R.id.Dashboard_profile_btn);
        wishlistBtn = findViewById(R.id.Dashboard_wishlist_btn);
        cartBtn = findViewById(R.id.Dashboard_cart_btn);
        title = findViewById(R.id.Dashboard_title);
        categoryRecyclerView = findViewById(R.id.Dashboard_category_recycler);

        // SharedPreferences & DB
        sp = getSharedPreferences(ConstantSP.PREF, MODE_PRIVATE);
        db = openOrCreateDatabase("finalApp", MODE_PRIVATE, null);
        String createTable = "CREATE TABLE IF NOT EXISTS FUSER(" +
                "USERID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "NAME VARCHAR(50), EMAIL VARCHAR(20)," +
                "CONTACTS BIGINT(10), PASSWORD VARCHAR(10)," +
                "GENDER ENUM, CITY VARCHAR(10))";
        db.execSQL(createTable);

        // Set welcome title
        title.setText("Welcome, " + sp.getString(ConstantSP.NAME, ""));

        // Setup RecyclerView with event categories
        ArrayList<CategoryList> categoryList = new ArrayList<>();
        for (int i = 0; i < nameArray.length; i++) {
            CategoryList item = new CategoryList();
            item.setId(idArray[i]);
            item.setName(nameArray[i]);
            item.setImage(imageArray[i]);
            categoryList.add(item);
        }
        categoryRecyclerView.setLayoutManager(
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        CategoryRecyclerAdapter adapter = new CategoryRecyclerAdapter(Dashboard.this, categoryList);
        categoryRecyclerView.setAdapter(adapter);

        // Profile icon click → PopupMenu
        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(Dashboard.this, profileBtn);
                popupMenu.getMenu().add(0, 1, 0, "Profile Edit");
                popupMenu.getMenu().add(0, 2, 1, "Logout");
                popupMenu.getMenu().add(0, 3, 2, "Delete Account");

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        if (id == 1) {
                            // Navigate to ProfileActivity
                            Intent intent = new Intent(Dashboard.this, ProfileActivity.class);
                            startActivity(intent);
                        } else if (id == 2) {
                            showLogoutDialog();
                        } else if (id == 3) {
                            showDeleteDialog();
                        }
                        return true;
                    }
                });
                popupMenu.show();
            }
        });

        // Wishlist button click
        wishlistBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Dashboard.this, "Wishlist coming soon!", Toast.LENGTH_SHORT).show();
            }
        });

        // Cart button click
        cartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Dashboard.this, "Cart coming soon!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showLogoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Dashboard.this);
        builder.setTitle("Log Out");
        builder.setMessage("Are you sure you want to log out?");
        builder.setIcon(R.mipmap.app_icon);
        builder.setCancelable(false);

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                doLogOut();
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // dismiss
            }
        });
        builder.setNeutralButton("Rate Us", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(Dashboard.this, "Rate us 5★", Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();
    }

    private void showDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Dashboard.this);
        builder.setTitle("Delete Account");
        builder.setMessage("Are you sure you want to delete your account?");
        builder.setIcon(R.mipmap.app_icon);
        builder.setCancelable(false);

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String deleteQuery = "DELETE FROM FUSER WHERE USERID = '" +
                        sp.getString(ConstantSP.USERID, "") + "'";
                db.execSQL(deleteQuery);
                Toast.makeText(Dashboard.this, "Account deleted successfully", Toast.LENGTH_SHORT).show();
                doLogOut();
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // dismiss
            }
        });
        builder.show();
    }

    private void doLogOut() {
        sp.edit().clear().commit();
        Intent intent = new Intent(Dashboard.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
