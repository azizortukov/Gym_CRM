package uz.anas.gymcrm.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.anas.gymcrm.entity.Trainee;
import uz.anas.gymcrm.entity.Trainer;
import uz.anas.gymcrm.entity.User;
import uz.anas.gymcrm.repo.TraineeRepo;
import uz.anas.gymcrm.repo.UserRepo;

import javax.annotation.PostConstruct;
import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TraineeService {

    private final TraineeRepo traineeRepo;
    private final CredentialGenerator credentialGenerator;
    private final UserRepo userRepo;
    private final Log log = LogFactory.getLog(TraineeService.class);
    @Value("${trainee.data}")
    private List<String> traineeData;

    @PostConstruct
    public void init() {
        for (String line : traineeData) {
            String[] parts = line.split(",");
            User user = User.builder()
                    .firstName(parts[0])
                    .lastName(parts[1])
                    .username(parts[2])
                    .password(parts[3])
                    .isActive(Boolean.parseBoolean(parts[4]))
                    .build();

            Trainee trainee = Trainee.builder()
                    .user(user)
                    .dateOfBirth(Date.valueOf(parts[5]))
                    .address(parts[6])
                    .build();
            traineeRepo.save(trainee);
        }
    }

    @Transactional
    public Trainee createTrainee(@Valid Trainee trainee) {
        String username = trainee.getUser().getUsername() + "." + trainee.getUser().getLastName();
        if (userRepo.existsByUsername(username)) {
            username = credentialGenerator.genUsername(username);
        }
        trainee.getUser().setUsername(username);
        trainee.getUser().setPassword(credentialGenerator.genPassword());

        Trainee savedTrainee = traineeRepo.save(trainee);
        log.info("Trainee saved with id: " + savedTrainee.getId());
        return savedTrainee;
    }

    public Optional<Trainee> getTraineeByUsername(@NotNull User authentication, String username) {
        if (!traineeRepo.isAuthenticated(authentication)) {
            log.warn("Request sent without authentication");
            return Optional.empty();
        }
        return traineeRepo.findByUsername(username);
    }

    @Transactional
    public void changePasswordByUsername(@NotNull User authentication, String username, String newPassword) {
        if (!traineeRepo.isAuthenticated(authentication)) {
            log.warn("Request sent without authentication");
        }
        traineeRepo.findByUsername(username).ifPresent(trainee -> {
            User user = trainee.getUser();
            if (user != null) {
                user.setPassword(credentialGenerator.genPassword());
                userRepo.save(user);
            } else {
                log.warn("Trainee with id: " + trainee.getId() + " user not found");
            }
        });
    }

    // For updating trainee's trainers list
    @Transactional
    public Trainee updateTrainee(@NotNull User authentication, @Valid Trainee trainee, @NotNull Set<Trainer> trainerList) {
        if (!traineeRepo.isAuthenticated(authentication)) {
            log.warn("Request sent without authentication");
            throw new RuntimeException("User is not authenticated");
        }
        trainee.setTrainers(trainerList);
        traineeRepo.save(trainee);
        log.info("Trainee trainers list updated with id: " + trainee.getId());
        return trainee;
    }

    public void activateTraineeByUsername(@NotNull User authentication, String username) {
        if (!traineeRepo.isAuthenticated(authentication)) {
            log.warn("Request sent without authentication");
        }
        traineeRepo.findByUsername(username).ifPresent(trainee -> {
            User user = trainee.getUser();
            if (user != null) {
                boolean currentStatus = user.isActive();
                user.setActive(!currentStatus);
                userRepo.save(user);

            } else {
                log.warn("Trainee with id: " + trainee.getId() + " user not found");
            }
        });
    }

    public void deleteByUsername(@NotNull User authentication, String username) {
        if (!traineeRepo.isAuthenticated(authentication)) {
            log.warn("Request sent without authentication");
        }
        traineeRepo.deleteByUsername(username);
    }

    public List<Trainee> getAllTrainees() {
        return traineeRepo.findAll();
    }
}
