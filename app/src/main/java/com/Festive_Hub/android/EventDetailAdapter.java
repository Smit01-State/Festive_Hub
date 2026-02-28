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

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class EventDetailAdapter extends RecyclerView.Adapter<EventDetailAdapter.MyHolder> {
    Context context;
    ArrayList<EventList> arrayList;
    SharedPreferences sp;

    public EventDetailAdapter(Context context, ArrayList<EventList> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        sp = context.getSharedPreferences(ConstantSP.PREF, Context.MODE_PRIVATE);
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_eventdetail, parent, false);
        return new MyHolder(view);
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView name, vendor, discPrice, origPrice, discountTag;
        ImageView image;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.custom_event_name);
            vendor = itemView.findViewById(R.id.custom_event_vendor);
            discPrice = itemView.findViewById(R.id.custom_event_discount_price);
            origPrice = itemView.findViewById(R.id.custom_event_original_price);
            discountTag = itemView.findViewById(R.id.custom_event_discount_tag);
            image = itemView.findViewById(R.id.custom_event_image);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        EventList event = arrayList.get(position);
        holder.name.setText(event.getEventName());
        holder.vendor.setText(event.getVendorName());
        holder.discPrice.setText(ConstantSP.PRICE_SYMBOL + event.getEventDiscountPrice());
        holder.origPrice.setText(ConstantSP.PRICE_SYMBOL + event.getEventPrice());
        holder.discountTag.setText(event.getDiscount() + "% OFF");

        // Strikethrough for original price
        holder.origPrice.setPaintFlags(holder.origPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        Glide.with(context).load(event.getImage()).placeholder(R.mipmap.ic_launcher).into(holder.image);

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
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
