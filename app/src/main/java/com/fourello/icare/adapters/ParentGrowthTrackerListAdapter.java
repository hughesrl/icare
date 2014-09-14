package com.fourello.icare.adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
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

public class ParentGrowthTrackerListAdapter extends ParseQueryAdapter<Visits> {

    public ParentGrowthTrackerListAdapter(Context context,
                           ParseQueryAdapter.QueryFactory<Visits> queryFactory) {
        super(context, queryFactory);
    }

    @Override
    public View getItemView(Visits visits, View convertView, ViewGroup parent) {
        int width = getContext().getResources().getDisplayMetrics().widthPixels;
        Log.d("TABLE ROW", width+"");
        int columnWidth = width/4;

        View row = convertView;
        ViewHolder holder;
        DateFormat df = new SimpleDateFormat("MMM dd");

        if(row == null) {
            LayoutInflater inflater = ((Activity)getContext()).getLayoutInflater();
            row = inflater.inflate(R.layout.list_parent_growth_tracker_item, parent, false);

            holder = new ViewHolder();
            holder.visitDate = (CustomTextView) row.findViewById(R.id.txtVisitDate);
            holder.weight = (CustomTextView) row.findViewById(R.id.txtWeight);
            holder.height = (CustomTextView) row.findViewById(R.id.txtHeight);
            holder.head = (CustomTextView) row.findViewById(R.id.txtHead);
            row.setTag(holder);

        } else {
            holder = (ViewHolder)row.getTag();
        }
        holder.visitDate.setWidth(columnWidth);
        holder.weight.setWidth(columnWidth);
        holder.height.setWidth(columnWidth);
        holder.head.setWidth(columnWidth);

        holder.visitDate.setGravity(Gravity.CENTER);
        holder.weight.setGravity(Gravity.CENTER);
        holder.height.setGravity(Gravity.CENTER);
        holder.head.setGravity(Gravity.CENTER);

        holder.visitDate.setText(df.format(visits.getCreatedAt()));
        holder.weight.setText(visits.getWeight()+" "+getContext().getResources().getString(R.string.lblKg));
        holder.height.setText(visits.getHeight()+" "+getContext().getResources().getString(R.string.lblCm));
        holder.head.setText(visits.getHead()+" "+getContext().getResources().getString(R.string.lblCm));

        return row;
    }
    private static class ViewHolder {
        CustomTextView visitDate;
        CustomTextView weight;
        CustomTextView height;
        CustomTextView head;
    }
}


