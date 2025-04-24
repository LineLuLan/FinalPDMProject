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

    public Donor getDonorById(Integer donorId) {
        return donorRepository.findById(donorId)
                .orElseThrow(() -> new ResourceNotFoundException("Donor not found with id: " + donorId));
    }

    public List<Donor> getAllDonors() {
        return donorRepository.findAll();
    }

    @Transactional
    public Donor addDonor(Donor donor) {
        System.out.println("[DEBUG] addDonor called with: " + donor);
        // Tự động set registrationDate nếu chưa có
        if (donor.getRegistrationDate() == null) {
            donor.setRegistrationDate(java.time.LocalDateTime.now());
        }
        // Kiểm tra trùng lặp donor theo name, bloodType, age, gender
        var existing = donorRepository.findByIdentity(donor.getName(), donor.getBloodType(), donor.getAge());
        if (!existing.isEmpty()) {
            throw new RuntimeException("Donor already exists with same name, blood type, age, and gender!");
        }
        int result = donorRepository.save(donor);
        if (result > 0 && donor.getDonorSsn() != null) {
            return donor;
        } else {
            throw new RuntimeException("[ERROR] donorId is still null after save! Possible DB/config error.");
        }
    }

    public Donor updateDonor(Integer donorId, Donor donorDetails) {
        Donor donor = getDonorById(donorId);
        donor.setName(donorDetails.getName());
        // Cập nhật các trường khác tương tự
        donorRepository.update(donor);
        return donor;
    }

    public void deleteDonor(Integer donorId) {
        Donor donor = getDonorById(donorId);
        donorRepository.deleteById(donorId);
    }
}