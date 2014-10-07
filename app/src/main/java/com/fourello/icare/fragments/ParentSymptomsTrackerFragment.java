package com.fourello.icare.fragments;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.fourello.icare.DashboardParentFragmentActivity;
import com.fourello.icare.ICareApplication;
import com.fourello.icare.R;
import com.fourello.icare.adapters.ParentSymptomsListAdapter;
import com.fourello.icare.datas.PatientChildData;
import com.fourello.icare.datas.PatientDatabase;
import com.fourello.icare.datas.PatientNotes;
import com.fourello.icare.view.CustomButton;
import com.fourello.icare.view.CustomEditTextView;
import com.fourello.icare.view.CustomTextView;
import com.fourello.icare.widgets.ParseProxyObject;
import com.fourello.icare.widgets.Utils;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class ParentSymptomsTrackerFragment extends Fragment implements
        DashboardParentFragmentActivity.OpenMenuCallbacks,
        View.OnClickListener{

    // TODO: Rename and change types of parameters
    private ParseProxyObject mParamLoginData;
    private ArrayList<PatientChildData> mParamChildData;
    private int mParamChildDataPosition;

    private ViewGroup myFragmentView;

    private ProgressDialog mProgressDialog;

    private static int pos = 0;
    private ParentSymptomsListAdapter symptomsTrackerListAdapter;

    public PatientNotes patientNotes;

    private ListView symptomsTracker;

    public boolean isConnected;


    public static Fragment newInstance(int position, PatientDatabase patientData, ParseProxyObject loginData, String patientObjectId) {
        ParentSymptomsTrackerFragment f = new ParentSymptomsTrackerFragment();

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

        patientNotes = new PatientNotes();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        myFragmentView = (ViewGroup) inflater.inflate(R.layout.fragment_parent_symptoms_tracker, container, false);

        ((DashboardParentFragmentActivity)getActivity()).changePageTitle("My Tracker");

        symptomsTracker = (ListView) myFragmentView.findViewById(R.id.listSymptomsTracker);

        ConnectivityManager cm = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

//        // Set up the Parse query to use in the adapter
//        ParseQueryAdapter.QueryFactory<PatientNotes> factory = new ParseQueryAdapter.QueryFactory<PatientNotes>() {
//            public ParseQuery<PatientNotes> create() {
//                ParseQuery<PatientNotes> query = PatientNotes.getQuery();
//                query.addDescendingOrder("createdAt");
//                query.fromLocalDatastore();
//                return query;
//            }
//        };





        int width = getActivity().getResources().getDisplayMetrics().widthPixels;
        int columnWidth = (width-100)/3;

        CustomTextView txtHeaderDate = (CustomTextView) myFragmentView.findViewById(R.id.txtHeaderDate);
        CustomTextView txtHeaderSubject = (CustomTextView) myFragmentView.findViewById(R.id.txtHeaderSubject);
        CustomTextView txtHeaderStatus = (CustomTextView) myFragmentView.findViewById(R.id.txtHeaderStatus);
        CustomTextView txtHeaderActions = (CustomTextView) myFragmentView.findViewById(R.id.txtHeaderActions);

        txtHeaderDate.setWidth(columnWidth);
        txtHeaderSubject.setWidth(columnWidth);
        txtHeaderStatus.setWidth(columnWidth);
        txtHeaderActions.setWidth(columnWidth);

        txtHeaderDate.setGravity(Gravity.CENTER);
        txtHeaderSubject.setGravity(Gravity.CENTER);
        txtHeaderStatus.setGravity(Gravity.CENTER);
        txtHeaderActions.setGravity(Gravity.CENTER);


        ImageButton addSymptom = (ImageButton) myFragmentView.findViewById(R.id.addSymptom);
        addSymptom.setOnClickListener(this);


        return myFragmentView;
    }

    @Override
    public void onMenuPressedCallback() {
        ((DashboardParentFragmentActivity) getActivity()).showMenuContents(getView());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addSymptom:
                showDialogAddSymptom();
                break;
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void showDialogAddSymptom() {
        final Dialog dialog = new Dialog(getActivity(), R.style.DialogSlideAnimZoom);

        dialog.setContentView(R.layout.dialog_add_new_symptom);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        final CustomEditTextView etSymptomSubject = (CustomEditTextView) dialog.findViewById(R.id.etSymptomSubject);
        final CustomEditTextView etSymptomSymptoms = (CustomEditTextView) dialog.findViewById(R.id.etSymptomSymptoms);
        final CustomEditTextView etSymptomNotes = (CustomEditTextView) dialog.findViewById(R.id.etSymptomNotes);
        final CustomEditTextView etSymptomQuestion = (CustomEditTextView) dialog.findViewById(R.id.etSymptomQuestion);

        CustomButton btnSaveNewSymptom = (CustomButton) dialog.findViewById(R.id.btnSaveNewSymptom);
        btnSaveNewSymptom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConnectivityManager cm = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

                if (mProgressDialog == null) {
                    mProgressDialog = Utils.createProgressDialog(getActivity());
                    mProgressDialog.show();
                } else {
                    mProgressDialog.show();
                }
                final String txtSymptomSubject = etSymptomSubject.getText().toString().trim();
                String txtSymptomSymptoms = etSymptomSymptoms.getText().toString().trim();
                final String txtSymptomNotes = etSymptomNotes.getText().toString().trim();
                final String txtSymptomQuestion = etSymptomQuestion.getText().toString().trim();

                if(txtSymptomSubject.equalsIgnoreCase("") || txtSymptomSymptoms.equalsIgnoreCase("") ||
                        txtSymptomNotes.equalsIgnoreCase("") || txtSymptomQuestion.equalsIgnoreCase("")) {

                    Toast.makeText(getActivity(), "All fields Are Required", Toast.LENGTH_LONG).show();
                } else {
                    patientNotes.setPatientId(mParamChildData.get(mParamChildDataPosition).getPatientObjectId());
                    patientNotes.setSubject(txtSymptomSubject);
                    patientNotes.setNotes(txtSymptomNotes);
                    patientNotes.setQuestion(txtSymptomQuestion);

                    if(isConnected) {
                        patientNotes.saveEventually(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    ParseQuery<PatientNotes> query = PatientNotes.getQuery();
                                    query.whereEqualTo("patientid", mParamChildData.get(mParamChildDataPosition).getPatientObjectId());
                                    query.findInBackground(new FindCallback<PatientNotes>() {
                                        public void done(final List<PatientNotes> patientNotesObject, ParseException e) {
                                            if (e == null) {
                                                try {
                                                    PatientNotes.unpinAll(ICareApplication.PARENT_GROUP_PATIENT_NOTES);
                                                    PatientNotes.pinAllInBackground(ICareApplication.PARENT_GROUP_PATIENT_NOTES, patientNotesObject,
                                                            new SaveCallback() {
                                                                public void done(ParseException e) {
                                                                    if (e == null) {
                                                                        new PopulateSymptomsDataTask() {
                                                                            protected Void doInBackground(Void... params) {
                                                                                dialog.dismiss();
                                                                                super.doInBackground();
                                                                                return null;
                                                                            }
                                                                        }.execute();
                                                                        Log.i("ROBERT PatientNotes", "DATA SAVED : " + patientNotesObject.size());
                                                                    } else {
                                                                        Log.i("ROBERT PatientNotes", "Error pinning PatientNotes: " + e.getMessage());
                                                                    }
                                                                }
                                                            }
                                                    );
                                                } catch (ParseException e1) {
                                                    e1.printStackTrace();
                                                }
                                            } else {
                                                Log.i("ROBERT PatientNotes",
                                                        "loadFromParse: Error finding pinned visits: "
                                                                + e.getMessage());
                                            }
                                        }
                                    });

                                } else {
                                    Log.i("ROBERT PatientNotes saveEventually",
                                            "loadFromParse: Error finding pinned visits: "
                                                    + e.getMessage());
                                }
                            }
                        });
                    } else {
                        patientNotes.saveEventually();
                        Toast.makeText(getActivity(), "No Internet Connection, this will be sync once connected to Internet.", Toast.LENGTH_LONG).show();
                        new PopulateSymptomsDataTask() {
                            protected Void doInBackground(Void... params) {
                                dialog.dismiss();
                                super.doInBackground();
                                return null;
                            }
                        }.execute();

                    }
                }
            }
        });


        ImageButton btnCloseMenuSymptom = (ImageButton) dialog.findViewById(R.id.btnCloseMenuSymptom);
        btnCloseMenuSymptom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public class PopulateSymptomsDataTask extends AsyncTask<Void, Void, Void> {
        private ParseQueryAdapter.QueryFactory<PatientNotes> queryFactory;

        // Override this method to do custom remote calls
        protected Void doInBackground(Void... params) {

            queryFactory = new ParseQueryAdapter.QueryFactory<PatientNotes>() {
                public ParseQuery<PatientNotes> create() {
                    ParseQuery<PatientNotes> query = PatientNotes.getQuery();
                    query.addDescendingOrder("createdAt");
                    query.fromLocalDatastore();
                    return query;
                }
            };


            //queryFactory = factory;

            return null;
        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void result) {
            // Set up the adapter
//            inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            symptomsTrackerListAdapter = new ParentSymptomsListAdapter(getActivity(), queryFactory);

            symptomsTrackerListAdapter.notifyDataSetChanged();
            // Attach the query adapter to the view

            symptomsTracker.setAdapter(symptomsTrackerListAdapter);

//            symptomsTracker.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                }
//            });

            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mProgressDialog == null) {
            mProgressDialog = Utils.createProgressDialog(getActivity());
            mProgressDialog.show();
        } else {
            mProgressDialog.show();
        }
        if(!isConnected) {
            new PopulateSymptomsDataTask().execute();
        } else {
            ParseQuery<PatientNotes> query = PatientNotes.getQuery();
            query.whereEqualTo("patientid", mParamChildData.get(mParamChildDataPosition).getPatientObjectId());
            query.findInBackground(new FindCallback<PatientNotes>() {
                public void done(final List<PatientNotes> patientNotesObject, ParseException e) {
                    if (e == null) {
                        try {
                            PatientNotes.unpinAll(ICareApplication.PARENT_GROUP_PATIENT_NOTES);

                            PatientNotes.pinAllInBackground(ICareApplication.PARENT_GROUP_PATIENT_NOTES, patientNotesObject,
                                    new SaveCallback() {
                                        public void done(ParseException e) {
                                            if (e == null) {
                                                new PopulateSymptomsDataTask().execute();
                                            } else {
                                                Log.i("ROBERT PatientNotes",
                                                        "Error pinning PatientNotes: "
                                                                + e.getMessage());
                                            }
                                        }
                                    });
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }
                    } else {
                        Log.i("ROBERT PatientNotes",
                                "loadFromParse: Error finding pinned visits: "
                                        + e.getMessage());
                    }
                }
            });
        }
    }

    public void updateListSymptoms() {
        symptomsTrackerListAdapter.notifyDataSetChanged();
    }
}
