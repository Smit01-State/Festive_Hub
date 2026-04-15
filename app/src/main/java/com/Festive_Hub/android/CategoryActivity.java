package com.Festive_Hub.android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class CategoryActivity extends AppCompatActivity {

    GridView listView;

    int[] idArray = {1, 2, 3, 4, 5, 6};
    String[] nameArray = {
            "Religious Festivals",
            "Cultural Festivals",
            "Music & Arts",
            "Food Festivals",
            "Tech Expos",
            "Sports Events"
    };
    String[] imageArray = {
            "https://img.freepik.com/free-vector/diwali-festival-background-with-diya-oil-lamp_1017-34084.jpg",
            "https://img.freepik.com/free-vector/holi-festival-background-with-colorful-powder_23-2148842426.jpg",
            "https://img.freepik.com/free-vector/music-event-poster-template-with-abstract-shapes_23-2148293060.jpg",
            "https://img.freepik.com/free-vector/food-festival-poster-template_23-2148530460.jpg",
            "https://img.freepik.com/free-vector/technology-background-with-hexagonal-shapes_23-2148386801.jpg",
            "https://img.freepik.com/free-vector/abstract-sport-texture-background-design_23-2148858882.jpg"
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
           list.setId(idArray[i]);
           list.setName(nameArray[i]);
           list.setImage(imageArray[i]);

           arrayList.add(list);
        }

       CategoryAdapter adapter = new CategoryAdapter(CategoryActivity.this,arrayList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                getSharedPreferences(ConstantSP.PREF, MODE_PRIVATE).edit().putInt(ConstantSP.CATEGORYID, arrayList.get(position).getId()).apply();
                Intent intent = new Intent(CategoryActivity.this, EventActivity.class);
                startActivity(intent);
            }
        });
    }
}
