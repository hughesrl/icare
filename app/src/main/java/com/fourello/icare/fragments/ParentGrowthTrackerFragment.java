package com.fourello.icare.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TableLayout;

import com.fourello.icare.DashboardParentFragmentActivity;
import com.fourello.icare.R;
import com.fourello.icare.adapters.ParentGrowthTrackerListAdapter;
import com.fourello.icare.datas.PatientChildData;
import com.fourello.icare.datas.PatientDatabase;
import com.fourello.icare.datas.Visits;
import com.fourello.icare.view.CustomTextView;
import com.fourello.icare.widgets.ParseProxyObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import java.util.ArrayList;

public class ParentGrowthTrackerFragment extends Fragment implements
        DashboardParentFragmentActivity.OpenMenuCallbacks,
        View.OnClickListener{

    // TODO: Rename and change types of parameters
    private ParseProxyObject mParamLoginData;
    private ArrayList<PatientChildData> mParamChildData;
    private int mParamChildDataPosition;

    private ViewGroup myFragmentView;

    private ProgressDialog mProgressDialog;
    TableLayout table_header;
    TableLayout table_layout;

    private String[] titles = {"Date", "Weight","Height", "Head"};

    private static int pos = 0;
    private ParentGrowthTrackerListAdapter growthTrackerListAdapter;


    public static Fragment newInstance(int position, PatientDatabase patientData, ParseProxyObject loginData, String patientObjectId) {
        ParentGrowthTrackerFragment f = new ParentGrowthTrackerFragment();

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
        myFragmentView = (ViewGroup) inflater.inflate(R.layout.fragment_parent_growth_tracker, container, false);

        ((DashboardParentFragmentActivity)getActivity()).changePageTitle("My Tracker");

        // Set up the Parse query to use in the adapter
        ParseQueryAdapter.QueryFactory<Visits> factory = new ParseQueryAdapter.QueryFactory<Visits>() {
            public ParseQuery<Visits> create() {
                ParseQuery<Visits> query = Visits.getQuery();
                query.addDescendingOrder("updatedAt");
                query.fromLocalDatastore();
                return query;
            }
        };

        // Set up the adapter
        inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        growthTrackerListAdapter = new ParentGrowthTrackerListAdapter(getActivity(), factory);

        // Attach the query adapter to the view
        ListView growthTrackerListView = (ListView) myFragmentView.findViewById(R.id.listGrowthTracker);
        growthTrackerListView.setAdapter(growthTrackerListAdapter);

        int width = getActivity().getResources().getDisplayMetrics().widthPixels;
        int columnWidth = width/4;

        CustomTextView txtHeaderVisitDate = (CustomTextView) myFragmentView.findViewById(R.id.txtHeaderVisitDate);
        CustomTextView txtHeaderWeight = (CustomTextView) myFragmentView.findViewById(R.id.txtHeaderWeight);
        CustomTextView txtHeaderHeight = (CustomTextView) myFragmentView.findViewById(R.id.txtHeaderHeight);
        CustomTextView txtHeaderHead = (CustomTextView) myFragmentView.findViewById(R.id.txtHeaderHead);

        txtHeaderVisitDate.setWidth(columnWidth);
        txtHeaderWeight.setWidth(columnWidth);
        txtHeaderHeight.setWidth(columnWidth);
        txtHeaderHead.setWidth(columnWidth);

        txtHeaderVisitDate.setGravity(Gravity.CENTER);
        txtHeaderWeight.setGravity(Gravity.CENTER);
        txtHeaderHeight.setGravity(Gravity.CENTER);
        txtHeaderHead.setGravity(Gravity.CENTER);


        return myFragmentView;
    }



    public void onResume() {
        super.onResume();
//        table_header = (TableLayout) getActivity().findViewById(R.id.tableHeader);
//
//        int width = getActivity().getResources().getDisplayMetrics().widthPixels;
//        int columnWidth = width/4;
//        Log.d("TABLE HEAD", width+"");
//
//        TableRow rowHeader = new TableRow(getActivity());
//        rowHeader.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
//                TableLayout.LayoutParams.MATCH_PARENT));
//        // inner for loop
//        for (int head = 0; head < titles.length; head++) {
//            TextView tv = new TextView(getActivity());
//            tv.setWidth(columnWidth);
//            tv.setPadding(5, 5, 5, 5);
//            tv.setText(titles[head]);
//            tv.setGravity(Gravity.CENTER_HORIZONTAL);
//            rowHeader.addView(tv);
//        }
//        table_header.addView(rowHeader);
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


}
