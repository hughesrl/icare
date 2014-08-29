package com.fourello.icare;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.fourello.icare.adapters.MenuItemsAdapter;
import com.fourello.icare.datas.MenuItems;
import com.fourello.icare.datas.PatientDatabase;
import com.fourello.icare.fragments.AddBabyFragment;
import com.fourello.icare.fragments.AddUserFragment;
import com.fourello.icare.fragments.CheckInFragment;
import com.fourello.icare.fragments.CheckinPatientDialogFragment;
import com.fourello.icare.fragments.DoctorDashboardFragment;
import com.fourello.icare.fragments.MyDashboardFragment;
import com.fourello.icare.fragments.NoticeDialogFragment;
import com.fourello.icare.fragments.PatientDatabaseActionsFragment;
import com.fourello.icare.fragments.PatientDatabaseFragment;
import com.fourello.icare.fragments.PromosFragment;
import com.fourello.icare.fragments.SettingsFragment;
import com.fourello.icare.fragments.ValidateEmailFragment;
import com.fourello.icare.view.CustomTextView;
import com.fourello.icare.view.RoundedAvatarDrawable;
import com.fourello.icare.view.RoundedImageView;
import com.fourello.icare.widgets.FragmentUtils;
import com.fourello.icare.widgets.ParseProxyObject;
import com.fourello.icare.widgets.PasswordDialogFragment;
import com.parse.DeleteCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class DashboardDoctorFragmentActivity extends FragmentActivity implements
        FragmentUtils.ActivityForResultStarter,
        PasswordDialogFragment.PasswordDialogListener,
        CheckinPatientDialogFragment.CheckinPatientDialogListener {

    private static final int MY_REQUEST_CODE = 1;

    public Dialog dialog;
    public ImageButton btnShowMenu;
    public ParseProxyObject loginData;

    public byte[] myPicture;

    public ListView listSubMenu;
    public FragmentManager mFragmentManager;

    public int validPassword;

    private OpenMenuCallbacks mCallbacks;
    private SparseArray<Bundle> requests;

    @Override
    public void onDialogDoneClick(DialogFragment dialog) {

    }

    @Override
    public void onDialogCancelClick(DialogFragment dialog) {
        dialog.dismiss();
    }

    @Override
    public void startActivityForResultWhileSavingOrigin(int requestCode, Intent intent, int[] indices) {
        //special method for start an activity for result.
//we save the information about where this request comes from in a bundle and store it based on requestCode
        final Bundle bundle = new Bundle();
        bundle.putInt("code", requestCode);
        bundle.putParcelable("intent", intent);
        bundle.putIntArray("indices", indices);

        this.requests.put(requestCode, bundle);
        this.startActivityForResult(intent, requestCode);
    }


    public interface OpenMenuCallbacks {
        public void onMenuPressedCallback();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null)
        {
            this.requests = new SparseArray<Bundle>();
        }
        else
        {
            this.requests = savedInstanceState.getSparseParcelableArray("requests");
        }

        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getActionBar().hide();
        setContentView(R.layout.activity_dashboard);

        Intent extras = getIntent();
        loginData = (ParseProxyObject) extras.getSerializableExtra("loginData");

        myPicture = extras.getByteArrayExtra("myPicture");

        mFragmentManager = getSupportFragmentManager();

        if (findViewById(R.id.alt_fragment_content_container) != null) {
            if (savedInstanceState != null) {
                return;
            }
            DoctorDashboard();
        }

        btnShowMenu = (ImageButton)findViewById(R.id.btnShowMenu);
        dialog = new Dialog(DashboardDoctorFragmentActivity.this, R.style.DialogSlideAnim);
        dialog.setContentView(R.layout.activity_menu);

        if(extras.getBooleanExtra("isRestarted",false) == false) {
            showMenu(btnShowMenu);
        } else {

        }
        btnShowMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCallbacks != null)
                    mCallbacks.onMenuPressedCallback();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSparseParcelableArray("requests", this.requests);
    }

    @Override
    public void onStart() {
        super.onStart();
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }
    @Override
    public void onBackPressed() {
    }

    public void showMenu(final View view) {
        showMenuContents(view);
    }


    public void showPasswordDialog(Fragment fragment, String purpose) {
        // To create an instance of DialogFragment and displays
        DialogFragment passwordDialog = new PasswordDialogFragment(purpose);
        passwordDialog.setArguments(getIntent().getExtras());
        passwordDialog.setTargetFragment(fragment, MY_REQUEST_CODE);
        passwordDialog.show(getSupportFragmentManager(), "NoticeDialogFragment");
    }

    // Through the Fragment.onAttach () of the callback, the instance of DialogFragment can receive the realization method of NoticeDialogFragment.NoticeDialogListener is used to define the reference
    @Override
    public void onDialogPositiveClick(DialogFragment dialog, String password, String purposeToOpen) {
        // Active user touch button dialog
    }

    @Override
     public void onDialogNegativeClick(DialogFragment dialog) {
        // The user touches the negative Button Dialog
        validPassword = 0;
        dialog.dismiss();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void showMenuContents(final View view) {
        view.setVisibility(View.VISIBLE);
        if (dialog.isShowing()) {
            dialog.dismiss();
        } else {
            dialog.setContentView(R.layout.activity_menu);

            CustomTextView txtUserName = (CustomTextView) dialog.findViewById(R.id.userName);
            txtUserName.setText(loginData.getString("firstname") + " " + loginData.getString("lastname"));

            ImageView imgViewMyPicture = (ImageView) dialog.findViewById(R.id.btnMyPicture);
            Bitmap bMap = BitmapFactory.decodeByteArray(myPicture, 0, myPicture.length);
            int width=350;
            int height=350;
            Bitmap resizedbitmap=Bitmap.createScaledBitmap(bMap, width, height, true);
            RoundedAvatarDrawable r = new RoundedAvatarDrawable(resizedbitmap);

            imgViewMyPicture.setBackground(r);

//            Bitmap profileInCircle = RoundedImageView.getRoundedCornerBitmap(bMap);
//            imgViewMyPicture.setImageBitmap(r.getBitmap());
            imgViewMyPicture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    Intent intent = getIntent();
                    intent.putExtra("loginData", loginData);
                    intent.putExtra("myPicture", myPicture);
                    intent.putExtra("isRestarted", true);
                    finish();
                    startActivity(intent);
                }
            });

            // Get ListView object from xml
            listSubMenu = (ListView) dialog.findViewById(R.id.listSubMenu);

            ImageButton btnMyPatients = (ImageButton) dialog.findViewById(R.id.btnMyPatients);
            btnMyPatients.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Defined Array values to show in ListView
                    MenuItems menu_items_data[] = new MenuItems[]{
                            new MenuItems("Add User", true),
                            new MenuItems("Add Patient", true),
                            new MenuItems("Patient Database", true),
                            new MenuItems("Messages", false)
                    };
                    MenuItemsAdapter adapter = new MenuItemsAdapter(DashboardDoctorFragmentActivity.this, R.layout.list_menu_items, menu_items_data);
                    // Assign adapter to ListView
                    listSubMenu.setAdapter(adapter);
                    listSubMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            dialog.dismiss();
                            switch (position) {
                                case 0:
                                    AddUser();
                                    break;
                                case 1:
                                    ParentEmailValidate(null, null);
                                    break;
                                case 2:
                                    PatientDatabase();
                                    break;
                            }
                        }
                    });
                }
            });

            ImageButton btnSettings = (ImageButton) dialog.findViewById(R.id.btnSettings);
            btnSettings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Defined Array values to show in ListView
                    MenuItems menu_items_data[] = new MenuItems[]{
                            new MenuItems("Settings", true),
                            new MenuItems("Doctor Information", true),
                            new MenuItems("Logout", true)
                    };
                    final MenuItemsAdapter adapter = new MenuItemsAdapter(DashboardDoctorFragmentActivity.this, R.layout.list_menu_items, menu_items_data);
                    // Assign adapter to ListView
                    listSubMenu.setAdapter(adapter);

                    listSubMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            dialog.dismiss();
                            switch (position) {
                                case 0:
                                    // Settings
                                    Settings();
                                    break;
                                case 1:
                                    // Doctor Information
                                    break;
                                case 2:
                                    ParseObject.unpinAllInBackground(new DeleteCallback() {
                                        public void done(ParseException e) {
                                            if (e != null) {
                                                // There was some error.
                                                return;
                                            } else {
                                                ParseObject.unpinAllInBackground(ICareApplication.DOCTORS_LABEL, new DeleteCallback() {
                                                    public void done(ParseException e) {
                                                        if (e != null) {
                                                            // There was some error.
                                                            return;
                                                        }
                                                        Intent intent = new Intent(DashboardDoctorFragmentActivity.this, LoginSignupActivity.class);
                                                        startActivity(intent);
                                                        finish();
                                                    }
                                                });
                                            }
                                        }
                                    });
                                    break;
                                default:
                                    break;
                            }
                        }
                    });
                }
            });

            //            Handler handler = new Handler();
            //            handler.postDelayed(new Runnable() {
            //                public void run() {
            ////                    Bitmap map= ICareApplication.takeScreenShot(Dashboard_DoctorActivity.this);
            ////
            ////                    Bitmap fast=ICareApplication.fastblur(map, 10);
            ////                    final Drawable draw=new BitmapDrawable(getResources(),fast);
            ////                    dialog.getWindow().setBackgroundDrawable(draw);
            //                    WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
            //                    lp.dimAmount=0.9f;  // this sets the amount of darkening
            //                    dialog.getWindow().setAttributes(lp);
            //                    dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            //
            //                    dialog.show();
            //                    view.setVisibility(View.INVISIBLE);
            //
            //                    ImageButton btnCloseMenu = (ImageButton)dialog.findViewById(R.id.btnCloseMenu);
            //                    btnCloseMenu.setOnClickListener(new View.OnClickListener() {
            //                        @Override
            //                        public void onClick(View v) {
            //                            dialog.dismiss();
            //                            view.setVisibility(View.VISIBLE);
            //                        }
            //                    });
            //                }
            //            }, 10);


            WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
            lp.dimAmount = 0.9f;  // this sets the amount of darkening
            dialog.getWindow().setAttributes(lp);
            dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            dialog.show();

            ImageButton btnCloseMenu = (ImageButton) dialog.findViewById(R.id.btnCloseMenu);
            btnCloseMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    view.setVisibility(View.VISIBLE);
                }
            });
        }
    }
    public void changePageTitle(String pageTitle) {
        CustomTextView tvPageTitle = (CustomTextView)findViewById(R.id.pageTitle);
        tvPageTitle.setText(pageTitle);
    }

    // -----------------------------------------------
    // FRAGMENTS
    // -----------------------------------------------
    public void DoctorDashboard() {
        mFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        DoctorDashboardFragment myFragment = new DoctorDashboardFragment();

        Bundle bundle = getIntent().getExtras();

        myFragment.setArguments(bundle);

        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.replace(R.id.alt_fragment_content_container, myFragment, "DOCTOR_DASHBOARD_FRAGMENT");
        transaction.commit();
        // Fragment must implement the callback.
        if (!(myFragment instanceof OpenMenuCallbacks)) {
            throw new IllegalStateException(
                    "Fragment must implement the callbacks.");
        }
        mCallbacks = (OpenMenuCallbacks) myFragment;
    }

    public void AddUser() {
        mFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        AddUserFragment myFragment = new AddUserFragment();

        Bundle bundle = getIntent().getExtras();

        myFragment.setArguments(bundle);

        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.replace(R.id.alt_fragment_content_container, myFragment, "ADD_USER_FRAGMENT");
        transaction.commit();
        // Fragment must implement the callback.
        if (!(myFragment instanceof OpenMenuCallbacks)) {
            throw new IllegalStateException(
                    "Fragment must implement the callbacks.");
        }
        mCallbacks = (OpenMenuCallbacks) myFragment;
    }


    public void ParentEmailValidate(String newUserRecordObjectId, String newRecordObjectId) {
        mFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        ValidateEmailFragment myFragment = new ValidateEmailFragment();

        Bundle bundle = getIntent().getExtras();
        bundle.putString("newUserRecordObjectId", newUserRecordObjectId);
        bundle.putString("newPatientRecordObjectId", newRecordObjectId);

        myFragment.setArguments(bundle);

        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.replace(R.id.alt_fragment_content_container, myFragment, "PARENT_EMAIL_VALIDATE_FRAGMENT");
        transaction.commit();
        // Fragment must implement the callback.
        if (!(myFragment instanceof OpenMenuCallbacks)) {
            throw new IllegalStateException(
                    "Fragment must implement the callbacks.");
        }
        mCallbacks = (OpenMenuCallbacks) myFragment;
    }
    public void AddBaby(String newUserRecordObjectId, String newRecordObjectId) {
        mFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        AddBabyFragment myFragment = new AddBabyFragment();

        Bundle bundle = getIntent().getExtras();
        bundle.putString("newUserRecordObjectId", newUserRecordObjectId);
        bundle.putString("newPatientRecordObjectId", newRecordObjectId);

        myFragment.setArguments(bundle);

        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.replace(R.id.alt_fragment_content_container, myFragment, "ADD_BABY_FRAGMENT");
        transaction.commit();
        // Fragment must implement the callback.
        if (!(myFragment instanceof OpenMenuCallbacks)) {
            throw new IllegalStateException(
                    "Fragment must implement the callbacks.");
        }
        mCallbacks = (OpenMenuCallbacks) myFragment;
    }

    public void PatientDatabase() {
        mFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        PatientDatabaseFragment myFragment = new PatientDatabaseFragment();

        Bundle bundle = getIntent().getExtras();

        myFragment.setArguments(bundle);

        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.replace(R.id.alt_fragment_content_container, myFragment, "PATIENT_DATABASE_FRAGMENT");
        transaction.commit();
        // Fragment must implement the callback.
        if (!(myFragment instanceof OpenMenuCallbacks)) {
            throw new IllegalStateException(
                    "Fragment must implement the callbacks.");
        }
        mCallbacks = (OpenMenuCallbacks) myFragment;
    }
    public void PatientDatabaseActions(PatientDatabase patientDatabase, ArrayList<PatientDatabase> patientDatabaseComplete, int position) {
        mFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        PatientDatabaseActionsFragment myFragment = new PatientDatabaseActionsFragment();


        String patientObjectId = patientDatabase.getPatientObjectId();
        String patientName = patientDatabase.getFullName();

        Bundle bundle = getIntent().getExtras();
        bundle.putString(PatientDatabaseActionsFragment.ARG_PATIENT_OBJECT_ID, patientObjectId);
        bundle.putInt(PatientDatabaseActionsFragment.ARG_PATIENT_DATA_POSITION, position);
//        bundle.putParcelableArrayList("test", patientDatabase.getVisits());
        bundle.putParcelable(PatientDatabaseActionsFragment.ARG_PATIENT_DATA, patientDatabase);
        bundle.putParcelableArrayList(PatientDatabaseActionsFragment.ARG_PATIENT_DATA_COMPLETE, patientDatabaseComplete);
        myFragment.setArguments(bundle);

        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.replace(R.id.alt_fragment_content_container, myFragment, "PATIENT_DATABASE_ACTIONS_FRAGMENT");
        transaction.commit();
        // Fragment must implement the callback.
        if (!(myFragment instanceof OpenMenuCallbacks)) {
            throw new IllegalStateException(
                    "Fragment must implement the callbacks.");
        }
        mCallbacks = (OpenMenuCallbacks) myFragment;

    }

    public void Settings() {
        mFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        SettingsFragment myFragment = new SettingsFragment();

        Bundle bundle = getIntent().getExtras();

        myFragment.setArguments(bundle);

        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.replace(R.id.alt_fragment_content_container, myFragment, "DOCTOR_DASHBOARD_FRAGMENT");
        transaction.commit();
        // Fragment must implement the callback.
        if (!(myFragment instanceof OpenMenuCallbacks)) {
            throw new IllegalStateException(
                    "Fragment must implement the callbacks.");
        }
        mCallbacks = (OpenMenuCallbacks) myFragment;

    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data){
//        super.onActivityResult(requestCode,resultCode,data);
//
//        Toast.makeText(getApplicationContext(), requestCode+" - DashboardDoctorFragmentActivity", Toast.LENGTH_LONG).show();
//    }
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data)
{
//first check if we saved any data about the origin of this request (which fragment)
    final Bundle request = this.requests.get(requestCode, null);
    if (request != null)
    {
//find the indices-array
        final int[] indices = request.getIntArray("indices");

        FragmentManager fm = this.getSupportFragmentManager();
        Fragment f = null;
//loop backwards
        for(int i = indices.length - 1 ; i >= 0 ; i--)
        {
            if (fm != null)
            {
//find a list of active fragments
                List<Fragment> flist = fm.getFragments();
                if (flist != null && indices[i] < flist.size())
                {
                    f = flist.get(indices[i]);
                    fm = f.getChildFragmentManager();
                }
            }
        }

//we found our fragment that initiated the request to startActivityForResult, give it the callback!
        if (f != null)
        {
            f.onActivityResult(requestCode, resultCode, data);
            return ;
        }
    }

    super.onActivityResult(requestCode, resultCode, data);
}

}
