package uz.anas.gymcrm.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.anas.gymcrm.model.dto.Authentication;
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
    public ResponseEntity<List<GetTraineeTrainingDto>> getTraineeTrainingsByUsername(
            @PathVariable String traineeUsername,
            @RequestParam (required = false) Date from,
            @RequestParam (required = false) Date to,
            @RequestParam (required = false) String trainerUsername,
            @RequestParam (required = false) String trainingType,
            @RequestHeader String authUsername,
            @RequestHeader String authPassword
    ) {
        var res = trainingService.getTraineeTrainings(new Authentication(authUsername, authPassword),
                traineeUsername, trainerUsername, from, to, trainingType);

        return ResponseEntity.ok(res);
    }


    @GetMapping("/trainer/{trainerUsername}")
    public ResponseEntity<List<GetTrainerTrainingDto>> getTrainerTrainingsByUsername(
            @PathVariable String trainerUsername,
            @RequestParam (required = false) Date from,
            @RequestParam (required = false) Date to,
            @RequestParam (required = false) String traineeUsername,
            @RequestHeader String authUsername,
            @RequestHeader String authPassword
    ) {
        var res = trainingService.getTrainerTrainings(new Authentication(authUsername, authPassword),
                trainerUsername, traineeUsername, from, to);

        return ResponseEntity.ok(res);
    }

    @GetMapping("/types")
    public ResponseEntity<List<TrainingType>> getTrainingTypes(
            @RequestHeader String authUsername,
            @RequestHeader String authPassword
    ) {
        var res = trainingService.getTrainingTypes(new Authentication(authUsername, authPassword));
        return ResponseEntity.ok(res);
    }

    @PostMapping
    public ResponseEntity<?> createTrainer(
            @RequestBody PostTrainingDto postTrainingDto,
            @RequestHeader String authUsername,
            @RequestHeader String authPassword
    ) {
        trainingService.createTraining(new Authentication(authUsername, authPassword), postTrainingDto);
        return ResponseEntity.ok().build();
    }

}
