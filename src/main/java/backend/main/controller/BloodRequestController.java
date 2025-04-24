package backend.main.controller;

import backend.main.model.BloodRequest;
import backend.main.service.BloodRequestService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bloodRequests")
public class BloodRequestController {
    private final BloodRequestService bloodRequestService;

    public BloodRequestController(BloodRequestService bloodRequestService) {
        this.bloodRequestService = bloodRequestService;
    }

    @GetMapping
    public ResponseEntity<List<BloodRequest>> getAllBloodRequests() {
        return ResponseEntity.ok(bloodRequestService.getAllBloodRequests());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BloodRequest> getBloodRequestById(@PathVariable Integer id) {
        return bloodRequestService.getBloodRequestById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<BloodRequest>> getBloodRequestsByStatus(@PathVariable String status) {
        return ResponseEntity.ok(bloodRequestService.getBloodRequestsByStatus(status));
    }

    @GetMapping("/pssn/{pssn}")
    public ResponseEntity<List<BloodRequest>> getBloodRequestsByPssn(@PathVariable String pssn) {
        return ResponseEntity.ok(bloodRequestService.getBloodRequestsByPssn(pssn));
    }

    @PostMapping
    public ResponseEntity<Integer> addBloodRequest(@RequestBody BloodRequest request) {
        int result = bloodRequestService.addBloodRequest(request);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PutMapping("/{id}/doctor/{doctorDssn}")
    public ResponseEntity<Integer> updateBloodRequest(@PathVariable Integer id, @RequestBody BloodRequest request, @PathVariable String doctorDssn) {
        request.setRequestId(id);
        int result = bloodRequestService.updateBloodRequest(request, doctorDssn);
        if (result == -1) {
            return ResponseEntity.status(403).body(-1); // Không đúng bác sĩ phụ trách
        } else if (result == -2) {
            return ResponseEntity.status(404).body(-2); // Không tìm thấy bệnh nhân
        }
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBloodRequest(@PathVariable Integer id) {
        bloodRequestService.deleteBloodRequest(id);
        return ResponseEntity.noContent().build();
    }
}
