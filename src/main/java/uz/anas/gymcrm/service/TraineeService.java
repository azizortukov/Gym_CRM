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
import uz.anas.gymcrm.model.dto.TrainerBaseDto;
import uz.anas.gymcrm.model.dto.put.PutTraineeDto;
import uz.anas.gymcrm.model.entity.Trainee;
import uz.anas.gymcrm.model.entity.Trainer;
import uz.anas.gymcrm.model.entity.User;
import uz.anas.gymcrm.model.mapper.TraineeMapper;
import uz.anas.gymcrm.repository.TraineeRepository;
import uz.anas.gymcrm.repository.TrainerRepository;
import uz.anas.gymcrm.repository.UserRepository;

import java.sql.Date;
import java.util.*;

@Service
@RequiredArgsConstructor
public class TraineeService {


    private final TraineeRepository traineeRepo;
    private final CredentialGenerator credentialGenerator;
    private final UserRepository userRepo;
    private final TrainerRepository trainerRepo;
    private final TraineeMapper traineeMapper;
    @Value("${trainee.data}")
    private String traineeData;
    private final Log log = LogFactory.getLog(TraineeService.class);

    @PostConstruct
    public void init() {
        for (String line : traineeData.split(";")) {
                String[] parts = line.split(",");
                User user = new User(parts[0], parts[1], parts[2], parts[3], Boolean.parseBoolean(parts[4]));
                Trainee trainee = new Trainee(user, Date.valueOf(parts[5]), parts[6]);
                traineeRepo.save(trainee);
            }
    }

    @Transactional
    public Trainee create(@Valid Trainee trainee) {
        String username = trainee.getUser().getFirstName() + "." + trainee.getUser().getLastName();
        if (userRepo.existsByUsername(username)) {
            username = credentialGenerator.genUsername(username);
        }
        trainee.getUser().setUsername(username);
        trainee.getUser().setPassword(credentialGenerator.genPassword());

        Trainee savedTrainee = traineeRepo.save(trainee);
        log.info("Trainee saved with id: " + savedTrainee.getId());
        return savedTrainee;
    }

    public Optional<Trainee> getByUsername(@NotNull Authentication authentication, String username) {
        if (!userRepo.isAuthenticated(authentication)) {
            log.warn("Request sent without authentication");
            throw new RuntimeException("Request sent without authentication");
        }
        return traineeRepo.findByUserUsername(username);
    }

    @Transactional
    public void changePasswordByUsername(@NotNull Authentication authentication, String username, String newPassword) {
        if (!userRepo.isAuthenticated(authentication)) {
            log.warn("Request sent without authentication");
            throw new RuntimeException("Request sent without authentication");
        }
        traineeRepo.findByUserUsername(username).ifPresent(trainee -> {
            User user = trainee.getUser();
            if (user != null) {
                user.setPassword(newPassword);
                userRepo.save(user);
            } else {
                log.warn("Trainee with id: " + trainee.getId() + " user not found");
            }
        });
    }

    // For updating trainee's trainers list
    @Transactional
    public List<TrainerBaseDto> updateTrainerList(@NotNull Authentication authentication, String traineeUsername, Set<TrainerBaseDto> trainerList) {
        if (!userRepo.isAuthenticated(authentication)) {
            log.warn("Request sent without authentication");
            throw new RuntimeException("Request sent without authentication");
        }
        Optional<Trainee> traineeOptional = traineeRepo.findByUserUsername(traineeUsername);
        // Checkpoint if trainee by given username exists
        if (traineeOptional.isEmpty()) {
            log.warn("Trainee with %s username is not found".formatted(traineeUsername));
            throw new RuntimeException("Trainee with %s username is not found".formatted(traineeUsername));
        }
        Trainee trainee = traineeOptional.get();
        Set<Trainer> newTrainers = new HashSet<>();
        List<TrainerBaseDto> ansTrainersDto = new ArrayList<>();

        // Setting every trainer with given username to trainee
        for (TrainerBaseDto trainerBaseDto : trainerList) {
            trainerRepo.findByUserUsername(trainerBaseDto.username()).ifPresent(newTrainers::add);
        }
        for (Trainer trainer : newTrainers) {
            ansTrainersDto.add(traineeMapper.toTrainerBaseDto(trainer));
        }

        trainee.setTrainers(newTrainers);
        log.info("Trainee trainers list updated with id: " + trainee.getId());
        return ansTrainersDto;
    }

    @Transactional
    public Trainee update(@NotNull Authentication authentication, @Valid PutTraineeDto traineeDto) {
        if (!userRepo.isAuthenticated(authentication)) {
            log.warn("Request sent without authentication");
            throw new RuntimeException("Request sent without authentication");
        }
        Optional<Trainee> traineeOptional = traineeRepo.findByUserUsername(traineeDto.username());
        if (traineeOptional.isEmpty()) {
            log.warn("Trainee with username %s not found for update".formatted(traineeDto.username()));
            throw new RuntimeException("Trainee with username " + traineeDto.username() + " not found for update");
        }

        // Needed fields that can be updated are being updated
        Trainee trainee = traineeOptional.get();
        trainee.getUser().setFirstName(traineeDto.firstName());
        trainee.getUser().setLastName(traineeDto.lastName());
        trainee.getUser().setIsActive(traineeDto.isActive());

        trainee.setAddress(traineeDto.address());
        trainee.setDateOfBirth(traineeDto.dateOfBirth());

        traineeRepo.save(trainee);
        log.info("Trainee updated with id: " + trainee.getId());
        return trainee;
    }

    public void activateTraineeByUsername(@NotNull Authentication authentication, String username) {
        if (!userRepo.isAuthenticated(authentication)) {
            log.warn("Request sent without authentication");
            throw new RuntimeException("User is not authenticated");
        }
        traineeRepo.findByUserUsername(username).ifPresent(trainee -> {
            User user = trainee.getUser();
            if (user != null) {
                boolean currentStatus = user.getIsActive();
                user.setIsActive(!currentStatus);
                userRepo.save(user);

            } else {
                log.warn("Trainee with id: " + trainee.getId() + " user not found");
            }
        });
    }

    public void deleteByUsername(@NotNull Authentication authentication, String username) {
        if (!userRepo.isAuthenticated(authentication)) {
            log.warn("Request sent without authentication");
            throw new RuntimeException("User is not authenticated");
        }
        traineeRepo.deleteByUserUsername(username);
    }

    public List<TrainerBaseDto> getTrainersByNotAssigned(@NotNull Authentication authentication, String traineeUsername) {
        if (!userRepo.isAuthenticated(authentication)) {
            log.warn("Request sent without authentication");
            throw new RuntimeException("User is not authenticated");
        }
        return traineeRepo.findByTraineeUsernameNotAssigned(traineeUsername);
    }

    public List<Trainee> getAllTrainees() {
        return traineeRepo.findAll();
    }
}
