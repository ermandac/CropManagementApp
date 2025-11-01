# Firebase Storage Setup Guide

## Error: "Object does not exist at location"

This error occurs when Firebase Storage hasn't been properly initialized in your Firebase Console. Follow these steps to set it up:

## Step-by-Step Setup Instructions

### 1. Open Firebase Console
1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Select your project: **cropmanagementapp-ba8af**

### 2. Enable Firebase Storage
1. In the left sidebar, click on **Build** → **Storage**
2. If Storage is not enabled, click **Get Started**
3. You'll see a dialog about Security Rules

### 3. Configure Storage Security Rules
Choose one of the following options:

#### Option A: Start in Test Mode (Recommended for Development)
```
rules_version = '2';
service firebase.storage {
  match /b/{bucket}/o {
    match /{allPaths=**} {
      allow read, write: if request.time < timestamp.date(2025, 12, 31);
    }
  }
}
```
**Note:** This allows all reads and writes until Dec 31, 2025. Good for development but NOT for production.

#### Option B: Production Rules (Recommended for Live App)
```
rules_version = '2';
service firebase.storage {
  match /b/{bucket}/o {
    // Allow anyone to read images
    match /crops/{categoryId}/{fileName} {
      allow read: if true;
      // Only authenticated users can write
      allow write: if request.auth != null;
    }
    
    // Deny all other paths by default
    match /{allPaths=**} {
      allow read, write: if false;
    }
  }
}
```

### 4. Verify Storage Bucket URL
Make sure your Storage bucket URL matches: `gs://cropmanagementapp-ba8af.firebasestorage.app`

This should be visible in:
- Firebase Console → Storage → Files tab (top of the page)
- Your `google-services.json` file under `storage_bucket`

### 5. Test the Setup
1. In Firebase Console → Storage, you should see the Files tab
2. The bucket should be empty initially
3. Try uploading an image from your app's admin panel
4. If successful, you'll see a folder structure: `crops/{categoryId}/{imageName}.jpg`

## Alternative: Using Firebase CLI

If you prefer using the command line:

```bash
# Install Firebase CLI if not already installed
npm install -g firebase-tools

# Login to Firebase
firebase login

# Initialize Firebase Storage in your project directory
firebase init storage

# Select your project: cropmanagementapp-ba8af

# Deploy storage rules
firebase deploy --only storage
```

## Troubleshooting

### Issue: Still getting "Object does not exist" error

**Possible causes:**
1. **Storage not initialized**: Complete steps 1-3 above
2. **Wrong bucket URL**: Verify the bucket URL matches in both Firebase Console and your app
3. **Network/Firebase connection issue**: Check your internet connection and Firebase status
4. **App not connected to Firebase**: Verify `google-services.json` is in the correct location (`app/` directory)

### Issue: Permission denied

**Possible causes:**
1. **Authentication required**: If using production rules, ensure users are authenticated before uploading
2. **Rules too restrictive**: Review your Security Rules in Firebase Console

### Issue: Upload starts but fails

**Check:**
1. Image file size (app compresses to max 1024px, but check original size)
2. Firebase project quota (free tier has 5GB storage limit)
3. Network stability during upload

## Current App Configuration

The app has been updated to:
- Use explicit Storage bucket URL: `gs://cropmanagementapp-ba8af.firebasestorage.app`
- Handle Android 13+ storage permissions correctly
- Request runtime permissions before accessing images
- Compress images before upload (max 1024px, 80% quality)

## What Happens When You Upload an Image

1. User selects "Select Image" button
2. App checks and requests storage permissions (if needed)
3. User picks an image from gallery
4. App compresses the image
5. App uploads to Firebase Storage at path: `crops/{categoryId}/{cropName}_{timestamp}.jpg`
6. Firebase returns a download URL
7. App saves the URL to Firestore with the crop data

## Important Notes

- Free tier: 5GB storage, 1GB/day downloads, 20K/day uploads
- Images are organized by category for better management
- Each image gets a unique timestamp-based name to avoid conflicts
- Old images are automatically deleted when updating a crop with a new image

## Need Help?

If you continue to have issues:
1. Check Firebase Console for any error messages
2. Enable Storage in Firebase Console if not already done
3. Verify your security rules are set correctly
4. Check logcat for detailed error messages from the app

---
**Last Updated:** October 26, 2025
