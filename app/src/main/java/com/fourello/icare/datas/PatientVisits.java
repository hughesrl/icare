package com.fourello.icare.datas;

import android.os.Parcel;
import android.os.Parcelable;

public class PatientVisits implements Parcelable {
    public String patientObjectId;
    public String visitDate;

    public static Creator<PatientVisits> getCreator() {
        return CREATOR;
    }

    public PatientVisits() {
        patientObjectId = "";
        visitDate = "";
    }

    public PatientVisits(Parcel in) {
        patientObjectId = in.readString();
        visitDate = in.readString();
    }


    public PatientVisits(String patientObjectId, String visitDate) {
        super();
        setPatientObjectId(patientObjectId);
        setVisitDate(visitDate);
    }

    public String getPatientObjectId() {
        return patientObjectId;
    }
    public void setPatientObjectId(String patientObjectId) {
        this.patientObjectId = patientObjectId;
    }

    public String getVisitDate() {
        return visitDate;
    }
    public void setVisitDate(String visitDate) {
        this.visitDate = visitDate;
    }


    @Override
    public int describeContents() {
        return 0;
    }
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(patientObjectId);
        dest.writeString(visitDate);
    }

    public static final Creator<PatientVisits> CREATOR = new Creator<PatientVisits>()
    {
        public PatientVisits createFromParcel(Parcel in)
        {
            return new PatientVisits(in);
        }
        public PatientVisits[] newArray(int size)
        {
            return new PatientVisits[size];
        }
    };
}
