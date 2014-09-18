package com.fourello.icare.fragments;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.fourello.icare.DashboardParentFragmentActivity;
import com.fourello.icare.R;
import com.fourello.icare.adapters.ParentImmunizationListAdapter;
import com.fourello.icare.adapters.ParentMedicationListAdapter;
import com.fourello.icare.datas.MedsAndVaccines;
import com.fourello.icare.datas.PatientChildData;
import com.fourello.icare.datas.PatientDatabase;
import com.fourello.icare.view.CustomEditTextView;
import com.fourello.icare.view.CustomTextView;
import com.fourello.icare.widgets.ParseProxyObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import java.util.ArrayList;

public class ParentImmunizationTrackerFragment extends Fragment implements
        DashboardParentFragmentActivity.OpenMenuCallbacks,
        View.OnClickListener{

    // TODO: Rename and change types of parameters
    private ParseProxyObject mParamLoginData;
    private ArrayList<PatientChildData> mParamChildData;
    private int mParamChildDataPosition;

    private ViewGroup myFragmentView;

    private ProgressDialog mProgressDialog;

    private static int pos = 0;
    private ParentImmunizationListAdapter immunizationTrackerListAdapter;


    public static Fragment newInstance(int position, PatientDatabase patientData, ParseProxyObject loginData, String patientObjectId) {
        ParentImmunizationTrackerFragment f = new ParentImmunizationTrackerFragment();

//        myChild = patientData;
//        mParamLoginData = loginData;
//        mParamPatientObjectId = patientObjectId;

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParamLoginData = (ParseProxyObject) getArguments().getSerializable(DashboardParentFragmentActivity.ARG_LOGIN_DATA);
            mParamChildData = getArguments().getParcelableArrayList(DashboardParentFragmentActivity.ARG_CHILD_DATA);
            mParamChildDataPosition = getArguments().getInt(DashboardParentFragmentActivity.ARG_CHILD_DATA_POS);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        myFragmentView = (ViewGroup) inflater.inflate(R.layout.fragment_parent_immunization_tracker, container, false);

        ((DashboardParentFragmentActivity)getActivity()).changePageTitle("My Tracker");

        // Set up the Parse query to use in the adapter
        ParseQueryAdapter.QueryFactory<MedsAndVaccines> factory = new ParseQueryAdapter.QueryFactory<MedsAndVaccines>() {
            public ParseQuery<MedsAndVaccines> create() {
                ParseQuery<MedsAndVaccines> query = MedsAndVaccines.getQuery();
                query.whereMatches("type", "1");
                query.addDescendingOrder("vaccine_datetaken");
                query.fromLocalDatastore();
                return query;
            }
        };

        // Set up the adapter
        inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        immunizationTrackerListAdapter = new ParentImmunizationListAdapter(getActivity(), factory);

        // Attach the query adapter to the view
        ListView immunizationTracker = (ListView) myFragmentView.findViewById(R.id.listImmunizationTracker);
        immunizationTracker.setAdapter(immunizationTrackerListAdapter);

        immunizationTracker.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                MedsAndVaccines medsAndVaccines = immunizationTrackerListAdapter.getItem(position);
//                showDialogMedication(medsAndVaccines);
            }
        });
        int width = getActivity().getResources().getDisplayMetrics().widthPixels;
        int columnWidth = width/3;

        CustomTextView txtHeaderVaccine = (CustomTextView) myFragmentView.findViewById(R.id.txtHeaderVaccine);
        CustomTextView txtHeaderDose = (CustomTextView) myFragmentView.findViewById(R.id.txtHeaderDose);
        CustomTextView txtHeaderDateTaken = (CustomTextView) myFragmentView.findViewById(R.id.txtHeaderDateTaken);

        txtHeaderVaccine.setWidth(columnWidth);
        txtHeaderDose.setWidth(columnWidth);
        txtHeaderDateTaken.setWidth(columnWidth);

        txtHeaderVaccine.setGravity(Gravity.CENTER);
        txtHeaderDose.setGravity(Gravity.CENTER);
        txtHeaderDateTaken.setGravity(Gravity.CENTER);

        return myFragmentView;
    }



    public void onResume() {
        super.onResume();
    }

    @Override
    public void onMenuPressedCallback() {
        ((DashboardParentFragmentActivity) getActivity()).showMenuContents(getView());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSaveInformation:
//                SaveInformation();
                break;
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void showDialogMedication(MedsAndVaccines medsAndVaccines) {
        final Dialog dialog = new Dialog(getActivity(), R.style.DialogSlideAnimZoom);

        dialog.setContentView(R.layout.dialog_medicine_tracker);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);



        CustomEditTextView etBrandName = (CustomEditTextView) dialog.findViewById(R.id.etBrandName);
        CustomEditTextView etGenericName = (CustomEditTextView) dialog.findViewById(R.id.etGenericName);
        CustomEditTextView etPreparation = (CustomEditTextView) dialog.findViewById(R.id.etPreparation);
        CustomEditTextView etDose = (CustomEditTextView) dialog.findViewById(R.id.etDose);
        CustomEditTextView etFrequency = (CustomEditTextView) dialog.findViewById(R.id.etFrequency);
        CustomEditTextView etDuration = (CustomEditTextView) dialog.findViewById(R.id.etDuration);
        CustomEditTextView etGoal = (CustomEditTextView) dialog.findViewById(R.id.etGoal);

        etBrandName.setText(medsAndVaccines.getName());
        etGenericName.setText(medsAndVaccines.getMedsGenericName());
        etPreparation.setText(medsAndVaccines.getMedsPreparation());
        etDose.setText(medsAndVaccines.getMedsDose());
        etFrequency.setText(medsAndVaccines.getMedsFrequency());
        etDuration.setText(medsAndVaccines.getMedsDuration());
        etGoal.setText(medsAndVaccines.getMedsGoal());


        ImageButton btnCloseMenuMedicine = (ImageButton) dialog.findViewById(R.id.btnCloseMenuMedicine);
        btnCloseMenuMedicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }


}
