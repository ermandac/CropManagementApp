package com.cma.thesis.cropmanagementapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for displaying organic farming items in admin list
 */
public class AdapterAdminOrganic extends BaseAdapter {
    
    private Context context;
    private List<Class_OrganicFarming> organicList;
    private List<Class_OrganicFarming> originalList;
    private OnItemActionListener listener;
    
    public interface OnItemActionListener {
        void onEdit(Class_OrganicFarming item);
        void onDelete(Class_OrganicFarming item);
    }
    
    public AdapterAdminOrganic(Context context, List<Class_OrganicFarming> organicList, OnItemActionListener listener) {
        this.context = context;
        this.organicList = new ArrayList<>(organicList);
        this.originalList = new ArrayList<>(organicList);
        this.listener = listener;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_admin_organic, parent, false);
            holder = new ViewHolder();
            holder.tvName = convertView.findViewById(R.id.tv_organic_name);
            holder.tvType = convertView.findViewById(R.id.tv_organic_type);
            holder.tvCategory = convertView.findViewById(R.id.tv_organic_category);
            holder.tvDescription = convertView.findViewById(R.id.tv_organic_description);
            holder.tvMaterialsCount = convertView.findViewById(R.id.tv_organic_materials_count);
            holder.tvDuration = convertView.findViewById(R.id.tv_organic_duration);
            holder.btnEdit = convertView.findViewById(R.id.btn_edit_organic);
            holder.btnDelete = convertView.findViewById(R.id.btn_delete_organic);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        
        Class_OrganicFarming item = organicList.get(position);
        
        // Populate views
        holder.tvName.setText(item.getName() != null ? item.getName() : "N/A");
        holder.tvType.setText(item.getType() != null ? item.getType() : "N/A");
        holder.tvCategory.setText("Category: " + (item.getCategory() != null ? item.getCategory() : "N/A"));
        holder.tvDescription.setText(item.getDescription() != null ? item.getDescription() : "No description");
        
        // Materials count
        if (item.getMaterials() != null && !item.getMaterials().isEmpty()) {
            int count = item.getMaterials().size();
            holder.tvMaterialsCount.setText(count + (count == 1 ? " material required" : " materials required"));
            holder.tvMaterialsCount.setVisibility(View.VISIBLE);
        } else {
            holder.tvMaterialsCount.setVisibility(View.GONE);
        }
        
        // Duration
        if (item.getDuration() != null && !item.getDuration().isEmpty()) {
            holder.tvDuration.setText("Duration: " + item.getDuration());
            holder.tvDuration.setVisibility(View.VISIBLE);
        } else {
            holder.tvDuration.setVisibility(View.GONE);
        }
        
        // Button listeners
        holder.btnEdit.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEdit(item);
            }
        });
        
        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDelete(item);
            }
        });
        
        return convertView;
    }
    
    public void filter(String query) {
        organicList.clear();
        if (query.isEmpty()) {
            organicList.addAll(originalList);
        } else {
            String lowerQuery = query.toLowerCase();
            for (Class_OrganicFarming item : originalList) {
                if (item.getName() != null && item.getName().toLowerCase().contains(lowerQuery)) {
                    organicList.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }
    
    public void filterByType(String type) {
        organicList.clear();
        if (type == null || type.isEmpty() || type.equalsIgnoreCase("All")) {
            organicList.addAll(originalList);
        } else {
            for (Class_OrganicFarming item : originalList) {
                if (item.getType() != null && item.getType().equalsIgnoreCase(type)) {
                    organicList.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }
    
    public void updateData(List<Class_OrganicFarming> newList) {
        this.organicList.clear();
        this.originalList.clear();
        this.organicList.addAll(newList);
        this.originalList.addAll(newList);
        notifyDataSetChanged();
    }
    
    static class ViewHolder {
        TextView tvName;
        TextView tvType;
        TextView tvCategory;
        TextView tvDescription;
        TextView tvMaterialsCount;
        TextView tvDuration;
        Button btnEdit;
        Button btnDelete;
    }
}
