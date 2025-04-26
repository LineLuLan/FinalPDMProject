package backend.main.repository;

import backend.main.config.DatabaseConfig;
import backend.main.model.DonationHistory;
import org.springframework.stereotype.Repository;

import java.sql.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class DonationHistoryRepositoryImpl implements DonationHistoryRepository {
    


    @Override
    public Optional<DonationHistory> findById(Integer donationId) {
        String sql = "SELECT * FROM DonationHistory WHERE donation_id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, donationId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapRowToDonationHistory(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<DonationHistory> findAll() {
        List<DonationHistory> list = new ArrayList<>();
        String sql = "SELECT * FROM DonationHistory";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(mapRowToDonationHistory(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public int save(DonationHistory donation) {
        // Nếu date == null thì tự động lấy thời gian thực tế
        if (donation.getDate() == null) {
            donation.setDate(java.time.LocalDateTime.now());
        }
        String sql = "INSERT INTO DonationHistory (donor_ssn, bid, date, quantity) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            setDonationHistoryParameters(stmt, donation);
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) return 0;
            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                donation.setDonationId(generatedKeys.getInt(1));
            }
            return affectedRows;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int update(DonationHistory donation) {
        String sql = "UPDATE DonationHistory SET donor_ssn = ?, bid = ?, date = ?, quantity = ? WHERE donation_id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            setDonationHistoryParameters(stmt, donation);
            stmt.setInt(5, donation.getDonationId());
            return stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int deleteById(Integer donationId) {
        String sql = "DELETE FROM DonationHistory WHERE donation_id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, donationId);
            return stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public List<DonationHistory> findByDonorSsn(String donorSsn) {
        List<DonationHistory> list = new ArrayList<>();
        String sql = "SELECT * FROM DonationHistory WHERE donor_ssn = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, donorSsn);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(mapRowToDonationHistory(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<DonationHistory> findByBid(Integer bid) {
        List<DonationHistory> list = new ArrayList<>();
        String sql = "SELECT * FROM DonationHistory WHERE bid = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, bid);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(mapRowToDonationHistory(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private DonationHistory mapRowToDonationHistory(ResultSet rs) throws SQLException {
        DonationHistory dh = new DonationHistory();
        dh.setDonationId(rs.getInt("donation_id"));
        dh.setDonorSsn(rs.getString("donor_ssn"));
        dh.setBid(rs.getInt("bid"));
        Timestamp ts = rs.getTimestamp("date");
        if (ts != null) dh.setDate(ts.toLocalDateTime());
        dh.setQuantity(rs.getInt("quantity"));
        return dh;
    }

    private void setDonationHistoryParameters(PreparedStatement stmt, DonationHistory donation) throws SQLException {
        stmt.setString(1, donation.getDonorSsn());
        stmt.setInt(2, donation.getBid());
        stmt.setTimestamp(3, donation.getDate() != null ? Timestamp.valueOf(donation.getDate()) : null);
        stmt.setInt(4, donation.getQuantity());
    }
}
