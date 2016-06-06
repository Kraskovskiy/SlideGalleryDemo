package com.kab.slidegallerydemo.model;

import com.kab.slidegallerydemo.model.Images;

import java.util.Comparator;

/**
 * Created by Suxx on 05.06.2016.
 */
public class CustomComparator implements Comparator<Images> {
    @Override
    public int compare(Images o1, Images o2) {
        if (o1.getIndex() > o2.getIndex()) {
            return 1;
        } else if (o1.getIndex() < o2.getIndex()) {
            return -1;
        }
        return 0;
    }
}