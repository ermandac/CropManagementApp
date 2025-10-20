#!/usr/bin/env python3
"""
Firestore Direct Upload Script
Uploads migrated SQLite data directly to Google Cloud Firestore
using Firebase CLI
"""

import json
import sys
from pathlib import Path

# Configuration
MIGRATION_JSON = 'firestore_migration.json'

def upload_with_firestore_cli():
    """
    Firebase CLI upload instructions
    """
    print("=" * 70)
    print("FIRESTORE DATA UPLOAD - FIREBASE CLI METHOD")
    print("=" * 70)
    
    # Check if JSON file exists
    if not Path(MIGRATION_JSON).exists():
        print(f"Error: {MIGRATION_JSON} not found!")
        print("   Run 'python migrate_to_firestore.py' first")
        sys.exit(1)
    
    print("\nFirebase CLI is the most reliable method for uploading.\n")
    print("Installation Instructions:")
    print("-" * 70)
    print("\n1. Install Node.js from: https://nodejs.org/")
    print("   (Choose LTS version)\n")
    
    print("2. Install Firebase CLI:")
    print("   npm install -g firebase-tools\n")
    
    print("3. Login to Firebase:")
    print("   firebase login\n")
    
    print("4. Navigate to your project directory and run:")
    print("   firebase firestore:bulk-import firestore_migration.json --project cropmanagementapp-ba8af\n")
    
    print("-" * 70)
    print("\nData will be uploaded to Firestore automatically.\n")
    
    return True

if __name__ == '__main__':
    print("\n" + "=" * 70)
    print("FIRESTORE UPLOAD OPTIONS")
    print("=" * 70 + "\n")
    
    upload_with_firestore_cli()
    
    print("=" * 70)
    print("NEXT STEPS:")
    print("=" * 70)
    print("1. Install Firebase CLI (see above)")
    print("2. Run: firebase firestore:bulk-import firestore_migration.json --project cropmanagementapp-ba8af")
    print("3. Wait for upload to complete")
    print("4. Verify data in Firebase Console")
    print("5. Then we'll update Android Activities to use Firestore")
    print("=" * 70 + "\n")
