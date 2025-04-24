package backend.main.service;

import backend.main.model.BloodBank;
import backend.main.repository.BloodBankRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BloodBankService {
    private final BloodBankRepository bloodBankRepository;

    @Autowired
    public BloodBankService(BloodBankRepository bloodBankRepository) {
        this.bloodBankRepository = bloodBankRepository;
    }

    public BloodBank getBloodBankById(Integer id) {
        return bloodBankRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("BloodBank not found with id: " + id));
    }

    public List<BloodBank> getAllBloodBanks() {
        return bloodBankRepository.findAll();
    }

    public int addBloodBank(BloodBank bloodBank) {
        return bloodBankRepository.save(bloodBank);
    }

    public int updateBloodBank(BloodBank bloodBank) {
        return bloodBankRepository.update(bloodBank);
    }

    public int deleteBloodBank(Integer id) {
        return bloodBankRepository.deleteById(id);
    }

    public List<BloodBank> findByLocation(String location) {
        return bloodBankRepository.findByLocation(location);
    }
}
