package uz.anas.gymcrm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.anas.gymcrm.model.entity.Trainer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TrainerRepository extends JpaRepository<Trainer, UUID> {

    Optional<Trainer> findByUserUsername(String username);

    void deleteByUserUsername(String username);

    @Query(value = """
            SELECT * FROM trainer t
            JOIN public.users u ON u.id = t.user_id
            WHERE (u.username IS NULL OR u.username <> :traineeUsername)""", nativeQuery = true)
    List<Trainer> findByTraineeUsernameNotAssigned(String traineeUsername);
}
