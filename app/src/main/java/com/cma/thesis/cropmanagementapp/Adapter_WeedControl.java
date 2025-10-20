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

public class Adapter_WeedControl extends BaseAdapter {

    public Adapter_WeedControl(Context context, int layout, ArrayList<Class_Weeds> weedscrop) {
        this.context = context;
        this.layout = layout;
        this.weedlist = weedscrop;
    }

    private Context context;
    private int layout;

    private ArrayList<Class_Weeds> weedlist;

    @Override
    public int getCount() {
        return weedlist.size();
    }

    @Override
    public Object getItem(int position) {
        return weedlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {

        View row = view;
        ViewHolder vh = new ViewHolder();
        final Class_Weeds weeds = weedlist.get(position);

        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout, null);

            vh.txtweeds = (TextView) row.findViewById(R.id.txtweeds);

            row.setTag(vh);
        } else {
            vh = (ViewHolder) row.getTag();
        }

        vh.txtweeds.setText(weeds.getWeedcontrol());

        return row;
    }

    public class ViewHolder {
        TextView txtweeds;
    }
}
