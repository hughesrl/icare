package com.fourello.icare.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.fourello.icare.DashboardDoctorFragmentActivity;
import com.fourello.icare.ICareApplication;
import com.fourello.icare.R;
import com.fourello.icare.view.CustomButton;
import com.fourello.icare.view.CustomEditTextView;
import com.fourello.icare.view.CustomTextView;
import com.fourello.icare.widgets.ParseProxyObject;
import com.fourello.icare.widgets.Utils;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DoctorInformationFragment extends Fragment implements
        DashboardDoctorFragmentActivity.OpenMenuCallbacks {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private String newUserRecordObjectId;
    private String newPatientRecordObjectId;

    public static final String ARG_LOGIN_DATA = "loginData";
    public static final String ARG_SETTING_OBJECT_ID = "setting_object_id";
    private ParseProxyObject mParamLoginData;
    private ProgressDialog mProgressDialog;

    private View myFragmentView;
    ImageView imgDoctorPhoto;

    CustomEditTextView etDoctorsName, etSpecialty, etPRCLicNo, etPRTNo, etClinicsName, etClinicAddress,
            etClinicSchedule, etDoctorContactNo, etDoctorMobileNo, etClinicNotes, etSecretarysName, etSecretarysContactNo;


    private String secretaryLinkedDoctorId;
    private String settingsObjectId = "";

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SchedulerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DoctorInformationFragment newInstance(String param1, String param2) {
        DoctorInformationFragment fragment = new DoctorInformationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_LOGIN_DATA, param1);
        fragment.setArguments(args);
        return fragment;
    }
    public DoctorInformationFragment() {
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
        myFragmentView = inflater.inflate(R.layout.fragment_doctor_information, container, false);

        imgDoctorPhoto = (ImageView)myFragmentView.findViewById(R.id.doctor_photo);
        etDoctorsName = (CustomEditTextView)myFragmentView.findViewById(R.id.etDoctorsName);
        etSpecialty = (CustomEditTextView)myFragmentView.findViewById(R.id.etSpecialty);
        etPRCLicNo = (CustomEditTextView)myFragmentView.findViewById(R.id.etPRCLicNo);
        etPRTNo = (CustomEditTextView)myFragmentView.findViewById(R.id.etPRTNo);
        etClinicsName = (CustomEditTextView)myFragmentView.findViewById(R.id.etClinicsName);
        etClinicAddress = (CustomEditTextView)myFragmentView.findViewById(R.id.etClinicAddress);
        etClinicSchedule = (CustomEditTextView)myFragmentView.findViewById(R.id.etClinicSchedule);
        etDoctorContactNo = (CustomEditTextView)myFragmentView.findViewById(R.id.etDoctorContactNo);
        etDoctorMobileNo = (CustomEditTextView)myFragmentView.findViewById(R.id.etDoctorMobileNo);
        etClinicNotes = (CustomEditTextView)myFragmentView.findViewById(R.id.etClinicNotes);
        etSecretarysName = (CustomEditTextView)myFragmentView.findViewById(R.id.etSecretarysName);
        etSecretarysContactNo = (CustomEditTextView)myFragmentView.findViewById(R.id.etSecretarysContactNo);

        secretaryLinkedDoctorId = mParamLoginData.getString("linked_doctorid");

        new GetDoctorInformationDataTask(getActivity(), myFragmentView).execute();

        return myFragmentView;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) { //make sure fragment codes match up {
//            String editText = data.getStringExtra("password");
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }


    @Override
    public void onMenuPressedCallback() {
        ((DashboardDoctorFragmentActivity) getActivity()).showMenuContents(getView());
    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    @Override
    public void onResume() {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        super.onResume();
    }

    private class GetDoctorInformationDataTask extends AsyncTask<Void, Void, Void> {
        Context context;

        public GetDoctorInformationDataTask(Context context, View myFragmentView) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            if (mProgressDialog == null) {
                mProgressDialog = Utils.createProgressDialog(context);
                mProgressDialog.show();
            } else {
                mProgressDialog.show();
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            // Adding Contents from Users Class
            ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(ICareApplication.DOCTORS_LABEL);
            query.whereEqualTo("doctorID", Integer.parseInt(secretaryLinkedDoctorId));
            try {
                if(query.find().size() == 0) { // No PIN
//                    getActivity().runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            lblCheckingPIN.setVisibility(View.GONE);
//                            lblPINOfTheDay.setVisibility(View.GONE);
//                            etPINOfTheDay.setVisibility(View.GONE);
//                            btnGetPINOfTheDay.setVisibility(View.VISIBLE);
//                        }
//                    });
                } else {
                    query.getFirstInBackground(new GetCallback<ParseObject>() {
                        @Override
                        public void done(ParseObject parseObject, ParseException e) {
                            if(e == null) {
                                if(parseObject.has("photoFile")) {
                                    ParseFile myPhoto = parseObject.getParseFile("photoFile");
                                    if (myPhoto != null) {
                                        try {
                                            byte[] data = myPhoto.getData();

                                            Bitmap bMap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                            imgDoctorPhoto.setBackground(Utils.resizedBitmapDisplay(getActivity(), bMap));
                                        } catch (ParseException e2) {
                                            // TODO Auto-generated catch block
                                            e2.printStackTrace();
                                        }
                                    }
                                }
                                if(parseObject.has("firstname") && parseObject.has("lastname")) {
                                    etDoctorsName.setText(parseObject.getString("firstname")+" "+parseObject.getString("lastname"));
                                }
                                String specialty = "";
                                if (parseObject.has("specialty1") && parseObject.has("specialty2")) {
                                    specialty = parseObject.getString("specialty1") + " / " + parseObject.getString("specialty2");
                                } else if (parseObject.has("specialty1")) {
                                    specialty = parseObject.getString("specialty1");
                                } else if (parseObject.has("specialty2")) {
                                    specialty = parseObject.getString("specialty2");
                                }

                                etSpecialty.setText(specialty);

                                if(parseObject.has("prcno")) {
                                    etPRCLicNo.setText(parseObject.get("prcno").toString());
                                }
                                if(parseObject.has("ptrno")) {
                                    etPRTNo.setText(parseObject.get("ptrno").toString());
                                }
                                if(parseObject.has("clinic1_name")) {
                                    etClinicsName.setText(parseObject.get("clinic1_name").toString());
                                }

                                String address = "";
                                if(parseObject.has("clinic1_address_1") && parseObject.has("clinic1_city")) {
                                    address = parseObject.getString("clinic1_address_1") + ", " + parseObject.getString("clinic1_city");
                                } else if (parseObject.has("clinic1_address_1")) {
                                    address = parseObject.getString("clinic1_address_1");
                                } else if (parseObject.has("clinic1_city")) {
                                    address = parseObject.getString("clinic1_city");
                                }
                                etClinicAddress.setText(address);

                                String schedule = "";
                                if(parseObject.has("clinic1_schedule") && parseObject.has("clinic1_time")) {
                                    schedule = parseObject.getString("clinic1_schedule") + ", " + parseObject.getString("clinic1_time");
                                } else if (parseObject.has("clinic1_schedule")) {
                                    schedule = parseObject.getString("clinic1_schedule");
                                } else if (parseObject.has("clinic1_time")) {
                                    schedule = parseObject.getString("clinic1_time");
                                }
                                etClinicSchedule.setText(schedule);

                                if(parseObject.has("clinic1_contactno")) {
                                    etDoctorContactNo.setText(parseObject.get("clinic1_contactno").toString());
                                }
                                if(parseObject.has("mobileno")) {
                                    etDoctorMobileNo.setText(parseObject.get("mobileno").toString());
                                }
                                if(parseObject.has("clinic1_notes")) {
                                    etClinicNotes.setText(parseObject.get("clinic1_notes").toString());
                                }

                                String secretary = "";
                                if(parseObject.has("secretary1_firstname") && parseObject.has("secretary1_lastname")) {
                                    secretary = parseObject.getString("secretary1_firstname") + ", " + parseObject.getString("secretary1_lastname");
                                } else if (parseObject.has("secretary1_firstname")) {
                                    secretary = parseObject.getString("secretary1_firstname");
                                } else if (parseObject.has("secretary1_lastname")) {
                                    secretary = parseObject.getString("secretary1_lastname");
                                }
                                etSecretarysName.setText(secretary);

                                if(parseObject.has("secretary1_mobile")) {
                                    etSecretarysContactNo.setText(parseObject.get("secretary1_mobile").toString());
                                }

                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mProgressDialog.dismiss();
                                    }
                                });

                            }
                        }
                    });
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

        }
    }
}
