package uz.anas.gymcrm.repo;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import uz.anas.gymcrm.entity.Trainer;
import uz.anas.gymcrm.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public class TrainerRepo {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public Trainer save(@Valid Trainer trainer) {
        em.persist(trainer);
        em.flush();
        return trainer;
    }

    public Optional<Trainer> findByUsername(String username) {
        try {
            String jpql = "from Trainer t where t.user.username = :username";
            Trainer trainer = em.createQuery(jpql, Trainer.class)
                    .setParameter("username", username)
                    .getSingleResult();
            return Optional.ofNullable(trainer);
        } catch (NoResultException e) {
            return Optional.empty();
        }

    }

    public List<Trainer> findByTraineeUsernameNotAssigned(String traineeUsername) {
        String jpql = "from Trainer t where t.user.username is null or t.user.username <> :traineeUsername ";
        return em.createQuery(jpql, Trainer.class).getResultList();
    }

    public boolean isAuthenticated(@NotNull User user) {
        try {
            String jpql = "from Trainer t where t.user.username = :username";
            Trainer trainer = em.createQuery(jpql, Trainer.class)
                    .setParameter("username", user.getUsername())
                    .getSingleResult();
            return trainer != null;
        } catch (NoResultException e) {
            return false;
        }

    }

    public List<Trainer> findAll() {
        String jpql = "from Trainer t";
        return em.createQuery(jpql, Trainer.class).getResultList();
    }
}
