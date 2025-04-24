package backend.main.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import backend.main.model.BloodStock;
import backend.main.service.BloodStockService;

@RestController
@RequestMapping("/api/bloodStocks")
public class BloodStockController {

    private final BloodStockService bloodStockService;

    @Autowired
    public BloodStockController(BloodStockService bloodStockService) {
        this.bloodStockService = bloodStockService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<BloodStock> getBloodStockById(@PathVariable Integer id) {
        return bloodStockService.getBloodStockById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<BloodStock>> getAllBloodStocks() {
        return ResponseEntity.ok(bloodStockService.getAllBloodStocks());
    }

    @PostMapping
    public ResponseEntity<Void> addBloodStock(@RequestBody BloodStock bloodStock) {
        bloodStockService.addBloodStock(bloodStock);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateBloodStock(@PathVariable Integer id, @RequestBody BloodStock bloodStock) {
        bloodStock.setStockId(id);
        bloodStockService.updateBloodStock(bloodStock);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBloodStock(@PathVariable Integer id) {
        bloodStockService.deleteBloodStock(id);
        return ResponseEntity.noContent().build();
    }
}
