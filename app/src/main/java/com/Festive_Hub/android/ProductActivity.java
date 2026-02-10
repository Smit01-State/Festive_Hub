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

public class ProductActivity extends AppCompatActivity {

    int[] productIDArray = {1,2,3};
    int[] SubcategoryIdArray = {1,1,2};
    String[] vendorName = {
            "GM TRENDS",
            "TRIPR",
            "MK BROTHERS"};
    String[] productNameArray = {
            "Men Printed Round Neck Cotton Blend Blue T-Shirt",
            "Men Solid Henley Neck Cotton Blend Black, Beige T-Shirt",
            "Men Printed, Graphic Print Black Track Pants"
    };
    String[] productPriceArray = {
            "399","999","1499"
    };
    String[] productDiscountPriceArray = {
            "241","215","205"
    };
    String[] DiscountArray = {
            "39","78","86"
    };

    String[] imageArray = {
            "https://rukminim2.flixcart.com/image/612/612/xif0q/t-shirt/p/s/r/m-6002-never-royal-blue-m-gm-trends-original-imahj6jgp3ywwwgh.jpeg?q=70",
            "https://rukminim2.flixcart.com/image/612/612/xif0q/t-shirt/y/c/3/l-tblbghn-d213-tripr-original-imahj8x8nae4aazn.jpeg?q=70",
            "https://rukminim2.flixcart.com/image/612/612/xif0q/track-pant/v/b/6/xl-spider-mk-brothers-original-imahj6xuj2ryrver.jpeg?q=70"
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

        sp = getSharedPreferences(ConstantSP.PREF,MODE_PRIVATE);

        Productrecycler.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));

        arrayList = new ArrayList<ProductCategoryList>();
        for(int i = 0; i< productNameArray.length; i++){
            if(sp.getInt(ConstantSP.SUBCATEGORYID,0)==SubcategoryIdArray[i]) {

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

        // ArrayAdapter adapter = new ArrayAdapter(CategoryActivity.this, android.R.layout.simple_list_item_1,nameArray);
        ProductCategoryAdapter adapter= new ProductCategoryAdapter(ProductActivity.this,arrayList);
        Productrecycler.setAdapter(adapter);



    }
}