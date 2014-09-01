package com.fourello.icare.fragments;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.fourello.icare.DashboardDoctorFragmentActivity;
import com.fourello.icare.ICareApplication;
import com.fourello.icare.R;
import com.fourello.icare.adapters.PatientDatabaseAdapter;
import com.fourello.icare.adapters.TabsPagerAdapter;
import com.fourello.icare.datas.PatientDatabase;
import com.fourello.icare.datas.PatientVisits;
import com.fourello.icare.datas.SpinnerItems;
import com.fourello.icare.widgets.ParseProxyObject;
import com.fourello.icare.widgets.Utils;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Todo IMPORTANT
 * Patient Database Listing and Functionalities
 */

public class PatientDatabaseActionsFragment extends Fragment implements
        DashboardDoctorFragmentActivity.OpenMenuCallbacks{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String ARG_IS_IN_CLINIC_SURVEY = "isInClinicSurvey";
    public static final String ARG_LOGIN_DATA = "loginData";
    public static final String ARG_MY_PICTURE = "myPicture";
    public static final String ARG_PATIENT_OBJECT_ID = "patientObjectId";
    public static final String ARG_PATIENT_DATA = "patientData";
    public static final String ARG_PATIENT_DATA_COMPLETE = "patientDataComplete";
    public static final String ARG_PATIENT_DATA_POSITION = "patientDataPosition";

    // TODO: Rename and change types of parameters
    private ParseProxyObject mParamLoginData;
    private byte[] mParamMyPicture;
    private String mParamPatientObjectId;
    private int mParamPatientDataPosition;
    private PatientDatabase mParamPatientData;
    private ArrayList<PatientDatabase> mParamPatientDataComplete;

    private ListView listView;

    private String contactnumber = "";

    private Boolean isInClinic;

    private final Handler handler = new Handler();
    private PagerSlidingTabStrip tabs;
    private ViewPager pager;
//    private ViewPager mViewPager;
    private MyPagerAdapter pagerAdapter;
    private List<PatientDatabase> patientDatabaselist;
    private List<PatientVisits> patientVisitsList = null;
    private ProgressDialog mProgressDialog;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SchedulerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PatientDatabaseActionsFragment newInstance(String param1, String param2) {
        PatientDatabaseActionsFragment fragment = new PatientDatabaseActionsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_LOGIN_DATA, param1);
        args.putString(ARG_MY_PICTURE, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public PatientDatabaseActionsFragment() {
        // Required empty public constructor
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParamLoginData = (ParseProxyObject) getArguments().getSerializable(ARG_LOGIN_DATA);
            mParamMyPicture = getArguments().getByteArray(ARG_MY_PICTURE);

            mParamPatientObjectId = getArguments().getString(ARG_PATIENT_OBJECT_ID);

            mParamPatientData = getArguments().getParcelable(ARG_PATIENT_DATA);
            mParamPatientDataComplete = getArguments().getParcelableArrayList(ARG_PATIENT_DATA_COMPLETE);
            mParamPatientDataPosition = getArguments().getInt(ARG_PATIENT_DATA_POSITION);

//            if (mProgressDialog == null) {
//                mProgressDialog = Utils.createProgressDialog(getActivity());
//                mProgressDialog.show();
//            } else {
//                mProgressDialog.show();
//            }

//            patientDatabaselist = new ArrayList<PatientDatabase>();
//            /* Adding the Visits */
//            ParseQuery<ParseObject> queryVisits = new ParseQuery<ParseObject>(ICareApplication.VISITS_LABEL);
//            queryVisits.setSkip(0);
//            queryVisits.whereEqualTo("patientid", mParamPatientObjectId);
//            queryVisits.addDescendingOrder("createdAt");
//            queryVisits.findInBackground(new FindCallback<ParseObject>() {
//                @Override
//                public void done(List<ParseObject> parseObjectsVisits, ParseException e) {
//                    Log.d("HUGHES", "DONE");
//                    DateFormat df = new SimpleDateFormat("MMMM dd, yyyy");
//                    if (e == null) {
//                        if (parseObjectsVisits.size() > 0) {
//                            patientVisitsList = new ArrayList<PatientVisits>();
//                            for (int i = 0; i < parseObjectsVisits.size(); i++) {
//                                ParseObject patientDatabaseObject = parseObjectsVisits.get(i);
//
//                                PatientVisits visitMap = new PatientVisits();
//
//                                visitMap.setPatientObjectId(patientDatabaseObject.getObjectId());
//                                Date visitDateParse = patientDatabaseObject.getCreatedAt();
//                                String visitDate = df.format(visitDateParse);
//                                visitMap.setVisitDate(visitDate);
//
//                                Log.d(ICareApplication.VISITS_LABEL + "  Found", visitDate);
//
//                                if(!patientVisitsList.contains(visitMap)) {
//                                    patientVisitsList.add(visitMap);
//                                }
//                            }
////                            if(patientVisitsList != null) {
////                                mParamPatientData.setVisits(patientVisitsList);
////                                mParamPatientDataComplete.set(mParamPatientDataPosition, mParamPatientData);
////                            }
//
//                        } else {
//                            Log.d("HUGHES", "NO DATA FOUND");
//                            mProgressDialog.dismiss();
//                        }
//                    } else {
//                        Log.d("HUGHES", e.getLocalizedMessage());
//                    }
//                    mProgressDialog.dismiss();
//                }
//            });
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        ViewGroup myFragmentView = (ViewGroup) inflater.inflate(R.layout.fragment_patient_database_actions, container, false);

        ((DashboardDoctorFragmentActivity)getActivity()).changePageTitle(getString(R.string.title_my_patients));

        // The content view embeds two fragments; now retrieve them and attach
        // their "hide" button.
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        tabs = (PagerSlidingTabStrip) myFragmentView.findViewById(R.id.tabs);
        pager = (ViewPager) myFragmentView.findViewById(R.id.pager);
        pagerAdapter = new MyPagerAdapter(getChildFragmentManager());
        pager.setAdapter(pagerAdapter);
        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
                .getDisplayMetrics());
        pager.setPageMargin(pageMargin);
        tabs.setViewPager(pager);
        if(mParamPatientData.getpIsInClinic() > 0) {
            pager.setCurrentItem(1);
        }

        return myFragmentView;
    }
    public class MyPagerAdapter extends FragmentPagerAdapter {
        private final String[] TITLES = { "Check In", "Patient Information", "Visits" };

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }
        @Override
        public int getCount() {
            return TITLES.length;
        }
        @Override
        public Fragment getItem(int position) {
            Fragment f = new Fragment();
            Bundle args = getArguments();
            switch(position){
                case 0: // Check In
                    CheckInFragment checkInFragment = new CheckInFragment();
                    args.putInt(ARG_IS_IN_CLINIC_SURVEY, mParamPatientData.getpIsInClinic());
                    args.putByteArray(ARG_MY_PICTURE, mParamPatientData.getPatientphoto());
                    checkInFragment.setArguments(args);

                    return checkInFragment;
                case 1: // Patient Information
                    PatientInformationFragment patientInformationFragment = new PatientInformationFragment();

                    patientInformationFragment.setArguments(args);
                    return patientInformationFragment;

                case 2: // Patient Visit Calendar
                    PatientVisitsFragment patientVisitsFragment = new PatientVisitsFragment();
//                    args.putByteArray();
//                    args.putParcelableArrayList(PatientDatabaseActionsFragment.ARG_PATIENT_DATA, patientVisitsList);
                    patientVisitsFragment.setArguments(args);
                    return patientVisitsFragment;
            }
        return null;
        }
    }


    @Override
    public void onMenuPressedCallback() {
        ((DashboardDoctorFragmentActivity) getActivity()).showMenuContents(getView());
    }

}
