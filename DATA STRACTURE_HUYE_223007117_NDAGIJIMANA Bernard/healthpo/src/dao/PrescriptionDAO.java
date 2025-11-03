package dao;

import model.Prescription;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PrescriptionDAO {

    public void add(Prescription p) {
        String sql = "INSERT INTO Prescription (AppointmentID, Attribute1, Attribute2, Attribute3, CreatedAt) VALUES (?, ?, ?, ?, NOW())";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            if (p.getAppointmentID() != null)
                ps.setInt(1, p.getAppointmentID());
            else
                ps.setNull(1, Types.INTEGER);
            ps.setString(2, p.getAttribute1());
            ps.setString(3, p.getAttribute2());
            ps.setString(4, p.getAttribute3());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update(Prescription p) {
        String sql = "UPDATE Prescription SET Attribute1=?, Attribute2=?, Attribute3=? WHERE PrescriptionID=?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getAttribute1());
            ps.setString(2, p.getAttribute2());
            ps.setString(3, p.getAttribute3());
            ps.setInt(4, p.getPrescriptionID());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM Prescription WHERE PrescriptionID=?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Prescription getById(int id) {
        String sql = "SELECT * FROM Prescription WHERE PrescriptionID=?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return extract(rs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Prescription> getAll() {
        List<Prescription> list = new ArrayList<>();
        String sql = "SELECT * FROM Prescription ORDER BY CreatedAt DESC";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(extract(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Prescription> getByAppointmentId(int appointmentId) {
        List<Prescription> list = new ArrayList<>();
        String sql = "SELECT * FROM Prescription WHERE AppointmentID=?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, appointmentId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(extract(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    private Prescription extract(ResultSet rs) throws SQLException {
        Prescription p = new Prescription();
        p.setPrescriptionID(rs.getInt("PrescriptionID"));
        p.setAppointmentID(rs.getObject("AppointmentID") != null ? rs.getInt("AppointmentID") : null);
        p.setAttribute1(rs.getString("Attribute1"));
        p.setAttribute2(rs.getString("Attribute2"));
        p.setAttribute3(rs.getString("Attribute3"));
        p.setCreatedAt(rs.getTimestamp("CreatedAt"));
        return p;
    }
}
