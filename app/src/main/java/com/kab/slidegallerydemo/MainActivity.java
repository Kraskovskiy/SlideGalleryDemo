package com.kab.slidegallerydemo;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Chronometer;
import android.widget.Toast;

import com.kab.slidegallerydemo.model.CustomComparator;
import com.kab.slidegallerydemo.model.Data;
import com.kab.slidegallerydemo.model.Images;
import com.kab.slidegallerydemo.model.JsonAsyncLoader;
import com.kab.slidegallerydemo.view.MyPagerAdapterNew;
import com.kab.slidegallerydemo.view.SettingsFragment;

import java.util.Collections;
import java.util.List;


public class MainActivity extends AppCompatActivity implements MyImageCallback, ViewPager.OnPageChangeListener, JsonLoadCompleted {
    ViewPager mViewPager;
    MyPagerAdapterNew mAdapterViewPager;
    Context mContext;
    Fragment mSettingsFragment;
    FragmentTransaction fTrans;
    Data mData;
    public Chronometer mChronometer;
    private static final String TAG = "MainActivityGallery";
    public FloatingActionButton fab,fab1,fab2,fab3;
    public Animation fab_open,
            fab_open1,
            fab_open2,
            fab_open3,
            fab_close,
            fab_close1,
            fab_close2,
            fab_close3,
            rotate_forward,
            rotate_backward;
    public static Boolean isFabOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = getApplicationContext();
        mData = new Data(mContext);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setOverflowIcon(getResources().getDrawable(R.drawable.settings));
        setSupportActionBar(toolbar);

        mData.getFromFavorites();
        mSettingsFragment = new SettingsFragment();
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mChronometer = (Chronometer) findViewById(R.id.ch2);

        new JsonAsyncLoader(getApplicationContext(), this).execute();

