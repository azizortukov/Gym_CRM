package uz.anas.gymcrm.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import uz.anas.gymcrm.entity.Trainer;

import java.util.*;
import java.util.logging.Logger;

@Repository
@RequiredArgsConstructor
public class TrainerDAO implements UserDao{

    private final Map<UUID, Trainer> trainers = new HashMap<>();
    private static final Logger log = Logger.getLogger(TraineeDAO.class.getName());

    public Trainer save(Trainer trainer) {
        trainer.setId(UUID.randomUUID());
        trainers.put(trainer.getId(), trainer);
        return trainer;
    }

    public Optional<Trainer> findById(UUID id) {
        return Optional.ofNullable(trainers.get(id));
    }

    public Optional<Trainer> findByUsername(String username) {
        for (Trainer value : trainers.values()) {
            if (value.getUsername().equals(username)) {
                return Optional.of(value);
            }
        }
        return Optional.empty();
    }

    public void deleteById(UUID id) {
        if (trainers.containsKey(id)) {
            trainers.remove(id);
        } else {
            log.warning("Trainer with id " + id + " not found");
        }
    }

    public void deleteByUsername(String username) {
        Optional<Trainer> trainerOptional = findByUsername(username);
        if (trainerOptional.isPresent()) {
            trainers.remove(trainerOptional.get().getId());
        } else {
            log.warning("Trainer with username: " + username + " not found");
        }
    }

    @Override
    public boolean existsByUsername(String username) {
        for (Trainer value : trainers.values()) {
            if (value.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    public List<Trainer> findAll() {
        return new ArrayList<>(trainers.values());
    }
}
