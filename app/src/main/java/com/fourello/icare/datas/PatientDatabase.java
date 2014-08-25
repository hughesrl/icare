package com.fourello.icare.datas;

import android.os.Parcel;
import android.os.Parcelable;

public class PatientDatabase implements Parcelable {
    public String patientObjectId;
    public String firtname;
    public String middlename;
    public String lastname;
    public String mobilenumbers;
    public byte[] patientphoto;

    public String parentEmail;
    public String accompaniedBy;
    public String parentRelationship;
    public String allergyRisk;

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

    public String pMomsFname;
    public String pMomsMname;
    public String pMomsLname;
    public String pMomsWorkPlace;
    public String pMomsWorkAs;
    public String pMomsHMO;

    public String pDadsFname;
    public String pDadsMname;
    public String pDadsLname;
    public String pDadsWorkPlace;
    public String pDadsWorkAs;
    public String pDadsHMO;

    public String pAddress1;
    public String pAddress2;

    public static Creator<PatientDatabase> getCreator() {
        return CREATOR;
    }





    public PatientDatabase() {
        patientObjectId = "";
        firtname = "";
        middlename = "";
        lastname = "";
        mobilenumbers = "";
        patientphoto = null;

        parentEmail = "";
        accompaniedBy = "";
        parentRelationship = "";
        allergyRisk = "";

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

        pVaccinationsGiven= "";
        pMomsFname= "";
        pMomsMname= "";
        pMomsLname= "";
        pMomsWorkPlace= "";
        pMomsWorkAs= "";

        pMomsHMO= "";
        pDadsFname= "";
        pDadsMname= "";
        pDadsLname= "";
        pDadsWorkPlace= "";
        pDadsWorkAs= "";
        pDadsHMO= "";
        pAddress1= "";
        pAddress2= "";
    }

    public PatientDatabase(Parcel in) {
        patientObjectId = in.readString();
        firtname = in.readString();
        middlename = in.readString();
        lastname = in.readString();
        mobilenumbers = in.readString();

        patientphoto = new byte[in.readInt()];
        in.readByteArray(patientphoto);

        parentEmail = in.readString();
        accompaniedBy = in.readString();
        parentRelationship = in.readString();
        allergyRisk = in.readString();

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

        pVaccinationsGiven= in.readString();
        pMomsFname= in.readString();
        pMomsMname= in.readString();
        pMomsLname= in.readString();
        pMomsWorkPlace= in.readString();
        pMomsWorkAs= in.readString();

        pMomsHMO= in.readString();
        pDadsFname= in.readString();
        pDadsMname= in.readString();
        pDadsLname= in.readString();
        pDadsWorkPlace= in.readString();
        pDadsWorkAs= in.readString();
        pDadsHMO= in.readString();
        pAddress1= in.readString();
        pAddress2= in.readString();
    }


    public PatientDatabase(String patientObjectId, String firstname, String lastname, String middlename, String mobilenumbers, byte[] patientphoto,
                           String bDate,String bPlace,String drName,String deliveryType,String pWeight,String pHeight,
                           String pHead,String pChest,String pAbdomen,String pCircumcisedOn,String pEarPiercedOn,String pDistinguishingMarks,
                           String pNewbornScreening,String pVaccinationsGiven,String pMomsFname,String pMomsMname,
                           String pMomsLname,String pMomsWorkPlace,String pMomsWorkAs,String pMomsHMO,String pDadsFname,
                           String pDadsMname,String pDadsLname,String pDadsWorkPlace,String pDadsWorkAs,String pDadsHMO,
                           String pAddress1,String pAddress2) {
        super();
        setPatientObjectId(patientObjectId);
        setFirtname(firstname);
        setMiddlename(middlename);
        setLastname(lastname);
        setMobilenumbers(mobilenumbers);
        setPatientphoto(patientphoto);
        setbDate(bDate);
        setbPlace(bPlace);
        setDrName(drName);
        setDeliveryType(deliveryType);
        setpWeight(pWeight);
        setpHeight(pHeight);
        setpHead(pHead);
        setpChest(pChest);
        setpAbdomen(pAbdomen);
        setpCircumcisedOn(pCircumcisedOn);
        setpCircumcisedOn(pCircumcisedOn);
        setpEarPiercedOn(pEarPiercedOn);
        setpDistinguishingMarks(pDistinguishingMarks);
        setpNewbornScreening(pNewbornScreening);
        setpVaccinationsGiven(pVaccinationsGiven);
        setpMomsFname(pMomsFname);
        setpMomsMname(pMomsMname);
        setpMomsLname(pMomsLname);
        setpMomsWorkPlace(pMomsWorkPlace);
        setpMomsWorkAs(pMomsWorkAs);
        setpMomsHMO(pMomsHMO);
        setpDadsFname(pDadsFname);
        setpDadsMname(pDadsMname);
        setpDadsLname(pDadsLname);
        setpDadsWorkPlace(pDadsWorkPlace);
        setpDadsWorkAs(pDadsWorkAs);
        setpDadsHMO(pDadsHMO);
        setpAddress1(pAddress1);
        setpAddress2(pAddress2);
    }

    public String getPatientObjectId() {
        return patientObjectId;
    }
    public void setPatientObjectId(String patientObjectId) {
        this.patientObjectId = patientObjectId;
    }

    public String getFirtname() {
        return firtname;
    }
    public void setFirtname(String firtname) {
        this.firtname = firtname;
    }

