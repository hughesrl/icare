package com.fourello.icare;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.fourello.icare.widgets.ParseProxyObject;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.List;

/**
 * Created by Admin on 8/2/2014.
 */
public class MainActivity extends Activity {

    public int doctor_or_user_count = 0;
    public ParseProxyObject loginData;
    public byte[] myPicture;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Determine whether the current user is an anonymous user
        ParseQuery<ParseObject> queryDoctors = ParseQuery.getQuery(ICareApplication.DOCTORS_LABEL);
        queryDoctors.fromLocalDatastore();
        queryDoctors.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objectsDoctor, ParseException e) {
                if (e == null) {
                    int doctorInTable = objectsDoctor.size();
                    doctor_or_user_count += objectsDoctor.size();

                    if(doctorInTable > 0) {
                        final ParseObject project = objectsDoctor.get(0);
                        if(project.has("photoFile")) {
                            ParseFile myPhoto = (ParseFile) project.get("photoFile");

                            myPhoto.getDataInBackground(new GetDataCallback() {
                                @Override
                                public void done(byte[] bytes, ParseException e) {
                                    if (e == null) {
                                        myPicture = bytes;
                                    }
                                    loginData = new ParseProxyObject(project);
                                }
                            });
                        } else {
                            Drawable drawable= getResources().getDrawable(R.drawable.doctor_icon);
                            Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
                            ByteArrayOutputStream out = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);

                            myPicture = out.toByteArray();

                            loginData = new ParseProxyObject(project);
                        }

                        // Doctor
                        Intent intent = new Intent(MainActivity.this, DashboardDoctorFragmentActivity.class);
                        intent.putExtra("loginData", loginData);
                        intent.putExtra("myPicture", myPicture);
                        startActivity(intent);
                        finish();
                    } else {
                        ParseQuery<ParseObject> queryUsers = ParseQuery.getQuery(ICareApplication.USERS_LABEL);
                        queryUsers.fromLocalDatastore();
                        queryUsers.findInBackground(new FindCallback<ParseObject>() {
                            public void done(List<ParseObject> objectUsers, ParseException e) {
                                if (e == null) {
                                    int userInTable = objectUsers.size();
                                    doctor_or_user_count += objectUsers.size();
                                    if (userInTable > 0) {
                                        ParseObject users = objectUsers.get(0);
                                        ParseProxyObject userLoginData = new ParseProxyObject(users);

                                        Intent intent = new Intent(MainActivity.this, DashboardParentFragmentActivity.class);
                                        intent.putExtra("loginData", userLoginData);

                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Intent intent = new Intent(MainActivity.this, LoginSignupActivity.class);
                                        startActivity(intent);
                                        finish();

//                                        if (doctor_or_user_count == 0) {
//                                            Intent intent = new Intent(MainActivity.this, LoginSignupActivity.class);
//                                            startActivity(intent);
//                                            finish();
//                                        } else if (doctor_or_user_count == 1) {
//                                            //Login success for Doctors / Secretary
//                                            Intent intent = new Intent(MainActivity.this, DashboardDoctorFragmentActivity.class);
//                                            intent.putExtra("loginData", loginData);
//                                            intent.putExtra("myPicture", myPicture);
//                                            startActivity(intent);
//                                            finish();
//                                        } else if (doctor_or_user_count == 2) {
//                                            //Login success for Parents
//                                            Intent intent = new Intent(MainActivity.this, DashboardParentFragmentActivity.class);
//                                            startActivity(intent);
//                                            finish();
//                                        } else {
//                                            Intent intent = new Intent(MainActivity.this, LoginSignupActivity.class);
//                                            startActivity(intent);
//                                            finish();
//                                        }
                                    }
                                } else {
                                    Log.d("robert", e.getLocalizedMessage());
                                    Intent intent = new Intent(MainActivity.this, LoginSignupActivity.class);
                                    startActivity(intent);
                                    finish();
                                }

                            }
                        });
                    }

                } else {
//                    Intent intent = new Intent(MainActivity.this, LoginSignupActivity.class);
//                    startActivity(intent);
//                    finish();
                }

            }
        });




//        query.findInBackground(new FindCallback<ParseObject>() {
//            public void done(List<ParseObject> objects, ParseException e) {
//                if (e == null) {
//                    if(objects.size() == 0) {
//                        // If user is anonymous, send the user to LoginSignupActivity.class
//
//                    } else {
//
//                    }
//                } else {
//                    Intent intent = new Intent(MainActivity.this,
//                            LoginSignupActivity.class);
//                    startActivity(intent);
//                    finish();
//                }
//
//            }
//        });
//        if (ParseAnonymousUtils.isLinked(ParseUser.getCurrentUser())) {
//            // If user is anonymous, send the user to LoginSignupActivity.class
//            Intent intent = new Intent(MainActivity.this,
//                    LoginSignupActivity.class);
//            startActivity(intent);
//            finish();
//        } else {
//            // If current user is NOT anonymous user
//            // Get current user data from Parse.com
//            ParseUser currentUser = ParseUser.getCurrentUser();
//            if (currentUser != null) {
//                // Send logged in users to Welcome.class
//                Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
//                startActivity(intent);
//                finish();
//            } else {
//                // Send user to LoginSignupActivity.class
//                Intent intent = new Intent(MainActivity.this,
//                        LoginSignupActivity.class);
//                startActivity(intent);
//                finish();
//            }
//        }

    }



}