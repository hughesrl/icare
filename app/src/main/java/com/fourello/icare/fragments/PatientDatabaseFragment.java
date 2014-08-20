package com.fourello.icare.fragments;

import android.app.ProgressDialog;
import android.content.Context;
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
import com.fourello.icare.datas.PatientDatabase;
import com.fourello.icare.widgets.ParseProxyObject;
import com.fourello.icare.widgets.Utils;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.ArrayList;
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

        patientDatabaselist = new ArrayList<PatientDatabase>();

        if (mProgressDialog == null) {
            mProgressDialog = Utils.createProgressDialog(getActivity());
            mProgressDialog.show();
        } else {
            mProgressDialog.show();
        }

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(ICareApplication.PATIENTS_LABEL);
        query.whereEqualTo("doctorid", mParamLoginData.getString("linked_doctorid"));
        query.addDescendingOrder("createdAt");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if(e == null) {
                    for (int i = 0; i < parseObjects.size(); i++) {
                        ParseObject patientDatabaseObject = (ParseObject) parseObjects.get(i);

                        final PatientDatabase map = new PatientDatabase();
                        map.setPatientObjectId(patientDatabaseObject.getObjectId());
                        map.setFirtname((String) patientDatabaseObject.get("firstname"));
                        map.setMiddlename((String) patientDatabaseObject.get("middlename"));
                        map.setLastname((String) patientDatabaseObject.get("lastname"));

                        if (patientDatabaseObject.getString("mothercontactnumber") != null && patientDatabaseObject.getString("fathercontactnumber") != null) {
                            contactnumber = patientDatabaseObject.getString("mothercontactnumber") + " / " + patientDatabaseObject.getString("fathercontactnumber");
                        } else if (patientDatabaseObject.getString("mothercontactnumber") != null) {
                            contactnumber = patientDatabaseObject.getString("mothercontactnumber");
                        } else if (patientDatabaseObject.getString("fathercontactnumber") != null) {
                            contactnumber = patientDatabaseObject.getString("fathercontactnumber");
                        }

                        map.setMobilenumbers(contactnumber);

                        if(patientDatabaseObject.has("babypicture")) {
                            final ParseFile myPhoto = (ParseFile) patientDatabaseObject.get("babypicture");
                            myPhoto.getDataInBackground(new GetDataCallback() {
                                @Override
                                public void done(byte[] bytes, ParseException e) {
                                    if (e == null) {
                                        Log.d("test", "We've got data in data.");
                                        // use data for something
                                        map.setPatientphoto(bytes);
                                    } else {
                                        Log.d("test", "There was a problem downloading the data.");
                                    }
                                }
                            });
                        }
                        patientDatabaselist.add(map);
                    }
                    adapter = new PatientDatabaseAdapter(getActivity(), patientDatabaselist);
                    setListAdapter(adapter);
                    getListView().setOnItemClickListener(PatientDatabaseFragment.this);
                    mProgressDialog.dismiss();
                } else {
                    Log.d("RBERT", "There was a problem downloading the data.");
                }
            }
        });

        return myFragmentView;
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        Toast.makeText(getActivity(), "yES", Toast.LENGTH_SHORT)
//                .show();
        showPatientDatabaseActionsDialog(position);
    }

    @Override
    public void onMenuPressedCallback() {
        ((DashboardDoctorFragmentActivity) getActivity()).showMenuContents(getView());
    }

    public void showPatientDatabaseActionsDialog(int position) {
        FragmentActivity activity = (FragmentActivity)(getActivity());
        FragmentManager fm = activity.getSupportFragmentManager();

        PatientDatabaseActionDialogFragment patientDialog = new PatientDatabaseActionDialogFragment();
        Bundle bundle = new Bundle();

        patientDialog.show(fm, "NoticeDialogFragment");
    }



    private class LoadMorePatientDatabaseDataTask extends AsyncTask<Void, Void, Void> {
        Context context;
        ViewGroup viewGroup;

        public LoadMorePatientDatabaseDataTask(Context context, ViewGroup myFragmentView) {
            this.context = context;
            this.viewGroup = myFragmentView;
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
            // Create the array
            patientDatabaselist = new ArrayList<PatientDatabase>();
            try {
                // Locate the class table named "TestLimit" in Parse.com
                ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(ICareApplication.PATIENTS_LABEL);
                query.whereEqualTo("doctorid", mParamLoginData.getString("linked_doctorid"));
                // Set the limit of objects to show
                query.addDescendingOrder("createdAt");
                query.setLimit(limit += 50);
                ob = query.find();
                for (ParseObject patientDatabaseObject : ob) {
                    PatientDatabase map = new PatientDatabase();
                    map.setFirtname((String) patientDatabaseObject.get("firstname"));
                    map.setLastname((String) patientDatabaseObject.get("lastname"));

                    if (patientDatabaseObject.getString("mothercontactnumber") != null && patientDatabaseObject.getString("fathercontactnumber") != null) {
                        contactnumber = patientDatabaseObject.getString("mothercontactnumber") + " / " + patientDatabaseObject.getString("fathercontactnumber");
                    } else if (patientDatabaseObject.getString("mothercontactnumber") != null) {
                        contactnumber = patientDatabaseObject.getString("mothercontactnumber");
                    } else if (patientDatabaseObject.getString("fathercontactnumber") != null) {
                        contactnumber = patientDatabaseObject.getString("fathercontactnumber");
                    }

                    map.setMobilenumbers(contactnumber);
                    patientDatabaselist.add(map);
                }
            } catch (ParseException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // Locate listview last item
            int position = listView.getLastVisiblePosition();
            // Pass the results into ListViewAdapter.java
            adapter = new PatientDatabaseAdapter(context, patientDatabaselist);
            // Binds the Adapter to the ListView
            listView.setAdapter(adapter);
            // Show the latest retrived results on the top
            listView.setSelectionFromTop(position, 0);
            // Close the progressdialog
            mProgressDialog.dismiss();
        }
    }

}
