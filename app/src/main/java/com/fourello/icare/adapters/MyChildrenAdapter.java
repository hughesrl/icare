package com.fourello.icare.adapters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fourello.icare.R;
import com.fourello.icare.datas.MyChildren;
import com.fourello.icare.widgets.Utils;

import java.util.ArrayList;

public class MyChildrenAdapter extends ArrayAdapter<MyChildren> {
    private Activity context;
    ArrayList<MyChildren> spinnerItems;

    public MyChildrenAdapter(Activity context, int resource, ArrayList<MyChildren> spinnerItems) {
        super(context, resource, spinnerItems);
        this.context = context;
        this.spinnerItems = spinnerItems;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyChildren current = spinnerItems.get(position);

        LayoutInflater inflater = context.getLayoutInflater();
        View row = inflater.inflate(R.layout.spinner_row_my_children_photo, parent, false);
        ImageView patientPhoto = (ImageView) row.findViewById(R.id.patientPhoto);

        byte[] childPhotoData = current.getPatientphoto();

        if(childPhotoData!=null) {
            Bitmap bMap = BitmapFactory.decodeByteArray(childPhotoData, 0, childPhotoData.length);
            patientPhoto.setBackground(Utils.resizedBitmapDisplayPatientQueue(context, bMap));
        }

        return row;

    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {   // This view starts when we click the spinner.
        View row = convertView;
        if(row == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            row = inflater.inflate(R.layout.spinner_row_my_children, parent, false);
        }

        MyChildren item = spinnerItems.get(position);

        if(item != null) {   // Parse the data from each object and set it.
            TextView myChildName = (TextView) row.findViewById(R.id.spinnerTxtTitle);
            ImageView patientPhoto = (ImageView) row.findViewById(R.id.patientPhoto);

            if(myChildName != null)
                myChildName.setText(item.getPatientName());

            byte[] childPhotoData = item.getPatientphoto();

            if(childPhotoData!=null) {
                Log.d("ROBERT BTE", childPhotoData.length + "");
                Bitmap bMap = BitmapFactory.decodeByteArray(childPhotoData, 0, childPhotoData.length);
                patientPhoto.setBackground(Utils.resizedBitmapDisplayPatientQueue(context, bMap));
            }

        }

        return row;
    }
    public int getPosition(String text) {
        for(int s=0;s<=(spinnerItems.size()-1);s++) {
            if(spinnerItems.get(s).getPatientName().equalsIgnoreCase(text.trim())) {
                return s;
            }
        }
        return 0;
    }
}
