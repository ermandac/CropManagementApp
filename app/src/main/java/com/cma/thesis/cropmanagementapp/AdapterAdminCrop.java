package com.cma.thesis.cropmanagementapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * RecyclerView adapter for displaying admin crop list
 */
public class AdapterAdminCrop extends RecyclerView.Adapter<AdapterAdminCrop.CropViewHolder> {
    
    private Context context;
    private List<DocumentSnapshot> cropList;
    private List<DocumentSnapshot> cropListFull; // For search functionality
    private OnCropActionListener listener;
    
    public interface OnCropActionListener {
        void onEditClick(DocumentSnapshot crop);
        void onDeleteClick(DocumentSnapshot crop);
    }
    
    public AdapterAdminCrop(Context context, OnCropActionListener listener) {
        this.context = context;
        this.cropList = new ArrayList<>();
        this.cropListFull = new ArrayList<>();
        this.listener = listener;
    }
    
    @NonNull
    @Override
    public CropViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_admin_crop, parent, false);
        return new CropViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull CropViewHolder holder, int position) {
        DocumentSnapshot crop = cropList.get(position);
        
        // Bind crop data
        String cropName = crop.getString("cropName");
        String scienceName = crop.getString("scienceName");
        String duration = crop.getString("duration");
        String imageUrl = crop.getString("image");
        
        holder.tvCropName.setText(cropName != null ? cropName : "Unknown Crop");
        holder.tvScienceName.setText(scienceName != null ? scienceName : "");
        holder.tvDuration.setText(duration != null ? duration : "");
        
        // Load image using Glide
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(context)
                    .load(imageUrl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.plants)
                    .error(R.drawable.plants)
                    .into(holder.ivCropImage);
        } else {
            holder.ivCropImage.setImageResource(R.drawable.plants);
        }
        
        // Handle edit button click
        holder.btnEdit.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEditClick(crop);
            }
        });
        
        // Handle delete button click
        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteClick(crop);
            }
        });
    }
    
    @Override
    public int getItemCount() {
        return cropList.size();
    }
    
    /**
     * Update the crop list
     */
    public void setCropList(List<DocumentSnapshot> crops) {
        this.cropList = crops;
        this.cropListFull = new ArrayList<>(crops); // Keep copy for search
        notifyDataSetChanged();
    }
    
    /**
     * Filter crops based on search query
     */
    public void filter(String query) {
        cropList.clear();
        
        if (query.isEmpty()) {
            cropList.addAll(cropListFull);
        } else {
            String lowerCaseQuery = query.toLowerCase();
            for (DocumentSnapshot crop : cropListFull) {
                String cropName = crop.getString("cropName");
                String scienceName = crop.getString("scienceName");
                
                if ((cropName != null && cropName.toLowerCase().contains(lowerCaseQuery)) ||
                    (scienceName != null && scienceName.toLowerCase().contains(lowerCaseQuery))) {
                    cropList.add(crop);
                }
            }
        }
        notifyDataSetChanged();
    }
    
    /**
     * ViewHolder class
     */
    static class CropViewHolder extends RecyclerView.ViewHolder {
        ImageView ivCropImage;
        TextView tvCropName;
        TextView tvScienceName;
        TextView tvDuration;
        ImageButton btnEdit;
        ImageButton btnDelete;
        
        public CropViewHolder(@NonNull View itemView) {
            super(itemView);
            ivCropImage = itemView.findViewById(R.id.ivCropImage);
            tvCropName = itemView.findViewById(R.id.tvCropName);
            tvScienceName = itemView.findViewById(R.id.tvScientificName);
            tvDuration = itemView.findViewById(R.id.tvDuration);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
