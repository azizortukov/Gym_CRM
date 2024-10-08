package uz.anas.gymcrm.service;

import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uz.anas.gymcrm.model.dto.Authentication;
import uz.anas.gymcrm.model.dto.ResponseDto;
import uz.anas.gymcrm.model.dto.get.GetTrainerDto;
import uz.anas.gymcrm.model.dto.patch.TraineeActivationDto;
import uz.anas.gymcrm.model.dto.post.PostTrainerDto;
import uz.anas.gymcrm.model.dto.put.PutTrainerDto;
import uz.anas.gymcrm.model.entity.Trainer;
import uz.anas.gymcrm.model.entity.User;
import uz.anas.gymcrm.model.entity.enums.Specialization;
import uz.anas.gymcrm.model.mapper.TrainerMapper;
import uz.anas.gymcrm.repository.TrainerRepository;
import uz.anas.gymcrm.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TrainerService {

    private final TrainerRepository trainerRepo;
    private final CredentialGenerator credentialGenerator;
    private final UserRepository userRepo;
    private final TrainerMapper trainerMapper;
    @Value("${trainer.data}")
    private String trainerData;

    @PostConstruct
    public void init() {
        for (String line : trainerData.split(";")) {
            String[] parts = line.split(",");
            User user = new User(parts[0], parts[1], parts[2], parts[3], Boolean.parseBoolean(parts[4]));
            Trainer trainer = new Trainer(user, Specialization.AEROBICS);
            trainerRepo.save(trainer);
        }
    }

    public ResponseDto<PostTrainerDto> createTrainer(@Valid PostTrainerDto trainerDto) {
        Trainer trainer = trainerMapper.toEntity(trainerDto);
        String username = trainer.getUser().getFirstName() + "." + trainer.getUser().getLastName();
        if (userRepo.existsByUsername(username)) {
            username = credentialGenerator.genUsername(username);
        }
        trainer.getUser().setUsername(username);
        trainer.getUser().setPassword(credentialGenerator.genPassword());
        Trainer savedTrainer = trainerRepo.save(trainer);
        return new ResponseDto<>(trainerMapper.toPostDto(savedTrainer));
    }

    public ResponseDto<GetTrainerDto> getTrainerByUsername(@NotNull Authentication authentication, String username) {
        if (!userRepo.isAuthenticated(authentication)) {
            log.warn("Request sent without authentication");
            return new ResponseDto<>("User is not authenticated");
        }
        Optional<Trainer> trainer = trainerRepo.findByUserUsername(username);
        if (trainer.isPresent()) {
            GetTrainerDto trainerDt0 = trainerMapper.toGetDto(trainer.get());
            return new ResponseDto<>(trainerDt0);
        } else {
            log.warn("Trainer not found for username {}", username);
            return new ResponseDto<>("Trainer is not found");
        }
    }

    public ResponseDto<?> activateTrainerByUsername(@NotNull Authentication authentication, TraineeActivationDto activationDto) {
        if (!userRepo.isAuthenticated(authentication)) {
            log.warn("Request sent without authentication");
            return new ResponseDto<>("User is not authenticated");
        }
        trainerRepo.findByUserUsername(activationDto.username()).ifPresent(trainer -> {
            User user = trainer.getUser();
            if (user != null) {
                user.setIsActive(activationDto.isActive());
                userRepo.save(user);
            } else {
                log.warn("Trainer with id: {} user not found", trainer.getId());
            }
        });
        return new ResponseDto<>();
    }

    public ResponseDto<PutTrainerDto> updateTrainer(Authentication authentication, PutTrainerDto trainerDto) {
        if (!userRepo.isAuthenticated(authentication)) {
            log.warn("Request sent without authentication");
            return new ResponseDto<>("User is not authenticated");
        }
        Optional<Trainer> trainerOptional = trainerRepo.findByUserUsername(trainerDto.username());
        if (trainerOptional.isEmpty()) {
            log.warn("Trainee with username {} not found for update", trainerDto.username());
            return new ResponseDto<>("Trainee with username " + trainerDto.username() + " not found for update");
        }

        // Needed fields that can be updated are being updated
        Trainer trainer = trainerOptional.get();
        trainer.getUser().setFirstName(trainerDto.firstName());
        trainer.getUser().setLastName(trainerDto.lastName());
        trainer.getUser().setIsActive(trainerDto.isActive());

        trainerRepo.save(trainer);
        log.info("Trainee updated with id: " + trainer.getId());
        return new ResponseDto<>(trainerMapper.toPutDto(trainer));
    }

    public List<Trainer> getAllTrainers() {
        return trainerRepo.findAll();
    }
}
