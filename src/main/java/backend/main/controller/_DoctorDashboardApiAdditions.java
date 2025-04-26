package backend.main.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import backend.main.model.BloodStock;
import backend.main.model.BloodRequest;
import backend.main.model.Doctor;
import backend.main.service.BloodStockService;
import backend.main.service.BloodRequestService;
import backend.main.service.DoctorService;

@RestController
@RequestMapping("/api/doctors")
public class _DoctorDashboardApiAdditions {
    @Autowired
    private DoctorService doctorService;
    @Autowired
    private BloodStockService bloodStockService;
    @Autowired
    private BloodRequestService bloodRequestService;

    // API: Lấy danh sách BloodStock của blood bank mà doctor phụ trách
    @GetMapping("/{id}/blood-stocks")
    public ResponseEntity<List<BloodStock>> getBloodStocksByDoctor(@PathVariable Integer id) {
        Doctor doctor = doctorService.getDoctorById(id);
        if (doctor == null || doctor.getBloodBankId() == null) {
            return ResponseEntity.notFound().build();
        }
        List<BloodStock> stocks = bloodStockService.getAllBloodStocks().stream()
            .filter(stock -> stock.getBid() != null && stock.getBid().equals(doctor.getBloodBankId()))
            .toList();
        return ResponseEntity.ok(stocks);
    }

    // API: Lấy danh sách BloodRequest liên quan đến doctor (theo dssn)
    @GetMapping("/{id}/blood-requests")
    public ResponseEntity<List<BloodRequest>> getBloodRequestsByDoctor(@PathVariable Integer id) {
        Doctor doctor = doctorService.getDoctorById(id);
        if (doctor == null || doctor.getDssn() == null) {
            return ResponseEntity.notFound().build();
        }
        List<BloodRequest> requests = bloodRequestService.getAllBloodRequests().stream()
            .filter(req -> req.getDssn() != null && req.getDssn().equals(doctor.getDssn()))
            .toList();
        return ResponseEntity.ok(requests);
    }
}
