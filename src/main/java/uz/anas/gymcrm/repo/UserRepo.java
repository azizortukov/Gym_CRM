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
        String jpql = "from User t where t.username = :username";
        TypedQuery<User> query = em.createQuery(jpql, User.class);
        query.setParameter("username", username);
        return query.getSingleResult() != null;
    }

    public boolean isAuthenticated(User user) {
        String jpql = "from User t where t.username = :username and t.password = :password";
        TypedQuery<User> query = em.createQuery(jpql, User.class);
        query.setParameter("username", user.getUsername());
        query.setParameter("password", user.getPassword());
        User singleResult = query.getSingleResult();
        return singleResult != null;
    }
}
