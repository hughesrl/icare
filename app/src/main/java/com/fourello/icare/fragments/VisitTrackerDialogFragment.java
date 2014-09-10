package com.fourello.icare.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fourello.icare.R;
import com.fourello.icare.datas.PatientDatabase;
import com.fourello.icare.datas.PatientVisits;
import com.fourello.icare.datas.SpinnerItems;
import com.fourello.icare.view.CustomEditTextView;
import com.fourello.icare.view.CustomTextView;
import com.fourello.icare.view.RoundedImageView;
import com.fourello.icare.widgets.Utils;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class VisitTrackerDialogFragment extends DialogFragment {

    public static final String ARG_PATIENT_RECORD_OBJECT_ID = "mParamPatientRecordObjectId";
    public static final String ARG_PATIENT_FULL_NAME = "mParamPatientFullName";
    public static final String ARG_PATIENT_PHOTO = "mParamPatientPhoto";

    public static final String ARG_PATIENT_ARRAY_LIST_POSITION = "mParamPatientArrayListPosition";

    private String mParamPatientRecordObjectId;
    private String mParamPatientFullName;
    private byte[] mParamPatientPhoto;
    private int mParamPatientArrayListPosition;

    private ProgressDialog mProgressDialog;
    //keep track of camera capture intent
    private final int CAMERA_CAPTURE = 1;
    //keep track of cropping intent
    private final int PIC_CROP = 2;
    //captured picture uri
    private Uri picUri;

    private View inflatedView;
    private byte[] bytearray;

    private List<PatientDatabase> patientDatabaseArrayList;

    PatientVisits patientVisits;

    /*In order to receive event callback, create a dialog box activity must implement this interface.
         * In case the host need to query the properties dialog box, each method will pass a DialogFragment instance.  */
    public interface CheckinPatientDialogListener {
        public void onDialogDoneClick(DialogFragment dialog);
        public void onDialogCancelClick(DialogFragment dialog);
    }

    // Examples of the use of this interface to transmit motion events
    CheckinPatientDialogListener mListener;

    public static VisitTrackerDialogFragment newInstance(String mParamPatientRecordObjectId,
                                                           String mParamPatientFullName,
                                                           byte[] mParamPatientPhoto,
                                                           int mParamPatientArrayListPosition) {
        VisitTrackerDialogFragment f = new VisitTrackerDialogFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putString(ARG_PATIENT_RECORD_OBJECT_ID, mParamPatientRecordObjectId);
        args.putString(ARG_PATIENT_FULL_NAME, mParamPatientFullName);
        args.putByteArray(ARG_PATIENT_PHOTO, mParamPatientPhoto);
        args.putInt(ARG_PATIENT_ARRAY_LIST_POSITION, mParamPatientArrayListPosition);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams windowParams = window.getAttributes();
        windowParams.dimAmount = 0.90f;
        windowParams.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(windowParams);
    }
    // Override the Fragment.onAttach () method to instantiate NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // To verify whether acrivity inherits the callback interface host
        try {
            // Instantiate a NoticeDialogListener so that we can pass the event to the host
            mListener = (CheckinPatientDialogListener) getTargetFragment();
        } catch (ClassCastException e) {
            // Activity no inheritance interface exception is thrown
            throw new ClassCastException(activity.toString()
                    + " must implement CheckinPatientDialogListener");
        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, android.R.style.Theme_Light_Panel);
        if (getArguments() != null) {
            mParamPatientRecordObjectId = getArguments().getString(ARG_PATIENT_RECORD_OBJECT_ID);
            mParamPatientFullName = getArguments().getString(ARG_PATIENT_FULL_NAME);
            mParamPatientPhoto = getArguments().getByteArray(ARG_PATIENT_PHOTO);
            mParamPatientArrayListPosition = getArguments().getInt(ARG_PATIENT_ARRAY_LIST_POSITION);
        }
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        inflatedView = inflater.inflate(R.layout.dialog_visit_tracker, container, true);

        setCancelable(true);

        byte[] myPicture = patientVisits.getPhotoFile();

        if(myPicture!=null) {
            Bitmap bMap = BitmapFactory.decodeByteArray(myPicture, 0, myPicture.length);
            ImageView image = (ImageView) inflatedView.findViewById(R.id.patient_photo);
            image.setBackground(Utils.resizedBitmapDisplayUserPhoto(getActivity(), bMap));
        }

        TextView etPatientsName = (TextView) inflatedView.findViewById(R.id.etPatientsName);
        etPatientsName.setText(patientVisits.getPatientname());

        TextView etAge = (TextView) inflatedView.findViewById(R.id.etAge);
        etAge.setText(patientVisits.getAge());

        TextView etAccompaniedBy = (TextView) inflatedView.findViewById(R.id.etAccompaniedBy);
        etAccompaniedBy.setText(patientVisits.getAccompaniedBy());

        TextView etPurpose = (TextView) inflatedView.findViewById(R.id.etPurpose);
        etPurpose.setText(patientVisits.getPupose_of_visit());

        // MORE HERE
        // PATIENT NOTES
        // MORE HERE
        TextView etAllergyRisk = (TextView) inflatedView.findViewById(R.id.etAllergyRisk);
        etAllergyRisk.setText(patientVisits.getAllergyrisk());

        TextView etTypeOfDelivery = (TextView) inflatedView.findViewById(R.id.etTypeOfDelivery);
        etTypeOfDelivery.setText(patientVisits.getTypeofdelivery());


        TextView etInstructions = (TextView) inflatedView.findViewById(R.id.etInstructions);
        etInstructions.setText(patientVisits.getInstructions());

        TextView etNextVisit = (TextView) inflatedView.findViewById(R.id.etNextVisit);
        etNextVisit.setText(patientVisits.getNextvisit());

        try {
            String medications = patientVisits.getMedications();
            JSONArray newJArrayMedications = new JSONArray(medications);

            ListView listMedicationsTaken = (ListView)inflatedView.findViewById(R.id.medicationsTaken);
            JSONAdapterMedications jSONAdapterMedications = new JSONAdapterMedications(getActivity(), newJArrayMedications);

            listMedicationsTaken.setAdapter(jSONAdapterMedications);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // GROWTH TRACKER
        CustomTextView lblGrowthTrackerWeightValue = (CustomTextView) inflatedView.findViewById(R.id.lblGrowthTrackerWeightValue);
        CustomTextView lblGrowthTrackerHeightValue = (CustomTextView) inflatedView.findViewById(R.id.lblGrowthTrackerHeightValue);
        CustomTextView lblGrowthTrackerHeadValue = (CustomTextView) inflatedView.findViewById(R.id.lblGrowthTrackerHeadValue);
        CustomTextView lblGrowthTrackerChestValue = (CustomTextView) inflatedView.findViewById(R.id.lblGrowthTrackerChestValue);
        CustomTextView lblGrowthTrackerTemperatureValue = (CustomTextView) inflatedView.findViewById(R.id.lblGrowthTrackerTemperatureValue);

        CustomEditTextView etGrowthTrackerWeight = (CustomEditTextView) inflatedView.findViewById(R.id.etGrowthTrackerWeight);
        CustomEditTextView etGrowthTrackerHeight = (CustomEditTextView) inflatedView.findViewById(R.id.etGrowthTrackerHeight);
        CustomEditTextView etGrowthTrackerHead = (CustomEditTextView) inflatedView.findViewById(R.id.etGrowthTrackerHead);
        CustomEditTextView etGrowthTrackerChest = (CustomEditTextView) inflatedView.findViewById(R.id.etGrowthTrackerChest);
        CustomEditTextView etGrowthTrackerTemperature = (CustomEditTextView) inflatedView.findViewById(R.id.etGrowthTrackerTemperature);

        lblGrowthTrackerWeightValue.setText(patientVisits.getWeight());
        lblGrowthTrackerHeightValue.setText(patientVisits.getHeight());
        lblGrowthTrackerHeadValue.setText(patientVisits.getHead());
        lblGrowthTrackerChestValue.setText(patientVisits.getChest());
        lblGrowthTrackerTemperatureValue.setText(patientVisits.getTemperature());

        etGrowthTrackerWeight.setText(patientVisits.getWeight());
        etGrowthTrackerHeight.setText(patientVisits.getHeight());
        etGrowthTrackerHead.setText(patientVisits.getHead());
        etGrowthTrackerChest.setText(patientVisits.getChest());
        etGrowthTrackerTemperature.setText(patientVisits.getTemperature());

        return inflatedView;
    }


    public void setVisitContent(PatientVisits patientVisits) {
        this.patientVisits = patientVisits;
    }


    public class JSONAdapterMedications extends BaseAdapter implements ListAdapter {
        private final Activity activity;
        private final JSONArray jsonArray;

        private JSONAdapterMedications (Activity activity, JSONArray jsonArray) {
            assert activity != null;
            assert jsonArray != null;

            this.jsonArray = jsonArray;
            this.activity = activity;
        }


        @Override public int getCount() {
            return jsonArray.length();
        }

        @Override public JSONObject getItem(int position) {
            return jsonArray.optJSONObject(position);
        }

        @Override public long getItemId(int position) {
            JSONObject jsonObject = getItem(position);

            return jsonObject.optLong("id");
        }

        @Override public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null)
                convertView = activity.getLayoutInflater().inflate(R.layout.list_visit_tracker_items, null);
                CustomTextView text =(CustomTextView) convertView.findViewById(R.id.txtVisitDate);

                JSONObject json_data = getItem(position);
            if(null!=json_data ){
                String jj = null;
                try {
                    jj = json_data.getString("brandname");
                    text.setText(jj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            return convertView;
        }
    }
}
