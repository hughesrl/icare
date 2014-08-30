package com.fourello.icare.datas;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

import java.util.Date;

@ParseClassName("Patients")
public class Patients extends ParseObject {

    public Patients() {
        // A default constructor is required.
    }

    public ParseFile getPhotoFile() {
        return getParseFile("photoFile");
    }
    public void setPhotoFile(ParseFile photoFile) {
        put("photoFile", photoFile);
    }

    public String getAbdomenCircumference() {
        return getString("abdomencircumference");
    }
    public void setAbdomenCircumference(String abdomencircumference) {
        put("abdomencircumference", abdomencircumference);
    }

    public String getAddress1() {
        return getString("address_1");
    }
    public void setAddress1(String address_1) {
        put("address_1", address_1);
    }

    public String getAddress2() {
        return getString("address_2");
    }
    public void setAddress2(String address_2) {
        put("address_2", address_2);
    }

    public String getAddressBrgy() {
        return getString("address_brgy");
    }
    public void setAddressBrgy(String address_brgy) {
        put("address_brgy", address_brgy);
    }

    public String getAddressSt() {
        return getString("address_st");
    }
    public void setAddressSt(String address_st) {
        put("address_st", address_st);
    }

    public String getAddressUnit() {
        return getString("address_unit");
    }
    public void setAddressUnit(String address_unit) {
        put("address_unit", address_unit);
    }

    public String getAge() {
        return getString("age");
    }
    public void setAge(String age) {
        put("age", age);
    }

    public String getBabyPhoto() {
        return getString("babyphoto");
    }
    public void setBabyPhoto(String babyphoto) {
        put("babyphoto", babyphoto);
    }

    public String getBirthCertificatePhoto() {
        return getString("birthcertificatephoto");
    }
    public void setBirthCertificatePhoto(String birthcertificatephoto) {
        put("birthcertificatephoto", birthcertificatephoto);
    }

    public Date getChestCircumference() {
        return getDate("chestcircumference");
    }
    public void setChestCircumference(Date chestcircumference) {
        put("chestcircumference", chestcircumference);
    }

    public String getCircumcisedOn() {
        return getString("circumcisedon");
    }
    public void setCircumcisedOn(String circumcisedon) {
        put("circumcisedon", circumcisedon);
    }

    public String getCity() {
        return getString("city");
    }
    public void setCity(String city) {
        put("city", city);
    }

    public Date getDateOfBirth() {
        return getDate("dateofbirth");
    }
    public void setDateOfBirth(Date dateOfBirth) {
        put("dateofbirth", dateOfBirth);
    }

    public String getDeliveredBy() {
        return getString("deliveredby");
    }
    public void setDeliveredBy(String deliveredby) {
        put("deliveredby", deliveredby);
    }

    public String getDoctorId() {
        return getString("doctorid");
    }
    public void setDoctorId(String doctorid) {
        put("doctorid", doctorid);
    }

    public Date getEarPiercedOn() {
        return getDate("earpiercedon");
    }
    public void setEarPiercedOn(Date earpiercedon) {
        put("earpiercedon", earpiercedon);
    }

    public String getFatherBirthday() {
        return getString("fatherbirthday");
    }
    public void setFatherBirthday(String fatherbirthday) {
        put("fatherbirthday", fatherbirthday);
    }

    public String getFatherCompany() {
        return getString("fathercompany");
    }
    public void setFatherCompany(String fathercompany) {
        put("fathercompany", fathercompany);
    }

    public String getFatherContactNumber() {
        return getString("fathercontactnumber");
    }
    public void setFatherContactNumber(String fathercontactnumber) {
        put("fathercontactnumber", fathercontactnumber);
    }

    public String getFatherEmail() {
        return getString("fatheremail");
    }
    public void setFatherEmail(String fatheremail) {
        put("fatheremail", fatheremail);
    }

    public String getFatherFirstName() {
        return getString("fatherfirstname");
    }
    public void setFatherFirstName(String fatherfirstname) {
        put("fatherfirstname", fatherfirstname);
    }

    public String getFatherHMO() {
        return getString("fatherhmo");
    }
    public void setFatherHMO(String fatherhmo) {
        put("fatherhmo", fatherhmo);
    }

