package backend.main.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import backend.main.model.BloodRequest;
import backend.main.service.BloodRequestService;

@RestController
@RequestMapping("/api/blood-requests")
public class _DoctorDashboardApiAdditions2 {
    @Autowired
    private BloodRequestService bloodRequestService;

    // API: Accept blood request
    @PutMapping("/{requestId}/accept")
    public ResponseEntity<?> acceptBloodRequest(@PathVariable Integer requestId) {
        BloodRequest req = bloodRequestService.getBloodRequestById(requestId).orElse(null);
        if (req == null) return ResponseEntity.notFound().build();
        req.setStatus("APPROVED"); // Đúng với logic cập nhật responseDate
        bloodRequestService.updateBloodRequest(req, req.getDssn());
        return ResponseEntity.ok().build();
    }

    // API: Reject blood request
    @PutMapping("/{requestId}/reject")
    public ResponseEntity<?> rejectBloodRequest(@PathVariable Integer requestId) {
        BloodRequest req = bloodRequestService.getBloodRequestById(requestId).orElse(null);
        if (req == null) return ResponseEntity.notFound().build();
        req.setStatus("REJECTED");
        bloodRequestService.updateBloodRequest(req, req.getDssn());
        return ResponseEntity.ok().build();
    }
}
