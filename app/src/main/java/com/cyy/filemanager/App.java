package com.cyy.filemanager;

import android.app.Application;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.io.IOException;

/**
 * Created by study on 16/12/26.
 *
 */

public class App extends Application {

    public static App app;

    @Override
    public void onCreate() {
        super.onCreate();

        try {
            Runtime.getRuntime().exec("su");
        } catch (IOException e) {
            e.printStackTrace();
        }

        app = this;

    }

    public static App getApp(){
        return app;
    }
}
