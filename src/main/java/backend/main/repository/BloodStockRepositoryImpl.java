package backend.main.repository;

import backend.main.config.DatabaseConfig;
import backend.main.model.BloodStock;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

@Repository
public class BloodStockRepositoryImpl implements BloodStockRepository {
    @Override
    public int incrementQuantityByStockId(Integer stockId, Integer quantity) {
        String sql = "UPDATE BloodStock SET quantity = quantity + ? WHERE stockId = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, quantity);
            stmt.setInt(2, stockId);
            return stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int decrementQuantityByStockId(Integer stockId, Integer quantity) {
        String sql = "UPDATE BloodStock SET quantity = quantity - ? WHERE stockId = ? AND quantity >= ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, quantity);
            stmt.setInt(2, stockId);
            stmt.setInt(3, quantity);
            return stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Interface method: tăng số lượng máu theo bid (thực tế sẽ tìm stockId phù hợp rồi gọi hàm trên)
     */
    @Override
    public int incrementQuantityByBid(Integer bid, Integer quantity) {
        // Tìm stockId phù hợp
        Optional<BloodStock> stock = findById(bid);
        if (stock.isPresent()) {
            return incrementQuantityByStockId(stock.get().getStockId(), quantity);
        } else {
            System.err.println("[WARNING] Không tìm thấy stockId phù hợp cho bid=" + bid);
            return 0;
        }
    }

    /**
     * Interface method: giảm số lượng máu theo bid (thực tế sẽ tìm stockId phù hợp rồi gọi hàm trên)
     */
    @Override
    public int decrementQuantityByBid(Integer bid, Integer quantity) {
        // Tìm stockId phù hợp
        Optional<BloodStock> stock = findById(bid);
        if (stock.isPresent()) {
            return decrementQuantityByStockId(stock.get().getStockId(), quantity);
        } else {
            System.err.println("[WARNING] Không tìm thấy stockId phù hợp cho bid=" + bid);
            return 0;
        }
    }

    public Optional<BloodStock> findById(Integer id) {
        String sql = "SELECT * FROM BloodStock WHERE stockId = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapRowToBloodStock(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<BloodStock> findAll() {
        List<BloodStock> stocks = new ArrayList<>();
        String sql = "SELECT * FROM BloodStock";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                stocks.add(mapRowToBloodStock(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stocks;
    }

    @Override
    public int save(BloodStock stock) {
        // Check for duplicate by bid + storageLocation
        String checkSql = "SELECT COUNT(*) FROM BloodStock WHERE bid = ? AND storageLocation = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
            checkStmt.setInt(1, stock.getBid());
            checkStmt.setString(2, stock.getStorageLocation());
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                // Duplicate found
                System.err.println("BloodStock already exists with same bid and storageLocation.");
                return -1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
        String sql = "INSERT INTO BloodStock (bloodType, quantity, status, expirationDate, bid, storageLocation) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, stock.getBloodType());
            stmt.setInt(2, stock.getQuantity());
            stmt.setString(3, stock.getStatus());
            stmt.setDate(4, stock.getExpirationDate() != null ? Date.valueOf(stock.getExpirationDate()) : null);
            stmt.setInt(5, stock.getBid());
            stmt.setString(6, stock.getStorageLocation());
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        stock.setStockId(generatedKeys.getInt(1));
                    }
                }
            }
            return affectedRows;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int update(BloodStock stock) {
        String sql = "UPDATE BloodStock SET bloodType = ?, quantity = ?, status = ?, expirationDate = ?, storageLocation = ? WHERE stockId = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            setBloodStockParameters(stmt, stock, true);
            stmt.setInt(6, stock.getStockId());
            return stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int deleteById(Integer id) {
        String sql = "DELETE FROM BloodStock WHERE stockId = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public List<BloodStock> findByBloodType(String bloodType) {
        List<BloodStock> stocks = new ArrayList<>();
        String sql = "SELECT * FROM BloodStock WHERE bloodType = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, bloodType);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                stocks.add(mapRowToBloodStock(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stocks;
    }

    @Override
    public List<BloodStock> findByBloodTypeAndBid(String bloodType, Integer bid) {
        List<BloodStock> stocks = new ArrayList<>();
        String sql = "SELECT * FROM BloodStock WHERE bloodType = ? AND bid = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, bloodType);
            stmt.setInt(2, bid);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                stocks.add(mapRowToBloodStock(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stocks;
    }

    @Override
    public List<BloodStock> findLowStock(Integer threshold) {
        List<BloodStock> stocks = new ArrayList<>();
        String sql = "SELECT * FROM BloodStock WHERE quantity < ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, threshold);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                stocks.add(mapRowToBloodStock(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stocks;
    }

    private BloodStock mapRowToBloodStock(ResultSet rs) throws SQLException {
        BloodStock stock = new BloodStock();
        stock.setStockId(rs.getInt("stockId"));
        stock.setBloodType(rs.getString("bloodType"));
        stock.setQuantity(rs.getInt("quantity"));
        stock.setStatus(rs.getString("status"));
        Date date = rs.getDate("expirationDate");
        stock.setExpirationDate(date != null ? date.toLocalDate() : null);
        stock.setBid(rs.getInt("bid"));
        stock.setStorageLocation(rs.getString("storageLocation"));
        return stock;
    }

    // Chỉ dùng cho update, KHÔNG dùng cho insert mới
    private void setBloodStockParameters(PreparedStatement stmt, BloodStock stock, boolean isUpdate) throws SQLException {
        if (isUpdate) {
            stmt.setString(1, stock.getBloodType());
            stmt.setInt(2, stock.getQuantity());
            stmt.setString(3, stock.getStatus());
            stmt.setDate(4, stock.getExpirationDate() != null ? Date.valueOf(stock.getExpirationDate()) : null);
            stmt.setString(5, stock.getStorageLocation());
        }
    }
}
