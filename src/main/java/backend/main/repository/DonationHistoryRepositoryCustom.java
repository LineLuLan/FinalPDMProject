package backend.main.repository;

import java.util.List;
import java.util.Map;

public interface DonationHistoryRepositoryCustom {
    List<Map<String, Object>> findAllWithDonorInfo();
}
