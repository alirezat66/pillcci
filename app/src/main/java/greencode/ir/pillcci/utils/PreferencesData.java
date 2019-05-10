package greencode.ir.pillcci.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import greencode.ir.pillcci.controler.AppController;


public class PreferencesData {

    public static void saveString( String key, String value) {
        SharedPreferences sharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(AppController.getContext());
        sharedPrefs.edit().putString(key, value).commit();
    }
    public static void saveString( String key, String value,Context context) {
        SharedPreferences sharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        sharedPrefs.edit().putString(key, value).commit();
    }
    public static void saveFloat(String key, Float value) {
        Context context = AppController.getContext();
        SharedPreferences sharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        sharedPrefs.edit().putFloat(key, value).commit();
    }
    public static void saveInt(String key, int value) {
        SharedPreferences sharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(AppController.getContext());
        sharedPrefs.edit().putInt(key, value).commit();
    }
    public static void saveLong(String key, long value) {

        SharedPreferences sharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(AppController.getContext());
        sharedPrefs.edit().putLong(key, value).apply();
    }


    public static void saveBoolean(String key, boolean value) {
        SharedPreferences sharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(AppController.getContext());
        sharedPrefs.edit().putBoolean(key, value).commit();
    }
    public static boolean saveBool( String key, boolean value){
        SharedPreferences sharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(AppController.getContext());
       return sharedPrefs.edit().putBoolean(key, value).commit();
    }
    public static boolean saveBool( String key, boolean value,Context context){
        SharedPreferences sharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(context);
       return sharedPrefs.edit().putBoolean(key, value).commit();
    }
    public static int getInt(String key, int defaultValue) {
        SharedPreferences sharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(AppController.getContext());
        return sharedPrefs.getInt(key, defaultValue);
    }
    public static int getInt(Context context,String key, int defaultValue) {
        SharedPreferences sharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        return sharedPrefs.getInt(key, defaultValue);
    }
    public static int getInt(String key) {
        SharedPreferences sharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(AppController.getContext());
        return sharedPrefs.getInt(key, 0);
    }
    public static String getString( String key, String defaultValue) {
        SharedPreferences sharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(AppController.getContext());
        return sharedPrefs.getString(key, defaultValue);
    }
    public static String getString( String key) {

        SharedPreferences sharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(AppController.getContext());
        return sharedPrefs.getString(key, "");
    }
 public static String getString( String key,Context context) {
        SharedPreferences sharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        return sharedPrefs.getString(key, "");
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        SharedPreferences sharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(AppController.getContext());
        return sharedPrefs.getBoolean(key, defaultValue);
    }
    public static boolean getBoolean( String key) {

            SharedPreferences sharedPrefs = PreferenceManager
                    .getDefaultSharedPreferences(AppController.getContext());
            return sharedPrefs.getBoolean(key, false);


    }
    public static boolean getBoolean( String key,Context context) {

            SharedPreferences sharedPrefs = PreferenceManager
                    .getDefaultSharedPreferences(context);
            return sharedPrefs.getBoolean(key, false);


    }


    public static long getLong(String key) {
        SharedPreferences sharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(AppController.getContext());
        return sharedPrefs.getLong(key, 0);
    }
}
