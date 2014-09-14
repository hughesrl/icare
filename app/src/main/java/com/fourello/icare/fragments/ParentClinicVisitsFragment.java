package com.fourello.icare.fragments;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.fourello.icare.DashboardParentFragmentActivity;
import com.fourello.icare.R;
import com.fourello.icare.adapters.ParentClinicVisitsListAdapter;
import com.fourello.icare.datas.PatientChildData;
import com.fourello.icare.datas.PatientDatabase;
import com.fourello.icare.datas.Visits;
import com.fourello.icare.view.CustomEditTextView;
import com.fourello.icare.view.CustomTextView;
import com.fourello.icare.widgets.ParseProxyObject;
import com.fourello.icare.widgets.Utils;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ParentClinicVisitsFragment extends Fragment implements
        DashboardParentFragmentActivity.OpenMenuCallbacks,
        View.OnClickListener{

    // TODO: Rename and change types of parameters
    private ParseProxyObject mParamLoginData;
    private ArrayList<PatientChildData> mParamChildData;
    private int mParamChildDataPosition;

    private ViewGroup myFragmentView;

    private ProgressDialog mProgressDialog;
    TableLayout table_header;
    TableLayout table_layout;

    private String[] titles = {"Date", "Weight","Height", "Head"};

    private static int pos = 0;
    private ParentClinicVisitsListAdapter clinicVisitsListAdapter;


    public static Fragment newInstance(int position, PatientDatabase patientData, ParseProxyObject loginData, String patientObjectId) {
        ParentClinicVisitsFragment f = new ParentClinicVisitsFragment();

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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        myFragmentView = (ViewGroup) inflater.inflate(R.layout.fragment_parent_clinic_visits, container, false);

        ((DashboardParentFragmentActivity)getActivity()).changePageTitle("My Clinic Visit");

        // Set up the Parse query to use in the adapter
        ParseQueryAdapter.QueryFactory<Visits> factory = new ParseQueryAdapter.QueryFactory<Visits>() {
            public ParseQuery<Visits> create() {
                ParseQuery<Visits> query = Visits.getQuery();
                query.addDescendingOrder("updatedAt");
                query.fromLocalDatastore();
                return query;
            }
        };

        // Set up the adapter
        inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        clinicVisitsListAdapter = new ParentClinicVisitsListAdapter(getActivity(), factory);

        // Attach the query adapter to the view
        ListView clinicVisitsListView = (ListView) myFragmentView.findViewById(R.id.listClinicVisits);
        clinicVisitsListView.setAdapter(clinicVisitsListAdapter);

        clinicVisitsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Visits clinicVisits = clinicVisitsListAdapter.getItem(position);
                try {
                    showDialogClinicVisits(clinicVisits);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
        int width = getActivity().getResources().getDisplayMetrics().widthPixels;
        int columnWidth = width/3;

        CustomTextView txtHeaderVisitDate = (CustomTextView) myFragmentView.findViewById(R.id.txtHeaderVisitDate);
        CustomTextView txtHeaderInstructions = (CustomTextView) myFragmentView.findViewById(R.id.txtHeaderInstructions);
        CustomTextView txtHeaderNextVisit = (CustomTextView) myFragmentView.findViewById(R.id.txtHeaderNextVisit);

        txtHeaderVisitDate.setWidth(columnWidth);
        txtHeaderInstructions.setWidth(columnWidth);
        txtHeaderNextVisit.setWidth(columnWidth);

        txtHeaderVisitDate.setGravity(Gravity.CENTER);
        txtHeaderInstructions.setGravity(Gravity.CENTER);
        txtHeaderNextVisit.setGravity(Gravity.CENTER);


        return myFragmentView;
    }


    @Override
    public void onMenuPressedCallback() {
        ((DashboardParentFragmentActivity) getActivity()).showMenuContents(getView());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSaveInformation:
//                SaveInformation();
                break;
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void showDialogClinicVisits(Visits patientVisits) throws ParseException {
        final Dialog inflatedView = new Dialog(getActivity(), R.style.DialogSlideAnimZoom);

        inflatedView.setContentView(R.layout.dialog_visit_tracker);
        inflatedView.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        DateFormat df = new SimpleDateFormat("MMMM dd, yyyy");

        ParseFile myPicture = patientVisits.getPhotoFile();

        if(myPicture!=null) {
            Bitmap bMap = BitmapFactory.decodeByteArray(myPicture.getData(), 0, myPicture.getData().length);
            ImageView image = (ImageView) inflatedView.findViewById(R.id.patient_photo);
            image.setBackground(Utils.resizedBitmapDisplayUserPhoto(getActivity(), bMap));
        }

        TextView etPatientsName = (TextView) inflatedView.findViewById(R.id.etPatientsName);
        etPatientsName.setText(patientVisits.getPatientName());

        TextView etAge = (TextView) inflatedView.findViewById(R.id.etAge);
        etAge.setText(patientVisits.getAge());

        TextView etAccompaniedBy = (TextView) inflatedView.findViewById(R.id.etAccompaniedBy);
        etAccompaniedBy.setText(patientVisits.getAccompaniedBy());

        TextView etPurpose = (TextView) inflatedView.findViewById(R.id.etPurpose);
        etPurpose.setText(patientVisits.getPurposeOfVisit());

        // MORE HERE
        // PATIENT NOTES
        // MORE HERE
        TextView etAllergyRisk = (TextView) inflatedView.findViewById(R.id.etAllergyRisk);
        etAllergyRisk.setText(patientVisits.getAllergyRisk());

        TextView etTypeOfDelivery = (TextView) inflatedView.findViewById(R.id.etTypeOfDelivery);
        etTypeOfDelivery.setText(patientVisits.getTypeOfDelivery());


        TextView etInstructions = (TextView) inflatedView.findViewById(R.id.etInstructions);
        etInstructions.setText(patientVisits.getInstructions());

        TextView etNextVisit = (TextView) inflatedView.findViewById(R.id.etNextVisit);
        etNextVisit.setText(df.format(patientVisits.getNextVisit()));

        try {
            String vaccinations = patientVisits.getVaccinations().toString();
            final JSONArray newJArrayVaccinations = new JSONArray(vaccinations);

            ListView vaccinationsTaken = (ListView)inflatedView.findViewById(R.id.vaccinationsTaken);
            final JSONAdapterMedicationsVaccinations jSONAdapterVaccinations = new JSONAdapterMedicationsVaccinations(getActivity(), newJArrayVaccinations);

            vaccinationsTaken.setAdapter(jSONAdapterVaccinations);
            vaccinationsTaken.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    showDialogVaccinations(jSONAdapterVaccinations.getItem(position));
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            String medications = patientVisits.getMedications().toString();
            final JSONArray newJArrayMedications = new JSONArray(medications);

            ListView listMedicationsTaken = (ListView)inflatedView.findViewById(R.id.medicationsTaken);
            final JSONAdapterMedicationsVaccinations jSONAdapterMedications = new JSONAdapterMedicationsVaccinations(getActivity(), newJArrayMedications);

            listMedicationsTaken.setAdapter(jSONAdapterMedications);
            listMedicationsTaken.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    showDialogMedication(jSONAdapterMedications.getItem(position));
                }
            });
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


        ImageButton btnCloseMenuClinicVisit = (ImageButton) inflatedView.findViewById(R.id.btnClosePatientCheckin);
        btnCloseMenuClinicVisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inflatedView.dismiss();
            }
        });

        inflatedView.show();
    }

    public class JSONAdapterMedicationsVaccinations extends BaseAdapter implements ListAdapter {
        private final Activity activity;
        private final JSONArray jsonArray;

        private JSONAdapterMedicationsVaccinations (Activity activity, JSONArray jsonArray) {
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

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void showDialogMedication(JSONObject item) {
        final Dialog dialog = new Dialog(getActivity(), R.style.DialogSlideAnimZoom);

        dialog.setContentView(R.layout.dialog_medicine_tracker);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        LinearLayout layoutGoal = (LinearLayout) dialog.findViewById(R.id.layoutGoal);


        CustomEditTextView etBrandName = (CustomEditTextView) dialog.findViewById(R.id.etBrandName);
        CustomEditTextView etGenericName = (CustomEditTextView) dialog.findViewById(R.id.etGenericName);
        CustomEditTextView etPreparation = (CustomEditTextView) dialog.findViewById(R.id.etPreparation);
        CustomEditTextView etDose = (CustomEditTextView) dialog.findViewById(R.id.etDose);
        CustomEditTextView etFrequency = (CustomEditTextView) dialog.findViewById(R.id.etFrequency);
        CustomEditTextView etDuration = (CustomEditTextView) dialog.findViewById(R.id.etDuration);

        try {
            etBrandName.setText(item.getString("brandname"));
            etGenericName.setText(item.getString("genericname"));
            //etPreparation.setText(item.getString("preparation"));
            etDose.setText(item.getString("dose"));
            etFrequency.setText(item.getString("frequency"));
            etDuration.setText(item.getString("duration"));
            layoutGoal.setVisibility(View.GONE);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        ImageButton btnCloseMenuMedicine = (ImageButton) dialog.findViewById(R.id.btnCloseMenuMedicine);
        btnCloseMenuMedicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void showDialogVaccinations(JSONObject item) {
        final Dialog dialog = new Dialog(getActivity(), R.style.DialogSlideAnimZoom);

        dialog.setContentView(R.layout.dialog_vaccination_tracker);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        LinearLayout layoutGoal = (LinearLayout) dialog.findViewById(R.id.layoutGoal);


        CustomEditTextView etVaccineName = (CustomEditTextView) dialog.findViewById(R.id.etVaccineName);
        CustomEditTextView etDose = (CustomEditTextView) dialog.findViewById(R.id.etDose);
        CustomEditTextView etDateLastTaken = (CustomEditTextView) dialog.findViewById(R.id.etDateLastTaken);
        CustomEditTextView etNextVaccination = (CustomEditTextView) dialog.findViewById(R.id.etNextVaccination);

        try {
            etVaccineName.setText(item.getString("name"));
            etDose.setText(item.getString("vaccine_dose"));

            JSONArray dateTaken = item.getJSONArray("vaccine_datetaken");
            JSONObject dateTakenObject = dateTaken.getJSONObject(0);
            etDateLastTaken.setText(dateTakenObject.getString("iso"));
//            etNextVaccination.setText(item.getString("frequency"));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        ImageButton btnCloseMenuMedicine = (ImageButton) dialog.findViewById(R.id.btnCloseMenuMedicine);
        btnCloseMenuMedicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }


}
