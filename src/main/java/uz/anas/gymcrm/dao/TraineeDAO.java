package uz.anas.gymcrm.dao;

import org.springframework.stereotype.Repository;
import uz.anas.gymcrm.entity.Trainee;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;
import java.util.logging.Logger;

@Repository
public class TraineeDAO implements UserDao {

    private Map<UUID, Trainee> trainees;
    private static final Logger log = Logger.getLogger(TraineeDAO.class.getName());

    @PostConstruct
    public void init() {
        trainees = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(Paths.get("src/main/resources/trainee-data.csv").toFile()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                Trainee trainee = Trainee.builder()
                        .id(UUID.fromString(parts[0]))
                        .firstName(parts[1])
                        .lastName(parts[2])
                        .username(parts[3])
                        .password(parts[4])
                        .isActive(Boolean.parseBoolean(parts[5]))
                        .dateOfBirth(LocalDate.parse(parts[6]))
                        .address(parts[7])
                        .build();

                trainees.put(trainee.getId(), trainee);
            }
        } catch (IOException e) {
            log.warning(e.getMessage());
        }
    }


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