        createCronometer();
        createFab();
    }

    public void animateFabShow() {
        startAnimation(rotate_backward, 0);
        fab1.startAnimation(fab_close1);
        fab2.startAnimation(fab_close2);
        fab3.startAnimation(fab_close3);
        fab1.setClickable(false);
        fab2.setClickable(false);
        fab3.setClickable(false);
        isFabOpen = false;
    }
    public void animateFabHide(){
        startAnimation(rotate_forward,270);
        fab1.startAnimation(fab_open1);
        fab2.startAnimation(fab_open2);
        fab3.startAnimation(fab_open3);
        fab1.setClickable(true);
        fab2.setClickable(true);
        fab3.setClickable(true);
        isFabOpen = true;
    }


    public void animateFAB(){
        if(isFabOpen){
            animateFabShow();
        } else {
            animateFabHide();
        }
    }

    public void createFab() {
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateFAB();

            }
        });

        fab1 = (FloatingActionButton)findViewById(R.id.fab1);
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickFab1();
            }
        });

        fab2 = (FloatingActionButton)findViewById(R.id.fab2);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickFab2();
            }
        });

        fab3 = (FloatingActionButton)findViewById(R.id.fab3);
        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickFab3();
            }
        });

        fab_open1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open1);
        fab_open2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open2);
        fab_open3 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open3);

        fab_close1 = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_close1);
        fab_close2 = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_close2);
        fab_close3 = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_close3);

        rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_backward);
    }

    public void onClickFab1() {
        mData.setOnlyFavorites(!mData.getOnlyFavorites());
        finish();
        startActivity(getIntent());
    }

    public void onClickFab2(){
        mData.setAutoSlide(!mData.getAutoSlide());
        if (mData.getAutoSlide()) {
            mChronometer.start();
        }
        else {
            mChronometer.stop();
        }
    }

    public void onClickFab3() {
        mData.setShuffle(!mData.getShuffle());
        finish();
        startActivity(getIntent());
    }

    public void startAnimation(Animation animation, float deg) {
        if (Build.VERSION.SDK_INT >= 21) {
            fab.startAnimation(animation);
        } else {
            fab.animate().rotation(deg).setDuration(200).setInterpolator(new LinearInterpolator());
        }
    }

    public void createCronometer() {
        mChronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                long elapsedMillis = SystemClock.elapsedRealtime()
                        - mChronometer.getBase();

                if (elapsedMillis > mData.getTimeSlide() * 1000) {
                    mChronometer.setBase(SystemClock.elapsedRealtime());
                    if (mViewPager.getCurrentItem() < Data.PAGE_COUNT - 1) {
                        mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
                    } else {
                        mViewPager.setCurrentItem(0);
                    }
                }
            }
        });

        if (mData.getAutoSlide()) {
            mChronometer.start();
        }
        else {
            mChronometer.stop();
        }

    }

    public void showSetting() {
        if (checkCurrentFragment() != 1) {
            fTrans = getFragmentManager().beginTransaction();
            fTrans.replace(R.id.fl, mSettingsFragment, "SettingsFragment");
            fTrans.addToBackStack(null);
            fTrans.commit();
        }
        else {
            hideSettings();
        }
    }

    public void hideSettings() {
        if ( getFragmentManager().getBackStackEntryCount() > 0) {
            for (int i = 0; i < getFragmentManager().getBackStackEntryCount(); i++) {
                getFragmentManager().popBackStackImmediate();
            }
        }
        fTrans = getFragmentManager().beginTransaction();
        fTrans.disallowAddToBackStack();
        fTrans.remove(mSettingsFragment);
        fTrans.commit();
    }

    @Override
    public void onBackPressed() {
        if (checkCurrentFragment() == 1) {
            hideSettings();
        }
        else {
            finish();
        }

    }

    public void createPagerNew() {
        mViewPager.setPageTransformer(true, new ReaderViewPagerTransformer(getTypeofTransform(mData.getTypeOfTransition())));

        if (mData.getOnlyFavorites() && Data.mImagesListFaforites != null && Data.mImagesListFaforites.size() > 0) {
            Data.PAGE_COUNT = Data.mImagesListFaforites.size();
        } else {
            Data.PAGE_COUNT = Data.mImagesList.size();
        }

        mAdapterViewPager = new MyPagerAdapterNew(getFragmentManager(), this, getOrderType(getFaforites()));

        if (mData.getOnlyFavorites() && Data.mImagesListFaforites != null && Data.mImagesListFaforites.size() > 0) {
            mAdapterViewPager.setNumItems(Data.mImagesListFaforites.size());
        } else {
            mAdapterViewPager.setNumItems(Data.mImagesList.size());
        }
        mViewPager.setAdapter(mAdapterViewPager);
        mViewPager.addOnPageChangeListener(this);
    }


    public List<Images> getFaforites() {
        if (mData.getOnlyFavorites() && Data.mImagesListFaforites != null && Data.mImagesListFaforites.size() > 0) {
            Data.PAGE_COUNT = Data.mImagesListFaforites.size();
            return Data.mImagesListFaforites;
        } else {
            if (mData.getOnlyFavorites()) {
                Toast.makeText(MainActivity.this, R.string.favoritesError, Toast.LENGTH_SHORT).show();
            }
            Data.PAGE_COUNT = Data.mImagesList.size();
            return Data.mImagesList;
        }
    }


    public List<Images> getOrderType(List<Images> list) {
        if (mData.getShuffle()) {
            Collections.shuffle(list);
            return list;
        } else {
            Collections.sort(list, new CustomComparator());
            return list;
        }
    }

    public int checkCurrentFragment() {
        Fragment myFragment1 = getFragmentManager().findFragmentByTag("SettingsFragment");
        if (myFragment1 != null && myFragment1.isVisible()) {
            return 1;
        }
        return 0;
    }

    public ReaderViewPagerTransformer.TransformType getTypeofTransform(int value) {
        switch (value) {
            case 0:
                return ReaderViewPagerTransformer.TransformType.FLOW;
            case 1:
                return ReaderViewPagerTransformer.TransformType.DEPTH;
            case 2:
                return ReaderViewPagerTransformer.TransformType.ZOOM;
            case 3:
                return ReaderViewPagerTransformer.TransformType.SLIDE_OVER;
            default:
                return ReaderViewPagerTransformer.TransformType.FLOW;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        showSetting();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            showSetting();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void callbackImageResponse() {
        Log.e(TAG, "callbackImageResponse: ");
        refresh(mViewPager.getCurrentItem());
    }

    @Override
    public void callbackImageError(String err) {
        Log.d("callbackImageError", err);
    }

    @Override
    public void callbackImageLoad(int position) {
        Log.d("callbackImageLoad", "callbackImageLoad");
    }

    public void refresh(final int position) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //execute code on main thread
                Fragment fragment = mAdapterViewPager.getFragment(position);
                if (fragment != null){
                    fragment.onResume();
                }
            }
        });
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        Log.e(TAG, "onPageSelected" + position);
        Fragment fragment = mAdapterViewPager.getFragment(position);
        if (fragment != null) {
            fragment.onResume();
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void JsonLoadCompleted() {
        createPagerNew();
        Data.isJsonLoad = true;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (isFabOpen) {
            isFabOpen = false;
        }
    }
}
