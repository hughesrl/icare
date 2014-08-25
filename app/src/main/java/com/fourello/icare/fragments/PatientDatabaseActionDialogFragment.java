package com.fourello.icare.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fourello.icare.R;
//import com.fourello.icare.adapters.ViewPagerAdapter;
import com.fourello.icare.datas.PatientDatabase;
import com.fourello.icare.datas.SpinnerItems;
import com.fourello.icare.view.RoundedImageView;
import com.fourello.icare.widgets.Utils;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PatientDatabaseActionDialogFragment extends DialogFragment {

    public static final String ARG_PATIENT_RECORD_OBJECT_ID = "mParamPatientRecordObjectId";
    public static final String ARG_PATIENT_FULL_NAME = "mParamPatientFullName";
    public static final String ARG_PATIENT_PHOTO = "mParamPatientPhoto";

    public static final String ARG_PATIENT_ARRAY_LIST_POSITION = "mParamPatientArrayListPosition";

    private String mParamPatientRecordObjectId;
    private String mParamPatientFullName;
    private byte[] mParamPatientPhoto;
    private int mParamPatientArrayListPosition;

    private ProgressDialog mProgressDialog;
    //keep track of camera capture intent
    private final int CAMERA_CAPTURE = 1;
    //keep track of cropping intent
    private final int PIC_CROP = 2;
    //captured picture uri
    private Uri picUri;

    private ViewPager mViewPager;
    private SectionsPagerAdapter mViewPagerAdapter;
    private View inflatedView;
    private byte[] bytearray;

    private List<PatientDatabase> patientDatabaseArrayList;

    private String[] TITLES = {"aaa", "bbb", "ccc"};


    /*In order to receive event callback, create a dialog box activity must implement this interface.
         * In case the host need to query the properties dialog box, each method will pass a DialogFragment instance.  */
    public interface CheckinPatientDialogListener {
        public void onDialogDoneClick(DialogFragment dialog);
        public void onDialogCancelClick(DialogFragment dialog);


    }

    // Examples of the use of this interface to transmit motion events
    CheckinPatientDialogListener mListener;

    public static PatientDatabaseActionDialogFragment newInstance(String mParamPatientRecordObjectId,
                                                           String mParamPatientFullName,
                                                           byte[] mParamPatientPhoto,
                                                           int mParamPatientArrayListPosition) {
        PatientDatabaseActionDialogFragment f = new PatientDatabaseActionDialogFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putString(ARG_PATIENT_RECORD_OBJECT_ID, mParamPatientRecordObjectId);
        args.putString(ARG_PATIENT_FULL_NAME, mParamPatientFullName);
        args.putByteArray(ARG_PATIENT_PHOTO, mParamPatientPhoto);
        args.putInt(ARG_PATIENT_ARRAY_LIST_POSITION, mParamPatientArrayListPosition);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams windowParams = window.getAttributes();
        windowParams.dimAmount = 0.90f;
        windowParams.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(windowParams);
    }
    // Override the Fragment.onAttach () method to instantiate NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // To verify whether acrivity inherits the callback interface host
        try {
            // Instantiate a NoticeDialogListener so that we can pass the event to the host
            mListener = (CheckinPatientDialogListener) getTargetFragment();
        } catch (ClassCastException e) {
            // Activity no inheritance interface exception is thrown
            throw new ClassCastException(activity.toString()
                    + " must implement CheckinPatientDialogListener");
        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, android.R.style.Theme_Light_Panel);
        if (getArguments() != null) {
            mParamPatientRecordObjectId = getArguments().getString(ARG_PATIENT_RECORD_OBJECT_ID);
            mParamPatientFullName = getArguments().getString(ARG_PATIENT_FULL_NAME);
            mParamPatientPhoto = getArguments().getByteArray(ARG_PATIENT_PHOTO);
            mParamPatientArrayListPosition = getArguments().getInt(ARG_PATIENT_ARRAY_LIST_POSITION);
        }

    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        inflatedView = inflater.inflate(R.layout.dialog_patient_database_actions, container, true);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bg_image);
        ImageView image = (ImageView) inflatedView.findViewById(R.id.bg_img);
        image.setImageBitmap(Utils.getRoundedCornerBitmap(bitmap, 30));

        mViewPager = (ViewPager) inflatedView.findViewById(R.id.pager);
        mViewPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());
        mViewPager.setAdapter(mViewPagerAdapter);

        mViewPager.setCurrentItem(0);

        setCancelable(false);

        return inflatedView;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("resultCode", resultCode+" ");
        if( requestCode == CAMERA_CAPTURE ) {
            if(resultCode != 0) {
                final Bitmap thePic = data.getExtras().getParcelable("data");
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                thePic.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                bytearray = stream.toByteArray();// get byte array here
                Log.d(ARG_PATIENT_RECORD_OBJECT_ID, mParamPatientRecordObjectId);
                ParseObject updatePatient = ParseObject.createWithoutData("Patients", mParamPatientRecordObjectId);
                if (bytearray != null) {
                    System.out.println("BYTESSS");  //test case
                    System.out.println(bytearray.toString());  //test case
                    ParseFile file = new ParseFile("UserProfile.jpg", bytearray);
                    file.saveInBackground();
                    updatePatient.put("babypicture", file);
                }
                if (mProgressDialog == null) {
                    mProgressDialog = Utils.createProgressDialog(getActivity());
                    mProgressDialog.show();
                } else {
                    mProgressDialog.show();
                }
                updatePatient.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        mProgressDialog.dismiss();
                        if (e == null) {
                            mProgressDialog.dismiss();

                            //                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            //                        thePic.compress(Bitmap.CompressFormat.PNG, 100, stream);
                            //                        byte[] byteArray = stream.toByteArray();
                            //
                            //                        PatientDatabase map = new PatientDatabase();
                            //                        map.setPatientphoto(byteArray);
                            //
                            //                        patientDatabaseArrayList.set(mParamPatientArrayListPosition, map);

                            Bitmap profileInCircle = RoundedImageView.getRoundedCornerBitmap(thePic);

                            ImageButton patient_photo = (ImageButton) inflatedView.findViewById(R.id.patient_photo);
                            patient_photo.setImageBitmap(profileInCircle);
                        } else {
                            Log.e("ERROR", e.toString());
                        }
                    }

                });
            }


            //carry out the crop operation
