package com.cma.thesis.cropmanagementapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * RecyclerView adapter for displaying admin fertilizer list
 */
public class AdapterAdminFertilizer extends RecyclerView.Adapter<AdapterAdminFertilizer.FertilizerViewHolder> {
    
    private Context context;
    private List<DocumentSnapshot> fertilizerList;
    private List<DocumentSnapshot> fertilizerListFull; // For search functionality
    private OnFertilizerActionListener listener;
    
    public interface OnFertilizerActionListener {
        void onEditClick(DocumentSnapshot fertilizer);
        void onDeleteClick(DocumentSnapshot fertilizer);
    }
    
    public AdapterAdminFertilizer(Context context, OnFertilizerActionListener listener) {
        this.context = context;
        this.fertilizerList = new ArrayList<>();
        this.fertilizerListFull = new ArrayList<>();
        this.listener = listener;
    }
    
    @NonNull
    @Override
    public FertilizerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_admin_fertilizer, parent, false);
        return new FertilizerViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull FertilizerViewHolder holder, int position) {
        DocumentSnapshot fertilizer = fertilizerList.get(position);
        
        // Bind fertilizer data
        String name = fertilizer.getString("name");
        String npk = fertilizer.getString("npk");
        String category = fertilizer.getString("category");
        Boolean isOrganic = fertilizer.getBoolean("isOrganic");
        
        holder.tvFertilizerName.setText(name != null ? name : "Unknown Fertilizer");
        holder.tvNPK.setText(npk != null ? "NPK: " + npk : "NPK: N/A");
        holder.tvCategory.setText(category != null ? category : "N/A");
        
        // Set type (Organic or Chemical)
        String type = (isOrganic != null && isOrganic) ? "Organic" : "Chemical";
        holder.tvType.setText(type);
        
        // Handle edit button click
        holder.btnEdit.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEditClick(fertilizer);
            }
        });
        
        // Handle delete button click
        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteClick(fertilizer);
            }
        });
    }
    
    @Override
    public int getItemCount() {
        return fertilizerList.size();
    }
    
    /**
     * Update the fertilizer list
     */
    public void setFertilizerList(List<DocumentSnapshot> fertilizers) {
        this.fertilizerList = fertilizers;
        this.fertilizerListFull = new ArrayList<>(fertilizers); // Keep copy for search
        notifyDataSetChanged();
    }
    
    /**
     * Filter fertilizers based on search query
     */
    public void filter(String query) {
        fertilizerList.clear();
        
        if (query.isEmpty()) {
            fertilizerList.addAll(fertilizerListFull);
        } else {
            String lowerCaseQuery = query.toLowerCase();
            for (DocumentSnapshot fertilizer : fertilizerListFull) {
                String name = fertilizer.getString("name");
                String category = fertilizer.getString("category");
                String npk = fertilizer.getString("npk");
                
                if ((name != null && name.toLowerCase().contains(lowerCaseQuery)) ||
                    (category != null && category.toLowerCase().contains(lowerCaseQuery)) ||
                    (npk != null && npk.toLowerCase().contains(lowerCaseQuery))) {
                    fertilizerList.add(fertilizer);
                }
            }
        }
        notifyDataSetChanged();
    }
    
    /**
     * Filter fertilizers by type (Organic/Chemical)
     */
    public void filterByType(String filterType) {
        fertilizerList.clear();
        
        if (filterType.equals("All")) {
            fertilizerList.addAll(fertilizerListFull);
        } else {
            boolean isOrganic = filterType.equals("Organic");
            for (DocumentSnapshot fertilizer : fertilizerListFull) {
                Boolean fertilizerIsOrganic = fertilizer.getBoolean("isOrganic");
                if (fertilizerIsOrganic != null && fertilizerIsOrganic == isOrganic) {
                    fertilizerList.add(fertilizer);
                }
            }
        }
        notifyDataSetChanged();
    }
    
    /**
     * ViewHolder class
     */
    static class FertilizerViewHolder extends RecyclerView.ViewHolder {
        TextView tvFertilizerName;
        TextView tvNPK;
        TextView tvCategory;
        TextView tvType;
        ImageButton btnEdit;
        ImageButton btnDelete;
        
        public FertilizerViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFertilizerName = itemView.findViewById(R.id.tvFertilizerName);
            tvNPK = itemView.findViewById(R.id.tvNPK);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            tvType = itemView.findViewById(R.id.tvType);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
