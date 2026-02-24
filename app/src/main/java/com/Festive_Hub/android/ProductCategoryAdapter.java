package com.Festive_Hub.android;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.Festive_Hub.android.ConstantSP;
import com.Festive_Hub.android.ProductCategoryList;
import com.Festive_Hub.android.ProductDetailActivity;
import com.Festive_Hub.android.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ProductCategoryAdapter extends RecyclerView.Adapter<ProductCategoryAdapter.MyHolder> {
    Context context;
    ArrayList<ProductCategoryList> arrayList;
    SharedPreferences sp;

    public ProductCategoryAdapter(Context context, ArrayList<ProductCategoryList> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        sp = context.getSharedPreferences(ConstantSP.PREF,MODE_PRIVATE);
    }

    @NonNull
    @Override
    public ProductCategoryAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_product,parent,false);
        return new ProductCategoryAdapter.MyHolder(view);
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView productName, vendorName, originalPrice, discountedPrice, discount;
        ImageView image;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.custom_product_name);
            vendorName = itemView.findViewById(R.id.custom_product_vendor_name);
            originalPrice = itemView.findViewById(R.id.custom_product_price);
            discountedPrice = itemView.findViewById(R.id.custom_product_discount_price);
            discount = itemView.findViewById(R.id.custom_product_discount);
            image = itemView.findViewById(R.id.custom_product_image);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ProductCategoryAdapter.MyHolder holder, int position) {
        holder.productName.setText(arrayList.get(position).getProductName());
        holder.vendorName.setText(arrayList.get(position).getVendorName());
        holder.originalPrice.setText("₹"+arrayList.get(position).getProductPrice());
        holder.discountedPrice.setText("₹"+arrayList.get(position).getProductDiscountPrice());
        holder.discount.setText(arrayList.get(position).getDiscount()+"%");

        holder.originalPrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
//        holder.image.setImageResource(Integer.parseInt(arrayList.get(position).getImage()));
        Glide.with(context).load(arrayList.get(position).getImage()).placeholder(R.mipmap.ic_launcher).into(holder.image);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp.edit().putInt(ConstantSP.PRODUCTID, arrayList.get(position).getProductID()).commit();
                sp.edit().putString(ConstantSP.PRODUCTNAME, arrayList.get(position).getProductName()).commit();
                sp.edit().putString(ConstantSP.PRODUCTVENDORNAME, arrayList.get(position).getVendorName()).commit();
                sp.edit().putString(ConstantSP.PRODUCTPRICE, arrayList.get(position).getProductPrice()).commit();
                sp.edit().putString(ConstantSP.PRODUCTDISCOUNTPRICE, arrayList.get(position).getProductDiscountPrice()).commit();
                sp.edit().putString(ConstantSP.PRODUCTDISCOUNT, arrayList.get(position).getDiscount()).commit();
                sp.edit().putString(ConstantSP.PRODUCTDISCOUNT, arrayList.get(position).getImage()).commit();

                Intent intent = new Intent(context, ProductDetailActivity.class);
                context.startActivity(intent);


            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
