package com.fourello.icare;

import android.app.Activity;
import android.app.ProgressDialog;
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

import com.fourello.icare.R;
import com.fourello.icare.datas.SpinnerItems;
import com.fourello.icare.view.CustomButton;
import com.fourello.icare.view.CustomEditTextView;
import com.fourello.icare.widgets.Utils;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import java.util.ArrayList;

public class AddUserActivity extends Activity {

    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getActionBar().hide();
        setContentView(R.layout.activity_add_user);

        progress = new ProgressDialog(this);
        progress.setCanceledOnTouchOutside(false);

        Spinner customSpinner = (Spinner) findViewById(R.id.spinnerRelationship);
        CustomAdapter adapter = new CustomAdapter(this, android.R.layout.simple_spinner_item, populateReindeer());
        customSpinner.setAdapter(adapter);

//        CustomEditTextView etFirstname = (CustomEditTextView)findViewById(R.id.etFirstname);
//        CustomEditTextView etFirstname = (CustomEditTextView)findViewById(R.id.etLastname);
//        CustomEditTextView etFirstname = (CustomEditTextView)findViewById(R.id.etContactNumber);
//        CustomEditTextView etFirstname = (CustomEditTextView)findViewById(R.id.etEmail);
//        CustomEditTextView etFirstname = (CustomEditTextView)findViewById(R.id.etFirstname);
//        CustomEditTextView etFirstname = (CustomEditTextView)findViewById(R.id.etFirstname);
//        CustomEditTextView etFirstname = (CustomEditTextView)findViewById(R.id.etFirstname);

        CustomButton btnAddUser = (CustomButton)findViewById(R.id.btnAddUser);
        btnAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.validatePassword(AddUserActivity.this);
//                progress.show();
//                ParseObject gameScore = new ParseObject("Users");
//                gameScore.put("score", 1337);
//                gameScore.put("playerName", "Sean Plott");
//                gameScore.put("cheatMode", false);
//                gameScore.saveInBackground(new SaveCallback() {
//                    @Override
//                    public void done(ParseException e) {
//                        progress.dismiss();
//                        if(e != null) {
//
//                        } else {
//
//                        }
//                    }
//                });
            }
        });
    }

    class CustomAdapter extends ArrayAdapter<SpinnerItems> {
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

            LayoutInflater inflater = getLayoutInflater();
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



    private ArrayList<SpinnerItems> populateReindeer(){
        final ArrayList<SpinnerItems> spinnerItems = new ArrayList<SpinnerItems>();
        spinnerItems.add(new SpinnerItems("Relationship to Patient", false));
        spinnerItems.add(new SpinnerItems("Mother", true));
        spinnerItems.add(new SpinnerItems("Father", true));
        return spinnerItems;
    }




}
