package com.cma.thesis.cropmanagementapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Provides FAQ data organized by categories
 */
public class FAQDataProvider {
    
    /**
     * Get all FAQ categories
     */
    public static List<String> getCategories() {
        List<String> categories = new ArrayList<>();
        categories.add("General");
        categories.add("Using Crop Information");
        categories.add("Using the Planner");
        categories.add("Pest Management");
        categories.add("Fertilizers & Organic Farming");
        categories.add("Troubleshooting");
        return categories;
    }
    
    /**
     * Get all FAQs organized by category
     */
    public static HashMap<String, List<Class_FAQ>> getAllFAQs() {
        HashMap<String, List<Class_FAQ>> faqData = new HashMap<>();
        
        // General FAQs
        faqData.put("General", getGeneralFAQs());
        
        // Crop Information FAQs
        faqData.put("Using Crop Information", getCropInfoFAQs());
        
        // Planner FAQs
        faqData.put("Using the Planner", getPlannerFAQs());
        
        // Pest Management FAQs
        faqData.put("Pest Management", getPestFAQs());
        
        // Fertilizers & Organic FAQs
        faqData.put("Fertilizers & Organic Farming", getFertilizerOrganicFAQs());
        
        // Troubleshooting FAQs
        faqData.put("Troubleshooting", getTroubleshootingFAQs());
        
        return faqData;
    }
    
    private static List<Class_FAQ> getGeneralFAQs() {
        List<Class_FAQ> faqs = new ArrayList<>();
        
        faqs.add(new Class_FAQ(
            "What is this app for?",
            "This app helps farmers and agricultural professionals manage their crops effectively. It provides detailed cultivation guidelines, planning tools, pest management information, fertilizer recommendations, and organic farming techniques.",
            "General"
        ));
        
        faqs.add(new Class_FAQ(
            "Do I need internet to use this app?",
            "Partially. The Planner feature works offline - you can create and manage your crop plans without internet. However, viewing crop information, pest management guides, fertilizers, and organic farming content requires an internet connection.",
            "General"
        ));
        
        faqs.add(new Class_FAQ(
            "What crops are covered in this app?",
            "The app covers 6 main categories:\n• Vegetables (Tomato, Potato, Onion, etc.)\n• Fruits (Mango, Banana, Citrus, etc.)\n• Pulses (Chickpea, Lentil, Beans, etc.)\n• Spices (Turmeric, Chili, Ginger, etc.)\n• Plantation Crops (Coffee, Tea, Rubber, etc.)\n• Medicinal Plants (Tulsi, Aloe vera, etc.)",
            "General"
        ));
        
        faqs.add(new Class_FAQ(
            "Is this app free?",
            "Yes, this app is completely free to use for all farmers and agricultural professionals.",
            "General"
        ));
        
        return faqs;
    }
    
    private static List<Class_FAQ> getCropInfoFAQs() {
        List<Class_FAQ> faqs = new ArrayList<>();
        
        faqs.add(new Class_FAQ(
            "How do I find information about a specific crop?",
            "1. From the home screen, tap the category card (e.g., Vegetables, Fruits)\n2. You'll see a list of all crops in that category\n3. Tap on any crop to view detailed cultivation guidelines\n4. Expand different sections to see specific information like soil requirements, irrigation, pest control, etc.",
            "Using Crop Information"
        ));
        
        faqs.add(new Class_FAQ(
            "What information is available for each crop?",
            "Each crop includes:\n• Introduction and overview\n• Scientific name\n• Growing duration and season\n• Varieties available\n• Soil and climate requirements\n• Land preparation steps\n• Materials needed\n• Irrigation requirements\n• Weed control methods\n• Pest and disease management\n• Harvesting guidelines",
            "Using Crop Information"
        ));
        
        faqs.add(new Class_FAQ(
            "Can I search for crops?",
            "Yes! When viewing a crop category list, use the search icon at the top to search for specific crops by name.",
            "Using Crop Information"
        ));
        
        faqs.add(new Class_FAQ(
            "How do I navigate between different sections of crop information?",
            "Crop information is organized in expandable sections. Simply tap on any section header (like 'Soil & Climate', 'Irrigation', etc.) to expand and view the details. Tap again to collapse.",
            "Using Crop Information"
        ));
        
        return faqs;
    }
    
