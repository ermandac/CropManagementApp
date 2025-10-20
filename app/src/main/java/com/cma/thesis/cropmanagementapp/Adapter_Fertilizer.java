package com.cma.thesis.cropmanagementapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Mimoy on 4/27/2018.
 */

public class Adapter_Fertilizer extends BaseAdapter {

    public Adapter_Fertilizer(Context context, int layout, ArrayList<Class_Fertilizer> fertilizer) {
        this.context = context;
        this.layout = layout;
        this.fertilizerlist = fertilizer;
    }

    private Context context;
    private int layout;

    private ArrayList<Class_Fertilizer> fertilizerlist;

    @Override
    public int getCount() {
        return fertilizerlist.size();
    }

    @Override
    public Object getItem(int position) {
        return fertilizerlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {

        View row = view;
        ViewHolder vh = new ViewHolder();
        final Class_Fertilizer fertilizer = fertilizerlist.get(position);

        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout, null);

            vh.txtFertilizer = (TextView) row.findViewById(R.id.txtfertilizer);
            row.setTag(vh);
        } else {
            vh = (ViewHolder) row.getTag();
        }

        vh.txtFertilizer.setText(fertilizer.getChemical());
        return row;
    }

    public class ViewHolder {
        TextView txtFertilizer;
    }


}
