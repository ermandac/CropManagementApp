#!/usr/bin/env python3
"""
Fertilizer Data Upload Script
Populates Firestore with common Philippine agricultural fertilizers
"""

import firebase_admin
from firebase_admin import credentials, firestore
from datetime import datetime

# Initialize Firebase
cred = credentials.Certificate('app/google-services.json')
firebase_admin.initialize_app(cred)
db = firestore.client()

# Comprehensive fertilizer data for Philippines
fertilizers = [
    {
        "id": 1,
        "name": "Urea",
        "npk": "46-0-0",
        "category": "Nitrogen",
        "description": "High nitrogen content fertilizer for rapid vegetative growth. Most widely used nitrogen fertilizer in the Philippines.",
        "benefits": "Promotes leafy growth, enhances green color, increases crop yield",
        "bestFor": ["Rice", "Corn", "Vegetables", "Sugarcane"],
        "application": "Apply 2-3 bags (40-60 kg) per hectare during planting and topdressing stages",
        "timing": "Split application: 50% at planting, 50% at tillering/knee-high stage",
        "precautions": "Avoid direct contact with seeds. Apply during cooler hours to reduce volatilization.",
        "priceRange": "₱800-1,000 per 50kg bag",
        "isOrganic": False,
        "popularity": "Very High"
    },
    {
        "id": 2,
        "name": "Complete Fertilizer 14-14-14",
        "npk": "14-14-14",
        "category": "Complete",
        "description": "Balanced NPK fertilizer providing equal amounts of nitrogen, phosphorus, and potassium for overall plant health.",
        "benefits": "Complete nutrition, promotes root development, flowering, and fruiting",
        "bestFor": ["Vegetables", "Fruits", "Corn", "Rice"],
        "application": "Apply 2-4 bags (40-80 kg) per hectare depending on crop requirements",
        "timing": "Apply at planting or early growth stage for best results",
        "precautions": "Store in dry place. Apply evenly to avoid fertilizer burn.",
        "priceRange": "₱1,200-1,500 per 50kg bag",
        "isOrganic": False,
        "popularity": "Very High"
    },
    {
        "id": 3,
        "name": "Ammonium Sulfate",
        "npk": "21-0-0-24S",
        "category": "Nitrogen",
        "description": "Nitrogen fertilizer with sulfur, ideal for alkaline soils and sulfur-deficient crops.",
        "benefits": "Provides both nitrogen and sulfur, acidifies soil slightly",
        "bestFor": ["Rice", "Onions", "Garlic", "Vegetables"],
        "application": "Apply 3-4 bags per hectare for rice, adjust based on soil test",
        "timing": "Apply during active growth periods",
        "precautions": "Can acidify soil over time. Monitor soil pH regularly.",
        "priceRange": "₱700-900 per 50kg bag",
        "isOrganic": False,
        "popularity": "High"
    },
    {
        "id": 4,
        "name": "Diammonium Phosphate (DAP)",
        "npk": "18-46-0",
        "category": "Phosphorus",
        "description": "High phosphorus fertilizer essential for root development and early plant growth.",
        "benefits": "Promotes strong root systems, improves flowering and fruit set",
        "bestFor": ["Rice", "Corn", "Vegetables", "Legumes"],
        "application": "Apply 1-2 bags (20-40 kg) per hectare at planting",
        "timing": "Best applied at planting time, incorporate into soil",
        "precautions": "Place below or beside seeds to avoid germination inhibition",
        "priceRange": "₱1,500-1,800 per 50kg bag",
        "isOrganic": False,
        "popularity": "Very High"
    },
    {
        "id": 5,
        "name": "Muriate of Potash (KCl)",
        "npk": "0-0-60",
        "category": "Potassium",
        "description": "High potassium fertilizer for fruit quality, disease resistance, and overall plant vigor.",
        "benefits": "Improves fruit quality, enhances disease resistance, regulates water use",
        "bestFor": ["Fruits", "Vegetables", "Rice", "Sugarcane"],
        "application": "Apply 1-2 bags (20-40 kg) per hectare depending on crop",
        "timing": "Apply during flowering and fruiting stages",
        "precautions": "Avoid using on salt-sensitive crops. Can increase soil salinity.",
        "priceRange": "₱1,000-1,300 per 50kg bag",
        "isOrganic": False,
        "popularity": "High"
    },
    {
        "id": 6,
        "name": "Complete Fertilizer 16-16-16",
        "npk": "16-16-16",
        "category": "Complete",
        "description": "Higher concentration balanced fertilizer for intensive farming systems.",
        "benefits": "Complete nutrition in higher concentration, cost-effective for large farms",
        "bestFor": ["Corn", "Rice", "Vegetables", "Fruits"],
        "application": "Apply 2-3 bags per hectare at planting and during active growth",
        "timing": "Split application recommended for best results",
        "precautions": "Avoid over-application. Follow crop-specific recommendations.",
        "priceRange": "₱1,300-1,600 per 50kg bag",
        "isOrganic": False,
        "popularity": "High"
    },
    {
        "id": 7,
        "name": "Organic Compost",
        "npk": "Variable (1-2-1 avg)",
        "category": "Organic",
        "description": "Decomposed organic matter that improves soil structure and provides slow-release nutrients.",
        "benefits": "Improves soil structure, increases water retention, adds beneficial microorganisms",
        "bestFor": ["All crops", "Vegetables", "Fruits", "Ornamentals"],
        "application": "Apply 2-5 tons per hectare, incorporate into soil before planting",
        "timing": "Best applied 2-4 weeks before planting",
        "precautions": "Ensure compost is fully decomposed before application",
        "priceRange": "₱100-300 per 50kg bag",
        "isOrganic": True,
        "popularity": "High"
    },
    {
        "id": 8,
        "name": "Vermicast (Worm Castings)",
        "npk": "1-1-1 (approx)",
        "category": "Organic",
        "description": "Premium organic fertilizer produced by earthworms, rich in beneficial microbes.",
        "benefits": "Excellent soil conditioner, high in plant growth hormones, disease-suppressive",
        "bestFor": ["Vegetables", "Fruits", "Ornamentals", "Seedlings"],
        "application": "Apply 1-3 tons per hectare or 1-2 handfuls per plant",
        "timing": "Can be applied anytime during growing season",
        "precautions": "Store in cool, moist place. Avoid direct sunlight.",
        "priceRange": "₱150-400 per kg",
        "isOrganic": True,
        "popularity": "Medium"
    },
    {
        "id": 9,
        "name": "Chicken Manure",
        "npk": "3-2-2 (approx)",
        "category": "Organic",
        "description": "Nitrogen-rich organic fertilizer from poultry waste, excellent soil amendment.",
        "benefits": "High nitrogen content, improves soil fertility, readily available",
        "bestFor": ["Vegetables", "Corn", "Rice", "Fruits"],
        "application": "Apply 2-4 tons per hectare, must be well-composted",
        "timing": "Apply 2-3 weeks before planting to allow decomposition",
        "precautions": "Must be composted first. Fresh manure can burn plants.",
        "priceRange": "₱50-150 per 50kg bag (dried)",
        "isOrganic": True,
        "popularity": "High"
    },
    {
        "id": 10,
        "name": "NPK 12-24-12",
        "npk": "12-24-12",
        "category": "Complete",
        "description": "Starter fertilizer with high phosphorus for transplants and seedlings.",
        "benefits": "Promotes strong root establishment, ideal for transplanted crops",
        "bestFor": ["Transplanted vegetables", "Rice", "Corn"],
        "application": "Apply 1-2 bags per hectare at transplanting",
        "timing": "Apply at planting or transplanting time",
        "precautions": "Band or place near roots for maximum efficiency",
        "priceRange": "₱1,100-1,400 per 50kg bag",
        "isOrganic": False,
        "popularity": "Medium"
    },
    {
        "id": 11,
        "name": "Ammonium Nitrate",
        "npk": "33-0-0",
        "category": "Nitrogen",
        "description": "Fast-acting nitrogen fertilizer with both nitrate and ammonium forms.",
        "benefits": "Rapid nitrogen availability, suitable for quick greening",
        "bestFor": ["Vegetables", "Pastures", "Forage crops"],
        "application": "Apply 2-3 bags per hectare during active growth",
        "timing": "Split application during vegetative stage",
        "precautions": "Highly hygroscopic. Store properly. Handle with care.",
        "priceRange": "₱1,000-1,200 per 50kg bag",
        "isOrganic": False,
        "popularity": "Medium"
    },
    {
        "id": 12,
        "name": "Solophos (Single Superphosphate)",
        "npk": "0-20-0",
        "category": "Phosphorus",
        "description": "Phosphorus fertilizer with calcium and sulfur, good for acidic soils.",
        "benefits": "Provides phosphorus, calcium, and sulfur in one application",
        "bestFor": ["Rice", "Corn", "Legumes", "Root crops"],
        "application": "Apply 2-3 bags per hectare at planting",
        "timing": "Incorporate into soil before or at planting",
        "precautions": "Less concentrated than DAP, may need higher application rates",
        "priceRange": "₱600-800 per 50kg bag",
        "isOrganic": False,
        "popularity": "Medium"
    },
    {
        "id": 13,
        "name": "Calcium Nitrate",
        "npk": "15.5-0-0 + 19% Ca",
        "category": "Nitrogen",
        "description": "Nitrogen fertilizer with calcium, prevents blossom end rot and tipburn.",
        "benefits": "Provides readily available nitrogen and calcium",
        "bestFor": ["Tomatoes", "Peppers", "Lettuce", "Cabbage"],
        "application": "Apply 1-2 bags per hectare or as foliar spray",
        "timing": "During active growth, especially fruiting stage",
        "precautions": "Very soluble. Can be used in fertigation systems.",
        "priceRange": "₱1,500-2,000 per 25kg bag",
        "isOrganic": False,
        "popularity": "Medium"
    },
    {
        "id": 14,
        "name": "Carbonized Rice Hull",
        "npk": "0-0-0 (soil conditioner)",
        "category": "Organic",
        "description": "Carbonized rice hulls used as soil amendment to improve aeration and drainage.",
        "benefits": "Improves soil structure, increases porosity, retains moisture",
        "bestFor": ["All crops", "Container growing", "Nurseries"],
        "application": "Mix 10-30% by volume into soil or growing media",
        "timing": "Apply during land preparation or potting",
        "precautions": "Not a nutrient source, use with other fertilizers",
        "priceRange": "₱50-100 per sack",
        "isOrganic": True,
        "popularity": "High"
    },
    {
        "id": 15,
        "name": "Foliar Fertilizer 20-20-20",
        "npk": "20-20-20",
        "category": "Foliar",
        "description": "Water-soluble complete fertilizer for foliar application and fertigation.",
        "benefits": "Quick nutrient absorption through leaves, corrects deficiencies fast",
        "bestFor": ["All crops", "Vegetables", "Fruits", "Ornamentals"],
        "application": "Mix 1-2 tablespoons per liter of water, spray on leaves",
        "timing": "Apply early morning or late afternoon, every 7-14 days",
        "precautions": "Avoid spraying during hot sunny hours. Do not exceed recommended rates.",
        "priceRange": "₱200-400 per kg",
        "isOrganic": False,
        "popularity": "High"
    },
    {
        "id": 16,
        "name": "Kieserite (Magnesium Sulfate)",
        "npk": "0-0-0 + 16% Mg + 22% S",
        "category": "Secondary",
        "description": "Magnesium and sulfur fertilizer for correcting deficiencies.",
        "benefits": "Corrects magnesium deficiency, provides sulfur",
        "bestFor": ["Coconut", "Fruits", "Vegetables"],
        "application": "Apply 50-100 kg per hectare based on soil test",
        "timing": "Apply when deficiency symptoms appear or as preventive",
        "precautions": "Conduct soil test before application",
        "priceRange": "₱80-120 per kg",
        "isOrganic": False,
        "popularity": "Low"
    },
    {
        "id": 17,
        "name": "Zinc Sulfate",
        "npk": "0-0-0 + 23% Zn",
        "category": "Micronutrient",
        "description": "Zinc fertilizer for correcting zinc deficiency, common in rice.",
        "benefits": "Corrects zinc deficiency, improves rice tillering and yield",
        "bestFor": ["Rice", "Corn", "Citrus"],
        "application": "Apply 5-10 kg per hectare or as foliar spray",
        "timing": "Apply before flooding for rice, or as foliar during growth",
        "precautions": "Small amounts needed. Excess can be toxic.",
        "priceRange": "₱150-250 per kg",
        "isOrganic": False,
        "popularity": "Medium"
    },
    {
        "id": 18,
        "name": "Fermented Plant Juice (FPJ)",
        "npk": "Variable",
        "category": "Organic",
        "description": "Fermented extract of young plants, rich in growth hormones and nutrients.",
        "benefits": "Promotes vigorous growth, natural growth hormone source",
        "bestFor": ["All crops", "Vegetables", "Fruits"],
        "application": "Dilute 1:500 to 1:1000, apply as foliar spray or soil drench",
        "timing": "Apply during vegetative stage for best results",
        "precautions": "Must be properly fermented. Dilute before use.",
        "priceRange": "DIY or ₱50-150 per liter (concentrated)",
        "isOrganic": True,
        "popularity": "Medium"
    },
    {
        "id": 19,
        "name": "Fish Amino Acid (FAA)",
        "npk": "Variable (high nitrogen)",
        "category": "Organic",
        "description": "Fermented fish waste providing amino acids and nutrients.",
        "benefits": "Rich in amino acids, promotes plant growth and stress tolerance",
        "bestFor": ["Vegetables", "Fruits", "Rice"],
        "application": "Dilute 1:500 to 1:1000, apply as foliar or soil drench",
        "timing": "Apply during active growth periods",
        "precautions": "Strong odor when concentrated. Dilute properly before use.",
        "priceRange": "DIY or ₱100-200 per liter (concentrated)",
        "isOrganic": True,
        "popularity": "Medium"
    },
    {
        "id": 20,
        "name": "Slow-Release Fertilizer",
        "npk": "Various formulations",
        "category": "Specialty",
        "description": "Controlled-release fertilizer with coated granules for gradual nutrient supply.",
        "benefits": "Reduces application frequency, minimizes nutrient loss, steady feeding",
        "bestFor": ["Container plants", "Nurseries", "High-value crops"],
        "application": "Follow package directions, typically one application per season",
        "timing": "Apply at planting or start of growing season",
        "precautions": "More expensive. Best for specialty applications.",
        "priceRange": "₱2,000-5,000 per 50kg bag",
        "isOrganic": False,
        "popularity": "Low"
    }
]

