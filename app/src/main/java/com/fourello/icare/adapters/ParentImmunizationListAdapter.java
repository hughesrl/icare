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

public class ParentImmunizationListAdapter extends ParseQueryAdapter<MedsAndVaccines> {

    public ParentImmunizationListAdapter(Context context,
                                         QueryFactory<MedsAndVaccines> queryFactory) {
        super(context, queryFactory);
    }

    @Override
    public View getItemView(MedsAndVaccines vaccinations, View convertView, ViewGroup parent) {
        int width = getContext().getResources().getDisplayMetrics().widthPixels;
        int columnWidth = width/3;

        View row = convertView;
        ViewHolder holder;
        DateFormat df = new SimpleDateFormat("MMM dd");

        if(row == null) {
            LayoutInflater inflater = ((Activity)getContext()).getLayoutInflater();
            row = inflater.inflate(R.layout.list_parent_immunization_tracker_item, parent, false);

            holder = new ViewHolder();
            holder.vaccine = (CustomTextView) row.findViewById(R.id.txtVaccine);
            holder.dose = (CustomTextView) row.findViewById(R.id.txtDose);
            holder.dateTaken = (CustomTextView) row.findViewById(R.id.txtDateTaken);

            row.setTag(holder);

        } else {
            holder = (ViewHolder)row.getTag();
        }
        holder.vaccine.setWidth(columnWidth);
        holder.dose.setWidth(columnWidth);
        holder.dateTaken.setWidth(columnWidth);

        holder.vaccine.setGravity(Gravity.CENTER);
        holder.dose.setGravity(Gravity.CENTER);
        holder.dateTaken.setGravity(Gravity.CENTER);

        holder.vaccine.setText(vaccinations.getName());
        holder.dose.setText(vaccinations.getVaccineDose());
        holder.dateTaken.setText(df.format(vaccinations.getVaccineDateTaken()));

        return row;
    }
    private static class ViewHolder {
        CustomTextView vaccine;
        CustomTextView dose;
        CustomTextView dateTaken;
    }
}


