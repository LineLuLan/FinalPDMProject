package backend.main.repository;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import backend.main.config.DatabaseConfig;
import backend.main.model.Donor;

import org.springframework.stereotype.Repository;

@Repository
public class DonorRepositoryImpl implements DonorRepository {
    
    @Override
    public Optional<Donor> findById(String donorSsn) {
        String sql = "SELECT * FROM Donor WHERE donor_ssn = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, donorSsn);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapRowToDonor(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Donor> findAll() {
        List<Donor> donors = new ArrayList<>();
        String sql = "SELECT * FROM Donor";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                donors.add(mapRowToDonor(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return donors;
    }

    @Override
    public int save(Donor donor) {
        String sql = "INSERT INTO Donor (name, phone, email, age, blood_type, weight, last_donation_date, health_status, is_eligible, registration_date, donor_ssn) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            setDonorParameters(stmt, donor);
            int affectedRows = stmt.executeUpdate();
            return affectedRows;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int update(Donor donor) {
        String sql = "UPDATE Donor SET name = ?, phone = ?, email = ?, age = ?, blood_type = ?, weight = ?, last_donation_date = ?, health_status = ?, is_eligible = ?, registration_date = ? WHERE donor_ssn = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            setDonorParameters(stmt, donor);
            stmt.setString(10, donor.getDonorSsn());
            return stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int deleteById(String donorSsn) {
        String sql = "DELETE FROM Donor WHERE donor_ssn = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, donorSsn);
            return stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public List<Donor> findByBloodType(String bloodType) {
        List<Donor> donors = new ArrayList<>();
        String sql = "SELECT * FROM Donor WHERE blood_type = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, bloodType);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    donors.add(mapRowToDonor(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return donors;
    }

    @Override
    public List<Donor> findEligibleDonors() {
        List<Donor> donors = new ArrayList<>();
        String sql = "SELECT * FROM Donor WHERE isEligible = TRUE";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                donors.add(mapRowToDonor(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return donors;
    }

    @Override
    public List<Donor> findByIdentity(String name, String bloodType, Integer age) {
        List<Donor> list = new ArrayList<>();
        String sql = "SELECT * FROM Donor WHERE name = ? AND blood_type = ? AND age = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setString(2, bloodType);
            stmt.setInt(3, age);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(mapRowToDonor(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Optional<Donor> findBySsn(String donorSsn) {
        String sql = "SELECT * FROM Donor WHERE donor_ssn = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, donorSsn);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapRowToDonor(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    private Donor mapRowToDonor(ResultSet rs) throws SQLException {
        Donor donor = new Donor();
        donor.setDonorSsn(rs.getString("donor_ssn"));
        donor.setName(rs.getString("name"));
        donor.setPhone(rs.getString("phone"));
        donor.setEmail(rs.getString("email"));
        donor.setAge(rs.getInt("age"));
        donor.setBloodType(rs.getString("blood_type"));
        donor.setWeight(rs.getDouble("weight"));
        Date date = rs.getDate("last_donation_date");
        donor.setLastDonationDate(date != null ? date.toLocalDate() : null);
        donor.setHealthStatus(rs.getString("health_status"));
        donor.setIsEligible(rs.getBoolean("is_eligible"));
        Timestamp timestamp = rs.getTimestamp("registration_date");
        donor.setRegistrationDate(timestamp != null ? timestamp.toLocalDateTime() : null);
        return donor;
    }

    // Helper method to set PreparedStatement parameters
    private void setDonorParameters(PreparedStatement stmt, Donor donor) throws SQLException {
        stmt.setString(1, donor.getName());
        stmt.setString(2, donor.getPhone());
        stmt.setString(3, donor.getEmail());
        stmt.setInt(4, donor.getAge());
        stmt.setString(5, donor.getBloodType());
        stmt.setDouble(6, donor.getWeight());
        stmt.setDate(7, donor.getLastDonationDate() != null ? Date.valueOf(donor.getLastDonationDate()) : null);
        stmt.setString(8, donor.getHealthStatus());
        stmt.setBoolean(9, donor.getIsEligible() != null ? donor.getIsEligible() : true);
        stmt.setTimestamp(10, donor.getRegistrationDate() != null ? Timestamp.valueOf(donor.getRegistrationDate()) : new Timestamp(System.currentTimeMillis()));
        stmt.setString(11, donor.getDonorSsn());
    }
}