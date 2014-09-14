package com.fourello.icare.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fourello.icare.R;
import com.fourello.icare.datas.Visits;
import com.fourello.icare.view.CustomTextView;
import com.parse.ParseQueryAdapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class ParentClinicVisitsListAdapter extends ParseQueryAdapter<Visits> {

    public ParentClinicVisitsListAdapter(Context context,
                                         QueryFactory<Visits> queryFactory) {
        super(context, queryFactory);
    }

    @Override
    public View getItemView(Visits visits, View convertView, ViewGroup parent) {
        int width = getContext().getResources().getDisplayMetrics().widthPixels;
        int columnWidth = width/3;

        View row = convertView;
        ViewHolder holder;
        DateFormat df = new SimpleDateFormat("MMMM dd");

        if(row == null) {
            LayoutInflater inflater = ((Activity)getContext()).getLayoutInflater();
            row = inflater.inflate(R.layout.list_parent_clinic_visits_item, parent, false);

            holder = new ViewHolder();
            holder.visitDate = (CustomTextView) row.findViewById(R.id.txtVisitDate);
            holder.instructions = (CustomTextView) row.findViewById(R.id.txtInstructions);
            holder.nextvisit = (CustomTextView) row.findViewById(R.id.txtNextVisit);
            row.setTag(holder);

        } else {
            holder = (ViewHolder)row.getTag();
        }
        holder.visitDate.setWidth(columnWidth);
        holder.instructions.setWidth(columnWidth);
        holder.nextvisit.setWidth(columnWidth);

        holder.visitDate.setGravity(Gravity.CENTER);
        holder.instructions.setGravity(Gravity.CENTER);
        holder.nextvisit.setGravity(Gravity.CENTER);

        holder.visitDate.setText(df.format(visits.getCreatedAt()));

        holder.instructions.setText(visits.getInstructions());
        if(visits.getNextVisit() != null) {
            holder.nextvisit.setText(df.format(visits.getNextVisit()));
        }

        return row;
    }
    private static class ViewHolder {
        CustomTextView visitDate;
        CustomTextView instructions;
        CustomTextView nextvisit;
    }
}


