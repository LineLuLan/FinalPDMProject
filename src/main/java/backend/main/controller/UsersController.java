package backend.main.controller;

import backend.main.model.Users;
import backend.main.service.UsersService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UsersController {
    private final UsersService usersService;

    public UsersController(UsersService usersService) {
        this.usersService = usersService;
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
                return ResponseEntity.ok(user);
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
