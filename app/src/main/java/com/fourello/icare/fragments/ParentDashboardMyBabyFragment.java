package com.fourello.icare.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.fourello.icare.BTGridPager.BTFragmentGridPager;
import com.fourello.icare.DashboardParentFragmentActivity;
import com.fourello.icare.R;
import com.fourello.icare.datas.PatientChildData;
import com.fourello.icare.datas.Visits;
import com.fourello.icare.view.CustomTextView;
import com.fourello.icare.view.HoloCircleSeekBar;
import com.fourello.icare.widgets.ParseProxyObject;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ParentDashboardMyBabyFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ParentDashboardMyBabyFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class ParentDashboardMyBabyFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private ParseProxyObject mParamLoginData;
    private ArrayList<PatientChildData> mParamChildData;
    private int mParamChildDataPosition;
    private byte[] mParamMyPicture;
    private String mParam1;
    private String mParam2;

    private ViewGroup myFragmentView;
    private OnFragmentInteractionListener mListener;

    private BTFragmentGridPager.FragmentGridPagerAdapter mFragmentGridPagerAdapter;
    private JSONArray arrayMedications;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyDashboardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ParentDashboardMyBabyFragment newInstance(String param1, String param2) {
        ParentDashboardMyBabyFragment fragment = new ParentDashboardMyBabyFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public ParentDashboardMyBabyFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParamLoginData = (ParseProxyObject) getArguments().getSerializable(DashboardParentFragmentActivity.ARG_LOGIN_DATA);
            mParamChildData = getArguments().getParcelableArrayList(DashboardParentFragmentActivity.ARG_CHILD_DATA);
            mParamChildDataPosition = getArguments().getInt(DashboardParentFragmentActivity.ARG_CHILD_DATA_POS);
            mParamMyPicture = getArguments().getByteArray(DashboardParentFragmentActivity.ARG_MY_PICTURE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myFragmentView = (ViewGroup) inflater.inflate(R.layout.fragment_parent_dashboard_my_baby, container, false);


        final HoloCircleSeekBar weightSeekBar = (HoloCircleSeekBar) myFragmentView.findViewById(R.id.weightSeekBar);
        final HoloCircleSeekBar heightSeekBar = (HoloCircleSeekBar) myFragmentView.findViewById(R.id.heightSeekBar);
        final HoloCircleSeekBar headSeekBar = (HoloCircleSeekBar) myFragmentView.findViewById(R.id.headSeekBar);
        final HoloCircleSeekBar nextVisitSeekBar = (HoloCircleSeekBar) myFragmentView.findViewById(R.id.nextVisiSeekBar);
        final CustomTextView lblNextVisitDate = (CustomTextView) myFragmentView.findViewById(R.id.lblNextVisitDate);

        weightSeekBar.setIsEnable(false);
        heightSeekBar.setIsEnable(false);
        headSeekBar.setIsEnable(false);
        nextVisitSeekBar.setIsEnable(false);

        ParseQuery<Visits> queryVisits = Visits.getQuery();
        queryVisits.fromLocalDatastore();
        queryVisits.addDescendingOrder("updatedAt");
        try {
            if(queryVisits.count() == 0) {
                LinearLayout nodata = (LinearLayout) myFragmentView.findViewById(R.id.nodata);
                nodata.setVisibility(View.VISIBLE);

                HorizontalScrollView visitrackerdata = (HorizontalScrollView) myFragmentView.findViewById(R.id.visitrackerdata);
                visitrackerdata.setVisibility(View.GONE);
            } else {
                queryVisits.getFirstInBackground(new GetCallback<Visits>() {
                    @Override
                    public void done(Visits parseObject, ParseException e) {
                        DateFormat df = new SimpleDateFormat("MMMM dd, yyyy");
                        if(e == null) {
                            if(parseObject.has("nextvisit")) {
                                int totalDays = getDaysDifference(parseObject.getCreatedAt(), parseObject.getNextVisit());
                                int remainingDays = getDaysDifference(new Date(), parseObject.getNextVisit());
                                if(remainingDays > 0) {

                                    nextVisitSeekBar.setMaxPosition(totalDays);
                                    nextVisitSeekBar.setInitPositionCountdown(totalDays - remainingDays); // position starts from zero

                                    String nextVisitDate = df.format(parseObject.getNextVisit());
                                    lblNextVisitDate.setText(nextVisitDate);

                                    if (parseObject.has("weight")) {
                                        weightSeekBar.setMaxPosition(Integer.parseInt(parseObject.getWeight())); // position starts from zero
                                        weightSeekBar.setInitPosition(Integer.parseInt(parseObject.getWeight())); // position starts from zero
                                    }

                                    if (parseObject.has("height")) {
                                        heightSeekBar.setMaxPosition(Integer.parseInt(parseObject.getHeight()));
                                        heightSeekBar.setInitPosition(Integer.parseInt(parseObject.getHeight())); // position starts from zero
                                    }

                                    if (parseObject.has("head")) {
                                        headSeekBar.setMaxPosition(Integer.parseInt(parseObject.getHead()));
                                        headSeekBar.setInitPosition(Integer.parseInt(parseObject.getHead())); // position starts from zero
                                    }


                                    if (parseObject.has("medications")) {
                                        arrayMedications = parseObject.getMedications();
                                        if (arrayMedications != null) {
                                            final BTFragmentGridPager mFragmentGridPager = (BTFragmentGridPager) myFragmentView.findViewById(R.id.fragmentGridPager);
                                            mFragmentGridPagerAdapter = new BTFragmentGridPager.FragmentGridPagerAdapter() {
                                                @Override
                                                public int rowCount() {
                                                    return arrayMedications.length();
                                                }

                                                @Override
                                                public int columnCount(int row) {
                                                    return arrayMedications.length();
                                                }

                                                @Override
                                                public Fragment getItem(BTFragmentGridPager.GridIndex index) {
                                                    ParentMyBabyMedicinesFragment panelFrag1 = new ParentMyBabyMedicinesFragment();
                                                    panelFrag1.setGridIndex(index);
                                                    try {
                                                        JSONObject jsonObjectMedications = arrayMedications.getJSONObject(index.getRow());
                                                        panelFrag1.setMedName(jsonObjectMedications.getString("brandname"));
                                                        panelFrag1.setAdditionalData(jsonObjectMedications.getString("frequency"));
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }

                                                    return panelFrag1;
                                                }
                                            };
                                            mFragmentGridPager.setGridPagerAdapter(mFragmentGridPagerAdapter);
                                        } else {
                                            LinearLayout medicineLayout = (LinearLayout) myFragmentView.findViewById(R.id.medicineLayout);
                                            medicineLayout.setVisibility(View.GONE);
                                        }
                                    }
                                } else {
                                    LinearLayout nodata = (LinearLayout) myFragmentView.findViewById(R.id.nodata);
                                    nodata.setVisibility(View.VISIBLE);

                                    HorizontalScrollView visitrackerdata = (HorizontalScrollView) myFragmentView.findViewById(R.id.visitrackerdata);
                                    visitrackerdata.setVisibility(View.GONE);
                                }
                            } else {
                                LinearLayout nodata = (LinearLayout) myFragmentView.findViewById(R.id.nodata);
                                nodata.setVisibility(View.VISIBLE);

                                HorizontalScrollView visitrackerdata = (HorizontalScrollView) myFragmentView.findViewById(R.id.visitrackerdata);
                                visitrackerdata.setVisibility(View.GONE);
                            }
                        } else {
                            Log.d("ROBERT", e.toString());
                        }
                    }
                });
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return myFragmentView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

//    @Override
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//        try {
//            mListener = (OnFragmentInteractionListener) activity;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }


    public static int getDaysDifference(Date fromDate, Date toDate) {
        if(fromDate==null||toDate==null)
            return 0;

        return (int)( (toDate.getTime() - fromDate.getTime()) / (1000 * 60 * 60 * 24));
    }
}
