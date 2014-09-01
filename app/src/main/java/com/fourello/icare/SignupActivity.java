package com.fourello.icare;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.fourello.icare.view.CustomButton;
import com.fourello.icare.view.CustomEditTextView;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignupActivity extends Activity {

    private CustomEditTextView eTxtFirstname;
    private CustomEditTextView eTxtLastname;
    private CustomEditTextView eTxtEmail;
    //private CustomEditTextView eTxtUsername;
    private CustomEditTextView eTxtPassword;
    private CustomEditTextView eTxtConfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getActionBar().hide();
        setContentView(R.layout.activity_signup);

        CustomButton btnSing_up = (CustomButton)findViewById(R.id.btnSign_up);
        btnSing_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                eTxtFirstname = (CustomEditTextView)findViewById(R.id.etFirstname);
                eTxtLastname = (CustomEditTextView)findViewById(R.id.etLastname);
                eTxtEmail = (CustomEditTextView)findViewById(R.id.etEmail);
                eTxtPassword = (CustomEditTextView)findViewById(R.id.etPassword);
                //eTxtUsername = (CustomEditTextView)findViewById(R.id.etUsername);
                eTxtConfirmPassword = (CustomEditTextView)findViewById(R.id.etConfirmPassword);

                // Retrieve the text entered from the EditText
                String firstnameTxt = eTxtFirstname.getText().toString();
                String lastnameTxt = eTxtLastname.getText().toString();
                final String emailTxt = eTxtEmail.getText().toString();
                String passwordTxt = eTxtPassword.getText().toString();
                final String confirmpasswordTxt = eTxtConfirmPassword.getText().toString();


                // Force user to fill up the form
                if (firstnameTxt.equals("") &&
                        lastnameTxt.equals("") &&
                        emailTxt.equals("")  &&
                        passwordTxt.equals("") &&
                        confirmpasswordTxt.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please complete the sign up form", Toast.LENGTH_LONG).show();

                } else {
                    // Save new user data into Parse.com Data Storage
                    ParseUser user = new ParseUser();
                    user.setEmail(emailTxt);
                    user.setUsername(emailTxt);
                    user.setPassword(confirmpasswordTxt);

                    user.put("firstname", firstnameTxt);
                    user.put("lastname", lastnameTxt);

//                    if(passwordTxt.equalsIgnoreCase(confirmpasswordTxt)) {
//                        Toast.makeText(getApplicationContext(), "Password does not match!", Toast.LENGTH_LONG).show();
//                    } else {
                        user.signUpInBackground(new SignUpCallback() {
                            public void done(ParseException e) {
                                if (e == null) {
                                    Intent intent = new Intent(SignupActivity.this, DashboardDoctorFragmentActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);

                                    // Show a simple Toast message upon successful registration
                                    Toast.makeText(getApplicationContext(), "Successfully Signed up, logging in.", Toast.LENGTH_LONG).show();

                                    finish();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Sign up Error", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
//                    }
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
}
