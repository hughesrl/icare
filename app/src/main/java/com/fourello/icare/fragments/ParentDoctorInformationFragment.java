package com.fourello.icare.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.fourello.icare.DashboardParentFragmentActivity;
import com.fourello.icare.R;
import com.fourello.icare.datas.Doctors;
import com.fourello.icare.datas.PatientChildData;
import com.fourello.icare.datas.Patients;
import com.fourello.icare.view.CustomButton;
import com.fourello.icare.view.CustomEditTextView;
import com.fourello.icare.widgets.ParseProxyObject;
import com.fourello.icare.widgets.Utils;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.ArrayList;

public class ParentDoctorInformationFragment extends Fragment implements
        DashboardParentFragmentActivity.OpenMenuCallbacks {
    // TODO: Rename parameter arguments, choose names that match
    private ParseProxyObject mParamLoginData;
    private ArrayList<PatientChildData> mParamChildData;
    private int mParamChildDataPosition;

    private ProgressDialog mProgressDialog;

    private View myFragmentView;
    ImageView imgDoctorPhoto;

    CustomEditTextView etDoctorsName, etClinicsName, etClinicAddress,
             etDoctorContactNo, etSecretarysName, etSecretarysContactNo;



    private Patients myData;
    private PatientChildData myChild;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SchedulerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ParentDoctorInformationFragment newInstance(String param1, String param2) {
        ParentDoctorInformationFragment fragment = new ParentDoctorInformationFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_LOGIN_DATA, param1);
//        fragment.setArguments(args);
        return fragment;
    }
    public ParentDoctorInformationFragment() {
        // Required empty public constructor
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myFragmentView = inflater.inflate(R.layout.fragment_parent_doctor_information, container, false);

        ((DashboardParentFragmentActivity)getActivity()).changePageTitle("Settings");

        myChild = mParamChildData.get(mParamChildDataPosition);


        imgDoctorPhoto = (ImageView)myFragmentView.findViewById(R.id.doctor_photo);
        etDoctorsName = (CustomEditTextView)myFragmentView.findViewById(R.id.etDoctorsName);
//        etSpecialty = (CustomEditTextView)myFragmentView.findViewById(R.id.etSpecialty);
//        etPRCLicNo = (CustomEditTextView)myFragmentView.findViewById(R.id.etPRCLicNo);
//        etPRTNo = (CustomEditTextView)myFragmentView.findViewById(R.id.etPRTNo);
        etClinicsName = (CustomEditTextView)myFragmentView.findViewById(R.id.etClinicsName);
        etClinicAddress = (CustomEditTextView)myFragmentView.findViewById(R.id.etClinicAddress);
//        etClinicSchedule = (CustomEditTextView)myFragmentView.findViewById(R.id.etClinicSchedule);
        etDoctorContactNo = (CustomEditTextView)myFragmentView.findViewById(R.id.etDoctorContactNo);
//        etDoctorMobileNo = (CustomEditTextView)myFragmentView.findViewById(R.id.etDoctorMobileNo);
//        etClinicNotes = (CustomEditTextView)myFragmentView.findViewById(R.id.etClinicNotes);
        etSecretarysName = (CustomEditTextView)myFragmentView.findViewById(R.id.etSecretarysName);
        etSecretarysContactNo = (CustomEditTextView)myFragmentView.findViewById(R.id.etSecretarysContactNo);

        final LinearLayout linkDoctor = (LinearLayout)myFragmentView.findViewById(R.id.linkDoctor);
        final ScrollView doctorInfo = (ScrollView)myFragmentView.findViewById(R.id.doctorInfo);

        ParseQuery<Doctors> query = Doctors.getQuery();
        query.fromLocalDatastore();
        try {
            if(query.count() == 0) {
                //TODO: NO DOCTOR

                doctorInfo.setVisibility(View.GONE);
                linkDoctor.setVisibility(View.VISIBLE);

                final CustomEditTextView etDoctorID = (CustomEditTextView)myFragmentView.findViewById(R.id.etDoctorID);
                CustomButton btnLinkMyDoctor = (CustomButton)myFragmentView.findViewById(R.id.btnLinkMyDoctor);

                btnLinkMyDoctor.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!etDoctorID.getText().toString().trim().equals("")) {
                            if (mProgressDialog == null) {
                                mProgressDialog = Utils.createProgressDialog(getActivity());
                                mProgressDialog.show();
                            } else {
                                mProgressDialog.show();
                            }
                            String doctorObjectId = etDoctorID.getText().toString();
                            ParseQuery<Doctors> queryLinkMyDoctor = Doctors.getQuery();
                            queryLinkMyDoctor.getInBackground(doctorObjectId, new GetCallback<Doctors>() {
                                @Override
                                public void done(Doctors doctors, ParseException e) {
                                    if (e == null) {
                                        myData = ParseObject.createWithoutData(Patients.class, myChild.getPatientObjectId());

                                        myData.setDoctorId(String.valueOf(doctors.getDoctorID()));
                                        // Remove Exisiting Doctor in Background
                                        Doctors.unpinAllInBackground();
                                        // Save new Doctor Information in Background
                                        doctors.pinInBackground(
                                            new SaveCallback() {
                                                public void done(ParseException e) {
                                                    if (e == null) {
                                                        myData.saveInBackground(
                                                            new SaveCallback() {
                                                                @Override
                                                                public void done(ParseException error) {
                                                                    if (error == null) {
                                                                        getActivity().runOnUiThread(new Runnable() {
                                                                            public void run() {
                                                                                doctorInfo.setVisibility(View.VISIBLE);
                                                                                linkDoctor.setVisibility(View.GONE);

                                                                                getDoctorInformation();
                                                                                mProgressDialog.dismiss();
                                                                            }
                                                                        });

                                                                    }
                                                                }
                                                            }
                                                        );
                                                    } else {
                                                        Log.i("ROBERT DOCTOR", "Error pinning todos: " + e.getMessage());
                                                    }
                                                }
                                            }
                                        );
                                    } else {
                                        Log.i("ROBERT DOCTOR", "loadFromParse: Error finding pinned doctor: " + e.getMessage());
                                    }
                                }
                            });
                        }

                    }
                });

            } else {
                getDoctorInformation();
            }

            Log.d("ROBERT", query.count()+"");
        } catch (ParseException e) {
            e.printStackTrace();
        }

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
        ((DashboardParentFragmentActivity)getActivity()).showMenuContents(getView());
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

    public void getDoctorInformation() {
        ParseQuery<Doctors> query = Doctors.getQuery();
        query.fromLocalDatastore();
        query.getFirstInBackground(new GetCallback<Doctors>() {
            @Override
            public void done(Doctors doctors, ParseException e) {
                if(e == null) {
                    if(doctors.has("photoFile")) {
                        ParseFile myPhoto = doctors.getPhotoFile();
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
                    if(doctors.has("firstname") && doctors.has("lastname")) {
                        etDoctorsName.setText(doctors.getString("firstname")+" "+doctors.getString("lastname"));
                    }
//                    String specialty = "";
//                    if (doctors.has("specialty1") && doctors.has("specialty2")) {
//                        specialty = doctors.getString("specialty1") + " / " + doctors.getString("specialty2");
//                    } else if (doctors.has("specialty1")) {
//                        specialty = doctors.getString("specialty1");
//                    } else if (doctors.has("specialty2")) {
//                        specialty = doctors.getString("specialty2");
//                    }
//
//                    etSpecialty.setText(specialty);

//                    if(doctors.has("prcno")) {
//                        etPRCLicNo.setText(doctors.get("prcno").toString());
//                    }
//                    if(doctors.has("ptrno")) {
//                        etPRTNo.setText(doctors.get("ptrno").toString());
//                    }
                    if(doctors.has("clinic1_name")) {
                        etClinicsName.setText(doctors.get("clinic1_name").toString());
                    }

                    String address = "";
                    if(doctors.has("clinic1_address_1") && doctors.has("clinic1_city")) {
                        address = doctors.getString("clinic1_address_1") + ", " + doctors.getString("clinic1_city");
                    } else if (doctors.has("clinic1_address_1")) {
                        address = doctors.getString("clinic1_address_1");
                    } else if (doctors.has("clinic1_city")) {
                        address = doctors.getString("clinic1_city");
                    }
                    etClinicAddress.setText(address);

                    String schedule = "";
                    if(doctors.has("clinic1_schedule") && doctors.has("clinic1_time")) {
                        schedule = doctors.getString("clinic1_schedule") + ", " + doctors.getString("clinic1_time");
                    } else if (doctors.has("clinic1_schedule")) {
                        schedule = doctors.getString("clinic1_schedule");
                    } else if (doctors.has("clinic1_time")) {
                        schedule = doctors.getString("clinic1_time");
                    }
//                    etClinicSchedule.setText(schedule);

                    if(doctors.has("clinic1_contactno")) {
                        etDoctorContactNo.setText(doctors.get("clinic1_contactno").toString());
                    }
//                    if(doctors.has("mobileno")) {
//                        etDoctorMobileNo.setText(doctors.get("mobileno").toString());
//                    }
//                    if(doctors.has("clinic1_notes")) {
//                        etClinicNotes.setText(doctors.get("clinic1_notes").toString());
//                    }

                    String secretary = "";
                    if(doctors.has("secretary1_firstname") && doctors.has("secretary1_lastname")) {
                        secretary = doctors.getString("secretary1_firstname") + ", " + doctors.getString("secretary1_lastname");
                    } else if (doctors.has("secretary1_firstname")) {
                        secretary = doctors.getString("secretary1_firstname");
                    } else if (doctors.has("secretary1_lastname")) {
                        secretary = doctors.getString("secretary1_lastname");
                    }
                    etSecretarysName.setText(secretary);

                    if(doctors.has("secretary1_mobile")) {
                        etSecretarysContactNo.setText(doctors.get("secretary1_mobile").toString());
                    }
                }
            }
        });
    }

}
