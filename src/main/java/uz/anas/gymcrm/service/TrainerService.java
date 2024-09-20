package uz.anas.gymcrm.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.anas.gymcrm.entity.Trainer;
import uz.anas.gymcrm.entity.User;
import uz.anas.gymcrm.entity.enums.Specialization;
import uz.anas.gymcrm.repo.TrainerRepo;
import uz.anas.gymcrm.repo.UserRepo;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TrainerService {

    private final TrainerRepo trainerRepo;
    private final CredentialGenerator credentialGenerator;
    private final UserRepo userRepo;
    private final Log log = LogFactory.getLog(TrainerService.class);
    @Value("${trainer.data}")
    private List<String> trainerData;

    @PostConstruct
    public void init() {
        for (String line : trainerData) {
            String[] parts = line.split(",");
            User user = User.builder()
                    .firstName(parts[0])
                    .lastName(parts[1])
                    .username(parts[2])
                    .password(parts[3])
                    .isActive(Boolean.parseBoolean(parts[4]))
                    .build();

            Trainer trainer = Trainer.builder()
                    .user(user)
                    .specialization(Specialization.AEROBICS)
                    .build();
            trainerRepo.save(trainer);
        }
    }

    public Trainer createTrainer(@Valid Trainer trainer) {
        String username = trainer.getUser().getFirstName() + "." + trainer.getUser().getLastName();
        if (userRepo.existsByUsername(username)) {
            username = credentialGenerator.genUsername(username);
        }
        trainer.getUser().setUsername(username);
        trainer.getUser().setPassword(credentialGenerator.genPassword());
        return trainerRepo.save(trainer);
    }

    public Optional<Trainer> getTrainerByUsername(@NotNull User authentication, String username) {
        if (!trainerRepo.isAuthenticated(authentication)) {
            log.warn("Request sent without authentication");
            throw new RuntimeException("User is not authenticated");
        }
        return trainerRepo.findByUsername(username);
    }

    @Transactional
    public void changePasswordByUsername(@NotNull User authentication, String username, String newPassword) {
        if (!trainerRepo.isAuthenticated(authentication)) {
            log.warn("Request sent without authentication");
            throw new RuntimeException("User is not authenticated");
        }
        trainerRepo.findByUsername(username).ifPresent(trainer -> {
            User user = trainer.getUser();
            if (user != null) {
                user.setPassword(newPassword);
                userRepo.save(user);
            } else {
                log.warn("Trainer with id: " + trainer.getId() + " user not found");
            }
        });
    }

    public void activateTrainerByUsername(@NotNull User authentication, String username, boolean isActive) {
        if (!trainerRepo.isAuthenticated(authentication)) {
            log.warn("Request sent without authentication");
            throw new RuntimeException("User is not authenticated");
        }
        trainerRepo.findByUsername(username).ifPresent(trainer -> {
            User user = trainer.getUser();
            if (user != null) {
                user.setActive(isActive);
                userRepo.save(user);
            } else {
                log.warn("Trainer with id: " + trainer.getId() + " user not found");
            }
        });
    }

    public List<Trainer> getTrainersByNotAssigned(@NotNull User authentication, String traineeUsername) {
        if (!trainerRepo.isAuthenticated(authentication)) {
            log.warn("Request sent without authentication");
            throw new RuntimeException("User is not authenticated");
        }
        return trainerRepo.findByTraineeUsernameNotAssigned(traineeUsername);
    }

    public List<Trainer> getAllTrainers() {
        return trainerRepo.findAll();
    }
}
