package com.cma.thesis.cropmanagementapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;



public class AdapterPlanner extends BaseAdapter
{
    public AdapterPlanner(Context context, int layout, ArrayList<Class_Planner> plannerList) {
        this.context = context;
        this.layout = layout;
        this.plannerList = plannerList;
    }

    private Context context;
    private  int layout;
    private ArrayList<Class_Planner> plannerList;

    @Override
    public int getCount() {
        return plannerList.size();
    }

    @Override
    public Object getItem(int position) {
        return plannerList.get(position);
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

            vh.txtPlan = (TextView)row.findViewById(R.id.txtplan);

            row.setTag(vh);
        }
        else
        {
            vh = (viewHolder) row.getTag();
        }

        Class_Planner plan = plannerList.get(position);

        vh.txtPlan.setText(plan.getStartdate());

        return row;
    }

    private class viewHolder
    {
        TextView txtPlan;
    }
}
