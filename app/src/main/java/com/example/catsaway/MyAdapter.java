package com.example.catsaway;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MyAdapter extends BaseAdapter {

    private ArrayList<DataClass> dataList;
    private Context context;
    LayoutInflater layoutInflater;

    public MyAdapter(Context context, ArrayList<DataClass> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }


    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (layoutInflater == null) {
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (view == null) {
            view = layoutInflater.inflate(R.layout.grid_item, null);
        }

        ImageView gridImage = view.findViewById(R.id.gridImage);
        TextView timestamp = view.findViewById(R.id.timestamp);

        Glide.with(context).load(dataList.get(i).getImageURL()).into(gridImage);

        // Convert timestamp to formatted date
        long timestampMillis = dataList.get(i).getTimestamp();
        String formattedDate = formatDate(timestampMillis);
        timestamp.setText(formattedDate);

        return view;
    }

    // Helper method to format date
    private String formatDate(long timestampMillis) {
        // Define the date format you want
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());

        // Create a Date object using the timestamp
        Date date = new Date(timestampMillis);

        // Format the date as a string
        return sdf.format(date);
    }
}