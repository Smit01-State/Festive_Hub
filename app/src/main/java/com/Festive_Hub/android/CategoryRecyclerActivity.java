package com.Festive_Hub.android;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;

public class CategoryRecyclerActivity extends AppCompatActivity {


    RecyclerView recyclerView;


int[ ] idArray = {1,2,3,4,5,6,7,8,9};
    String[] nameArray = {
            "Minutes",
            "Mobiles & Tablets",
            "Fashion",
            "Electronics",
            "TVs & Appliances",
            "Home & Furniture",
            "Flight Bookings",
            "Beauty, Food..",
            "Grocery"
    };
    String[] imageArray = {

            "https://rukminim2.flixcart.com/fk-p-flap/186/186/image/f03c562321e764bb.jpg?q=60",
            "https://rukminim2.flixcart.com/fk-p-flap/186/186/image/d7eae409dc461a54.jpg?q=60",
            "https://rukminim2.flixcart.com/fk-p-flap/64/64/image/ff559cb9d803d424.png?q=100",
            "https://rukminim2.flixcart.com/fk-p-flap/64/64/image/af646c36d74c4be9.png?q=100",
            "https://rukminim2.flixcart.com/fk-p-flap/64/64/image/e90944802d996756.jpg?q=100",
            "https://rukminim2.flixcart.com/fk-p-flap/64/64/image/1788f177649e6991.png?q=100",
            "https://rukminim2.flixcart.com/fk-p-flap/64/64/image/3c647c2e0d937dc5.png?q=100",
            "https://rukminim2.flixcart.com/fk-p-flap/64/64/image/b3020c99672953b9.png?q=100",
            "https://rukminim2.flixcart.com/fk-p-flap/64/64/image/e730a834ad950bae.png?q=100"
    };

    ArrayList<CategoryList> arrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_category_recycler);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



        recyclerView = findViewById(R.id.Category_Recycler_view);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.HORIZONTAL));

        arrayList = new ArrayList<>();
        for(int i = 0; i< nameArray.length;i++){
            CategoryList list = new CategoryList();
            list.setId(idArray[i]);
            list.setName(nameArray[i]);
            list.setImage(imageArray[i]);

            arrayList.add(list);

        }

        // ArrayAdapter adapter = new ArrayAdapter(CategoryActivity.this, android.R.layout.simple_list_item_1,nameArray);
        CategoryRecyclerAdapter adapter= new CategoryRecyclerAdapter(CategoryRecyclerActivity.this,arrayList);
        recyclerView.setAdapter(adapter);

    }
}