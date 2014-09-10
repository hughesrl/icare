package com.fourello.icare;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;

import com.fourello.icare.adapters.MenuItemsAdapter;
import com.fourello.icare.adapters.MyChildrenAdapter;
import com.fourello.icare.datas.MenuItems;
import com.fourello.icare.datas.MyChildren;
import com.fourello.icare.datas.PatientDatabase;
import com.fourello.icare.datas.Patients;
import com.fourello.icare.fragments.AddBabyFragment;
import com.fourello.icare.fragments.AddUserFragment;
import com.fourello.icare.fragments.CheckinPatientDialogFragment;
import com.fourello.icare.fragments.DoctorInformationFragment;
import com.fourello.icare.fragments.ParentDashboardFragment;
import com.fourello.icare.fragments.PatientDatabaseActionsFragment;
import com.fourello.icare.fragments.PatientDatabaseFragment;
import com.fourello.icare.fragments.SettingsFragment;
import com.fourello.icare.fragments.ValidateEmailFragment;
import com.fourello.icare.view.CustomTextView;
import com.fourello.icare.widgets.FragmentUtils;
import com.fourello.icare.widgets.ParseProxyObject;
import com.fourello.icare.widgets.PasswordDialogFragment;
import com.fourello.icare.widgets.Utils;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class DashboardParentFragmentActivity extends FragmentActivity implements View.OnClickListener,
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

    private List<MyChildren> listMyChildren;

    private Spinner mySpinnerChildren;
    private MyChildrenAdapter myChildrenAdapter;

    private Patients myChild;

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
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            this.requests = new SparseArray<Bundle>();
        } else {
            this.requests = savedInstanceState.getSparseParcelableArray("requests");
        }

        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getActionBar().hide();
        setContentView(R.layout.activity_dashboard);

        mFragmentManager = getSupportFragmentManager();

        myChild = new Patients();

        final Intent extras = getIntent();
        loginData = (ParseProxyObject) extras.getSerializableExtra("loginData");
        myPicture = extras.getByteArrayExtra("myPicture");

        listMyChildren = new ArrayList<MyChildren>();

        // Get Children from LocalDatastore
        ParseQuery<ParseObject> queryChildren = ParseQuery.getQuery(ICareApplication.PATIENTS_LABEL);
        queryChildren.whereEqualTo("email", loginData.getString("email"));
        queryChildren.fromLocalDatastore();
        queryChildren.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (e == null) {
                    for (int i = 0; i < parseObjects.size(); i++) {
                        ParseObject patientObjects = parseObjects.get(i);

                        MyChildren myChildrenInfo = new MyChildren();

                        myChildrenInfo.setPatientObjectId(patientObjects.getObjectId());
                        ParseFile myPhoto = (ParseFile)patientObjects.get("photoFile");
                        if(myPhoto!=null) {
                            Log.d("ROBERT" +patientObjects.getObjectId(), "WithPhoto");
                            try {
                                byte[] data = myPhoto.getData();
                                myChildrenInfo.setPatientphoto(data);
                            } catch (ParseException e2) {
                                // TODO Auto-generated catch block
                                e2.printStackTrace();
                            }
                        } else {
                            //Log.d("ROBERT" +patientObjects.getObjectId(), "NoPhoto");
                        }
                        myChildrenInfo.setPatientName(patientObjects.getString("firstname")+" "+patientObjects.getString("lastname"));
                        myChildrenInfo.setPatientFirstName(patientObjects.getString("firstname"));
                        myChildrenInfo.setPatientMiddleName(patientObjects.getString("middlename"));
                        myChildrenInfo.setPatientLastName(patientObjects.getString("lastname"));

                        if (patientObjects.getString("placeofbirth") != null) {
                            myChildrenInfo.setbPlace(patientObjects.getString("placeofbirth"));
                        }
                        if (patientObjects.getString("deliveredby") != null) {
                            myChildrenInfo.setDrName(patientObjects.getString("deliveredby"));
                        }
                        if (patientObjects.getString("typeofdelivery") != null) {
                            myChildrenInfo.setDeliveryType(patientObjects.getString("typeofdelivery"));
                        }
                        if (patientObjects.getString("weight") != null) {
                            myChildrenInfo.setpWeight(patientObjects.getString("weight"));
                        }
                        if (patientObjects.getString("length") != null) {
                            myChildrenInfo.setpHeight(patientObjects.getString("length"));
                        }
                        if (patientObjects.getString("headcircumference") != null) {
                            myChildrenInfo.setpHead(patientObjects.getString("headcircumference"));
                        }
                        if (patientObjects.getString("chestcircumference") != null) {
                            myChildrenInfo.setpChest(patientObjects.getString("chestcircumference"));
                        }
                        if (patientObjects.getString("abdomencircumference") != null) {
                            myChildrenInfo.setpAbdomen(patientObjects.getString("abdomencircumference"));
                        }
//                        if (patientObjects.getString("allergyrisk") != null) {
//                            myChildrenInfo.setAllergyRisk(patientObjects.getString("allergyrisk"));
//                        }
//                        if (patientObjects.getDate("circumcisedon") != null) {
//                            Date circumcisedon = patientObjects.getDate("circumcisedon");
//                            String circumcisedOn = df.format(circumcisedon);
//                            map.setpCircumcisedOn(circumcisedOn);
//                        }
//                        if (patientObjects.getDate("earpiercedon") != null) {
//                            map.setpEarPiercedOn(patientObjects.getString("earpiercedon"));
//                            Date earpiercedon = patientDatabaseObject.getDate("earpiercedon");
//                            String earpiercedOn = df.format(earpiercedon);
//                            map.setpCircumcisedOn(earpiercedOn);
//                        }

                        listMyChildren.add(myChildrenInfo);
                    }
                }

