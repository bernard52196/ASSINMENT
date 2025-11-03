package dao;

import model.User;
import util.DBConnection;

import java.sql.*;

public class UserDAO {

    public int registerUser(String username, String password, String email, String fullName, String role) {
        String sql = "INSERT INTO User (Username, PasswordHash, Email, FullName, Role) VALUES (?,?,?,?,?)";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, username);
            ps.setString(2, password); // TODO: hash in production
            ps.setString(3, email);
            ps.setString(4, fullName);
            ps.setString(5, role);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return -1;
    }

    public User authenticateUser(String username, String password) {
        String sql = "SELECT * FROM User WHERE Username=? AND PasswordHash=?";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                User u = new User();
                u.setUserID(rs.getInt("UserID"));
                u.setUsername(rs.getString("Username"));
                u.setPasswordHash(rs.getString("PasswordHash"));
                u.setEmail(rs.getString("Email"));
                u.setFullName(rs.getString("FullName"));
                u.setRole(rs.getString("Role"));
                u.setCreatedAt(rs.getTimestamp("CreatedAt"));
                u.setLastLogin(rs.getTimestamp("LastLogin"));
                updateLastLogin(u.getUserID());
                return u;
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public void updateLastLogin(int userId) {
        String sql = "UPDATE User SET LastLogin=CURRENT_TIMESTAMP WHERE UserID=?";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
}
