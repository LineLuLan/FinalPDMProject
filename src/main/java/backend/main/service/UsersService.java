package backend.main.service;

import backend.main.model.Users;
import backend.main.repository.UsersRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsersService {
    private final UsersRepository usersRepository;

    public UsersService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public Optional<Users> getUserById(Integer id) {
        return usersRepository.findById(id);
    }

    public Optional<Users> getUserByEmail(String email) {
        return usersRepository.findByEmail(email);
    }

    public List<Users> getAllUsers() {
        return usersRepository.findAll();
    }

    public Object addUser(Users user) {
        // Kiểm tra email đã tồn tại
        if (usersRepository.existsByEmail(user.getEmail())) {
            return "Email had existed!";
        }
        // Đảm bảo isActive luôn true nếu null
        if (user.getIsActive() == null) {
            user.setIsActive(true);
        }
        // Đảm bảo createdAt là thời điểm hiện tại nếu chưa có
        if (user.getCreatedAt() == null) {
            user.setCreatedAt(java.time.LocalDateTime.now());
        }
        return usersRepository.save(user);
    }

    public int updateUser(Users user) {
        Users current = usersRepository.findById(user.getUserId()).orElse(null);
        if (current == null) return 0;
        // Merge fields: if incoming field is null, keep old value
        if (user.getEmail() == null) user.setEmail(current.getEmail());
        if (user.getPassword() == null) user.setPassword(current.getPassword());
        if (user.getRole() == null) user.setRole(current.getRole());
        if (user.getIsActive() == null) user.setIsActive(current.getIsActive());
        if (user.getCreatedAt() == null) user.setCreatedAt(current.getCreatedAt());
        return usersRepository.update(user);
    }

    public int deleteUser(Integer id) {
        return usersRepository.deleteById(id);
    }
}
