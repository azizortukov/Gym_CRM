package uz.anas.gymcrm.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import uz.anas.gymcrm.entity.Trainee;
import uz.anas.gymcrm.entity.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

@Repository
@RequiredArgsConstructor
public class TraineeDAO {

    private final Map<UUID, Trainee> trainees = new HashMap<>();
    private final UserDAO userDAO;
    private static final Logger log = Logger.getLogger(TraineeDAO.class.getName());

    public void save(Trainee trainee) {
        trainees.put(trainee.getId(), trainee);
    }

    public Optional<Trainee> findById(UUID id) {
        Trainee trainee = trainees.get(id);
        if (trainee == null) {
            log.warning("Trainee with id: " + id + " not found");
            return Optional.empty();
        } else {
            return Optional.of(trainee);
        }
    }

    public Optional<Trainee> findByUsername(String username) {
        Optional<User> user = userDAO.findByUsername(username);
        if (user.isPresent()) {
            Trainee trainee = trainees.get(user.get().getId());
            return Optional.of(trainee);
        }
        return Optional.empty();
    }

    public void deleteById(UUID id) {
        trainees.remove(id);
    }

    public void deleteByUsername(String username) {
        Optional<User> user = userDAO.findByUsername(username);
        if (user.isPresent()) {
            trainees.remove(user.get().getId());
        } else {
            log.warning("Trainee with username: " + username + " not found");
        }
    }

}
