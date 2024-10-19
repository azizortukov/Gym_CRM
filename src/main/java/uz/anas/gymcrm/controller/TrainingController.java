package uz.anas.gymcrm.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Training APIs")
public class TrainingController {

    private final TrainingService trainingService;

    @Operation(summary = "Getting trainee's training list by username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = """
                    The list of training will be provided in data field. In case of exceptions,
                    error message is provided.""",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDto.class)))
    })
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

    @Operation(summary = "Getting trainer's training list by username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = """
                    The list of training will be provided in data field. In case of exceptions,
                    error message is provided.""",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDto.class)))
    })
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

    @Operation(summary = "Getting list of training types")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = """
                    The list of training types will be provided in data field. In case of exceptions,
                    error message is provided.""",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDto.class)))
    })
    @GetMapping("/types")
    public ResponseDto<List<TrainingType>> getTrainingTypes(
            @RequestHeader String authUsername,
            @RequestHeader String authPassword
    ) {
        return trainingService.getTrainingTypes(new Authentication(authUsername, authPassword));
    }

    @Operation(summary = "Creating new training")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = """
                    If training will be saved successfully data will be empty. In case of exceptions,
                    error message is provided.""",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDto.class)))
    })
    @PostMapping
    public ResponseDto<?> createTraining(
            @RequestBody PostTrainingDto postTrainingDto,
            @RequestHeader String authUsername,
            @RequestHeader String authPassword
    ) {
        return trainingService.createTraining(new Authentication(authUsername, authPassword), postTrainingDto);
    }

}
