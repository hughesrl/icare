package com.fourello.icare.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.parse.GetCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import com.parse.ParseException;
import java.util.ArrayList;

public class AddUserFragment extends Fragment implements
        PasswordDialogFragment.PasswordDialogListener,
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
    public static AddUserFragment newInstance(String param1, String param2) {
        AddUserFragment fragment = new AddUserFragment();
        Bundle args = new Bundle();
        args.putString(ARG_LOGIN_DATA, param1);
        fragment.setArguments(args);
        return fragment;
    }
    public AddUserFragment() {
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
        myFragmentView = inflater.inflate(R.layout.fragment_add_user, container, false);

        ((DashboardDoctorFragmentActivity)getActivity()).changePageTitle(getString(R.string.title_my_patients));


        Spinner customSpinner = (Spinner) myFragmentView.findViewById(R.id.spinnerRelationship);
        CustomAdapter adapter = new CustomAdapter(getActivity(), android.R.layout.simple_spinner_item, populateRelatioship());
        customSpinner.setAdapter(adapter);

        CustomButton btnAddUser = (CustomButton)myFragmentView.findViewById(R.id.btnAddUser);
        btnAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((DashboardDoctorFragmentActivity)getActivity()).showPasswordDialog(AddUserFragment.this, PasswordDialogFragment.PASSWORD_DIALOG_CUSTOM);
            }
        });
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
    }



    private ArrayList<SpinnerItems> populateRelatioship(){
        final ArrayList<SpinnerItems> spinnerItems = new ArrayList<SpinnerItems>();
        spinnerItems.add(new SpinnerItems("Relationship to Patient", false));
        spinnerItems.add(new SpinnerItems("Mother", true));
        spinnerItems.add(new SpinnerItems("Father", true));
        return spinnerItems;
    }

    // Through the Fragment.onAttach () of the callback, the instance of DialogFragment can receive the realization method of NoticeDialogFragment.NoticeDialogListener is used to define the reference
    @Override
    public void onDialogPositiveClick(final DialogFragment passwordDialog, String password, String purposeToOpen) {
        // Active user touch button dialog

        if(purposeToOpen.equals(PasswordDialogFragment.PASSWORD_DIALOG_MENU)) {
            passwordDialog.dismiss();
            if (password.trim().equals(mParamLoginData.getString("password"))) {
                ((DashboardDoctorFragmentActivity) getActivity()).showMenuContents(getView());
            } else { // incorrect Password
                Toast.makeText(getActivity(), "Incorrect Password", Toast.LENGTH_LONG).show();
            }
        } else {
            //Toast.makeText(getActivity(), "Checking Password : " + password, Toast.LENGTH_LONG).show();

//            Spinner customSpinner = (Spinner) myFragmentView.findViewById(R.id.spinnerRelationship);
//            CustomAdapter adapter = new CustomAdapter(getActivity(), android.R.layout.simple_spinner_item, populateReindeer());
//            customSpinner.setAdapter(adapter);

            final CustomEditTextView etFirstname = (CustomEditTextView) myFragmentView.findViewById(R.id.etFirstname);
            final CustomEditTextView etLastname = (CustomEditTextView) myFragmentView.findViewById(R.id.etLastname);
            final Spinner spinnerRelationship = (Spinner) myFragmentView.findViewById(R.id.spinnerRelationship);
            final CustomEditTextView etContactNumber = (CustomEditTextView) myFragmentView.findViewById(R.id.etContactNumber);
            final CustomEditTextView etEmail = (CustomEditTextView) myFragmentView.findViewById(R.id.etEmail);
            final CustomEditTextView etPassword = (CustomEditTextView) myFragmentView.findViewById(R.id.etPassword);
            final CustomEditTextView etConfirmPassword = (CustomEditTextView) myFragmentView.findViewById(R.id.etConfirmPassword);

            if (password.trim().equals(mParamLoginData.getString("password"))) {
                if (mProgressDialog == null) {
                    mProgressDialog = Utils.createProgressDialog(getActivity());
                    mProgressDialog.show();
                } else {
                    mProgressDialog.show();
                }
                final String strRelationship = spinnerRelationship.getSelectedItem().toString();

                final ParseObject newUser = new ParseObject(ICareApplication.USERS_LABEL);
                newUser.put("firstname", etFirstname.getText().toString());
                newUser.put("lastname", etLastname.getText().toString());
                newUser.put("email", etEmail.getText().toString());
                newUser.put("username", etEmail.getText().toString());
                newUser.put("password", etConfirmPassword.getText().toString());
                newUser.put("role", strRelationship);

                newUser.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        //progress.dismiss();
                        if (e == null) {
                            newUserRecordObjectId = (String) newUser.get("objectId");

                            final ParseObject initialPatientData = new ParseObject(ICareApplication.PATIENTS_LABEL);
                            initialPatientData.put("doctorid", mParamLoginData.getString("linked_doctorid").toString());
                            initialPatientData.put("email", etEmail.getText().toString());
                            initialPatientData.put("username", etFirstname.getText().toString());
                            initialPatientData.put("password", etFirstname.getText().toString());
                            if (strRelationship.equalsIgnoreCase("mother")) {
                                initialPatientData.put("mothercontactnumber", etContactNumber.getText().toString());
                                initialPatientData.put("motheremail", etEmail.getText().toString());
                                initialPatientData.put("motherfirstname", etFirstname.getText().toString());
                                initialPatientData.put("motherlastname", etLastname.getText().toString());
                            } else if (strRelationship.equalsIgnoreCase("father")) {
                                initialPatientData.put("fathercontactnumber", etContactNumber.getText().toString());
                                initialPatientData.put("fatheremail", etEmail.getText().toString());
                                initialPatientData.put("fatherfirstname", etFirstname.getText().toString());
                                initialPatientData.put("fatherlastname", etLastname.getText().toString());
                            } else {
                                initialPatientData.put("mothercontactnumber", etContactNumber.getText().toString());
                                initialPatientData.put("motheremail", etEmail.getText().toString());
                                initialPatientData.put("motherfirstname", etFirstname.getText().toString());
                                initialPatientData.put("motherlastname", etLastname.getText().toString());
                            }
                            passwordDialog.dismiss();
                            initialPatientData.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e == null) {
                                        mProgressDialog.dismiss();
                                        newPatientRecordObjectId = initialPatientData.getObjectId();
                                        ((DashboardDoctorFragmentActivity) getActivity()).ParentEmailValidate(newUserRecordObjectId, newPatientRecordObjectId);
                                    }
                                }
                            });
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
        ((DashboardDoctorFragmentActivity)getActivity()).showPasswordDialog(AddUserFragment.this, PasswordDialogFragment.PASSWORD_DIALOG_MENU);
    }
}
