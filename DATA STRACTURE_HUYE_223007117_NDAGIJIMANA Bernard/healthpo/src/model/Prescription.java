package model;

import java.sql.Timestamp;

public class Prescription {
    private int prescriptionID;
    private Integer appointmentID;
    private String attribute1;
    private String attribute2;
    private String attribute3;
    private Timestamp createdAt;

    public int getPrescriptionID() { return prescriptionID; }
    public void setPrescriptionID(int prescriptionID) { this.prescriptionID = prescriptionID; }

    public Integer getAppointmentID() { return appointmentID; }
    public void setAppointmentID(Integer appointmentID) { this.appointmentID = appointmentID; }

    public String getAttribute1() { return attribute1; }
    public void setAttribute1(String attribute1) { this.attribute1 = attribute1; }

    public String getAttribute2() { return attribute2; }
    public void setAttribute2(String attribute2) { this.attribute2 = attribute2; }

    public String getAttribute3() { return attribute3; }
    public void setAttribute3(String attribute3) { this.attribute3 = attribute3; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return "Prescription{" +
                "ID=" + prescriptionID +
                ", AppointmentID=" + appointmentID +
                ", Attribute1='" + attribute1 + '\'' +
                ", Attribute2='" + attribute2 + '\'' +
                ", Attribute3='" + attribute3 + '\'' +
                ", CreatedAt=" + createdAt +
                '}';
    }
}
