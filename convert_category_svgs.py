#!/usr/bin/env python3
"""
Batch convert SVG icons from assets/icons to Android vector drawables
"""
import os
import re

# Color mapping for each category
COLORS = {
    'medicinal': '#9C27B0',  # Purple for medicinal
    'organic': '#4CAF50',    # Green for organic
    'pest': '#FF5722',       # Red-orange for pest
    'plantation': '#795548', # Brown for plantation/trees
    'pulses': '#FFC107',     # Amber for pulses
    'spices': '#FF9800',     # Orange for spices
    'vegetable': '#8BC34A',  # Light green for vegetables
}

def read_svg_path(svg_file):
    """Extract path data from SVG file"""
    with open(svg_file, 'r', encoding='utf-8') as f:
        content = f.read()
        # Extract path d attribute
        match = re.search(r'<path[^>]*d="([^"]+)"', content)
        if match:
            return match.group(1)
    return None

def create_vector_drawable(name, path_data, color):
    """Create Android vector drawable XML"""
    return f'''<?xml version="1.0" encoding="utf-8"?>
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="64dp"
    android:height="64dp"
    android:viewportWidth="640"
    android:viewportHeight="640">
    <path
        android:fillColor="{color}"
        android:pathData="{path_data}"/>
</vector>
'''

# SVG to convert
svgs_to_convert = ['medicinal', 'organic', 'pest', 'plantation', 'pulses', 'spices', 'vegetable']

assets_dir = 'app/src/main/assets/icons'
drawable_dir = 'app/src/main/res/drawable'

for svg_name in svgs_to_convert:
    svg_path = os.path.join(assets_dir, f'{svg_name}.svg')
    output_path = os.path.join(drawable_dir, f'ic_category_{svg_name}.xml')
    
    if not os.path.exists(svg_path):
        print(f"⚠️  SVG not found: {svg_path}")
        continue
    
    path_data = read_svg_path(svg_path)
    if not path_data:
        print(f"⚠️  Could not extract path from: {svg_path}")
        continue
    
    color = COLORS.get(svg_name, '#4CAF50')  # Default to green
    xml_content = create_vector_drawable(svg_name, path_data, color)
    
    with open(output_path, 'w', encoding='utf-8') as f:
        f.write(xml_content)
    
    print(f"✅ Converted {svg_name}.svg → ic_category_{svg_name}.xml ({color})")

print("\n✨ All category icons converted!")
