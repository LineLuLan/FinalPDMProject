package backend.main.service;

import backend.main.model.Patient;
import backend.main.model.Users;
import backend.main.model.Doctor;
import backend.main.repository.PatientRepository;
import backend.main.repository.UsersRepository;
import backend.main.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegistrationService {
    private final UsersRepository usersRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    @Autowired
    public RegistrationService(UsersRepository usersRepository, PatientRepository patientRepository, DoctorRepository doctorRepository) {
        this.usersRepository = usersRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
    }

    @Transactional
    public Patient registerPatientAndUser(Users user, Patient patient) {
        // Save user first
        if (usersRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email đã tồn tại!");
        }
        if (user.getIsActive() == null) user.setIsActive(true);
        if (user.getCreatedAt() == null) user.setCreatedAt(java.time.LocalDateTime.now());
        Users savedUser = usersRepository.save(user);

        // Gán userId vừa tạo cho patient trước khi lưu
        patient.setUserId(savedUser.getUserId());
        try {
            // Save patient
            int result = patientRepository.save(patient);
            if (result > 0 && patient.getPssn() != null) {
                return patientRepository.findById(patient.getPssn()).orElseThrow(() -> new RuntimeException("Patient not found after save!"));
            } else {
                throw new RuntimeException("Failed to save patient or PID not assigned.");
            }
        } catch (Exception e) {
            // Rollback user if patient fails
            usersRepository.deleteById(savedUser.getUserId());
            throw e;
        }
    }

    @Transactional
    public Doctor registerDoctorAndUser(Users user, Doctor doctor) {
        // Save user first
        if (usersRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email đã tồn tại!");
        }
        // Kiểm tra dssn duy nhất nếu có
        if (doctor.getDssn() != null && doctorRepository.findByLicenseNumber(doctor.getDssn()).isPresent()) {
            throw new RuntimeException("DSSN đã tồn tại!");
        }
        if (user.getIsActive() == null) user.setIsActive(true);
        if (user.getCreatedAt() == null) user.setCreatedAt(java.time.LocalDateTime.now());
        Users savedUser = usersRepository.save(user);

        try {
            doctor.setUserId(savedUser.getUserId());
            int result = doctorRepository.save(doctor);
            if (result > 0 && doctor.getDssn() != null) {
                return doctorRepository.findByLicenseNumber(doctor.getDssn()).orElseThrow(() -> new RuntimeException("Doctor not found after save!"));
            } else {
                throw new RuntimeException("Failed to save doctor or DSSN not assigned.");
            }
        } catch (Exception e) {
            // Rollback user if doctor fails
            usersRepository.deleteById(savedUser.getUserId());
            throw e;
        }
    }
}
