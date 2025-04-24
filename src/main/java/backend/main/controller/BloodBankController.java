package backend.main.controller;

import backend.main.model.BloodBank;
import backend.main.service.BloodBankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bloodBanks")
public class BloodBankController {

    private final BloodBankService bloodBankService;

    @Autowired
    public BloodBankController(BloodBankService bloodBankService) {
        this.bloodBankService = bloodBankService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<BloodBank> getBloodBankById(@PathVariable Integer id) {
        BloodBank bank = bloodBankService.getBloodBankById(id);
        return ResponseEntity.ok(bank);
    }

    @GetMapping
    public ResponseEntity<List<BloodBank>> getAllBloodBanks() {
        return ResponseEntity.ok(bloodBankService.getAllBloodBanks());
    }

    @PostMapping
    public ResponseEntity<Void> addBloodBank(@RequestBody BloodBank bloodBank) {
        bloodBankService.addBloodBank(bloodBank);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateBloodBank(@PathVariable Integer id, @RequestBody BloodBank bloodBank) {
        bloodBank.setBid(id);
        bloodBankService.updateBloodBank(bloodBank);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBloodBank(@PathVariable Integer id) {
        bloodBankService.deleteBloodBank(id);
        return ResponseEntity.noContent().build();
    }
}
