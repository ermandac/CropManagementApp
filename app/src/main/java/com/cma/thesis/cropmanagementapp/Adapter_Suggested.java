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

public class Adapter_Suggested extends BaseAdapter {

    public Adapter_Suggested(Context context, int layout, ArrayList<Class_Suggested> suggested) {
        this.context = context;
        this.layout = layout;
        this.suggestedlist = suggested;
    }

    private Context context;
    private int layout;

    private ArrayList<Class_Suggested> suggestedlist;

    @Override
    public int getCount() {
        return suggestedlist.size();
    }

    @Override
    public Object getItem(int position) {
        return suggestedlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {

        View row = view;
        ViewHolder vh = new ViewHolder();
        final Class_Suggested suggested = suggestedlist.get(position);

        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout, null);

            vh.txtsuggestedd = (TextView) row.findViewById(R.id.txtsuggestedcrop);
            vh.image = (ImageView) row.findViewById(R.id.imgsuggestedcrop);

            row.setTag(vh);
        } else {
            vh = (ViewHolder) row.getTag();
        }

        vh.txtsuggestedd.setText(suggested.getCropname());

        byte[] decodedString = Base64.decode(suggested.getImage(), Base64.DEFAULT);
        Bitmap imgBitMap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        vh.image.setImageBitmap(imgBitMap);


        return row;
    }

    public class ViewHolder {
        TextView txtsuggestedd;
        ImageView image;
    }


}
