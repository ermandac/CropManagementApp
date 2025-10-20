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

public class Adapter_PestChemical extends BaseAdapter {

    public Adapter_PestChemical(Context context, int layout, ArrayList<Class_PestChemical> chemicalpest) {
        this.context = context;
        this.layout = layout;
        this.chemicalplist = chemicalpest;
    }

    private Context context;
    private int layout;

    private ArrayList<Class_PestChemical> chemicalplist;

    @Override
    public int getCount() {
        return chemicalplist.size();
    }

    @Override
    public Object getItem(int position) {
        return chemicalplist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {

        View row = view;
        ViewHolder vh = new ViewHolder();
        final Class_PestChemical chemicalpestc = chemicalplist.get(position);

        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout, null);

            vh.txtchemicalpest = (TextView) row.findViewById(R.id.txtlistpestchemical);

            row.setTag(vh);
        } else {
            vh = (ViewHolder) row.getTag();
        }

        vh.txtchemicalpest.setText(chemicalpestc.getChemicalpest());

        return row;
    }
    public class ViewHolder {
        TextView txtchemicalpest;
    }
}
