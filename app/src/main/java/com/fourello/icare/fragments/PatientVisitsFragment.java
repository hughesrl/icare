package com.fourello.icare.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.fourello.icare.ICareApplication;
import com.fourello.icare.R;
import com.fourello.icare.adapters.PatientVisitsAdapter;
import com.fourello.icare.datas.PatientDatabase;
import com.fourello.icare.datas.PatientVisits;
import com.fourello.icare.widgets.FragmentUtils;
import com.fourello.icare.widgets.ParseProxyObject;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PatientVisitsFragment extends ListFragment {
    public static final String ARG_LOGIN_DATA = "loginData";
    private static  PatientDatabase mParamPatientData;
    private static ParseProxyObject mParamLoginData;
    private static String mParamPatientObjectId;




    private static ViewGroup myFragmentView;

    // TODO: Rename and change types of parameters

    private byte[] mParamMyPicture;

    PatientVisitsAdapter adapter;
    private List<PatientVisits> patientVisitsList = null;


    public static Fragment newInstance(int position, PatientDatabase patientData, ParseProxyObject loginData, String patientObjectId) {
        PatientVisitsFragment f = new PatientVisitsFragment();

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

//        patientVisitsList = new ArrayList<PatientVisits>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        myFragmentView = (ViewGroup) inflater.inflate(R.layout.fragment_patient_visits, container, false);

        patientVisitsList = new ArrayList<PatientVisits>();
        patientVisitsList.clear();
            /* Adding the Visits */
        ParseQuery<ParseObject> queryVisits = new ParseQuery<ParseObject>(ICareApplication.VISITS_LABEL);
        queryVisits.setSkip(0);
        queryVisits.whereEqualTo("patientid", mParamPatientObjectId);
        queryVisits.addDescendingOrder("createdAt");
        queryVisits.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjectsVisits, ParseException e) {
                Log.d("HUGHES", "DONE");
                DateFormat df = new SimpleDateFormat("MMMM dd, yyyy");
                if (e == null) {
                    if (parseObjectsVisits.size() > 0) {
                        for (int i = 0; i < parseObjectsVisits.size(); i++) {
                            ParseObject patientDatabaseObject = parseObjectsVisits.get(i);

                            PatientVisits visitMap = new PatientVisits();

                            visitMap.setPatientObjectId(patientDatabaseObject.getObjectId());

                            visitMap.setAccompaniedBy(patientDatabaseObject.getString("accompaniedby"));
                            visitMap.setAge(patientDatabaseObject.getString("age"));

                            if(patientDatabaseObject.containsKey("allergyrisk")) {
                                visitMap.setAllergyrisk(patientDatabaseObject.getString("allergyrisk"));
                            }
                            visitMap.setChest(patientDatabaseObject.getString("chest"));
                            visitMap.setDoctorid(patientDatabaseObject.getString("doctorid"));
                            visitMap.setEmail(patientDatabaseObject.getString("email"));
                            visitMap.setHead(patientDatabaseObject.getString("head"));
                            visitMap.setHeight(patientDatabaseObject.getString("height"));
                            visitMap.setInstructions(patientDatabaseObject.getString("instructions"));

                            if(patientDatabaseObject.containsKey("medications")) {
                                visitMap.setMedications(patientDatabaseObject.getJSONArray("medications").toString());
                            }
                            visitMap.setNextvisit(patientDatabaseObject.getString("nextvisit"));
                            visitMap.setPatientid(patientDatabaseObject.getString("patientid"));
                            visitMap.setPatientname(patientDatabaseObject.getString("patientname"));
                            visitMap.setPersonal_notes(patientDatabaseObject.getString("personal_notes"));

                            ParseFile myPhoto = (ParseFile)patientDatabaseObject.get("photoFile");
                            if(myPhoto!=null) {
                                try {
                                    byte[] data = myPhoto.getData();
                                    visitMap.setPhotoFile(data);
                                } catch (ParseException e2) {
                                    // TODO Auto-generated catch block
                                    e2.printStackTrace();
                                }
                            }
                            visitMap.setPupose_of_visit(patientDatabaseObject.getString("purpose_of_visit"));
                            visitMap.setQuestion_for_doctor(patientDatabaseObject.getString("question_for_doctor"));
                            visitMap.setRelationship_to_patient(patientDatabaseObject.getString("relationship_to_patient"));
                            visitMap.setTemperature(patientDatabaseObject.getString("temperature"));
                            visitMap.setTypeofdelivery(patientDatabaseObject.getString("typeofdelivery"));
                            if(patientDatabaseObject.containsKey("vaccinations")) {
                                visitMap.setVaccinations(patientDatabaseObject.getJSONArray("vaccinations").toString());
                            }
                            visitMap.setWeight(patientDatabaseObject.getString("weight"));

                            Date visitDateParse = patientDatabaseObject.getCreatedAt();
                            String visitDate = df.format(visitDateParse);
                            visitMap.setVisitDate(visitDate);

                            Log.d(ICareApplication.VISITS_LABEL + "  VISIT Found", visitDate);

                            patientVisitsList.add(visitMap);
                        }
                        adapter = new PatientVisitsAdapter(getActivity(), patientVisitsList);
                        setListAdapter(adapter);

                        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                VisitTrackerDialogFragment visitTrackerDialogFragment = new VisitTrackerDialogFragment();
                                visitTrackerDialogFragment.setVisitContent(patientVisitsList.get(i));
                                visitTrackerDialogFragment.show(getChildFragmentManager(), "fragment_visit_tracker");
                            }
                        });
                    }
                }
            }
        });

        return myFragmentView;
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        FragmentUtils.startActivityForResultWhileSavingOrigin(this, requestCode, intent, null);
    }

}
