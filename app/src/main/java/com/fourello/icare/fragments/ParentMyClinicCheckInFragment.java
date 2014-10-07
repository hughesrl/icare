package com.fourello.icare.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fourello.icare.DashboardParentFragmentActivity;
import com.fourello.icare.ICareApplication;
import com.fourello.icare.R;
import com.fourello.icare.datas.ClinicSurvey;
import com.fourello.icare.datas.Doctors;
import com.fourello.icare.datas.PatientChildData;
import com.fourello.icare.datas.Patients;
import com.fourello.icare.datas.SpinnerItems;
import com.fourello.icare.view.CustomButton;
import com.fourello.icare.view.CustomEditTextView;
import com.fourello.icare.view.CustomTextView;
import com.fourello.icare.widgets.FragmentUtils;
import com.fourello.icare.widgets.ParseProxyObject;
import com.fourello.icare.widgets.Utils;
import com.parse.CountCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ParentMyClinicCheckInFragment extends Fragment implements
        DashboardParentFragmentActivity.OpenMenuCallbacks {
    // TODO: Rename parameter arguments, choose names that match
    private ParseProxyObject mParamLoginData;
    private ArrayList<PatientChildData> mParamChildData;
    private int mParamChildDataPosition;

    private ProgressDialog mProgressDialog;

    private View myFragmentView;

    private CustomEditTextView pName, pAccompaniedBy, pMomsNotes, etGrowthTrackerWeight, etGrowthTrackerHeight, etGrowthTrackerHead, etGrowthTrackerChest, etGrowthTrackerTemperature;
    private Spinner spinnerRelationshipToPatient, spinnerPurpose, spinnerTypeOfDelivery, spinnerAllergyRisk;
    private static ImageView patient_photo;

    private String accompaniedBy = "";
    private String relationship = "";

    //keep track of camera capture intent
    private final int CAMERA_CAPTURE = 1;
    private final int UPLOAD_PHOTO = 2;
    private byte[] bytearray;

    private Patients myData;
    //private PatientChildData myChild;

    private LinearLayout checkInLayoutEmpty;
    private LinearLayout checkInLayout;
    private PatientChildData childData;
    private ClinicSurvey clinicSurvery;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SchedulerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ParentMyClinicCheckInFragment newInstance(String param1, String param2) {
        ParentMyClinicCheckInFragment fragment = new ParentMyClinicCheckInFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_LOGIN_DATA, param1);
//        fragment.setArguments(args);
        return fragment;
    }
    public ParentMyClinicCheckInFragment() {
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
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myFragmentView = inflater.inflate(R.layout.fragment_parent_check_in, container, false);

        clinicSurvery = new ClinicSurvey();

        checkInLayoutEmpty = (LinearLayout) myFragmentView.findViewById(R.id.checkInLayoutEmpty);
        checkInLayout = (LinearLayout) myFragmentView.findViewById(R.id.checkInLayout);

        ((DashboardParentFragmentActivity)getActivity()).changePageTitle("My Clinic");

        ParseQuery<Doctors> query = Doctors.getQuery();
        query.fromLocalDatastore();
        try {
            if(query.count() == 0) {
                Toast.makeText(getActivity(), "Please set your Doctor", Toast.LENGTH_LONG).show();
            } else {
                query.getFirstInBackground(new GetCallback<Doctors>() {
                    @Override
                    public void done(Doctors doctors, ParseException e) {
                        if(e == null) {
                            new CheckIfPINOfTheDayExists(getActivity(), inflater, container, doctors.getDoctorID()).execute();
                        }
                    }
                });
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }


        childData = mParamChildData.get(mParamChildDataPosition);

        patient_photo = (ImageView)myFragmentView.findViewById(R.id.patient_photo);
        patient_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        accompaniedBy = mParamLoginData.getString("firstname") + " " + mParamLoginData.getString("lastname");
        pAccompaniedBy = (CustomEditTextView) myFragmentView.findViewById(R.id.etAccompaniedBy);
        pAccompaniedBy.setText(accompaniedBy);

        relationship = mParamLoginData.getString("role");
        spinnerRelationshipToPatient = (Spinner) myFragmentView.findViewById(R.id.spinnerRelationshipToPatient);
        CustomAdapter adapterspinnerRelationshipToPatient = new CustomAdapter(getActivity(), android.R.layout.simple_spinner_item, ICareApplication.populateRelationshipToPatient());
        spinnerRelationshipToPatient.setAdapter(adapterspinnerRelationshipToPatient);

        // Set Selection
        int spinnerPosition = adapterspinnerRelationshipToPatient.getPosition(relationship);
        spinnerRelationshipToPatient.setSelection(spinnerPosition);

        pName = (CustomEditTextView) myFragmentView.findViewById(R.id.etPatientsName);
        pName.setText(childData.getFullName());


        spinnerPurpose = (Spinner) myFragmentView.findViewById(R.id.spinnerPurpose);
        CustomAdapter adapterspinnerPurpose = new CustomAdapter(getActivity(), android.R.layout.simple_spinner_item, ICareApplication.populatePurpose());
        spinnerPurpose.setAdapter(adapterspinnerPurpose);

        spinnerTypeOfDelivery = (Spinner) myFragmentView.findViewById(R.id.spinnerTypeOfDelivery);
        CustomAdapter adapterspinnerTypeOfDelivery = new CustomAdapter(getActivity(), android.R.layout.simple_spinner_item, ICareApplication.populateTypeOfDelivery());
        spinnerTypeOfDelivery.setAdapter(adapterspinnerTypeOfDelivery);

        if(!childData.getDeliveryType().isEmpty()) {
            // Set Selection
            int spinnerTypeOfDeliveryPosition = adapterspinnerRelationshipToPatient.getPosition(childData.getDeliveryType());
            spinnerTypeOfDelivery.setSelection(spinnerTypeOfDeliveryPosition);
        }

        spinnerAllergyRisk = (Spinner) myFragmentView.findViewById(R.id.spinnerAllergyRisk);
        CustomAdapter adapterspinnerAllergyRisk = new CustomAdapter(getActivity(), android.R.layout.simple_spinner_item, ICareApplication.populateAllergyRisk());
        spinnerAllergyRisk.setAdapter(adapterspinnerAllergyRisk);
        if(!childData.getAllergyRisk().isEmpty()) {
            // Set Selection
            int spinnerAllergyRiskPosition = adapterspinnerRelationshipToPatient.getPosition(childData.getAllergyRisk());
            spinnerAllergyRisk.setSelection(spinnerAllergyRiskPosition);
        }

        pMomsNotes = (CustomEditTextView) myFragmentView.findViewById(R.id.etMomsNotes);


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

        CustomButton btnQuickSurvey = (CustomButton) myFragmentView.findViewById(R.id.btnQuickSurvey);
        btnQuickSurvey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                final String typeOfDelivery = String.valueOf(spinnerTypeOfDelivery.getSelectedItem());
                final String purpose = String.valueOf(spinnerPurpose.getSelectedItem());
                final String allergyRisk = String.valueOf(spinnerAllergyRisk.getSelectedItem());

                clinicSurvery.setAccompaniedBy(accompaniedBy);
                clinicSurvery.setDoctorId(childData.getDoctorID());
                clinicSurvery.setEmail(mParamLoginData.getString("email"));
                clinicSurvery.setChest(growthTrackerChest);
                clinicSurvery.setHead(growthTrackerHead);
                clinicSurvery.setHeight(growthTrackerHeight);
                clinicSurvery.setTemperature(growthTrackerTemperature);
                clinicSurvery.setWeight(growthTrackerWeight);
                clinicSurvery.setPatientObjectId(childData.getPatientObjectId());
                clinicSurvery.setPatientFullname(childData.getFullName());
                //clinicSurvery.setMomsNotes(momsNotes+" "); // question
                clinicSurvery.setQuestion(momsNotes+" "); // question
                clinicSurvery.setPurposeOfVisit(purpose);
                clinicSurvery.setRelationshipToPatient(relationshipToPatient);
                clinicSurvery.setTypeOfDeliveryType(typeOfDelivery);
                clinicSurvery.setAllergyRisk(allergyRisk);

                try {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd, yyyy");
                    String birthdayAndTime = childData.getbDate();

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
                            ((DashboardParentFragmentActivity) getActivity()).ParentDashboard(mParamChildData, mParamChildDataPosition);
                        } else {
                            Toast.makeText(
                                    getActivity().getApplicationContext(),
                                    "Error saving: " + clinicSurveryError.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


        return myFragmentView;
    }

    private void selectImage() {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    try {
                        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(captureIntent, CAMERA_CAPTURE);
                    } catch (ActivityNotFoundException anfe) {
                        //display an error message
                        String errorMessage = "Whoops - your device doesn't support capturing images!";
                        Toast toast = Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT);
                        toast.show();
                    }
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent, UPLOAD_PHOTO);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        FragmentUtils.startActivityForResultWhileSavingOrigin(this, requestCode, intent, null);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == CAMERA_CAPTURE) {
                final Bitmap thePic = data.getExtras().getParcelable("data");

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                thePic.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                bytearray = stream.toByteArray();// get byte array here
                Log.d("ROBERT bytearray", bytearray.length+"");
                if (bytearray != null) {

                    final ParseFile photoFile = new ParseFile("photo.jpg", bytearray);
                    photoFile.saveInBackground(new SaveCallback() {
                        public void done(ParseException e) {
                            if (e == null) {
                                clinicSurvery.setPatientphoto(photoFile);
                                patient_photo.setBackground(Utils.resizedBitmapDisplay(getActivity(), thePic));
                            } else {
                                Toast.makeText(getActivity(),
                                        "Error saving: " + e.getMessage(),
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            } else if (requestCode == UPLOAD_PHOTO) {
                Uri selectedImage = data.getData();
                try {
                    final Bitmap thumbnail = decodeUri(selectedImage);

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    bytearray = stream.toByteArray();// get byte array here
                    if (bytearray != null) {
                        Log.w("path of image from gallery......******************.........", selectedImage + "");

                        if (bytearray != null) {
                            final ParseFile photoFile = new ParseFile("photo.jpg", bytearray);
                            photoFile.saveInBackground(new SaveCallback() {
                                public void done(ParseException e) {
                                    if (e != null) {
                                        Toast.makeText(getActivity(),
                                                "Error saving: " + e.getMessage(),
                                                Toast.LENGTH_LONG).show();
                                    } else {
                                        clinicSurvery.setPatientphoto(photoFile);
                                        patient_photo.setBackground(Utils.resizedBitmapDisplay(getActivity(), thumbnail));
                                    }
                                }
                            });
                        }

                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private Bitmap decodeUri(Uri selectedImage) throws FileNotFoundException {
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(
                getActivity().getContentResolver().openInputStream(selectedImage), null, o);

        final int REQUIRED_SIZE = 100;

        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE) {
                break;
            }
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(
                getActivity().getContentResolver().openInputStream(selectedImage), null, o2);
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

    private class CheckIfPINOfTheDayExists extends AsyncTask<Void, Void, Integer> {
        Context context;
        LayoutInflater inflater;
        ViewGroup container;
        private String ret = "";
        int recordCount = 0;

        int doctorID;

        public CheckIfPINOfTheDayExists(Context context, LayoutInflater inflater, ViewGroup container, int objectId) {
            this.context = context;
            this.inflater = inflater;
            this.container = container;
            this.doctorID = objectId;
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
        protected Integer doInBackground(Void... params) {
            // Create the array
            final Date midnight = new Date();
            midnight.setHours(0);
            midnight.setMinutes(0);
            midnight.setSeconds(0);

            final Date elevenfiftynine = new Date();
            elevenfiftynine.setHours(23);
            elevenfiftynine.setMinutes(59);
            elevenfiftynine.setSeconds(59);

            Log.d("ROBERT DID", doctorID+"");
            Log.d("ROBERT midnight", midnight+"");
            Log.d("ROBERT elevenfiftynine", elevenfiftynine+"");
            ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(ICareApplication.SETTINGS_LABEL);
            query.whereEqualTo("doctorid", doctorID+"");
            query.whereGreaterThan("updatedAt", midnight);
            query.whereLessThan("updatedAt", elevenfiftynine);

            query.countInBackground(new CountCallback() {
                @Override
                public void done(int i, ParseException e) {
                    if (e == null) {
                        recordCount = i;
                        Log.d("ROBERT",recordCount+"");
                        if (recordCount == 0) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ((DashboardParentFragmentActivity) getActivity()).showAlertDialog("alert", String.valueOf(doctorID));
                                    mProgressDialog.dismiss();
                                }
                            });
                        } else {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ((DashboardParentFragmentActivity) getActivity()).showAlertDialog("enter_pin", String.valueOf(doctorID));
                                    mProgressDialog.dismiss();
                                }
                            });
                        }
                    } else {
                        Log.d("ROBERT", "ERROR :"+ e.toString());
                    }
                }
            });
            return recordCount;
        }

        @Override
        protected void onPostExecute(Integer result) {
//            Log.d("ROBERT", "COUNT:"+result);
//            if(result == 0) {
//                ((DashboardParentFragmentActivity) getActivity()).showAlertDialog("alert");
//            } else {
//                ((DashboardParentFragmentActivity) getActivity()).showAlertDialog("enter_pin");
//            }
//
//            mProgressDialog.dismiss();
        }
    }




    /*CUSTOMS*/
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
}
