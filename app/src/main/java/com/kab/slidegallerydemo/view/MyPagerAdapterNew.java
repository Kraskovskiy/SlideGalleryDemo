package com.kab.slidegallerydemo.view;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.kab.slidegallerydemo.MyImageCallback;
import com.kab.slidegallerydemo.model.Data;
import com.kab.slidegallerydemo.model.Images;
import com.kab.slidegallerydemo.view.FirstFragment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Suxx on 02.06.16.
 */// FragmentStatePagerAdapter //FragmentPagerAdapter
public class MyPagerAdapterNew extends FragmentPagerAdapter {
    private static int NUM_ITEMS = Data.PAGE_COUNT;
    private Map<Integer, String> mFragmentTags;
    private FragmentManager mFragmentManager;
    MyImageCallback mCallback;
    List<Images> mList;

    public MyPagerAdapterNew(FragmentManager fragmentManager, MyImageCallback mCallback, List<Images> list) {
        super(fragmentManager);
        mFragmentManager = fragmentManager;
        mFragmentTags = new HashMap<Integer, String>();
        this.mCallback = mCallback;
        this.mList = list;
    }

    // Returns total number of pages
    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    public void setNumItems(int num) {
        NUM_ITEMS = num;
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
        return FirstFragment.newInstance(mCallback,position,mList);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "Page " + position;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Object object = super.instantiateItem(container, position);
        if (object instanceof Fragment) {
            Fragment fragment = (Fragment) object;
            String tag = fragment.getTag();
            mFragmentTags.put(position, tag);
        }
        return object;
    }

    public Fragment getFragment(int position) {
        Fragment fragment = null;
        String tag = mFragmentTags.get(position);
        if (tag != null) {
            fragment = mFragmentManager.findFragmentByTag(tag);
        }
        return fragment;
    }
}