    public String getMiddlename() {
        return middlename;
    }
    public void setMiddlename(String middlename) {
        this.middlename = middlename;
    }

    public String getLastname() {
        return lastname;
    }
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getMobilenumbers() {
        return mobilenumbers;
    }
    public void setMobilenumbers(String mobilenumbers) {
        this.mobilenumbers = mobilenumbers;
    }

    public byte[] getPatientphoto() {
        return patientphoto;
    }
    public void setPatientphoto(byte[] patientphoto) {
        this.patientphoto = patientphoto;
    }

    public String getFullName() {
        return firtname + " " + middlename + " " + lastname;
    }

// ------------ FROM USERS
    public String getParentEmail() {
        return parentEmail;
    }

    public void setParentEmail(String parentEmail) {
        this.parentEmail = parentEmail;
    }

    public String getAccompaniedBy() {
        return accompaniedBy;
    }

    public void setAccompaniedBy(String accompaniedBy) {
        this.accompaniedBy = accompaniedBy;
    }

    public String getParentRelationship() {
        return parentRelationship;
    }

    public void setParentRelationship(String parentRelationship) {
        this.parentRelationship = parentRelationship;
    }

    public String getAllergyRisk() {
        return allergyRisk;
    }

    public void setAllergyRisk(String allergyRisk) {
        this.allergyRisk = allergyRisk;
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

    public String getpMomsFname() {
        return pMomsFname;
    }

    public void setpMomsFname(String pMomsFname) {
        this.pMomsFname = pMomsFname;
    }

    public String getpMomsMname() {
        return pMomsMname;
    }

    public void setpMomsMname(String pMomsMname) {
        this.pMomsMname = pMomsMname;
    }

    public String getpMomsLname() {
        return pMomsLname;
    }

    public void setpMomsLname(String pMomsLname) {
        this.pMomsLname = pMomsLname;
    }

    public String getpMomsWorkPlace() {
        return pMomsWorkPlace;
    }

    public void setpMomsWorkPlace(String pMomsWorkPlace) {
        this.pMomsWorkPlace = pMomsWorkPlace;
    }

    public String getpMomsWorkAs() {
        return pMomsWorkAs;
    }

    public void setpMomsWorkAs(String pMomsWorkAs) {
        this.pMomsWorkAs = pMomsWorkAs;
    }

    public String getpMomsHMO() {
        return pMomsHMO;
    }

    public void setpMomsHMO(String pMomsHMO) {
        this.pMomsHMO = pMomsHMO;
    }

    public String getpDadsFname() {
        return pDadsFname;
    }

    public void setpDadsFname(String pDadsFname) {
        this.pDadsFname = pDadsFname;
    }

    public String getpDadsMname() {
        return pDadsMname;
    }

    public void setpDadsMname(String pDadsMname) {
        this.pDadsMname = pDadsMname;
    }

    public String getpDadsLname() {
        return pDadsLname;
    }

    public void setpDadsLname(String pDadsLname) {
        this.pDadsLname = pDadsLname;
    }

    public String getpDadsWorkPlace() {
        return pDadsWorkPlace;
    }

    public void setpDadsWorkPlace(String pDadsWorkPlace) {
        this.pDadsWorkPlace = pDadsWorkPlace;
    }

    public String getpDadsWorkAs() {
        return pDadsWorkAs;
    }

    public void setpDadsWorkAs(String pDadsWorkAs) {
        this.pDadsWorkAs = pDadsWorkAs;
    }

    public String getpDadsHMO() {
        return pDadsHMO;
    }

    public void setpDadsHMO(String pDadsHMO) {
        this.pDadsHMO = pDadsHMO;
    }

    public String getpAddress1() {
        return pAddress1;
    }

    public void setpAddress1(String pAddress1) {
        this.pAddress1 = pAddress1;
    }

    public String getpAddress2() {
        return pAddress2;
    }

    public void setpAddress2(String pAddress2) {
        this.pAddress2 = pAddress2;
    }

    @Override
    public int describeContents() {
        return 0;
    }
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(patientObjectId);
        dest.writeString(firtname);
        dest.writeString(middlename);
        dest.writeString(lastname);
        dest.writeString(mobilenumbers);
        dest.writeByteArray(patientphoto);

        dest.writeString(parentEmail);
        dest.writeString(accompaniedBy);
        dest.writeString(parentRelationship);
        dest.writeString(allergyRisk);

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

        dest.writeString(pVaccinationsGiven);
        dest.writeString(pMomsFname);
        dest.writeString(pMomsMname);
        dest.writeString(pMomsLname);
        dest.writeString(pMomsWorkPlace);
        dest.writeString(pMomsWorkAs);

        dest.writeString(pMomsHMO);
        dest.writeString(pDadsFname);
        dest.writeString(pDadsMname);
        dest.writeString(pDadsLname);
        dest.writeString(pDadsWorkPlace);
        dest.writeString(pDadsWorkAs);
        dest.writeString(pDadsHMO);
        dest.writeString(pAddress1);
        dest.writeString(pAddress2);

    }

    public static final Parcelable.Creator<PatientDatabase> CREATOR = new Parcelable.Creator<PatientDatabase>()
    {
        public PatientDatabase createFromParcel(Parcel in)
        {
            return new PatientDatabase(in);
        }
        public PatientDatabase[] newArray(int size)
        {
            return new PatientDatabase[size];
        }
    };
}
