package uz.anas.gymcrm.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import uz.anas.gymcrm.entity.Trainer;
import uz.anas.gymcrm.entity.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

@Repository
@RequiredArgsConstructor
public class TrainerDAO {

    private final Map<UUID, Trainer> trainers = new HashMap<>();
    private final UserDAO userDAO;
    private static final Logger log = Logger.getLogger(TraineeDAO.class.getName());

    public void save(Trainer trainer) {
        trainers.put(trainer.getId(), trainer);
    }

    public Optional<Trainer> findById(UUID id) {
        Trainer trainer = trainers.get(id);
        if (trainer == null) {
            log.warning("Trainer with id: " + id + " not found");
            return Optional.empty();
        } else {
            return Optional.of(trainer);
        }
    }

    public Optional<Trainer> findByUsername(String username) {
        Optional<User> user = userDAO.findByUsername(username);
        if (user.isPresent()) {
            Trainer trainer = trainers.get(user.get().getId());
            return Optional.of(trainer);
        }
        return Optional.empty();
    }

    public void deleteById(UUID id) {
        trainers.remove(id);
    }

    public void deleteByUsername(String username) {
        Optional<User> user = userDAO.findByUsername(username);
        if (user.isPresent()) {
            trainers.remove(user.get().getId());
        } else {
            log.warning("Trainer with username: " + username + " not found");
        }
    }

}
