package antifraud.persistence;

import antifraud.model.user.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    User findByUsername(String username);

    List<User> findAll();

    User deleteByUsername(String username);
}
