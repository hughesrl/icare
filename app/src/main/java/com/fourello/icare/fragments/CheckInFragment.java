package com.fourello.icare.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fourello.icare.DashboardDoctorFragmentActivity;
import com.fourello.icare.ICareApplication;
import com.fourello.icare.R;
import com.fourello.icare.datas.ClinicSurvey;
import com.fourello.icare.datas.PatientCheckIn;
import com.fourello.icare.datas.PatientDatabase;
import com.fourello.icare.datas.SpinnerItems;
import com.fourello.icare.view.CustomButton;
import com.fourello.icare.view.CustomEditTextView;
import com.fourello.icare.view.CustomTextView;
import com.fourello.icare.view.RoundedImageView;
import com.fourello.icare.widgets.FragmentUtils;
import com.fourello.icare.widgets.HumanTime;
import com.fourello.icare.widgets.ParseProxyObject;
import com.fourello.icare.widgets.PasswordDialogFragment;
import com.fourello.icare.widgets.Utils;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CheckInFragment extends Fragment implements
        PasswordDialogFragment.PasswordDialogListener {
    public static final String ARG_LOGIN_DATA = "loginData";
    public static final String ARG_MY_PICTURE = "myPicture";
    public static final String ARG_PATIENT_OBJECT_ID = "patientObjectId";
    public static final String ARG_PATIENT_DATA = "patientData";
    private static  PatientDatabase mParamPatientData;
    private static ParseProxyObject mParamLoginData;
    private static String mParamPatientObjectId;

    private PatientCheckIn patientCheckIn;
    private ClinicSurvey clinicSurvery;




    private static ViewGroup myFragmentView;

    // TODO: Rename and change types of parameters

    private byte[] mParamMyPicture;


    private ProgressDialog mProgressDialog;
    //keep track of camera capture intent
    public final static int CAMERA_CAPTURE = 1;
    private byte[] bytearray;

    private CustomEditTextView pName, pAccompaniedBy, pMomsNotes, etGrowthTrackerWeight, etGrowthTrackerHeight, etGrowthTrackerHead, etGrowthTrackerChest, etGrowthTrackerTemperature;
    private Spinner spinnerRelationshipToPatient, spinnerPurpose, spinnerAllergyRisk;
    private static ImageView patient_photo;

    private String accompaniedBy = "";
    private String relationship = "";


    public static Fragment newInstance(int position, PatientDatabase patientData, ParseProxyObject loginData, String patientObjectId) {
        CheckInFragment f = new CheckInFragment();

        mParamPatientData = patientData;
        mParamLoginData = loginData;
        mParamPatientObjectId = patientObjectId;

        return f;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParamLoginData = (ParseProxyObject) getArguments().getSerializable(PatientDatabaseActionsFragment.ARG_LOGIN_DATA);
            mParamMyPicture = getArguments().getByteArray(PatientDatabaseActionsFragment.ARG_MY_PICTURE);
            mParamPatientObjectId = getArguments().getString(PatientDatabaseActionsFragment.ARG_PATIENT_OBJECT_ID);
            mParamPatientData = getArguments().getParcelable(PatientDatabaseActionsFragment.ARG_PATIENT_DATA);
        }

        patientCheckIn = new PatientCheckIn();

        clinicSurvery = new ClinicSurvey();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        myFragmentView = (ViewGroup) inflater.inflate(R.layout.fragment_checkin_patient, container, false);

        // Adding Contents from Users Class
        ParseQuery<ParseObject> queryUsers = new ParseQuery<ParseObject>(ICareApplication.USERS_LABEL);
        queryUsers.whereEqualTo("email", mParamPatientData.getParentEmail());
        queryUsers.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> usersDatabaseParseObject, ParseException e) {
                if (e == null) {
                    if (usersDatabaseParseObject.size() > 0) {
                        ParseObject usersDatabaseObject = usersDatabaseParseObject.get(0);

                        accompaniedBy = usersDatabaseObject.get("firstname") + " " + usersDatabaseObject.get("lastname");
                        pAccompaniedBy = (CustomEditTextView) myFragmentView.findViewById(R.id.etAccompaniedBy);
                        pAccompaniedBy.setText(accompaniedBy);

                        relationship = usersDatabaseObject.getString("role");
                        spinnerRelationshipToPatient = (Spinner) myFragmentView.findViewById(R.id.spinnerRelationshipToPatient);
                        CustomAdapter adapterspinnerRelationshipToPatient = new CustomAdapter(getActivity(), android.R.layout.simple_spinner_item, ICareApplication.populateRelationshipToPatient());
                        spinnerRelationshipToPatient.setAdapter(adapterspinnerRelationshipToPatient);
                        // Set Selection
                        int spinnerPosition = adapterspinnerRelationshipToPatient.getPosition(relationship);
                        spinnerRelationshipToPatient.setSelection(spinnerPosition);
                    }
                }
            }
        });
        pName = (CustomEditTextView) myFragmentView.findViewById(R.id.etPatientsName);
        pName.setText(mParamPatientData.getFullName());



        pMomsNotes = (CustomEditTextView) myFragmentView.findViewById(R.id.etMomsNotes);
