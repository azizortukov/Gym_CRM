package uz.anas.gymcrm.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import uz.anas.gymcrm.entity.Trainee;

import java.util.*;
import java.util.logging.Logger;

@Repository
@RequiredArgsConstructor
public class TraineeDAO implements UserDao {

    private final Map<UUID, Trainee> trainees = new HashMap<>();
    private static final Logger log = Logger.getLogger(TraineeDAO.class.getName());

    public Trainee save(Trainee trainee) {
        trainee.setId(UUID.randomUUID());
        trainees.put(trainee.getId(), trainee);
        return trainee;
    }

    public Optional<Trainee> findById(UUID id) {
        return Optional.ofNullable(trainees.get(id));
    }

    public Optional<Trainee> findByUsername(String username) {
        for (Trainee trainee : trainees.values()) {
            if (trainee.getUsername().equals(username)) {
                return Optional.of(trainee);
            }
        }
        return Optional.empty();
    }

    public void deleteById(UUID id) {
        if (trainees.containsKey(id)) {
            trainees.remove(id);
        } else {
            log.warning("Trainee with id " + id + " not found");
            throw new IllegalArgumentException("Trainee with id " + id + " not found");
        }
    }

    @Override
    public boolean existsByUsername(String username) {
        for (Trainee value : trainees.values()) {
            if (value.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    public List<Trainee> findAll() {
        return new ArrayList<>(trainees.values());
    }
}
