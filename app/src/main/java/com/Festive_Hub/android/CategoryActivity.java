package com.Festive_Hub.android;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class CategoryActivity extends AppCompatActivity {

    GridView listView;

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
        setContentView(R.layout.activity_category);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });

        listView = findViewById(R.id.category_listview);

        arrayList = new ArrayList<>();
        for(int i = 0; i< nameArray.length;i++){
           CategoryList list = new CategoryList();
           list.setName(nameArray[i]);
           list.setImage(imageArray[i]);

           arrayList.add(list);

        }

        // ArrayAdapter adapter = new ArrayAdapter(CategoryActivity.this, android.R.layout.simple_list_item_1,nameArray);
       CategoryAdapter adapter = new CategoryAdapter(CategoryActivity.this,arrayList);
        listView.setAdapter(adapter);
        
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(CategoryActivity.this, arrayList.get(position).getName(), Toast.LENGTH_SHORT).show();
            }

        });


    }
}