//        pMomsNotes.setText(mParamPatientData.getMoms());



        spinnerPurpose = (Spinner) myFragmentView.findViewById(R.id.spinnerPurpose);
        CustomAdapter adapterspinnerPurpose = new CustomAdapter(getActivity(), android.R.layout.simple_spinner_item, ICareApplication.populatePurpose());
        spinnerPurpose.setAdapter(adapterspinnerPurpose);

        spinnerAllergyRisk = (Spinner) myFragmentView.findViewById(R.id.spinnerAllergyRisk);
        CustomAdapter adapterspinnerAllergyRisk = new CustomAdapter(getActivity(), android.R.layout.simple_spinner_item, ICareApplication.populateAllergyRisk());
        spinnerAllergyRisk.setAdapter(adapterspinnerAllergyRisk);


        // GROWTH TRACKER
        CustomTextView lblGrowthTrackerWeightValue = (CustomTextView) myFragmentView.findViewById(R.id.lblGrowthTrackerWeightValue);
        CustomTextView lblGrowthTrackerHeightValue = (CustomTextView) myFragmentView.findViewById(R.id.lblGrowthTrackerHeightValue);
        CustomTextView lblGrowthTrackerHeadValue = (CustomTextView) myFragmentView.findViewById(R.id.lblGrowthTrackerHeadValue);
        CustomTextView lblGrowthTrackerChestValue = (CustomTextView) myFragmentView.findViewById(R.id.lblGrowthTrackerChestValue);
        CustomTextView lblGrowthTrackerTemperatureValue = (CustomTextView) myFragmentView.findViewById(R.id.lblGrowthTrackerTemperatureValue);

        etGrowthTrackerWeight = (CustomEditTextView) myFragmentView.findViewById(R.id.etGrowthTrackerWeight);
        etGrowthTrackerHeight = (CustomEditTextView) myFragmentView.findViewById(R.id.etGrowthTrackerHeight);
        etGrowthTrackerHead = (CustomEditTextView) myFragmentView.findViewById(R.id.etGrowthTrackerHead);
        etGrowthTrackerChest = (CustomEditTextView) myFragmentView.findViewById(R.id.etGrowthTrackerChest);
        etGrowthTrackerTemperature = (CustomEditTextView) myFragmentView.findViewById(R.id.etGrowthTrackerTemperature);

        etGrowthTrackerWeight.addTextChangedListener(new CustomTextWatcher(etGrowthTrackerWeight, lblGrowthTrackerWeightValue));
        etGrowthTrackerHeight.addTextChangedListener(new CustomTextWatcher(etGrowthTrackerHeight, lblGrowthTrackerHeightValue));
        etGrowthTrackerHead.addTextChangedListener(new CustomTextWatcher(etGrowthTrackerHead, lblGrowthTrackerHeadValue));
        etGrowthTrackerChest.addTextChangedListener(new CustomTextWatcher(etGrowthTrackerChest, lblGrowthTrackerChestValue));
        etGrowthTrackerTemperature.addTextChangedListener(new CustomTextWatcher(etGrowthTrackerTemperature, lblGrowthTrackerTemperatureValue));

        etGrowthTrackerWeight.setOnKeyListener(new CustomOnKeyListener(etGrowthTrackerWeight, lblGrowthTrackerWeightValue));
        etGrowthTrackerHeight.setOnKeyListener(new CustomOnKeyListener(etGrowthTrackerHeight, lblGrowthTrackerHeightValue));
        etGrowthTrackerHead.setOnKeyListener(new CustomOnKeyListener(etGrowthTrackerHead, lblGrowthTrackerHeadValue));
        etGrowthTrackerChest.setOnKeyListener(new CustomOnKeyListener(etGrowthTrackerChest, lblGrowthTrackerChestValue));
        etGrowthTrackerTemperature.setOnKeyListener(new CustomOnKeyListener(etGrowthTrackerTemperature, lblGrowthTrackerTemperatureValue));

        // PATIENT PHOTO
        patient_photo = (ImageView) myFragmentView.findViewById(R.id.patient_photo);

