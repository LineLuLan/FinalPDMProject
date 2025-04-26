package backend.main.service;

import backend.main.model.BloodRequest;
import backend.main.repository.BloodRequestRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BloodRequestService {
    private final BloodRequestRepository bloodRequestRepository;
    private final backend.main.service.BloodStockService bloodStockService;

    public BloodRequestService(BloodRequestRepository bloodRequestRepository, backend.main.service.BloodStockService bloodStockService) {
        this.bloodRequestRepository = bloodRequestRepository;
        this.bloodStockService = bloodStockService;
    }

    public Optional<BloodRequest> getBloodRequestById(Integer id) {
        return bloodRequestRepository.findById(id);
    }

    public List<BloodRequest> getAllBloodRequests() {
        return bloodRequestRepository.findAll();
    }

    public int addBloodRequest(BloodRequest request) {
        // Nếu status null, tự động set PENDING
        if (request.getStatus() == null) {
            request.setStatus("PENDING");
        }
        // Nếu requestDate null hoặc là 'pending', tự động set requestDate là thời gian thực
        if (request.getRequestDate() == null || request.getStatus().equalsIgnoreCase("PENDING")) {
            request.setRequestDate(java.time.LocalDate.now());
        }
        // Nếu bloodType null, tự động lấy từ Patient
        if (request.getBloodType() == null) {
            backend.main.repository.PatientRepository patientRepository = new backend.main.repository.PatientRepositoryImpl();
            java.util.Optional<backend.main.model.Patient> patientOpt = patientRepository.findById(request.getPssn());
            if (patientOpt.isPresent()) {
                request.setBloodType(patientOpt.get().getBloodType());
            }
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
        // Nếu duyệt (approved) thì chỉ trừ kho máu của blood bank mà bác sĩ phụ trách, đúng nhóm máu
        if (request.getStatus() != null && request.getStatus().equalsIgnoreCase("approved")) {
            // Lấy blood bank id của doctor
            backend.main.repository.DoctorRepository doctorRepository = new backend.main.repository.DoctorRepositoryImpl();
            java.util.Optional<backend.main.model.Doctor> doctorOpt = doctorRepository.findByDssn(doctorDssn);
            if (doctorOpt.isPresent()) {
                Integer bid = doctorOpt.get().getBloodBankId();
                java.util.List<backend.main.model.BloodStock> stocks = bloodStockService.findByBloodTypeAndBid(existing.getBloodType(), bid);
                int quantityToDeduct = existing.getQuantity();
                boolean deducted = false;
                for (backend.main.model.BloodStock stock : stocks) {
                    if (stock.getQuantity() >= quantityToDeduct) {
                        bloodStockService.decrementQuantityByBid(stock.getStockId(), quantityToDeduct);
                        deducted = true;
                        break;
                    }
                }
                if (!deducted) {
                    // Không đủ máu trong kho, trả về lỗi đặc biệt
                    return -3;
                }
            } else {
                // Không tìm thấy thông tin doctor
                return -4;
            }
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
