package uz.anas.gymcrm.controller;

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
public class TrainerController {

    private final TrainerService trainerService;

    @GetMapping("/{username}")
    public ResponseDto<GetTrainerDto> getTrainerByUsername(@PathVariable String username, @RequestHeader String authUsername, @RequestHeader String authPassword) {
        return trainerService.getTrainerByUsername(new Authentication(authUsername, authPassword), username);
    }

    // I need to check if user is already registered as trainee, they must not register however
    // while saving new trainer, I'm getting firstname and lastname. Multiple people's firstname
    // and lastname can be same even though they're not trainee.
    @PostMapping
    public ResponseDto<PostTrainerDto> createTrainer(@RequestBody PostTrainerDto trainerDto) {
        return trainerService.createTrainer(trainerDto);
    }

    @PutMapping("/{username}")
    public ResponseDto<PutTrainerDto> updateTrainer(@RequestBody PutTrainerDto trainerDto, @RequestHeader String authUsername, @RequestHeader String authPassword) {
        return trainerService.updateTrainer(new Authentication(authUsername, authPassword), trainerDto);
    }

    @PatchMapping
    public ResponseDto<?> activationDeactivation(
            @RequestBody TraineeActivationDto activationDto,
            @RequestHeader String authUsername,
            @RequestHeader String authPassword
    ) {
        return trainerService.activateTrainerByUsername(new Authentication(authUsername, authPassword), activationDto);
    }

}