    private static List<Class_FAQ> getPlannerFAQs() {
        List<Class_FAQ> faqs = new ArrayList<>();
        
        faqs.add(new Class_FAQ(
            "What is the Planner feature?",
            "The Planner helps you create cultivation schedules for specific crops. You can set start and end dates, choose your planting method (Sabong Tanim or Lipat Tanim), enable notifications, and track your farming progress. All data is stored locally on your device and works offline!",
            "Using the Planner"
        ));
        
        faqs.add(new Class_FAQ(
            "How do I create a new crop plan?",
            "1. From the Home screen, tap a crop category (e.g., Vegetables, Fruits)\n2. Select the crop you want to plant (e.g., Tomato, Rice)\n3. In the crop details, look for the Planner option\n4. First, choose your planting method:\n   • Sabong Tanim (Direct Seeding) - default\n   • Lipat Tanim (Transplanting)\n5. Check 'Enable Notifications' if you want reminders (recommended)\n6. Tap 'Create Plan' button\n7. Select your START DATE from the calendar\n8. Select your END DATE (harvest/completion date)\n9. Your plan is automatically saved!\n\nThe plan will appear in the list below the settings.",
            "Using the Planner"
        ));
        
        faqs.add(new Class_FAQ(
            "What are Sabong Tanim and Lipat Tanim?",
            "These are Filipino terms for planting methods:\n\n• Sabong Tanim (Direct Seeding): Seeds are planted directly in the final growing location\n\n• Lipat Tanim (Transplanting): Seeds are first grown in a nursery/seedbed, then transplanted to the main field when ready\n\nChoose the method you actually use for farming. You can change this later when editing a plan.",
            "Using the Planner"
        ));
        
        faqs.add(new Class_FAQ(
            "How do I view my existing plans?",
            "1. Go to Home screen\n2. Select the crop category\n3. Tap on the crop you created a plan for\n4. Scroll to the Planner section\n5. You'll see all plans for that crop showing:\n   • Start date - End date\n   • Planting method\n   • Edit and Delete buttons\n\nTap on a plan to view details or add comments about your progress.",
            "Using the Planner"
        ));
        
        faqs.add(new Class_FAQ(
            "Can I edit or delete a plan?",
            "Yes! Each plan has edit and delete options:\n\n• To Edit: Tap the edit icon (pencil) on the plan. You can:\n  - Edit Dates: Change start and/or end date\n  - Change Method: Switch between Sabong Tanim and Lipat Tanim\n\n• To Delete: Tap the delete icon (trash) and confirm\n\nNote: Deleting a plan removes all associated data including comments!",
            "Using the Planner"
        ));
        
        faqs.add(new Class_FAQ(
            "How do notifications work?",
            "Get reminded when your crop is ready to harvest:\n\n• When creating a plan, check the 'Enable Notifications' box\n• You'll receive a notification on the harvest date (end date)\n• The notification reminds you that your crop is ready\n• Works completely offline - no internet needed\n• Ensure notification permissions are enabled:\n  Settings > Apps > Crop Management App > Notifications > Allow\n\nSimple and effective - never miss harvest time!",
            "Using the Planner"
        ));
        
        faqs.add(new Class_FAQ(
            "Does the Planner work without internet?",
            "Yes, completely offline!\n\n• All plans are stored in a local database on your device\n• Create, view, edit, and delete plans without internet\n• Notifications work offline too\n• No data is sent to the cloud\n\nPerfect for farmers in rural areas with limited connectivity!",
            "Using the Planner"
        ));
        
        faqs.add(new Class_FAQ(
            "Can I create multiple plans?",
            "Yes! You can create as many plans as you need:\n\n• Multiple plans for different crops (Tomato, Rice, Corn, etc.)\n• Multiple plans for the same crop (different cycles or fields)\n• Each plan has independent dates and settings\n• Plans are organized by crop for easy access\n\nManage your entire farming schedule in one app!",
            "Using the Planner"
        ));
        
        faqs.add(new Class_FAQ(
            "How do I add comments to track progress?",
            "Comments let you record daily activities and observations:\n\n1. Open the crop's Planner section\n2. Tap on the plan you want to update\n3. Look for the Comments section\n4. Add your comment (e.g., 'Watered today', 'Applied fertilizer', 'Noticed pests')\n5. Save the comment\n\nComments are automatically timestamped, creating a complete farming journal for each plan!",
            "Using the Planner"
        ));
        
        return faqs;
    }
    
    private static List<Class_FAQ> getPestFAQs() {
        List<Class_FAQ> faqs = new ArrayList<>();
        
        faqs.add(new Class_FAQ(
            "How do I identify pests affecting my crops?",
            "1. Go to Home screen and tap 'Pest Management'\n2. Browse through the pest list or use search\n3. Look for pests by symptoms you're observing\n4. Each pest entry includes:\n   • Photos/description for identification\n   • Symptoms on affected crops\n   • Scientific name and category\n   • Severity level",
            "Pest Management"
        ));
        
        faqs.add(new Class_FAQ(
            "What pest control methods are provided?",
            "For each pest, you'll find:\n• Chemical control methods (pesticides with dosage)\n• Biological control options (natural predators)\n• Cultural practices (crop management)\n• Prevention tips to avoid future infestations\n\nWe recommend starting with prevention and organic methods before using chemicals.",
            "Pest Management"
        ));
        
        faqs.add(new Class_FAQ(
            "How do I find pests that affect a specific crop?",
            "1. Go to your crop information (e.g., Tomato)\n2. Scroll to the 'Pest & Disease Control' section\n3. Tap to expand and see common pests for that crop\n4. Tap on any pest to view detailed control methods",
            "Pest Management"
        ));
        
        faqs.add(new Class_FAQ(
            "What do the severity levels mean?",
            "• Low: Minor damage, easy to control\n• Medium: Moderate damage, requires attention\n• High: Severe damage, immediate action needed\n\nHigh severity pests can cause significant crop loss if not controlled quickly.",
            "Pest Management"
        ));
        
        return faqs;
    }
    
