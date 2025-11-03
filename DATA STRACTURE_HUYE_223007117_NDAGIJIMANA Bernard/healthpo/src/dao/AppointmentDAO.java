package dao;

import model.Appointment;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AppointmentDAO {

    public int add(Appointment a) {
        String sql = "INSERT INTO Appointment (OrderNumber, PatientID, DoctorID, AppointmentDate, Status, TotalAmount, PaymentMethod, Notes) VALUES (?,?,?,?,?,?,?,?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, a.getOrderNumber());
            ps.setInt(2, a.getPatientID());
            ps.setInt(3, a.getDoctorID());
            ps.setTimestamp(4, a.getAppointmentDate());
            ps.setString(5, a.getStatus());
            ps.setDouble(6, a.getTotalAmount());
            ps.setString(7, a.getPaymentMethod());
            ps.setString(8, a.getNotes());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public List<Appointment> getAll() {
        List<Appointment> list = new ArrayList<>();
        String sql = "SELECT * FROM Appointment ORDER BY AppointmentDate DESC";
        try (Connection c = DBConnection.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Appointment a = new Appointment();
                a.setAppointmentID(rs.getInt("AppointmentID"));
                a.setOrderNumber(rs.getString("OrderNumber"));
                a.setPatientID(rs.getInt("PatientID"));
                a.setDoctorID(rs.getInt("DoctorID"));
                a.setAppointmentDate(rs.getTimestamp("AppointmentDate"));
                a.setStatus(rs.getString("Status"));
                a.setTotalAmount(rs.getDouble("TotalAmount"));
                a.setPaymentMethod(rs.getString("PaymentMethod"));
                a.setNotes(rs.getString("Notes"));
                a.setCreatedAt(rs.getTimestamp("CreatedAt"));
                list.add(a);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Appointment> getByDoctor(int doctorId) {
        List<Appointment> list = new ArrayList<>();
        String sql = "SELECT * FROM Appointment WHERE DoctorID=? ORDER BY AppointmentDate DESC";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, doctorId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Appointment a = new Appointment();
                a.setAppointmentID(rs.getInt("AppointmentID"));
                a.setOrderNumber(rs.getString("OrderNumber"));
                a.setPatientID(rs.getInt("PatientID"));
                a.setDoctorID(rs.getInt("DoctorID"));
                a.setAppointmentDate(rs.getTimestamp("AppointmentDate"));
                a.setStatus(rs.getString("Status"));
                a.setTotalAmount(rs.getDouble("TotalAmount"));
                a.setPaymentMethod(rs.getString("PaymentMethod"));
                a.setNotes(rs.getString("Notes"));
                a.setCreatedAt(rs.getTimestamp("CreatedAt"));
                list.add(a);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Appointment> getByPatient(int patientId) {
        List<Appointment> list = new ArrayList<>();
        String sql = "SELECT * FROM Appointment WHERE PatientID=? ORDER BY AppointmentDate DESC";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, patientId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Appointment a = new Appointment();
                a.setAppointmentID(rs.getInt("AppointmentID"));
                a.setOrderNumber(rs.getString("OrderNumber"));
                a.setPatientID(rs.getInt("PatientID"));
                a.setDoctorID(rs.getInt("DoctorID"));
                a.setAppointmentDate(rs.getTimestamp("AppointmentDate"));
                a.setStatus(rs.getString("Status"));
                a.setTotalAmount(rs.getDouble("TotalAmount"));
                a.setPaymentMethod(rs.getString("PaymentMethod"));
                a.setNotes(rs.getString("Notes"));
                a.setCreatedAt(rs.getTimestamp("CreatedAt"));
                list.add(a);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void update(Appointment a) {
        String sql = "UPDATE Appointment SET OrderNumber=?, PatientID=?, DoctorID=?, AppointmentDate=?, Status=?, TotalAmount=?, PaymentMethod=?, Notes=? WHERE AppointmentID=?";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, a.getOrderNumber());
            ps.setInt(2, a.getPatientID());
            ps.setInt(3, a.getDoctorID());
            ps.setTimestamp(4, a.getAppointmentDate());
            ps.setString(5, a.getStatus());
            ps.setDouble(6, a.getTotalAmount());
            ps.setString(7, a.getPaymentMethod());
            ps.setString(8, a.getNotes());
            ps.setInt(9, a.getAppointmentID());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void updateStatus(int id, String newStatus) {
        String sql = "UPDATE Appointment SET Status=? WHERE AppointmentID=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, newStatus);
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM Appointment WHERE AppointmentID=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
