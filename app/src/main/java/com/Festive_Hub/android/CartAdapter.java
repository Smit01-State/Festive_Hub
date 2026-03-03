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

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyHolder> {

    Context context;
    ArrayList<CartList> arrayList;
    SharedPreferences sp;
    SQLiteDatabase db;
    CartActivity cartActivity;

    public CartAdapter(Context context, ArrayList<CartList> arrayList, CartActivity cartActivity) {
        this.context = context;
        this.arrayList = arrayList;
        this.cartActivity = cartActivity;
        sp = context.getSharedPreferences(ConstantSP.PREF, Context.MODE_PRIVATE);
        db = context.openOrCreateDatabase("finalApp", Context.MODE_PRIVATE, null);
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_cart, parent, false);
        return new MyHolder(view);
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView name, vendor, discPrice, origPrice, discountTag, quantity;
        ImageView image, delete, plus, minus;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.custom_cart_name);
            vendor = itemView.findViewById(R.id.custom_cart_vendor);
            discPrice = itemView.findViewById(R.id.custom_cart_discount_price);
            origPrice = itemView.findViewById(R.id.custom_cart_original_price);
            discountTag = itemView.findViewById(R.id.custom_cart_discount_tag);
            image = itemView.findViewById(R.id.custom_cart_image);
            delete = itemView.findViewById(R.id.custom_cart_delete);
            quantity = itemView.findViewById(R.id.custom_cart_quantity);
            plus = itemView.findViewById(R.id.custom_cart_plus);
            minus = itemView.findViewById(R.id.custom_cart_minus);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        CartList cart = arrayList.get(position);
        holder.name.setText(cart.getEventName());
        holder.vendor.setText(cart.getVendorName());
        holder.discPrice.setText(ConstantSP.PRICE_SYMBOL + cart.getEventDiscountPrice());
        holder.origPrice.setText(ConstantSP.PRICE_SYMBOL + cart.getEventPrice());
        holder.discountTag.setText(cart.getDiscount() + "% OFF");
        holder.quantity.setText(String.valueOf(cart.getQuantity()));

        // Strikethrough for original price
        holder.origPrice.setPaintFlags(holder.origPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        Glide.with(context).load(cart.getImage()).placeholder(R.mipmap.ic_launcher).into(holder.image);

        // Click on item → Detail Page
        holder.itemView.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();
            sp.edit().putInt(ConstantSP.PRODUCTID, arrayList.get(pos).getEventID()).commit();
            sp.edit().putString(ConstantSP.PRODUCTNAME, arrayList.get(pos).getEventName()).commit();
            sp.edit().putString(ConstantSP.PRODUCTVENDORNAME, arrayList.get(pos).getVendorName()).commit();
            sp.edit().putString(ConstantSP.PRODUCTPRICE, arrayList.get(pos).getEventPrice()).commit();
            sp.edit().putString(ConstantSP.PRODUCTDISCOUNTPRICE, arrayList.get(pos).getEventDiscountPrice()).commit();
            sp.edit().putString(ConstantSP.PRODUCTDISCOUNT, arrayList.get(pos).getDiscount()).commit();
            sp.edit().putString(ConstantSP.PRODUCTIMAGE, arrayList.get(pos).getImage()).commit();

            Intent intent = new Intent(context, EventDetailActivity.class);
            context.startActivity(intent);
        });

        // Plus Click
        holder.plus.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();
            int newQty = arrayList.get(pos).getQuantity() + 1;
            updateQty(pos, newQty);
        });

        // Minus Click
        holder.minus.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();
            int currentQty = arrayList.get(pos).getQuantity();
            if (currentQty > 1) {
                updateQty(pos, currentQty - 1);
            }
        });

        // Delete Click
        holder.delete.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();
            int eventID = arrayList.get(pos).getEventID();
            db.execSQL("DELETE FROM CART WHERE EVENTID = " + eventID);
            arrayList.remove(pos);
            notifyItemRemoved(pos);
            notifyItemRangeChanged(pos, arrayList.size());
            cartActivity.updateTotal();
            Toast.makeText(context, "Removed from Cart", Toast.LENGTH_SHORT).show();
        });
    }

    private void updateQty(int position, int newQty) {
        int eventID = arrayList.get(position).getEventID();
        db.execSQL("UPDATE CART SET QTY = " + newQty + " WHERE EVENTID = " + eventID);
        arrayList.get(position).setQuantity(newQty);
        notifyItemChanged(position);
        cartActivity.updateTotal();
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
