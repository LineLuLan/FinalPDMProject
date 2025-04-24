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
            return "Email đã tồn tại!";
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
        return usersRepository.update(user);
    }

    public int deleteUser(Integer id) {
        return usersRepository.deleteById(id);
    }
}
