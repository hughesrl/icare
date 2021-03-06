package com.fourello.icare.adapters;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.fourello.icare.DashboardDoctorFragmentActivity;
import com.fourello.icare.R;
import com.fourello.icare.datas.PatientDatabase;
import com.fourello.icare.fragments.CheckinPatientDialogFragment;
import com.fourello.icare.fragments.PatientDatabaseFragment;
import com.fourello.icare.widgets.ParseProxyObject;
import com.fourello.icare.widgets.PasswordDialogFragment;
import com.fourello.icare.widgets.Utils;

import java.util.ArrayList;
import java.util.List;

public class PatientDatabaseAdapter extends BaseAdapter {

    ProgressDialog mProgressDialog;
    // Declare Variables
    Context mContext;
    LayoutInflater inflater;
    private List<PatientDatabase> patientDatabaselist = null;
    private ArrayList<PatientDatabase> arraylist;
    protected int count;

    public PatientDatabaseAdapter(Context context, List<PatientDatabase> patientDatabaselist) {
        mContext = context;
        this.patientDatabaselist = patientDatabaselist;
        inflater = (LayoutInflater) mContext
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

    }


    public class ViewHolder {
        TextView name;
        TextView mobilenumber;

        ImageButton patientInfo;
        ImageButton patientCheckin;
        ImageButton patientCalendar;

    }

    @Override
    public int getCount() {
        return patientDatabaselist.size();
    }

    @Override
    public PatientDatabase getItem(int position) {
        return patientDatabaselist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.list_patient_database_items, null, false);
            // Locate the TextView in listview_item.xml
            holder.name = (TextView) view.findViewById(R.id.txtName);
            holder.mobilenumber = (TextView) view.findViewById(R.id.txtMobile);

//            holder.patientCheckin = (ImageButton) view.findViewById(R.id.btnPatientCheckin);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        // Set the results into TextView
        String patientName = patientDatabaselist.get(position).getLastname()+", "+patientDatabaselist.get(position).getFirtname()+" "+patientDatabaselist.get(position).getMiddlename();
        holder.name.setText(patientName);
        holder.mobilenumber.setText(patientDatabaselist.get(position).getMobilenumbers());

        return view;
    }

//    public void showPatientDialog(PatientDatabase patientDatabase, int position) {
//        FragmentActivity activity = (FragmentActivity)(mContext);
//        FragmentManager fm = activity.getSupportFragmentManager();
//
//        DialogFragment patientDialog = new CheckinPatientDialogFragment();
//        Bundle bundle = new Bundle();
//
//        bundle.putString(CheckinPatientDialogFragment.ARG_PATIENT_RECORD_OBJECT_ID, patientDatabase.getPatientObjectId());
//        bundle.putString(CheckinPatientDialogFragment.ARG_PATIENT_FULL_NAME, patientDatabase.getFullName());
//        bundle.putByteArray(CheckinPatientDialogFragment.ARG_PATIENT_PHOTO, patientDatabase.getPatientphoto());
//        bundle.putInt(CheckinPatientDialogFragment.ARG_PATIENT_ARRAY_LIST_POSITION, position);
//        patientDialog.setArguments(bundle);
//
////        Fragment test = new PatientDatabaseFragment();
//        patientDialog.setTargetFragment(activity.getSupportFragmentManager().getFragment(bundle,"d"), 12);
//        patientDialog.show(fm, "NoticeDialogFragment");
//    }


}
