package com.fourello.icare;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.fourello.icare.datas.MyChildren;
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
import java.util.ArrayList;
import java.util.List;


public class SyncParentDataActivity extends Activity {

    public ParseProxyObject loginData;

    private ProgressDialog mProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getActionBar().hide();
        setContentView(R.layout.sync_parent_children);
        Intent extras = getIntent();
        loginData = (ParseProxyObject) extras.getSerializableExtra("loginData");

        if (mProgressDialog == null) {
            mProgressDialog = Utils.createProgressDialogWithMessage(SyncParentDataActivity.this, "Syncing");
            mProgressDialog.show();
        } else {
            mProgressDialog.show();
        }
        // Create the array
        // Get Children Sync
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(ICareApplication.PATIENTS_LABEL);
        query.whereEqualTo("email", loginData.getString("email"));
        query.addAscendingOrder("firstname");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (e == null) {
                    ParseObject.pinAllInBackground(ICareApplication.PATIENTS_LABEL, parseObjects, new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                // Get Children from LocalDatastore
                                ParseQuery<ParseObject> queryChildren = ParseQuery.getQuery(ICareApplication.PATIENTS_LABEL);
                                queryChildren.fromLocalDatastore();
                                queryChildren.findInBackground(new FindCallback<ParseObject>() {
                                    @Override
                                    public void done(List<ParseObject> parseObjects, ParseException e) {
                                        if (e == null) {
                                            for (int i = 0; i < parseObjects.size(); i++) {
                                                ParseObject patientObjects = parseObjects.get(i);

                                                Log.d("ROBERT", patientObjects.getObjectId());
                                                Log.d("ROBERT", i + " OF " + parseObjects.size());
                                            }

                                            Intent intent = new Intent(SyncParentDataActivity.this, DashboardParentFragmentActivity.class);
                                            intent.putExtra("loginData", loginData);
                                            mProgressDialog.dismiss();
                                            startActivity(intent);
                                        }
                                    }
                                });

                            }
                        }
                    });
                }
            }
        });

    }
}
