package backend.main.repository;

import backend.main.model.Users;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface UsersRepository {
    boolean existsByEmail(String email);
    Optional<Users> findById(Integer userId);
    Optional<Users> findByEmail(String email);
    List<Users> findAll();
    int save(Users user);
    int update(Users user);
    int deleteById(Integer userId);
}
