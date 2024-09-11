package uz.anas.gymcrm.repo;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import uz.anas.gymcrm.entity.Training;

import java.util.Date;
import java.util.List;

@Repository
public class TrainingRepo {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public Training save(Training training) {
        em.persist(training);
        em.flush();
        return training;
    }

    public List<Training> findByTraineeAndCriteria(
            @NotEmpty String traineeUsername, String trainerFirstName,
            Date fromDate, Date toDate, String trainingType) {

        String jpql = """
                select t from Training t
                where t.trainee.user.username = :traineeUsername and
                (:fromDate is null or t.trainingDate >= :fromDate) and
                (:toDate is null or t.trainingDate <= :toDate) and
                (:trainerFirstName is null or t.trainer.user.firstName ilike :trainerFirstName) and
                (:trainingType is null or t.trainingType.trainingTypeName = :trainingType)""";

        TypedQuery<Training> query = em.createQuery(jpql, Training.class);

        query.setParameter("traineeUsername", traineeUsername);
        query.setParameter("fromDate", fromDate);
        query.setParameter("toDate", toDate);
        query.setParameter("trainerFirstName", "%" + trainerFirstName.toLowerCase() + "%");
        query.setParameter("trainingType", trainingType);
        return query.getResultList();

    }

    public List<Training> findByTrainerAndCriteria(
            @NotEmpty String trainerUsername, String traineeFirstName,
            Date fromDate, Date toDate) {

        String jpql = """
                select t from Training t
                where t.trainer.user.username = :trainerUsername and
                (:fromDate is null or t.trainingDate >= :fromDate) and
                (:toDate is null or t.trainingDate <= :toDate) and
                (:trainerFirstName is null or t.trainee.user.firstName ilike :traineeFirstName)""";

        TypedQuery<Training> query = em.createQuery(jpql, Training.class);

        query.setParameter("trainerUsername", trainerUsername);
        query.setParameter("fromDate", fromDate);
        query.setParameter("toDate", toDate);
        query.setParameter("traineeFirstName", "%" + traineeFirstName.toLowerCase() + "%");
        return query.getResultList();

    }


}
