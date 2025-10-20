import sqlite3
import json

# Connect to SQLite database
conn = sqlite3.connect('app/src/main/assets/cms.sqlite')
cursor = conn.cursor()

# Get all table names
cursor.execute("SELECT name FROM sqlite_master WHERE type='table';")
tables = cursor.fetchall()

schema = {}

for table in tables:
    table_name = table[0]
    
    # Get column info
    cursor.execute(f"PRAGMA table_info({table_name})")
    columns = cursor.fetchall()
    
    # Get sample data count
    cursor.execute(f"SELECT COUNT(*) FROM {table_name}")
    row_count = cursor.fetchone()[0]
    
    schema[table_name] = {
        'columns': [{'name': col[1], 'type': col[2], 'notnull': col[3], 'default': col[4]} for col in columns],
        'row_count': row_count
    }
    
    print(f"\n{'='*60}")
    print(f"TABLE: {table_name} (Rows: {row_count})")
    print(f"{'='*60}")
    for col in columns:
        print(f"  {col[1]:30} {col[2]:15} {'NOT NULL' if col[3] else ''}")

conn.close()

print("\n" + "="*60)
print("Schema extracted successfully!")
