package com.fourello.icare.datas;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.json.JSONArray;

import java.util.Date;

@ParseClassName("Visits")
public class Visits extends ParseObject {

    public Visits() {
        // A default constructor is required.
    }

    public String getAccompaniedBy() {
        return getString("accompaniedby");
    }
    public void setAccompaniedBy(String accompaniedby) {
        put("accompaniedby", accompaniedby);
    }

    public String getAge() {
        return getString("age");
    }
    public void setAge(String age) {
        put("age", age);
    }

    public String getAllergyRisk() {
        return getString("allergyrisk");
    }
    public void setAllergyRisk(String allergyrisk) {
        put("allergyrisk", allergyrisk);
    }

    public String getChest() {
        return getString("chest");
    }
    public void setChest(String chest) {
        put("chest", chest);
    }

    public String getDiagnosis() {
        return getString("diagnosis");
    }
    public void setDiagnosis(String diagnosis) {
        put("diagnosis", diagnosis);
    }

    public int getDoctorId() {
        return getInt("doctorid");
    }
    public void setDoctorId(String doctorId) {
        put("doctorId", doctorId);
    }

    public String getEmail() {
        return getString("email");
    }
    public void setEmail(String email) {
        put("email", email);
    }

    public String getHead() {
        return getString("head");
    }
    public void setHead(String head) {
        put("head", head);
    }

    public String getHeight() {
        return getString("height");
    }
    public void setHeight(String height) {
        put("height", height);
    }

    public String getInstructions() {
        return getString("instructions");
    }
    public void setInstructions(String instructions) {
        put("instructions", instructions);
    }

    public JSONArray getMedications() {
        return getJSONArray("medications");
    }
    public void setMedications(JSONArray medications) {
        put("medications", medications);
    }

    public Date getNextVisit() {
        return getDate("nextvisit");
    }
    public void setNextVisit(Date nextVisit) {
        put("nextvisit", nextVisit);
    }

    public String getPatientId() {
        return getString("patientid");
    }
    public void setPatientId(String patientId) {
        put("patientid", patientId);
    }

    public String getPatientName() {
        return getString("patientname");
    }
    public void setPatientName(String patientName) {
        put("patientname", patientName);
    }

    public String getPersonalNotes() {
        return getString("personal_notes");
    }
    public void setPersonalNotes(String personalNotes) {
        put("personal_notes", personalNotes);
    }

    public ParseFile getPhotoFile() {
        return getParseFile("photoFile");
    }
    public void setPhotoFile(ParseFile photoFile) {
        put("photoFile", photoFile);
    }

    public String getPurposeOfVisit() {
        return getString("purpose_of_visit");
    }
    public void setPurposeOfVisit(String purposeOfVisit) {
        put("purpose_of_visit", purposeOfVisit);
    }

    public String getQuestionForDoctor() {
        return getString("question_for_doctor");
    }
    public void setQuestionForDoctor(String questionForDoctor) {
        put("question_for_doctor", questionForDoctor);
    }

    public String getRelationshipToPatient() {
        return getString("relationship_to_patient");
    }
    public void setRelationshipToPatient(String relationshipToPatient) {
        put("relationship_to_patient", relationshipToPatient);
    }

    public String getTemperature() {
        return getString("temperature");
    }
    public void setTemperature(String temperature) {
        put("temperature", temperature);
    }

    public String getTypeOfDelivery() {
        return getString("typeofdelivery");
    }
    public void setTypeOfDelivery(String typeOfDelivery) {
        put("typeofdelivery", typeOfDelivery);
    }

    public JSONArray getVaccinations() {
        return getJSONArray("vaccinations");
    }
    public void setVaccinations(JSONArray vaccinations) {
        put("vaccinations", vaccinations);
    }

    public String getWeight() {
        return getString("weight");
    }
    public void setWeight(String weight) {
        put("weight", weight);
    }

    public static ParseQuery<Visits> getQuery() {
        return ParseQuery.getQuery(Visits.class);
    }
}