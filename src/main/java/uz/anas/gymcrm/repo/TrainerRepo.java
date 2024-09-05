package uz.anas.gymcrm.repo;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import uz.anas.gymcrm.entity.Trainer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class TrainerRepo {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public Trainer save(Trainer trainer) {
        em.persist(trainer);
        em.flush();
        return trainer;
    }

    public List<Trainer> findAll() {
        return em.createQuery("from Trainer", Trainer.class)
                .getResultList();
    }

    public Optional<Trainer> findById(UUID id) {
        String jpql = "from Trainer t where t.id = :id";
        Trainer trainer = em.createQuery(jpql, Trainer.class)
                .setParameter("id", id)
                .getSingleResult();
        
        return Optional.ofNullable(trainer);
    }

    public Optional<Trainer> findByUsername(String username) {
        String jpql = "from Trainer t where t.user.username = :username";
        Trainer trainer = em.createQuery(jpql, Trainer.class)
                .setParameter("username", username)
                .getSingleResult();

        return Optional.ofNullable(trainer);
    }

    public void deleteById(UUID trainerId) {
        em.remove(em.find(Trainer.class, trainerId));
    }
}
