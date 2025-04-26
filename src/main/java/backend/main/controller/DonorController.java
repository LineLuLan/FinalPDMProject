package backend.main.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import backend.main.model.Donor;
import backend.main.service.DonorService;

@RestController
@RequestMapping("/api/donors")
public class DonorController {

    private final DonorService donorService;

    @Autowired
    public DonorController(DonorService donorService) {
        this.donorService = donorService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Donor> getDonorBySsn(@PathVariable("ssn") String ssn) {
        Donor donor = donorService.getDonorBySsn(ssn);
        return ResponseEntity.ok(donor);
    }

    @GetMapping
    public ResponseEntity<List<Donor>> getAllDonors() {
        List<Donor> donors = donorService.getAllDonors();
        return ResponseEntity.ok(donors);
    }

    @PostMapping
    public ResponseEntity<Donor> addDonor(@RequestBody Donor donor) {
        if (donor.getPhone() == null || donor.getPhone().trim().isEmpty()) {
            throw new RuntimeException("Số điện thoại không được để trống!");
        }
        Donor savedDonor = donorService.addDonor(donor);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedDonor);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Donor> updateDonor(@PathVariable("ssn") String ssn, @RequestBody Donor donor) {
        Donor updatedDonor = donorService.updateDonor(ssn, donor);
        return ResponseEntity.ok(updatedDonor);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDonor(@PathVariable("ssn") String ssn) {
        donorService.deleteDonor(ssn);
        return ResponseEntity.noContent().build();
    }
}
