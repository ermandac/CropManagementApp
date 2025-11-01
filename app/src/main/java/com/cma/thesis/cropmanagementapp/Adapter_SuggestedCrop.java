package com.cma.thesis.cropmanagementapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class Adapter_SuggestedCrop extends BaseAdapter {

    private Context context;
    private List<Class_SuggestedCrop> cropsList;
    private LayoutInflater inflater;

    public Adapter_SuggestedCrop(Context context, List<Class_SuggestedCrop> cropsList) {
        this.context = context;
        this.cropsList = cropsList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return cropsList != null ? cropsList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return cropsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_suggested_crop_item, parent, false);
            holder = new ViewHolder();
            holder.cropIcon = convertView.findViewById(R.id.crop_icon);
            holder.cropName = convertView.findViewById(R.id.crop_name);
            holder.cropMonth = convertView.findViewById(R.id.crop_month);
            holder.cropCategory = convertView.findViewById(R.id.crop_category);
            holder.plantingReason = convertView.findViewById(R.id.planting_reason);
            holder.expectedHarvest = convertView.findViewById(R.id.expected_harvest);
            holder.cropDescription = convertView.findViewById(R.id.crop_description);
            holder.bestVarieties = convertView.findViewById(R.id.best_varieties);
            holder.cropTips = convertView.findViewById(R.id.crop_tips);
            holder.popularity = convertView.findViewById(R.id.popularity);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Class_SuggestedCrop crop = cropsList.get(position);

        if (crop != null) {
            // Set crop name
            if (crop.getCropName() != null) {
                holder.cropName.setText(crop.getCropName());
            }

            // Set month
            if (crop.getMonth() != null) {
                holder.cropMonth.setText("Month: " + crop.getMonth());
            }

            // Set category
            if (crop.getCategory() != null) {
                holder.cropCategory.setText(crop.getCategory());
            }

            // Set planting reason
            if (crop.getPlantingReason() != null) {
                holder.plantingReason.setText(crop.getPlantingReason());
            }

            // Set expected harvest
            if (crop.getExpectedHarvest() != null) {
                holder.expectedHarvest.setText(crop.getExpectedHarvest());
            }

            // Set description
            if (crop.getDescription() != null) {
                holder.cropDescription.setText(crop.getDescription());
            }

            // Set best varieties
            if (crop.getBestVarieties() != null && !crop.getBestVarieties().isEmpty()) {
                holder.bestVarieties.setText(crop.getVarietiesString());
            } else {
                holder.bestVarieties.setText("No varieties listed");
            }

            // Set tips
            if (crop.getTips() != null) {
                holder.cropTips.setText(crop.getTips());
            }

            // Set popularity
            if (crop.getPopularity() != null) {
                holder.popularity.setText(crop.getPopularity());
                // Set background color based on popularity
                switch (crop.getPopularity()) {
                    case "Very High":
                        holder.popularity.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
                        break;
                    case "High":
                        holder.popularity.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
                        break;
                    case "Medium":
                        holder.popularity.setBackgroundColor(context.getResources().getColor(android.R.color.holo_orange_dark));
                        break;
                    default:
                        holder.popularity.setBackgroundColor(context.getResources().getColor(android.R.color.darker_gray));
                        break;
                }
            }
        }

        return convertView;
    }

    /**
     * Update the list of crops and refresh the adapter
     */
    public void updateCropsList(List<Class_SuggestedCrop> newCropsList) {
        this.cropsList = newCropsList;
        notifyDataSetChanged();
    }

    /**
     * ViewHolder pattern for better performance
     */
    static class ViewHolder {
        ImageView cropIcon;
        TextView cropName;
        TextView cropMonth;
        TextView cropCategory;
        TextView plantingReason;
        TextView expectedHarvest;
        TextView cropDescription;
        TextView bestVarieties;
        TextView cropTips;
        TextView popularity;
    }
}
