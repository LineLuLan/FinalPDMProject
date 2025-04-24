package backend.main.service;

import backend.main.model.BloodRequest;
import backend.main.repository.BloodRequestRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BloodRequestService {
    private final BloodRequestRepository bloodRequestRepository;

    public BloodRequestService(BloodRequestRepository bloodRequestRepository) {
        this.bloodRequestRepository = bloodRequestRepository;
    }

    public Optional<BloodRequest> getBloodRequestById(Integer id) {
        return bloodRequestRepository.findById(id);
    }

    public List<BloodRequest> getAllBloodRequests() {
        return bloodRequestRepository.findAll();
    }

    public int addBloodRequest(BloodRequest request) {
        // Nếu status null hoặc là 'pending', tự động set requestDate là thời gian thực
        if (request.getStatus() == null || request.getStatus().equalsIgnoreCase("pending")) {
            request.setRequestDate(java.time.LocalDate.now());
        }
        // Nếu dssn null, tự động lấy assignedDoctorId từ Patient
        if (request.getDssn() == null || request.getDssn().isEmpty()) {
            backend.main.repository.PatientRepository patientRepository = new backend.main.repository.PatientRepositoryImpl();
            java.util.Optional<backend.main.model.Patient> patientOpt = patientRepository.findById(request.getPssn());
            if (patientOpt.isPresent()) {
                request.setDssn(patientOpt.get().getAssignedDoctorId());
            }
        }
        return bloodRequestRepository.save(request);
    }

    public int updateBloodRequest(BloodRequest request, String doctorDssn) {
        // Lấy lại bản ghi gốc từ DB
        java.util.Optional<backend.main.model.BloodRequest> existingOpt = bloodRequestRepository.findById(request.getRequestId());
        if (existingOpt.isEmpty()) return -2;
        backend.main.model.BloodRequest existing = existingOpt.get();

        // Kiểm tra quyền bác sĩ
        backend.main.repository.PatientRepository patientRepository = new backend.main.repository.PatientRepositoryImpl();
        java.util.Optional<backend.main.model.Patient> patientOpt = patientRepository.findById(existing.getPssn());
        if (patientOpt.isPresent()) {
            String assignedDoctorId = patientOpt.get().getAssignedDoctorId();
            if (!doctorDssn.equals(assignedDoctorId)) {
                return -1; // Không đúng bác sĩ phụ trách
            }
        } else {
            return -2; // Không tìm thấy bệnh nhân
        }

        // Chỉ update status và responseDate, giữ nguyên các trường khác
        existing.setStatus(request.getStatus());
        if (request.getStatus() != null &&
            (request.getStatus().equalsIgnoreCase("approved") || request.getStatus().equalsIgnoreCase("rejected"))) {
            existing.setResponseDate(java.time.LocalDate.now());
        }
        return bloodRequestRepository.update(existing);
    }

    public int deleteBloodRequest(Integer id) {
        return bloodRequestRepository.deleteById(id);
    }

    public List<BloodRequest> getBloodRequestsByStatus(String status) {
        return bloodRequestRepository.findByStatus(status);
    }

    public List<BloodRequest> getBloodRequestsByPssn(String pssn) {
        return bloodRequestRepository.findByPssn(pssn);
    }
}
