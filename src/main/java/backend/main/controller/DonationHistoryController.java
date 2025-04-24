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

    @PostMapping
    public ResponseEntity<Integer> addDonationHistory(@RequestBody DonationHistory donation) {
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
