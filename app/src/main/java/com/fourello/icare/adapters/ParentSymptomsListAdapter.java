package com.fourello.icare.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fourello.icare.R;
import com.fourello.icare.datas.MedsAndVaccines;
import com.fourello.icare.datas.PatientNotes;
import com.fourello.icare.view.CustomTextView;
import com.parse.ParseQueryAdapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class ParentSymptomsListAdapter extends ParseQueryAdapter<PatientNotes> {

    public ParentSymptomsListAdapter(Context context,
                                     QueryFactory<PatientNotes> queryFactory) {
        super(context, queryFactory);
    }

    @Override
    public View getItemView(PatientNotes patientNotes, View convertView, ViewGroup parent) {
        int width = getContext().getResources().getDisplayMetrics().widthPixels;
        int columnWidth = (width-100)/3;
//        int columnWidth = width/4;

        View row = convertView;
        ViewHolder holder;
        DateFormat df = new SimpleDateFormat("MMMM dd, yyyy");

        if(row == null) {
            LayoutInflater inflater = ((Activity)getContext()).getLayoutInflater();
            row = inflater.inflate(R.layout.list_parent_symptoms_tracker_item, parent, false);

            holder = new ViewHolder();
            holder.date = (CustomTextView) row.findViewById(R.id.txtDate);
            holder.subject = (CustomTextView) row.findViewById(R.id.txtSubject);
            holder.status = (CustomTextView) row.findViewById(R.id.txtStatus);

            row.setTag(holder);

        } else {
            holder = (ViewHolder)row.getTag();
        }
        holder.date.setWidth(columnWidth);
        holder.subject.setWidth(columnWidth);
        holder.status.setWidth(columnWidth);

        holder.date.setGravity(Gravity.CENTER);
        holder.subject.setGravity(Gravity.CENTER);
        holder.status.setGravity(Gravity.CENTER);

        if(patientNotes.getCreatedAt() != null) {
            holder.date.setText(df.format(patientNotes.getCreatedAt()));
        }
        holder.subject.setText(patientNotes.getSubject());
//        holder.status.setText(patientNotes.getS());

        return row;
    }
    private static class ViewHolder {
        CustomTextView date;
        CustomTextView subject;
        CustomTextView status;
    }
}


