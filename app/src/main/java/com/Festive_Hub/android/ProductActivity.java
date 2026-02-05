package com.Festive_Hub.android;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ProductActivity extends AppCompatActivity {

    int[] productIDArray = {};
    int[] SubcategoryIdArray = {};
    String[] vendorName = {};
    String[] productNameArray = {};
    String[] productPriceArray = {};
    String[] productDiscountPriceArray = {};
    String[] DiscountArray = {};

    RecyclerView recyclerView;

    ArrayList<ProductCategoryList> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.Product_Recycler);


    }
}