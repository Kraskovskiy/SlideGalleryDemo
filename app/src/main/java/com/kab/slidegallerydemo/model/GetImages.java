package com.kab.slidegallerydemo.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.kab.slidegallerydemo.MyImageCallback;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Suxx on 01.06.16.
 */
public class GetImages {
    MyImageCallback mCallback;
    String mId;
    BitmapCache mBitmapCache;
    static Bitmap mBitmap;

    public GetImages(MyImageCallback callback) {
        this.mCallback = callback;
    }

    public GetImages(MyImageCallback callback,String id) {
        this.mCallback = callback;
        this.mId = id;
        mBitmapCache = new BitmapCache();
    }

    public void getImages(String url) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("GetImagesonFailure", e.toString());
                mCallback.callbackImageError(e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    mBitmap = BitmapFactory.decodeStream(response.body().byteStream());
                    mBitmapCache.addBitmapToMemoryCache(mId,mBitmap);
                    mCallback.callbackImageResponse();
                } catch (OutOfMemoryError e) {
                    mCallback.callbackImageError(e.toString());
                    mBitmapCache.clearBitmapToMemoryCache();
                    Log.e("GetImage","OutOfMemoryError"+e.toString());
                }
                Log.e("GetImagesonResponse",response.message());
            }
        });
    }
}