    private static List<Class_FAQ> getFertilizerOrganicFAQs() {
        List<Class_FAQ> faqs = new ArrayList<>();
        
        faqs.add(new Class_FAQ(
            "How do I find the right fertilizer for my crop?",
            "1. Tap 'Fertilizers' from the home screen\n2. Browse the fertilizer list\n3. Each fertilizer shows:\n   • NPK ratio (Nitrogen-Phosphorus-Potassium)\n   • Suitable crops\n   • Application rate and method\n   • Benefits\n\nYou can also check your crop's cultivation guidelines for recommended fertilizers.",
            "Fertilizers & Organic Farming"
        ));
        
        faqs.add(new Class_FAQ(
            "What organic farming techniques are available?",
            "The app covers:\n• Organic fertilizers (Compost, Vermicompost, Green manure)\n• Natural pest control (Neem, botanical pesticides)\n• Soil management (Mulching, cover cropping)\n• Water conservation (Drip irrigation)\n• Sustainable practices (Crop rotation, companion planting)\n\nEach technique includes step-by-step application methods.",
            "Fertilizers & Organic Farming"
        ));
        
        faqs.add(new Class_FAQ(
            "How do I make compost or vermicompost?",
            "1. Go to 'Organic Farming' from home screen\n2. Search for 'Compost' or 'Vermicompost'\n3. Tap to view detailed instructions including:\n   • Materials needed\n   • Step-by-step preparation method\n   • Duration and maintenance\n   • Application guidelines\n   • Benefits for your crops",
            "Fertilizers & Organic Farming"
        ));
        
        faqs.add(new Class_FAQ(
            "What is NPK ratio and why is it important?",
            "NPK stands for Nitrogen (N), Phosphorus (P), and Potassium (K) - the three main nutrients plants need.\n\n• Nitrogen: For leaf growth (green parts)\n• Phosphorus: For root and flower development\n• Potassium: For overall plant health and disease resistance\n\nDifferent crops need different NPK ratios. For example, leafy vegetables need more nitrogen, while flowering crops need more phosphorus.",
            "Fertilizers & Organic Farming"
        ));
        
        return faqs;
    }
    
    private static List<Class_FAQ> getTroubleshootingFAQs() {
        List<Class_FAQ> faqs = new ArrayList<>();
        
        faqs.add(new Class_FAQ(
            "The app says 'No internet connection'. What should I do?",
            "Crop information, pest data, and fertilizer details require internet. Please:\n1. Check your WiFi or mobile data connection\n2. Try refreshing the page\n3. If problem persists, restart the app\n\nNote: The Planner works offline, so you can still manage your crop plans without internet.",
            "Troubleshooting"
        ));
        
        faqs.add(new Class_FAQ(
            "Crop information is not loading. How do I fix this?",
            "Try these steps:\n1. Ensure you have active internet connection\n2. Pull down to refresh the screen\n3. Close and reopen the app\n4. Clear app cache: Settings > Apps > Crop Management App > Clear Cache\n5. Update the app if a new version is available",
            "Troubleshooting"
        ));
        
        faqs.add(new Class_FAQ(
            "My planner data disappeared. How do I recover it?",
            "Planner data is stored on your device. If it's missing:\n1. Make sure you're using the same device and user account\n2. Don't clear app data (only clear cache if needed)\n3. Check if you accidentally deleted plans\n\nTo prevent loss, avoid:\n• Clearing app data\n• Uninstalling the app without backup\n• Using multiple accounts on same device",
            "Troubleshooting"
        ));
        
        faqs.add(new Class_FAQ(
            "Notifications are not working. What should I do?",
            "1. Go to your phone's Settings > Apps > Crop Management App\n2. Enable 'Notifications'\n3. For Android 13+, ensure notification permission is granted\n4. Check that 'Do Not Disturb' mode is off\n5. Make sure battery optimization is not blocking the app",
            "Troubleshooting"
        ));
        
        faqs.add(new Class_FAQ(
            "How do I clear app cache without losing my data?",
            "Go to Settings > Apps > Crop Management App > Storage > Clear Cache\n\nImportant: Choose 'Clear Cache' NOT 'Clear Data'\n• Clear Cache: Safe - removes temporary files, keeps your planner data\n• Clear Data: Dangerous - deletes all your saved plans and settings",
            "Troubleshooting"
        ));
        
        faqs.add(new Class_FAQ(
            "The app is running slowly. How can I speed it up?",
            "1. Close other apps running in background\n2. Clear app cache (Settings > Apps > Clear Cache)\n3. Restart your phone\n4. Ensure you have enough storage space (at least 100MB free)\n5. Update to the latest app version\n6. If using old device, consider reducing background processes",
            "Troubleshooting"
        ));
        
        return faqs;
    }
}
