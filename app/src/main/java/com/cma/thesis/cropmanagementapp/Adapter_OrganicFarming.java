package com.cma.thesis.cropmanagementapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Adapter_OrganicFarming extends BaseAdapter {
    
    private Context context;
    private List<Class_OrganicFarming> organicList;
    private LayoutInflater inflater;

    public Adapter_OrganicFarming(Context context, int layout, List<Class_OrganicFarming> organicList) {
        this.context = context;
        this.organicList = organicList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return organicList.size();
    }

    @Override
    public Object getItem(int position) {
        return organicList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_organic_farming_item, parent, false);
            holder = new ViewHolder();
            holder.organicIcon = convertView.findViewById(R.id.organic_icon);
            holder.organicName = convertView.findViewById(R.id.organic_name);
            holder.organicType = convertView.findViewById(R.id.organic_type);
            holder.organicCategory = convertView.findViewById(R.id.organic_category);
            holder.organicDescription = convertView.findViewById(R.id.organic_description);
            holder.organicMaterials = convertView.findViewById(R.id.organic_materials);
            holder.organicBenefits = convertView.findViewById(R.id.organic_benefits);
            holder.organicApplicationMethod = convertView.findViewById(R.id.organic_application_method);
            holder.organicDuration = convertView.findViewById(R.id.organic_duration);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Class_OrganicFarming item = organicList.get(position);

        // Set icon
        holder.organicIcon.setImageResource(R.drawable.ic_organic);

        // Set name
        holder.organicName.setText(item.getName() != null ? item.getName() : "N/A");

        // Set type
        holder.organicType.setText(item.getType() != null ? item.getType() : "N/A");

        // Set category
        holder.organicCategory.setText(item.getCategory() != null ? item.getCategory() : "N/A");

        // Set description
        holder.organicDescription.setText(item.getDescription() != null ? item.getDescription() : "N/A");

        // Set materials using helper method
        holder.organicMaterials.setText(item.getMaterialsString());

        // Set benefits
        holder.organicBenefits.setText(item.getBenefits() != null ? item.getBenefits() : "N/A");

        // Set application method
        holder.organicApplicationMethod.setText(item.getApplicationMethod() != null ? item.getApplicationMethod() : "N/A");

        // Set duration
        holder.organicDuration.setText(item.getDuration() != null ? item.getDuration() : "N/A");

        return convertView;
    }

    public void updateOrganicList(List<Class_OrganicFarming> newList) {
        this.organicList = newList;
        notifyDataSetChanged();
    }

    static class ViewHolder {
        ImageView organicIcon;
        TextView organicName;
        TextView organicType;
        TextView organicCategory;
        TextView organicDescription;
        TextView organicMaterials;
        TextView organicBenefits;
        TextView organicApplicationMethod;
        TextView organicDuration;
    }
}
