package com.fourello.icare;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fourello.icare.datas.Doctors;
import com.fourello.icare.datas.MedsAndVaccines;
import com.fourello.icare.datas.Patients;
import com.fourello.icare.datas.SpinnerItems;
import com.fourello.icare.datas.Visits;
import com.fourello.icare.view.CustomButton;
import com.fourello.icare.view.CustomEditTextView;
import com.fourello.icare.view.CustomTextView;
import com.fourello.icare.widgets.ParseProxyObject;
import com.fourello.icare.widgets.Utils;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class SyncParentDataActivity extends Activity {

    private ProgressDialog mProgressDialog;
    public ParseProxyObject loginData;

    private LinearLayout loadingLayout;
    private ScrollView addBabyLayout;

    private ImageView patient_photo;

    private byte[] bytearray;
    private Patients myChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getActionBar().hide();
        setContentView(R.layout.sync_parent_children);
        Intent extras = getIntent();
        loginData = (ParseProxyObject) extras.getSerializableExtra("loginData");

        myChild = new Patients();

        loadingLayout = (LinearLayout)findViewById(R.id.loadingLayout);
        addBabyLayout = (ScrollView)findViewById(R.id.addBabyLayout);

        // Create the array
        // Get Children Sync
        loadPatientFromParse(loginData.getString("email"));

//        ParseQuery<ParseObject> queryCount = new ParseQuery<ParseObject>(ICareApplication.PATIENTS_LABEL);
//        queryCount.whereEqualTo("email", loginData.getString("email"));
//        queryCount.addAscendingOrder("firstname");
//        queryCount.countInBackground(new CountCallback() {
//            @Override
//            public void done(int i, ParseException e) {
//                if(e == null) {
//                    if(i == 0) {
//                        loadingLayout.setVisibility(View.GONE);
//                        addBabyLayout.setVisibility(View.VISIBLE);
//                    } else {
//                        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(ICareApplication.PATIENTS_LABEL);
//                        query.whereEqualTo("email", loginData.getString("email"));
//                        query.addAscendingOrder("firstname");
//                        query.findInBackground(new FindCallback<ParseObject>() {
//                            @Override
//                            public void done(List<ParseObject> parseObjects, ParseException e) {
//                                if (e == null) {
//                                    ParseObject.pinAllInBackground(ICareApplication.PATIENTS_LABEL, parseObjects, new SaveCallback() {
//                                        @Override
//                                        public void done(ParseException e) {
//                                            if (e == null) {
//                                                // Get Children from LocalDatastore
//                                                ParseQuery<ParseObject> queryChildren = ParseQuery.getQuery(ICareApplication.PATIENTS_LABEL);
//                                                queryChildren.fromLocalDatastore();
//                                                queryChildren.findInBackground(new FindCallback<ParseObject>() {
//                                                    @Override
//                                                    public void done(List<ParseObject> parseObjects, ParseException e) {
//                                                        if (e == null) {
//                                                            for (int i = 0; i < parseObjects.size(); i++) {
//                                                                ParseObject patientObjects = parseObjects.get(i);
//
//                                                                Log.d("ROBERT", patientObjects.getObjectId());
//                                                                Log.d("ROBERT", i + " OF " + parseObjects.size());
//                                                            }
//
//                                                            Intent intent = new Intent(SyncParentDataActivity.this, DashboardParentFragmentActivity.class);
//                                                            intent.putExtra("loginData", loginData);
//                                                            startActivity(intent);
//                                                        }
//                                                    }
//                                                });
//
//                                            }
//                                        }
//                                    });
//                                }
//                            }
//                        });
//                    }
//                }
//            }
//        });


        Spinner spinnerGender = (Spinner) findViewById(R.id.spinnerGender);
        CustomAdapter adapter = new CustomAdapter(SyncParentDataActivity.this, android.R.layout.simple_spinner_item, ICareApplication.populateGender());
        spinnerGender.setAdapter(adapter);

        patient_photo = (ImageView) findViewById(R.id.patient_photo);

        patient_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        CustomButton btnAddBabyDone = (CustomButton)findViewById(R.id.btnAddBabyDone);
        btnAddBabyDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mProgressDialog == null) {
                    mProgressDialog = Utils.createProgressDialog(SyncParentDataActivity.this);
                    mProgressDialog.show();
                } else {
                    mProgressDialog.show();
                }

                final CustomEditTextView etFirstname = (CustomEditTextView) findViewById(R.id.etFirstname);
                final CustomEditTextView etMiddlename = (CustomEditTextView) findViewById(R.id.etMiddlename);
                final CustomEditTextView etLastname = (CustomEditTextView) findViewById(R.id.etLastname);
                final Spinner spinnerGender = (Spinner) findViewById(R.id.spinnerGender);
                final CustomTextView etBirthday = (CustomTextView) findViewById(R.id.spinnerBirthday);

                final String strGender = spinnerGender.getSelectedItem().toString();

                final ParseQuery<ParseObject> query = ParseQuery.getQuery(ICareApplication.USERS_LABEL);
                query.whereEqualTo("email", loginData.getString("email"));
                query.findInBackground(new FindCallback<ParseObject>() {
                    public void done(List<ParseObject> objects, ParseException e) {
                        final ParseObject project = objects.get(0);
                        String parentObjectId = project.getObjectId();
                        Log.d("ObjectId", parentObjectId);
                        if (e == null) {
                            if (objects.size() == 0) {
                                Toast.makeText(SyncParentDataActivity.this, "Invalid Email", Toast.LENGTH_LONG).show();
                            } else {

                                myChild.setFirstName(etFirstname.getText().toString());
                                myChild.setMiddleName(etMiddlename.getText().toString());
                                myChild.setLastName(etLastname.getText().toString());
                                myChild.setGender(strGender);

                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd, yyyy HH:mm");
                                Date convertedDate = new Date();
                                try {
                                    String birthdayAndTime = etBirthday.getText().toString().trim() + " 00:00";
                                    convertedDate = simpleDateFormat.parse(birthdayAndTime);
                                } catch (java.text.ParseException e1) {
                                    e1.printStackTrace();
                                }
                                myChild.setDateOfBirth(convertedDate);

                                myChild.setDoctorId(loginData.getString("linked_doctorid"));
                                myChild.setEmail(project.getString("email"));

                                if (project.getString("role").equalsIgnoreCase("mother")) {
                                    myChild.setMotherEmail(project.getString("email"));
                                    myChild.setMotherFirstName(project.getString("firstname"));
                                    myChild.setMotherLastName(project.getString("lastname"));
                                } else if (project.getString("role").equalsIgnoreCase("father")) {
                                    myChild.setFatherEmail(project.getString("email"));
                                    myChild.setFatherFirstName(project.getString("firstname"));
                                    myChild.setFatherLastName(project.getString("lastname"));
                                } else {
                                    myChild.setMotherEmail(project.getString("email"));
                                    myChild.setMotherFirstName(project.getString("firstname"));
                                    myChild.setMotherLastName(project.getString("lastname"));
                                }

                                myChild.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException myChildError) {
                                        if (myChildError == null) {
                                            loadPatientFromParse(loginData.getString("email"));
                                        } else {
                                            Toast.makeText(
                                                    SyncParentDataActivity.this,
                                                    "Error saving: " + myChildError.getMessage(),
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        }
                    }
                });
            }
      });
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

            LayoutInflater inflater = LayoutInflater.from(SyncParentDataActivity.this);
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

    private void selectImage() {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(SyncParentDataActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")){
                    try {
                        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(captureIntent, 1);
                    } catch (ActivityNotFoundException anfe) {
                        //display an error message
                        String errorMessage = "Whoops - your device doesn't support capturing images!";
                        Toast toast = Toast.makeText(SyncParentDataActivity.this, errorMessage, Toast.LENGTH_SHORT);
                        toast.show();
                    }
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1){
            if (resultCode == RESULT_OK) { // Take Photo
                if (mProgressDialog == null) {
                    mProgressDialog = Utils.createProgressDialog(SyncParentDataActivity.this);
                    mProgressDialog.show();
                } else {
                    mProgressDialog.show();
                }

                final Bitmap thePic = data.getExtras().getParcelable("data");

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                thePic.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                bytearray = stream.toByteArray();// get byte array here
                Log.d("ROBERT bytearray", bytearray.toString()+"");
                if (bytearray != null) {
                    final ParseFile photoFile = new ParseFile("photo.jpg", bytearray);
                    photoFile.saveInBackground(new SaveCallback() {
                        public void done(ParseException e) {
                            if (e != null) {
                                Toast.makeText(SyncParentDataActivity.this,
                                        "Error saving: " + e.getMessage(),
                                        Toast.LENGTH_LONG).show();
                            } else {
                                myChild.setPhotoFile(photoFile);
                                patient_photo.setBackground(Utils.resizedBitmapDisplay(SyncParentDataActivity.this, thePic));

                                mProgressDialog.dismiss();
                            }
                        }
                    });
                }

            }
        }else if (requestCode == 2){
            if (resultCode == RESULT_OK) {
                if (mProgressDialog == null) {
                    mProgressDialog = Utils.createProgressDialog(SyncParentDataActivity.this);
                    mProgressDialog.show();
                } else {
                    mProgressDialog.show();
                }
                Uri selectedImage = data.getData();
                // h=1;
                //imgui = selectedImage;
                String[] filePath = { MediaStore.Images.Media.DATA };
                Cursor c = getContentResolver().query(selectedImage,filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                final String picturePath = c.getString(columnIndex);
                c.close();
                final Bitmap thePic = (BitmapFactory.decodeFile(picturePath));
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                thePic.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                bytearray = stream.toByteArray();// get byte array here
                Log.d("ROBERT bytearray", bytearray.toString()+"");
                if (bytearray != null) {
                    final ParseFile photoFile = new ParseFile(loginData.getString("objectId")+"_photo.jpg", bytearray);
                    photoFile.saveInBackground(new SaveCallback() {
                        public void done(ParseException e) {
                            if (e != null) {
                                Toast.makeText(SyncParentDataActivity.this,
                                        "Error saving: " + e.getMessage(),
                                        Toast.LENGTH_LONG).show();
                            } else {
                                Log.w("path of image from gallery......******************.........", picturePath + "");
                                myChild.setPhotoFile(photoFile);
                                patient_photo.setBackground(Utils.resizedBitmapDisplay(SyncParentDataActivity.this, thePic));
                                mProgressDialog.dismiss();
                            }
                        }
                    });
                }
            }
        }
    }

    private void loadPatientFromParse(String email) {
        ParseQuery<Patients> query = Patients.getQuery();
        query.whereEqualTo("email", email);
        query.addAscendingOrder("firstname");
        query.setLimit(1);
        query.findInBackground(new FindCallback<Patients>() {
            public void done(final List<Patients> patientObject, ParseException e) {
                if (e == null) {
                    if (patientObject.size() == 0) {
                        loadingLayout.setVisibility(View.GONE);
                        addBabyLayout.setVisibility(View.VISIBLE);
                    } else {
                        Patients.pinAllInBackground(patientObject,
                            new SaveCallback() {
                                public void done(ParseException e) {
                                    if (e == null) {
                                        if (!isFinishing()) {
                                            if(!patientObject.get(0).getDoctorId().isEmpty()) {
                                                Log.i("DOCTOR", patientObject.get(0).getDoctorId()+"");
                                                loadDoctorFromParse(Integer.parseInt(patientObject.get(0).getDoctorId()));
                                            }
                                            loadVisitsFromParse(patientObject.get(0).getObjectId());

                                            loadMedsAndVaccinesFromParse(patientObject.get(0).getObjectId());

                                            Intent intent = new Intent(SyncParentDataActivity.this, DashboardParentFragmentActivity.class);
                                            intent.putExtra("loginData", loginData);
                                            startActivity(intent);
                                            SyncParentDataActivity.this.finish();
                                            Log.i("patient", "DATA SAVED : SIZE:" + patientObject.size() +
                                                    " OBJECTID:" + patientObject.get(0).getObjectId());
                                        }
                                    } else {
                                        Log.i("patient",
                                                "Error pinning todos: "
                                                        + e.getMessage());
                                    }
                                }
                            }
                        );
                    }

                } else {
                    Log.i("patient",
                            "loadFromParse: Error finding pinned patient: "
                                    + e.getMessage());
                }
            }
        });
    }

    private void loadDoctorFromParse(int doctorID) { // DoctorId
        ParseQuery<Doctors> query = Doctors.getQuery();
        query.whereEqualTo("doctorID", doctorID);
        query.getFirstInBackground(new GetCallback<Doctors>() {
            @Override
            public void done(Doctors doctors, ParseException e) {
                if(e == null) {
                    doctors.pinInBackground(
                            new SaveCallback() {
                                public void done(ParseException e) {
                                    if (e == null) {
                                        if (!isFinishing()) {
                                            Log.i("ROBERT DOCTOR", "DATA SAVED : ");
                                        }
                                    } else {
                                        Log.i("ROBERT DOCTOR", "Error pinning todos: " + e.getMessage());
                                    }
                                }
                            }
                    );
                } else {
                    Log.i("ROBERT DOCTOR", "loadFromParse: Error finding pinned doctor: " + e.getMessage());
                }
            }
        });
    }

    private void loadVisitsFromParse(String objectId) {
        ParseQuery<Visits> query = Visits.getQuery();
        query.whereEqualTo("patientid", objectId);
        query.findInBackground(new FindCallback<Visits>() {
            public void done(final List<Visits> visitsObject, ParseException e) {
                if (e == null) {
                    Visits.pinAllInBackground(visitsObject,
                            new SaveCallback() {
                                public void done(ParseException e) {
                                    if (e == null) {
                                        if (!isFinishing()) {
                                            Log.i("loadVisitsFromParse", "DATA SAVED : "+visitsObject.size());
                                        }
                                    } else {
                                        Log.i("loadVisitsFromParse",
                                                "Error pinning todos: "
                                                        + e.getMessage());
                                    }
                                }
                            });
                } else {
                    Log.i("VisitsList",
                            "loadFromParse: Error finding pinned visits: "
                                    + e.getMessage());
                }
            }
        });
    }
    private void loadMedsAndVaccinesFromParse(String objectId) {
        ParseQuery<MedsAndVaccines> query = MedsAndVaccines.getQuery();
        query.whereEqualTo("patientid", objectId);
        query.findInBackground(new FindCallback<MedsAndVaccines>() {
            public void done(final List<MedsAndVaccines> medsAndVaccinesObject, ParseException e) {
                if (e == null) {
                    MedsAndVaccines.pinAllInBackground(medsAndVaccinesObject,
                            new SaveCallback() {
                                public void done(ParseException e) {
                                    if (e == null) {
                                        if (!isFinishing()) {
                                            Log.i("loadMedsAndVaccinesFromParse", "DATA SAVED : "+medsAndVaccinesObject.size());
                                        }
                                    } else {
                                        Log.i("loadMedsAndVaccinesFromParse",
                                                "Error pinning todos: "
                                                        + e.getMessage());
                                    }
                                }
                            });
                } else {
                    Log.i("VisitsList",
                            "loadFromParse: Error finding pinned visits: "
                                    + e.getMessage());
                }
            }
        });
    }
}
