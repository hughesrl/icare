package com.fourello.icare.widgets;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.fourello.icare.R;

public class AlertDialogFragment extends DialogFragment {
    public static final String PASSWORD_DIALOG_MENU = "open_menu";
    public static final String DOCTOR_ID = "doctorID";
    public static final String PURPOSE_TO_OPEN = "purposeToOpen";

    private ParseProxyObject loginData;
    private int isValid;
    private EditText etCheckInPINOfTheDay;
    private String doctorID;
    private String purposeToOpen;

    public AlertDialogFragment() { }

    /*In order to receive event callback, create a dialog box activity must implement this interface.
     * In case the host need to query the properties dialog box, each method will pass a DialogFragment instance.  */
    public interface AlertDialogListener {
        public void onCheckInDialogPositiveClick(DialogFragment dialog, String purpose, String doctorID, String etPINOfTheDay);
        public void onCheckInDialogNegativeClick(DialogFragment dialog);
    }

    // Examples of the use of this interface to transmit motion events
    AlertDialogListener mListener;

    public static AlertDialogFragment newInstance(String purposeToOpen) {
        AlertDialogFragment f = new AlertDialogFragment();
        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putString(DOCTOR_ID, purposeToOpen);
        args.putString(PURPOSE_TO_OPEN, purposeToOpen);
        f.setArguments(args);
        return f;
    }

    // Override the Fragment.onAttach () method to instantiate NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // To verify whether acrivity inherits the callback interface host
        try {
            // Instantiate a NoticeDialogListener so that we can pass the event to the host
            mListener = (AlertDialogListener) activity;
        } catch (ClassCastException e) {
            // Activity no inheritance interface exception is thrown
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams windowParams = window.getAttributes();

        int width = getResources().getDisplayMetrics().widthPixels - (getResources().getDisplayMetrics().widthPixels/4);
        window.setLayout(width, windowParams.WRAP_CONTENT);
        windowParams.dimAmount = 0.90f;
        windowParams.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(windowParams);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, android.R.style.Theme_Light_Panel);
        setCancelable(false);
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // GET PURPOSE TO OPEN
        doctorID = getArguments().getString(DOCTOR_ID);
        purposeToOpen = getArguments().getString(PURPOSE_TO_OPEN);

//        Toast.makeText(getActivity(), purposeToOpen+" - toOPEN", Toast.LENGTH_LONG).show();
        loginData = (ParseProxyObject) getArguments().getSerializable("loginData");

        final View dialogView = inflater.inflate(R.layout.dialog_alert, container, false);
        LinearLayout displayAlert = (LinearLayout) dialogView.findViewById(R.id.displayAlert);
        LinearLayout displayPINOfTheDay = (LinearLayout) dialogView.findViewById(R.id.displayPINOfTheDay);
        LinearLayout displayOkayAlertPINNotMatch = (LinearLayout) dialogView.findViewById(R.id.displayOkayAlertPINNotMatch);

        if(purposeToOpen.equalsIgnoreCase("alert")) {
            displayAlert.setVisibility(View.VISIBLE);
            displayPINOfTheDay.setVisibility(View.GONE);
            displayOkayAlertPINNotMatch.setVisibility(View.GONE);
        } else if(purposeToOpen.equalsIgnoreCase("error")) {
            displayAlert.setVisibility(View.GONE);
            displayPINOfTheDay.setVisibility(View.GONE);
            displayOkayAlertPINNotMatch.setVisibility(View.VISIBLE);
        } else {
            displayAlert.setVisibility(View.GONE);
            displayPINOfTheDay.setVisibility(View.VISIBLE);
            displayOkayAlertPINNotMatch.setVisibility(View.GONE);
        }
        etCheckInPINOfTheDay = (EditText)dialogView.findViewById(R.id.etCheckInPINOfTheDay);

//        if(!etPINOfTheDay.getText().toString().isEmpty()) {
        Button btnDialogOkayPIN = (Button) dialogView.findViewById(R.id.btnDialogOkayPIN);
        btnDialogOkayPIN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onCheckInDialogPositiveClick(AlertDialogFragment.this, purposeToOpen, doctorID, etCheckInPINOfTheDay.getText().toString());
            }
        });
//        }
        Button btnDialogCancelPIN = (Button)dialogView.findViewById(R.id.btnDialogCancelPIN);
        btnDialogCancelPIN.setOnClickListener(new View.OnClickListener() {
            // DIALOG PASSWORD
            @Override
            public void onClick(View v) {
                mListener.onCheckInDialogNegativeClick(AlertDialogFragment.this);
            }
        });


        Button btnDialogOkayAlertPIN = (Button) dialogView.findViewById(R.id.btnDialogOkayAlertPIN);
        btnDialogOkayAlertPIN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onCheckInDialogPositiveClick(AlertDialogFragment.this, purposeToOpen, "" ,"");
            }
        });

        Button btnDialogOkayAlertPINNotMatch = (Button)dialogView.findViewById(R.id.btnDialogOkayAlertPINNotMatch);
        btnDialogOkayAlertPINNotMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onCheckInDialogPositiveClick(AlertDialogFragment.this, purposeToOpen, doctorID, etCheckInPINOfTheDay.getText().toString());
//                mListener.onDialogNegativeClick(AlertDialogFragment.this);
            }
        });

        return dialogView;
    }


    public int getIsValid() {
        return isValid;
    }
}
