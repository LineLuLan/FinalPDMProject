package backend.main.repository;

import backend.main.config.DatabaseConfig;
import backend.main.model.BloodBank;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

@Repository
public class BloodBankRepositoryImpl implements BloodBankRepository {
    @Override
    public Optional<BloodBank> findById(Integer bankId) {
        String sql = "SELECT * FROM BloodBank WHERE bid = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, bankId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapRowToBloodBank(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<BloodBank> findAll() {
        List<BloodBank> banks = new ArrayList<>();
        String sql = "SELECT * FROM BloodBank";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                banks.add(mapRowToBloodBank(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return banks;
    }

    @Override
    public int save(BloodBank bank) {
        // Check for duplicate by name + address + city + country
        String checkSql = "SELECT COUNT(*) FROM BloodBank WHERE name = ? AND address = ? AND city = ? AND country = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
            checkStmt.setString(1, bank.getName());
            checkStmt.setString(2, bank.getAddress());
            checkStmt.setString(3, bank.getCity());
            checkStmt.setString(4, bank.getCountry());
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                // Duplicate found
                System.err.println("BloodBank already exists with same name, address, city, and country.");
                return -1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
        String sql = "INSERT INTO BloodBank (name, contactEmail, contactPhone, address, city, country, postalCode, lastInventoryDate) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            setBloodBankParameters(stmt, bank, false);
            return stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int update(BloodBank bank) {
        String sql = "UPDATE BloodBank SET name = ?, contactEmail = ?, contactPhone = ?, address = ?, city = ?, country = ?, postalCode = ?, lastInventoryDate = ? WHERE bid = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            setBloodBankParameters(stmt, bank, true);
            stmt.setInt(9, bank.getBid());
            return stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int deleteById(Integer bankId) {
        String sql = "DELETE FROM BloodBank WHERE bid = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, bankId);
            return stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public List<BloodBank> findByLocation(String location) {
        List<BloodBank> banks = new ArrayList<>();
        String sql = "SELECT * FROM BloodBank WHERE city = ? OR country = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, location);
            stmt.setString(2, location);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    banks.add(mapRowToBloodBank(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return banks;
    }

    // Helper method to map ResultSet to BloodBank object
    private BloodBank mapRowToBloodBank(ResultSet rs) throws SQLException {
        BloodBank bank = new BloodBank();
        bank.setBid(rs.getInt("bid"));
        bank.setName(rs.getString("name"));
        bank.setContactEmail(rs.getString("contactEmail"));
        bank.setContactPhone(rs.getString("contactPhone"));
        bank.setAddress(rs.getString("address"));
        bank.setCity(rs.getString("city"));
        bank.setCountry(rs.getString("country"));
        bank.setPostalCode(rs.getString("postalCode"));
        Date date = rs.getDate("lastInventoryDate");
        bank.setLastInventoryDate(date != null ? date.toLocalDate() : null);
        return bank;
    }

    // Helper method to set PreparedStatement parameters
    private void setBloodBankParameters(PreparedStatement stmt, BloodBank bank, boolean isUpdate) throws SQLException {
        stmt.setString(1, bank.getName());
        stmt.setString(2, bank.getContactEmail());
        stmt.setString(3, bank.getContactPhone());
        stmt.setString(4, bank.getAddress());
        stmt.setString(5, bank.getCity());
        stmt.setString(6, bank.getCountry());
        stmt.setString(7, bank.getPostalCode());
        if (bank.getLastInventoryDate() != null) {
            stmt.setDate(8, Date.valueOf(bank.getLastInventoryDate()));
        } else {
            stmt.setNull(8, Types.DATE);
        }
    }
}
