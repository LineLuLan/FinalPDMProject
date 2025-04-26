package backend.main.repository;

import java.util.List;
import java.util.Optional;

import backend.main.model.BloodStock;

public interface BloodStockRepository {
    Optional<BloodStock> findById(Integer stockId);
    List<BloodStock> findAll();
    int save(BloodStock bloodStock);
    int update(BloodStock bloodStock);
    int deleteById(Integer stockId);
    List<BloodStock> findByBloodType(String bloodType);
    List<BloodStock> findByBloodTypeAndBid(String bloodType, Integer bid);
    List<BloodStock> findLowStock(Integer threshold);
    int incrementQuantityByBid(Integer bid, Integer quantity);
    int decrementQuantityByBid(Integer bid, Integer quantity);
}
