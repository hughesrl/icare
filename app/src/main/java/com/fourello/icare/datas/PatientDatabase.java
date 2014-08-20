package com.fourello.icare.datas;

public class PatientDatabase {
    public String patientObjectId;
    public String firtname;
    public String middlename;
    public String lastname;
    public String mobilenumbers;
    public byte[] patientphoto;

    public PatientDatabase() {}

    public PatientDatabase(String patientObjectId, String firstname, String lastname, String middlename, String mobilenumbers, byte[] patientphoto) {
        super();
        setPatientObjectId(patientObjectId);
        setFirtname(firstname);
        setMiddlename(middlename);
        setLastname(lastname);
        setMobilenumbers(mobilenumbers);
        setPatientphoto(patientphoto);
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
}
