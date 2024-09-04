package uz.anas.gymcrm.dao;

import org.springframework.stereotype.Repository;
import uz.anas.gymcrm.entity.Training;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.logging.Logger;

@Repository
public class TrainingDAO {

    private Map<UUID, Training> trainings;
    private static final Logger log = Logger.getLogger(TraineeDAO.class.getName());

    @PostConstruct
    public void init() {
        trainings = new HashMap<>();
    }

    public Training save(Training training) {
        training.setId(UUID.randomUUID());
        trainings.put(training.getId(), training);
        return training;
    }

    public Optional<Training> findById(UUID id) {
        return Optional.ofNullable(trainings.get(id));
    }

    public void deleteById(UUID id) {
        if (trainings.containsKey(id)) {
            trainings.remove(id);
        } else {
            log.warning("Training with id " + id + " not found");
            throw new IllegalArgumentException("Training with id " + id + " not found");
        }
    }

    public List<Training> findAll() {
        return new ArrayList<>(trainings.values());
    }
}
