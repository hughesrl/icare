package com.fourello.icare.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.fourello.icare.DashboardParentFragmentActivity;
import com.fourello.icare.ICareApplication;
import com.fourello.icare.R;
import com.fourello.icare.adapters.SpinnerCustomAdapter;
import com.fourello.icare.datas.PatientChildData;
import com.fourello.icare.datas.PatientDatabase;
import com.fourello.icare.datas.Patients;
import com.fourello.icare.view.CustomButton;
import com.fourello.icare.view.CustomEditTextView;
import com.fourello.icare.view.CustomTextView;
import com.fourello.icare.widgets.FragmentUtils;
import com.fourello.icare.widgets.ParseProxyObject;
import com.fourello.icare.widgets.Utils;
import com.parse.FindCallback;
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
import java.util.List;

public class ParentBabyInfoFragment extends Fragment implements
        DashboardParentFragmentActivity.OpenMenuCallbacks,
        View.OnClickListener{

    // TODO: Rename and change types of parameters
    private ParseProxyObject mParamLoginData;
    private ArrayList<PatientChildData> mParamChildData;
    private int mParamChildDataPosition;

    private ViewGroup myFragmentView;

    private ProgressDialog mProgressDialog;
    //keep track of camera capture intent
    private final int CAMERA_CAPTURE = 1;
    private final int UPLOAD_PHOTO = 2;
    private byte[] bytearray;

    ImageView imgPatientPhoto;

    CustomEditTextView etPatientsFName, etPatientsMName, etPatientsLName,
            etIWasBornDate,etBornAt,
            etIWasDeliveredBy,
            etWhenIWasBornIWeighed, etAndIMeasured, etGrowthTrackerHead, etGrowthTrackerChest, etGrowthTrackerAbdomen,
            etCircumcisedOn, etEarsPiercedOn, etDistinguishingMarks,
            etNewBornScreening, etVaccinationsGiven,
            etMomFName, etMomMName, etMomLName, etSheWorksAt, etSheWorksAtAs, etHerHMOIs,
            etDadFName, etDadMName, etDadLName, etHeWorksAt, etHeWorksAtAs, etHisHMOIs,
            etILiveAtAddress1, etILiveAtAddress2;

    Spinner spinnerMyMomGaveBirthToMeThrough, spinnerAllergyRisk;

    private Patients patients;

    private PatientChildData myChild;
    public static Fragment newInstance(int position, PatientDatabase patientData, ParseProxyObject loginData, String patientObjectId) {
        ParentBabyInfoFragment f = new ParentBabyInfoFragment();

//        myChild = patientData;
//        mParamLoginData = loginData;
//        mParamPatientObjectId = patientObjectId;

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParamLoginData = (ParseProxyObject) getArguments().getSerializable(DashboardParentFragmentActivity.ARG_LOGIN_DATA);
            mParamChildData = getArguments().getParcelableArrayList(DashboardParentFragmentActivity.ARG_CHILD_DATA);
            mParamChildDataPosition = getArguments().getInt(DashboardParentFragmentActivity.ARG_CHILD_DATA_POS);
        }

//        patients = new Patients();




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        myFragmentView = (ViewGroup) inflater.inflate(R.layout.fragment_parent_baby_info, container, false);

        CustomTextView lblAllergyRiskNotes = (CustomTextView) myFragmentView.findViewById(R.id.lblAllergyRiskNotes);
        lblAllergyRiskNotes.setText(Html.fromHtml(getString(R.string.lblAllergyRiskNotes)));
        Linkify.addLinks(lblAllergyRiskNotes, Linkify.WEB_URLS);

        myChild = mParamChildData.get(mParamChildDataPosition);

        patients = ParseObject.createWithoutData(Patients.class, myChild.getPatientObjectId());
        ((DashboardParentFragmentActivity)getActivity()).changePageTitle("My Baby");

        imgPatientPhoto = (ImageView)myFragmentView.findViewById(R.id.patient_photo);
        imgPatientPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        etPatientsFName         = (CustomEditTextView)myFragmentView.findViewById(R.id.etPatientsFName);
        etPatientsMName         = (CustomEditTextView)myFragmentView.findViewById(R.id.etPatientsMName);
        etPatientsLName         = (CustomEditTextView)myFragmentView.findViewById(R.id.etPatientsLName);
        etIWasBornDate          = (CustomEditTextView)myFragmentView.findViewById(R.id.etIWasBornDate);
        etBornAt                = (CustomEditTextView)myFragmentView.findViewById(R.id.etBornAt);
        etIWasDeliveredBy       = (CustomEditTextView)myFragmentView.findViewById(R.id.etIWasDeliveredBy);
        etWhenIWasBornIWeighed  = (CustomEditTextView)myFragmentView.findViewById(R.id.etWhenIWasBornIWeighed);
        etAndIMeasured          = (CustomEditTextView)myFragmentView.findViewById(R.id.etAndIMeasured);
        etGrowthTrackerHead     = (CustomEditTextView)myFragmentView.findViewById(R.id.etGrowthTrackerHead);
        etGrowthTrackerChest    = (CustomEditTextView)myFragmentView.findViewById(R.id.etGrowthTrackerChest);
        etGrowthTrackerAbdomen  = (CustomEditTextView)myFragmentView.findViewById(R.id.etGrowthTrackerAbdomen);
        etCircumcisedOn         = (CustomEditTextView)myFragmentView.findViewById(R.id.etCircumcisedOn);
        etEarsPiercedOn         = (CustomEditTextView)myFragmentView.findViewById(R.id.etEarsPiercedOn);
        etDistinguishingMarks   = (CustomEditTextView)myFragmentView.findViewById(R.id.etDistinguishingMarks);
        etNewBornScreening      = (CustomEditTextView)myFragmentView.findViewById(R.id.etNewBornScreening);
        etVaccinationsGiven     = (CustomEditTextView)myFragmentView.findViewById(R.id.etVaccinationsGiven);
        etMomFName              = (CustomEditTextView)myFragmentView.findViewById(R.id.etMomFName);
        etMomMName              = (CustomEditTextView)myFragmentView.findViewById(R.id.etMomMName);
        etMomLName              = (CustomEditTextView)myFragmentView.findViewById(R.id.etMomLName);
        etSheWorksAt            = (CustomEditTextView)myFragmentView.findViewById(R.id.etSheWorksAt);
        etSheWorksAtAs          = (CustomEditTextView)myFragmentView.findViewById(R.id.etSheWorksAtAs);
        etHerHMOIs              = (CustomEditTextView)myFragmentView.findViewById(R.id.etHerHMOIs);
        etDadFName              = (CustomEditTextView)myFragmentView.findViewById(R.id.etDadFName);
        etDadMName              = (CustomEditTextView)myFragmentView.findViewById(R.id.etDadMName);
        etDadLName              = (CustomEditTextView)myFragmentView.findViewById(R.id.etDadLName);
        etHeWorksAt             = (CustomEditTextView)myFragmentView.findViewById(R.id.etHeWorksAt);
        etHeWorksAtAs           = (CustomEditTextView)myFragmentView.findViewById(R.id.etHeWorksAtAs);
        etHisHMOIs              = (CustomEditTextView)myFragmentView.findViewById(R.id.etHisHMOIs);
        etILiveAtAddress1       = (CustomEditTextView)myFragmentView.findViewById(R.id.etILiveAtAddress1);
        etILiveAtAddress2       = (CustomEditTextView)myFragmentView.findViewById(R.id.etILiveAtAddress2);


        spinnerMyMomGaveBirthToMeThrough = (Spinner) myFragmentView.findViewById(R.id.spinnerMyMomGaveBirthToMeThrough);
        SpinnerCustomAdapter adapterMyMomGaveBirthToMeThrough = new SpinnerCustomAdapter(getActivity(), android.R.layout.simple_spinner_item, ICareApplication.populateTypeOfDelivery());
        spinnerMyMomGaveBirthToMeThrough.setAdapter(adapterMyMomGaveBirthToMeThrough);

        spinnerAllergyRisk = (Spinner) myFragmentView.findViewById(R.id.spinnerAllergyRisk);
        SpinnerCustomAdapter adapterAllergyRisk = new SpinnerCustomAdapter(getActivity(), android.R.layout.simple_spinner_item, ICareApplication.populateAllergyRisk());
        spinnerAllergyRisk.setAdapter(adapterAllergyRisk);

        if(myChild.getPatientphoto()!=null) {
            Bitmap bMap = BitmapFactory.decodeByteArray(myChild.getPatientphoto(), 0, myChild.getPatientphoto().length);
            imgPatientPhoto.setBackground(Utils.resizedBitmapDisplay(getActivity(), bMap));
        }

        etPatientsFName.setText(myChild.getFirtname());
        etPatientsMName.setText(myChild.getMiddlename());
        etPatientsLName.setText(myChild.getLastname());

        etIWasBornDate.setText(myChild.getbDate());
        etBornAt.setText(myChild.getbPlace());
        etIWasDeliveredBy.setText(myChild.getDrName());

        // Set Selection Delivery Type
        int spinnerPosition = adapterMyMomGaveBirthToMeThrough.getPosition(myChild.getDeliveryType());
        spinnerMyMomGaveBirthToMeThrough.setSelection(spinnerPosition);

        etWhenIWasBornIWeighed.setText(myChild.getpWeight());
        etAndIMeasured.setText(myChild.getpHeight());
        etGrowthTrackerHead.setText(myChild.getpHead());
        etGrowthTrackerChest.setText(myChild.getpChest());
        etGrowthTrackerAbdomen.setText(myChild.getpAbdomen());

        // Set Selection Delivery Type
        int spinnerPositionAllergy = adapterAllergyRisk.getPosition(myChild.getAllergyRisk());
        spinnerAllergyRisk.setSelection(spinnerPositionAllergy);

        etCircumcisedOn.setText(myChild.getpCircumcisedOn());
        etEarsPiercedOn.setText(myChild.getpEarPiercedOn());
        etDistinguishingMarks.setText(myChild.getpDistinguishingMarks());

        etNewBornScreening.setText(myChild.getpNewbornScreening());
        etVaccinationsGiven.setText(myChild.getpVaccinationsGiven());

        etMomFName.setText(myChild.getpMomsFname());
        etMomMName.setText(myChild.getpMomsMname());
        etMomLName.setText(myChild.getpMomsLname());
        etSheWorksAt.setText(myChild.getpMomsWorkPlace());
        etSheWorksAtAs.setText(myChild.getpMomsWorkAs());
        etHerHMOIs.setText(myChild.getpMomsHMO());

        etDadFName.setText(myChild.getpDadsFname());
        etDadMName.setText(myChild.getpDadsMname());
        etDadLName.setText(myChild.getpDadsLname());
        etHeWorksAt.setText(myChild.getpDadsWorkPlace());
        etHeWorksAtAs.setText(myChild.getpDadsWorkAs());
        etHisHMOIs.setText(myChild.getpDadsHMO());

        etILiveAtAddress1.setText(myChild.getpAddress1());
        etILiveAtAddress2.setText(myChild.getpAddress2());


        CustomButton btnSaveInformation = (CustomButton) myFragmentView.findViewById(R.id.btnSaveInformation);
        btnSaveInformation.setOnClickListener(this);

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
                    Log.d("ROBERT", photoFile.getName());
                    photoFile.saveInBackground(new SaveCallback() {
                        public void done(ParseException e) {
                            if (e == null) {
                                patients.setPhotoFile(photoFile);
                                imgPatientPhoto.setBackground(Utils.resizedBitmapDisplay(getActivity(), thePic));
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
                            final ParseFile photoFile = new ParseFile(myChild.getFirtname()+"_photo.jpg", bytearray);
                            photoFile.saveInBackground(new SaveCallback() {
                                public void done(ParseException e) {
                                    if (e != null) {
                                        Toast.makeText(getActivity(),
                                                "Error saving: " + e.getMessage(),
                                                Toast.LENGTH_LONG).show();
                                    } else {
                                        patients.setPhotoFile(photoFile);
                                        imgPatientPhoto.setBackground(Utils.resizedBitmapDisplay(getActivity(), thumbnail));
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

    private void SaveInformation() {
        if (mProgressDialog == null) {
            mProgressDialog = Utils.createProgressDialog(getActivity());
            mProgressDialog.show();
        } else {
            mProgressDialog.show();
        }


        patients.setTypeOfDelivery(String.valueOf(spinnerMyMomGaveBirthToMeThrough.getSelectedItem()));
        patients.setAllergyRisk(String.valueOf(spinnerAllergyRisk.getSelectedItem()));

        patients.setFirstName(String.valueOf(etPatientsFName.getText()));
        patients.setMiddleName(String.valueOf(etPatientsMName.getText()));
        patients.setLastName(String.valueOf(etPatientsLName.getText()));

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd, yyyy");
        Date convertedDate;
        try {
            convertedDate = simpleDateFormat.parse(etIWasBornDate.getText().toString().trim());
            patients.setDateOfBirth(convertedDate);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }


        patients.setPlaceOfBirth(String.valueOf(etBornAt.getText()));
        patients.setDeliveredBy(String.valueOf(etIWasDeliveredBy.getText()));
        patients.setWeight(String.valueOf(etWhenIWasBornIWeighed.getText()));
        patients.setLength(String.valueOf(etAndIMeasured.getText()));
        patients.setHeadCircumference(String.valueOf(etGrowthTrackerHead.getText()));
        patients.setChestCircumference(String.valueOf(etGrowthTrackerChest.getText()));
        patients.setAbdomenCircumference(String.valueOf(etGrowthTrackerAbdomen.getText()));

        try {
            convertedDate = simpleDateFormat.parse(etCircumcisedOn.getText().toString().trim());
            patients.setCircumcisedOn(convertedDate);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        try {
            convertedDate = simpleDateFormat.parse(etEarsPiercedOn.getText().toString().trim());
            patients.setEarPiercedOn(convertedDate);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
//        final String sDistinguishingMarks = String.valueOf(etDistinguishingMarks.getText());
//        final String sNewBornScreening = String.valueOf(etNewBornScreening.getText());
//        final String sVaccinationsGiven = String.valueOf(etVaccinationsGiven.getText());
        patients.setMotherFirstName(String.valueOf(etMomFName.getText()));
        patients.setMotherMiddleName(String.valueOf(etMomMName.getText()));
        patients.setMotherLastName(String.valueOf(etMomLName.getText()));
        patients.setMotherCompany(String.valueOf(etSheWorksAt.getText()));
        patients.setMotherProfession(String.valueOf(etSheWorksAtAs.getText()));
        patients.setMotherHMO(String.valueOf(etHerHMOIs.getText()));

        patients.setFatherFirstName(String.valueOf(etDadFName.getText()));
        patients.setFatherMiddleName(String.valueOf(etDadMName.getText()));
        patients.setFatherLastName(String.valueOf(etDadLName.getText()));
        patients.setFatherCompany(String.valueOf(etHeWorksAt.getText()));
        patients.setFatherProfession(String.valueOf(etHeWorksAtAs.getText()));
        patients.setFatherHMO(String.valueOf(etHisHMOIs.getText()));

        patients.setAddress1(String.valueOf(etILiveAtAddress1.getText()));
        patients.setAddress2(String.valueOf(etILiveAtAddress2.getText()));

        patients.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException error) {
                if (error == null) {
                    ParseObject.unpinAllInBackground(ICareApplication.PATIENTS_LABEL);

                    ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(ICareApplication.PATIENTS_LABEL);
                    query.whereEqualTo("objectId", myChild.getPatientObjectId());
                    query.addAscendingOrder("firstname");
                    query.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> parseObjects, ParseException e) {
                            if (e == null) {
                                ParseObject.pinAllInBackground(ICareApplication.PATIENTS_LABEL, parseObjects);

                                Intent intent = new Intent(getActivity(), DashboardParentFragmentActivity.class);
                                intent.putExtra("loginData", mParamLoginData);
                                startActivity(intent);
                            } else {
                                Toast.makeText(
                                        getActivity().getApplicationContext(),
                                        "Error saving: " + e.getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }
                            mProgressDialog.dismiss();
                        }
                    });
                } else {
                    Toast.makeText(
                            getActivity().getApplicationContext(),
                            "Error saving: " + error.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    public void onMenuPressedCallback() {
        ((DashboardParentFragmentActivity) getActivity()).showMenuContents(getView());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSaveInformation:
                SaveInformation();
                break;
        }
    }


}
