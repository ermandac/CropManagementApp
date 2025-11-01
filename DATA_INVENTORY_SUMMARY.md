# Crop Management App - Data Inventory Summary

This document provides an overview of all existing data in your Crop Management App across all categories.

---

## 📊 Data Categories Overview

Your app currently has data for the following categories:

### ✅ **Currently Available Data**

1. **Fertilizers** - 20 items (fertilizers_data.json)
2. **Pests** - 10 items (pest_data.json)
3. **Organic Farming** - Multiple items (organic_farming_data.json)
4. **Suggested Crops** - 24 items (suggested_crops_data.json)
5. **Categories, Crops & Procedures** - From firestore_migration.json

---

## 📋 Detailed Data Breakdown

### 1. FERTILIZERS (20 Items)
*Source: `app/src/main/assets/fertilizers_data.json`*

| ID | Name | NPK | Category | Organic |
|----|------|-----|----------|---------|
| 1 | Urea | 46-0-0 | Nitrogen | No |
| 2 | Complete Fertilizer 14-14-14 | 14-14-14 | Complete | No |
| 3 | Ammonium Sulfate | 21-0-0-24S | Nitrogen | No |
| 4 | Diammonium Phosphate (DAP) | 18-46-0 | Phosphorus | No |
| 5 | Muriate of Potash (KCl) | 0-0-60 | Potassium | No |
| 6 | Complete Fertilizer 16-16-16 | 16-16-16 | Complete | No |
| 7 | Organic Compost | Variable | Organic | Yes |
| 8 | Vermicast (Worm Castings) | 1-1-1 | Organic | Yes |
| 9 | Chicken Manure | 3-2-2 | Organic | Yes |
| 10 | NPK 12-24-12 | 12-24-12 | Complete | No |
| 11 | Ammonium Nitrate | 33-0-0 | Nitrogen | No |
| 12 | Solophos | 0-20-0 | Phosphorus | No |
| 13 | Calcium Nitrate | 15.5-0-0 + 19% Ca | Nitrogen | No |
| 14 | Carbonized Rice Hull | Soil conditioner | Organic | Yes |
| 15 | Foliar Fertilizer 20-20-20 | 20-20-20 | Foliar | No |
| 16 | Kieserite (Magnesium Sulfate) | 0-0-0 + 16% Mg | Secondary | No |
| 17 | Zinc Sulfate | 0-0-0 + 23% Zn | Micronutrient | No |
| 18 | Fermented Plant Juice (FPJ) | Variable | Organic | Yes |
| 19 | Fish Amino Acid (FAA) | Variable | Organic | Yes |
| 20 | Slow-Release Fertilizer | Various | Specialty | No |

**Sample Data Structure:**
```json
{
  "id": 1,
  "name": "Urea",
  "npk": "46-0-0",
  "category": "Nitrogen",
  "description": "High nitrogen content fertilizer...",
  "benefits": "Promotes leafy growth...",
  "bestFor": ["Rice", "Corn", "Vegetables"],
  "application": "Apply 2-3 bags per hectare...",
  "timing": "Split application...",
  "precautions": "Avoid direct contact with seeds...",
  "priceRange": "₱800-1,000 per 50kg bag",
  "isOrganic": false,
  "popularity": "Very High"
}
```

---

### 2. PESTS (10 Items)
*Source: `app/src/main/assets/pest_data.json`*

| ID | Pest Name | Scientific Name | Category | Severity |
|----|-----------|-----------------|----------|----------|
| 1 | Aphids | Aphidoidea | Insect | Medium |
| 2 | Fruit Flies | Bactrocera spp. | Insect | High |
| 3 | Whiteflies | Bemisia tabaci | Insect | High |
| 4 | Corn Borer | Ostrinia furnacalis | Insect | Very High |
| 5 | Mealybugs | Pseudococcidae | Insect | Medium |
| 6 | Armyworm | Spodoptera spp. | Insect | High |
| 7 | Root-Knot Nematode | Meloidogyne spp. | Nematode | Very High |
| 8 | Leaf Miner | Liriomyza spp. | Insect | Medium |
| 9 | Thrips | Thysanoptera | Insect | Medium |
| 10 | Diamond Back Moth | Plutella xylostella | Insect | High |

**Sample Data Structure:**
```json
{
  "id": "1",
  "pestName": "Aphids",
  "scientificName": "Aphidoidea",
  "category": "Insect",
  "affectedCrops": ["Tomato", "Lettuce", "Cabbage"],
  "description": "Small, soft-bodied insects...",
  "symptoms": "Curled or yellowing leaves...",
  "controlMethods": ["Spray with soapy water", "Introduce ladybugs"],
  "preventionTips": "Encourage beneficial insects...",
  "severity": "Medium",
  "commonSeason": "Dry season (December to May)"
}
```

---

### 3. ORGANIC FARMING PRACTICES
*Source: `app/src/main/assets/organic_farming_data.json`*

Contains comprehensive information about organic farming methods and practices compatible with Philippine agriculture.

---

### 4. SUGGESTED CROPS (24 Items - Month by Month)
*Source: `app/src/main/assets/suggested_crops_data.json`*

