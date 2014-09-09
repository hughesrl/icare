package com.fourello.icare.datas;

import android.os.Parcel;
import android.os.Parcelable;

import com.parse.ParseClassName;

@ParseClassName("Patients")
public class MyChildren implements Parcelable {
    public String patientObjectId;
    public String patientName;
    public String patientFirstName;
    public String patientMiddleName;
    public String patientLastName;
    public byte[] patientphoto;

    public String bDate;
    public String bPlace;
    public String drName;
    public String deliveryType;
    public String pWeight;
    public String pHeight;
    public String pHead;
    public String pChest;
    public String pAbdomen;
    public String pCircumcisedOn;
    public String pEarPiercedOn;
    public String pDistinguishingMarks;
    public String pNewbornScreening;
    public String pVaccinationsGiven;

    public static Creator<MyChildren> getCreator() {
        return CREATOR;
    }

    public MyChildren() {
        patientObjectId = "";
        patientName = "";
        patientFirstName = "";
        patientMiddleName = "";
        patientLastName = "";
        patientphoto = null;

        bDate = "";
        bPlace = "";
        drName = "";
        deliveryType= "";
        pWeight= "";
        pHeight= "";
        pHead= "";
        pChest= "";
        pAbdomen= "";
        pCircumcisedOn= "";
        pEarPiercedOn= "";
        pDistinguishingMarks= "";
        pNewbornScreening= "";
    }
    public MyChildren(Parcel in) {
        patientObjectId = in.readString();
        patientName = in.readString();
        patientFirstName = in.readString();
        patientMiddleName = in.readString();
        patientLastName = in.readString();
        patientphoto = new byte[in.readInt()];
        in.readByteArray(patientphoto);

        bDate = in.readString();
        bPlace = in.readString();
        drName = in.readString();
        deliveryType= in.readString();
        pWeight= in.readString();
        pHeight= in.readString();
        pHead= in.readString();
        pChest= in.readString();
        pAbdomen= in.readString();
        pCircumcisedOn= in.readString();
        pEarPiercedOn= in.readString();
        pDistinguishingMarks= in.readString();
        pNewbornScreening= in.readString();


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

    public String getPatientFirstName() {
        return patientFirstName;
    }
    public void setPatientFirstName(String patientFirstName) {
        this.patientFirstName = patientFirstName;
    }

    public String getPatientMiddleName() {
        return patientMiddleName;
    }
    public void setPatientMiddleName(String patientMiddleName) {
        this.patientMiddleName = patientMiddleName;
    }

    public String getPatientLastName() {
        return patientLastName;
    }
    public void setPatientLastName(String patientLastName) {
        this.patientLastName = patientLastName;
    }

    public byte[] getPatientphoto() {
        return patientphoto;
    }
    public void setPatientphoto(byte[] patientphoto) {
        this.patientphoto = patientphoto;
    }

    public String getbDate() {
        return bDate;
    }
    public void setbDate(String bDate) {
        this.bDate = bDate;
    }

    public String getbPlace() {
        return bPlace;
    }
    public void setbPlace(String bPlace) {
        this.bPlace = bPlace;
    }

    public String getDrName() {
        return drName;
    }
    public void setDrName(String drName) {
        this.drName = drName;
    }

    public String getDeliveryType() {
        return deliveryType;
    }
    public void setDeliveryType(String deliveryType) {
        this.deliveryType = deliveryType;
    }

    public String getpWeight() {
        return pWeight;
    }
    public void setpWeight(String pWeight) {
        this.pWeight = pWeight;
    }

    public String getpHeight() {
        return pHeight;
    }
    public void setpHeight(String pHeight) {
        this.pHeight = pHeight;
    }

    public String getpHead() {
        return pHead;
    }
    public void setpHead(String pHead) {
        this.pHead = pHead;
    }

    public String getpChest() {
        return pChest;
    }
    public void setpChest(String pChest) {
        this.pChest = pChest;
    }

    public String getpAbdomen() {
        return pAbdomen;
    }
    public void setpAbdomen(String pAbdomen) {
        this.pAbdomen = pAbdomen;
    }

    public String getpCircumcisedOn() {
        return pCircumcisedOn;
    }
    public void setpCircumcisedOn(String pCircumcisedOn) {
        this.pCircumcisedOn = pCircumcisedOn;
    }

    public String getpEarPiercedOn() {
        return pEarPiercedOn;
    }
    public void setpEarPiercedOn(String pEarPiercedOn) {
        this.pEarPiercedOn = pEarPiercedOn;
    }

    public String getpDistinguishingMarks() {
        return pDistinguishingMarks;
    }
    public void setpDistinguishingMarks(String pDistinguishingMarks) {
        this.pDistinguishingMarks = pDistinguishingMarks;
    }

    public String getpNewbornScreening() {
        return pNewbornScreening;
    }
    public void setpNewbornScreening(String pNewbornScreening) {
        this.pNewbornScreening = pNewbornScreening;
    }

    public String getpVaccinationsGiven() {
        return pVaccinationsGiven;
    }

    public void setpVaccinationsGiven(String pVaccinationsGiven) {
        this.pVaccinationsGiven = pVaccinationsGiven;
    }

    @Override
    public int describeContents() {
        return 0;
    }
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(patientObjectId);
        dest.writeString(patientName);
        dest.writeString(patientFirstName);
        dest.writeString(patientMiddleName);
        dest.writeString(patientLastName);
        dest.writeByteArray(patientphoto);

        dest.writeString(bDate);
        dest.writeString(bPlace);
        dest.writeString(drName);
        dest.writeString(deliveryType);
        dest.writeString(pWeight);
        dest.writeString(pHeight);
        dest.writeString(pHead);
        dest.writeString(pChest);
        dest.writeString(pAbdomen);
        dest.writeString(pCircumcisedOn);
        dest.writeString(pEarPiercedOn);
        dest.writeString(pDistinguishingMarks);
        dest.writeString(pNewbornScreening);

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