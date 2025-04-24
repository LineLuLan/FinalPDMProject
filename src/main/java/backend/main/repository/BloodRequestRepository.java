package backend.main.repository;

import backend.main.model.BloodRequest;
import java.util.List;
import java.util.Optional;

public interface BloodRequestRepository {
    Optional<BloodRequest> findById(Integer requestId);
    List<BloodRequest> findAll();
    int save(BloodRequest request);
    int update(BloodRequest request);
    int deleteById(Integer requestId);
    List<BloodRequest> findByStatus(String status);
    List<BloodRequest> findByPssn(String pssn);
}
