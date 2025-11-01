package com.cma.thesis.cropmanagementapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class Adapter_Pest extends BaseAdapter {

    private Context context;
    private List<Class_Pest> pestsList;
    private LayoutInflater inflater;

    // Constructor for new Firestore-based implementation
    public Adapter_Pest(Context context, List<Class_Pest> pestsList) {
        this.context = context;
        this.pestsList = pestsList;
        this.inflater = LayoutInflater.from(context);
    }

    // Legacy constructor for backwards compatibility with Activity_PestList
    // The layoutResourceId parameter is ignored - using list_pest_item.xml
    public Adapter_Pest(Context context, int layoutResourceId, List<Class_Pest> pestsList) {
        this.context = context;
        this.pestsList = pestsList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return pestsList != null ? pestsList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return pestsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_pest_item, parent, false);
            holder = new ViewHolder();
            holder.pestIcon = convertView.findViewById(R.id.pest_icon);
            holder.pestName = convertView.findViewById(R.id.pest_name);
            holder.scientificName = convertView.findViewById(R.id.scientific_name);
            holder.category = convertView.findViewById(R.id.category);
            holder.affectedCrops = convertView.findViewById(R.id.affected_crops);
            holder.symptoms = convertView.findViewById(R.id.symptoms);
            holder.controlMethods = convertView.findViewById(R.id.control_methods);
            holder.preventionTips = convertView.findViewById(R.id.prevention_tips);
            holder.commonSeason = convertView.findViewById(R.id.common_season);
            holder.severityBadge = convertView.findViewById(R.id.severity_badge);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Class_Pest pest = pestsList.get(position);

        if (pest != null) {
            // Set pest icon
            holder.pestIcon.setImageResource(R.drawable.ic_category_pest);

            // Set pest name
            if (pest.getPestName() != null) {
                holder.pestName.setText(pest.getPestName());
            }

            // Set scientific name
            if (pest.getScientificName() != null) {
                holder.scientificName.setText(pest.getScientificName());
            }

            // Set category
            if (pest.getCategory() != null) {
                holder.category.setText(pest.getCategory());
            }

            // Set affected crops
            if (pest.getAffectedCrops() != null && !pest.getAffectedCrops().isEmpty()) {
                holder.affectedCrops.setText(pest.getAffectedCropsString());
            } else {
                holder.affectedCrops.setText("No crops listed");
            }

            // Set symptoms
            if (pest.getSymptoms() != null) {
                holder.symptoms.setText(pest.getSymptoms());
            }

            // Set control methods
            if (pest.getControlMethods() != null && !pest.getControlMethods().isEmpty()) {
                holder.controlMethods.setText(pest.getControlMethodsString());
            } else {
                holder.controlMethods.setText("No control methods listed");
            }

            // Set prevention tips
            if (pest.getPreventionTips() != null) {
                holder.preventionTips.setText(pest.getPreventionTips());
            }

            // Set common season
            if (pest.getCommonSeason() != null) {
                holder.commonSeason.setText(pest.getCommonSeason());
            }

            // Set severity badge with color
            if (pest.getSeverity() != null) {
                holder.severityBadge.setText(pest.getSeverity());
                // Set background color based on severity
                switch (pest.getSeverity()) {
                    case "Very High":
                        holder.severityBadge.setBackgroundColor(context.getResources().getColor(android.R.color.holo_red_dark));
                        break;
                    case "High":
                        holder.severityBadge.setBackgroundColor(context.getResources().getColor(android.R.color.holo_orange_dark));
                        break;
                    case "Medium":
                        holder.severityBadge.setBackgroundColor(context.getResources().getColor(android.R.color.holo_orange_light));
                        break;
                    default:
                        holder.severityBadge.setBackgroundColor(context.getResources().getColor(android.R.color.darker_gray));
                        break;
                }
            }
        }

        return convertView;
    }

    /**
     * Update the list of pests and refresh the adapter
     */
    public void updatePestsList(List<Class_Pest> newPestsList) {
        this.pestsList = newPestsList;
        notifyDataSetChanged();
    }

    /**
     * ViewHolder pattern for better performance
     */
    static class ViewHolder {
        ImageView pestIcon;
        TextView pestName;
        TextView scientificName;
        TextView category;
        TextView affectedCrops;
        TextView symptoms;
        TextView controlMethods;
        TextView preventionTips;
        TextView commonSeason;
        TextView severityBadge;
    }
}
