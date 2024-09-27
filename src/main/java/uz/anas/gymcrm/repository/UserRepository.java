package uz.anas.gymcrm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.anas.gymcrm.model.dto.Authentication;
import uz.anas.gymcrm.model.entity.User;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    boolean existsByUsername(String username);

    @Query(nativeQuery = true, value = """
            SELECT COUNT(*) > 0
            FROM users
            WHERE username = :#{#auth.username} AND password = :#{#auth.password}""")
    boolean isAuthenticated(Authentication auth);

}