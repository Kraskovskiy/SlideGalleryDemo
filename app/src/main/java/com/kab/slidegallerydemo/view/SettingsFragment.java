package com.kab.slidegallerydemo.view;

import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import com.kab.slidegallerydemo.MainActivity;
import com.kab.slidegallerydemo.R;
import com.kab.slidegallerydemo.model.Data;



/**
 * Created by Suxx on 04.06.2016.
 */
public class SettingsFragment extends Fragment implements View.OnClickListener{
    Spinner mSpinner;
    Data mData;
    CheckBox mChAutoSlide;
    CheckBox mChFavorites;
    CheckBox mChShuffle;
    EditText mEtTime;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, null);
        mSpinner = (Spinner)view.findViewById(R.id.spinner);
        mData = new Data(getActivity().getApplicationContext());

        mEtTime = (EditText) view.findViewById(R.id.etTime);
        mEtTime.setText(String.valueOf(mData.getTimeSlide()));
        mEtTime.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {}
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!mEtTime.getText().toString().equals("")) {
                    mData.setTimeSlide(Integer.decode(mEtTime.getText().toString()));
                }
            }
        });

        mChAutoSlide = (CheckBox) view.findViewById(R.id.chAutoSlide);
        mChAutoSlide.setOnClickListener(this);
        mChAutoSlide.setChecked(mData.getAutoSlide());

        mChFavorites = (CheckBox) view.findViewById(R.id.chFavorites);
        mChFavorites.setOnClickListener(this);
        mChFavorites.setChecked(mData.getOnlyFavorites());

        mChShuffle = (CheckBox) view.findViewById(R.id.chShuffle);
        mChShuffle.setOnClickListener(this);
        mChShuffle.setChecked(mData.getShuffle());

        createSpinner();
        return view;
    }

    public void createSpinner() {
        String[] lang = getResources().getStringArray(R.array.typeOfTransition);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),R.layout.spinner_item,lang);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);

        mSpinner.setAdapter(spinnerArrayAdapter);
        mSpinner.setSelection(mData.getTypeOfTransition());
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapter, View view,
                                       int position, long id) {
                if (mData.getTypeOfTransition() != position) {
                    mData.setTypeOfTransition(position);
                    refreshPager();
                } else {
                    mData.setTypeOfTransition(position);
                }
            }
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
    }

    public void refreshPager() {
        if (Data.isJsonLoad) {
            ((MainActivity) getActivity()).createPagerNew();
        }
    }

    public void startSlide() {
        if (mChAutoSlide.isChecked()) {
            ((MainActivity) getActivity()).mChronometer.start();
        } else {
            ((MainActivity) getActivity()).mChronometer.stop();
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.chAutoSlide:
                mData.setAutoSlide(mChAutoSlide.isChecked());
                startSlide();
                break;
            case R.id.chFavorites:
                mData.setOnlyFavorites(mChFavorites.isChecked());
                getActivity().finish();
                startActivity(getActivity().getIntent());
                break;
            case R.id.chShuffle:
                mData.setShuffle(mChShuffle.isChecked());
                //refreshPager();
                getActivity().finish();
                startActivity(getActivity().getIntent());
                break;
        }
    }
}