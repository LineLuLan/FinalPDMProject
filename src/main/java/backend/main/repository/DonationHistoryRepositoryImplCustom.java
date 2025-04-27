package backend.main.repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DonationHistoryRepositoryImplCustom implements DonationHistoryRepositoryCustom {
    @Override
    public List<Map<String, Object>> findAllWithDonorInfo() {
        List<Map<String, Object>> list = new ArrayList<>();
        String sql = "SELECT dh.*, d.name as donor_name, d.blood_type " +
                     "FROM DonationHistory dh LEFT JOIN Donor d ON dh.donor_ssn = d.donor_ssn";
        try (Connection conn = backend.main.config.DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("donationId", rs.getInt("donation_id"));
                row.put("donorSsn", rs.getString("donor_ssn"));
                row.put("donorName", rs.getString("donor_name"));
                row.put("bloodType", rs.getString("blood_type"));
                row.put("bid", rs.getInt("bid"));
                row.put("date", rs.getTimestamp("date"));
                row.put("quantity", rs.getInt("quantity"));
                list.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
