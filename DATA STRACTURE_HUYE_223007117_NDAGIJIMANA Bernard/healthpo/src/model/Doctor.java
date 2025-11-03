package model;

import java.sql.Date;
import java.sql.Timestamp;

public class Doctor {
    private int doctorID;
    private int userID;
    private String name;
    private String identifier;
    private String status;
    private String location;
    private String contact;
    private Date assignedSince;
    private Timestamp createdAt;

    public int getDoctorID() { return doctorID; }
    public void setDoctorID(int doctorID) { this.doctorID = doctorID; }
    public int getUserID() { return userID; }
    public void setUserID(int userID) { this.userID = userID; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getIdentifier() { return identifier; }
    public void setIdentifier(String identifier) { this.identifier = identifier; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }
    public Date getAssignedSince() { return assignedSince; }
    public void setAssignedSince(Date assignedSince) { this.assignedSince = assignedSince; }
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}
