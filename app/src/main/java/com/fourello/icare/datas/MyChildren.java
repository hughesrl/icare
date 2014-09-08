package com.fourello.icare.datas;

import android.os.Parcel;
import android.os.Parcelable;

import com.parse.ParseClassName;

@ParseClassName("Patients")
public class MyChildren implements Parcelable {
    public String patientObjectId;
    public String patientName;
    public byte[] patientphoto;

    public static Creator<MyChildren> getCreator() {
        return CREATOR;
    }

    public MyChildren() {
        patientObjectId = "";
        patientName = "";
        patientphoto = null;
    }
    public MyChildren(Parcel in) {
        patientObjectId = in.readString();
        patientName = in.readString();
        patientphoto = new byte[in.readInt()];
        in.readByteArray(patientphoto);


    }

    public MyChildren(String patientObjectId, String patientname, byte[] patientphoto) {
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

    public static final Creator<MyChildren> CREATOR = new Creator<MyChildren>()
    {
        public MyChildren createFromParcel(Parcel in)
        {
            return new MyChildren(in);
        }
        public MyChildren[] newArray(int size)
        {
            return new MyChildren[size];
        }
    };
}