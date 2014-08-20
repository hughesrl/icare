package com.fourello.icare.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fourello.icare.DashboardDoctorFragmentActivity;
import com.fourello.icare.ICareApplication;
import com.fourello.icare.R;
import com.fourello.icare.datas.SpinnerItems;
import com.fourello.icare.view.CustomButton;
import com.fourello.icare.view.CustomEditTextView;
import com.fourello.icare.widgets.ParseProxyObject;
import com.fourello.icare.widgets.PasswordDialogFragment;
import com.fourello.icare.widgets.Utils;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import java.util.ArrayList;

public class SettingsFragment extends Fragment implements
        DashboardDoctorFragmentActivity.OpenMenuCallbacks {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private String newUserRecordObjectId;
    private String newPatientRecordObjectId;

    private static final String ARG_LOGIN_DATA = "loginData";
    private ParseProxyObject mParamLoginData;
    private ProgressDialog mProgressDialog;

    private OnFragmentInteractionListener mListener;

    private View myFragmentView;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SchedulerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_LOGIN_DATA, param1);
        fragment.setArguments(args);
        return fragment;
    }
    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParamLoginData = (ParseProxyObject) getArguments().getSerializable(ARG_LOGIN_DATA);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myFragmentView = inflater.inflate(R.layout.fragment_settings, container, false);

        ((DashboardDoctorFragmentActivity)getActivity()).changePageTitle(getString(R.string.title_settings));

        return myFragmentView;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) { //make sure fragment codes match up {
            String editText = data.getStringExtra("password");
            //Toast.makeText(getActivity(), editText, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
//        try {
//            mListener = (OnFragmentInteractionListener) activity;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString() + " must implement onViewSelected");
//        }
    }
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
        public void onFragmentInteraction(int isValid);
    }

    @Override
    public void onMenuPressedCallback() {
        ((DashboardDoctorFragmentActivity) getActivity()).showMenuContents(getView());
    }
}
