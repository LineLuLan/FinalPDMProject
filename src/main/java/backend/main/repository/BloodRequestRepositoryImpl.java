package backend.main.repository;

import backend.main.config.DatabaseConfig;
import backend.main.model.BloodRequest;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class BloodRequestRepositoryImpl implements BloodRequestRepository {


    @Override
    public Optional<BloodRequest> findById(Integer requestId) {
        String sql = "SELECT * FROM BloodRequest WHERE request_id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, requestId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapRowToBloodRequest(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<BloodRequest> findAll() {
        List<BloodRequest> list = new ArrayList<>();
        String sql = "SELECT * FROM BloodRequest";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(mapRowToBloodRequest(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public int save(BloodRequest request) {
        String sql = "INSERT INTO BloodRequest (dssn, pssn, blood_type, quantity, status, request_date, response_date) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            setBloodRequestParameters(stmt, request);
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) return 0;
            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                request.setRequestId(generatedKeys.getInt(1));
            }
            return affectedRows;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int update(BloodRequest request) {
        String sql = "UPDATE BloodRequest SET dssn = ?, pssn = ?, blood_type = ?, quantity = ?, status = ?, request_date = ?, response_date = ? WHERE request_id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            setBloodRequestParameters(stmt, request);
            stmt.setInt(8, request.getRequestId());
            return stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int deleteById(Integer requestId) {
        String sql = "DELETE FROM BloodRequest WHERE request_id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, requestId);
            return stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public List<BloodRequest> findByStatus(String status) {
        List<BloodRequest> list = new ArrayList<>();
        String sql = "SELECT * FROM BloodRequest WHERE status = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(mapRowToBloodRequest(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<BloodRequest> findByPssn(String pssn) {
        List<BloodRequest> list = new ArrayList<>();
        String sql = "SELECT * FROM BloodRequest WHERE pssn = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, pssn);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(mapRowToBloodRequest(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private BloodRequest mapRowToBloodRequest(ResultSet rs) throws SQLException {
        BloodRequest br = new BloodRequest();
        br.setRequestId(rs.getInt("request_id"));
        br.setDssn(rs.getString("dssn"));
        br.setPssn(rs.getString("pssn"));
        br.setBloodType(rs.getString("blood_type"));
        br.setQuantity(rs.getInt("quantity"));
        br.setStatus(rs.getString("status"));
        Date reqDate = rs.getDate("request_date");
        Date respDate = rs.getDate("response_date");
        if (reqDate != null) br.setRequestDate(reqDate.toLocalDate());
        if (respDate != null) br.setResponseDate(respDate.toLocalDate());
        return br;
    }

    private void setBloodRequestParameters(PreparedStatement stmt, BloodRequest request) throws SQLException {
        stmt.setString(1, request.getDssn());
        stmt.setString(2, request.getPssn());
        stmt.setString(3, request.getBloodType());
        stmt.setInt(4, request.getQuantity());
        stmt.setString(5, request.getStatus());
        stmt.setDate(6, request.getRequestDate() != null ? Date.valueOf(request.getRequestDate()) : null);
        stmt.setDate(7, request.getResponseDate() != null ? Date.valueOf(request.getResponseDate()) : null);
    }
}
