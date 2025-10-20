package com.cma.thesis.cropmanagementapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.SQLException;

public class Activity_AddCrops extends AppCompatActivity {

    public static Class_DatabaseHelper mydatabase;

    EditText cropname,description;
    Spinner category;
    ImageView cropImage;
    Button save,chooseImage;

    final int request = 999;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_crops);
        Init();

        // action when clicking choose button
        chooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(
                        Activity_AddCrops.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        request);

            }
        });

        // action when clicking save button
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mydatabase = new Class_DatabaseHelper(getApplicationContext());
                try
                {
                    mydatabase.checkAndCopyDatabase();
                    mydatabase.openDatabase();
                }
                catch (SQLException e)
                {
                    System.out.println(e.getMessage());
                }
                try
                {
                    mydatabase.InsertCrops(13,
                            cropname.getText().toString(),
                            description.getText().toString(),
                            1,
                            imageConverted(cropImage)
                    );
                    //Toast.makeText(getApplicationContext(), cropname.getText().toString().trim().toUpperCase() + " successfully added", Toast.LENGTH_SHORT).show();
                }
                catch (Exception e)
                {
                    //Toast.makeText(Activity_AddCrops.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

    private byte[] imageConverted(ImageView image)
    {
        Bitmap bm = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream bstream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG,100,bstream);
        byte[] bytearray = bstream.toByteArray();

        return bytearray;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        if(requestCode == request)
        {
            if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED)
            {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,request);
            }
            else
            {
                //Toast.makeText(getApplicationContext(), "Access denied", Toast.LENGTH_LONG);
            }
            return;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode==request && resultCode==RESULT_OK && data!=null)
        {
            Uri uri = data.getData();
            try {
                InputStream is = getContentResolver().openInputStream(uri);
                Bitmap bm = BitmapFactory.decodeStream(is);
                cropImage.setImageBitmap(bm);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //initialized variables
    private void Init()
    {
        cropname    = (EditText) findViewById(R.id.etcropnameupdate);
        description = (EditText) findViewById(R.id.etdescription);
        category    = (Spinner)  findViewById(R.id.spCategory);
        cropImage   = (ImageView)findViewById(R.id.imgCropImage);
        save        = (Button) findViewById(R.id.btnsaveCrops);
        chooseImage = (Button) findViewById(R.id.btnchooseImage);
    }



}