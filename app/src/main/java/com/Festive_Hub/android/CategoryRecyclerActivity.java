package com.Festive_Hub.android;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Space;

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


    int[ ] idArray = {1,2,3,4};
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
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));

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
