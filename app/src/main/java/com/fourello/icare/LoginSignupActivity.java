package com.fourello.icare;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.fourello.icare.view.CustomButton;
import com.fourello.icare.view.CustomEditTextView;
import com.fourello.icare.widgets.ParseProxyObject;
import com.fourello.icare.widgets.Utils;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.util.List;


public class    LoginSignupActivity extends Activity {

    private CustomEditTextView eTxtUsername;
    private CustomEditTextView eTxtPassword;

//    private DoctorInfoOperations mDoctorOperationsHelper;
//    private UserInfoOperations mUsersOperationsHelper;

    private ProgressDialog mProgressDialog;

    public ParseProxyObject loginData;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getActionBar().hide();
        setContentView(R.layout.activity_login_signup);

        CustomButton btnLogin = (CustomButton)findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mProgressDialog == null) {
                    mProgressDialog = Utils.createProgressDialog(LoginSignupActivity.this);
                    mProgressDialog.show();
                } else {
                    mProgressDialog.show();
                }
                eTxtUsername = (CustomEditTextView)findViewById(R.id.etUsername);
                eTxtPassword = (CustomEditTextView)findViewById(R.id.etPassword);

                String usernameTxt = eTxtUsername.getText().toString();
                String passwordTxt = eTxtPassword.getText().toString();

                Boolean is_email = ICareApplication.isEmailValid(usernameTxt);
                if(!is_email) {

                    // input data is for Doctors or Secretary
                    ParseQuery<ParseObject> query = ParseQuery.getQuery(ICareApplication.DOCTORS_LABEL);
                    query.whereEqualTo("username", usernameTxt);
                    query.whereEqualTo("password", passwordTxt);
                    query.findInBackground(new FindCallback<ParseObject>() {
                        public void done(List<ParseObject> objects, ParseException e) {
                            if (e == null) {
                                if(objects.size() == 0) {
                                    usernameOrPasswordIsInvalid();
                                } else {
                                    //Login success
                                    ParseObject.pinAllInBackground(ICareApplication.DOCTORS_LABEL, objects);

                                    final ParseObject project = objects.get(0);

                                    if(project.has("photoFile")) {
                                        final ParseFile myPhoto = (ParseFile) project.get("photoFile");
                                        Log.d("ROBERT URL", myPhoto.getUrl()+" -URL");
                                        myPhoto.saveInBackground(new SaveCallback() {
                                            @Override
                                            public void done(ParseException e) {
                                                if (e == null) {
                                                    myPhoto.getDataInBackground(new GetDataCallback() {
                                                        @Override
                                                        public void done(byte[] bytes, ParseException e) {
                                                            if (e == null) {
                                                                byte[] myPicture = bytes;
                                                                loginData = new ParseProxyObject(project);
                                                                loginSuccessful(loginData, myPicture);
                                                            }
                                                        }
                                                    });
                                                }
                                            }
                                        });
                                    } else {
                                        Drawable drawable= getResources().getDrawable(R.drawable.doctor_icon);
                                        Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
                                        ByteArrayOutputStream out = new ByteArrayOutputStream();
                                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

                                        byte[] myPicture = out.toByteArray();

                                        loginData = new ParseProxyObject(project);
                                        loginSuccessful(loginData, myPicture);
                                    }
                                }
                            } else {
                                somethingWentWrong();
                            }

                        }
                    });
                } else {
                    // input data is for Parents
                    ParseQuery<ParseObject> query = ParseQuery.getQuery(ICareApplication.USERS_LABEL);
                    query.whereExists("firstname");
                    query.whereExists("lastname");
                    query.whereExists("role");
                    query.whereEqualTo("email", usernameTxt);
                    query.whereEqualTo("password", passwordTxt);
                    query.findInBackground(new FindCallback<ParseObject>() {
                        public void done(List<ParseObject> objects, ParseException e) {
                            if (e == null) {
                                if(objects.size() == 0) {
                                    usernameOrPasswordIsInvalid();
                                } else {
                                    //Login success
                                    ParseObject.pinAllInBackground(ICareApplication.PARENT_GROUP, objects);
                                    final ParseObject project = objects.get(0);

                                    loginData = new ParseProxyObject(project);

                                    loginSuccessfulParent(loginData);
                                }
                            } else {
                                somethingWentWrong();
                            }

                        }
                    });

                }
            }
        });

        CustomButton btnSign_up = (CustomButton)findViewById(R.id.btnSign_up);
        btnSign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginSignupActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        //overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
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

    public void somethingWentWrong() {
        Toast.makeText(getApplicationContext(), "Oops! Something went wrong.", Toast.LENGTH_LONG).show();
        mProgressDialog.dismiss();
    }

    public void usernameOrPasswordIsInvalid() {
        Toast.makeText(getApplicationContext(), "No such user exist, please signup", Toast.LENGTH_LONG).show();
        mProgressDialog.dismiss();
    }

    public void loginSuccessful(ParseProxyObject loginData, byte[] myPicture) {
        mProgressDialog.dismiss();
        Intent intent = new Intent(LoginSignupActivity.this, DashboardDoctorFragmentActivity.class);
        intent.putExtra("loginData", loginData);
        if(myPicture != null) {
            intent.putExtra("myPicture", myPicture);
        }
        startActivity(intent);

        Toast.makeText(getApplicationContext(), "Successfully Logged in", Toast.LENGTH_LONG).show();
        finish();
    }

    public void loginSuccessfulParent(ParseProxyObject loginData) {
        Log.d("ROBERT LOGINDATA", loginData.getValues().toString());
        mProgressDialog.dismiss();
        // Get Children Sync
        Intent intent = new Intent(LoginSignupActivity.this, SyncParentDataActivity.class);

        intent.putExtra("loginData", loginData);
        startActivity(intent);
        //Toast.makeText(getApplicationContext(), "Successfully Logged in", Toast.LENGTH_LONG).show();
        finish();
    }


}
