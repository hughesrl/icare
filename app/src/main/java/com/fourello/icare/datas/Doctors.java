package com.fourello.icare.datas;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.Date;

@ParseClassName("Doctors")
public class Doctors extends ParseObject {

    public Doctors() {
        // A default constructor is required.
    }

    public ParseFile getPhotoFile() {
        return getParseFile("photoFile");
    }
    public void setPhotoFile(ParseFile photoFile) {
        put("photoFile", photoFile);
    }

    public String getClinic1Address1() {
        return getString("clinic1_address_1");
    }
    public void setClinic1Address1(String clinic1_address_1) {
        put("clinic1_address_1", clinic1_address_1);
    }

    public String getClinic1City() {
        return getString("clinic1_city");
    }
    public void setClinic1City(String clinic1_city) {
        put("clinic1_city", clinic1_city);
    }

    public String getClinic1ContactNo() {
        return getString("clinic1_contactno");
    }
    public void setClinic1ContactNo(String clinic1_contactno) {
        put("clinic1_contactno", clinic1_contactno);
    }

    public String getClinic1Name() {
        return getString("clinic1_name");
    }
    public void setClinic1Name(String clinic1_name) {
        put("clinic1_name", clinic1_name);
    }

    public String getClinic1Notes() {
        return getString("clinic1_notes");
    }
    public void setClinic1Notes(String clinic1_notes) {
        put("clinic1_notes", clinic1_notes);
    }

    public String getClinic1Schedule() {
        return getString("clinic1_schedule");
    }
    public void setClinic1Schedule(String clinic1_schedule) {
        put("clinic1_schedule", clinic1_schedule);
    }

    public String getClinic1Time() {
        return getString("clinic1_time");
    }
    public void setClinic1Time(String clinic1_time) {
        put("clinic1_time", clinic1_time);
    }

    public int getDoctorID() {
        return getInt("doctorID");
    }
    public void setDoctorID(String doctorID) {
        put("doctorID", doctorID);
    }

    public String getDoctorEmail() {
        return getString("email");
    }
    public void setDoctorEmail(String email) {
        put("email", email);
    }

    public String getFirstName() {
        return getString("firstname");
    }
    public void setFirstName(String firstName) {
        put("firstname", firstName);
    }

    public String getLastName() {
        return getString("lastname");
    }
    public void setLastName(String lastname) {
        put("lastname", lastname);
    }

    public String getLinkedDoctorId() {
        return getString("linked_doctorid");
    }
    public void setLinkedDoctorId(String linked_doctorid) {
        put("linked_doctorid", linked_doctorid);
    }

    public String getMobileNo() {
        return getString("mobileno");
    }
    public void setMobileNo(String mobileno) {
        put("mobileno", mobileno);
    }

    public Date getPassword() {
        return getDate("password");
    }
    public void setPassword(Date password) {
        put("password", password);
    }

    public String getSecretary1FirstName() {
        return getString("secretary1_firstname");
    }
    public void setSecretary1FirstName(String secretary1_firstname) {
        put("secretary1_firstname", secretary1_firstname);
    }

    public String getSecretary1LastName() {
        return getString("secretary1_lastname");
    }
    public void setSecretary1FirstName(Date secretary1_lastname) {
        put("secretary1_lastname", secretary1_lastname);
    }

    public String getSecretary1Mobile() {
        return getString("secretary1_mobile");
    }
    public void setSecretary1Mobile(String secretary1_mobile) {
        put("secretary1_mobile", secretary1_mobile);
    }

    public String getType() {
        return getString("type");
    }
    public void setType(String Type) {
        put("Type", Type);
    }

    public String getUserName() {
        return getString("username");
    }
    public void setUserName(String username) {
        put("username", username);
    }

    public static ParseQuery<Doctors> getQuery() {
        return ParseQuery.getQuery(Doctors.class);
    }
}