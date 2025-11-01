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
 * Updated for Firestore migration - displays full fertilizer information
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

            vh.txtFertilizerName = (TextView) row.findViewById(R.id.txtfertilizer_name);
            vh.txtFertilizerNpk = (TextView) row.findViewById(R.id.txtfertilizer_npk);
            vh.txtFertilizerDescription = (TextView) row.findViewById(R.id.txtfertilizer_description);
            vh.txtFertilizerBenefits = (TextView) row.findViewById(R.id.txtfertilizer_benefits);
            
            // Fallback for old layout (if new TextViews don't exist)
            vh.txtFertilizer = (TextView) row.findViewById(R.id.txtfertilizer);
            
            row.setTag(vh);
        } else {
            vh = (ViewHolder) row.getTag();
        }

        // Use new layout if available, fallback to old layout
        if (vh.txtFertilizerName != null) {
            // New detailed layout
            vh.txtFertilizerName.setText(fertilizer.getName());
            
            if (vh.txtFertilizerNpk != null && fertilizer.getNpk() != null) {
                vh.txtFertilizerNpk.setText("NPK: " + fertilizer.getNpk());
                vh.txtFertilizerNpk.setVisibility(View.VISIBLE);
            }
            
            if (vh.txtFertilizerDescription != null && fertilizer.getDescription() != null) {
                vh.txtFertilizerDescription.setText(fertilizer.getDescription());
                vh.txtFertilizerDescription.setVisibility(View.VISIBLE);
            }
            
            if (vh.txtFertilizerBenefits != null && fertilizer.getBenefits() != null) {
                vh.txtFertilizerBenefits.setText("Benefits: " + fertilizer.getBenefits());
                vh.txtFertilizerBenefits.setVisibility(View.VISIBLE);
            }
        } else if (vh.txtFertilizer != null) {
            // Old simple layout - just show the name
            vh.txtFertilizer.setText(fertilizer.getName());
        }
        
        return row;
    }

    public class ViewHolder {
        // New fields
        TextView txtFertilizerName;
        TextView txtFertilizerNpk;
        TextView txtFertilizerDescription;
        TextView txtFertilizerBenefits;
        
        // Old field for backward compatibility
        TextView txtFertilizer;
    }

}
