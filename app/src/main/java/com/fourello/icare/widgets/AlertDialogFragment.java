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
    public static final String PASSWORD_DIALOG_CUSTOM = "custom_display";
    public static final String PURPOSE_TO_OPEN = "purposeToOpen";

    private ParseProxyObject loginData;
    private int isValid;
    private EditText etPINOfTheDay;
    private String purposeToOpen;

    public AlertDialogFragment() { }

    /*In order to receive event callback, create a dialog box activity must implement this interface.
     * In case the host need to query the properties dialog box, each method will pass a DialogFragment instance.  */
    public interface AlertDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog, String password, String purpose);
        public void onDialogNegativeClick(DialogFragment dialog);
    }

    // Examples of the use of this interface to transmit motion events
    AlertDialogListener mListener;

    public static AlertDialogFragment newInstance(String purposeToOpen) {
        AlertDialogFragment f = new AlertDialogFragment();
        // Supply num input as an argument.
        Bundle args = new Bundle();
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


    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // GET PURPOSE TO OPEN
        purposeToOpen = getArguments().getString(PURPOSE_TO_OPEN);

//        Toast.makeText(getActivity(), purposeToOpen+" - toOPEN", Toast.LENGTH_LONG).show();
        loginData = (ParseProxyObject) getArguments().getSerializable("loginData");

        final View dialogView = inflater.inflate(R.layout.dialog_alert, container, false);
        LinearLayout displayAlert = (LinearLayout) dialogView.findViewById(R.id.displayAlert);
        LinearLayout displayPINOfTheDay = (LinearLayout) dialogView.findViewById(R.id.displayPINOfTheDay);
        if(purposeToOpen.equalsIgnoreCase("alert")) {
            displayAlert.setVisibility(View.VISIBLE);
            displayPINOfTheDay.setVisibility(View.GONE);
        } else {
            displayAlert.setVisibility(View.GONE);
            displayPINOfTheDay.setVisibility(View.VISIBLE);
        }
        etPINOfTheDay = (EditText)dialogView.findViewById(R.id.etPINOfTheDay);

        Button btnDialogOkayAlertPIN = (Button)dialogView.findViewById(R.id.btnDialogOkayAlertPIN);
        btnDialogOkayAlertPIN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onDialogNegativeClick(AlertDialogFragment.this);
            }
        });
        Button btnDialogOkayPIN = (Button)dialogView.findViewById(R.id.btnDialogOkayPIN);
        btnDialogOkayPIN.setOnClickListener(new View.OnClickListener() {
            // DIALOG PASSWORD
            @Override
            public void onClick(View v) {
//                mListener.onDialogPositiveClick(AlertDialogFragment.this, etPassword.getText().toString(), purposeToOpen);
            }
        });

        return dialogView;
    }


    public int getIsValid() {
        return isValid;
    }
}