    public String getFatherLastName() {
        return getString("fatherlastname");
    }
    public void setFatherLastName(String fatherlastname) {
        put("fatherlastname", fatherlastname);
    }

    public String getFatherPhoto() {
        return getString("fatherphoto");
    }
    public void getFatherPhoto(String fatherphoto) {
        put("fatherphoto", fatherphoto);
    }

    public String getFatherProfession() {
        return getString("fatherprofession");
    }
    public void setFatherProfession(String fatherprofession) {
        put("fatherprofession", fatherprofession);
    }

    public String getFirstName() {
        return getString("firstname");
    }
    public void getFirstName(String firstname) {
        put("firstname", firstname);
    }

    public String getGender() {
        return getString("gender");
    }
    public void getGender(String gender) {
        put("gender", gender);
    }

    public String getHeadCircumference() {
        return getString("headcircumference");
    }
    public void getHeadCircumference(String headcircumference) {
        put("headcircumference", headcircumference);
    }

    public String getLastName() {
        return getString("lastname");
    }
    public void getLastName(String lastname) {
        put("lastname", lastname);
    }

    public String getLength() {
        return getString("length");
    }
    public void getLength(String length) {
        put("length", length);
    }

    public String getMiddleName() {
        return getString("middlename");
    }
    public void getMiddleName(String middlename) {
        put("middlename", middlename);
    }

    public String getMotherBirthday() {
        return getString("motherbirthday");
    }
    public void setMotherBirthday(String motherbirthday) {
        put("motherbirthday", motherbirthday);
    }

    public String getMotherCompany() {
        return getString("mothercompany");
    }
    public void setMotherCompany(String mothercompany) {
        put("mothercompany", mothercompany);
    }

    public String getMotherContactNumber() {
        return getString("mothercontactnumber");
    }
    public void setMotherContactNumber(String mothercontactnumber) {
        put("mothercontactnumber", mothercontactnumber);
    }

    public String getMotherEmail() {
        return getString("motheremail");
    }
    public void setMotherEmail(String motheremail) {
        put("motheremail", motheremail);
    }

    public String getMotherFirstName() {
        return getString("motherfirstname");
    }
    public void setMotherFirstName(String motherfirstname) {
        put("motherfirstname", motherfirstname);
    }

    public String getMotherHMO() {
        return getString("motherhmo");
    }
    public void setMotherHMO(String motherhmo) {
        put("motherhmo", motherhmo);
    }

    public String getMotherLastName() {
        return getString("motherlastname");
    }
    public void setMotherLastName(String motherlastname) {
        put("motherlastname", motherlastname);
    }

    public String getMotherPhoto() {
        return getString("motherphoto");
    }
    public void getMotherPhoto(String motherphoto) {
        put("motherphoto", motherphoto);
    }

    public String getMotherProfession() {
        return getString("motherprofession");
    }
    public void setMotherProfession(String motherprofession) {
        put("motherprofession", motherprofession);
    }

    public String getOtherMobileNo() {
        return getString("othermobileno");
    }
    public void setOtherMobileNo(String othermobileno) {
        put("othermobileno", othermobileno);
    }

    public String getPlaceOfBirth() {
        return getString("placeofbirth");
    }
    public void setPlaceOfBirth(String placeofbirth) {
        put("placeofbirth", placeofbirth);
    }

    public String getProvince() {
        return getString("province");
    }
    public void setProvince(String province) {
        put("province", province);
    }

    public String getTimeOfBirth() {
        return getString("timeofbirth");
    }
    public void setTimeOfBirth(String timeofbirth) {
        put("timeofbirth", timeofbirth);
    }

    public String getTypeOfDelivery() {
        return getString("typeofdelivery");
    }
    public void setTypeOfDelivery(String typeofdelivery) {
        put("typeofdelivery", typeofdelivery);
    }

    public String getWeight() {
        return getString("weight");
    }
    public void setWeight(String weight) {
        put("weight", weight);
    }

    public String getZipCode() {
        return getString("zipcode");
    }
    public void setZipCode(String zipcode) {
        put("zipcode", zipcode);
    }
}