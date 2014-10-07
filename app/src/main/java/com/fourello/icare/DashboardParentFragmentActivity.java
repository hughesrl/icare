package com.fourello.icare;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
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
import com.fourello.icare.datas.Doctors;
import com.fourello.icare.datas.MenuItems;
import com.fourello.icare.datas.PatientChildData;
import com.fourello.icare.datas.Patients;
import com.fourello.icare.fragments.CheckinPatientDialogFragment;
import com.fourello.icare.fragments.ParentBabyInfoFragment;
import com.fourello.icare.fragments.ParentClinicVisitsFragment;
import com.fourello.icare.fragments.ParentDashboardFragment;
import com.fourello.icare.fragments.ParentDoctorInformationFragment;
import com.fourello.icare.fragments.ParentGrowthTrackerFragment;
import com.fourello.icare.fragments.ParentImmunizationTrackerFragment;
import com.fourello.icare.fragments.ParentMedicationTrackerFragment;
import com.fourello.icare.fragments.ParentMyClinicCheckInFragment;
import com.fourello.icare.fragments.ParentSymptomsTrackerFragment;
import com.fourello.icare.view.CustomTextView;
import com.fourello.icare.widgets.AlertDialogFragment;
import com.fourello.icare.widgets.FragmentUtils;
import com.fourello.icare.widgets.ParseProxyObject;
import com.fourello.icare.widgets.PasswordDialogFragment;
import com.fourello.icare.widgets.Utils;
import com.parse.CountCallback;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DashboardParentFragmentActivity extends FragmentActivity implements View.OnClickListener,
        FragmentUtils.ActivityForResultStarter,
        AlertDialogFragment.AlertDialogListener,
        CheckinPatientDialogFragment.CheckinPatientDialogListener {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String ARG_LOGIN_DATA = "loginData";
    public static final String ARG_CHILD_DATA = "childData";
    public static final String ARG_CHILD_DATA_POS = "childDataPosition";
    public static final String ARG_MY_PICTURE = "myPicture";


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

    private List<PatientChildData> listMyChildren;

    private Spinner mySpinnerChildren;
    private MyChildrenAdapter myChildrenAdapter;

    private Patients myChild;

    private PatientChildData myChildData;

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

    public Date midnight;
    public Date elevenfiftynine;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            this.requests = new SparseArray<Bundle>();
        } else {
            this.requests = savedInstanceState.getSparseParcelableArray("requests");
        }

        // Create the array
        midnight = new Date();
        midnight.setHours(0);
        midnight.setMinutes(0);
        midnight.setSeconds(0);

        elevenfiftynine = new Date();
        elevenfiftynine.setHours(23);
        elevenfiftynine.setMinutes(59);
        elevenfiftynine.setSeconds(59);

        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getActionBar().hide();
        setContentView(R.layout.activity_dashboard);

        mFragmentManager = getSupportFragmentManager();

        myChild = new Patients();

        final Intent extras = getIntent();
        loginData = (ParseProxyObject) extras.getSerializableExtra("loginData");
        myPicture = extras.getByteArrayExtra("myPicture");

        listMyChildren = new ArrayList<PatientChildData>();

        // Get Children from LocalDatastore
        ParseQuery<Patients> queryChildren = Patients.getQuery();
        queryChildren.fromLocalDatastore();
        queryChildren.findInBackground(new FindCallback<Patients>() {
            @Override
            public void done(List<Patients> parseObjects, ParseException e) {
                DateFormat df = new SimpleDateFormat("MMM dd, yyyy");
                if (e == null) {
                    for (int i = 0; i < parseObjects.size(); i++) {
                        ParseObject patientObjects = parseObjects.get(i);

                        PatientChildData myChildrenInfo = new PatientChildData();

                        myChildrenInfo.setPatientObjectId(patientObjects.getObjectId());

                        myChildrenInfo.setFirtname(patientObjects.getString("firstname"));
                        myChildrenInfo.setMiddlename(patientObjects.getString("middlename"));
                        myChildrenInfo.setLastname(patientObjects.getString("lastname"));

                        if (patientObjects.getDate("dateofbirth") != null) {
                            Date dateofbirth = patientObjects.getDate("dateofbirth");
                            String dateofbirthString = df.format(dateofbirth);
                            myChildrenInfo.setbDate(dateofbirthString);

                            Log.d("ROBERT dateofbirthString", dateofbirthString);
                        }
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
                        if (patientObjects.getString("allergyrisk") != null) {
                            myChildrenInfo.setAllergyRisk(patientObjects.getString("allergyrisk"));
                        }
                        if (patientObjects.getDate("circumcisedon") != null) {
                            Date circumcisedon = patientObjects.getDate("circumcisedon");
                            String circumcisedOn = df.format(circumcisedon);
                            myChildrenInfo.setpCircumcisedOn(circumcisedOn);
                        }
                        if (patientObjects.getDate("earpiercedon") != null) {
                            myChildrenInfo.setpEarPiercedOn(patientObjects.getString("earpiercedon"));
                            Date earpiercedon = patientObjects.getDate("earpiercedon");
                            String earpiercedOn = df.format(earpiercedon);
                            myChildrenInfo.setpCircumcisedOn(earpiercedOn);
                        }

                        if (patientObjects.getString("email") != null) {
                            myChildrenInfo.setParentEmail(patientObjects.getString("email"));
                        }

                        if(patientObjects.getString("doctorid") != null) {

                            myChildrenInfo.setDoctorID(patientObjects.getString("doctorid"));

                            Log.d("ROBERT DOCTORID", patientObjects.getString("doctorid"));
                        }
                        // Distinguishing Marks cannot found

                        // Newborn Screening cannot found

                        // Vaccinations Given cannot found

                        /* MOTHER */
                        if (patientObjects.getString("motherfirstname") != null) {
                            myChildrenInfo.setpMomsFname(patientObjects.getString("motherfirstname"));
                        }
                        if (patientObjects.getString("mothermiddlename") != null) {
                            myChildrenInfo.setpMomsMname(patientObjects.getString("mothermiddlename"));
                        }
                        if (patientObjects.getString("motherlastname") != null) {
                            myChildrenInfo.setpMomsLname(patientObjects.getString("motherlastname"));
                        }
                        if (patientObjects.getString("mothercompany") != null) {
                            myChildrenInfo.setpMomsWorkPlace(patientObjects.getString("mothercompany"));
                        }
                        if (patientObjects.getString("motherprofession") != null) {
                            myChildrenInfo.setpMomsWorkAs(patientObjects.getString("motherprofession"));
                        }
                        if (patientObjects.getString("motherhmo") != null) {
                            myChildrenInfo.setpMomsHMO(patientObjects.getString("motherhmo"));
                        }

                        /* FATHER */
                        if (patientObjects.getString("fatherfirstname") != null) {
                            myChildrenInfo.setpDadsFname(patientObjects.getString("fatherfirstname"));
                        }
                        if (patientObjects.getString("fathermiddlename") != null) {
                            myChildrenInfo.setpDadsMname(patientObjects.getString("fathermiddlename"));
                        }
                        if (patientObjects.getString("fatherlastname") != null) {
                            myChildrenInfo.setpDadsLname(patientObjects.getString("fatherlastname"));
                        }
                        if (patientObjects.getString("fathercompany") != null) {
                            myChildrenInfo.setpDadsWorkPlace(patientObjects.getString("fathercompany"));
                        }
                        if (patientObjects.getString("fatherprofession") != null) {
                            myChildrenInfo.setpDadsWorkAs(patientObjects.getString("fatherprofession"));
                        }
                        if (patientObjects.getString("fatherhmo") != null) {
                            myChildrenInfo.setpDadsHMO(patientObjects.getString("fatherhmo"));
                        }

                        /* ADDRESS */
                        if (patientObjects.getString("address_1") != null) {
                            myChildrenInfo.setpAddress1(patientObjects.getString("address_1"));
                        }
                        if (patientObjects.getString("address_2") != null) {
                            myChildrenInfo.setpAddress2(patientObjects.getString("address_2"));
                        }

                        String contactnumber = "";
                        if (patientObjects.getString("mothercontactnumber") != null && patientObjects.getString("fathercontactnumber") != null) {
                            contactnumber = patientObjects.getString("mothercontactnumber") + " / " + patientObjects.getString("fathercontactnumber");
                        } else if (patientObjects.getString("mothercontactnumber") != null) {
                            contactnumber = patientObjects.getString("mothercontactnumber");
                        } else if (patientObjects.getString("fathercontactnumber") != null) {
                            contactnumber = patientObjects.getString("fathercontactnumber");
                        }


                        myChildrenInfo.setMobilenumbers(contactnumber);

                        if(patientObjects.has("photoFile")) {
                            ParseFile myPhoto = patientObjects.getParseFile("photoFile");
                            if (myPhoto != null) {
//                                myChildrenInfo.setPatientphoto(myPhoto.getUrl());
                                try {
                                    byte[] data = myPhoto.getData();
                                    myChildrenInfo.setPatientphoto(data);
                                } catch (ParseException e2) {
                                    // TODO Auto-generated catch block
                                    e2.printStackTrace();
                                }
                            }
                        }

                        listMyChildren.add(myChildrenInfo);
                    }
                }

                if (findViewById(R.id.alt_fragment_content_container) != null) {
                    if (savedInstanceState != null) {
                        return;
                    }
                    ParentDashboard(listMyChildren, 0);
                }

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

    public void showAlertDialog(String purpose, String doctorID) {
        // To create an instance of DialogFragment and displays
        DialogFragment passwordDialog = AlertDialogFragment.newInstance(purpose);
        Bundle args = getIntent().getExtras();
        args.putString(AlertDialogFragment.PURPOSE_TO_OPEN, purpose);
        args.putString(AlertDialogFragment.DOCTOR_ID, doctorID);

        passwordDialog.setArguments(args);
//        passwordDialog.setTargetFragment(fragment, MY_REQUEST_CODE);
        passwordDialog.show(getSupportFragmentManager(), "NoticeDialogFragment");
    }

    // Through the Fragment.onAttach () of the callback, the instance of DialogFragment can receive the realization method of NoticeDialogFragment.NoticeDialogListener is used to define the reference
    @Override
    public void onCheckInDialogPositiveClick(DialogFragment dialog, String purposeToOpen, String doctorID, String etPINOfTheDay) {
        if(purposeToOpen.equalsIgnoreCase("enter_pin")) {

            new CheckIfPINOfTheDayMatch(this, dialog, doctorID, etPINOfTheDay).execute();

        } else if(purposeToOpen.equalsIgnoreCase("alert")) {
            dialog.dismiss();
            Intent intent = getIntent();
            intent.putExtra("loginData", loginData);
            intent.putExtra("myPicture", myPicture);
            intent.putExtra("isRestarted", true);
            finish();
            startActivity(intent);
        } else if(purposeToOpen.equalsIgnoreCase("error")) {
            dialog.dismiss();
            Intent intent = getIntent();
            intent.putExtra("loginData", loginData);
            intent.putExtra("myPicture", myPicture);
            intent.putExtra("isRestarted", true);
            finish();
            startActivity(intent);
        } else {
            // Check if PIN of the day match.
            dialog.dismiss();
        }
    }

    @Override
     public void onCheckInDialogNegativeClick(DialogFragment dialog) {
        // The user touches the negative Button Dialog
        dialog.dismiss();
        Intent intent = getIntent();
        intent.putExtra("loginData", loginData);
        intent.putExtra("myPicture", myPicture);
        intent.putExtra("isRestarted", true);
        finish();
        startActivity(intent);
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

            imgViewMyPicture.setOnClickListener(this);
            mySpinnerChildren = (Spinner) dialog.findViewById(R.id.spinnerChildren);

            mySpinnerChildren.setClickable(false); // disable the dropdown for version 1
            myChildrenAdapter = new MyChildrenAdapter(DashboardParentFragmentActivity.this, R.layout.spinner_row, (ArrayList<PatientChildData>) listMyChildren);
            mySpinnerChildren.setAdapter(myChildrenAdapter);
            mySpinnerChildren.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    byte[] childPhoto = listMyChildren.get(position).getPatientphoto();

                    txtUserName.setText("Baby "+listMyChildren.get(position).getFirtname());
                    if(childPhoto!=null) {
                        Bitmap bMap = BitmapFactory.decodeByteArray(childPhoto, 0, childPhoto.length);
                        imgViewMyPicture.setBackground(Utils.resizedBitmapDisplayUserPhoto(DashboardParentFragmentActivity.this, bMap));
                    }
//                    ParentDashboard(listMyChildren, position);
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
            case R.id.btnMyPicture:
                    dialog.dismiss();
                    Intent intent = getIntent();
                    intent.putExtra("loginData", loginData);
                    intent.putExtra("myPicture", myPicture);
                    intent.putExtra("isRestarted", true);
                    finish();
                    startActivity(intent);
                break;
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
                                BabyInfo(listMyChildren, mySpinnerChildren.getSelectedItemPosition());
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
                                GrowthTracker();
                                break;
                            case 1:
                                MedicationTracker();
                                break;
                            case 2:
                                ImmunizationTracker();
                                break;
                            case 3:
                                SymptomsTracker(listMyChildren, mySpinnerChildren.getSelectedItemPosition());
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
                                ClinicVisits();
                                break;
                            case 1:
                                CheckIn(listMyChildren, mySpinnerChildren.getSelectedItemPosition());
//                                Toast.makeText(getApplication(), "Please wait.", Toast.LENGTH_LONG).show();
//
//                                try {
//                                    CheckIfPINOfTheDayExists();
//                                } catch (ParseException e) {
//                                    e.printStackTrace();
//                                }
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
                                DoctorInformation(listMyChildren, mySpinnerChildren.getSelectedItemPosition());
                                break;
                            case 1:;
//                                Patients.unpinAllInBackground(ICareApplication.PARENT_GROUP, new DeleteCallback() {
//                                    @Override
//                                    public void done(ParseException e) {
//                                        if(e==null) {
//                                            Log.d("ROBERT", "PATIENTS NO ERROR");
//                                        } else {
//                                            Log.d("ROBERT", "PATIENTS ERROR :" + e.getMessage());
//                                        }
//                                    }
//                                });
//                                Visits.unpinAllInBackground(ICareApplication.PARENT_GROUP);
//                                MedsAndVaccines.unpinAllInBackground(ICareApplication.PARENT_GROUP);
//                                Doctors.unpinAllInBackground(ICareApplication.PARENT_GROUP, new DeleteCallback() {
//                                    @Override
//                                    public void done(ParseException e) {
//                                        if(e==null) {
//                                            Log.d("ROBERT", "DOCTORS NO ERROR");
//                                        } else {
//                                            Log.d("ROBERT", "DOCTORS ERROR :" + e.getMessage());
//                                        }
//                                    }
//                                });
                                ParseObject.unpinAllInBackground(ICareApplication.PARENT_GROUP, new DeleteCallback() {
                                    public void done(ParseException e) {
                                        if (e != null) {
                                            return;
                                        } else {
                                            ParseQuery<Doctors> query = Doctors.getQuery();
                                            query.fromLocalDatastore();

                                            query.findInBackground(new FindCallback<Doctors>() {
                                                @Override
                                                public void done(List<Doctors> doctorses, ParseException e) {
//                                                    ParseObject.unpinAll(ICareApplication.PARENT_GROUP, doctorses);
                                                    try {
                                                        ParseObject.unpinAll(doctorses);
                                                    } catch (ParseException e1) {
                                                        e1.printStackTrace();
                                                    }
                                                }
                                            });

//                                            Patients.unpinAllInBackground(ICareApplication.PATIENTS_GROUP);
//                                            Visits.unpinAllInBackground();
//                                            MedsAndVaccines.unpinAllInBackground();
//
////                                            ParseObject.unpinAllInBackground(ICareApplication.DOCTORS_LABEL);
////                                            Doctors.unpinAllInBackground();
//                                            Doctors.unpinAllInBackground(ICareApplication.DOCTORS_GROUP);
//                                            ParseObject.unpinAllInBackground(ICareApplication.USERS_LABEL, new DeleteCallback() {
//                                                public void done(ParseException e) {
//                                                    if (e == null) {
                                                        Intent intent = new Intent(DashboardParentFragmentActivity.this, LoginSignupActivity.class);
                                                        startActivity(intent);
                                                        finish();
//                                                    }
//                                                }
//                                            });
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
    public void ParentDashboard(List<PatientChildData> listMyChildren, int childPosition) {
        mFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        ParentDashboardFragment myFragment = new ParentDashboardFragment();
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        if(myFragment.isInLayout()) {
            transaction.remove(myFragment);
            myFragment = new ParentDashboardFragment();
        }

        Bundle bundle = getIntent().getExtras();

        bundle.putInt(ARG_CHILD_DATA, childPosition);
        bundle.putParcelableArrayList(ARG_CHILD_DATA, (ArrayList<PatientChildData>) listMyChildren);
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

    public void BabyInfo(List<PatientChildData> listMyChildren, int childPosition) {
        mFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        ParentBabyInfoFragment myFragment = new ParentBabyInfoFragment();

        Bundle bundle = getIntent().getExtras();

        bundle.putInt(ARG_CHILD_DATA, childPosition);
        bundle.putParcelableArrayList(ARG_CHILD_DATA, (ArrayList<PatientChildData>) listMyChildren);
        myFragment.setArguments(bundle);

        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.replace(R.id.alt_fragment_content_container, myFragment, "BABY_INFO_FRAGMENT");
        transaction.commit();
        // Fragment must implement the callback.
        if (!(myFragment instanceof OpenMenuCallbacks)) {
            throw new IllegalStateException(
                    "Fragment must implement the callbacks.");
        }
        mCallbacks = (OpenMenuCallbacks) myFragment;
    }
    public void GrowthTracker() {
        mFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        ParentGrowthTrackerFragment myFragment = new ParentGrowthTrackerFragment();
        Bundle bundle = getIntent().getExtras();

        myFragment.setArguments(bundle);

        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.replace(R.id.alt_fragment_content_container, myFragment, "GROWTH_TRACKER_FRAGMENT");
        transaction.commit();
        // Fragment must implement the callback.
        if (!(myFragment instanceof OpenMenuCallbacks)) {
            throw new IllegalStateException(
                    "Fragment must implement the callbacks.");
        }
        mCallbacks = (OpenMenuCallbacks) myFragment;
    }
    public void MedicationTracker() {
        mFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        ParentMedicationTrackerFragment myFragment = new ParentMedicationTrackerFragment();
        Bundle bundle = getIntent().getExtras();

        myFragment.setArguments(bundle);

        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.replace(R.id.alt_fragment_content_container, myFragment, "MEDICATION_TRACKER_FRAGMENT");
        transaction.commit();
        // Fragment must implement the callback.
        if (!(myFragment instanceof OpenMenuCallbacks)) {
            throw new IllegalStateException(
                    "Fragment must implement the callbacks.");
        }
        mCallbacks = (OpenMenuCallbacks) myFragment;
    }
    public void ImmunizationTracker() {
        mFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        ParentImmunizationTrackerFragment myFragment = new ParentImmunizationTrackerFragment();
        Bundle bundle = getIntent().getExtras();

        myFragment.setArguments(bundle);

        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.replace(R.id.alt_fragment_content_container, myFragment, "IMMUNIZATION_TRACKER_FRAGMENT");
        transaction.commit();
        // Fragment must implement the callback.
        if (!(myFragment instanceof OpenMenuCallbacks)) {
            throw new IllegalStateException(
                    "Fragment must implement the callbacks.");
        }
        mCallbacks = (OpenMenuCallbacks) myFragment;
    }
    public void SymptomsTracker(List<PatientChildData> listMyChildren, int childPosition) {
        mFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        ParentSymptomsTrackerFragment myFragment = new ParentSymptomsTrackerFragment();
        Bundle bundle = getIntent().getExtras();

        bundle.putInt(ARG_CHILD_DATA, childPosition);
        bundle.putParcelableArrayList(ARG_CHILD_DATA, (ArrayList<PatientChildData>) listMyChildren);
        myFragment.setArguments(bundle);

        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.replace(R.id.alt_fragment_content_container, myFragment, "IMMUNIZATION_TRACKER_FRAGMENT");
        transaction.commit();
        // Fragment must implement the callback.
        if (!(myFragment instanceof OpenMenuCallbacks)) {
            throw new IllegalStateException(
                    "Fragment must implement the callbacks.");
        }
        mCallbacks = (OpenMenuCallbacks) myFragment;
    }
    public void ClinicVisits() {
        mFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        ParentClinicVisitsFragment myFragment = new ParentClinicVisitsFragment();
        Bundle bundle = getIntent().getExtras();

        myFragment.setArguments(bundle);

        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.replace(R.id.alt_fragment_content_container, myFragment, "MEDICATION_TRACKER_FRAGMENT");
        transaction.commit();
        // Fragment must implement the callback.
        if (!(myFragment instanceof OpenMenuCallbacks)) {
            throw new IllegalStateException(
                    "Fragment must implement the callbacks.");
        }
        mCallbacks = (OpenMenuCallbacks) myFragment;
    }
    public void CheckIn(List<PatientChildData> listMyChildren, int childPosition) {
        mFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        ParentMyClinicCheckInFragment myFragment = new ParentMyClinicCheckInFragment();
        Bundle bundle = getIntent().getExtras();

        bundle.putSerializable(ARG_LOGIN_DATA, loginData);
        bundle.putInt(ARG_CHILD_DATA_POS, childPosition);
        bundle.putParcelableArrayList(ARG_CHILD_DATA, (ArrayList<PatientChildData>) listMyChildren);
        myFragment.setArguments(bundle);

        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.replace(R.id.alt_fragment_content_container, myFragment, "IMMUNIZATION_TRACKER_FRAGMENT");
        transaction.commit();
        // Fragment must implement the callback.
        if (!(myFragment instanceof OpenMenuCallbacks)) {
            throw new IllegalStateException(
                    "Fragment must implement the callbacks.");
        }
        mCallbacks = (OpenMenuCallbacks) myFragment;
    }

    public void DoctorInformation(List<PatientChildData> listMyChildren, int childPosition) {
        mFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        ParentDoctorInformationFragment myFragment = new ParentDoctorInformationFragment();

        Bundle bundle = getIntent().getExtras();

        bundle.putInt(ARG_CHILD_DATA, childPosition);
        bundle.putParcelableArrayList(ARG_CHILD_DATA, (ArrayList<PatientChildData>) listMyChildren);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //first check if we saved any data about the origin of this request (which fragment)
        final Bundle request = this.requests.get(requestCode, null);
        if (request != null) {
            //find the indices-array
            final int[] indices = request.getIntArray("indices");

            FragmentManager fm = this.getSupportFragmentManager();
            Fragment f = null;
            //loop backwards
            for(int i = indices.length - 1 ; i >= 0 ; i--) {
                if (fm != null) {
                    //find a list of active fragments
                    List<Fragment> flist = fm.getFragments();
                    if (flist != null && indices[i] < flist.size()) {
                        f = flist.get(indices[i]);
                        fm = f.getChildFragmentManager();
                    }
                }
            }
            //we found our fragment that initiated the request to startActivityForResult, give it the callback!
            if (f != null) {
                f.onActivityResult(requestCode, resultCode, data);
                return ;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private class CheckIfPINOfTheDayMatch extends AsyncTask<Void, Void, Integer> {
        Context context;
        DialogFragment dialog;
        String doctorID;
        String pinOfTheDay;
        ProgressDialog mProgressDialog;

        public CheckIfPINOfTheDayMatch(Context context, DialogFragment dialog, String objectId, String pinOfTheDay) {
            this.context = context;
            this.dialog = dialog;
            this.doctorID = objectId;
            this.pinOfTheDay = pinOfTheDay;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog

            mProgressDialog = Utils.createProgressDialog(context);
            mProgressDialog.show();

        }

        @Override
        protected Integer doInBackground(Void... params) {
            // Create the array
            final Date midnight = new Date();
            midnight.setHours(0);
            midnight.setMinutes(0);
            midnight.setSeconds(0);

            final Date elevenfiftynine = new Date();
            elevenfiftynine.setHours(23);
            elevenfiftynine.setMinutes(59);
            elevenfiftynine.setSeconds(59);

            Log.d("ROBERT MID", midnight.toString());
            Log.d("ROBERT 1159", elevenfiftynine.toString());
            ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(ICareApplication.SETTINGS_LABEL);
            query.whereEqualTo("doctorid", doctorID);
            query.whereEqualTo("pin", pinOfTheDay);
            query.whereGreaterThan("updatedAt", midnight);
            query.whereLessThan("updatedAt", elevenfiftynine);

            query.countInBackground(new CountCallback() {
                @Override
                public void done(int i, ParseException e) {
                    if(i == 0) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mProgressDialog.dismiss();
                                //showAlertDialog("error", doctorID);
                                //Toast.makeText(DashboardParentFragmentActivity.this, "PIN does not match.", Toast.LENGTH_LONG).show();
                                showAlertDialog("error", doctorID);
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();
                                mProgressDialog.dismiss();

                            }
                        });
                    }
                }
            });
            return 0;
        }

        @Override
        protected void onPostExecute(Integer result) {
        }
    }


}
