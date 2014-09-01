package com.fourello.icare.widgets;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fourello.icare.R;

public class PasswordDialogFragment extends DialogFragment {
    public static final String PASSWORD_DIALOG_MENU = "open_menu";
    public static final String PASSWORD_DIALOG_CUSTOM = "custom_display";
    public static final String PURPOSE_TO_OPEN = "purposeToOpen";

    private ParseProxyObject loginData;
    private int isValid;
    private EditText etPassword;
    private String purposeToOpen;

    public PasswordDialogFragment() { }

    /*In order to receive event callback, create a dialog box activity must implement this interface.
     * In case the host need to query the properties dialog box, each method will pass a DialogFragment instance.  */
    public interface PasswordDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog, String password, String purpose);
        public void onDialogNegativeClick(DialogFragment dialog);
    }

    // Examples of the use of this interface to transmit motion events
    PasswordDialogListener mListener;

    public static PasswordDialogFragment newInstance(String purposeToOpen) {
        PasswordDialogFragment f = new PasswordDialogFragment();
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
            mListener = (PasswordDialogListener) getTargetFragment();
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

        final View dialogView = inflater.inflate(R.layout.dialog_password, container, false);
        etPassword = (EditText)dialogView.findViewById(R.id.password);

        Button btnCancel = (Button)dialogView.findViewById(R.id.btnDialogCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                dialogPassword.dismiss();
                mListener.onDialogNegativeClick(PasswordDialogFragment.this);
            }
        });
        Button btnOkay = (Button)dialogView.findViewById(R.id.btnDialogOkay);
        btnOkay.setOnClickListener(new View.OnClickListener() {
            // DIALOG PASSWORD
            @Override
            public void onClick(View v) {
                mListener.onDialogPositiveClick(PasswordDialogFragment.this, etPassword.getText().toString(), purposeToOpen);
            }
        });

        return dialogView;
    }


    public int getIsValid() {
        return isValid;
    }
}
