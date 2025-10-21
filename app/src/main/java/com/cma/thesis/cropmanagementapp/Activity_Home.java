package com.cma.thesis.cropmanagementapp;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import android.util.Log;
import android.view.View;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.sql.SQLException;

public class Activity_Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

         static Class_DatabaseHelper dbhelper;
    private static final String NOTIFICATION_CHANNEL_ID = "crop_notifications";
    private static final int REQ_POST_NOTIFICATIONS = 1001;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ensureNotificationSetup();

        dbhelper = new Class_DatabaseHelper(this);
        try
        {
            dbhelper.checkAndCopyDatabase();
            dbhelper.openDatabase();
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }

        // Initialize Firestore and upload migrated data
        initializeFirestore();
    }

    private void initializeFirestore() {
        // Data migration is complete - disable automatic migration to avoid redundant uploads
        // Uncomment the code below if you need to re-migrate data to Firestore
        /*
        FirestoreInitializer initializer = new FirestoreInitializer(this);
        initializer.uploadMigratedData(new FirestoreInitializer.UploadCallback() {
            @Override
            public void onSuccess(String message) {
                Log.d("TAG", "Firestore Upload Success: " + message);
                Toast.makeText(Activity_Home.this, "Data migrated to Firestore!", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(String error) {
                Log.e("TAG", "Firestore Upload Error: " + error);
                Toast.makeText(Activity_Home.this, "Error: " + error, Toast.LENGTH_LONG).show();
            }
        });
        */
        Log.d("TAG", "Firestore migration already complete - skipping automatic upload");
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Activity_Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.nav_suggested) {
            Log.d("TAG","error");
        }

        if (id == R.id.nav_fertilizers) {
            Log.d("TAG","error");
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        if (id == R.id.nav_suggested) {
            Intent intent = new Intent(getApplicationContext(), activity_topcrop.class);
            startActivity(intent);
        }
        else  if (id == R.id.nav_fertilizers) {
            Intent intent = new Intent(getApplicationContext(), activity_fetilizer.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_admin_panel) {
            Intent intent = new Intent(getApplicationContext(), Activity_AdminLogin.class);
            startActivity(intent);
        }
        return true;
    }



    public void onClickFruits(View v)
    {
        Intent intent = new Intent(getApplicationContext(),Activity_CropList.class);
        intent.putExtra("category","1");
        startActivity(intent);
    }
    public void onClickMedicinal(View v)
    {
        Intent intent = new Intent(getApplicationContext(),Activity_CropList.class);
        intent.putExtra("category","6");
        startActivity(intent);
    }
    public void onClickPlantation(View v)
    {
        Intent intent = new Intent(getApplicationContext(),Activity_CropList.class);
        intent.putExtra("category","5");
        startActivity(intent);
    }
    public void onClickPulses(View v)
    {
        Intent intent = new Intent(getApplicationContext(),Activity_CropList.class);
        intent.putExtra("category","3");
        startActivity(intent);
    }
    public void onClickSpices(View v)
    {
        Intent intent = new Intent(getApplicationContext(),Activity_CropList.class);
        intent.putExtra("category","4");
        startActivity(intent);
    }
    public void onClickVeges(View v)
    {
        Intent intent = new Intent(getApplicationContext(),Activity_CropList.class);
        intent.putExtra("category","2");
        startActivity(intent);
    }

    public void onClickPest(View v)
    {
        notifyComingSoon("Pest");
    }

    public void onClickOrganic(View v)
    {
        notifyComingSoon("Organic");
    }

    public void SetNotification()
    {
        if (Build.VERSION.SDK_INT >= 33) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, REQ_POST_NOTIFICATIONS);
                return;
            }
        }

        NotificationCompat.Builder notificationbuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Reminder")
                .setContentText("Test");
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1,notificationbuilder.build());
    }

    private void notifyComingSoon(String feature) {
        Toast.makeText(this, "Coming soon: " + feature, Toast.LENGTH_SHORT).show();
    }

    private void ensureNotificationSetup() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel channel = manager.getNotificationChannel(NOTIFICATION_CHANNEL_ID);
            if (channel == null) {
                channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "General Notifications", NotificationManager.IMPORTANCE_DEFAULT);
                manager.createNotificationChannel(channel);
            }
        }
    }
}
