package com.kab.slidegallerydemo.model;

import android.content.Context;
import android.os.AsyncTask;

import com.kab.slidegallerydemo.JsonLoadCompleted;
import com.kab.slidegallerydemo.model.Data;
import com.kab.slidegallerydemo.model.GetDataFromJSON;
import com.kab.slidegallerydemo.model.Images;

import java.util.List;
import java.util.Map;

/**
 * Created by Suxx on 03.06.16.
 */
public class JsonAsyncLoader extends AsyncTask<Void, Void, Void> {

    private Context mContext;
    List<Images> mImagesList;
    JsonLoadCompleted mJsonLoadCompleted;

    public JsonAsyncLoader(Context context,JsonLoadCompleted jsonLoadCompleted) {
        this.mContext = context;
        this.mJsonLoadCompleted = jsonLoadCompleted;
     }

    @Override
    protected Void doInBackground(Void... voids) {
        this.mImagesList = GetDataFromJSON.loadJSONFromAssetCity(mContext);
        getFavorites(mImagesList);
        return null;
    }

    public void getFavorites(List<Images> list) {
        Data data = new Data(mContext);
        Map<String, String> favorites = data.getFromFavorites();
        Data.mImagesListFaforites.clear();
        if (favorites != null) {
            for (Images i : list) {
                if (favorites.containsKey(i.getId())) {
                    Data.mImagesListFaforites.add(i);
                }
            }
        }
    }

    @Override
    protected void onPostExecute(Void result) {
        Data.mImagesList = this.mImagesList;
        mJsonLoadCompleted.JsonLoadCompleted();
    }
}
