package com.fourello.icare.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fourello.icare.BTGridPager.BTFragmentGridPager;
import com.fourello.icare.R;
import com.fourello.icare.view.CustomTextView;
import com.fourello.icare.view.HoloCircleSeekBar;

public class ParentMyBabyMedicinesFragment extends Fragment  {
    TextView mLabel;
    BTFragmentGridPager.GridIndex mGridIndex;
    String medName;
    String additionalData;
    String dose;
    private ViewGroup myFragmentView;

    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        // Inflate the layout for this fragment
        myFragmentView = (ViewGroup) inflater.inflate(R.layout.list_parent_my_baby_medicines_fragment, container, false);

        HoloCircleSeekBar heightSeekBar = (HoloCircleSeekBar) myFragmentView.findViewById(R.id.heightSeekBar);
        heightSeekBar.setMaxPosition(60);
        heightSeekBar.setInitPosition(45); // position starts from zero
        heightSeekBar.setIsEnable(false);

        CustomTextView lblMedicineName = (CustomTextView) myFragmentView.findViewById(R.id.lblMedicineName);
        lblMedicineName.setText(medName);

        CustomTextView lblAdditionalData = (CustomTextView) myFragmentView.findViewById(R.id.lblAdditionalData);
        lblAdditionalData.setText(additionalData);

        return myFragmentView;
    }
    public void setTxtRow(BTFragmentGridPager.GridIndex gridIndex) {
        mLabel.setText("(" + gridIndex.getRow() + ", " + gridIndex.getCol() + ")");
    }
    public void setGridIndex(BTFragmentGridPager.GridIndex gridIndex){
        mGridIndex = gridIndex;
    }
    public void setMedName(String medName){
        this.medName = medName;
    }

    public void setAdditionalData(String additionalData){
        this.additionalData = additionalData;
    }
}
