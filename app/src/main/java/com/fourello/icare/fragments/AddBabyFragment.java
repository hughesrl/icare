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
import com.fourello.icare.datas.Patients;
import com.fourello.icare.datas.SpinnerItems;
import com.fourello.icare.view.CustomButton;
import com.fourello.icare.view.CustomEditTextView;
import com.fourello.icare.view.CustomTextView;
import com.fourello.icare.widgets.ParseProxyObject;
import com.fourello.icare.widgets.PasswordDialogFragment;
import com.fourello.icare.widgets.Utils;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AddBabyFragment extends Fragment implements
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
    public static AddBabyFragment newInstance(String param1, String param2) {
        AddBabyFragment fragment = new AddBabyFragment();
        Bundle args = new Bundle();
        args.putString(ARG_LOGIN_DATA, param1);
        fragment.setArguments(args);
        return fragment;
    }
    public AddBabyFragment() {
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
        myFragmentView = inflater.inflate(R.layout.fragment_add_baby, container, false);

        ((DashboardDoctorFragmentActivity)getActivity()).changePageTitle(getString(R.string.title_my_patients));

        Spinner customSpinner = (Spinner) myFragmentView.findViewById(R.id.spinnerGender);
        CustomAdapter adapter = new CustomAdapter(getActivity(), android.R.layout.simple_spinner_item, populateGender());
        customSpinner.setAdapter(adapter);

        CustomButton btnAddBabyDone = (CustomButton)myFragmentView.findViewById(R.id.btnAddBabyDone);
        btnAddBabyDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((DashboardDoctorFragmentActivity)getActivity()).showPasswordDialog(AddBabyFragment.this, PasswordDialogFragment.PASSWORD_DIALOG_CUSTOM);
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



    private ArrayList<SpinnerItems> populateGender(){
        final ArrayList<SpinnerItems> spinnerItems = new ArrayList<SpinnerItems>();
        spinnerItems.add(new SpinnerItems("Gender", false));
        spinnerItems.add(new SpinnerItems("Male", true));
        spinnerItems.add(new SpinnerItems("Female", true));
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
            final CustomEditTextView etFirstname = (CustomEditTextView) myFragmentView.findViewById(R.id.etFirstname);
            final CustomEditTextView etMiddlename = (CustomEditTextView) myFragmentView.findViewById(R.id.etMiddlename);
            final CustomEditTextView etLastname = (CustomEditTextView) myFragmentView.findViewById(R.id.etLastname);
            final Spinner spinnerGender = (Spinner) myFragmentView.findViewById(R.id.spinnerGender);
            final CustomTextView etBirthday = (CustomTextView) myFragmentView.findViewById(R.id.spinnerBirthday);


            if (password.trim().equals(mParamLoginData.getString("password"))) {
                if (mProgressDialog == null) {
                    mProgressDialog = Utils.createProgressDialog(getActivity());
                    mProgressDialog.show();
                } else {
                    mProgressDialog.show();
                }
                final String strGender = spinnerGender.getSelectedItem().toString();

//                Log.d("mParamNewPatientRecordObjectId", mParamNewPatientRecordObjectId);
                if(mParamNewPatientRecordObjectId != null) {
                    ParseObject updatePatient = ParseObject.createWithoutData(ICareApplication.PATIENTS_LABEL, mParamNewPatientRecordObjectId);
                    updatePatient.put("firstname", etFirstname.getText().toString());
                    updatePatient.put("middlename", etMiddlename.getText().toString());
                    updatePatient.put("lastname", etLastname.getText().toString());
                    updatePatient.put("gender", strGender);

                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd, yyyy HH:mm");
                    Date convertedDate = new Date();
                    try {
                        String birthdayAndTime = etBirthday.getText().toString().trim() + " 00:00";
                        convertedDate = simpleDateFormat.parse(birthdayAndTime);
                    } catch (java.text.ParseException e) {
                        e.printStackTrace();
                    }
                    updatePatient.put("dateofbirth", convertedDate);

                    updatePatient.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            mProgressDialog.dismiss();
                            if (e == null) {
                                passwordDialog.dismiss();
                                ((DashboardDoctorFragmentActivity) getActivity()).PatientDatabase();
                            } else {
                                Log.e("ERROR", e.toString());
                            }
                        }

                    });
                } else {
                    final ParseQuery<ParseObject> query = ParseQuery.getQuery(ICareApplication.USERS_LABEL);
                    query.whereEqualTo("objectId", mParamNewUserRecordObjectId);
                    query.findInBackground(new FindCallback<ParseObject>() {
                        public void done(List<ParseObject> objects, ParseException e) {

                            final ParseObject project = objects.get(0);

                            mParamNewUserRecordObjectId = project.getObjectId();
                            Log.d("ObjectId", mParamNewUserRecordObjectId);
                            if (e == null) {
                                if (objects.size() == 0) {
                                    Toast.makeText(getActivity(), "Invalid Email", Toast.LENGTH_LONG).show();
                                } else {
                                    //final ParseObject initialPatientData = new ParseObject(ICareApplication.PATIENTS_LABEL);
                                    final Patients initialPatientData = new Patients();
//----------------------
                                    initialPatientData.put("firstname", etFirstname.getText().toString());
                                    initialPatientData.put("middlename", etMiddlename.getText().toString());
                                    initialPatientData.put("lastname", etLastname.getText().toString());
                                    initialPatientData.put("gender", strGender);

                                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd, yyyy HH:mm");
                                    Date convertedDate = new Date();
                                    try {
                                        String birthdayAndTime = etBirthday.getText().toString().trim() + " 00:00";
                                        convertedDate = simpleDateFormat.parse(birthdayAndTime);
                                    } catch (java.text.ParseException e1) {
                                        e1.printStackTrace();
                                    }
                                    initialPatientData.put("dateofbirth", convertedDate);
//----------------------

                                    initialPatientData.put("doctorid", mParamLoginData.getString("linked_doctorid").toString());
                                    initialPatientData.put("email", project.getString("email"));
                                    initialPatientData.put("username", project.getString("email"));
                                    initialPatientData.put("password", project.getString("password"));
                                    if (project.getString("role").equalsIgnoreCase("mother")) {
//                                        initialPatientData.put("mothercontactnumber", project.getString("role").getText().toString());
                                        initialPatientData.put("motheremail", project.getString("email"));
                                        initialPatientData.put("motherfirstname", project.getString("firstname"));
                                        initialPatientData.put("motherlastname", project.getString("lastname"));
                                    } else if (project.getString("role").equalsIgnoreCase("father")) {
//                                        initialPatientData.put("fathercontactnumber", etContactNumber.getText().toString());
                                        initialPatientData.put("fatheremail", project.getString("email"));
                                        initialPatientData.put("fatherfirstname", project.getString("firstname"));
                                        initialPatientData.put("fatherlastname", project.getString("lastname"));
                                    } else {
//                                        initialPatientData.put("mothercontactnumber", etContactNumber.getText().toString());
                                        initialPatientData.put("motheremail", project.getString("email"));
                                        initialPatientData.put("motherfirstname", project.getString("firstname"));
                                        initialPatientData.put("motherlastname", project.getString("lastname"));
                                    }
                                    passwordDialog.dismiss();
                                    initialPatientData.saveInBackground(new SaveCallback() {
                                        @Override
                                        public void done(ParseException e) {
                                            if (e == null) {
                                                mProgressDialog.dismiss();
                                                ((DashboardDoctorFragmentActivity) getActivity()).PatientDatabase();
                                            }
                                        }
                                    });
                                }
                            }
                        }
                    });
                }
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
        ((DashboardDoctorFragmentActivity)getActivity()).showPasswordDialog(AddBabyFragment.this, PasswordDialogFragment.PASSWORD_DIALOG_MENU);
    }
}
