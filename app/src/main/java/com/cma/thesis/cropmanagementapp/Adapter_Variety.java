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

public class Adapter_Variety extends BaseAdapter {

    public Adapter_Variety(Context context, int layout, ArrayList<Class_Variety> varietycrop) {
        this.context = context;
        this.layout = layout;
        this.varietylist = varietycrop;
    }

    private Context context;
    private int layout;

    private ArrayList<Class_Variety> varietylist;

    @Override
    public int getCount() {
        return varietylist.size();
    }

    @Override
    public Object getItem(int position) {
        return varietylist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {

        View row = view;
        ViewHolder vh = new ViewHolder();
        final Class_Variety varieties = varietylist.get(position);

        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout, null);

            vh.txtvariety = (TextView) row.findViewById(R.id.txtlistvarietyone);

            row.setTag(vh);
        } else {
            vh = (ViewHolder) row.getTag();
        }

        vh.txtvariety.setText(varieties.getVariety());

        return row;
    }
    public class ViewHolder {
        TextView txtvariety;
    }
}
