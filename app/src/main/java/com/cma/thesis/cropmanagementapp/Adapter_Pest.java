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



public class Adapter_Pest extends BaseAdapter
{
    public Adapter_Pest(Context context, int layout, ArrayList<Class_Pest> pestList) {
        this.context = context;
        this.layout = layout;
        this.pestList = pestList;
    }

    private Context context;
    private  int layout;

    private ArrayList<Class_Pest> pestList;

    @Override
    public int getCount() {
        return pestList.size();
    }

    @Override
    public Object getItem(int position) {
        return pestList.get(position);
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

            vh.imgPest = (ImageView)row.findViewById(R.id.imgpest);
            vh.txtPestname = (TextView)row.findViewById(R.id.txtpestname);

            row.setTag(vh);
        }
        else
        {
            vh = (viewHolder) row.getTag();
        }

        Class_Pest pest = pestList.get(position);

        vh.txtPestname.setText(pest.getPestname());
        byte[] decodedString = Base64.decode(pest.getImage(), Base64.DEFAULT);
        Bitmap imgBitMap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        vh.imgPest.setImageBitmap(imgBitMap);

        return row;
    }

    private class viewHolder
    {
        ImageView imgPest;
        TextView txtPestname;
    }
}
