package com.kab.slidegallerydemo.view;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kab.slidegallerydemo.model.BitmapCache;
import com.kab.slidegallerydemo.MyImageCallback;
import com.kab.slidegallerydemo.R;
import com.kab.slidegallerydemo.model.Data;
import com.kab.slidegallerydemo.model.GetImages;
import com.kab.slidegallerydemo.model.Images;

import java.util.List;

/**
 * Created by Suxx on 02.06.16.
 */
public class FirstFragment extends Fragment {
    Context mContext;
    ImageView mImageView;
    ImageView mIvFavorites;
    TextView mTvPage;
    EditText mEtFavoritesText;
    public static MyImageCallback mCallback;
    ProgressBar mPBar;
    int mPageNumber;
    static final String ARGUMENT_PAGE_NUMBER = "arg_page_number";
    static List<Images> mList;
    BitmapCache mBitmapCache;
    GetImages mGetImages;

    public static FirstFragment newInstance(MyImageCallback callback, int page, List<Images> list) {
        mList = list;
        mCallback = callback;
        FirstFragment firstFragment = new FirstFragment();
        Bundle arguments = new Bundle();
        arguments.putInt(ARGUMENT_PAGE_NUMBER, page);
        firstFragment.setArguments(arguments);
        return firstFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPageNumber = getArguments().getInt(ARGUMENT_PAGE_NUMBER);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_page, container, false);
        mBitmapCache = new BitmapCache();
        mEtFavoritesText = (EditText) view.findViewById(R.id.etFavoritesText);
        mTvPage = (TextView) view.findViewById(R.id.tvPage);
        mImageView = (ImageView) view.findViewById(R.id.ivPage);

        mIvFavorites = (ImageView) view.findViewById(R.id.ivFavorites);
        mIvFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Data data = new Data(getActivity().getApplicationContext());
                if (!imageIsFavorites()) {
                    if (mEtFavoritesText.getVisibility() == View.INVISIBLE) {
                        mEtFavoritesText.setVisibility(View.VISIBLE);

                    } else {
                        mEtFavoritesText.setVisibility(View.INVISIBLE);
                        mTvPage.setText(mEtFavoritesText.getText().toString());
                        Data.mFaforitsListWithText.put(mList.get(mPageNumber).getId(), mEtFavoritesText.getText().toString());
                        data.saveFavorites(Data.mFaforitsListWithText);
                        Data.mImagesListFaforites.add(mList.get(mPageNumber));
                        setFavoritesTextColor();
                    }
                } else {
                    Data.mFaforitsListWithText.remove(mList.get(mPageNumber).getId());
                    Data.mImagesListFaforites.remove(mList.get(mPageNumber));
                    data.saveFavorites(Data.mFaforitsListWithText);
                    if (data.getOnlyFavorites()) {
                        getActivity().finish();
                        startActivity(getActivity().getIntent());
                    }
                    setFavoritesTextColor();
                }
            }
        });

        mPBar = (ProgressBar) view.findViewById(R.id.pBar);


        if (mBitmapCache.getBitmapFromMemCache(mList.get(mPageNumber).getId()) == null) {
            if (mGetImages == null) {
                mGetImages = new GetImages(mCallback, mList.get(mPageNumber).getId());
            }
            mGetImages.getImages(mList.get(mPageNumber).getUrl());
        } else {
            mImageView.setImageBitmap(mBitmapCache.getBitmapFromMemCache(mList.get(mPageNumber).getId()));
            Log.e("FirstFragment", "getBitmapFromMemCache");
        }
        setFavoritesTextColor();
        return view;
    }

    public Boolean imageIsFavorites() {
        if (Data.mFaforitsListWithText!=null&&Data.mFaforitsListWithText.size()>0&&Data.mFaforitsListWithText.containsKey(mList.get(mPageNumber).getId())) {
            return true;
        } else {
            return false;
        }
    }

    public void setFavoritesTextColor() {
        if (imageIsFavorites()) {
            mTvPage.setText(Data.mFaforitsListWithText.get(mList.get(mPageNumber).getId()));
            mIvFavorites.setImageDrawable(getResources().getDrawable(R.drawable.favourite_orange_));
        }
        else {
            mTvPage.setText("");
            mIvFavorites.setImageDrawable(getResources().getDrawable(R.drawable.favourite_grey_));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
            if (mBitmapCache.getBitmapFromMemCache(mList.get(mPageNumber).getId()) != null) {
                mImageView.setImageBitmap(mBitmapCache.getBitmapFromMemCache(mList.get(mPageNumber).getId()));
                mPBar.setVisibility(View.INVISIBLE);
            } else {
                //enable for load if Cashe is null
               /* if (mGetImages == null) {
                    mGetImages = new GetImages(mCallback, mList.get(mPageNumber).getId());
                    Log.e("FirstFragment", "onCreateView: mGetImages == null");
                }
                mGetImages.getImages(mList.get(mPageNumber).getUrl());*/
            }
    }

    @Override
    public void onDestroyView () {
        super.onDestroyView();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

}