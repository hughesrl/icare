package com.fourello.icare.datas;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.Date;

@ParseClassName("PatientNotes")
public class PatientNotes extends ParseObject {

    public PatientNotes() {
        // A default constructor is required.
    }

    public String getNotes() {
        return getString("notes");
    }
    public void setNotes(String notes) {
        put("notes", notes);
    }

    public String getPatientId() {
        return getString("patientid");
    }
    public void setPatientId(String patientId) {
        put("patientid", patientId);
    }

    public String getQuestion() {
        return getString("question");
    }
    public void setQuestion(String question) {
        put("question", question);
    }

    public String getSubject() {
        return getString("subject");
    }
    public void setSubject(String subject) {
        put("subject", subject);
    }

    public String getTag() {
        return getString("tag");
    }
    public void setTag(String tag) {
        put("tag", tag);
    }

    public void setCreateDate(Date createDate) {
        put("createdAt", createDate);
    }

    public static ParseQuery<PatientNotes> getQuery() {
        return ParseQuery.getQuery(PatientNotes.class);
    }
}