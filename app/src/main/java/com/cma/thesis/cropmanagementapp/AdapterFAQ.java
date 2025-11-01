package com.cma.thesis.cropmanagementapp;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

/**
 * Adapter for FAQ ExpandableListView
 */
public class AdapterFAQ extends BaseExpandableListAdapter {
    
    private Context context;
    private List<String> categories;
    private HashMap<String, List<Class_FAQ>> faqData;
    
    public AdapterFAQ(Context context, List<String> categories, HashMap<String, List<Class_FAQ>> faqData) {
        this.context = context;
        this.categories = categories;
        this.faqData = faqData;
    }
    
    @Override
    public int getGroupCount() {
        return categories.size();
    }
    
    @Override
    public int getChildrenCount(int groupPosition) {
        String category = categories.get(groupPosition);
        List<Class_FAQ> faqs = faqData.get(category);
        return faqs != null ? faqs.size() : 0;
    }
    
    @Override
    public Object getGroup(int groupPosition) {
        return categories.get(groupPosition);
    }
    
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        String category = categories.get(groupPosition);
        List<Class_FAQ> faqs = faqData.get(category);
        return faqs != null ? faqs.get(childPosition) : null;
    }
    
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }
    
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }
    
    @Override
    public boolean hasStableIds() {
        return false;
    }
    
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String categoryTitle = (String) getGroup(groupPosition);
        
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(android.R.layout.simple_expandable_list_item_1, null);
        }
        
        TextView textView = convertView.findViewById(android.R.id.text1);
        textView.setText(categoryTitle);
        textView.setTypeface(null, Typeface.BOLD);
        textView.setPadding(60, 20, 20, 20);
        textView.setTextSize(16);
        
        return convertView;
    }
    
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        Class_FAQ faq = (Class_FAQ) getChild(groupPosition, childPosition);
        
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            
            // Create a simple LinearLayout with two TextViews
            android.widget.LinearLayout layout = new android.widget.LinearLayout(context);
            layout.setOrientation(android.widget.LinearLayout.VERTICAL);
            layout.setPadding(40, 15, 20, 15);
            
            TextView questionView = new TextView(context);
            questionView.setId(android.R.id.text1);
            questionView.setTextSize(14);
            questionView.setTypeface(null, Typeface.BOLD);
            questionView.setTextColor(0xFF333333);
            layout.addView(questionView);
            
            TextView answerView = new TextView(context);
            answerView.setId(android.R.id.text2);
            answerView.setTextSize(13);
            answerView.setTextColor(0xFF666666);
            answerView.setPadding(0, 8, 0, 0);
            layout.addView(answerView);
            
            convertView = layout;
        }
        
        TextView questionView = convertView.findViewById(android.R.id.text1);
        TextView answerView = convertView.findViewById(android.R.id.text2);
        
        if (faq != null) {
            questionView.setText("Q: " + faq.getQuestion());
            answerView.setText("A: " + faq.getAnswer());
        }
        
        return convertView;
    }
    
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
