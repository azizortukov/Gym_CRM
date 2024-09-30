package uz.anas.gymcrm.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import uz.anas.gymcrm.model.dto.Authentication;
import uz.anas.gymcrm.model.dto.ResponseDto;
import uz.anas.gymcrm.model.dto.get.GetTraineeTrainingDto;
import uz.anas.gymcrm.model.dto.get.GetTrainerTrainingDto;
import uz.anas.gymcrm.model.dto.post.PostTrainingDto;
import uz.anas.gymcrm.model.entity.TrainingType;
import uz.anas.gymcrm.service.TrainingService;

import java.sql.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1/training")
@RequiredArgsConstructor
public class TrainingController {

    private final TrainingService trainingService;

    @GetMapping("/trainee/{traineeUsername}")
    public ResponseDto<List<GetTraineeTrainingDto>> getTraineeTrainingsByUsername(
            @PathVariable String traineeUsername,
            @RequestParam (required = false) Date from,
            @RequestParam (required = false) Date to,
            @RequestParam (required = false) String trainerUsername,
            @RequestParam (required = false) String trainingType,
            @RequestHeader String authUsername,
            @RequestHeader String authPassword
    ) {
        return trainingService.getTraineeTrainings(new Authentication(authUsername, authPassword),
                traineeUsername, trainerUsername, from, to, trainingType);
    }


    @GetMapping("/trainer/{trainerUsername}")
    public ResponseDto<List<GetTrainerTrainingDto>> getTrainerTrainingsByUsername(
            @PathVariable String trainerUsername,
            @RequestParam (required = false) Date from,
            @RequestParam (required = false) Date to,
            @RequestParam (required = false) String traineeUsername,
            @RequestHeader String authUsername,
            @RequestHeader String authPassword
    ) {
        return trainingService.getTrainerTrainings(new Authentication(authUsername, authPassword),
                trainerUsername, traineeUsername, from, to);
    }

    @GetMapping("/types")
    public ResponseDto<List<TrainingType>> getTrainingTypes(
            @RequestHeader String authUsername,
            @RequestHeader String authPassword
    ) {
        return trainingService.getTrainingTypes(new Authentication(authUsername, authPassword));
    }

    @PostMapping
    public ResponseDto<?> createTrainer(
            @RequestBody PostTrainingDto postTrainingDto,
            @RequestHeader String authUsername,
            @RequestHeader String authPassword
    ) {
        return trainingService.createTraining(new Authentication(authUsername, authPassword), postTrainingDto);
    }

}
