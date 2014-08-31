package com.fourello.icare.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.fourello.icare.ICareApplication;
import com.fourello.icare.R;
import com.fourello.icare.datas.PatientDatabase;
import com.fourello.icare.datas.Patients;
import com.fourello.icare.datas.SpinnerItems;
import com.fourello.icare.view.CustomEditTextView;
import com.fourello.icare.view.RoundedAvatarDrawable;
import com.fourello.icare.widgets.ParseProxyObject;
import com.fourello.icare.widgets.Utils;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.SaveCallback;

import java.util.ArrayList;

public class PatientInformationFragment extends Fragment {
    private static  PatientDatabase mParamPatientData;
    private static ParseProxyObject mParamLoginData;
    private static String mParamPatientObjectId;

    private ViewGroup myFragmentView;
    // TODO: Rename and change types of parameters

    private byte[] mParamMyPicture;


    private ProgressDialog mProgressDialog;
    //keep track of camera capture intent
    private final int CAMERA_CAPTURE = 1;
    private byte[] bytearray;

    ImageView imgPatientPhoto;

    CustomEditTextView etPatientsFName, etPatientsMName, etPatientsLName,
            etIWasBornDate,etBornAt,
            etIWasDeliveredBy,
            etWhenIWasBornIWeighed, etAndIMeasured, etGrowthTrackerHead, etGrowthTrackerChest, etGrowthTrackerAbdomen,
            etCircumcisedOn, etEarsPiercedOn, etDistinguishingMarks,
            etNewBornScreening, etVaccinationsGiven,
            etMomFName, etMomMName, etMomLName, etSheWorksAt, etSheWorksAtAs, etHerHMOIs,
            etDadFName, etDadMName, etDadLName, etHeWorksAt, etHeWorksAtAs, etHisHMOIs,
            etILiveAtAddress1, etILiveAtAddress2;

    Spinner spinnerMyMomGaveBirthToMeThrough, spinnerAllergyRisk;

    private Patients patients;
    public static Fragment newInstance(int position, PatientDatabase patientData, ParseProxyObject loginData, String patientObjectId) {
        PatientInformationFragment f = new PatientInformationFragment();

        mParamPatientData = patientData;
        mParamLoginData = loginData;
        mParamPatientObjectId = patientObjectId;

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParamLoginData = (ParseProxyObject) getArguments().getSerializable(PatientDatabaseActionsFragment.ARG_LOGIN_DATA);
            mParamMyPicture = getArguments().getByteArray(PatientDatabaseActionsFragment.ARG_MY_PICTURE);
            mParamPatientObjectId = getArguments().getString(PatientDatabaseActionsFragment.ARG_PATIENT_OBJECT_ID);
            mParamPatientData = getArguments().getParcelable(PatientDatabaseActionsFragment.ARG_PATIENT_DATA);
        }

        patients = new Patients();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        ViewGroup myFragmentView = (ViewGroup) inflater.inflate(R.layout.fragment_patient_information, container, false);

