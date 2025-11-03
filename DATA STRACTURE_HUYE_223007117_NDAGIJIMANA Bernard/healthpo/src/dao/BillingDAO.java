package dao;

import model.Billing;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BillingDAO {


    public int add(Billing b) {
        String sql = "INSERT INTO Billing (PatientID, PatientName, Amount, PaymentMethod, MedicalRecordID) VALUES (?,?,?,?,?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            if (b.getPatientID() == null) ps.setNull(1, Types.INTEGER);
            else ps.setInt(1, b.getPatientID());

            ps.setString(2, b.getPatientName());
            ps.setDouble(3, b.getAmount());
            ps.setString(4, b.getPaymentMethod());

            if (b.getMedicalRecordID() == null) ps.setNull(5, Types.INTEGER);
            else ps.setInt(5, b.getMedicalRecordID());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public List<Billing> getAll() {
        List<Billing> list = new ArrayList<>();
        String sql = "SELECT * FROM Billing ORDER BY CreatedAt DESC";
        try (Connection c = DBConnection.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Billing b = new Billing();
                b.setBillingID(rs.getInt("BillingID"));
                b.setPatientID(rs.getObject("PatientID") != null ? rs.getInt("PatientID") : null);
                b.setPatientName(rs.getString("PatientName"));
                b.setAmount(rs.getDouble("Amount"));
                b.setPaymentMethod(rs.getString("PaymentMethod"));
                b.setMedicalRecordID(rs.getObject("MedicalRecordID") != null ? rs.getInt("MedicalRecordID") : null);
                b.setCreatedAt(rs.getTimestamp("CreatedAt"));
                list.add(b);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }


    public List<Billing> getByPatient(int patientId) {
        List<Billing> list = new ArrayList<>();
        String sql = "SELECT * FROM Billing WHERE PatientID=? ORDER BY CreatedAt DESC";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, patientId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Billing b = new Billing();
                b.setBillingID(rs.getInt("BillingID"));
                b.setPatientID(rs.getInt("PatientID"));
                b.setPatientName(rs.getString("PatientName"));
                b.setAmount(rs.getDouble("Amount"));
                b.setPaymentMethod(rs.getString("PaymentMethod"));
                b.setMedicalRecordID(rs.getObject("MedicalRecordID") != null ? rs.getInt("MedicalRecordID") : null);
                b.setCreatedAt(rs.getTimestamp("CreatedAt"));
                list.add(b);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }


    public void update(Billing b) {
        String sql = "UPDATE Billing SET PatientName=?, Amount=?, PaymentMethod=? WHERE BillingID=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, b.getPatientName());
            ps.setDouble(2, b.getAmount());
            ps.setString(3, b.getPaymentMethod());
            ps.setInt(4, b.getBillingID());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void delete(int id) {
        String sql = "DELETE FROM Billing WHERE BillingID=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
