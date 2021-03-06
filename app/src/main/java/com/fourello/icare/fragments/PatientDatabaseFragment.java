package com.fourello.icare.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.fourello.icare.DashboardDoctorFragmentActivity;
import com.fourello.icare.ICareApplication;
import com.fourello.icare.R;
import com.fourello.icare.adapters.PatientDatabaseAdapter;
import com.fourello.icare.adapters.PatientVisitsAdapter;
import com.fourello.icare.datas.PatientDatabase;
import com.fourello.icare.datas.PatientVisits;
import com.fourello.icare.widgets.ParseProxyObject;
import com.fourello.icare.widgets.Utils;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Todo IMPORTANT
 * Patient Database Listing and Functionalities
 */

public class PatientDatabaseFragment extends ListFragment implements
        AdapterView.OnItemClickListener,
        /*CheckinPatientDialogFragment.CheckinPatientDialogListener,*/
        DashboardDoctorFragmentActivity.OpenMenuCallbacks{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_LOGIN_DATA = "loginData";
    private static final String ARG_MY_PICTURE = "myPicture";

    // TODO: Rename and change types of parameters
    private ParseProxyObject mParamLoginData;
    private byte[] mParamMyPicture;

    private ListView listView;

    private String contactnumber = "";

    List<ParseObject> ob;
    ProgressDialog mProgressDialog;
    PatientDatabaseAdapter adapter;
    private List<PatientDatabase> patientDatabaselist = null;
    private List<PatientVisits> patientVisitsList = null;
    // Set the limit of objects to show
    private int limit = 50;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SchedulerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PatientDatabaseFragment newInstance(String param1, String param2) {
        PatientDatabaseFragment fragment = new PatientDatabaseFragment();
        Bundle args = new Bundle();
        args.putString(ARG_LOGIN_DATA, param1);
        args.putString(ARG_MY_PICTURE, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public PatientDatabaseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
//        Toast.makeText(getActivity(), "ON RESUME", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParamLoginData = (ParseProxyObject) getArguments().getSerializable(ARG_LOGIN_DATA);
            mParamMyPicture = getArguments().getByteArray(ARG_MY_PICTURE);
        }
    }

//    @Override
//    public void onActivityCreated(Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//
//        adapter = new PatientDatabaseAdapter(getActivity(), patientDatabaselist);
//        setListAdapter(adapter);
//        getListView().setOnItemClickListener(PatientDatabaseFragment.this);
//        mProgressDialog.dismiss();
//    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup myFragmentView = (ViewGroup) inflater.inflate(R.layout.fragment_patient_database, container, false);

        ((DashboardDoctorFragmentActivity)getActivity()).changePageTitle(getString(R.string.title_my_patients));
        // The content view embeds two fragments; now retrieve them and attach
        // their "hide" button.
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        int accessType = Integer.parseInt(mParamLoginData.getString("type"));
//        new LoadPatientDatabaseDataTask(getActivity(), myFragmentView).execute();

        patientDatabaselist = new ArrayList<PatientDatabase>();


        if (mProgressDialog == null) {
            mProgressDialog = Utils.createProgressDialog(getActivity());
            mProgressDialog.show();
        } else {
            mProgressDialog.show();
        }

        final int patientsCount = 0;
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(ICareApplication.PATIENTS_LABEL);
        query.whereEqualTo("doctorid", mParamLoginData.getString("linked_doctorid"));
        query.addAscendingOrder("lastname");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                DateFormat df = new SimpleDateFormat("MMM dd, yyyy");
                if (e == null) {

                    for (int i = 0; i < parseObjects.size(); i++) {
                        final ParseObject patientDatabaseObject = parseObjects.get(i);

                        final PatientDatabase map = new PatientDatabase();
                        map.setPatientObjectId(patientDatabaseObject.getObjectId());
                        map.setFirtname((String) patientDatabaseObject.get("firstname"));
                        map.setMiddlename((String) patientDatabaseObject.get("middlename"));
                        map.setLastname((String) patientDatabaseObject.get("lastname"));

                        if (patientDatabaseObject.getDate("dateofbirth") != null) {
                            Date dateOfBirthParse = patientDatabaseObject.getDate("dateofbirth");
                            String dateOfBirth = df.format(dateOfBirthParse);
                            map.setbDate(dateOfBirth);
                        }
                        if (patientDatabaseObject.getString("placeofbirth") != null) {
                            map.setbPlace(patientDatabaseObject.getString("placeofbirth"));
                        }
                        if (patientDatabaseObject.getString("deliveredby") != null) {
                            map.setDrName(patientDatabaseObject.getString("deliveredby"));
                        }
                        if (patientDatabaseObject.getString("typeofdelivery") != null) {
                            map.setDeliveryType(patientDatabaseObject.getString("typeofdelivery"));
                        }
                        if (patientDatabaseObject.getString("weight") != null) {
                            map.setpWeight(patientDatabaseObject.getString("weight"));
                        }
                        if (patientDatabaseObject.getString("length") != null) {
                            map.setpHeight(patientDatabaseObject.getString("length"));
                        }
                        if (patientDatabaseObject.getString("headcircumference") != null) {
                            map.setpHead(patientDatabaseObject.getString("headcircumference"));
                        }
                        if (patientDatabaseObject.getString("chestcircumference") != null) {
                            map.setpChest(patientDatabaseObject.getString("chestcircumference"));
                        }
                        if (patientDatabaseObject.getString("abdomencircumference") != null) {
                            map.setpAbdomen(patientDatabaseObject.getString("abdomencircumference"));
                        }
                        if (patientDatabaseObject.getString("allergyrisk") != null) {
                            map.setAllergyRisk(patientDatabaseObject.getString("allergyrisk"));
                        }
                        if (patientDatabaseObject.getDate("circumcisedon") != null) {
                            Date circumcisedon = patientDatabaseObject.getDate("circumcisedon");
                            String circumcisedOn = df.format(circumcisedon);
                            map.setpCircumcisedOn(circumcisedOn);
                        }
                        if (patientDatabaseObject.getDate("earpiercedon") != null) {
                            map.setpEarPiercedOn(patientDatabaseObject.getString("earpiercedon"));
                            Date earpiercedon = patientDatabaseObject.getDate("earpiercedon");
                            String earpiercedOn = df.format(earpiercedon);
                            map.setpCircumcisedOn(earpiercedOn);
                        }

                        if (patientDatabaseObject.getString("email") != null) {
                            map.setParentEmail(patientDatabaseObject.getString("email"));
                        }
                        // Distinguishing Marks cannot found

                        // Newborn Screening cannot found

                        // Vaccinations Given cannot found

                        /* MOTHER */
                        if (patientDatabaseObject.getString("motherfirstname") != null) {
                            map.setpMomsFname(patientDatabaseObject.getString("motherfirstname"));
                        }
                        if (patientDatabaseObject.getString("mothermiddlename") != null) {
                            map.setpMomsMname(patientDatabaseObject.getString("mothermiddlename"));
                        }
                        if (patientDatabaseObject.getString("motherlastname") != null) {
                            map.setpMomsLname(patientDatabaseObject.getString("motherlastname"));
                        }
                        if (patientDatabaseObject.getString("mothercompany") != null) {
                            map.setpMomsWorkPlace(patientDatabaseObject.getString("mothercompany"));
                        }
                        if (patientDatabaseObject.getString("motherprofession") != null) {
                            map.setpMomsWorkAs(patientDatabaseObject.getString("motherprofession"));
                        }
                        if (patientDatabaseObject.getString("motherhmo") != null) {
                            map.setpMomsHMO(patientDatabaseObject.getString("motherhmo"));
                        }

                        /* FATHER */
                        if (patientDatabaseObject.getString("fatherfirstname") != null) {
                            map.setpDadsFname(patientDatabaseObject.getString("fatherfirstname"));
                        }
                        if (patientDatabaseObject.getString("fathermiddlename") != null) {
                            map.setpDadsMname(patientDatabaseObject.getString("fathermiddlename"));
                        }
                        if (patientDatabaseObject.getString("fatherlastname") != null) {
                            map.setpDadsLname(patientDatabaseObject.getString("fatherlastname"));
                        }
                        if (patientDatabaseObject.getString("fathercompany") != null) {
                            map.setpDadsWorkPlace(patientDatabaseObject.getString("fathercompany"));
                        }
                        if (patientDatabaseObject.getString("fatherprofession") != null) {
                            map.setpDadsWorkAs(patientDatabaseObject.getString("fatherprofession"));
                        }
                        if (patientDatabaseObject.getString("fatherhmo") != null) {
                            map.setpDadsHMO(patientDatabaseObject.getString("fatherhmo"));
                        }

                        /* ADDRESS */
                        if (patientDatabaseObject.getString("address_1") != null) {
                            map.setpAddress1(patientDatabaseObject.getString("address_1"));
                        }
                        if (patientDatabaseObject.getString("address_2") != null) {
                            map.setpAddress2(patientDatabaseObject.getString("address_2"));
                        }

                        if (patientDatabaseObject.getString("mothercontactnumber") != null && patientDatabaseObject.getString("fathercontactnumber") != null) {
                            contactnumber = patientDatabaseObject.getString("mothercontactnumber") + " / " + patientDatabaseObject.getString("fathercontactnumber");
                        } else if (patientDatabaseObject.getString("mothercontactnumber") != null) {
                            contactnumber = patientDatabaseObject.getString("mothercontactnumber");
                        } else if (patientDatabaseObject.getString("fathercontactnumber") != null) {
                            contactnumber = patientDatabaseObject.getString("fathercontactnumber");
                        }


                        map.setMobilenumbers(contactnumber);

                        if(patientDatabaseObject.has("photoFile")) {
                            ParseFile myPhoto = patientDatabaseObject.getParseFile("photoFile");
                            if (myPhoto != null) {
                                map.setPhotoFileUrl(myPhoto.getUrl());
                                try {
                                    byte[] data = myPhoto.getData();
                                    map.setPatientphoto(data);
                                } catch (ParseException e2) {
                                    // TODO Auto-generated catch block
                                    e2.printStackTrace();
                                }
                            }
                        }
                        patientDatabaselist.add(map);
                    }

                } else {
                    Log.d("RBERT", "There was a problem downloading the data.");
                }
                adapter = new PatientDatabaseAdapter(getActivity(), patientDatabaselist);
                setListAdapter(adapter);
                getListView().setOnItemClickListener(PatientDatabaseFragment.this);
                mProgressDialog.dismiss();
            }
        });



        return myFragmentView;
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        PatientDatabase itemAtPosition = patientDatabaselist.get(position);

        showPatientDatabaseActionsDialog(itemAtPosition, (ArrayList<PatientDatabase>) patientDatabaselist, position);
    }


    @Override
    public void onMenuPressedCallback() {
        ((DashboardDoctorFragmentActivity) getActivity()).showMenuContents(getView());
    }

    public void showPatientDatabaseActionsDialog(PatientDatabase patientDatabase, ArrayList<PatientDatabase> patientDatabaselist, int position) {
        ((DashboardDoctorFragmentActivity) getActivity()).PatientDatabaseActions(patientDatabase, patientDatabaselist, position);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
    }

//    private class LoadPatientDatabaseDataTask extends AsyncTask<Void, Void, Void> {
//        Context context;
//        ViewGroup viewGroup;
//
//        public LoadPatientDatabaseDataTask(Context context, ViewGroup myFragmentView) {
//            this.context = context;
//            this.viewGroup = myFragmentView;
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            // Create a progressdialog
//            if (mProgressDialog == null) {
//                mProgressDialog = Utils.createProgressDialog(context);
//                mProgressDialog.show();
//            } else {
//                mProgressDialog.show();
//            }
//        }
//
//        @Override
//        protected Void doInBackground(Void... params) {
//            // Create the array
//
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void result) {
//            adapter = new PatientDatabaseAdapter(getActivity(), patientDatabaselist);
//            setListAdapter(adapter);
//            getListView().setOnItemClickListener(PatientDatabaseFragment.this);
//            mProgressDialog.dismiss();
//        }
//    }

}
