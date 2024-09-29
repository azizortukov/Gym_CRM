package uz.anas.gymcrm.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.anas.gymcrm.model.dto.Authentication;
import uz.anas.gymcrm.model.dto.TrainerBaseDto;
import uz.anas.gymcrm.model.dto.get.GetTraineeDto;
import uz.anas.gymcrm.model.dto.patch.TraineeActivationDto;
import uz.anas.gymcrm.model.dto.post.PostTraineeDto;
import uz.anas.gymcrm.model.dto.put.PutTraineeDto;
import uz.anas.gymcrm.model.entity.Trainee;
import uz.anas.gymcrm.model.mapper.TraineeMapper;
import uz.anas.gymcrm.service.TraineeService;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/trainee")
@RequiredArgsConstructor
public class TraineeController {

    private final TraineeService traineeService;
    private final TraineeMapper traineeMapper;
    private final Log log = LogFactory.getLog(TraineeController.class);

    @GetMapping("/{username}")
    public ResponseEntity<GetTraineeDto> getTraineeByUsername(
            @PathVariable String username,
            @RequestHeader String authUsername,
            @RequestHeader String authPassword
    ) {
        Optional<Trainee> trainee = traineeService.getByUsername(new Authentication(authUsername, authPassword), username);
        if (trainee.isPresent()) {
            GetTraineeDto traineeDto = traineeMapper.toGetDto(trainee.get());
            return ResponseEntity.ok(traineeDto);
        } else {
            log.warn("Trainee not found for username " + username);
            return ResponseEntity.notFound().build();
        }
    }

    // I need to check if user is already registered as trainer, they must not register however
    // while saving new trainee, I'm getting firstname and lastname. Multiple people's firstname
    // and lastname can be same even though they're not trainer.
    @PostMapping
    public ResponseEntity<PostTraineeDto> createTrainee(@RequestBody @Valid PostTraineeDto traineeDto) {
        Trainee trainee = traineeMapper.toEntity(traineeDto);
        trainee = traineeService.create(trainee);
        return ResponseEntity.ok(traineeMapper.toPostDto(trainee));
    }

    @PutMapping
    public ResponseEntity<PutTraineeDto> updateTrainee(
            @RequestBody @Valid PutTraineeDto traineeDto,
            @RequestHeader String authUsername,
            @RequestHeader String authPassword
    ) {
        Trainee trainee = traineeService.update(new Authentication(authUsername, authPassword), traineeDto);
        return ResponseEntity.ok(traineeMapper.toPutDto(trainee));
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<?> deleteTrainee(
            @PathVariable String username,
            @RequestHeader String authUsername,
            @RequestHeader String authPassword
    ) {
        traineeService.deleteByUsername(new Authentication(authUsername, authPassword), username);
        return ResponseEntity.ok().build();
    }

    @PatchMapping
    public ResponseEntity<?> activationDeactivation(
            @RequestBody TraineeActivationDto activationDto,
            @RequestHeader String authUsername,
            @RequestHeader String authPassword
    ) {
        traineeService.activateTraineeByUsername(new Authentication(authUsername, authPassword), activationDto.username());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/not_assigned/{username}")
    public ResponseEntity<List<TrainerBaseDto>> getNotActiveTrainer(
            @PathVariable String username,
            @RequestHeader String authUsername,
            @RequestHeader String authPassword
    ) {
        var trainers = traineeService.getTrainersByNotAssigned(new Authentication(authUsername, authPassword), username);
        return ResponseEntity.ok(trainers);
    }

    // This endpoint replaces all trainee's trainer list with given trainers' list. If given list of trainer usernames
    // is empty, then trainee's trainer list becomes empty
    @PutMapping("/trainer_list/{username}")
    public ResponseEntity<List<TrainerBaseDto>> updateTrainerList(
            @PathVariable String username,
            @RequestBody Set<TrainerBaseDto> trainerDtos,
            @RequestHeader String authUsername,
            @RequestHeader String authPassword
    ) {

        var ans = traineeService.updateTrainerList(new Authentication(authUsername, authPassword), username, trainerDtos);

        return ResponseEntity.ok(ans);
    }

}
