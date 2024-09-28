package uz.anas.gymcrm.service;

import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.anas.gymcrm.model.dto.Authentication;
import uz.anas.gymcrm.model.dto.put.PutTrainerDto;
import uz.anas.gymcrm.model.entity.Trainer;
import uz.anas.gymcrm.model.entity.User;
import uz.anas.gymcrm.model.entity.enums.Specialization;
import uz.anas.gymcrm.repository.TrainerRepository;
import uz.anas.gymcrm.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TrainerService {

    private final TrainerRepository trainerRepo;
    private final CredentialGenerator credentialGenerator;
    private final UserRepository userRepo;
    private final Log log = LogFactory.getLog(TrainerService.class);
    @Value("${trainer.data}")
    private String trainerData;

    @PostConstruct
    public void init() {
        for (String line : trainerData.split(";")) {
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

    public Optional<Trainer> getTrainerByUsername(@NotNull Authentication authentication, String username) {
        if (!userRepo.isAuthenticated(authentication)) {
            log.warn("Request sent without authentication");
            throw new RuntimeException("User is not authenticated");
        }
        return trainerRepo.findByUserUsername(username);
    }

    @Transactional
    public void changePasswordByUsername(@NotNull Authentication authentication, String username, String newPassword) {
        if (!userRepo.isAuthenticated(authentication)) {
            log.warn("Request sent without authentication");
            throw new RuntimeException("User is not authenticated");
        }
        trainerRepo.findByUserUsername(username).ifPresent(trainer -> {
            User user = trainer.getUser();
            if (user != null) {
                user.setPassword(newPassword);
                userRepo.save(user);
            } else {
                log.warn("Trainer with id: " + trainer.getId() + " user not found");
            }
        });
    }

    public void activateTrainerByUsername(@NotNull Authentication authentication, String username, boolean isActive) {
        if (!userRepo.isAuthenticated(authentication)) {
            log.warn("Request sent without authentication");
            throw new RuntimeException("User is not authenticated");
        }
        trainerRepo.findByUserUsername(username).ifPresent(trainer -> {
            User user = trainer.getUser();
            if (user != null) {
                user.setIsActive(isActive);
                userRepo.save(user);
            } else {
                log.warn("Trainer with id: " + trainer.getId() + " user not found");
            }
        });
    }

    public List<Trainer> getTrainersByNotAssigned(@NotNull Authentication authentication, String traineeUsername) {
        if (!userRepo.isAuthenticated(authentication)) {
            log.warn("Request sent without authentication");
            throw new RuntimeException("User is not authenticated");
        }
        return trainerRepo.findByTraineeUsernameNotAssigned(traineeUsername);
    }

    public List<Trainer> getAllTrainers() {
        return trainerRepo.findAll();
    }

    public Trainer updateTrainer(Authentication authentication, PutTrainerDto trainerDto) {
        if (!userRepo.isAuthenticated(authentication)) {
            log.warn("Request sent without authentication");
            throw new RuntimeException("Request sent without authentication");
        }
        Optional<Trainer> trainerOptional = trainerRepo.findByUserUsername(trainerDto.username());
        if (trainerOptional.isEmpty()) {
            log.warn("Trainee with username %s not found for update".formatted(trainerDto.username()));
            throw new RuntimeException("Trainee with username " + trainerDto.username() + " not found for update");
        }

        // Needed fields that can be updated are being updated
        Trainer trainer = trainerOptional.get();
        trainer.getUser().setFirstName(trainerDto.firstName());
        trainer.getUser().setLastName(trainerDto.lastName());
        trainer.getUser().setIsActive(trainerDto.isActive());

        trainerRepo.save(trainer);
        log.info("Trainee updated with id: " + trainer.getId());
        return trainer;
    }
}
