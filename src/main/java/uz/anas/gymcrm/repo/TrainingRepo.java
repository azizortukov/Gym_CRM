package uz.anas.gymcrm.repo;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import uz.anas.gymcrm.entity.Training;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

    public List<Training> findAll() {
        return em.createQuery("from Training", Training.class)
                .getResultList();
    }

    public Optional<Training> findById(UUID id) {
        String jpql = "from Training t where t.id = :id";
        Training training = em.createQuery(jpql, Training.class)
                .setParameter("id", id)
                .getSingleResult();

        return Optional.ofNullable(training);
    }

    public void deleteById(UUID trainingId) {
        em.remove(em.find(Training.class, trainingId));
    }
}
