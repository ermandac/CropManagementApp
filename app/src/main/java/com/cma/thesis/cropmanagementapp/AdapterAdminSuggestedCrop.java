package com.cma.thesis.cropmanagementapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for displaying suggested crops in admin panel
 */
public class AdapterAdminSuggestedCrop extends RecyclerView.Adapter<AdapterAdminSuggestedCrop.ViewHolder> {
    
    private Context context;
    private List<Class_SuggestedCrop> cropList;
    private List<Class_SuggestedCrop> filteredList;
    private OnSuggestedCropActionListener listener;
    
    public interface OnSuggestedCropActionListener {
        void onEditClick(Class_SuggestedCrop crop);
        void onDeleteClick(Class_SuggestedCrop crop);
    }
    
    public AdapterAdminSuggestedCrop(Context context, OnSuggestedCropActionListener listener) {
        this.context = context;
        this.listener = listener;
        this.cropList = new ArrayList<>();
        this.filteredList = new ArrayList<>();
    }
    
    public void setSuggestedCropList(List<Class_SuggestedCrop> crops) {
        this.cropList = crops;
        this.filteredList = new ArrayList<>(crops);
        notifyDataSetChanged();
    }
    
    public void filter(String query) {
        filteredList.clear();
        if (query == null || query.isEmpty()) {
            filteredList.addAll(cropList);
        } else {
            String lowerCaseQuery = query.toLowerCase();
            for (Class_SuggestedCrop crop : cropList) {
                if (crop.getCropName() != null && crop.getCropName().toLowerCase().contains(lowerCaseQuery)) {
                    filteredList.add(crop);
                }
            }
        }
        notifyDataSetChanged();
    }
    
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_admin_suggested_crop, parent, false);
        return new ViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Class_SuggestedCrop crop = filteredList.get(position);
        
        holder.tvCropName.setText(crop.getCropName() != null ? crop.getCropName() : "Unknown");
        holder.tvMonth.setText(crop.getMonth() != null ? crop.getMonth() : "N/A");
        holder.tvCategory.setText(crop.getCategory() != null ? crop.getCategory() : "N/A");
        
        holder.btnEdit.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEditClick(crop);
            }
        });
        
        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteClick(crop);
            }
        });
    }
    
    @Override
    public int getItemCount() {
        return filteredList.size();
    }
    
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCropName;
        TextView tvMonth;
        TextView tvCategory;
        ImageButton btnEdit;
        ImageButton btnDelete;
        
        ViewHolder(View itemView) {
            super(itemView);
            tvCropName = itemView.findViewById(R.id.tvCropName);
            tvMonth = itemView.findViewById(R.id.tvMonth);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
