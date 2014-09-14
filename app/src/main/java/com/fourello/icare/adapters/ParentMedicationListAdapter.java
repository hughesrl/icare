package com.fourello.icare.adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.fourello.icare.R;
import com.fourello.icare.datas.MedsAndVaccines;
import com.fourello.icare.view.CustomTextView;
import com.parse.ParseQueryAdapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class ParentMedicationListAdapter extends ParseQueryAdapter<MedsAndVaccines> {

    public ParentMedicationListAdapter(Context context,
                                       QueryFactory<MedsAndVaccines> queryFactory) {
        super(context, queryFactory);
    }

    @Override
    public View getItemView(MedsAndVaccines medications, View convertView, ViewGroup parent) {
        int width = getContext().getResources().getDisplayMetrics().widthPixels;
        Log.d("TABLE ROW", width+"");
        int columnWidth = (width-70)/5;

        View row = convertView;
        ViewHolder holder;
        DateFormat df = new SimpleDateFormat("MMM dd");

        if(row == null) {
            LayoutInflater inflater = ((Activity)getContext()).getLayoutInflater();
            row = inflater.inflate(R.layout.list_parent_medication_tracker_item, parent, false);

            holder = new ViewHolder();
            holder.medicine = (CustomTextView) row.findViewById(R.id.txtMedicine);
            holder.duration = (CustomTextView) row.findViewById(R.id.txtDuration);
            holder.frequency = (CustomTextView) row.findViewById(R.id.txtFrequency);
            holder.dose = (CustomTextView) row.findViewById(R.id.txtDose);
            holder.track = (CustomTextView) row.findViewById(R.id.txtTrack);
            holder.actions = (CheckBox) row.findViewById(R.id.med_complete);

            row.setTag(holder);

        } else {
            holder = (ViewHolder)row.getTag();
        }
        holder.medicine.setWidth(columnWidth);
        holder.duration.setWidth(columnWidth);
        holder.frequency.setWidth(columnWidth);
        holder.dose.setWidth(columnWidth);
        holder.track.setWidth(columnWidth);
        holder.actions.setWidth(100);

        holder.medicine.setGravity(Gravity.CENTER);
        holder.duration.setGravity(Gravity.CENTER);
        holder.frequency.setGravity(Gravity.CENTER);
        holder.dose.setGravity(Gravity.CENTER);
        holder.track.setGravity(Gravity.CENTER);
        holder.actions.setGravity(Gravity.CENTER);

        holder.medicine.setText(medications.getName());
        holder.duration.setText(medications.getMedsDuration());
        holder.frequency.setText(medications.getMedsFrequency());
        holder.dose.setText(medications.getMedsDose());
        holder.track.setText("0/"+medications.getMedsGoal());

        return row;
    }
    private static class ViewHolder {
        CustomTextView medicine;
        CustomTextView duration;
        CustomTextView frequency;
        CustomTextView dose;
        CustomTextView track;
        CheckBox actions;
    }
}


