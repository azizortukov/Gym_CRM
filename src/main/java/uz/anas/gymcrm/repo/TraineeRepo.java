package uz.anas.gymcrm.repo;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import uz.anas.gymcrm.entity.Trainee;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class TraineeRepo {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public Trainee save(Trainee trainee) {
        em.persist(trainee);
        em.flush();
        return trainee;
    }

    public List<Trainee> findAll() {
        return em.createQuery("from Trainee", Trainee.class)
                .getResultList();
    }

    public Optional<Trainee> findById(UUID id) {
        String jpql = "from Trainee t where t.id = :id";
        Trainee trainee = em.createQuery(jpql, Trainee.class)
                .setParameter("id", id)
                .getSingleResult();

        return Optional.ofNullable(trainee);
    }

    public Optional<Trainee> findByUsername(String username) {
        String jpql = "from Trainee t where t.user.username = :username";
        Trainee trainee = em.createQuery(jpql, Trainee.class)
                .setParameter("username", username)
                .getSingleResult();

        return Optional.ofNullable(trainee);
    }

    public void deleteById(UUID traineeId) {
        em.remove(em.find(Trainee.class, traineeId));
    }
}
