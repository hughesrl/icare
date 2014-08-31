package com.fourello.icare.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fourello.icare.DashboardDoctorFragmentActivity;
import com.fourello.icare.ICareApplication;
import com.fourello.icare.R;
import com.fourello.icare.datas.Settings;
import com.fourello.icare.datas.SpinnerItems;
import com.fourello.icare.view.CustomButton;
import com.fourello.icare.view.CustomEditTextView;
import com.fourello.icare.view.CustomTextView;
import com.fourello.icare.widgets.ParseProxyObject;
import com.fourello.icare.widgets.PasswordDialogFragment;
import com.fourello.icare.widgets.Utils;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SettingsFragment extends Fragment implements
        DashboardDoctorFragmentActivity.OpenMenuCallbacks {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private String newUserRecordObjectId;
    private String newPatientRecordObjectId;

    public static final String ARG_LOGIN_DATA = "loginData";
    public static final String ARG_SETTING_OBJECT_ID = "setting_object_id";
    private ParseProxyObject mParamLoginData;
    private ProgressDialog mProgressDialog;

    private OnFragmentInteractionListener mListener;

    private View myFragmentView;
    private CustomTextView lblCheckingPIN;
    private CustomTextView lblPINOfTheDay;
    private CustomEditTextView etPINOfTheDay;
    private CustomButton btnGetPINOfTheDay;


    private String doctorId;
    private String settingsObjectId = "";

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SchedulerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_LOGIN_DATA, param1);
        fragment.setArguments(args);
        return fragment;
    }
    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParamLoginData = (ParseProxyObject) getArguments().getSerializable(ARG_LOGIN_DATA);

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myFragmentView = inflater.inflate(R.layout.fragment_settings, container, false);

        ((DashboardDoctorFragmentActivity)getActivity()).changePageTitle(getString(R.string.title_settings));

        lblCheckingPIN = (CustomTextView) myFragmentView.findViewById(R.id.lblCheckingPIN);

        lblPINOfTheDay = (CustomTextView) myFragmentView.findViewById(R.id.lblPINOfTheDay);
        etPINOfTheDay = (CustomEditTextView) myFragmentView.findViewById(R.id.etPINOfTheDay);
        btnGetPINOfTheDay = (CustomButton) myFragmentView.findViewById(R.id.btnGetPINOfTheDay);

        doctorId = mParamLoginData.getString("linked_doctorid");


        new CheckPINOfTheDayDataTask(getActivity(), myFragmentView).execute();
        btnGetPINOfTheDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mProgressDialog == null) {
                    mProgressDialog = Utils.createProgressDialog(getActivity());
                    mProgressDialog.show();
                } else {
                    mProgressDialog.show();
                }

                // Adding Contents from Users Class
                ParseQuery<ParseObject> checkSettings = new ParseQuery<ParseObject>(ICareApplication.SETTINGS_LABEL);
                checkSettings.whereEqualTo("doctorid", doctorId);
                try {
                    if(checkSettings.find().size() > 0) {
                        settingsObjectId = checkSettings.getFirst().getObjectId();
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                final ArrayList<String> pins = new ArrayList<String>();
                ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(ICareApplication.SETTINGS_LABEL);
                query.findInBackground(new FindCallback<ParseObject>() {

                    @Override
                    public void done(List<ParseObject> parseObjects, ParseException e) {
                        if (e == null) {
                            for (int i = 0; i < parseObjects.size(); i++) {
                                ParseObject patientQueueObject = parseObjects.get(i);
                                if (patientQueueObject.has("pin")) {
                                    pins.add(patientQueueObject.getString("pin"));
                                }
                            }
                            final String RandomPINOfTheDay = Utils.getRandomString(5, pins);
                            ParseObject settings;
                            if(settingsObjectId.equalsIgnoreCase("")) {
                                settings = ParseObject.create(ICareApplication.SETTINGS_LABEL);
                            } else {
                                settings = ParseObject.createWithoutData(ICareApplication.SETTINGS_LABEL, settingsObjectId);
                            }
                            settings.put("doctorid", doctorId);
                            settings.put("pin", RandomPINOfTheDay);

                            settings.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException settingsError) {
                                    if (settingsError == null) {
                                        mProgressDialog.dismiss();
                                        lblCheckingPIN.setVisibility(View.GONE);
                                        etPINOfTheDay.setText(RandomPINOfTheDay);
                                        lblPINOfTheDay.setVisibility(View.VISIBLE);
                                        etPINOfTheDay.setVisibility(View.VISIBLE);
                                        btnGetPINOfTheDay.setVisibility(View.GONE);
                                    } else {
                                        Toast.makeText(
                                                getActivity().getApplicationContext(),
                                                "Error saving: " + settingsError.getMessage(),
                                                Toast.LENGTH_SHORT).show();
                                        Log.d("Error saving: ", settingsError.getMessage());
                                    }
                                }
                            });
                        }


                    }
                });
            }
        });
        return myFragmentView;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) { //make sure fragment codes match up {
            String editText = data.getStringExtra("password");
            //Toast.makeText(getActivity(), editText, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
//        try {
//            mListener = (OnFragmentInteractionListener) activity;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString() + " must implement onViewSelected");
//        }
    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(int isValid);
    }

    @Override
    public void onMenuPressedCallback() {
        ((DashboardDoctorFragmentActivity) getActivity()).showMenuContents(getView());
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

    private class CheckPINOfTheDayDataTask extends AsyncTask<Void, Void, Void> {
        Context context;
//        View viewGroup;

        public CheckPINOfTheDayDataTask(Context context, View myFragmentView) {
            this.context = context;
//            this.viewGroup = myFragmentView;
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
            Date midnight = new Date();
            midnight.setHours(0);
            midnight.setMinutes(0);
            midnight.setSeconds(0);

            Date elevenfiftynine = new Date();
            elevenfiftynine.setHours(23);
            elevenfiftynine.setMinutes(59);
            elevenfiftynine.setSeconds(59);

            // Adding Contents from Users Class
            ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(ICareApplication.SETTINGS_LABEL);
            query.whereEqualTo("doctorid", doctorId);
            query.whereGreaterThan("updatedAt", midnight);
            query.whereLessThan("updatedAt", elevenfiftynine);
            try {
                if(query.find().size() == 0) { // No PIN
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            lblCheckingPIN.setVisibility(View.GONE);
                            lblPINOfTheDay.setVisibility(View.GONE);
                            etPINOfTheDay.setVisibility(View.GONE);
                            btnGetPINOfTheDay.setVisibility(View.VISIBLE);
                        }
                    });

                } else {
                    query.getFirstInBackground(new GetCallback<ParseObject>() {
                        @Override
                        public void done(ParseObject parseObject, ParseException e) {
                            if(e == null) {
                                if(parseObject.has("pin")) {
                                    etPINOfTheDay.setText(parseObject.getString("pin"));

                                    lblCheckingPIN.setVisibility(View.GONE);
                                    lblPINOfTheDay.setVisibility(View.VISIBLE);
                                    etPINOfTheDay.setVisibility(View.VISIBLE);
                                    btnGetPINOfTheDay.setVisibility(View.GONE);
                                }
                            }
                        }
                    });
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            mProgressDialog.dismiss();
        }
    }
}
