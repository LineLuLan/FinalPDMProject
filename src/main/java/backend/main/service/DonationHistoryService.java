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
                java.util.List<backend.main.model.BloodStock> stocks = bloodStockRepository.findByBloodType(bloodType);
                for (backend.main.model.BloodStock stock : stocks) {
                    if (stock.getBid() != null && stock.getBid().equals(bid)) {
                        bloodStockRepository.incrementQuantityByBid(stock.getStockId(), donation.getQuantity());
                        break;
                    }
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
}
