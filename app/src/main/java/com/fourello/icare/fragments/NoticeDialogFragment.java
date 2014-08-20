package com.fourello.icare.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fourello.icare.R;

public class NoticeDialogFragment extends DialogFragment {

    /*In order to receive event callback, create a dialog box activity must implement this interface.
     * In case the host need to query the properties dialog box, each method will pass a DialogFragment instance.  */
    public interface NoticeDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
    }

    // Examples of the use of this interface to transmit motion events
    NoticeDialogListener mListener;

    public static NoticeDialogFragment newInstance(int num) {
        NoticeDialogFragment f = new NoticeDialogFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("num", num);
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
            mListener = (NoticeDialogListener) activity;
        } catch (ClassCastException e) {
            // Activity no inheritance interface exception is thrown
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
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

        View view = inflater.inflate(R.layout.dialog_password,
                container, false);

        // declare your UI elements here
        // For example:
        // Button mButton =  view.findViewById(R.id.dialog_button);
        // mButton.setOnClickListener(this);

        return view;
    }
}
