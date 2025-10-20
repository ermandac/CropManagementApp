#!/usr/bin/env python3
"""
SQLite to Firestore Migration Script
Migrates crop management data from SQLite to Google Cloud Firestore
"""

import sqlite3
import json
import base64
import sys
from datetime import datetime
from pathlib import Path

# Configuration
SQLITE_DB_PATH = 'app/src/main/assets/cms.sqlite'
OUTPUT_JSON_PATH = 'firestore_migration.json'

def encode_blob_to_base64(blob_data):
    """Convert BLOB data to base64 string"""
    if blob_data is None:
        return None
    try:
        return base64.b64encode(blob_data).decode('utf-8')
    except Exception as e:
        print(f"Error encoding blob: {e}")
        return None

def export_sqlite_to_json():
    """Export SQLite database to JSON format for Firestore"""
    
    if not Path(SQLITE_DB_PATH).exists():
        print(f"Error: SQLite database not found at {SQLITE_DB_PATH}")
        sys.exit(1)
    
    try:
        conn = sqlite3.connect(SQLITE_DB_PATH)
        cursor = conn.cursor()
        
        firestore_data = {
            'categories': {},
            'crops': {},
            'procedures': {},
            'metadata': {
                'exportDate': datetime.now().isoformat(),
                'exportTool': 'migrate_to_firestore.py v1.0'
            }
        }
        
        print("=" * 70)
        print("SQLITE TO FIRESTORE MIGRATION")
        print("=" * 70)
        
        # ===== MIGRATE CATEGORIES =====
        print("\n[1/3] Migrating Categories...")
        cursor.execute("SELECT id, category, image FROM tbl_category")
        categories = cursor.fetchall()
        
        for cat_id, category_name, image_blob in categories:
            image_b64 = encode_blob_to_base64(image_blob)
            firestore_data['categories'][str(cat_id)] = {
                'id': cat_id,
                'category': category_name,
                'image': image_b64,
                'createdAt': {'_type': 'Timestamp', 'value': datetime.now().isoformat()}
            }
            print(f"  ✓ Category {cat_id}: {category_name}")
        
        # ===== MIGRATE CROPS =====
        print("\n[2/3] Migrating Crops...")
        cursor.execute("""
            SELECT id, category_id, crop_name, description, image, science_name, 
                   duration, varieties, soil_climate, season, materials, main_field, 
                   weed_control, irrigation, growth_management, harvesting 
            FROM tbl_Crop
        """)
        crops = cursor.fetchall()
        
        for crop_data in crops:
            crop_id = crop_data[0]
            image_b64 = encode_blob_to_base64(crop_data[4])
            
            firestore_data['crops'][str(crop_id)] = {
                'id': crop_id,
                'categoryId': crop_data[1],
                'cropName': crop_data[2],
                'description': crop_data[3],
                'image': image_b64,
                'scienceName': crop_data[5],
                'duration': crop_data[6],
                'varieties': crop_data[7].split(',') if crop_data[7] else [],
                'soilClimate': crop_data[8],
                'season': crop_data[9],
                'materials': crop_data[10],
                'mainField': crop_data[11],
                'weedControl': crop_data[12],
                'irrigation': crop_data[13],
                'growthManagement': crop_data[14],
                'harvesting': crop_data[15],
                'createdAt': {'_type': 'Timestamp', 'value': datetime.now().isoformat()}
            }
            print(f"  ✓ Crop {crop_id}: {crop_data[2]}")
        
        # ===== MIGRATE PROCEDURES =====
        print("\n[3/3] Migrating Procedures...")
        cursor.execute("""
            SELECT id, crop_id, step_no, step_procedure, notif_days 
            FROM tbl_Procedure
        """)
        procedures = cursor.fetchall()
        
        for proc_id, crop_id, step_no, step_procedure, notif_days in procedures:
            firestore_data['procedures'][str(proc_id)] = {
                'id': proc_id,
                'cropId': crop_id,
                'stepNo': step_no,
                'stepProcedure': step_procedure,
                'notifDays': notif_days,
                'createdAt': {'_type': 'Timestamp', 'value': datetime.now().isoformat()}
            }
            print(f"  ✓ Procedure {proc_id}: Step {step_no} - {step_procedure}")
        
        conn.close()
        
        # ===== SAVE TO JSON =====
        print(f"\n[SAVING] Writing to {OUTPUT_JSON_PATH}...")
        with open(OUTPUT_JSON_PATH, 'w', encoding='utf-8') as f:
            json.dump(firestore_data, f, indent=2, ensure_ascii=False)
        
        # ===== PRINT SUMMARY =====
        print("\n" + "=" * 70)
        print("MIGRATION SUMMARY")
        print("=" * 70)
        print(f"✓ Categories migrated:  {len(firestore_data['categories'])}")
        print(f"✓ Crops migrated:       {len(firestore_data['crops'])}")
        print(f"✓ Procedures migrated:  {len(firestore_data['procedures'])}")
        print(f"✓ Output file:          {OUTPUT_JSON_PATH}")
        print("\n" + "=" * 70)
        print("NEXT STEPS:")
        print("=" * 70)
        print("1. Go to Firebase Console: https://console.firebase.google.com/")
        print("2. Select your project: 'cropmanagementapp-ba8af'")
        print("3. Go to Firestore Database")
        print("4. Use the 'firestore_migration.json' file to import data")
        print("5. Or use Firebase CLI: firebase firestore:bulk-import firestore_migration.json")
        print("\nDocumentation: https://firebase.google.com/docs/firestore/manage-data/export-import")
        print("=" * 70 + "\n")
        
        return True
        
    except sqlite3.Error as e:
        print(f"Database error: {e}")
        sys.exit(1)
    except Exception as e:
        print(f"Error: {e}")
        sys.exit(1)

if __name__ == '__main__':
    export_sqlite_to_json()