//            performCrop();

        }
    }
//
//    private void performCrop(){
//        try {
////call the standard crop action intent (the user device may not support it)
//            Intent cropIntent = new Intent("com.android.camera.action.CROP");
//            //indicate image type and Uri
//            //cropIntent.setDataAndType(picUri, "image/*");
//            cropIntent.setType("image/*");
//            cropIntent.setData(picUri); // Uri to the image you want to crop
//
//            //set crop properties
//            cropIntent.putExtra("crop", "true");
//            //indicate aspect of desired crop
//            cropIntent.putExtra("aspectX", 1);
//            cropIntent.putExtra("aspectY", 1);
//            //indicate output X and Y
//            cropIntent.putExtra("outputX", 256);
//            cropIntent.putExtra("outputY", 256);
//            //retrieve data on return
//            cropIntent.putExtra("return-data", true);
//            //start the activity - we handle returning in onActivityResult
//            startActivityForResult(cropIntent, PIC_CROP);
//        }
//        catch(ActivityNotFoundException anfe){
//            //display an error message
//            String errorMessage = "Whoops - your device doesn't support the crop action!";
//            Toast toast = Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT);
//            toast.show();
//        }
//    }

    private ArrayList<SpinnerItems> populatePurpose(){
        final ArrayList<SpinnerItems> spinnerItems = new ArrayList<SpinnerItems>();
        //spinnerItems.add(new SpinnerItems("Purpose", false));
        spinnerItems.add(new SpinnerItems("Checkup", true));
        spinnerItems.add(new SpinnerItems("Follow-up", true));
        spinnerItems.add(new SpinnerItems("Vaccination", true));
        return spinnerItems;
    }
    private ArrayList<SpinnerItems> populateTypeOfDelivery(){
        final ArrayList<SpinnerItems> spinnerItems = new ArrayList<SpinnerItems>();
        //spinnerItems.add(new SpinnerItems("Purpose", false));
        spinnerItems.add(new SpinnerItems("Normal", true));
        spinnerItems.add(new SpinnerItems("CS", true));
        return spinnerItems;
    }

    private class CustomAdapter extends ArrayAdapter<SpinnerItems> {
        private Activity context;
        ArrayList<SpinnerItems> spinnerItems;

        public CustomAdapter(Activity context, int resource, ArrayList<SpinnerItems> spinnerItems) {
            super(context, resource, spinnerItems);
            this.context = context;
            this.spinnerItems = spinnerItems;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            SpinnerItems current = spinnerItems.get(position);

            LayoutInflater inflater = getLayoutInflater(getArguments());
            View row = inflater.inflate(R.layout.spinner_row, parent, false);
            TextView name = (TextView) row.findViewById(R.id.spinnerTxtTitle);
            Typeface myTypeFace = Typeface.createFromAsset(context.getAssets(), "fonts/VAGRoundedLight.ttf");

            name.setTypeface(myTypeFace, Typeface.NORMAL);
            name.setGravity(Gravity.LEFT);
            name.setTextSize(18);
            if(current.getSpinnerStatus() == false) {
                name.setTextColor(Color.GRAY);
            } else {
                name.setTextColor(Color.BLACK);
            }

            name.setText(current.getSpinnerTitle());

            return row;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            if (row == null) {
                LayoutInflater inflater = context.getLayoutInflater();
                row = inflater.inflate(R.layout.spinner_row, parent, false);
            }
            SpinnerItems current = spinnerItems.get(position);
            TextView name = (TextView) row.findViewById(R.id.spinnerTxtTitle);
            if(current.getSpinnerStatus() == false) {
                name.setTextColor(Color.GRAY);
            } else {
                name.setTextColor(Color.BLACK);
            }
            name.setText(current.getSpinnerTitle());
            return row;
        }
    }

    private class CustomTextWatcher implements TextWatcher {
        private EditText mEditText;
        private TextView mTextView;

        public CustomTextWatcher(EditText e, TextView textView) {
            mEditText = e;
            mTextView = textView;
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            mTextView.setText(s);
        }

        public void afterTextChanged(Editable s) {
        }
    }

    private class CustomOnKeyListener implements View.OnKeyListener {

        private EditText mEditText;
        private TextView mTextView;

        public CustomOnKeyListener(EditText e, TextView textView) {
            mEditText = e;
            mTextView = textView;
        }

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
            if(keyCode == KeyEvent.KEYCODE_DEL){
                //this is for backspace
                if(mEditText.getText().toString().equals("")) {
                    mTextView.setText(getString(R.string.lblZero));
                }
            }
            return false;
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = new CheckInFragment();
            Bundle args = new Bundle();
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            return TITLES[position].toUpperCase(l);
        }
    }

}
