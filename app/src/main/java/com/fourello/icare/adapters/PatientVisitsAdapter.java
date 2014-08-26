package com.fourello.icare.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fourello.icare.R;
import com.fourello.icare.datas.PatientVisits;

import java.util.List;

public class PatientVisitsAdapter extends BaseAdapter {

    // Declare Variables
    Context mContext;
    LayoutInflater inflater;
    private List<PatientVisits> patientVisitsList = null;
    protected int count;

    public PatientVisitsAdapter(Context context, List<PatientVisits> patientVisitsList) {
        mContext = context;
        this.patientVisitsList = patientVisitsList;
        inflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

    }

    public class ViewHolder {
        TextView date;
    }

    @Override
    public int getCount() {
        return patientVisitsList.size();
    }

    @Override
    public PatientVisits getItem(int position) {
        return patientVisitsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.list_patient_visits_items, null, false);

            holder.date = (TextView) view.findViewById(R.id.txtVisitDate);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.date.setText(patientVisitsList.get(position).getVisitDate());
        return view;
    }
}
