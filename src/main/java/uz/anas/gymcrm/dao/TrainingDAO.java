package uz.anas.gymcrm.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import uz.anas.gymcrm.entity.Training;
import uz.anas.gymcrm.entity.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

@Repository
@RequiredArgsConstructor
public class TrainingDAO {

    private final Map<UUID, Training> trainings = new HashMap<>();
    private final UserDAO userDAO;
    private static final Logger log = Logger.getLogger(TraineeDAO.class.getName());

    public void save(Training training) {
        trainings.put(training.getId(), training);
    }

    public Optional<Training> findById(UUID id) {
        Training training = trainings.get(id);
        if (training == null) {
            log.warning("Training with id: " + id + " not found");
            return Optional.empty();
        } else {
            return Optional.of(training);
        }
    }

    public Optional<Training> findByUsername(String username) {
        Optional<User> user = userDAO.findByUsername(username);
        if (user.isPresent()) {
            Training training = trainings.get(user.get().getId());
            return Optional.of(training);
        }
        return Optional.empty();
    }

    public void deleteById(UUID id) {
        trainings.remove(id);
    }

    public void deleteByUsername(String username) {
        Optional<User> user = userDAO.findByUsername(username);
        if (user.isPresent()) {
            trainings.remove(user.get().getId());
        } else {
            log.warning("Training with username: " + username + " not found");
        }
    }

}
