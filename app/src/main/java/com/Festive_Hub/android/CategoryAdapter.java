package com.Festive_Hub.android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class CategoryAdapter extends BaseAdapter {

    Context context;
    ArrayList<CategoryList> arrayList;
    public CategoryAdapter(Context context, ArrayList<CategoryList> arrayList) {

    this.context = context;
    this.arrayList = arrayList;

    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        convertView =  layoutInflater.inflate(R.layout.custom_category,null);

        ImageView imageView = convertView.findViewById(R.id.custom_category_image);
        TextView name = convertView.findViewById(R.id.custom_category_name);

        name.setText(arrayList.get(position).getName());
        Glide.with(context).load(arrayList.get(position).getImage()).placeholder(R.mipmap.app_icon).into(imageView);


        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, arrayList.get(position).getName(), Toast.LENGTH_SHORT).show();
            }
        });

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, arrayList.get(position).getImage(), Toast.LENGTH_SHORT).show();
            }
        });


        return convertView;
    }
}
