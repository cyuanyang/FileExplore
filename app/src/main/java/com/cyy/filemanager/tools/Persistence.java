package com.cyy.filemanager.tools;

import android.content.Context;

import com.cyy.filemanager.App;

/**
 * Created by study on 17/1/5.
 *
 */

public class Persistence {

    private final static String name = "file_explore";

    public final static String kHideFile = "Persistence.kHideFile";

    public static void insertBoolean(String key , boolean b){
        App.getApp().getSharedPreferences(name , Context.MODE_PRIVATE).edit().putBoolean(key , b).apply();
    }
    public static boolean getBoolean(String key , boolean defaultValue){
        return App.getApp().getSharedPreferences(name , Context.MODE_PRIVATE).getBoolean(key , defaultValue);
    }

}
