package com.cma.thesis.cropmanagementapp;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

public class Activity_CropDetails extends AppCompatActivity {

    private TextView category,cropname,details;
    private ImageView cropImage;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrollcropdetail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        cropname = (TextView)findViewById(R.id.txtscrollCropname);
        category = (TextView)findViewById(R.id.txtscrollCategory);
        details = (TextView)findViewById(R.id.txtscrollcropDetail);
        cropImage = (ImageView) findViewById(R.id.imgscrollCropImage);

        String cropID = getIntent().getStringExtra("cropid");


        try {
            Cursor cursor = Activity_Home.dbhelper.getCropData("Select * from tbl_Crop where id='"+cropID+"'");
            if(cursor.moveToNext())
            {
                cropname.setText(cursor.getString(1));
                category.setText(cursor.getString(2));
                details.setText(cursor.getString(3));

                byte[] cropImagecursor = cursor.getBlob(4);
                Bitmap bm = BitmapFactory.decodeByteArray(cropImagecursor,0,cropImagecursor.length);
                cropImage.setImageBitmap(bm);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
