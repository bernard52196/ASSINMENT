package model;

import java.sql.Timestamp;

public class Billing {
    private int billingID;
    private Integer patientID;
    private String patientName;
    private double amount;
    private String paymentMethod;
    private Integer medicalRecordID;
    private Timestamp createdAt;

    public int getBillingID() { return billingID; }
    public void setBillingID(int billingID) { this.billingID = billingID; }
    public Integer getPatientID() { return patientID; }
    public void setPatientID(Integer patientID) { this.patientID = patientID; }
    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public Integer getMedicalRecordID() { return medicalRecordID; }
    public void setMedicalRecordID(Integer medicalRecordID) { this.medicalRecordID = medicalRecordID; }
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}
