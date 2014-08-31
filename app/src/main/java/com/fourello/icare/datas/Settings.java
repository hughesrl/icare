package com.fourello.icare.datas;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

import java.util.Date;

@ParseClassName("Settings")
public class Settings extends ParseObject {

    public Settings() {
        // A default constructor is required.
    }

    public String getDoctorId() {
        return getString("doctorid");
    }
    public void setDoctorId(String doctorid) {
        put("doctorid", doctorid);
    }

    public String getPin() {
        return getString("pin");
    }
    public void setPin(String pin) {
        put("pin", pin);
    }

}