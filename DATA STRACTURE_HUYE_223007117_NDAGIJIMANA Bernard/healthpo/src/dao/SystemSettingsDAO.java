package dao;

import util.DBConnection;
import java.sql.*;

public class SystemSettingsDAO {

    public String getDoctorCode() {
        String sql = "SELECT SettingValue FROM system_settings WHERE SettingKey='DoctorCode'";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getString(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "DOC00"; // default fallback
    }

    public boolean updateDoctorCode(String newCode) {
        String sql = "UPDATE system_settings SET SettingValue=? WHERE SettingKey='DoctorCode'";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, newCode);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
