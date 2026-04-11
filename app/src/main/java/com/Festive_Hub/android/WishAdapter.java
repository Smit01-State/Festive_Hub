package com.Festive_Hub.android;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class WishAdapter extends RecyclerView.Adapter<WishAdapter.MyHolder> {

    Context context;
    ArrayList<WishlistList> arrayList;
    SharedPreferences sp;
    SQLiteDatabase db;

    public WishAdapter(Context context, ArrayList<WishlistList> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        sp = context.getSharedPreferences(ConstantSP.PREF, Context.MODE_PRIVATE);
        db = context.openOrCreateDatabase("finalApp", Context.MODE_PRIVATE, null);
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_wish, parent, false);
        return new MyHolder(view);
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView name, vendor, discPrice, origPrice, discountTag;
        ImageView image, heart;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.custom_wish_name);
            vendor = itemView.findViewById(R.id.custom_wish_vendor);
            discPrice = itemView.findViewById(R.id.custom_wish_discount_price);
            origPrice = itemView.findViewById(R.id.custom_wish_original_price);
            discountTag = itemView.findViewById(R.id.custom_wish_discount_tag);
            image = itemView.findViewById(R.id.custom_wish_image);
            heart = itemView.findViewById(R.id.custom_wish_heart);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        WishlistList wish = arrayList.get(position);
        holder.name.setText(wish.getEventName());
        holder.vendor.setText(wish.getVendorName());
        holder.discPrice.setText(ConstantSP.PRICE_SYMBOL + wish.getEventDiscountPrice());
        holder.origPrice.setText(ConstantSP.PRICE_SYMBOL + wish.getEventPrice());
        holder.discountTag.setText(wish.getDiscount() + "% OFF");

        // Strikethrough for original price
        holder.origPrice.setPaintFlags(holder.origPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        Glide.with(context).load(wish.getImage()).placeholder(R.mipmap.ic_launcher).into(holder.image);

        // Click on item → Detail Page
        holder.itemView.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();
            sp.edit().putInt(ConstantSP.EVENTID, arrayList.get(pos).getEventID()).commit();
            sp.edit().putString(ConstantSP.EVENTNAME, arrayList.get(pos).getEventName()).commit();
            sp.edit().putString(ConstantSP.EVENTVENDORNAME, arrayList.get(pos).getVendorName()).commit();
            sp.edit().putString(ConstantSP.EVENTPRICE, arrayList.get(pos).getEventPrice()).commit();
            sp.edit().putString(ConstantSP.EVENTDISCOUNTPRICE, arrayList.get(pos).getEventDiscountPrice()).commit();
            sp.edit().putString(ConstantSP.EVENTDISCOUNT, arrayList.get(pos).getDiscount()).commit();
            sp.edit().putString(ConstantSP.EVENTIMAGE, arrayList.get(pos).getImage()).commit();

            Intent intent = new Intent(context, EventDetailActivity.class);
            context.startActivity(intent);
        });

        // Click on heart → Remove from Wishlist
        holder.heart.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();
            int eventID = arrayList.get(pos).getEventID();
            db.execSQL("DELETE FROM WISHLIST WHERE EVENTID = " + eventID);
            arrayList.remove(pos);
            notifyItemRemoved(pos);
            notifyItemRangeChanged(pos, arrayList.size());
            Toast.makeText(context, "Removed from Wishlist", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
