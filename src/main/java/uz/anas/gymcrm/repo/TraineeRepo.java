package uz.anas.gymcrm.repo;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import uz.anas.gymcrm.entity.Trainee;
import uz.anas.gymcrm.entity.User;

import java.util.Optional;

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

    public Optional<Trainee> findByUsername(String username) {
        String jpql = "from Trainee t where t.user.username = :username";
        Trainee trainee = em.createQuery(jpql, Trainee.class)
                .setParameter("username", username)
                .getSingleResult();

        return Optional.ofNullable(trainee);
    }

    public void deleteByUsername(String username) {
        em.createQuery("delete from Trainee t where t.user.username = :username")
                .setParameter("username", username)
                .executeUpdate();
    }

    public boolean isAuthenticated(@NotNull User user) {
        String jpql = "from Trainee t where t.user.username = :username";
        Trainee trainee = em.createQuery(jpql, Trainee.class)
                .setParameter("username", user.getUsername())
                .getSingleResult();

        return trainee != null;
    }


}
