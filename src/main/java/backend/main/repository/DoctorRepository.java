package backend.main.repository;

import java.util.List;
import java.util.Optional;

import backend.main.model.Doctor;

public interface DoctorRepository {
    Optional<Doctor> findById(Integer doctorId);
    List<Doctor> findAll();
    int save(Doctor doctor);
    int update(Doctor doctor);
    int deleteById(Integer doctorId);
    List<Doctor> findBySpecialization(String specialization);
    Optional<Doctor> findByLicenseNumber(String licenseNumber);
    Optional<Doctor> findByEmail(String email);
}
