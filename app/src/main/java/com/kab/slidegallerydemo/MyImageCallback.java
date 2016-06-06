package com.kab.slidegallerydemo;

import android.graphics.Bitmap;

/**
 * Created by Suxx on 01.06.16.
 */
public interface MyImageCallback {
    void callbackImageResponse();
    void callbackImageError(String err);
    void callbackImageLoad(int pagePosition);
}
