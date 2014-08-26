package com.fourello.icare.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fourello.icare.DashboardDoctorFragmentActivity;
import com.fourello.icare.ICareApplication;
import com.fourello.icare.R;
import com.fourello.icare.adapters.PatientVisitsAdapter;
import com.fourello.icare.datas.PatientCheckIn;
import com.fourello.icare.datas.PatientDatabase;
import com.fourello.icare.datas.PatientVisits;
import com.fourello.icare.datas.SpinnerItems;
import com.fourello.icare.view.CustomButton;
import com.fourello.icare.view.CustomEditTextView;
import com.fourello.icare.view.CustomTextView;
import com.fourello.icare.view.RoundedImageView;
import com.fourello.icare.widgets.FragmentUtils;
import com.fourello.icare.widgets.ParseProxyObject;
import com.fourello.icare.widgets.PasswordDialogFragment;
import com.fourello.icare.widgets.Utils;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PatientVisitsFragment extends ListFragment {
    public static final String ARG_LOGIN_DATA = "loginData";
    private static  PatientDatabase mParamPatientData;
    private static ParseProxyObject mParamLoginData;
    private static String mParamPatientObjectId;




    private static ViewGroup myFragmentView;

    // TODO: Rename and change types of parameters

    private byte[] mParamMyPicture;

    PatientVisitsAdapter adapter;
    private List<PatientVisits> patientVisitsList = null;


    public static Fragment newInstance(int position, PatientDatabase patientData, ParseProxyObject loginData, String patientObjectId) {
        PatientVisitsFragment f = new PatientVisitsFragment();

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

//        patientVisitsList = new ArrayList<PatientVisits>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        myFragmentView = (ViewGroup) inflater.inflate(R.layout.fragment_patient_visits, container, false);

//        patientVisitsList = mParamPatientData.getVisits();
//
//        Log.d("SIZE", patientVisitsList.get(0).getVisitDate());

//        Log.d("SIZE", patientVisitsList.size()+"");
//        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(ICareApplication.VISITS_LABEL);
//        query.whereEqualTo("patientid", mParamPatientObjectId);
//        query.addDescendingOrder("createdAt");
//        query.findInBackground(new FindCallback<ParseObject>() {
//            @Override
//            public void done(List<ParseObject> parseObjects, ParseException e) {
//                DateFormat df = new SimpleDateFormat("MMMM dd, yyyy");
//                if (e == null) {
//                    for (int i = 0; i < parseObjects.size(); i++) {
//                        ParseObject patientDatabaseObject = parseObjects.get(i);
//
//                        PatientVisits map = new PatientVisits();
//
//                        map.setPatientObjectId(patientDatabaseObject.getObjectId());
//                        Date visitDateParse = patientDatabaseObject.getCreatedAt();
//                        String visitDate = df.format(visitDateParse);
//                        map.setVisitDate(visitDate);
//
//                        if(patientVisitsList.size() == 0) {
//                            patientVisitsList.add(map);
//                        } else {
//                            if(!patientVisitsList.contains(map)) {
//                                patientVisitsList.add(map);
//                            }
//                        }
//                    }

//                    getListView().setOnItemClickListener(PatientVisitsFragment.this);

//                }
//            }
//        });

        patientVisitsList = new ArrayList<PatientVisits>();
        patientVisitsList.clear();
            /* Adding the Visits */
        ParseQuery<ParseObject> queryVisits = new ParseQuery<ParseObject>(ICareApplication.VISITS_LABEL);
        queryVisits.setSkip(0);
        queryVisits.whereEqualTo("patientid", mParamPatientObjectId);
        queryVisits.addDescendingOrder("createdAt");
        queryVisits.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjectsVisits, ParseException e) {
                Log.d("HUGHES", "DONE");
                DateFormat df = new SimpleDateFormat("MMMM dd, yyyy");
                if (e == null) {
                    if (parseObjectsVisits.size() > 0) {
//                        patientVisitsList = new ArrayList<PatientVisits>();
                        for (int i = 0; i < parseObjectsVisits.size(); i++) {
                            ParseObject patientDatabaseObject = parseObjectsVisits.get(i);

                            PatientVisits visitMap = new PatientVisits();

                            visitMap.setPatientObjectId(patientDatabaseObject.getObjectId());
                            Date visitDateParse = patientDatabaseObject.getCreatedAt();
                            String visitDate = df.format(visitDateParse);
                            visitMap.setVisitDate(visitDate);

                            Log.d(ICareApplication.VISITS_LABEL + "  VISIT Found", visitDate);

//                            if(!patientVisitsList.contains(visitMap)) {
                                patientVisitsList.add(visitMap);
//                            }
                        }
//                            if(patientVisitsList != null) {
//                                mParamPatientData.setVisits(patientVisitsList);
//                                mParamPatientDataComplete.set(mParamPatientDataPosition, mParamPatientData);
//                            }

                    } else {
                        Log.d("HUGHES", "NO DATA FOUND");
//                        mProgressDialog.dismiss();
                    }
                } else {
                    Log.d("HUGHES", e.getLocalizedMessage());
                }
//                mProgressDialog.dismiss();
            }
        });
        adapter = new PatientVisitsAdapter(getActivity(), patientVisitsList);
        setListAdapter(adapter);
        return myFragmentView;
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        FragmentUtils.startActivityForResultWhileSavingOrigin(this, requestCode, intent, null);
    }

}
