package uz.anas.gymcrm.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Trainee APIs")
public class TraineeController {

    private final TraineeService traineeService;

    @Operation(summary = "Getting trainee by username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = """
                    If trainee is found, response will be provided in data field. Otherwise,
                    error message is provided.""",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDto.class)))
    })
    @GetMapping("/{username}")
    public ResponseDto<GetTraineeDto> getTraineeByUsername(
            @PathVariable String username,
            @RequestHeader String authUsername,
            @RequestHeader String authPassword
    ) {
        return traineeService.getByUsername(new Authentication(authUsername, authPassword), username);
    }

    /* I need to check if user is already registered as trainer, they must not register however
     * while saving new trainee, I'm getting firstname and lastname. Multiple people's firstname
     * and lastname can be same even though they're not trainer. */
    @Operation(summary = "Creating new trainee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = """
                    If trainee is successfully created, response will be provided in data field. Otherwise,
                    error message is provided.""",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDto.class)))
    })
    @PostMapping
    public ResponseDto<PostTraineeDto> createTrainee(@RequestBody @Valid PostTraineeDto traineeDto) {
        return traineeService.create(traineeDto);
    }

    @Operation(summary = "Updating trainee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = """
                    If trainee is found by provided username, containing fields will be updated and data with new
                    info will be provided. Otherwise, error message is provided.""",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDto.class)))
    })
    @PutMapping
    public ResponseDto<PutTraineeDto> updateTrainee(
            @RequestBody @Valid PutTraineeDto traineeDto,
            @RequestHeader String authUsername,
            @RequestHeader String authPassword
    ) {
        return traineeService.update(new Authentication(authUsername, authPassword), traineeDto);
    }


    @Operation(summary = "Deleting trainee by username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = """
                    If trainee is successfully deleted, data will be empty. Otherwise,
                    error message is provided.""",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDto.class)))
    })
    @DeleteMapping("/{username}")
    public ResponseDto<?> deleteTrainee(
            @PathVariable String username,
            @RequestHeader String authUsername,
            @RequestHeader String authPassword
    ) {
        return traineeService.deleteByUsername(new Authentication(authUsername, authPassword), username);
    }

    @Operation(summary = "Patching trainee's status by username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = """
                    If trainee's status is successfully updated, data will be empty. Otherwise,
                    error message is provided.""",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDto.class)))
    })
    @PatchMapping
    public ResponseDto<?> activationDeactivation(
            @RequestBody TraineeActivationDto activationDto,
            @RequestHeader String authUsername,
            @RequestHeader String authPassword
    ) {
        return traineeService.activateTraineeByUsername(new Authentication(authUsername, authPassword), activationDto);
    }

    @Operation(summary = "Getting trainee's not assigned active trainers by trainee username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = """
                    Trainee's not assigned active trainers list will be provided. In case of exception,
                    error message is provided.""",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDto.class)))
    })
    @GetMapping("/not_assigned/{username}")
    public ResponseDto<List<TrainerBaseDto>> getNotActiveTrainer(
            @PathVariable String username,
            @RequestHeader String authUsername,
            @RequestHeader String authPassword
    ) {
        return traineeService.getTrainersByNotAssigned(new Authentication(authUsername, authPassword), username);
    }

    /* This endpoint replaces all trainee's trainer list with given trainers' list. If given list of trainer usernames
     * is empty, then trainee's trainer list becomes empty */
    @Operation(summary = "Getting trainee's trainer list by trainee username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = """
                    Trainee's assigned trainers list will be provided. In case of exception,
                    error message is provided.""",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDto.class)))
    })
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
