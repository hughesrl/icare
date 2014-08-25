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
import com.fourello.icare.R;
import com.fourello.icare.adapters.PatientDatabaseAdapter;
import com.fourello.icare.adapters.TabsPagerAdapter;
import com.fourello.icare.datas.PatientDatabase;
import com.fourello.icare.datas.SpinnerItems;
import com.fourello.icare.widgets.ParseProxyObject;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Todo IMPORTANT
 * Patient Database Listing and Functionalities
 */

public class PatientDatabaseActionsFragment extends Fragment implements
        DashboardDoctorFragmentActivity.OpenMenuCallbacks{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String ARG_LOGIN_DATA = "loginData";
    public static final String ARG_MY_PICTURE = "myPicture";
    public static final String ARG_PATIENT_OBJECT_ID = "patientObjectId";
    public static final String ARG_PATIENT_DATA = "patientData";

    // TODO: Rename and change types of parameters
    private ParseProxyObject mParamLoginData;
    private byte[] mParamMyPicture;
    private String mParamPatientObjectId;
    private PatientDatabase mParamPatientData;

    private ListView listView;

    private String contactnumber = "";


    private final Handler handler = new Handler();
    private PagerSlidingTabStrip tabs;
    private ViewPager pager;
//    private ViewPager mViewPager;
    private MyPagerAdapter pagerAdapter;

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


        return myFragmentView;
    }
    public class MyPagerAdapter extends FragmentPagerAdapter {
        private final String[] TITLES = { "Check In", "Patient Information", "Patient Calendar" };

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
                    args.putByteArray(ARG_MY_PICTURE, mParamPatientData.getPatientphoto());
                    checkInFragment.setArguments(args);

                    return checkInFragment;
                case 1: // Patient Information
                    PatientInformationFragment patientInformationFragment = new PatientInformationFragment();
//                    args.putByteArray(CheckInFragment.ARG_MY_PICTURE, mParamPatientData.getPatientphoto());

                    patientInformationFragment.setArguments(args);
                    return patientInformationFragment;

                case 2: // Patient Visit Calendar
                    return f;
            }
        return null;
        }
    }


    @Override
    public void onMenuPressedCallback() {
        ((DashboardDoctorFragmentActivity) getActivity()).showMenuContents(getView());
    }

}
