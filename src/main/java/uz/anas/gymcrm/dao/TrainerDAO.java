package uz.anas.gymcrm.dao;


import org.springframework.stereotype.Repository;
import uz.anas.gymcrm.entity.Trainee;
import uz.anas.gymcrm.entity.Trainer;
import uz.anas.gymcrm.entity.enums.Specialization;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;
import java.util.logging.Logger;

@Repository
public class TrainerDAO implements UserDao {

    private Map<UUID, Trainer> trainers;
    private static final Logger log = Logger.getLogger(TraineeDAO.class.getName());

    @PostConstruct
    public void init() {
        trainers = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(Paths.get("src/main/resources/trainer-data.csv").toFile()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                Trainer trainee = Trainer.builder()
                        .id(UUID.fromString(parts[0]))
                        .firstName(parts[1])
                        .lastName(parts[2])
                        .username(parts[3])
                        .password(parts[4])
                        .isActive(Boolean.parseBoolean(parts[5]))
                        .specialization(Specialization.valueOf(parts[6]))
                        .build();

                trainers.put(trainee.getId(), trainee);
            }
        } catch (IOException e) {
            log.warning(e.getMessage());
        }
    }

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
            throw new IllegalArgumentException("Trainer with id " + id + " not found");
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
