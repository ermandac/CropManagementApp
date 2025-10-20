package com.cma.thesis.cropmanagementapp;

import android.app.Application;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
            try {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                throwable.printStackTrace(pw);
                String stack = sw.toString();
                Log.e("GlobalCrash", stack);

                File logFile = new File(getFilesDir(), "last_crash.txt");
                FileWriter writer = new FileWriter(logFile, false);
                writer.write(stack);
                writer.flush();
                writer.close();
            } catch (Exception ignored) { }
        });
    }
}


