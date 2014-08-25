package com.fourello.icare.datas;

import android.os.Parcel;
import android.os.Parcelable;

public class PatientQueue implements Parcelable {
    public String patientObjectId;
    public String patientName;
    public byte[] patientphoto;

    public static Creator<PatientQueue> getCreator() {
        return CREATOR;
    }





    public PatientQueue() {
        patientObjectId = "";
        patientName = "";
        patientphoto = null;


    }

    public PatientQueue(Parcel in) {
        patientObjectId = in.readString();
        patientName = in.readString();
        patientphoto = new byte[in.readInt()];
        in.readByteArray(patientphoto);


    }


    public PatientQueue(String patientObjectId, String patientname, byte[] patientphoto) {
        super();
        setPatientObjectId(patientObjectId);
        setPatientName(patientname);
        setPatientphoto(patientphoto);

    }

    public String getPatientObjectId() {
        return patientObjectId;
    }
    public void setPatientObjectId(String patientObjectId) {
        this.patientObjectId = patientObjectId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }



    public byte[] getPatientphoto() {
        return patientphoto;
    }
    public void setPatientphoto(byte[] patientphoto) {
        this.patientphoto = patientphoto;
    }



    @Override
    public int describeContents() {
        return 0;
    }
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(patientObjectId);
        dest.writeString(patientName);
        dest.writeByteArray(patientphoto);


    }

    public static final Creator<PatientQueue> CREATOR = new Creator<PatientQueue>()
    {
        public PatientQueue createFromParcel(Parcel in)
        {
            return new PatientQueue(in);
        }
        public PatientQueue[] newArray(int size)
        {
            return new PatientQueue[size];
        }
    };
}
