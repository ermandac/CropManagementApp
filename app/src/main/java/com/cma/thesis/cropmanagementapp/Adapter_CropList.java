package com.cma.thesis.cropmanagementapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class Adapter_CropList extends BaseAdapter {

    private Context context;
    private ArrayList<Class_Crops> croplist;
    private LayoutInflater inflater;

    public Adapter_CropList(Context context, int layout, ArrayList<Class_Crops> croplist) {
        this.context = context;
        this.croplist = croplist;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return croplist.size();
    }

    @Override
    public Object getItem(int position) {
        return croplist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_crop_card_item, parent, false);
            holder = new ViewHolder();
            holder.cropIcon = convertView.findViewById(R.id.crop_icon);
            holder.cropName = convertView.findViewById(R.id.crop_name);
            holder.scientificName = convertView.findViewById(R.id.scientific_name);
            holder.climate = convertView.findViewById(R.id.climate);
            holder.season = convertView.findViewById(R.id.season);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Class_Crops crop = croplist.get(position);

        // Set crop name
        holder.cropName.setText(crop.getCropname() != null ? crop.getCropname() : "Unknown Crop");

        // Set scientific name
        holder.scientificName.setText(crop.getscienceName() != null ? crop.getscienceName() : "N/A");

        // Set climate
        holder.climate.setText(crop.getSoil_climate() != null ? crop.getSoil_climate() : "N/A");

        // Set season
        holder.season.setText(crop.getSeason() != null ? crop.getSeason() : "N/A");

        // Set crop icon (placeholder for now)
        holder.cropIcon.setImageResource(R.drawable.plants);

        return convertView;
    }

    static class ViewHolder {
        ImageView cropIcon;
        TextView cropName;
        TextView scientificName;
        TextView climate;
        TextView season;
    }
}
