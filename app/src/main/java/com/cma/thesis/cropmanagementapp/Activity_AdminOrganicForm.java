package com.cma.thesis.cropmanagementapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

/**
 * Admin form activity for creating/editing organic farming items
 */
public class Activity_AdminOrganicForm extends AppCompatActivity {
    
    private EditText etName, etCategory, etDescription, etBenefits, etApplicationMethod, etDuration;
    private Spinner spinnerType;
    private LinearLayout materialsContainer;
    private Button btnAddMaterial, btnSave, btnCancel;
    
    private FirestoreOrganicFarmingHelper firestoreHelper;
    private String organicId;
    private String mode; // CREATE or EDIT
    private List<EditText> materialFields;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_organic_form);
        
        firestoreHelper = new FirestoreOrganicFarmingHelper();
        materialFields = new ArrayList<>();
        
        // Get intent data
        organicId = getIntent().getStringExtra("ORGANIC_ID");
        mode = getIntent().getStringExtra("MODE");
        if (mode == null) mode = "CREATE";
        
        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(mode.equals("CREATE") ? "Add Organic Item" : "Edit Organic Item");
        }
        toolbar.setNavigationOnClickListener(v -> finish());
        
        // Initialize views
        initViews();
        
        // Setup type spinner
        setupTypeSpinner();
        
        // Load data if editing
        if (mode.equals("EDIT") && organicId != null) {
            loadOrganicData();
        }
    }
    
    private void initViews() {
        etName = findViewById(R.id.et_name);
        spinnerType = findViewById(R.id.spinner_type);
        etCategory = findViewById(R.id.et_category);
        etDescription = findViewById(R.id.et_description);
        materialsContainer = findViewById(R.id.materials_container);
        btnAddMaterial = findViewById(R.id.btn_add_material);
        etBenefits = findViewById(R.id.et_benefits);
        etApplicationMethod = findViewById(R.id.et_application_method);
        etDuration = findViewById(R.id.et_duration);
        btnSave = findViewById(R.id.btn_save);
        btnCancel = findViewById(R.id.btn_cancel);
        
        btnAddMaterial.setOnClickListener(v -> addMaterialField(""));
        btnSave.setOnClickListener(v -> saveOrganic());
        btnCancel.setOnClickListener(v -> finish());
    }
    
    private void setupTypeSpinner() {
        String[] types = {"Farming Method", "Material"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, types);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(adapter);
    }
    
    private void addMaterialField(String materialText) {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setLayoutParams(new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        
        EditText etMaterial = new EditText(this);
        etMaterial.setLayoutParams(new LinearLayout.LayoutParams(
            0,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            1f
        ));
        etMaterial.setHint("Material...");
        etMaterial.setText(materialText);
        
        Button btnRemove = new Button(this);
        btnRemove.setText("Remove");
        btnRemove.setTextSize(12);
        btnRemove.setOnClickListener(v -> {
            materialsContainer.removeView(row);
            materialFields.remove(etMaterial);
        });
        
        row.addView(etMaterial);
        row.addView(btnRemove);
        
        materialsContainer.addView(row);
        materialFields.add(etMaterial);
    }
    
    private void loadOrganicData() {
        firestoreHelper.getOrganicFarmingById(organicId, new FirestoreOrganicFarmingHelper.SingleOrganicCallback() {
            @Override
            public void onSuccess(Class_OrganicFarming item) {
                etName.setText(item.getName());
                etCategory.setText(item.getCategory());
                etDescription.setText(item.getDescription());
                etBenefits.setText(item.getBenefits());
                etApplicationMethod.setText(item.getApplicationMethod());
                etDuration.setText(item.getDuration());
                
                // Set type spinner
                if (item.getType() != null) {
                    if (item.getType().equals("Farming Method")) {
                        spinnerType.setSelection(0);
                    } else {
                        spinnerType.setSelection(1);
                    }
                }
                
                // Load materials
                if (item.getMaterials() != null) {
                    for (String material : item.getMaterials()) {
                        addMaterialField(material);
                    }
                }
            }
            
            @Override
            public void onError(String error) {
                Toast.makeText(Activity_AdminOrganicForm.this, "Error loading data: " + error, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
    
    private void saveOrganic() {
        // Validate
        String name = etName.getText().toString().trim();
        String type = spinnerType.getSelectedItem().toString();
        String category = etCategory.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        
        if (name.isEmpty()) {
            Toast.makeText(this, "Please enter name", Toast.LENGTH_SHORT).show();
            return;
        }
        if (category.isEmpty()) {
            Toast.makeText(this, "Please enter category", Toast.LENGTH_SHORT).show();
            return;
        }
        if (description.isEmpty()) {
            Toast.makeText(this, "Please enter description", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Collect materials
        List<String> materials = new ArrayList<>();
        for (EditText et : materialFields) {
            String mat = et.getText().toString().trim();
            if (!mat.isEmpty()) {
                materials.add(mat);
            }
        }
        
        // Create object
        Class_OrganicFarming item = new Class_OrganicFarming();
        if (mode.equals("EDIT")) {
            item.setId(organicId);
        }
        item.setName(name);
        item.setType(type);
        item.setCategory(category);
        item.setDescription(description);
        item.setMaterials(materials);
        item.setBenefits(etBenefits.getText().toString().trim());
        item.setApplicationMethod(etApplicationMethod.getText().toString().trim());
        item.setDuration(etDuration.getText().toString().trim());
        
        // Save to Firestore
        if (mode.equals("CREATE")) {
            firestoreHelper.addOrganicFarming(item, new FirestoreOrganicFarmingHelper.SingleItemCallback() {
                @Override
                public void onSuccess(String message) {
                    Toast.makeText(Activity_AdminOrganicForm.this, message, Toast.LENGTH_SHORT).show();
                    finish();
                }
                
                @Override
                public void onError(String error) {
                    Toast.makeText(Activity_AdminOrganicForm.this, error, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            firestoreHelper.updateOrganicFarming(item, new FirestoreOrganicFarmingHelper.SingleItemCallback() {
                @Override
                public void onSuccess(String message) {
                    Toast.makeText(Activity_AdminOrganicForm.this, message, Toast.LENGTH_SHORT).show();
                    finish();
                }
                
                @Override
                public void onError(String error) {
                    Toast.makeText(Activity_AdminOrganicForm.this, error, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
