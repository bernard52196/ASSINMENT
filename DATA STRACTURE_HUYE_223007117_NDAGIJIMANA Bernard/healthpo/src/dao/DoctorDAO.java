package dao;

import model.Doctor;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DoctorDAO {

    public int add(Doctor d) {
        String sql = "INSERT INTO Doctor (UserID, Name, Identifier, Status, Location, Contact, AssignedSince) VALUES (?,?,?,?,?,?,?)";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, d.getUserID());
            ps.setString(2, d.getName());
            ps.setString(3, d.getIdentifier());
            ps.setString(4, d.getStatus());
            ps.setString(5, d.getLocation());
            ps.setString(6, d.getContact());
            ps.setDate(7, d.getAssignedSince());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return -1;
    }

    public Doctor getByUserId(int userId) {
        String sql = "SELECT * FROM Doctor WHERE UserID=?";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Doctor d = new Doctor();
                d.setDoctorID(rs.getInt("DoctorID"));
                d.setUserID(rs.getInt("UserID"));
                d.setName(rs.getString("Name"));
                d.setIdentifier(rs.getString("Identifier"));
                d.setStatus(rs.getString("Status"));
                d.setLocation(rs.getString("Location"));
                d.setContact(rs.getString("Contact"));
                d.setAssignedSince(rs.getDate("AssignedSince"));
                d.setCreatedAt(rs.getTimestamp("CreatedAt"));
                return d;
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public List<Doctor> getAll() {
        List<Doctor> list = new ArrayList<>();
        String sql = "SELECT * FROM Doctor ORDER BY CreatedAt DESC";
        try (Connection c = DBConnection.getConnection(); Statement st = c.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Doctor d = new Doctor();
                d.setDoctorID(rs.getInt("DoctorID"));
                d.setUserID(rs.getInt("UserID"));
                d.setName(rs.getString("Name"));
                d.setIdentifier(rs.getString("Identifier"));
                d.setStatus(rs.getString("Status"));
                d.setLocation(rs.getString("Location"));
                d.setContact(rs.getString("Contact"));
                d.setAssignedSince(rs.getDate("AssignedSince"));
                d.setCreatedAt(rs.getTimestamp("CreatedAt"));
                list.add(d);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public void update(Doctor d) {
        String sql = "UPDATE Doctor SET Name=?, Identifier=?, Status=?, Location=?, Contact=?, AssignedSince=? WHERE DoctorID=?";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, d.getName());
            ps.setString(2, d.getIdentifier());
            ps.setString(3, d.getStatus());
            ps.setString(4, d.getLocation());
            ps.setString(5, d.getContact());
            ps.setDate(6, d.getAssignedSince());
            ps.setInt(7, d.getDoctorID());
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void delete(int id) {
        String sql = "DELETE FROM Doctor WHERE DoctorID=?";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
}
