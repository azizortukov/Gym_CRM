package uz.anas.gymcrm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.anas.gymcrm.model.dto.TrainerBaseDto;
import uz.anas.gymcrm.model.entity.Trainee;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TraineeRepository extends JpaRepository<Trainee, UUID> {

    Optional<Trainee> findByUserUsername(String username);

    void deleteByUserUsername(String username);

    @Query(value = """
            SELECT u.first_name AS firstName, u.last_name AS lastName,
                   u.username, t.specialization
            FROM trainer t
            JOIN users u ON u.id = t.user_id
            WHERE (u.username IS NULL OR u.username <> :traineeUsername)
            AND u.is_active IS TRUE""", nativeQuery = true)
    List<TrainerBaseDto> findByTraineeUsernameNotAssigned(String traineeUsername);
}
