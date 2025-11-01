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
 * RecyclerView adapter for displaying admin pest list
 */
public class AdapterAdminPest extends RecyclerView.Adapter<AdapterAdminPest.PestViewHolder> {
    
    private Context context;
    private List<DocumentSnapshot> pestList;
    private List<DocumentSnapshot> pestListFull; // For search functionality
    private OnPestActionListener listener;
    
    public interface OnPestActionListener {
        void onEditClick(DocumentSnapshot pest);
        void onDeleteClick(DocumentSnapshot pest);
    }
    
    public AdapterAdminPest(Context context, OnPestActionListener listener) {
        this.context = context;
        this.pestList = new ArrayList<>();
        this.pestListFull = new ArrayList<>();
        this.listener = listener;
    }
    
    @NonNull
    @Override
    public PestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_admin_pest, parent, false);
        return new PestViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull PestViewHolder holder, int position) {
        DocumentSnapshot pest = pestList.get(position);
        
        // Bind pest data
        String pestName = pest.getString("pestName");
        String scientificName = pest.getString("scientificName");
        String category = pest.getString("category");
        String severity = pest.getString("severity");
        
        holder.tvPestName.setText(pestName != null ? pestName : "Unknown Pest");
        holder.tvScientificName.setText(scientificName != null ? scientificName : "");
        holder.tvCategory.setText(category != null ? category : "N/A");
        holder.tvSeverity.setText(severity != null ? severity : "N/A");
        
        // Handle edit button click
        holder.btnEdit.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEditClick(pest);
            }
        });
        
        // Handle delete button click
        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteClick(pest);
            }
        });
    }
    
    @Override
    public int getItemCount() {
        return pestList.size();
    }
    
    /**
     * Update the pest list
     */
    public void setPestList(List<DocumentSnapshot> pests) {
        this.pestList = pests;
        this.pestListFull = new ArrayList<>(pests); // Keep copy for search
        notifyDataSetChanged();
    }
    
    /**
     * Filter pests based on search query
     */
    public void filter(String query) {
        pestList.clear();
        
        if (query.isEmpty()) {
            pestList.addAll(pestListFull);
        } else {
            String lowerCaseQuery = query.toLowerCase();
            for (DocumentSnapshot pest : pestListFull) {
                String pestName = pest.getString("pestName");
                String scientificName = pest.getString("scientificName");
                String category = pest.getString("category");
                
                if ((pestName != null && pestName.toLowerCase().contains(lowerCaseQuery)) ||
                    (scientificName != null && scientificName.toLowerCase().contains(lowerCaseQuery)) ||
                    (category != null && category.toLowerCase().contains(lowerCaseQuery))) {
                    pestList.add(pest);
                }
            }
        }
        notifyDataSetChanged();
    }
    
    /**
     * ViewHolder class
     */
    static class PestViewHolder extends RecyclerView.ViewHolder {
        TextView tvPestName;
        TextView tvScientificName;
        TextView tvCategory;
        TextView tvSeverity;
        ImageButton btnEdit;
        ImageButton btnDelete;
        
        public PestViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPestName = itemView.findViewById(R.id.tvPestName);
            tvScientificName = itemView.findViewById(R.id.tvScientificName);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            tvSeverity = itemView.findViewById(R.id.tvSeverity);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
