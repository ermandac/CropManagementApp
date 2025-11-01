# Bulk Data Upload Guide for Crop Management App

## Overview

This guide explains how to use the bulk upload feature to populate your Firestore database with all data categories when the app starts.

## What Gets Uploaded?

The bulk upload script uploads all of the following data categories to Firestore:

1. **Categories** - Crop categories (Fruits, Vegetables, Medicinal, etc.)
2. **Crops** - Complete crop information and procedures
3. **Procedures** - Growing procedures for each crop
4. **Fertilizers** - Fertilizer recommendations and details
5. **Pests** - Pest information and control methods
6. **Organic Farming** - Organic farming practices and techniques
7. **Suggested Crops** - Crop suggestions based on various conditions

## Data Sources

The upload reads from the following JSON files in `app/src/main/assets/`:

- `firestore_migration.json` - Categories, crops, and procedures
- `fertilizers_data.json` - Fertilizer data
- `pest_data.json` - Pest information
- `organic_farming_data.json` - Organic farming practices
- `suggested_crops_data.json` - Suggested crop recommendations

## How to Use

### Initial Setup (Bulk Upload All Data)

When you first run the app or need to populate/repopulate the database:

1. **The bulk upload is CURRENTLY ACTIVE** in `Activity_Home.java`
2. Simply **run the app** - the upload will start automatically
3. You'll see a Toast notification when the upload completes
4. Check LogCat for detailed upload progress (filter by tag "FirestoreInitializer")
5. **After successful upload**, comment out the bulk upload code to prevent re-uploading

### How to Disable After First Upload

After the initial upload succeeds, you should disable the automatic upload to avoid duplicate data:

1. Open `app/src/main/java/com/cma/thesis/cropmanagementapp/Activity_Home.java`
2. Find the `initializeFirestore()` method
3. Comment out the bulk upload section:

```java
private void initializeFirestore() {
    // BULK UPLOAD ALL DATA TO FIRESTORE
    // Uncomment the code below to upload all data categories to Firestore
    
    // FirestoreInitializer initializer = new FirestoreInitializer(this);
    // initializer.uploadAllData(new FirestoreInitializer.UploadCallback() {
    //     @Override
    //     public void onSuccess(String message) {
    //         Log.d("TAG", "Bulk Upload Success:\n" + message);
    //         Toast.makeText(Activity_Home.this, "All data uploaded successfully!", Toast.LENGTH_LONG).show();
    //     }
    //
    //     @Override
    //     public void onError(String error) {
    //         Log.e("TAG", "Bulk Upload Error:\n" + error);
    //         Toast.makeText(Activity_Home.this, "Upload error - check logs", Toast.LENGTH_LONG).show();
    //     }
    // });
}
```

### Selective Upload (Individual Categories)

If you only need to upload specific categories, use the individual upload methods instead:

```java
FirestoreInitializer initializer = new FirestoreInitializer(this);

// Upload only fertilizers
initializer.uploadFertilizerData(new FirestoreInitializer.UploadCallback() {
    @Override
    public void onSuccess(String message) {
        Log.d("TAG", "Fertilizer Upload Success: " + message);
        Toast.makeText(Activity_Home.this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onError(String error) {
        Log.e("TAG", "Fertilizer Upload Error: " + error);
        Toast.makeText(Activity_Home.this, "Error: " + error, Toast.LENGTH_LONG).show();
    }
});
```

Available individual upload methods:
- `uploadMigratedData()` - Categories, crops, procedures
- `uploadFertilizerData()` - Fertilizers only
- `uploadPestData()` - Pests only
- `uploadOrganicFarmingData()` - Organic farming only
- `uploadSuggestedCropsData()` - Suggested crops only

## Upload Process Flow

The bulk upload follows this sequence:

1. **Step 1/5**: Upload categories, crops, and procedures
2. **Step 2/5**: Upload fertilizers
3. **Step 3/5**: Upload pests
4. **Step 4/5**: Upload organic farming data
5. **Step 5/5**: Upload suggested crops

Each step must complete successfully before the next step begins.

## Monitoring Upload Progress

### Via Toast Notifications
- Success: "All data uploaded successfully!"
- Error: "Upload error - check logs"

### Via LogCat
Filter by tag: `FirestoreInitializer`

You'll see detailed logs like:
```
D/FirestoreInitializer: === Starting bulk upload of all data to Firestore ===
D/FirestoreInitializer: Step 1/5 Complete: ✓ Categories uploaded: 6
D/FirestoreInitializer: Step 2/5 Complete: ✓ Fertilizers uploaded: 25
D/FirestoreInitializer: Step 3/5 Complete: ✓ Pests uploaded: 15
D/FirestoreInitializer: Step 4/5 Complete: ✓ Organic farming items uploaded: 20
D/FirestoreInitializer: Step 5/5 Complete: ✓ Suggested crops uploaded: 30
```

## Error Handling

If an error occurs during upload:

1. Check the LogCat error message
2. Verify your Firebase configuration (`google-services.json`)
3. Ensure you have internet connectivity
4. Check Firestore security rules
5. Verify JSON file integrity in the assets folder

Common issues:
- **Missing JSON files**: Ensure all required JSON files exist in `app/src/main/assets/`
- **Permission denied**: Check Firestore security rules
- **Network errors**: Verify internet connection
- **Invalid JSON**: Validate JSON file structure

## Adding New Data Categories

To add a new data category to the bulk upload:

1. Create your JSON file in `app/src/main/assets/`
2. Add a new upload method in `FirestoreInitializer.java`:

```java
public void uploadNewCategoryData(final UploadCallback callback) {
    try {
        String jsonString = readJsonFromAssets("new_category_data.json");
        JSONArray dataArray = new JSONArray(jsonString);
        
        WriteBatch batch = db.batch();
        int count = 0;
        
        for (int i = 0; i < dataArray.length(); i++) {
            JSONObject item = dataArray.getJSONObject(i);
            String docId = item.getString("id");
            Map<String, Object> itemMap = jsonToMap(item);
            batch.set(db.collection("new_category").document(docId), itemMap);
            count++;
        }
        
        int finalCount = count;
        batch.commit().addOnSuccessListener(aVoid -> {
            callback.onSuccess("✓ Successfully uploaded " + finalCount + " items!");
        }).addOnFailureListener(e -> {
            callback.onError("Error: " + e.getMessage());
        });
        
    } catch (Exception e) {
        callback.onError("Error: " + e.getMessage());
    }
}
```

3. Add it to the `uploadAllData()` method in the appropriate sequence

## Best Practices

1. **Test with small datasets first** - Verify your JSON structure before bulk uploading
2. **Backup existing data** - Export Firestore data before re-uploading
3. **Monitor the first upload** - Watch LogCat to ensure everything uploads correctly
4. **Comment out after success** - Prevent duplicate uploads on every app start
5. **Version control your JSON files** - Keep track of data changes
6. **Use batch writes** - The script uses Firestore batch writes for efficiency (500 docs max per batch)

## Troubleshooting

### Upload hangs or doesn't complete
- Check internet connectivity
- Verify Firebase project is active
- Look for errors in LogCat

### Data not appearing in Firestore
- Check Firestore console for the collections
- Verify document IDs in the data
- Check for duplicate ID conflicts

### App crashes during upload
- Check JSON file syntax
- Verify all required fields are present
- Look for memory issues with large datasets

## Summary

The bulk upload feature provides a convenient way to populate your Firestore database with all necessary data when the app first runs. After the initial upload, remember to comment out the upload code to avoid re-uploading data on every app start.

For questions or issues, check the LogCat output for detailed error messages.
