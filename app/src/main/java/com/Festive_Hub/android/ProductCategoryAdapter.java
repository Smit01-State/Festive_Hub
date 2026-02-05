package com.Festive_Hub.android;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ProductCategoryAdapter extends RecyclerView.Adapter<ProductCategoryAdapter.MyHolder> {

    Context context;
    ArrayList<ProductCategoryList> arrayList;

    SharedPreferences sp;
    public ProductCategoryAdapter(Context context, ArrayList<ProductCategoryList> arrayList)  {
        this.context = context;
        this.arrayList = arrayList;

        this.sp= context.getSharedPreferences(ConstantSP.PREF,context.MODE_PRIVATE);


    }



    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sub_custom_category,parent,false);
        return new ProductCategoryAdapter.MyHolder(view);
    }

    public class MyHolder extends RecyclerView.ViewHolder{

        TextView vendorName,productName,productDiscountPrice,productPrice,productDiscount;
        ImageView imageView;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.custom_product_image);
            vendorName = itemView.findViewById(R.id.custom_product_vendor_name);
            productName = itemView.findViewById(R.id.custom_product_name);
            productDiscountPrice = itemView.findViewById(R.id.custom_product_discount_price);
            productPrice = itemView.findViewById(R.id.custom_product_price);
            productDiscount= itemView.findViewById(R.id.custom_product_discount);




        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        holder.vendorName.setText(arrayList.get(position).getVendorName());
        holder.productName.setText(arrayList.get(position).getProductNameArray());
        holder.productDiscountPrice.setText(ConstantSP.PRICE_SYMBOL + arrayList.get(position).getProductDiscountPriceArray());
        holder.productPrice.setText(ConstantSP.PRICE_SYMBOL + arrayList.get(position).getProductPriceArray());
        holder.productDiscount.setText(arrayList.get(position).getDiscountArray()+"% off");

        holder.productPrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context, arrayList.get(position).getName(), Toast.LENGTH_SHORT).show();
                sp.edit().putInt(ConstantSP.PRODUCTID,arrayList.get(position).getProductIDArray()).commit();
                sp.edit().putString(ConstantSP.PRODUCTVENDORNAME,arrayList.get(position).getVendorName()).commit();
                sp.edit().putString(ConstantSP.PRODUCTNAME,arrayList.get(position).getProductNameArray()).commit();
                sp.edit().putString(ConstantSP.PRODUCTDISCOUNTPRICE,arrayList.get(position).getProductDiscountPriceArray()).commit();
                sp.edit().putString(ConstantSP.PRODUCTPRICE,arrayList.get(position).getProductPriceArray()).commit();
                sp.edit().putString(ConstantSP.PRODUCTDISCOUNT,arrayList.get(position).getDiscountArray()).commit();
                sp.edit().putString(ConstantSP.PRODUCTIMAGE,arrayList.get(position).getImageArray()).commit();

                Intent intent = new Intent(context, ProductDetailActivity.class);
                context.startActivity(intent);
            }
        });

        /*holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Image Taped"+arrayList.get(position).getName(), Toast.LENGTH_SHORT).show();
            }
        });*/



    }






    public int getItemCount() {
        return arrayList.size();
    }


}


