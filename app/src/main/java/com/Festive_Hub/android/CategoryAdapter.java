package com.Festive_Hub.android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class CategoryAdapter extends BaseAdapter {

    Context context;
    String[] nameArray;
    String[] imageArray;
    public CategoryAdapter(Context context, String[] nameArray, String[] imageArray) {

    this.context = context;
    this.nameArray = nameArray;
    this.imageArray = imageArray;

    }

    @Override
    public int getCount() {
        return nameArray.length;
    }

    @Override
    public Object getItem(int position) {
        return nameArray[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        convertView = layoutInflater.inflate(R.layout.custom_category,null);

        ImageView imageView = convertView.findViewById(R.id.custom_category_image);
        TextView name = convertView.findViewById(R.id.custom_category_name);

        name.setText(nameArray[position]);
        Glide.with(context).load(imageArray[position]).placeholder(R.mipmap.app_icon).into(imageView);

        return convertView;
    }
}
