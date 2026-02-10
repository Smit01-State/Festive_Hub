package com.Festive_Hub.android;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.MyHolder>{

    Context context;

    ArrayList<SubCategoryList> arrayList;
    SharedPreferences sp;
    public SubCategoryAdapter(Context context, ArrayList<SubCategoryList> arrayList) {
        this.context =context;
        this.arrayList=arrayList;


        this.sp = context.getSharedPreferences(ConstantSP.PREF,Context.MODE_PRIVATE);

    }


    @NonNull
    @Override
    public SubCategoryAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sub_custom_category,parent,false);
        return new SubCategoryAdapter.MyHolder(view);
    }

    public class MyHolder extends RecyclerView.ViewHolder{
        
        TextView name;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            
            name = itemView.findViewById(R.id.custom_category_name);

        }
    }

    @Override
    public void onBindViewHolder(@NonNull SubCategoryAdapter.MyHolder holder, int position) {

        holder.name.setText(arrayList.get(position).getName());
        
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sp.edit().putInt(ConstantSP.SUBCATEGORYID, arrayList.get(position).getSubCategoryID()).commit();
                Intent intent = new Intent(context, ProductActivity.class);
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

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


}
