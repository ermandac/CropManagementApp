package com.cma.thesis.cropmanagementapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class AdapterPlanner extends BaseAdapter
{
    public AdapterPlanner(Context context, int layout, ArrayList<Class_Planner> plannerList, OnDeleteClickListener deleteListener, OnEditClickListener editListener) {
        this.context = context;
        this.layout = layout;
        this.plannerList = plannerList;
        this.deleteListener = deleteListener;
        this.editListener = editListener;
    }

    private Context context;
    private  int layout;
    private ArrayList<Class_Planner> plannerList;
    private OnDeleteClickListener deleteListener;
    private OnEditClickListener editListener;

    public interface OnDeleteClickListener {
        void onDeleteClick(Class_Planner plan, int position);
    }

    public interface OnEditClickListener {
        void onEditClick(Class_Planner plan, int position);
    }

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
    public View getView(final int position, View view, ViewGroup viewGroup) {

        View row = view;
        viewHolder vh;

        if(row == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout,null);

            vh = new viewHolder();
            vh.txtPlan = (TextView)row.findViewById(R.id.txtplan);
            vh.txtPlantingMethod = (TextView)row.findViewById(R.id.txtPlantingMethod);
            vh.btnEdit = (ImageButton)row.findViewById(R.id.btnEditPlan);
            vh.btnDelete = (ImageButton)row.findViewById(R.id.btnDeletePlan);

            row.setTag(vh);
        }
        else
        {
            vh = (viewHolder) row.getTag();
        }

        final Class_Planner plan = plannerList.get(position);

        // Format the date range: "Start Date - End Date"
        String dateRange = formatDateRange(plan.getStartDate(), plan.getEndDate());
        vh.txtPlan.setText(dateRange);

        // Display planting method
        String plantingMethodText = formatPlantingMethod(plan.getPlantingMethod());
        vh.txtPlantingMethod.setText(plantingMethodText);

        // Set up edit button click listener
        vh.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editListener != null) {
                    editListener.onEditClick(plan, position);
                }
            }
        });

        // Set up delete button click listener
        vh.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deleteListener != null) {
                    deleteListener.onDeleteClick(plan, position);
                }
            }
        });

        return row;
    }

    private String formatDateRange(String startDate, String endDate) {
        if (startDate != null && endDate != null && !startDate.isEmpty() && !endDate.isEmpty()) {
            return startDate + " - " + endDate;
        } else if (startDate != null && !startDate.isEmpty()) {
            return startDate;
        } else {
            return "No date";
        }
    }

    private String formatPlantingMethod(String method) {
        if (method == null || method.isEmpty()) {
            return "Method: Not specified";
        }
        if (method.equals("SABONG_TANIM")) {
            return "Method: Sabong Tanim (Direct Seeding)";
        } else if (method.equals("LIPAT_TANIM")) {
            return "Method: Lipat Tanim (Transplanting)";
        }
        return "Method: " + method;
    }

    private class viewHolder
    {
        TextView txtPlan;
        TextView txtPlantingMethod;
        ImageButton btnEdit;
        ImageButton btnDelete;
    }
}
