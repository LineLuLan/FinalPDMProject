package backend.main.repository;

import java.util.List;
import java.util.Optional;

import backend.main.model.BloodBank;

public interface BloodBankRepository {
    Optional<BloodBank> findById(Integer bankId);
    List<BloodBank> findAll();
    int save(BloodBank bloodBank);
    int update(BloodBank bloodBank);
    int deleteById(Integer bankId);
    List<BloodBank> findByLocation(String location);
}
