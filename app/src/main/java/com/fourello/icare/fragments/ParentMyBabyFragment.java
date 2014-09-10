package com.fourello.icare.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fourello.icare.BTGridPager.BTFragmentGridPager;
import com.fourello.icare.ICareApplication;
import com.fourello.icare.R;
import com.fourello.icare.datas.MyChildren;
import com.fourello.icare.view.CustomTextView;
import com.fourello.icare.view.HoloCircleSeekBar;
import com.fourello.icare.widgets.ParseProxyObject;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
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
 * {@link ParentMyBabyFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ParentMyBabyFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class ParentMyBabyFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private ParseProxyObject mParamLoginData;
    private ArrayList<MyChildren> mParamChildData;
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
    public static ParentMyBabyFragment newInstance(String param1, String param2) {
        ParentMyBabyFragment fragment = new ParentMyBabyFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public ParentMyBabyFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParamLoginData = (ParseProxyObject) getArguments().getSerializable(ParentDashboardFragment.ARG_LOGIN_DATA);
            mParamChildData = getArguments().getParcelableArrayList(ParentDashboardFragment.ARG_CHILD_DATA);
            mParamChildDataPosition = getArguments().getInt(ParentDashboardFragment.ARG_CHILD_DATA_POS);
            mParamMyPicture = getArguments().getByteArray(ParentDashboardFragment.ARG_MY_PICTURE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myFragmentView = (ViewGroup) inflater.inflate(R.layout.fragment_parent_my_baby, container, false);


        final HoloCircleSeekBar weightSeekBar = (HoloCircleSeekBar) myFragmentView.findViewById(R.id.weightSeekBar);
        final HoloCircleSeekBar heightSeekBar = (HoloCircleSeekBar) myFragmentView.findViewById(R.id.heightSeekBar);
        final HoloCircleSeekBar headSeekBar = (HoloCircleSeekBar) myFragmentView.findViewById(R.id.headSeekBar);
        final HoloCircleSeekBar nextVisitSeekBar = (HoloCircleSeekBar) myFragmentView.findViewById(R.id.nextVisiSeekBar);
        final CustomTextView lblNextVisitDate = (CustomTextView) myFragmentView.findViewById(R.id.lblNextVisitDate);

        weightSeekBar.setIsEnable(false);
        heightSeekBar.setIsEnable(false);
        headSeekBar.setIsEnable(false);
        nextVisitSeekBar.setIsEnable(false);

        ParseQuery<ParseObject> queryVisits = new ParseQuery<ParseObject>(ICareApplication.VISITS_LABEL);
        queryVisits.setSkip(0);
        queryVisits.whereEqualTo("patientid", mParamChildData.get(mParamChildDataPosition).getPatientObjectId());
        queryVisits.addDescendingOrder("updatedAt");
        queryVisits.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if(e == null) {

                    weightSeekBar.setMaxPosition(Integer.parseInt(parseObject.getString("weight"))); // position starts from zero
                    weightSeekBar.setInitPosition(Integer.parseInt(parseObject.getString("weight"))); // position starts from zero

                    heightSeekBar.setMaxPosition(Integer.parseInt(parseObject.getString("height")));
                    heightSeekBar.setInitPosition(Integer.parseInt(parseObject.getString("height"))); // position starts from zero

                    headSeekBar.setMaxPosition(Integer.parseInt(parseObject.getString("head")));
                    headSeekBar.setInitPosition(Integer.parseInt(parseObject.getString("head"))); // position starts from zero

                    DateFormat df = new SimpleDateFormat("MMMM dd, yyyy");
                    int totalDays  = getDaysDifference(parseObject.getCreatedAt(), parseObject.getDate("nextvisit"));
                    int remainingDays = getDaysDifference(new Date(), parseObject.getDate("nextvisit"));

                    nextVisitSeekBar.setMaxPosition(totalDays);
                    nextVisitSeekBar.setInitPositionCountdown(totalDays-remainingDays); // position starts from zero

                    String nextVisitDate = df.format(parseObject.getDate("nextvisit"));
                    lblNextVisitDate.setText(nextVisitDate);

                    arrayMedications = parseObject.getJSONArray("medications");

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
                    Log.d("ROBERT", e.toString());
                }
            }
        });



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
