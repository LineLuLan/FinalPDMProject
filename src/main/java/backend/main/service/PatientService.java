package backend.main.service;

import backend.main.model.Patient;
import backend.main.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientService {
    private final PatientRepository patientRepository;

    public Patient getPatientByUserId(Integer userId) {
        return patientRepository.findByUserId(userId)
            .orElseThrow(() -> new RuntimeException("Patient not found with userId: " + userId));
    }

    @Autowired
    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public Patient getPatientById(String pssn) {
        return patientRepository.findById(pssn)
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + pssn));
    }

    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    public Patient addPatient(Patient patient) {
        // Kiểm tra trùng hoàn toàn: tất cả các trường identity
        List<Patient> candidates = patientRepository.findByIdentity(patient.getName(), patient.getBloodType(), patient.getAge(), patient.getGender());
        if (!candidates.isEmpty()) {
            throw new RuntimeException("Bệnh nhân đã tồn tại với thông tin này!");
        }
        int result = patientRepository.save(patient);
        if (result > 0 && patient.getPssn() != null) {
            return getPatientById(patient.getPssn());
        } else {
            throw new RuntimeException("Failed to save patient or PID not assigned.");
        }
    }

    public Patient updatePatient(String pssn, Patient patient) {
        Patient current = getPatientById(pssn);
        // Merge fields: if incoming field is null, keep old value
        if (patient.getUserId() == null) patient.setUserId(current.getUserId());
        if (patient.getName() == null) patient.setName(current.getName());
        if (patient.getBloodType() == null) patient.setBloodType(current.getBloodType());
        if (patient.getAge() == null) patient.setAge(current.getAge());
        if (patient.getGender() == null) patient.setGender(current.getGender());
        if (patient.getPhone() == null) patient.setPhone(current.getPhone());
        if (patient.getEmail() == null) patient.setEmail(current.getEmail());
        if (patient.getAssignedDoctorId() == null) patient.setAssignedDoctorId(current.getAssignedDoctorId());
        patient.setPssn(pssn);
        int result = patientRepository.update(patient);
        if (result > 0) {
            return getPatientById(pssn);
        } else {
            throw new RuntimeException("Failed to update patient with id: " + pssn);
        }
    }

    public void deletePatient(String pssn) {
        int result = patientRepository.deleteById(pssn);
        if (result == 0) {
            throw new RuntimeException("Failed to delete patient with id: " + pssn);
        }
    }
}
