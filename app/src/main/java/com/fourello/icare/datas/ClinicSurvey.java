package com.fourello.icare.datas;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

import java.util.Date;

@ParseClassName("ClinicSurvey")
public class ClinicSurvey extends ParseObject {

    public ClinicSurvey() {
        // A default constructor is required.
    }

    public ParseFile getPatientphoto() {
        return getParseFile("photoFile");
    }
    public void setPatientphoto(ParseFile photoFile) {
        put("photoFile", photoFile);
    }

    public String getAccompaniedBy() {
        return getString("accompaniedby");
    }
    public void setAccompaniedBy(String accompaniedBy) {
        put("accompaniedby", accompaniedBy);
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

    public String getDoctorId() {
        return getString("doctorid");
    }
    public void setDoctorId(String doctorid) {
        put("doctorid", doctorid);
    }

    public String getEmail() {
        return getString("email");
    }
    public void setEmail(String email) {
        put("email", email);
    }

    public String getPatientObjectId() {
        return getString("patientid");
    }
    public void setPatientObjectId(String patientid) {
        put("patientid", patientid);
    }

    public String getPatientFullname() {
        return getString("patientname");
    }
    public void setPatientFullname(String patientFullname) {
        put("patientname", patientFullname);
    }

    public String getPersonalNotes() {
        return getString("personal_notes");
    }
    public void setPersonalNotes(String personal_notes) {
        put("personal_notes", personal_notes);
    }

    public String getQuestion() {
        return getString("question");
    }
    public void setQuestion(String question) {
        put("question", question);
    }

    public String getPurposeOfVisit() {
        return getString("purpose_of_visit");
    }
    public void setPurposeOfVisit(String purpose_of_visit) {
        put("purpose_of_visit", purpose_of_visit);
    }

    public String getRelationshipToPatient() {
        return getString("relationship_to_patient");
    }
    public void setRelationshipToPatient(String relationship_to_patient) {
        put("relationship_to_patient", relationship_to_patient);
    }

    public String getTypeOfDeliveryType() {
        return getString("typeofdelivery");
    }
    public void setTypeOfDeliveryType(String typeofdelivery) {
        put("typeofdelivery", typeofdelivery);
    }

    public String getAllergyRisk() {
        return getString("allergyrisk");
    }
    public void setAllergyRisk(String allergyrisk) {
        put("allergyrisk", allergyrisk);
    }

    public Date getDateOfBirth() {
        return getDate("dateofbirth");
    }
    public void setDateOfBirth(Date dateOfBirth) {
        put("dateofbirth", dateOfBirth);
    }


    public String getStatusTag() {
        return getString("status_tag");
    }
    public void setStatusTag(String status_tag) {
        put("status_tag", status_tag);
    }

    /*public String getVisitId() {
        return getString("visitid");
    }
    public void setVisitId(String visitid) {
        put("visitid", visitid);
    }*/

}