| Month | Crop Name | Category | Expected Harvest |
|-------|-----------|----------|------------------|
| January | Tomato | Vegetables | 90-120 days |
| January | Lettuce | Vegetables | 45-60 days |
| January | Watermelon | Fruits | 80-100 days |
| February | Eggplant | Vegetables | 70-90 days |
| February | Bitter Gourd | Vegetables | 60-75 days |
| March | Mungbean | Pulses | 55-65 days |
| March | Cucumber | Vegetables | 50-70 days |
| April | Corn | Vegetables | 90-110 days |
| April | Papaya | Fruits | 9-12 months |
| May | Rice | Vegetables | 110-130 days |
| May | Squash | Vegetables | 70-90 days |
| May | Banana | Fruits | 12-18 months |
| June | String Beans | Vegetables | 45-60 days |
| June | Kangkong | Vegetables | 25-30 days |
| July | Sweet Potato | Vegetables | 100-120 days |
| July | Okra | Vegetables | 50-65 days |
| August | Pechay | Vegetables | 30-45 days |
| September | Radish | Vegetables | 25-40 days |
| October | Carrot | Vegetables | 70-90 days |
| October | Cabbage | Vegetables | 70-90 days |
| November | Onion | Vegetables | 90-120 days |
| November | Garlic | Spices | 90-120 days |
| December | Bell Pepper | Vegetables | 60-90 days |
| December | Strawberry | Fruits | 60-90 days |

**Sample Data Structure:**
```json
{
  "id": "1",
  "cropName": "Tomato",
  "month": "January",
  "category": "Vegetables",
  "plantingReason": "Cool dry season ideal...",
  "description": "Tomatoes thrive in cool months...",
  "bestVarieties": ["Diamante Max", "Apollo", "Harabas"],
  "expectedHarvest": "90-120 days",
  "tips": "Ensure good drainage...",
  "popularity": "Very High"
}
```

---

### 5. CATEGORIES, CROPS & PROCEDURES
*Source: `app/src/main/assets/firestore_migration.json`*

⚠️ **NOTE:** This file currently shows as `firestore_migration.jsonX` (incorrect extension).
You should rename it to `firestore_migration.json` for the bulk upload to work properly.

This file contains:
- **Categories**: Fruits, Vegetables, Pulses, Spices, Medicinal, Plantation
- **Crops**: Detailed information for each crop type
- **Procedures**: Growing procedures for each crop

**Expected Categories:**
1. **Fruits** (Category ID: 1)
2. **Vegetables** (Category ID: 2)  
3. **Pulses** (Category ID: 3)
4. **Spices** (Category ID: 4)
5. **Plantation** (Category ID: 5)
6. **Medicinal** (Category ID: 6)

---

## 🎯 Data Requirements by Category

Based on your request for **Medicinal, Plantation, Pulses, Spices, and Vegetables**, here's what you need:

### Required Crop Data Structure

Each crop should have:
- **Basic Info**: Name, scientific name, category, description
- **Growing Info**: Season, duration, soil requirements, climate
- **Cultivation**: Planting procedure, varieties, field preparation
- **Care**: Irrigation, fertilization, weed control, growth management
- **Harvest**: Harvesting methods, post-harvest handling
- **Additional**: Disease/pest information, common issues

**Example Crop Entry:**
```json
{
  "cropId": "1",
  "cropName": "Tomato",
  "scientificName": "Solanum lycopersicum",
  "category": "Vegetables",
  "categoryId": "2",
  "description": "Popular vegetable crop...",
  "season": "Cool dry season",
  "duration": "90-120 days",
  "varieties": ["Diamante Max", "Apollo"],
  "soilType": "Well-drained loamy soil",
  "climate": "Cool temperatures 15-25°C",
  "procedures": {
    "preparation": "...",
    "planting": "...",
    "care": "...",
    "harvest": "..."
  }
}
```

---

## 📝 Sample Crop Data for Each Category

### VEGETABLES
- Tomato, Eggplant, Cabbage, Lettuce, Cucumber
- Bell Pepper, Bitter Gourd, Okra, Pechay
- Onion, Garlic, Carrot, Radish

### FRUITS
- Mango, Banana, Papaya, Watermelon
- Strawberry (highlands), Citrus

### PULSES
- Mungbean (Monggo), Peanut (Mani)
- Soybean, Cowpea (Sitaw)

### SPICES
- Garlic (Bawang), Ginger (Luya)
- Turmeric (Luyang Dilaw), Chili Pepper

### MEDICINAL
- Lagundi (Five-leaved Chaste Tree)
- Sambong (Blumea balsamifera)
- Oregano, Peppermint

### PLANTATION
- Coconut, Sugarcane, Coffee
- Cacao, Rubber, Oil Palm

---

## 🔧 How to Add New Data

### For Adding New Crops:
1. Use the Admin Panel in the app
2. Navigate to the appropriate category
3. Click "Add New Crop"
4. Fill in all required fields
5. Upload crop image
6. Save

### For Bulk Upload:
1. Ensure JSON files are in `app/src/main/assets/`
2. Fix the file extension: rename `firestore_migration.jsonX` to `firestore_migration.json`
3. Run the app - bulk upload will execute automatically
4. Check LogCat for upload progress
5. After successful upload, comment out the upload code in `Activity_Home.java`

---

## ✅ Current Status

- ✅ Fertilizers Data: Complete (20 items)
- ✅ Pest Data: Complete (10 items)
- ✅ Organic Farming Data: Complete
- ✅ Suggested Crops: Complete (24 items)
- ⚠️ Categories/Crops/Procedures: File extension needs fixing
- 🔄 Bulk Upload: Ready to use (currently ACTIVE in app)

---

## 📌 Next Steps

1. **Fix the file extension**: Rename `firestore_migration.jsonX` to `firestore_migration.json`
2. **Run the app**: Bulk upload will automatically populate Firestore
3. **Verify upload**: Check Firestore console to confirm data
4. **Disable auto-upload**: Comment out the upload code after successful upload
5. **Add more data**: Use the Admin Panel to add new crops as needed

For detailed instructions on the bulk upload process, see `BULK_UPLOAD_GUIDE.md`.

---

**Last Updated:** October 27, 2025
