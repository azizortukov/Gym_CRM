package uz.anas.gymcrm.repository;

import jakarta.validation.constraints.NotEmpty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.anas.gymcrm.model.entity.Training;
import uz.anas.gymcrm.model.entity.TrainingType;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Repository
public interface TrainingRepository extends JpaRepository<Training, UUID> {

    @Query(nativeQuery = true, value = """
            SELECT t FROM training t
            JOIN training_type tt ON tt.id = t.training_type_id
            JOIN trainee t2 ON t.trainee_id = t2.id
            JOIN users u ON u.id = t2.id
            WHERE u.username = :traineeUsername AND
            (:fromDate IS NULL OR t.training_date >= :fromDate) AND
            (:toDate IS NULL OR t.training_date <= :toDate) AND
            (:trainerFirstName IS NULL OR u.first_name ILIKE :trainerFirstName) AND
            (:trainingType IS NULL OR tt.training_type_name = :trainingType)
            """)
    List<Training> findByTraineeAndCriteria(
            @NotEmpty String traineeUsername, String trainerFirstName,
            Date fromDate, Date toDate, String trainingType);

    @Query(nativeQuery = true, value = """
            SELECT t FROM training t
            JOIN trainee t2 ON t.trainee_id = t2.id
            JOIN users u ON u.id = t2.id
            WHERE u.username = :trainerUsername AND
            (:fromDate IS NULL OR t.training_date >= :fromDate) AND
            (:toDate IS NULL OR t.training_date <= :toDate) AND
            (:trainerFirstName IS NULL OR u.first_name ILIKE :traineeFirstName)""")
    List<Training> findByTrainerAndCriteria(
            @NotEmpty String trainerUsername, String traineeFirstName,
            Date fromDate, Date toDate);

    @Query(nativeQuery = true, value = "SELECT * FROM training_type")
    List<TrainingType> findAllTrainingTypes();

}