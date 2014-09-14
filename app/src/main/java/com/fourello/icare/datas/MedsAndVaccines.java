package com.fourello.icare.datas;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.Date;

@ParseClassName("MedsAndVaccines")
public class MedsAndVaccines extends ParseObject {

    public MedsAndVaccines() {
        // A default constructor is required.
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

    public String getMedsDose() {
        return getString("meds_dose");
    }
    public void setMedsDose(String meds_dose) {
        put("meds_dose", meds_dose);
    }

    public String getMedsDuration() {
        return getString("meds_duration");
    }
    public void setMedsDuration(String meds_duration) {
        put("meds_duration", meds_duration);
    }

    public String getMedsFrequency() {
        return getString("meds_frequency");
    }
    public void setMedsFrequency(String meds_frequency) {
        put("meds_frequency", meds_frequency);
    }

    public String getMedsGenericName() {
        return getString("meds_genericname");
    }
    public void setMedsGenericName(String meds_genericname) {
        put("meds_genericname", meds_genericname);
    }
    public String getMedsPreparation() {
        return getString("meds_preparation");
    }
    public void setPreparation(String meds_preparation) {
        put("meds_preparation", meds_preparation);
    }

    public String getMedsGoal() {
        return getString("meds_goal");
    }
    public void setMedsGoal(String meds_goal) {
        put("meds_goal", meds_goal);
    }

    public String getName() {
        return getString("name");
    }
    public void setName(String name) {
        put("name", name);
    }

    public String getPatientId() {
        return getString("patientid");
    }
    public void setPatientId(String patientId) {
        put("patientid", patientId);
    }

    public String getType() {
        return getString("type");
    }
    public void setType(String type) {
        put("type", type);
    }

    public Date getVaccineDateTaken() {
        return getDate("vaccine_datetaken");
    }
    public void setVaccineDateTaken(Date vaccine_datetaken) {
        put("vaccine_datetaken", vaccine_datetaken);
    }

    public String getVaccineDose() {
        return getString("vaccine_dose");
    }
    public void setVaccineDose(String vaccine_dose) {
        put("vaccine_dose", vaccine_dose);
    }

    public String getVaccineId() {
        return getString("vaccineid");
    }
    public void setVaccineId(String vaccineid) {
        put("vaccineid", vaccineid);
    }

    public static ParseQuery<MedsAndVaccines> getQuery() {
        return ParseQuery.getQuery(MedsAndVaccines.class);
    }
}