//                if (findViewById(R.id.alt_fragment_content_container) != null) {
//                    if (savedInstanceState != null) {
//                        return;
//                    }
//                    ParentDashboard(listMyChildren, 0);
//                }

                btnShowMenu = (ImageButton)findViewById(R.id.btnShowMenu);
                dialog = new Dialog(DashboardParentFragmentActivity.this, R.style.DialogSlideAnim);
                dialog.setContentView(R.layout.activity_menu_parent);

                if(!extras.getBooleanExtra("isRestarted", false)) {
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
        //overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }
    @Override
    public void onBackPressed() {
    }

    public void showMenu(final View view) {
        showMenuContents(view);
    }

    public void showPasswordDialog(Fragment fragment, String purpose) {
        // To create an instance of DialogFragment and displays

        DialogFragment passwordDialog = PasswordDialogFragment.newInstance(purpose);
        Bundle args = getIntent().getExtras();
        args.putString(PasswordDialogFragment.PURPOSE_TO_OPEN, purpose);

        passwordDialog.setArguments(args);
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
            dialog.setContentView(R.layout.activity_menu_parent);

            final CustomTextView txtUserName = (CustomTextView) dialog.findViewById(R.id.userName);
            final ImageView imgViewMyPicture = (ImageView) dialog.findViewById(R.id.btnMyPicture);

            mySpinnerChildren = (Spinner) dialog.findViewById(R.id.spinnerChildren);

            mySpinnerChildren.setClickable(false); // disable the dropdown for version 1
            myChildrenAdapter = new MyChildrenAdapter(DashboardParentFragmentActivity.this, R.layout.spinner_row, (ArrayList<MyChildren>) listMyChildren);
            mySpinnerChildren.setAdapter(myChildrenAdapter);
            mySpinnerChildren.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    byte[] childPhoto = listMyChildren.get(position).getPatientphoto();

                    txtUserName.setText("Baby "+listMyChildren.get(position).getPatientFirstName());
                    Bitmap bMap = BitmapFactory.decodeByteArray(childPhoto, 0, childPhoto.length);
                    imgViewMyPicture.setBackground(Utils.resizedBitmapDisplayUserPhoto(DashboardParentFragmentActivity.this, bMap));

                    ParentDashboard(listMyChildren, position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            // Get ListView object from xml
            listSubMenu = (ListView) dialog.findViewById(R.id.listSubMenu);

            ImageButton btnMyBaby = (ImageButton) dialog.findViewById(R.id.btnMyBaby);
            btnMyBaby.setOnClickListener(this);

            ImageButton btnMyTracker = (ImageButton) dialog.findViewById(R.id.btnMyTracker);
            btnMyTracker.setOnClickListener(this);

            ImageButton btnMyClinicVisits = (ImageButton) dialog.findViewById(R.id.btnMyClinicVisits);
            btnMyClinicVisits.setOnClickListener(this);

            ImageButton btnSettings = (ImageButton) dialog.findViewById(R.id.btnSettings);
            btnSettings.setOnClickListener(this);

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
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnMyBaby:
//                Toast.makeText(this, mySpinnerChildren.getSelectedItemPosition()+" selected item", Toast.LENGTH_LONG).show();
                // Defined Array values to show in ListView
                MenuItems menu_items_data_btnMyBaby[] = new MenuItems[]{
                        new MenuItems("Baby Info", true)
                };
                MenuItemsAdapter adapter_btnMyBaby = new MenuItemsAdapter(DashboardParentFragmentActivity.this, R.layout.list_menu_items, menu_items_data_btnMyBaby);
                // Assign adapter to ListView
                listSubMenu.setAdapter(adapter_btnMyBaby);
                listSubMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        dialog.dismiss();
                        switch (position) {
                            case 0:
                                AddUser();
                                break;
                        }
                    }
                });
                break;
            case R.id.btnMyTracker:
                // Defined Array values to show in ListView
                MenuItems menu_items_data_btnMyTracker[] = new MenuItems[]{
                        new MenuItems("Growth Tracker", true),
                        new MenuItems("Medication Tracker", true),
                        new MenuItems("Immunization Tracker", true),
                        new MenuItems("Symptoms Tracker", true)
                };
                MenuItemsAdapter adapter_btnMyTracker = new MenuItemsAdapter(DashboardParentFragmentActivity.this, R.layout.list_menu_items, menu_items_data_btnMyTracker);
                // Assign adapter to ListView
                listSubMenu.setAdapter(adapter_btnMyTracker);
                listSubMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        dialog.dismiss();
                        switch (position) {
                            case 0:
                                AddUser();
                                break;
                        }
                    }
                });
                break;
            case R.id.btnMyClinicVisits:
                // Defined Array values to show in ListView
                MenuItems menu_items_data_btnMyClinicVisits[] = new MenuItems[]{
                        new MenuItems("Clinic Visits", true),
                        new MenuItems("Check-in", true)
                };
                MenuItemsAdapter adapter_btnMyClinicVisits = new MenuItemsAdapter(DashboardParentFragmentActivity.this, R.layout.list_menu_items, menu_items_data_btnMyClinicVisits);
                // Assign adapter to ListView
                listSubMenu.setAdapter(adapter_btnMyClinicVisits);
                listSubMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        dialog.dismiss();
                        switch (position) {
                            case 0:
                                AddUser();
                                break;
                        }
                    }
                });
                break;
            case R.id.btnSettings:
                // Defined Array values to show in ListView
                MenuItems menu_items_data_btnSettings[] = new MenuItems[]{
                        new MenuItems("My Doctor", true),
                        new MenuItems("Logout", true)
                };
                final MenuItemsAdapter adapter_btnSettings = new MenuItemsAdapter(DashboardParentFragmentActivity.this, R.layout.list_menu_items, menu_items_data_btnSettings);
                // Assign adapter to ListView
                listSubMenu.setAdapter(adapter_btnSettings);

                listSubMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        dialog.dismiss();
                        switch (position) {
                            case 0:
                                // Doctor Information
                                DoctorInformation();
                                break;
                            case 1:;
                                ParseObject.unpinAllInBackground(new DeleteCallback() {
                                    public void done(ParseException e) {
                                        if (e != null) {
                                            // There was some error.
                                            return;
                                        } else {
                                            ParseObject.unpinAllInBackground(ICareApplication.PATIENTS_LABEL);
                                            ParseObject.unpinAllInBackground(ICareApplication.USERS_LABEL, new DeleteCallback() {
                                                public void done(ParseException e) {
                                                    if (e != null) {
                                                        return;
                                                    }
                                                    Intent intent = new Intent(DashboardParentFragmentActivity.this, LoginSignupActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            });
                                        }
                                    }
                                });
                                break;
                        }
                    }
                });
                break;

        }
    }
    public void changePageTitle(String pageTitle) {
        CustomTextView tvPageTitle = (CustomTextView)findViewById(R.id.pageTitle);
        tvPageTitle.setText(pageTitle);
    }

    // -----------------------------------------------
    // FRAGMENTS
    // -----------------------------------------------
    public void ParentDashboard(List<MyChildren> listMyChildren, int childPosition) {
        mFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        ParentDashboardFragment myFragment = new ParentDashboardFragment();
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        if(myFragment.isInLayout()) {
            transaction.remove(myFragment);
            myFragment = new ParentDashboardFragment();
        }

        Bundle bundle = getIntent().getExtras();

        bundle.putInt(ParentDashboardFragment.ARG_CHILD_DATA, childPosition);
        bundle.putParcelableArrayList(ParentDashboardFragment.ARG_CHILD_DATA, (ArrayList<MyChildren>) listMyChildren);
        myFragment.setArguments(bundle);


        transaction.replace(R.id.alt_fragment_content_container, myFragment, "PARENT_DASHBOARD_FRAGMENT");
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
        byte[] patientPhoto = patientDatabase.getPatientphoto();

        Bundle bundle = getIntent().getExtras();
        bundle.putString(PatientDatabaseActionsFragment.ARG_PATIENT_OBJECT_ID, patientObjectId);
        bundle.putByteArray(PatientDatabaseActionsFragment.ARG_MY_PICTURE, patientPhoto);
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
        transaction.replace(R.id.alt_fragment_content_container, myFragment, "SETTINGS_FRAGMENT");
        transaction.commit();
        // Fragment must implement the callback.
        if (!(myFragment instanceof OpenMenuCallbacks)) {
            throw new IllegalStateException(
                    "Fragment must implement the callbacks.");
        }
        mCallbacks = (OpenMenuCallbacks) myFragment;

    }

    public void DoctorInformation() {
        mFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        DoctorInformationFragment myFragment = new DoctorInformationFragment();

        Bundle bundle = getIntent().getExtras();

        myFragment.setArguments(bundle);

        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.replace(R.id.alt_fragment_content_container, myFragment, "DOCTOR_INFORMATION_FRAGMENT");
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