        imgPatientPhoto         = (ImageView)myFragmentView.findViewById(R.id.patient_photo);
        etPatientsFName         = (CustomEditTextView)myFragmentView.findViewById(R.id.etPatientsFName);
        etPatientsMName         = (CustomEditTextView)myFragmentView.findViewById(R.id.etPatientsMName);
        etPatientsLName         = (CustomEditTextView)myFragmentView.findViewById(R.id.etPatientsLName);
        etIWasBornDate          = (CustomEditTextView)myFragmentView.findViewById(R.id.etIWasBornDate);
        etBornAt                = (CustomEditTextView)myFragmentView.findViewById(R.id.etBornAt);
        etIWasDeliveredBy       = (CustomEditTextView)myFragmentView.findViewById(R.id.etIWasDeliveredBy);
        etWhenIWasBornIWeighed  = (CustomEditTextView)myFragmentView.findViewById(R.id.etWhenIWasBornIWeighed);
        etAndIMeasured          = (CustomEditTextView)myFragmentView.findViewById(R.id.etAndIMeasured);
        etGrowthTrackerHead     = (CustomEditTextView)myFragmentView.findViewById(R.id.etGrowthTrackerHead);
        etGrowthTrackerChest    = (CustomEditTextView)myFragmentView.findViewById(R.id.etGrowthTrackerChest);
        etGrowthTrackerAbdomen  = (CustomEditTextView)myFragmentView.findViewById(R.id.etGrowthTrackerAbdomen);
        etCircumcisedOn         = (CustomEditTextView)myFragmentView.findViewById(R.id.etCircumcisedOn);
        etEarsPiercedOn         = (CustomEditTextView)myFragmentView.findViewById(R.id.etEarsPiercedOn);
        etDistinguishingMarks   = (CustomEditTextView)myFragmentView.findViewById(R.id.etDistinguishingMarks);
        etNewBornScreening      = (CustomEditTextView)myFragmentView.findViewById(R.id.etNewBornScreening);
        etVaccinationsGiven     = (CustomEditTextView)myFragmentView.findViewById(R.id.etVaccinationsGiven);
        etMomFName              = (CustomEditTextView)myFragmentView.findViewById(R.id.etMomFName);
        etMomMName              = (CustomEditTextView)myFragmentView.findViewById(R.id.etMomMName);
        etMomLName              = (CustomEditTextView)myFragmentView.findViewById(R.id.etMomLName);
        etSheWorksAt            = (CustomEditTextView)myFragmentView.findViewById(R.id.etSheWorksAt);
        etSheWorksAtAs          = (CustomEditTextView)myFragmentView.findViewById(R.id.etSheWorksAtAs);
        etHerHMOIs              = (CustomEditTextView)myFragmentView.findViewById(R.id.etHerHMOIs);
        etDadFName              = (CustomEditTextView)myFragmentView.findViewById(R.id.etDadFName);
        etDadMName              = (CustomEditTextView)myFragmentView.findViewById(R.id.etDadMName);
        etDadLName              = (CustomEditTextView)myFragmentView.findViewById(R.id.etDadLName);
        etHeWorksAt             = (CustomEditTextView)myFragmentView.findViewById(R.id.etHeWorksAt);
        etHeWorksAtAs           = (CustomEditTextView)myFragmentView.findViewById(R.id.etHeWorksAtAs);
        etHisHMOIs              = (CustomEditTextView)myFragmentView.findViewById(R.id.etHisHMOIs);
        etILiveAtAddress1       = (CustomEditTextView)myFragmentView.findViewById(R.id.etILiveAtAddress1);
        etILiveAtAddress2       = (CustomEditTextView)myFragmentView.findViewById(R.id.etILiveAtAddress2);

        spinnerMyMomGaveBirthToMeThrough = (Spinner) myFragmentView.findViewById(R.id.spinnerMyMomGaveBirthToMeThrough);
        CustomAdapter adapterMyMomGaveBirthToMeThrough = new CustomAdapter(getActivity(), android.R.layout.simple_spinner_item, ICareApplication.populateTypeOfDelivery());
        spinnerMyMomGaveBirthToMeThrough.setAdapter(adapterMyMomGaveBirthToMeThrough);

        spinnerAllergyRisk = (Spinner) myFragmentView.findViewById(R.id.spinnerAllergyRisk);
        CustomAdapter adapterAllergyRisk = new CustomAdapter(getActivity(), android.R.layout.simple_spinner_item, ICareApplication.populateAllergyRisk());
        spinnerAllergyRisk.setAdapter(adapterAllergyRisk);
//         Log.d("mParamPatientData.getPatientphoto().length", ""+mParamPatientData.getPatientphoto().length);
        if(mParamMyPicture!=null) {
            Bitmap bMap = BitmapFactory.decodeByteArray(mParamMyPicture, 0, mParamMyPicture.length);
            imgPatientPhoto.setBackground(Utils.resizedBitmapDisplay(getActivity(), bMap));
        }

        etPatientsFName.setText(mParamPatientData.getFirtname());
        etPatientsMName.setText(mParamPatientData.getMiddlename());
        etPatientsLName.setText(mParamPatientData.getLastname());

        etIWasBornDate.setText(mParamPatientData.getbDate());
        etBornAt.setText(mParamPatientData.getbPlace());
        etIWasDeliveredBy.setText(mParamPatientData.getDrName());

        // Set Selection Delivery Type
        int spinnerPosition = adapterMyMomGaveBirthToMeThrough.getPosition(mParamPatientData.getDeliveryType());
        spinnerMyMomGaveBirthToMeThrough.setSelection(spinnerPosition);

