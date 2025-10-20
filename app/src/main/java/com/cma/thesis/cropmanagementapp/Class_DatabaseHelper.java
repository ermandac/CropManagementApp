package com.cma.thesis.cropmanagementapp;



import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.nfc.Tag;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;

import static android.R.attr.tag;


public class Class_DatabaseHelper extends SQLiteOpenHelper {
    private static String DB_Name = "cms.sqlite";
    private SQLiteDatabase myDatabase;
    private final Context myContext;
    private String cropsTableName;

    public Class_DatabaseHelper(Context context) {
        super(context, DB_Name, null, 1);
        this.myContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private boolean checkDatabase() {
        SQLiteDatabase checkDB = null;
        try {
            String myPath = myContext.getDatabasePath(DB_Name).getPath();
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
        } catch (SQLiteException e) {

        }
        if (checkDB != null) checkDB.close();
        return checkDB != null ? true : false;
    }
    private void copyDatabase() throws IOException {
        InputStream myInput = myContext.getAssets().open(DB_Name);
        String outFileName = myContext.getDatabasePath(DB_Name).getPath();
        java.io.File outFile = new java.io.File(outFileName).getParentFile();
        if (outFile != null && !outFile.exists()) {
            // ensure databases directory exists
            outFile.mkdirs();
        }
        OutputStream myOutput = new FileOutputStream(outFileName);
        byte[] buffer=new byte[1024];
        int length;
        while((length =myInput.read(buffer))>0){
            myOutput.write(buffer,0,length);
        }
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }
    public void openDatabase() throws SQLException {
        String myPath = myContext.getDatabasePath(DB_Name).getPath();
        myDatabase=SQLiteDatabase.openDatabase(myPath,null,SQLiteDatabase.OPEN_READWRITE);
    }

    public void ExeSQLData(String sql) throws SQLException{
        myDatabase.execSQL(sql);
    }
    public Cursor QueryData(String query) throws SQLException{
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery(query,null);
    }

    public Cursor QueryData(String query, String[] args) throws SQLException{
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery(query, args);
    }

    @Override
    public synchronized void close() {
        if (myDatabase !=null)
            myDatabase.close();
        super.close();
    }
    public void checkAndCopyDatabase(){
        boolean dbExist=checkDatabase();
        if (dbExist) {
              Log.d("TAG","Database already exist");
            //Toast.makeText(myContext, "testesttest", Toast.LENGTH_SHORT).show();
        }else{
            this.getReadableDatabase();
            try {
                copyDatabase();
            }catch (IOException e){
                Log.d("TAG","Error copying database");
                //Toast.makeText(myContext, "erorr eroror", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void InsertCrops(int id,String cropname,String description,int category,byte[] image)
    {

        SQLiteDatabase mydatabase = getWritableDatabase();
        String sql = "INSERT INTO tbl_Crop VALUES(?,?,?,?,?) ";

        SQLiteStatement statement = mydatabase.compileStatement(sql);
        statement.clearBindings();

        statement.bindDouble(1, id);
        statement.bindString(2,cropname);
        statement.bindString(3,description);
        statement.bindString(4,String.valueOf(category));
        statement.bindBlob(5,image);

        statement.executeInsert();
    }



    public Cursor getCropData(String sql)
    {
        SQLiteDatabase mydatabase = getWritableDatabase();
        return mydatabase.rawQuery(sql,null);
    }


    public void UpdateCrops(String cropname,String description,int category,byte[] image,int id)
    {
        SQLiteDatabase mydatabase = getWritableDatabase();
        String sql = "UPDATE tbl_Crop set crop_name=?,description=?,category_id=?,image=? where id=?";

        SQLiteStatement statement = mydatabase.compileStatement(sql);
        statement.clearBindings();

        statement.bindString(1,cropname);
        statement.bindString(2,description);
        statement.bindString(3,String.valueOf(category));
        statement.bindBlob(4,image);
        statement.bindDouble(5,(double)id);

        statement.execute();
        mydatabase.close();
    }

    public void DeleteCropData(int id)
    {
        SQLiteDatabase mydatabase = getWritableDatabase();
        String sql = "Delete from tbl_Crop where id=?";

        SQLiteStatement statement = mydatabase.compileStatement(sql);
        statement.clearBindings();

        statement.bindDouble(1,(double)id);

        statement.execute();
        mydatabase.close();
    }

    //get crop fields
    public Cursor getTableCropFields(String sql)
    {
        SQLiteDatabase mydatabase = getWritableDatabase();
        return mydatabase.rawQuery(sql,null);
    }

    public String getCropFieldById(String id, String columnName) {
        SQLiteDatabase db = getReadableDatabase();
        String value = null;
        Cursor c = null;
        try {
            c = db.rawQuery("SELECT "+columnName+" FROM " + getCropsTableName() + " WHERE id=?", new String[]{ id });
            if (c.moveToFirst()) {
                value = c.getString(0);
            }
        } catch (Exception ignored) {
        } finally {
            if (c != null) c.close();
        }
        return value;
    }

    public synchronized String getCropsTableName() {
        if (cropsTableName != null) return cropsTableName;
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = null;
        try {
            c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name IN ('tbl_Crop','tbl_Crops') LIMIT 1", null);
            if (c.moveToFirst()) {
                cropsTableName = c.getString(0);
            } else {
                cropsTableName = "tbl_Crop"; // default
            }
        } finally {
            if (c != null) c.close();
        }
        return cropsTableName;
    }

    public void loadCrops(String cropLink, String cropID, final TextView viewText, Context context) {
        if (!NetworkUtil.isOnline(context)) {
            viewText.setText("No internet connection");
            return;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.GET, cropLink,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try
                        {
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);

                            //traversing through all the object
                            for (int i = 0; i < array.length(); i++)
                            {
                                //getting product object from json array
                                JSONObject crops = array.getJSONObject(i);
                                //Toast.makeText(myContext, crops.getString("field"), Toast.LENGTH_SHORT).show();
                                viewText.setText(crops.getString("field"));


                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String message = error.getMessage();
                        if (message == null) message = error.toString();
                        viewText.setText("Network error: " + message);
                    }
                });
        // set a sane retry policy
        stringRequest.setRetryPolicy(new com.android.volley.DefaultRetryPolicy(
                7000,
                1,
                1.5f
        ));
        Volley.newRequestQueue(context).add(stringRequest);
    }

    public void loadPest(String pestLink, String cropID, final TextView viewPest, Context context) {
        if (!NetworkUtil.isOnline(context)) {
            viewPest.setText("No internet connection");
            return;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.GET, pestLink,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try
                        {
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);

                            //traversing through all the object
                            for (int i = 0; i < array.length(); i++)
                            {
                                //getting product object from json array
                                JSONObject pest = array.getJSONObject(i);
                                viewPest.setText(pest.getString("pestname"));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String message = error.getMessage();
                        if (message == null) message = error.toString();
                        viewPest.setText("Network error: " + message);
                    }
                });
        stringRequest.setRetryPolicy(new com.android.volley.DefaultRetryPolicy(
                7000,
                1,
                1.5f
        ));
        Volley.newRequestQueue(context).add(stringRequest);
    }
}
