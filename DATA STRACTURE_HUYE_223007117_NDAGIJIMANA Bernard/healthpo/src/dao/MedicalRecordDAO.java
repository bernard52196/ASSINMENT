package dao;

import model.MedicalRecord;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MedicalRecordDAO {

    public int add(MedicalRecord m) {
        String sql = "INSERT INTO MedicalRecord (PatientID, Attribute1, Attribute2, Attribute3) VALUES (?,?,?,?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, m.getPatientID());
            ps.setString(2, m.getAttribute1());
            ps.setString(3, m.getAttribute2());
            ps.setString(4, m.getAttribute3());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public List<MedicalRecord> getAll() {
        List<MedicalRecord> list = new ArrayList<>();
        String sql = "SELECT * FROM MedicalRecord ORDER BY CreatedAt DESC";
        try (Connection c = DBConnection.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                MedicalRecord m = new MedicalRecord();
                m.setMedicalRecordID(rs.getInt("MedicalRecordID"));
                m.setPatientID(rs.getInt("PatientID"));
                m.setAttribute1(rs.getString("Attribute1"));
                m.setAttribute2(rs.getString("Attribute2"));
                m.setAttribute3(rs.getString("Attribute3"));
                m.setCreatedAt(rs.getTimestamp("CreatedAt"));
                list.add(m);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<MedicalRecord> getByPatient(int patientID) {
        List<MedicalRecord> list = new ArrayList<>();
        String sql = "SELECT * FROM MedicalRecord WHERE PatientID=? ORDER BY CreatedAt DESC";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, patientID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                MedicalRecord m = new MedicalRecord();
                m.setMedicalRecordID(rs.getInt("MedicalRecordID"));
                m.setPatientID(rs.getInt("PatientID"));
                m.setAttribute1(rs.getString("Attribute1"));
                m.setAttribute2(rs.getString("Attribute2"));
                m.setAttribute3(rs.getString("Attribute3"));
                m.setCreatedAt(rs.getTimestamp("CreatedAt"));
                list.add(m);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void update(MedicalRecord m) {
        String sql = "UPDATE MedicalRecord SET PatientID=?, Attribute1=?, Attribute2=?, Attribute3=? WHERE MedicalRecordID=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, m.getPatientID());
            ps.setString(2, m.getAttribute1());
            ps.setString(3, m.getAttribute2());
            ps.setString(4, m.getAttribute3());
            ps.setInt(5, m.getMedicalRecordID());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM MedicalRecord WHERE MedicalRecordID=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
