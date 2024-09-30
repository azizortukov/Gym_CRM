package uz.anas.gymcrm.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import uz.anas.gymcrm.model.dto.Authentication;
import uz.anas.gymcrm.model.dto.ResponseDto;
import uz.anas.gymcrm.model.dto.TrainerBaseDto;
import uz.anas.gymcrm.model.dto.get.GetTraineeDto;
import uz.anas.gymcrm.model.dto.patch.TraineeActivationDto;
import uz.anas.gymcrm.model.dto.post.PostTraineeDto;
import uz.anas.gymcrm.model.dto.put.PutTraineeDto;
import uz.anas.gymcrm.service.TraineeService;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/trainee")
@RequiredArgsConstructor
public class TraineeController {

    private final TraineeService traineeService;

    @GetMapping("/{username}")
    public ResponseDto<GetTraineeDto> getTraineeByUsername(
            @PathVariable String username,
            @RequestHeader String authUsername,
            @RequestHeader String authPassword
    ) {
        return traineeService.getByUsername(new Authentication(authUsername, authPassword), username);
    }

    // I need to check if user is already registered as trainer, they must not register however
    // while saving new trainee, I'm getting firstname and lastname. Multiple people's firstname
    // and lastname can be same even though they're not trainer.
    @PostMapping
    public ResponseDto<PostTraineeDto> createTrainee(@RequestBody @Valid PostTraineeDto traineeDto) {
        return traineeService.create(traineeDto);
    }

    @PutMapping
    public ResponseDto<PutTraineeDto> updateTrainee(
            @RequestBody @Valid PutTraineeDto traineeDto,
            @RequestHeader String authUsername,
            @RequestHeader String authPassword
    ) {
        return traineeService.update(new Authentication(authUsername, authPassword), traineeDto);
    }

    @DeleteMapping("/{username}")
    public ResponseDto<?> deleteTrainee(
            @PathVariable String username,
            @RequestHeader String authUsername,
            @RequestHeader String authPassword
    ) {
        return traineeService.deleteByUsername(new Authentication(authUsername, authPassword), username);
    }

    @PatchMapping
    public ResponseDto<?> activationDeactivation(
            @RequestBody TraineeActivationDto activationDto,
            @RequestHeader String authUsername,
            @RequestHeader String authPassword
    ) {
        return traineeService.activateTraineeByUsername(new Authentication(authUsername, authPassword), activationDto);
    }

    @GetMapping("/not_assigned/{username}")
    public ResponseDto<List<TrainerBaseDto>> getNotActiveTrainer(
            @PathVariable String username,
            @RequestHeader String authUsername,
            @RequestHeader String authPassword
    ) {
        return traineeService.getTrainersByNotAssigned(new Authentication(authUsername, authPassword), username);
    }

    // This endpoint replaces all trainee's trainer list with given trainers' list. If given list of trainer usernames
    // is empty, then trainee's trainer list becomes empty
    @PutMapping("/trainer_list/{username}")
    public ResponseDto<List<TrainerBaseDto>> updateTrainerList(
            @PathVariable String username,
            @RequestBody Set<TrainerBaseDto> trainerDtos,
            @RequestHeader String authUsername,
            @RequestHeader String authPassword
    ) {
        return traineeService.updateTrainerList(new Authentication(authUsername, authPassword), username, trainerDtos);
    }

}
