package com.fourello.icare.fragments;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;

import com.fourello.icare.DashboardDoctorFragmentActivity;
import com.fourello.icare.ICareApplication;
import com.fourello.icare.R;
import com.fourello.icare.adapters.PatientQueueAdapter;
import com.fourello.icare.datas.PatientQueue;
import com.fourello.icare.widgets.ParseProxyObject;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//import com.fourello.icare.Dashboard_DoctorActivity;

public class PatientQueueFragment extends ListFragment implements
        AdapterView.OnItemClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String ARG_LOGIN_DATA = "mParamLoginData";
    public static final String ARG_DOCTOR_ID = "mParamDoctorId";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ParseProxyObject mParamLoginData;
    private String mParamDoctorId;
    private PatientQueueAdapter adapter;
    private ArrayList<PatientQueue> patientQueueList;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PatientQueueFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PatientQueueFragment newInstance(String param1, String param2) {
        PatientQueueFragment fragment = new PatientQueueFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public PatientQueueFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParamLoginData = (ParseProxyObject) getArguments().getSerializable(ARG_LOGIN_DATA);
            mParamDoctorId = getArguments().getString(ARG_DOCTOR_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup myFragmentView = (ViewGroup) inflater.inflate(R.layout.fragment_patient_queue, container, false);

        final ProgressBar progressBar1 = (ProgressBar) myFragmentView.findViewById(R.id.progressBar1);

        progressBar1.setVisibility(View.VISIBLE);
        ((DashboardDoctorFragmentActivity) getActivity()).changePageTitle(getString(R.string.title_my_patients));
        // The content view embeds two fragments; now retrieve them and attach
        // their "hide" button.
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();


        patientQueueList = new ArrayList<PatientQueue>();

        Date midnight = new Date();
        midnight.setHours(0);
        midnight.setMinutes(0);
        midnight.setSeconds(0);

        Date elevenfiftynine = new Date();
        elevenfiftynine.setHours(23);
        elevenfiftynine.setMinutes(59);
        elevenfiftynine.setSeconds(59);

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(ICareApplication.CLINIC_SURVEY_LABEL);
        query.whereEqualTo("doctorid", mParamDoctorId);
        query.whereGreaterThan("createdAt", midnight);
        query.whereLessThan("createdAt", elevenfiftynine);
//        query.addDescendingOrder("createdAt");
        query.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (e == null) {
                    for (int i = 0; i < parseObjects.size(); i++) {
                        ParseObject patientQueueObject = parseObjects.get(i);

                        final PatientQueue map = new PatientQueue();
                        map.setPatientObjectId(patientQueueObject.getObjectId());
                        map.setPatientName(patientQueueObject.getString("patientname"));

                        ParseFile myPhoto = (ParseFile)patientQueueObject.get("photoFile");
                        if(myPhoto!=null) {
                            try {
                                byte[] data = myPhoto.getData();
                                map.setPatientphoto(data);
                            } catch (ParseException e2) {
                                // TODO Auto-generated catch block
                                e2.printStackTrace();
                            }
                        }
                        patientQueueList.add(map);
                    }

                    adapter = new PatientQueueAdapter(getActivity(), patientQueueList);
                    setListAdapter(adapter);
                    getListView().setOnItemClickListener(PatientQueueFragment.this);
                    getListView().setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            int action = event.getAction();
                            switch (action) {
                                case MotionEvent.ACTION_DOWN:
                                    // Disallow ScrollView to intercept touch events.
                                    v.getParent().requestDisallowInterceptTouchEvent(true);
                                    break;

                                case MotionEvent.ACTION_UP:
                                    // Allow ScrollView to intercept touch events.
                                    v.getParent().requestDisallowInterceptTouchEvent(false);
                                    break;
                            }

                            // Handle ListView touch events.
                            v.onTouchEvent(event);
                            return true;
                        }
                    });
                    progressBar1.setVisibility(View.GONE);
                }
            }
        });

        return myFragmentView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        Toast.makeText(getActivity(), "yES", Toast.LENGTH_SHORT)
//                .show();
        PatientQueue itemAtPosition = patientQueueList.get(position);

//        showPatientDatabaseActionsDialog(itemAtPosition, patientQueueList);
    }

}
