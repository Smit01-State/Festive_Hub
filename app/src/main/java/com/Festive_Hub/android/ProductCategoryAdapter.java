package com.Festive_Hub.android;

import static android.content.Context.MODE_PRIVATE;

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
        TextView eventName, location, eventDate, eventTime, entryFee;
        ImageView image;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            eventName = itemView.findViewById(R.id.custom_product_name);
            location = itemView.findViewById(R.id.custom_product_vendor_name);
            eventDate = itemView.findViewById(R.id.custom_product_price);
            eventTime = itemView.findViewById(R.id.custom_product_discount_price);
            entryFee = itemView.findViewById(R.id.custom_product_discount);
            image = itemView.findViewById(R.id.custom_product_image);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ProductCategoryAdapter.MyHolder holder, int position) {
        holder.eventName.setText(arrayList.get(position).getProductName());
        holder.location.setText(arrayList.get(position).getVendorName());
        holder.eventDate.setText(arrayList.get(position).getProductPrice());
        holder.eventTime.setText(arrayList.get(position).getProductDiscountPrice());
        
        String fee = arrayList.get(position).getDiscount();
        if(fee.equalsIgnoreCase("Free")) {
            holder.entryFee.setText(fee);
        } else {
            holder.entryFee.setText("₹" + fee);
        }

        // Removed strike-thru as it's not a price comparison anymore
        holder.eventDate.setPaintFlags(0);
        
        Glide.with(context).load(arrayList.get(position).getImage()).placeholder(R.mipmap.ic_launcher).into(holder.image);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = holder.getAdapterPosition();
                sp.edit().putInt(ConstantSP.PRODUCTID, arrayList.get(pos).getProductID()).commit();
                sp.edit().putString(ConstantSP.PRODUCTNAME, arrayList.get(pos).getProductName()).commit();
                sp.edit().putString(ConstantSP.PRODUCTVENDORNAME, arrayList.get(pos).getVendorName()).commit();
                sp.edit().putString(ConstantSP.PRODUCTPRICE, arrayList.get(pos).getProductPrice()).commit();
                sp.edit().putString(ConstantSP.PRODUCTDISCOUNTPRICE, arrayList.get(pos).getProductDiscountPrice()).commit();
                sp.edit().putString(ConstantSP.PRODUCTDISCOUNT, arrayList.get(pos).getDiscount()).commit();
                sp.edit().putString(ConstantSP.PRODUCTIMAGE, arrayList.get(pos).getImage()).commit();

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
