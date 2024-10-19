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
import uz.anas.gymcrm.model.dto.get.GetTrainerDto;
import uz.anas.gymcrm.model.dto.patch.TraineeActivationDto;
import uz.anas.gymcrm.model.dto.post.PostTrainerDto;
import uz.anas.gymcrm.model.dto.put.PutTrainerDto;
import uz.anas.gymcrm.service.TrainerService;

@RestController
@RequestMapping("/api/v1/trainer")
@RequiredArgsConstructor
@Tag(name = "Trainer APIs")
public class TrainerController {

    private final TrainerService trainerService;

    @GetMapping("/{username}")
    public ResponseDto<GetTrainerDto> getTrainerByUsername(@PathVariable String username, @RequestHeader String authUsername, @RequestHeader String authPassword) {
        return trainerService.getTrainerByUsername(new Authentication(authUsername, authPassword), username);
    }

    /* I need to check if user is already registered as trainee, they must not register however
     * while saving new trainer, I'm getting firstname and lastname. Multiple people's firstname
     * and lastname can be same even though they're not trainee. */
    @Operation(summary = "Creating new trainer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = """
                    If new trainer is saved, data field will be provided. In case of exception,
                    error message is provided.""",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDto.class)))
    })
    @PostMapping
    public ResponseDto<PostTrainerDto> createTrainer(@RequestBody PostTrainerDto trainerDto) {
        return trainerService.createTrainer(trainerDto);
    }

    @Operation(summary = "Updating trainer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = """
                    If trainer is found by provided username, containing fields will be updated and data with new
                    info will be provided. Otherwise, error message is provided.""",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDto.class)))
    })
    @PutMapping("/{username}")
    public ResponseDto<PutTrainerDto> updateTrainer(@RequestBody PutTrainerDto trainerDto, @RequestHeader String authUsername, @RequestHeader String authPassword) {
        return trainerService.updateTrainer(new Authentication(authUsername, authPassword), trainerDto);
    }

    @Operation(summary = "Patching trainer's status by username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = """
                    If trainer's status is successfully updated, data will be empty. Otherwise,
                    error message is provided.""",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDto.class)))
    })
    @PatchMapping
    public ResponseDto<?> activationDeactivation(
            @RequestBody TraineeActivationDto activationDto,
            @RequestHeader String authUsername,
            @RequestHeader String authPassword
    ) {
        return trainerService.activateTrainerByUsername(new Authentication(authUsername, authPassword), activationDto);
    }

}
