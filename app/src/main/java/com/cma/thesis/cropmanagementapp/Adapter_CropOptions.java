package com.cma.thesis.cropmanagementapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class Adapter_CropOptions extends BaseAdapter {
    
    private Context context;
    private String[] items;
    private String cropId;
    private int expandedPosition = -1;
    
    // For varieties (position 4)
    private ArrayList<Class_Variety> varietylist;
    private Adapter_Variety varietyAdapter;
    
    // For planting season (position 5)
    private String seasonText = "";
    
    // For planting materials (position 6)
    private ArrayList<Class_CropMaterials> materialsList;
    private Adapter_CropMaterials materialsAdapter;
    
    // For weed control (position 7)
    private ArrayList<Class_Weeds> weedsList;
    private Adapter_WeedControl weedsAdapter;
    
    // For new expandable items (text-only content)
    private String introductionText = "";
    private String scientificNameText = "";
    private String durationText = "";
    private String irrigationText = "";
    private String harvestingText = "";
    
    public Adapter_CropOptions(Context context, String[] items, String cropId) {
        this.context = context;
        this.items = items;
        this.cropId = cropId;
        this.varietylist = new ArrayList<>();
        this.materialsList = new ArrayList<>();
        this.weedsList = new ArrayList<>();
    }
    
    @Override
    public int getCount() {
        return items.length;
    }
    
    @Override
    public Object getItem(int position) {
        return items[position];
    }
    
    @Override
    public long getItemId(int position) {
        return position;
    }
    
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.expandable_list_item, parent, false);
            
            holder = new ViewHolder();
            holder.txtItem = convertView.findViewById(R.id.txtitem);
            holder.expandableSection = convertView.findViewById(R.id.expandable_section);
            holder.expandableTextSection = convertView.findViewById(R.id.expandable_text_section);
            holder.expandableTitle = convertView.findViewById(R.id.expandable_title);
            holder.expandableTextTitle = convertView.findViewById(R.id.expandable_text_title);
            holder.expandableTextContent = convertView.findViewById(R.id.expandable_text_content);
            holder.expandableList = convertView.findViewById(R.id.expandable_list);
            
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        
        holder.txtItem.setText(items[position]);
        
        // Check if this is an expandable item (1, 2, 3, 4, 5, 6, 7, 8, 9)
        // 1: Introduction, 2: Scientific Name, 3: Duration, 4: Varieties, 5: Season, 6: Materials, 7: Weed Control, 8: Irrigation, 9: Harvesting
        if (position == 1 || position == 2 || position == 3 || position == 4 || position == 5 || position == 6 || position == 7 || position == 8 || position == 9) {
            // Set click listener on the text item itself ONLY for expandable items
            holder.txtItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toggleExpansion(position);
                }
            });
            
            if (expandedPosition == position) {
                // Handle different types of expandable content
                if (position == 1) {
                    // Introduction - text only
                    holder.expandableSection.setVisibility(View.GONE);
                    holder.expandableTextSection.setVisibility(View.VISIBLE);
                    holder.expandableTextTitle.setText("Introduction:");
                    
                    if (introductionText.isEmpty()) {
                        loadIntroductionFromDatabase();
                    }
                    holder.expandableTextContent.setText(introductionText);
                    
                } else if (position == 2) {
                    // Scientific Name - text only
                    holder.expandableSection.setVisibility(View.GONE);
                    holder.expandableTextSection.setVisibility(View.VISIBLE);
                    holder.expandableTextTitle.setText("Scientific Name:");
                    
                    if (scientificNameText.isEmpty()) {
                        loadScientificNameFromDatabase();
                    }
                    holder.expandableTextContent.setText(scientificNameText);
                    
                } else if (position == 3) {
                    // Crop Duration - text only
                    holder.expandableSection.setVisibility(View.GONE);
                    holder.expandableTextSection.setVisibility(View.VISIBLE);
                    holder.expandableTextTitle.setText("Duration:");
                    
                    if (durationText.isEmpty()) {
                        loadDurationFromDatabase();
                    }
                    holder.expandableTextContent.setText(durationText);
                    
                } else if (position == 5) {
                    // Planting Season - text only
                    holder.expandableSection.setVisibility(View.GONE);
                    holder.expandableTextSection.setVisibility(View.VISIBLE);
                    holder.expandableTextTitle.setText("Season:");
                    
                    if (seasonText.isEmpty()) {
                        loadSeasonFromDatabase();
                    }
                    holder.expandableTextContent.setText(seasonText);
                    
                } else if (position == 8) {
                    // Irrigation - text only
                    holder.expandableSection.setVisibility(View.GONE);
                    holder.expandableTextSection.setVisibility(View.VISIBLE);
                    holder.expandableTextTitle.setText("Irrigation:");
                    
                    if (irrigationText.isEmpty()) {
                        loadIrrigationFromDatabase();
                    }
                    holder.expandableTextContent.setText(irrigationText);
                    
                } else if (position == 9) {
                    // Harvesting - text only
                    holder.expandableSection.setVisibility(View.GONE);
                    holder.expandableTextSection.setVisibility(View.VISIBLE);
                    holder.expandableTextTitle.setText("Harvesting:");
                    
                    if (harvestingText.isEmpty()) {
                        loadHarvestingFromDatabase();
                    }
                    holder.expandableTextContent.setText(harvestingText);
                    
                } else {
                    // Varieties, Materials, Weed Control - lists
                    holder.expandableTextSection.setVisibility(View.GONE);
                    holder.expandableSection.setVisibility(View.VISIBLE);
                    
                    if (position == 4) {
                        // Crop Varieties
                        holder.expandableTitle.setText("Varieties:");
                        if (varietylist.isEmpty()) {
                            loadVarietiesFromDatabase();
                        }
                        varietyAdapter = new Adapter_Variety(context, R.layout.list_variety, varietylist);
                        holder.expandableList.setAdapter(varietyAdapter);
                        
                    } else if (position == 6) {
                        // Planting Materials
                        holder.expandableTitle.setText("Materials:");
                        if (materialsList.isEmpty()) {
                            loadMaterialsFromAPI();
                        }
                        materialsAdapter = new Adapter_CropMaterials(context, R.layout.list_cropmaterial, materialsList);
                        holder.expandableList.setAdapter(materialsAdapter);
                        
                    } else if (position == 7) {
                        // Weed Control
                        holder.expandableTitle.setText("Weed Control:");
                        if (weedsList.isEmpty()) {
                            loadWeedsFromDatabase();
                        }
                        weedsAdapter = new Adapter_WeedControl(context, R.layout.list_weed_control, weedsList);
                        holder.expandableList.setAdapter(weedsAdapter);
                    }
                }
            } else {
                holder.expandableSection.setVisibility(View.GONE);
                holder.expandableTextSection.setVisibility(View.GONE);
            }
        } else {
            // For navigation items, don't set any click listener
            // Let the ListView's onItemClickListener handle navigation
            holder.expandableSection.setVisibility(View.GONE);
            holder.expandableTextSection.setVisibility(View.GONE);
        }
        
        return convertView;
    }
    
    
    public void toggleExpansion(int position) {
        // Check if this is an expandable item (1, 2, 3, 4, 5, 6, 7, 8, 9)
        if (position == 1 || position == 2 || position == 3 || position == 4 || position == 5 || position == 6 || position == 7 || position == 8 || position == 9) {
            if (expandedPosition == position) {
                expandedPosition = -1; // Collapse
            } else {
                expandedPosition = position; // Expand
            }
            notifyDataSetChanged();
        }
    }
    
    public boolean isExpandableItem(int position) {
        return position == 1 || position == 2 || position == 3 || position == 4 || position == 5 || position == 6 || position == 7 || position == 8 || position == 9;
    }
    
    private void loadVarietiesFromDatabase() {
        varietylist.clear();
        // First try Firestore, fall back to SQLite if needed
        try {
            Class_DatabaseHelper db = new Class_DatabaseHelper(context);
            String varieties = db.getCropFieldById(cropId, "varieties");
            if (varieties != null && !varieties.trim().isEmpty()) {
                for (String v : varieties.split("\\r?\\n|,")) {
                    String name = v.trim();
                    if (!name.isEmpty()) {
                        varietylist.add(new Class_Variety(0, name));
                    }
                }
            }
        } catch (Exception e) {
            android.util.Log.e("Adapter_CropOptions", "Error loading varieties: " + e.getMessage());
        }
        if (varietylist.isEmpty()) {
            varietylist.add(new Class_Variety(0, "No varieties available"));
        }
    }
    
    private void loadSeasonFromDatabase() {
        try {
            Class_DatabaseHelper db = new Class_DatabaseHelper(context);
            seasonText = db.getCropFieldById(cropId, "season");
        } catch (Exception e) {
            android.util.Log.e("Adapter_CropOptions", "Error loading season: " + e.getMessage());
            seasonText = "";
        }
        if (seasonText == null || seasonText.trim().isEmpty()) {
            seasonText = "No season data available";
        }
    }
    
    private void loadMaterialsFromAPI() {
        materialsList.clear();
        try {
            Class_DatabaseHelper db = new Class_DatabaseHelper(context);
            String materials = db.getCropFieldById(cropId, "materials");
            if (materials != null && !materials.trim().isEmpty()) {
                for (String v : materials.split("\\r?\\n|,")) {
                    String name = v.trim();
                    if (!name.isEmpty()) {
                        materialsList.add(new Class_CropMaterials(0, name));
                    }
                }
            }
        } catch (Exception e) {
            android.util.Log.e("Adapter_CropOptions", "Error loading materials: " + e.getMessage());
        }
        if (materialsList.isEmpty()) {
            materialsList.add(new Class_CropMaterials(0, "No materials available"));
        }
    }
    
    private void loadWeedsFromDatabase() {
        weedsList.clear();
        try {
            Class_DatabaseHelper db = new Class_DatabaseHelper(context);
            String weeds = db.getCropFieldById(cropId, "weed_control");
            if (weeds != null && !weeds.trim().isEmpty()) {
                for (String v : weeds.split("\\r?\\n|,")) {
                    String name = v.trim();
                    if (!name.isEmpty()) {
                        weedsList.add(new Class_Weeds(0, name));
                    }
                }
            }
        } catch (Exception e) {
            android.util.Log.e("Adapter_CropOptions", "Error loading weeds: " + e.getMessage());
        }
        if (weedsList.isEmpty()) {
            weedsList.add(new Class_Weeds(0, "No weed control data available"));
        }
    }
    
    private void loadIntroductionFromDatabase() {
        try {
            Class_DatabaseHelper db = new Class_DatabaseHelper(context);
            introductionText = db.getCropFieldById(cropId, "description");
        } catch (Exception e) {
            android.util.Log.e("Adapter_CropOptions", "Error loading introduction: " + e.getMessage());
            introductionText = "";
        }
        if (introductionText == null || introductionText.trim().isEmpty()) {
            introductionText = "No introduction available";
        }
    }
    
    private void loadScientificNameFromDatabase() {
        try {
            Class_DatabaseHelper db = new Class_DatabaseHelper(context);
            scientificNameText = db.getCropFieldById(cropId, "science_name");
        } catch (Exception e) {
            android.util.Log.e("Adapter_CropOptions", "Error loading scientific name: " + e.getMessage());
            scientificNameText = "";
        }
        if (scientificNameText == null || scientificNameText.trim().isEmpty()) {
            scientificNameText = "No scientific name available";
        }
    }
    
    private void loadIrrigationFromDatabase() {
        try {
            Class_DatabaseHelper db = new Class_DatabaseHelper(context);
            irrigationText = db.getCropFieldById(cropId, "irrigation");
        } catch (Exception e) {
            android.util.Log.e("Adapter_CropOptions", "Error loading irrigation: " + e.getMessage());
            irrigationText = "";
        }
        if (irrigationText == null || irrigationText.trim().isEmpty()) {
            irrigationText = "No irrigation data available";
        }
    }
    
    private void loadDurationFromDatabase() {
        try {
            Class_DatabaseHelper db = new Class_DatabaseHelper(context);
            durationText = db.getCropFieldById(cropId, "duration");
        } catch (Exception e) {
            android.util.Log.e("Adapter_CropOptions", "Error loading duration: " + e.getMessage());
            durationText = "";
        }
        if (durationText == null || durationText.trim().isEmpty()) {
            durationText = "No duration data available";
        }
    }
    
    private void loadHarvestingFromDatabase() {
        try {
            Class_DatabaseHelper db = new Class_DatabaseHelper(context);
            harvestingText = db.getCropFieldById(cropId, "harvesting");
        } catch (Exception e) {
            android.util.Log.e("Adapter_CropOptions", "Error loading harvesting: " + e.getMessage());
            harvestingText = "";
        }
        if (harvestingText == null || harvestingText.trim().isEmpty()) {
            harvestingText = "No harvesting data available";
        }
    }
    
    static class ViewHolder {
        TextView txtItem;
        LinearLayout expandableSection;
        LinearLayout expandableTextSection;
        TextView expandableTitle;
        TextView expandableTextTitle;
        TextView expandableTextContent;
        ListView expandableList;
    }
}
