package backend.main.repository;

import java.util.List;
import java.util.Optional;

import backend.main.model.Donor;

public interface DonorRepository {
    // Tránh trùng lập donor
    List<Donor> findByIdentity(String name, String bloodType, Integer age);
    Optional<Donor> findById(Integer donorId);
    List<Donor> findAll();
    int save(Donor donor);
    int update(Donor donor);
    int deleteById(Integer donorId);
    List<Donor> findByBloodType(String bloodType);
    List<Donor> findEligibleDonors();
}