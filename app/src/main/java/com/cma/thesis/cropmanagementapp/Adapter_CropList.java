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



public class Adapter_CropList extends BaseAdapter
{

    public Adapter_CropList(Context context, int layout, ArrayList<Class_Crops> croplist) {
        this.context = context;
        this.layout = layout;
        this.croplist = croplist;
    }

    private Context context;
    private  int layout;

    private ArrayList<Class_Crops> croplist;

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
    public View getView(int position, View view, ViewGroup viewGroup) {

        View row = view;
        viewHolder vh = new viewHolder();

        if(row == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout,null);

            vh.imgcrop = (ImageView)row.findViewById(R.id.imgcropview);
            vh.txtcropname = (TextView)row.findViewById(R.id.txtcropname);

            row.setTag(vh);
        }
        else
        {
            vh = (viewHolder) row.getTag();
        }

        Class_Crops crop = croplist.get(position);

        vh.txtcropname.setText(crop.getCropname());
        // Temporarily avoid decoding large blobs on UI thread to prevent ANRs
        vh.imgcrop.setImageResource(R.mipmap.ic_launcher);

        return row;
    }

    private class viewHolder
    {
        ImageView imgcrop;
        TextView txtcropname;
    }

}
