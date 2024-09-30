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
import uz.anas.gymcrm.model.dto.ResponseDto;
import uz.anas.gymcrm.model.dto.TrainerBaseDto;
import uz.anas.gymcrm.model.dto.get.GetTraineeDto;
import uz.anas.gymcrm.model.dto.patch.TraineeActivationDto;
import uz.anas.gymcrm.model.dto.post.PostTraineeDto;
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
    public ResponseDto<PostTraineeDto> create(@Valid PostTraineeDto postTraineeDto) {
        Trainee trainee = traineeMapper.toEntity(postTraineeDto);
        String username = trainee.getUser().getFirstName() + "." + trainee.getUser().getLastName();
        if (userRepo.existsByUsername(username)) {
            username = credentialGenerator.genUsername(username);
        }
        trainee.getUser().setUsername(username);
        trainee.getUser().setPassword(credentialGenerator.genPassword());

        traineeRepo.save(trainee);
        log.info("Trainee saved with id: " + trainee.getId());
        return new ResponseDto<>(traineeMapper.toPostDto(trainee));
    }

    public ResponseDto<GetTraineeDto> getByUsername(@NotNull Authentication authentication, String username) {
        if (!userRepo.isAuthenticated(authentication)) {
            log.warn("Request sent without authentication");
            return new ResponseDto<>("Request sent without authentication");
        }
        Optional<Trainee> trainee = traineeRepo.findByUserUsername(username);
        if (trainee.isPresent()) {
            GetTraineeDto traineeDto = traineeMapper.toGetDto(trainee.get());
            return new ResponseDto<>(traineeDto);
        } else {
            log.warn("Trainee not found for username " + username);
            return new ResponseDto<>("Trainee is not found");
        }
    }

    // For updating trainee's trainers list
    @Transactional
    public ResponseDto<List<TrainerBaseDto>> updateTrainerList(@NotNull Authentication authentication, String traineeUsername, Set<TrainerBaseDto> trainerList) {
        if (!userRepo.isAuthenticated(authentication)) {
            log.warn("Request sent without authentication");
            return new ResponseDto<>("Request sent without authentication");
        }
        Optional<Trainee> traineeOptional = traineeRepo.findByUserUsername(traineeUsername);
        // Checkpoint if trainee by given username exists
        if (traineeOptional.isEmpty()) {
            log.warn("Trainee with %s username is not found".formatted(traineeUsername));
            return new ResponseDto<>("Trainee with %s username is not found".formatted(traineeUsername));
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
        return new ResponseDto<>(ansTrainersDto);
    }

    @Transactional
    public ResponseDto<PutTraineeDto> update(@NotNull Authentication authentication, @Valid PutTraineeDto traineeDto) {
        if (!userRepo.isAuthenticated(authentication)) {
            log.warn("Request sent without authentication");
            return new ResponseDto<>("Request sent without authentication");
        }
        Optional<Trainee> traineeOptional = traineeRepo.findByUserUsername(traineeDto.username());
        if (traineeOptional.isEmpty()) {
            log.warn("Trainee with username %s not found for update".formatted(traineeDto.username()));
            return new ResponseDto<>("Trainee with username %s not found".formatted(traineeDto.username()));
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
        return new ResponseDto<>(traineeMapper.toPutDto(trainee));
    }

    public ResponseDto<?> activateTraineeByUsername(@NotNull Authentication authentication, TraineeActivationDto activationDto) {
        if (!userRepo.isAuthenticated(authentication)) {
            log.warn("Request sent without authentication");
            return new ResponseDto<>("User is not authenticated");
        }
        traineeRepo.findByUserUsername(activationDto.username()).ifPresent(trainee -> {
            User user = trainee.getUser();
            if (user != null) {
                user.setIsActive(activationDto.isActive());
                userRepo.save(user);
            } else {
                log.warn("Trainee with id: " + trainee.getId() + " user not found");
            }
        });
        return new ResponseDto<>();
    }

    public ResponseDto<?> deleteByUsername(@NotNull Authentication authentication, String username) {
        if (!userRepo.isAuthenticated(authentication)) {
            log.warn("Request sent without authentication");
            return new ResponseDto<>("User is not authenticated");
        }
        traineeRepo.deleteByUserUsername(username);
        return new ResponseDto<>();
    }

    public ResponseDto<List<TrainerBaseDto>> getTrainersByNotAssigned(@NotNull Authentication authentication, String traineeUsername) {
        if (!userRepo.isAuthenticated(authentication)) {
            log.warn("Request sent without authentication");
            return new ResponseDto<>("User is not authenticated");
        }
        var ansDto = traineeRepo.findByTraineeUsernameNotAssigned(traineeUsername);
        return new ResponseDto<>(ansDto);
    }

    public List<Trainee> getAllTrainees() {
        return traineeRepo.findAll();
    }
}
