package com.Festive_Hub.android;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;

public class SubCategoryActivity extends AppCompatActivity {


    RecyclerView recyclerView;

    SharedPreferences sp;
    int[] subCategoryIDArray = {1,2,3,4,5,6};
    int[] categoryIDArray = {3,3,3,4,4,3};
    String[] nameArray = {
            "Top Wear",
            "Bottom Wear",
            "Footwear",
            "Gaming",
            "Laptop",
            "Kids"
    };

    ArrayList<SubCategoryList> arrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sub_category);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        sp= getSharedPreferences(ConstantSP.PREF,MODE_PRIVATE);

        recyclerView = findViewById(R.id.SubCategory_Recycler_view);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.HORIZONTAL));

        arrayList = new ArrayList<>();
        for(int i = 0; i< nameArray.length;i++){
            if(sp.getInt(ConstantSP.CATEGORYID,0)==categoryIDArray[i]) {
                SubCategoryList list = new SubCategoryList();
                list.setCategoryID(categoryIDArray[i]);
                list.setSubCategoryID(subCategoryIDArray[i]);
                list.setName(nameArray[i]);
                arrayList.add(list);
            }

        }

        // ArrayAdapter adapter = new ArrayAdapter(CategoryActivity.this, android.R.layout.simple_list_item_1,nameArray);
        SubCategoryAdapter adapter= new SubCategoryAdapter(SubCategoryActivity.this,arrayList);
        recyclerView.setAdapter(adapter);

    }
    }
