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

public class Adapter_CropMaterials extends BaseAdapter {

    public Adapter_CropMaterials(Context context, int layout, ArrayList<Class_CropMaterials> classcropmaterial) {
        this.context = context;
        this.layout = layout;
        this.cropmaterialLIst = classcropmaterial;
    }

    private Context context;
    private int layout;

    private ArrayList<Class_CropMaterials> cropmaterialLIst;

    @Override
    public int getCount() {
        return cropmaterialLIst.size();
    }

    @Override
    public Object getItem(int position) {
        return cropmaterialLIst.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {

        View row = view;
        ViewHolder vh = new ViewHolder();
        final Class_CropMaterials weeds = cropmaterialLIst.get(position);

        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout, null);

            vh.txtcropmaterial = (TextView) row.findViewById(R.id.txtcropmaterial);

            row.setTag(vh);
        } else {
            vh = (ViewHolder) row.getTag();
        }

        vh.txtcropmaterial.setText(weeds.getMaterials());

        return row;
    }

    public class ViewHolder {
        TextView txtcropmaterial;
    }
}
