package uz.anas.gymcrm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.anas.gymcrm.model.entity.Trainee;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TraineeRepository extends JpaRepository<Trainee, UUID> {

    Optional<Trainee> findByUserUsername(String username);

    void deleteByUserUsername(String username);
}
