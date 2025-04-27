package backend.main.controller;

import backend.main.model.DonationHistory;
import backend.main.service.DonationHistoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/donation-history")
public class DonationHistoryController {
    private final DonationHistoryService donationHistoryService;

    public DonationHistoryController(DonationHistoryService donationHistoryService) {
        this.donationHistoryService = donationHistoryService;
    }

    @GetMapping
    public ResponseEntity<List<DonationHistory>> getAllDonationHistory() {
        return ResponseEntity.ok(donationHistoryService.getAllDonationHistory());
    }

    @GetMapping("/full")
    public ResponseEntity<java.util.List<java.util.Map<String, Object>>> getAllDonationHistoryWithDonorInfo() {
        return ResponseEntity.ok(donationHistoryService.getAllDonationHistoryWithDonorInfo());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DonationHistory> getDonationHistoryById(@PathVariable Integer id) {
        return donationHistoryService.getDonationHistoryById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/donor/{donorSsn}")
    public ResponseEntity<List<DonationHistory>> getDonationHistoryByDonorSsn(@PathVariable String donorSsn) {
        return ResponseEntity.ok(donationHistoryService.getDonationHistoryByDonorSsn(donorSsn));
    }

    @GetMapping("/blood-bank/{bid}")
    public ResponseEntity<List<DonationHistory>> getDonationHistoryByBid(@PathVariable Integer bid) {
        return ResponseEntity.ok(donationHistoryService.getDonationHistoryByBid(bid));
    }

    // Endpoint mới: trả về lịch sử hiến máu đã join donor, lọc theo bid (dùng cho dashboard doctor)
    @GetMapping("/blood-bank/{bid}/with-donor")
    public ResponseEntity<List<java.util.Map<String, Object>>> getDonationHistoryWithDonorInfoByBid(@PathVariable Integer bid) {
        List<java.util.Map<String, Object>> all = donationHistoryService.getAllDonationHistoryWithDonorInfo();
        List<java.util.Map<String, Object>> filtered = all.stream()
            .filter(row -> row.get("bid") != null && ((Integer) row.get("bid")).equals(bid))
            .toList();
        return ResponseEntity.ok(filtered);
    }

    @PostMapping
    public ResponseEntity<Integer> addDonationHistory(@RequestBody DonationHistory donation) {
        System.out.println("[DEBUG] API /api/donation-history received: donorSsn=" + donation.getDonorSsn() + ", bid=" + donation.getBid() + ", quantity=" + donation.getQuantity());
        int result = donationHistoryService.addDonationHistory(donation);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Integer> updateDonationHistory(@PathVariable Integer id, @RequestBody DonationHistory donation) {
        donation.setDonationId(id);
        int result = donationHistoryService.updateDonationHistory(donation);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDonationHistory(@PathVariable Integer id) {
        donationHistoryService.deleteDonationHistory(id);
        return ResponseEntity.noContent().build();
    }
}
