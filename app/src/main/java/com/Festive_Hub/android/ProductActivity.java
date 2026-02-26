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

public class ProductActivity extends AppCompatActivity {

        int[] productIDArray = { 1, 2, 3, 4, 5, 6 };
        int[] SubcategoryIdArray = { 1, 1, 2, 2, 3, 4 };
        String[] vendorName = {
                        "Central Park, NYC",
                        "City Square, Ahmedabad",
                        "St. Peter's Cathedral",
                        "Community Hall, London",
                        "Beach Side, Goa",
                        "Palace Grounds, Bangalore" };
        String[] productNameArray = {
                        "Grand Diwali Mela 2024",
                        "Deepavali Fireworks Show",
                        "Christmas Eve Grand Mass",
                        "Santa's Winter Workshop",
                        "Holi Color Fest 2024",
                        "Navratri Garba Night"
        };
        String[] productPriceArray = {
                        "Nov 12, 2024", "Nov 13, 2024", "Dec 24, 2024", "Dec 25, 2024", "Mar 25, 2024", "Oct 03, 2024"
        };
        String[] productDiscountPriceArray = {
                        "06:00 PM", "08:00 PM", "10:00 PM", "11:00 AM", "10:00 AM", "07:00 PM"
        };
        String[] DiscountArray = {
                        "Free", "50", "Free", "20", "30", "40"
        };

        String[] imageArray = {
                        "https://img.freepik.com/free-vector/happy-diwali-festival-banner-with-realistic-diya-oil-lamp_1017-34085.jpg",
                        "https://img.freepik.com/free-vector/diwali-celebration-background-with-fireworks_1017-15632.jpg",
                        "https://img.freepik.com/free-vector/christmas-background-with-realistic-decoration_23-2148761273.jpg",
                        "https://img.freepik.com/free-vector/christmas-workshop-landing-page_23-2148780366.jpg",
                        "https://img.freepik.com/free-vector/colorful-holi-festival-background_23-2148842427.jpg",
                        "https://img.freepik.com/free-vector/navratri-festival-background-with-realistic-dandiya_1017-21175.jpg"
        };

        RecyclerView Productrecycler;

        SharedPreferences sp;

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

                Productrecycler = findViewById(R.id.Product_Recycler);

                sp = getSharedPreferences(ConstantSP.PREF, MODE_PRIVATE);

                Productrecycler.setLayoutManager(
                                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

                arrayList = new ArrayList<ProductCategoryList>();
                for (int i = 0; i < productNameArray.length; i++) {
                        if (sp.getInt(ConstantSP.SUBCATEGORYID, 0) == SubcategoryIdArray[i]) {

                                ProductCategoryList list = new ProductCategoryList();

                                list.setVendorName(vendorName[i]);
                                list.setSubcategoryId(SubcategoryIdArray[i]);
                                list.setProductID(productIDArray[i]);
                                list.setProductName(productNameArray[i]);
                                list.setProductPrice(productPriceArray[i]);
                                list.setProductDiscountPrice(productDiscountPriceArray[i]);
                                list.setDiscount(DiscountArray[i]);
                                list.setImage(imageArray[i]);

                                arrayList.add(list);
                        }

                }

                // ArrayAdapter adapter = new ArrayAdapter(CategoryActivity.this,
                // android.R.layout.simple_list_item_1,nameArray);
                ProductCategoryAdapter adapter = new ProductCategoryAdapter(ProductActivity.this, arrayList);
                Productrecycler.setAdapter(adapter);

        }
}
