package uz.anas.gymcrm.repo;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import uz.anas.gymcrm.entity.User;

@Repository
public class UserRepo {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public User save(User user) {
        em.persist(user);
        em.flush();
        return user;
    }

    public boolean existsByUsername(String username) {
        String jpql = "SELECT t FROM User t WHERE t.username = :username";
        TypedQuery<User> query = em.createQuery(jpql, User.class);
        query.setParameter("username", username);
        return query.getSingleResult() != null;
    }
}
