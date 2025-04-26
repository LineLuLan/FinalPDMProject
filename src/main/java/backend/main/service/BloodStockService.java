package backend.main.service;

import backend.main.model.BloodStock;
import backend.main.repository.BloodStockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BloodStockService {
    private final BloodStockRepository bloodStockRepository;

    @Autowired
    public BloodStockService(BloodStockRepository bloodStockRepository) {
        this.bloodStockRepository = bloodStockRepository;
    }

    public Optional<BloodStock> getBloodStockById(Integer id) {
        return bloodStockRepository.findById(id);
    }

    public List<BloodStock> getAllBloodStocks() {
        return bloodStockRepository.findAll();
    }

    public int addBloodStock(BloodStock bloodStock) {
        return bloodStockRepository.save(bloodStock);
    }

    public int updateBloodStock(BloodStock bloodStock) {
        return bloodStockRepository.update(bloodStock);
    }

    public int deleteBloodStock(Integer id) {
        return bloodStockRepository.deleteById(id);
    }

    public List<BloodStock> findByBloodType(String bloodType) {
        return bloodStockRepository.findByBloodType(bloodType);
    }

    public List<BloodStock> findByBloodTypeAndBid(String bloodType, Integer bid) {
        return bloodStockRepository.findByBloodTypeAndBid(bloodType, bid);
    }

    public int decrementQuantityByBid(Integer bid, Integer quantity) {
        return bloodStockRepository.decrementQuantityByBid(bid, quantity);
    }

    public List<BloodStock> findLowStock(Integer threshold) {
        return bloodStockRepository.findLowStock(threshold);
    }
}
