package com.kab.slidegallerydemo.model;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.kab.slidegallerydemo.model.Images;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Suxx on 01.06.2016.
 */
public class GetDataFromJSON {
    public static List<Images> loadJSONFromAssetCity(Context context) {
        try {
           return readJsonStream(context.getAssets().open("images.json"));
        } catch (Exception e) {
           return null;
        }
    }

    public static int lengthListImages(Context context) {
        try {
            return readJsonStream(context.getAssets().open("images.json")).size();
        } catch (Exception e) {
            return 10;
        }
    }

    public static List<Images> readJsonStream(InputStream in) throws IOException {
        Gson gson = new Gson();
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        List<Images> imagesList = new ArrayList<Images>();
        reader.beginArray();

        while (reader.hasNext()) {
            Images images = gson.fromJson(reader, Images.class);
            imagesList.add(images);
            Log.e("Images.json", "getId()" + images.getId() + "getIndex " + images.getIndex()+ "getUrl() " +images.getUrl());
        }
        reader.endArray();
        reader.close();
        return imagesList;
    }

}
