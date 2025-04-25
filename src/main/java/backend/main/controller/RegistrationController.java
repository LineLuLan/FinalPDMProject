package backend.main.controller;

import backend.main.model.Patient;
import backend.main.model.Users;
import backend.main.model.Doctor;
import backend.main.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/registration")
public class RegistrationController {
    private final RegistrationService registrationService;

    @Autowired
    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping("/registerPatientUser")
    public ResponseEntity<?> registerPatientUser(@RequestBody RegistrationRequest request) {
        try {
            Patient savedPatient = registrationService.registerPatientAndUser(request.getUser(), request.getPatient());
            return ResponseEntity.status(HttpStatus.CREATED).body(savedPatient);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    // Đăng ký động: User + Patient hoặc User + Doctor
    @PostMapping("/registerUser")
    public ResponseEntity<?> registerUser(@RequestBody RegistrationRequest request) {
        try {
            if (request.getUser() == null) {
                return ResponseEntity.badRequest().body("Missing user info");
            }
            String role = request.getUser().getRole();
            if ("PATIENT".equalsIgnoreCase(role)) {
                Patient savedPatient = registrationService.registerPatientAndUser(request.getUser(), request.getPatient());
                return ResponseEntity.status(HttpStatus.CREATED).body(savedPatient);
            } else if ("DOCTOR".equalsIgnoreCase(role)) {
                Doctor savedDoctor = registrationService.registerDoctorAndUser(request.getUser(), request.getDoctor());
                return ResponseEntity.status(HttpStatus.CREATED).body(savedDoctor);
            } else {
                return ResponseEntity.badRequest().body("Invalid role");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    // DTO class for request body
    public static class RegistrationRequest {
        private Users user;
        private Patient patient;
        private Doctor doctor;

        public Users getUser() { return user; }
        public void setUser(Users user) { this.user = user; }
        public Patient getPatient() { return patient; }
        public void setPatient(Patient patient) { this.patient = patient; }
        public Doctor getDoctor() { return doctor; }
        public void setDoctor(Doctor doctor) { this.doctor = doctor; }
    }
}
