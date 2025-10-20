package com.cma.thesis.cropmanagementapp;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;



public class Class_Functions extends AppCompatActivity {

    public static byte[] imageConverted(ImageView image)
    {
        Bitmap bm = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream bstream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG,100,bstream);
        byte[] bytearray = bstream.toByteArray();

        return bytearray;
    }
}
