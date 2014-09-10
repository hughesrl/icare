package com.fourello.icare.datas;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;

public class PatientVisits implements Parcelable {
    public String patientObjectId;
    public String visitDate;

    public String accompaniedBy;
    public String age;
    public String allergyrisk;
    public String chest;
    public String diagnosis;
    public String doctorid;
    public String email;
    public String head;
    public String height;
    public String instructions;
    public String medications;
    public String nextvisit;
    public String patientid;
    public String patientname;
    public String personal_notes;
    public byte[] photoFile;
    public String pupose_of_visit;
    public String question_for_doctor;
    public String relationship_to_patient;
    public String temperature;
    public String typeofdelivery;
    public String vaccinations;
    public String weight;


    public static Creator<PatientVisits> getCreator() {
        return CREATOR;
    }

    public PatientVisits() {
        patientObjectId = "";
        accompaniedBy = "";
        age = "";
        allergyrisk = "";
        chest = "";
        diagnosis = "";
        doctorid = "";
        email = "";
        head = "";
        height = "";
        instructions = "";
        medications = "";
        nextvisit = "";
        patientid = "";
        patientname = "";
        personal_notes = "";
        photoFile = null;
        pupose_of_visit = "";
        question_for_doctor = "";
        relationship_to_patient = "";
        temperature = "";
        typeofdelivery = "";
        vaccinations = "";
        weight = "";
        visitDate = "";
    }

    public PatientVisits(Parcel in) {
        patientObjectId = in.readString();
        visitDate = in.readString();

        accompaniedBy = in.readString();
        age = in.readString();
        allergyrisk = in.readString();

        chest = in.readString();
        diagnosis = in.readString();
        doctorid = in.readString();
        email = in.readString();
        head = in.readString();
        height = in.readString();
        instructions = in.readString();
        medications = in.readString();
        nextvisit = in.readString();
        patientid = in.readString();
        patientname = in.readString();
        personal_notes = in.readString();

        photoFile = new byte[in.readInt()];
        in.readByteArray(photoFile);

        pupose_of_visit = in.readString();
        question_for_doctor = in.readString();
        relationship_to_patient = in.readString();
        temperature = in.readString();
        typeofdelivery = in.readString();
        vaccinations = in.readString();
        weight = in.readString();


    }


    public PatientVisits(String patientObjectId, String visitDate) {
        super();
        setPatientObjectId(patientObjectId);
        setVisitDate(visitDate);
    }

    public String getPatientObjectId() {
        return patientObjectId;
    }
    public void setPatientObjectId(String patientObjectId) {
        this.patientObjectId = patientObjectId;
    }

    public String getVisitDate() {
        return visitDate;
    }
    public void setVisitDate(String visitDate) {
        this.visitDate = visitDate;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getAccompaniedBy() {
        return accompaniedBy;
    }

    public void setAccompaniedBy(String accompaniedBy) {
        this.accompaniedBy = accompaniedBy;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getAllergyrisk() {
        return allergyrisk;
    }

    public void setAllergyrisk(String allergyrisk) {
        this.allergyrisk = allergyrisk;
    }

    public String getChest() {
        return chest;
    }

    public void setChest(String chest) {
        this.chest = chest;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getDoctorid() {
        return doctorid;
    }

    public void setDoctorid(String doctorid) {
        this.doctorid = doctorid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getMedications() {
        return medications;
    }

    public void setMedications(String medications) {
        this.medications = medications;
    }

    public String getNextvisit() {
        return nextvisit;
    }

    public void setNextvisit(String nextvisit) {
        this.nextvisit = nextvisit;
    }

    public String getPatientid() {
        return patientid;
    }

    public void setPatientid(String patientid) {
        this.patientid = patientid;
    }

    public String getPatientname() {
        return patientname;
    }

    public void setPatientname(String patientname) {
        this.patientname = patientname;
    }

    public String getPersonal_notes() {
        return personal_notes;
    }

    public void setPersonal_notes(String personal_notes) {
        this.personal_notes = personal_notes;
    }

    public byte[] getPhotoFile() {
        return photoFile;
    }

    public void setPhotoFile(byte[] photoFile) {
        this.photoFile = photoFile;
    }

    public String getPupose_of_visit() {
        return pupose_of_visit;
    }

    public void setPupose_of_visit(String pupose_of_visit) {
        this.pupose_of_visit = pupose_of_visit;
    }

    public String getQuestion_for_doctor() {
        return question_for_doctor;
    }

    public void setQuestion_for_doctor(String question_for_doctor) {
        this.question_for_doctor = question_for_doctor;
    }

    public String getRelationship_to_patient() {
        return relationship_to_patient;
    }

    public void setRelationship_to_patient(String relationship_to_patient) {
        this.relationship_to_patient = relationship_to_patient;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getTypeofdelivery() {
        return typeofdelivery;
    }

    public void setTypeofdelivery(String typeofdelivery) {
        this.typeofdelivery = typeofdelivery;
    }

    public String getVaccinations() {
        return vaccinations;
    }

    public void setVaccinations(String vaccinations) {
        this.vaccinations = vaccinations;
    }

    @Override
    public int describeContents() {
        return 0;
    }
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(patientObjectId);
        dest.writeString(visitDate);
    }

    public static final Creator<PatientVisits> CREATOR = new Creator<PatientVisits>()
    {
        public PatientVisits createFromParcel(Parcel in)
        {
            return new PatientVisits(in);
        }
        public PatientVisits[] newArray(int size)
        {
            return new PatientVisits[size];
        }
    };
}
