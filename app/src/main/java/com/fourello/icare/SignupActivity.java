package com.fourello.icare;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fourello.icare.datas.SpinnerItems;
import com.fourello.icare.view.CustomButton;
import com.fourello.icare.view.CustomEditTextView;
import com.fourello.icare.widgets.Utils;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import java.util.ArrayList;

public class SignupActivity extends Activity {

    private ProgressDialog mProgressDialog;

    private CustomEditTextView eTxtFirstname;
    private CustomEditTextView eTxtLastname;
    private CustomEditTextView eTxtEmail;
    private Spinner customSpinnerRelationship;

    private CustomEditTextView eTxtPassword;
    private CustomEditTextView eTxtConfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getActionBar().hide();
        setContentView(R.layout.activity_signup);

        eTxtFirstname = (CustomEditTextView)findViewById(R.id.etFirstname);
        eTxtLastname = (CustomEditTextView)findViewById(R.id.etLastname);
        customSpinnerRelationship = (Spinner) findViewById(R.id.spinnerRelationship);

        CustomAdapter adapter = new CustomAdapter(SignupActivity.this, android.R.layout.simple_spinner_item, ICareApplication.populateRelationshipToPatient());
        customSpinnerRelationship.setAdapter(adapter);

        eTxtEmail = (CustomEditTextView)findViewById(R.id.etEmail);
        eTxtPassword = (CustomEditTextView)findViewById(R.id.etPassword);
        eTxtConfirmPassword = (CustomEditTextView)findViewById(R.id.etConfirmPassword);

        CustomButton btnSing_up = (CustomButton)findViewById(R.id.btnSign_up);
        btnSing_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mProgressDialog == null) {
                    mProgressDialog = Utils.createProgressDialog(SignupActivity.this);
                    mProgressDialog.show();
                } else {
                    mProgressDialog.show();
                }

                // Retrieve the text entered from the EditText
                String firstnameTxt = eTxtFirstname.getText().toString();
                String lastnameTxt = eTxtLastname.getText().toString();

                final String strRelationship = customSpinnerRelationship.getSelectedItem().toString();
                final String emailTxt = eTxtEmail.getText().toString();
                String passwordTxt = eTxtPassword.getText().toString();
                final String confirmpasswordTxt = eTxtConfirmPassword.getText().toString();


                // Force user to fill up the form
                if (firstnameTxt.equals("") &&
                        lastnameTxt.equals("") &&
                        strRelationship.equals("") &&
                        emailTxt.equals("")  &&
                        passwordTxt.equals("") &&
                        confirmpasswordTxt.equals("")) {
                    mProgressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Please complete the sign up form", Toast.LENGTH_LONG).show();
                } else {
                    if (passwordTxt.trim().equals(confirmpasswordTxt)) {
                        // Save new user data into Parse.com Data Storage
                        final ParseObject newUser = new ParseObject(ICareApplication.USERS_LABEL);
                        newUser.put("firstname", firstnameTxt);
                        newUser.put("lastname", lastnameTxt);
                        newUser.put("email", emailTxt);
                        newUser.put("password", confirmpasswordTxt);
                        newUser.put("role", strRelationship);

                        newUser.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    mProgressDialog.dismiss();
                                    Intent intent = new Intent(SignupActivity.this, LoginSignupActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });
                    } else {
                        mProgressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Password not the same.", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
//        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.signup, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
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

            LayoutInflater inflater = LayoutInflater.from(SignupActivity.this);
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
}
