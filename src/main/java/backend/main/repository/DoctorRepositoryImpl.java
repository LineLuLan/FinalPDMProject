package backend.main.repository;

import backend.main.config.DatabaseConfig;
import backend.main.model.Doctor;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class DoctorRepositoryImpl implements DoctorRepository {
    


    @Override
    public Optional<Doctor> findById(Integer userId) {
        String sql = "SELECT * FROM Doctor WHERE user_id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapRowToDoctor(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Doctor> findAll() {
        List<Doctor> list = new ArrayList<>();
        String sql = "SELECT * FROM Doctor";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(mapRowToDoctor(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public int save(Doctor doctor) {
        String sql = "INSERT INTO Doctor (dssn, user_id, dname, specialization, email, blood_bank_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            setDoctorParameters(stmt, doctor);
            return stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int update(Doctor doctor) {
        String sql = "UPDATE Doctor SET dname = ?, specialization = ?, email = ?, blood_bank_id = ? WHERE dssn = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, doctor.getDname());
            stmt.setString(2, doctor.getSpecialization());
            stmt.setString(3, doctor.getEmail());
            stmt.setInt(4, doctor.getBloodBankId());
            stmt.setString(5, doctor.getDssn());
            return stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int deleteById(Integer userId) {
        String sql = "DELETE FROM Doctor WHERE user_id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            return stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public List<Doctor> findBySpecialization(String specialization) {
        List<Doctor> list = new ArrayList<>();
        String sql = "SELECT * FROM Doctor WHERE specialization = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, specialization);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(mapRowToDoctor(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public Optional<Doctor> findByLicenseNumber(String licenseNumber) {
        String sql = "SELECT * FROM Doctor WHERE dssn = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, licenseNumber);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapRowToDoctor(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<Doctor> findByEmail(String email) {
        String sql = "SELECT * FROM Doctor WHERE email = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapRowToDoctor(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    private Doctor mapRowToDoctor(ResultSet rs) throws SQLException {
        Doctor doctor = new Doctor();
        doctor.setDssn(rs.getString("dssn"));
        doctor.setUserId(rs.getInt("user_id"));
        doctor.setDname(rs.getString("dname"));
        doctor.setSpecialization(rs.getString("specialization"));
        doctor.setEmail(rs.getString("email"));
        doctor.setBloodBankId(rs.getInt("blood_bank_id"));
        return doctor;
    }

    private void setDoctorParameters(PreparedStatement stmt, Doctor doctor) throws SQLException {
        stmt.setString(1, doctor.getDssn());
        stmt.setInt(2, doctor.getUserId());
        stmt.setString(3, doctor.getDname());
        stmt.setString(4, doctor.getSpecialization());
        stmt.setString(5, doctor.getEmail());
        stmt.setInt(6, doctor.getBloodBankId());
    }
}