//        if(mParamMyPicture != null) {
//            Bitmap bMap = BitmapFactory.decodeByteArray(mParamMyPicture, 0, mParamMyPicture.length);
//            Bitmap profileInCircle = RoundedImageView.getRoundedCornerBitmap(bMap);
//
//            patient_photo.setImageBitmap(profileInCircle);
//        }
        patient_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(captureIntent, CAMERA_CAPTURE);
                } catch (ActivityNotFoundException anfe) {
                    //display an error message
                    String errorMessage = "Whoops - your device doesn't support capturing images!";
                    Toast toast = Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });


        CustomButton btnQuickSurvey = (CustomButton) myFragmentView.findViewById(R.id.btnQuickSurvey);
        btnQuickSurvey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((DashboardDoctorFragmentActivity)getActivity()).showPasswordDialog(CheckInFragment.this, PasswordDialogFragment.PASSWORD_DIALOG_CUSTOM);
            }
        });

        return myFragmentView;
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        FragmentUtils.startActivityForResultWhileSavingOrigin(this, requestCode, intent, null);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if( requestCode == CheckInFragment.CAMERA_CAPTURE ) { // 1 Checkin
            if(resultCode != 0) {
                final Bitmap thePic = data.getExtras().getParcelable("data");

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                thePic.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                bytearray = stream.toByteArray();// get byte array here
                Log.d("ROBERT bytearray", bytearray.toString()+"");
                if (bytearray != null) {
                    final ParseFile photoFile = new ParseFile(mParamPatientObjectId+"_photo.jpg", bytearray);
                    photoFile.saveInBackground(new SaveCallback() {
                        public void done(ParseException e) {
                            if (e != null) {
                                Toast.makeText(getActivity(),
                                        "Error saving: " + e.getMessage(),
                                        Toast.LENGTH_LONG).show();
                            } else {
                                clinicSurvery.setPatientphoto(photoFile);
//                                Bitmap profileInCircle = RoundedImageView.getRoundedCornerBitmap(thePic);
                                patient_photo.setBackground(Utils.resizedBitmapDisplay(getActivity(), thePic));
//                                patient_photo.setImageBitmap(profileInCircle);
                            }
                        }
                    });
                }



            }
        }
    }


    @Override
    public void onDialogPositiveClick(final DialogFragment passwordDialog, String password, String purposeToOpen) {
        if(purposeToOpen.equals(PasswordDialogFragment.PASSWORD_DIALOG_CUSTOM)) {
            if (password.trim().equals(mParamLoginData.getString("password"))) {
                passwordDialog.dismiss();
                if (mProgressDialog == null) {
                    mProgressDialog = Utils.createProgressDialog(getActivity());
                    mProgressDialog.show();
                } else {
                    mProgressDialog.show();
                }

//                String patientName = String.valueOf(pName.getText());
                final String accompaniedBy = String.valueOf(pAccompaniedBy.getText());
                final String momsNotes = String.valueOf(pMomsNotes.getText());
                final String growthTrackerWeight = String.valueOf(etGrowthTrackerWeight.getText());
                final String growthTrackerHeight = String.valueOf(etGrowthTrackerHeight.getText());
                final String growthTrackerHead = String.valueOf(etGrowthTrackerHead.getText());
                final String growthTrackerChest = String.valueOf(etGrowthTrackerChest.getText());
                final String growthTrackerTemperature = String.valueOf(etGrowthTrackerTemperature.getText());

                final String relationshipToPatient = String.valueOf(spinnerRelationshipToPatient.getSelectedItem());
                final String purpose = String.valueOf(spinnerPurpose.getSelectedItem());
                final String allergyRisk = String.valueOf(spinnerAllergyRisk.getSelectedItem());

                clinicSurvery.setAccompaniedBy(accompaniedBy);
                clinicSurvery.setDoctorId(mParamLoginData.getString("linked_doctorid"));
                clinicSurvery.setEmail(mParamPatientData.getParentEmail());
                clinicSurvery.setChest(growthTrackerChest);
                clinicSurvery.setHead(growthTrackerHead);
                clinicSurvery.setHeight(growthTrackerHeight);
                clinicSurvery.setTemperature(growthTrackerTemperature);
                clinicSurvery.setWeight(growthTrackerWeight);
                clinicSurvery.setPatientObjectId(mParamPatientData.getPatientObjectId());
                clinicSurvery.setPatientFullname(mParamPatientData.getFullName());
                clinicSurvery.setMomsNotes(momsNotes+" "); // question
                clinicSurvery.setPurposeOfVisit(purpose);
                clinicSurvery.setRelationshipToPatient(relationshipToPatient);
                clinicSurvery.setAllergyRisk(allergyRisk);

                try {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd, yyyy HH:mm");
                    String birthdayAndTime = mParamPatientData.getbDate() + " 00:00";

                    Date convertedDate = simpleDateFormat.parse(birthdayAndTime);

                    clinicSurvery.setDateOfBirth(convertedDate);
                } catch (java.text.ParseException e1) {
                    e1.printStackTrace();
                }

                clinicSurvery.setStatusTag("0");
//                clinicSurvery.setVisitId(patientCheckIn.getObjectId()); // visit id will be inputted once the doctor view the data

                clinicSurvery.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException clinicSurveryError) {
                        if (clinicSurveryError == null) {
                            mProgressDialog.dismiss();
                            ((DashboardDoctorFragmentActivity) getActivity()).PatientDatabase();
                        } else {
                            Toast.makeText(
                                    getActivity().getApplicationContext(),
                                    "Error saving: " + clinicSurveryError.getMessage(),
                                    Toast.LENGTH_SHORT).show();
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
        dialog.dismiss();
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

        public int getPosition(String text) {
            for(int s=0;s<=(spinnerItems.size()-1);s++) {
                if(spinnerItems.get(s).getSpinnerTitle().equalsIgnoreCase(text.trim())) {
                    return s;
                }
            }
            return 0;
        }
    }

    private class CustomTextWatcher implements TextWatcher {
        private EditText mEditText;
        private TextView mTextView;

        public CustomTextWatcher(EditText e, TextView textView) {
            mEditText = e;
            mTextView = textView;
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            mTextView.setText(s);
        }

        public void afterTextChanged(Editable s) {
        }
    }

    private class CustomOnKeyListener implements View.OnKeyListener {

        private EditText mEditText;
        private TextView mTextView;

        public CustomOnKeyListener(EditText e, TextView textView) {
            mEditText = e;
            mTextView = textView;
        }

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
            if(keyCode == KeyEvent.KEYCODE_DEL){
                //this is for backspace
                if(mEditText.getText().toString().equals("")) {
                    mTextView.setText(getString(R.string.lblZero));
                }
            }
            return false;
        }
    }

    public PatientCheckIn getCurrentPatientCheckIn() {
        return patientCheckIn;
    }
}
