package com.fourello.icare.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.fourello.icare.R;
import com.fourello.icare.datas.PatientNotes;
import com.fourello.icare.view.CustomTextView;
import com.parse.DeleteCallback;
import com.parse.ParseException;
import com.parse.ParseQueryAdapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class ParentSymptomsListAdapter extends ParseQueryAdapter<PatientNotes> {

    public ParentSymptomsListAdapter(Context context,
                                     QueryFactory<PatientNotes> queryFactory) {
        super(context, queryFactory);
    }

    @Override
    public View getItemView(final PatientNotes patientNotes, View convertView, ViewGroup parent) {
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
            holder.deleteSymptom = (ImageButton) row.findViewById(R.id.deleteSymptom);

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

        holder.deleteSymptom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Ask the user if they want to quit
                new AlertDialog.Builder(getContext())
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Delete "+ patientNotes.getSubject())
                        .setMessage("Are you sure you want to delete " + patientNotes.getSubject() + "?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                patientNotes.unpinInBackground(new DeleteCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        notifyDataSetChanged();
                                        patientNotes.deleteEventually(new DeleteCallback() {
                                            @Override
                                            public void done(ParseException e) {
                                            }
                                        });
                                    }
                                });

                            }

                        })
                        .setNegativeButton("CANCEL", null)
                        .show();
            }
        });

        return row;
    }

    private static class ViewHolder {
        CustomTextView date;
        CustomTextView subject;
        CustomTextView status;
        ImageButton deleteSymptom;
    }
}


