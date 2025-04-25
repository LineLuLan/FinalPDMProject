package backend.main.controller;

import backend.main.model.Users;
import backend.main.service.UsersService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/users")
public class UsersController {
    private final UsersService usersService;
    private final backend.main.repository.DoctorRepository doctorRepository;
    private final backend.main.service.PatientService patientService;

    public UsersController(UsersService usersService, backend.main.repository.DoctorRepository doctorRepository, backend.main.service.PatientService patientService) {
        this.usersService = usersService;
        this.doctorRepository = doctorRepository;
        this.patientService = patientService;
    }

    @GetMapping
    public ResponseEntity<List<Users>> getAllUsers() {
        return ResponseEntity.ok(usersService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Users> getUserById(@PathVariable Integer id) {
        return usersService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Users> getUserByEmail(@PathVariable String email) {
        return usersService.getUserByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody Users user) {
        Object result = usersService.addUser(user);
        if (result instanceof String) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(result);
        }
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Users loginRequest) {
        // Tìm user theo email
        var userOpt = usersService.getUserByEmail(loginRequest.getEmail());
        if (userOpt.isPresent()) {
            var user = userOpt.get();
            // So sánh password (plain text, thực tế nên mã hóa)
            if (user.getPassword().equals(loginRequest.getPassword())) {
                // Nếu là DOCTOR thì trả về thêm dssn
                if ("DOCTOR".equalsIgnoreCase(user.getRole())) {
                    // Luôn tìm doctor theo userId để lấy dssn (liên kết user <-> doctor)
                    java.util.Optional<backend.main.model.Doctor> doctorOpt = doctorRepository.findById(user.getUserId());
                    if (doctorOpt.isPresent()) {
                        String dssn = doctorOpt.get().getDssn();
                        java.util.Map<String, Object> result = new java.util.HashMap<>();
                        result.put("userId", user.getUserId());
                        result.put("email", user.getEmail());
                        result.put("role", user.getRole());
                        result.put("fullName", doctorOpt.get().getDname());
                        result.put("address", null); // Nếu Doctor không có trường address, để null hoặc bổ sung nếu có
                        result.put("phone", null); // Nếu Doctor không có trường phone, để null hoặc bổ sung nếu có
                        result.put("dssn", dssn); // Trả về dssn cho doctor
                        return ResponseEntity.ok(result);
                    } else {
                        // Không tìm thấy doctor liên kết, trả về user như cũ
                        return ResponseEntity.ok(user);
                    }
                } else if ("PATIENT".equalsIgnoreCase(user.getRole())) {
                    // Lấy Patient theo userId để lấy pssn
                    backend.main.model.Patient patientByUser = patientService.getPatientByUserId(user.getUserId());
                    String pssn = patientByUser != null ? patientByUser.getPssn() : null;
                    backend.main.model.Patient patient = null;
                    if (pssn != null) {
                        patient = patientService.getPatientById(pssn);
                    }
                    java.util.Map<String, Object> result = new java.util.HashMap<>();
                    result.put("userId", user.getUserId());
                    result.put("email", user.getEmail());
                    result.put("role", user.getRole());
                    result.put("isActive", user.getIsActive());
                    result.put("createdAt", user.getCreatedAt());
                    result.put("pssn", pssn);
                    // Thêm các trường khác nếu cần
                    return ResponseEntity.ok(result);
                } else {
                    return ResponseEntity.ok(user);
                }
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Sai mật khẩu");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy user");
        }
    }

    @PostMapping
    public ResponseEntity<?> addUser(@RequestBody Users user) {
        Object result = usersService.addUser(user);
        if (result instanceof String) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(result);
        }
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Integer> updateUser(@PathVariable Integer id, @RequestBody Users user) {
        user.setUserId(id);
        int result = usersService.updateUser(user);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        usersService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
