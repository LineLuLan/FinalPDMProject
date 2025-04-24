package backend.main.repository;

import backend.main.model.DonationHistory;
import java.util.List;
import java.util.Optional;

public interface DonationHistoryRepository {
    Optional<DonationHistory> findById(Integer donationId);
    List<DonationHistory> findAll();
    int save(DonationHistory donation);
    int update(DonationHistory donation);
    int deleteById(Integer donationId);
    List<DonationHistory> findByDonorSsn(String donorSsn);
}
