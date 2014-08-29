package com.fourello.icare.adapters;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.fourello.icare.R;
import com.fourello.icare.datas.PatientDatabase;
import com.fourello.icare.datas.PatientQueue;
import com.fourello.icare.view.CustomTextView;
import com.fourello.icare.view.RoundedAvatarDrawable;
import com.fourello.icare.view.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

public class PatientQueueAdapter extends BaseAdapter {

    ProgressDialog mProgressDialog;
    // Declare Variables
    Context mContext;
    LayoutInflater inflater;
    private List<PatientQueue> patientQueueList = null;
    private ArrayList<PatientQueue> arraylist;
    protected int count;

    public PatientQueueAdapter(Context context, List<PatientQueue> patientQueueList) {
        mContext = context;
        this.patientQueueList = patientQueueList;
        inflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

    }


    public class ViewHolder {
        CustomTextView name;
        ImageView photoFile;
    }

    @Override
    public int getCount() {
        return patientQueueList.size();
    }

    @Override
    public PatientQueue getItem(int position) {
        return patientQueueList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.list_patient_queue_items, null, false);

            holder.name = (CustomTextView) view.findViewById(R.id.txtName);
            holder.photoFile = (ImageView) view.findViewById(R.id.patientPhoto);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        // Set the results into TextView
        String patientName = patientQueueList.get(position).getPatientName();
        holder.name.setText(patientName);

        byte[] myPicture = patientQueueList.get(position).getPatientphoto();
        if(myPicture!=null) {
            Bitmap bMap = BitmapFactory.decodeByteArray(myPicture, 0, myPicture.length);
            int width=75;
            int height=80;
            Bitmap resizedbitmap=Bitmap.createScaledBitmap(bMap, width, height, true);
            RoundedAvatarDrawable r = new RoundedAvatarDrawable(resizedbitmap);

            holder.photoFile.setBackground(r);

//            Bitmap profileInCircle = RoundedImageView.getRoundedCornerBitmap(bMap);
//            holder.photoFile.setImageBitmap(profileInCircle);
        }

        return view;
    }


}
