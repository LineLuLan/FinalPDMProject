package backend.main.service;

import backend.main.model.DonationHistory;
import org.springframework.beans.factory.annotation.Autowired;
import backend.main.repository.DonationHistoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DonationHistoryService {
    private final DonationHistoryRepository donationHistoryRepository;

    public DonationHistoryService(DonationHistoryRepository donationHistoryRepository) {
        this.donationHistoryRepository = donationHistoryRepository;
    }

    public Optional<DonationHistory> getDonationHistoryById(Integer id) {
        return donationHistoryRepository.findById(id);
    }

    public List<DonationHistory> getAllDonationHistory() {
        return donationHistoryRepository.findAll();
    }

    @Autowired
    private backend.main.repository.DonorRepository donorRepository;
    @Autowired
    private backend.main.repository.BloodStockRepository bloodStockRepository;

    public int addDonationHistory(DonationHistory donation) {
        int result = donationHistoryRepository.save(donation);
        if (result > 0) {
            // Lấy bloodType của donor
            String donorSsn = donation.getDonorSsn();
            backend.main.model.Donor donor = null;
            if (donorRepository instanceof backend.main.repository.DonorRepositoryImpl) {
                // DonorRepositoryImpl chỉ có findById theo Integer, nên cần thêm hàm findBySsn nếu chưa có
                // Tạm thời giả sử có hàm findBySsn
                java.util.Optional<backend.main.model.Donor> donorOpt = ((backend.main.repository.DonorRepositoryImpl) donorRepository).findBySsn(donorSsn);
                if (donorOpt.isPresent()) {
                    donor = donorOpt.get();
                }
            }
            if (donor != null) {
                String bloodType = donor.getBloodType();
                Integer bid = donation.getBid();
                Integer quantity = donation.getQuantity();
                System.out.println("[DEBUG] addDonationHistory: donor_ssn=" + donor.getDonorSsn() + ", bloodType=" + bloodType + ", bid=" + bid + ", quantity=" + quantity);
                java.util.List<backend.main.model.BloodStock> stocks = bloodStockRepository.findByBloodTypeAndBid(bloodType, bid);
                System.out.println("[DEBUG] Found " + stocks.size() + " BloodStock(s) for bloodType=" + bloodType + " and bid=" + bid);
                for (backend.main.model.BloodStock s : stocks) {
                    System.out.println("[DEBUG] Stock: stockId=" + s.getStockId() + ", bloodType=" + s.getBloodType() + ", bid=" + s.getBid() + ", quantity=" + s.getQuantity());
                }
                if (!stocks.isEmpty()) {
                    backend.main.model.BloodStock stock = stocks.get(0);
                    System.out.println("[DEBUG] Incrementing stockId=" + stock.getStockId() + " by quantity=" + quantity);
                    bloodStockRepository.incrementQuantityByStockId(stock.getStockId(), quantity);
                } else {
                    System.err.println("[WARNING] No BloodStock found for bloodType=" + bloodType + " and bid=" + bid);
                }
            }
        }
        return result;
    }

    public int updateDonationHistory(DonationHistory donation) {
        return donationHistoryRepository.update(donation);
    }

    public int deleteDonationHistory(Integer id) {
        return donationHistoryRepository.deleteById(id);
    }

    public List<DonationHistory> getDonationHistoryByDonorSsn(String donorSsn) {
        return donationHistoryRepository.findByDonorSsn(donorSsn);
    }

    public List<DonationHistory> getDonationHistoryByBid(Integer bid) {
        return donationHistoryRepository.findByBid(bid);
    }
}
