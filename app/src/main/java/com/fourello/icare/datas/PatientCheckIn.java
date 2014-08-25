package com.fourello.icare.datas;

import android.os.Parcel;
import android.os.Parcelable;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

@ParseClassName("Visits")
public class PatientCheckIn extends ParseObject {

    public PatientCheckIn() {
        // A default constructor is required.
    }

    public int getDoctorId() {
        return getInt("doctorid");
    }
    public void setDoctorId(int doctorid) {
        put("doctorid", doctorid);
    }

    public String getPatientObjectId() {
        return getString("patientid");
    }
    public void setPatientObjectId(String patientid) {
        put("patientid", patientid);
    }

    public String getEmail() {
        return getString("email");
    }
    public void setEmail(String email) {
        put("email", email);
    }

    public String getPatientFullname() {
        return getString("patientname");
    }
    public void setPatientFullname(String patientFullname) {
        put("patientname", patientFullname);
    }

    public String getAccompaniedBy() {
        return getString("accompaniedby");
    }
    public void setAccompaniedBy(String accompaniedBy) {
        put("accompaniedby", accompaniedBy);
    }

    public String getRelationshipToPatient() {
        return getString("relationship_to_patient");
    }
    public void setRelationshipToPatient(String relationship_to_patient) {
        put("relationship_to_patient", relationship_to_patient);
    }

    public String getPurposeOfVisit() {
        return getString("purpose_of_visit");
    }
    public void setPurposeOfVisit(String purpose_of_visit) {
        put("purpose_of_visit", purpose_of_visit);
    }

    public String getAllergyRisk() {
        return getString("allergyrisk");
    }
    public void setAllergyRisk(String allergyrisk) {
        put("allergyrisk", allergyrisk);
    }

    public String getMomsNotes() {
        return getString("personal_notes");
    }
    public void setMomsNotes(String personal_notes) {
        put("personal_notes", personal_notes);
    }

    public String getWeight() {
        return getString("weight");
    }
    public void setWeight(String weight) {
        put("weight", weight);
    }

    public String getHeight() {
        return getString("height");
    }
    public void setHeight(String height) {
        put("height", height);
    }

    public String getHead() {
        return getString("head");
    }
    public void setHead(String head) {
        put("head", head);
    }

    public String getChest() {
        return getString("chest");
    }
    public void setChest(String chest) {
        put("chest", chest);
    }

    public String getTemperature() {
        return getString("temperature");
    }
    public void setTemperature(String temperature) {
        put("temperature", temperature);
    }

    public ParseFile getPatientphoto() {
        return getParseFile("photoFile");
    }
    public void setPatientphoto(ParseFile photoFile) {
        put("photoFile", photoFile);
    }

}