package uz.anas.gymcrm.dao;

import org.springframework.stereotype.Repository;
import uz.anas.gymcrm.entity.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

@Repository
public class UserDAO {

    private final Map<UUID, User> users = new HashMap<>();
    private static final Logger log = Logger.getLogger(TraineeDAO.class.getName());

    public void save(User user) {
        users.put(user.getId(), user);
    }

    public Optional<User> findById(UUID userId) {
        User user = users.get(userId);
        if (user == null) {
            log.warning("User with id: " + userId + " not found");
            return Optional.empty();
        } else {
            return Optional.of(user);
        }
    }

    public Optional<User> findByUsername(String username) {
        for (Map.Entry<UUID, User> entry : users.entrySet()) {
            if (entry.getValue().getUsername().equals(username)) {
                return Optional.of(entry.getValue());
            }
        }
        log.warning("User with username: " + username + " not found");
        return Optional.empty();
    }

    public void deleteById(UUID id) {
        users.remove(id);
    }

}