def upload_fertilizers():
    """Upload fertilizer data to Firestore"""
    print("Starting fertilizer data upload...")
    
    collection_ref = db.collection('fertilizers')
    
    # Upload each fertilizer
    for fertilizer in fertilizers:
        fertilizer['createdAt'] = datetime.now()
        fertilizer['updatedAt'] = datetime.now()
        
        # Use ID as document ID for consistency
        doc_id = str(fertilizer['id'])
        collection_ref.document(doc_id).set(fertilizer)
        print(f"✓ Uploaded: {fertilizer['name']}")
    
    print(f"\n✅ Successfully uploaded {len(fertilizers)} fertilizers to Firestore!")
    print("\nCategories included:")
    categories = set(f['category'] for f in fertilizers)
    for cat in sorted(categories):
        count = len([f for f in fertilizers if f['category'] == cat])
        print(f"  - {cat}: {count} items")
    
    print(f"\n📊 Organic fertilizers: {len([f for f in fertilizers if f['isOrganic']])}")
    print(f"📊 Chemical fertilizers: {len([f for f in fertilizers if not f['isOrganic']])}")

if __name__ == "__main__":
    try:
        upload_fertilizers()
    except Exception as e:
        print(f"❌ Error: {e}")
        import traceback
        traceback.print_exc()
