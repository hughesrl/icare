package com.fourello.icare.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
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
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class ValidateEmailFragment extends Fragment implements
        PasswordDialogFragment.PasswordDialogListener,
        DashboardDoctorFragmentActivity.OpenMenuCallbacks {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_LOGIN_DATA = "loginData";
    private static final String ARG_NEW_USER_RECORD_OBJECT_ID = "newUserRecordObjectId";
    private static final String ARG_NEW_PATIENT_RECORD_OBJECT_ID = "newPatientRecordObjectId";
    private ParseProxyObject mParamLoginData;
    private String mParamNewUserRecordObjectId;
    private String mParamNewPatientRecordObjectId;
    private ProgressDialog mProgressDialog;

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
    public static ValidateEmailFragment newInstance(String param1, String param2) {
        ValidateEmailFragment fragment = new ValidateEmailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_LOGIN_DATA, param1);
        fragment.setArguments(args);
        return fragment;
    }
    public ValidateEmailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParamLoginData = (ParseProxyObject) getArguments().getSerializable(ARG_LOGIN_DATA);
            mParamNewUserRecordObjectId = getArguments().getString(ARG_NEW_USER_RECORD_OBJECT_ID);
            mParamNewPatientRecordObjectId = getArguments().getString(ARG_NEW_PATIENT_RECORD_OBJECT_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myFragmentView = inflater.inflate(R.layout.fragment_validate_email, container, false);

        ((DashboardDoctorFragmentActivity)getActivity()).changePageTitle(getString(R.string.title_my_patients));



        CustomButton btnValidateEmail = (CustomButton)myFragmentView.findViewById(R.id.btnValidateEmail);
        btnValidateEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((DashboardDoctorFragmentActivity)getActivity()).showPasswordDialog(ValidateEmailFragment.this, PasswordDialogFragment.PASSWORD_DIALOG_CUSTOM);
            }
        });
        return myFragmentView;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) { //make sure fragment codes match up {
            String editText = data.getStringExtra("password");
            Toast.makeText(getActivity(), editText, Toast.LENGTH_LONG).show();
        }
    }

    // Through the Fragment.onAttach () of the callback, the instance of DialogFragment can receive the realization method of NoticeDialogFragment.NoticeDialogListener is used to define the reference
    @Override
    public void onDialogPositiveClick(final DialogFragment passwordDialog, String password, String purposeToOpen) {
        // Active user touch button dialog

        if(purposeToOpen.equals(PasswordDialogFragment.PASSWORD_DIALOG_MENU)) {
            if (password.trim().equals(mParamLoginData.getString("password"))) {
                passwordDialog.dismiss();
                ((DashboardDoctorFragmentActivity) getActivity()).showMenuContents(getView());
            } else { // incorrect Password
                Toast.makeText(getActivity(), "Incorrect Password", Toast.LENGTH_LONG).show();
            }
        } else {
            passwordDialog.dismiss();
            if (mProgressDialog == null) {
                mProgressDialog = Utils.createProgressDialog(getActivity());
                mProgressDialog.show();
            } else {
                mProgressDialog.show();
            }

            final CustomEditTextView etEmail = (CustomEditTextView) myFragmentView.findViewById(R.id.etEmail);
            String sEmail = etEmail.getText().toString();

            if (password.trim().equals(mParamLoginData.getString("password"))) {
                mProgressDialog.show();
                final ParseQuery<ParseObject> query = ParseQuery.getQuery(ICareApplication.USERS_LABEL);
                if(mParamNewUserRecordObjectId != null) {
                    query.whereEqualTo("objectId", mParamNewUserRecordObjectId);
                }
                query.whereEqualTo("email", sEmail);
                query.findInBackground(new FindCallback<ParseObject>() {
                    public void done(List<ParseObject> objects, ParseException e) {
                        mProgressDialog.dismiss();
                        final ParseObject project = objects.get(0);

                        mParamNewUserRecordObjectId = project.getObjectId();
                        Log.d("ObjectId", mParamNewUserRecordObjectId);
                        if (e == null) {
                            if (objects.size() == 0) {
                                Toast.makeText(getActivity(), "Invalid Email", Toast.LENGTH_LONG).show();
                            } else {
                                ((DashboardDoctorFragmentActivity)getActivity()).AddBaby(mParamNewUserRecordObjectId, mParamNewPatientRecordObjectId);
                            }
                        }
                    }
                });
            } else { // incorrect Password
                Toast.makeText(getActivity(), "Incorrect Password", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        // The user touches the negative Button Dialog
        dialog.dismiss();
    }

    @Override
    public void onMenuPressedCallback() {
        ((DashboardDoctorFragmentActivity)getActivity()).showPasswordDialog(ValidateEmailFragment.this, PasswordDialogFragment.PASSWORD_DIALOG_MENU);
    }
}
