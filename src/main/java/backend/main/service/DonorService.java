package backend.main.service;

import backend.main.exception.ResourceNotFoundException;
import backend.main.model.Donor;
import backend.main.repository.DonorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class DonorService {
    
    private final DonorRepository donorRepository;

    public DonorService(DonorRepository donorRepository) {
        this.donorRepository = donorRepository;
    }

    public Donor getDonorBySsn(String donorSsn) {
        return donorRepository.findById(donorSsn)
                .orElseThrow(() -> new ResourceNotFoundException("Donor not found with SSN: " + donorSsn));
    }

    public List<Donor> getAllDonors() {
        return donorRepository.findAll();
    }

    @Transactional
    public Donor addDonor(Donor donor) {
        System.out.println("[DEBUG] addDonor called with: " + donor);
        // Nếu đã có donor với donor_ssn này thì trả về luôn (không tạo mới)
        if (donor.getDonorSsn() != null) {
            var existingBySsn = donorRepository.findById(donor.getDonorSsn());
            if (existingBySsn.isPresent()) {
                return existingBySsn.get();
            }
        }
        // Tự động set registrationDate nếu chưa có
        if (donor.getRegistrationDate() == null) {
            donor.setRegistrationDate(java.time.LocalDateTime.now());
        }
        int result = donorRepository.save(donor);
        if (result > 0 && donor.getDonorSsn() != null) {
            return donor;
        } else {
            throw new RuntimeException("[ERROR] donor_ssn is still null after save! Possible DB/config error.");
        }
    }

    public Donor updateDonor(String donorSsn, Donor donorDetails) {
        Donor donor = getDonorBySsn(donorSsn);
        donor.setName(donorDetails.getName());
        // Cập nhật các trường khác tương tự
        donorRepository.update(donor);
        return donor;
    }

    public void deleteDonor(String donorSsn) {
        Donor donor = getDonorBySsn(donorSsn);
        donorRepository.deleteById(donorSsn);
    }
}