        etWhenIWasBornIWeighed.setText(mParamPatientData.getpWeight());
        etAndIMeasured.setText(mParamPatientData.getpHeight());
        etGrowthTrackerHead.setText(mParamPatientData.getpHead());
        etGrowthTrackerChest.setText(mParamPatientData.getpChest());
        etGrowthTrackerAbdomen.setText(mParamPatientData.getpAbdomen());

        // Set Selection Delivery Type
        int spinnerPositionAllergy = adapterAllergyRisk.getPosition(mParamPatientData.getAllergyRisk());
        spinnerAllergyRisk.setSelection(spinnerPositionAllergy);

        etCircumcisedOn.setText(mParamPatientData.getpCircumcisedOn());
        etEarsPiercedOn.setText(mParamPatientData.getpEarPiercedOn());
        etDistinguishingMarks.setText(mParamPatientData.getpDistinguishingMarks());

        etNewBornScreening.setText(mParamPatientData.getpNewbornScreening());
        etVaccinationsGiven.setText(mParamPatientData.getpVaccinationsGiven());

        etMomFName.setText(mParamPatientData.getpMomsFname());
        etMomMName.setText(mParamPatientData.getpMomsMname());
        etMomLName.setText(mParamPatientData.getpMomsLname());
        etSheWorksAt.setText(mParamPatientData.getpMomsWorkPlace());
        etSheWorksAtAs.setText(mParamPatientData.getpMomsWorkAs());
        etHerHMOIs.setText(mParamPatientData.getpMomsHMO());

        etDadFName.setText(mParamPatientData.getpDadsFname());
        etDadMName.setText(mParamPatientData.getpDadsMname());
        etDadLName.setText(mParamPatientData.getpDadsLname());
        etHeWorksAt.setText(mParamPatientData.getpDadsWorkPlace());
        etHeWorksAtAs.setText(mParamPatientData.getpDadsWorkAs());
        etHisHMOIs.setText(mParamPatientData.getpDadsHMO());

        etILiveAtAddress1.setText(mParamPatientData.getpAddress1());
        etILiveAtAddress2.setText(mParamPatientData.getpAddress2());

        return myFragmentView;
    }

    private class CustomAdapter extends ArrayAdapter<SpinnerItems> {
        private Activity context;
        ArrayList<SpinnerItems> spinnerItems;

        public CustomAdapter(Activity context, int resource, ArrayList<SpinnerItems> spinnerItems) {
            super(context, resource, spinnerItems);
            this.context = context;
            this.spinnerItems = spinnerItems;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            SpinnerItems current = spinnerItems.get(position);

            LayoutInflater inflater = getLayoutInflater(getArguments());
            View row = inflater.inflate(R.layout.spinner_row, parent, false);
            TextView name = (TextView) row.findViewById(R.id.spinnerTxtTitle);
            Typeface myTypeFace = Typeface.createFromAsset(context.getAssets(), "fonts/VAGRoundedLight.ttf");

            name.setTypeface(myTypeFace, Typeface.NORMAL);
            name.setGravity(Gravity.LEFT);
            name.setTextSize(18);
            if(current.getSpinnerStatus() == false) {
                name.setTextColor(Color.GRAY);
            } else {
                name.setTextColor(Color.BLACK);
            }

            name.setText(current.getSpinnerTitle());

            return row;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            if (row == null) {
                LayoutInflater inflater = context.getLayoutInflater();
                row = inflater.inflate(R.layout.spinner_row, parent, false);
            }
            SpinnerItems current = spinnerItems.get(position);
            TextView name = (TextView) row.findViewById(R.id.spinnerTxtTitle);
            if(current.getSpinnerStatus() == false) {
                name.setTextColor(Color.GRAY);
            } else {
                name.setTextColor(Color.BLACK);
            }
            name.setText(current.getSpinnerTitle());
            return row;
        }

        public int getPosition(String text) {
            for(int s=0;s<=(spinnerItems.size()-1);s++) {
                if(spinnerItems.get(s).getSpinnerTitle().equalsIgnoreCase(text.trim())) {
                    return s;
                }
            }
            return 0;
        }
    }

}
