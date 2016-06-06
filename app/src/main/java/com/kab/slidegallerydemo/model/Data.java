package com.kab.slidegallerydemo.model;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Suxx on 01.06.2016.
 */
public class Data {
    public static final String APP_PREFERENCES = "mySettingsSlide";
    public static List<Images> mImagesList = new ArrayList<Images>();
    public static List<Images> mImagesListShuffle = new ArrayList<Images>();
    public static Boolean isJsonLoad = false;
    public static List<Images> mImagesListFaforites = new ArrayList<Images>();
    public static Map<String,String> mFaforitsListWithText = new HashMap<String,String>();
    public static int PAGE_COUNT = 10;
    Context mContext;

    public SharedPreferences mSettings;

    public Data(Context context) {
        this.mContext = context;
        mSettings =  context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
    }

    public void setAutoSlide(Boolean value) {
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putBoolean("AutoSlide", value).apply();
    }

    public Boolean getAutoSlide() {
        return mSettings.getBoolean("AutoSlide",false);
    }

   public void setTimeSlide(int value) {
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putInt("TimeSlide", value).apply();
    }

    public int getTimeSlide() {
        return mSettings.getInt("TimeSlide",30);
    }

    public void setTypeOfTransition(int value) {
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putInt("TypeOfTransition", value).apply();
    }

    public int getTypeOfTransition() {
        return mSettings.getInt("TypeOfTransition",0);
    }

    public void setShuffle(Boolean value) {
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putBoolean("Shuffle", value).apply();
    }

    public Boolean getShuffle() {
        return mSettings.getBoolean("Shuffle",false);
    }

    public void setOnlyFavorites(Boolean value) {
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putBoolean("OnlyFavorites", value).apply();
    }

    public Boolean getOnlyFavorites() {
        return mSettings.getBoolean("OnlyFavorites",false);
    }

    public void saveFavorites(Map<String, String> values) {
        SharedPreferences.Editor editor = mContext.getSharedPreferences("HASH_MAP_PREFERENCES", 0).edit();
        editor.clear();
        for (HashMap.Entry entry : values.entrySet())
            editor.putString(entry.getKey().toString(), entry.getValue().toString());
        editor.apply();
    }

    public Map<String,String> getFromFavorites( ){
        SharedPreferences prefs = mContext.getSharedPreferences("HASH_MAP_PREFERENCES", 0);
        mFaforitsListWithText.clear();
        for( HashMap.Entry entry : prefs.getAll().entrySet() )
            mFaforitsListWithText.put( entry.getKey().toString(), entry.getValue().toString() );
        return mFaforitsListWithText;
    }


}
