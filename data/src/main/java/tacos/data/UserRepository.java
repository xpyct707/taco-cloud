package tacos.data;

import org.springframework.data.repository.CrudRepository;
import tacos.User;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
