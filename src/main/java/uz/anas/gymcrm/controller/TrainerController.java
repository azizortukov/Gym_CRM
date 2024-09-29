package uz.anas.gymcrm.controller;

import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.anas.gymcrm.model.dto.Authentication;
import uz.anas.gymcrm.model.dto.get.GetTrainerDto;
import uz.anas.gymcrm.model.dto.post.PostTrainerDto;
import uz.anas.gymcrm.model.dto.put.PutTrainerDto;
import uz.anas.gymcrm.model.entity.Trainer;
import uz.anas.gymcrm.model.mapper.TrainerMapper;
import uz.anas.gymcrm.service.TrainerService;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/trainer")
@RequiredArgsConstructor
public class TrainerController {

    private final TrainerService trainerService;
    private final TrainerMapper trainerMapper;
    private final Log log = LogFactory.getLog(TrainerController.class);

    @GetMapping("/{username}")
    public ResponseEntity<GetTrainerDto> getTrainerByUsername(@PathVariable String username, @RequestHeader String authUsername, @RequestHeader String authPassword) {
        Optional<Trainer> trainer = trainerService.getTrainerByUsername(new Authentication(authUsername, authPassword), username);
        if (trainer.isPresent()) {

            GetTrainerDto trainerDt0 = trainerMapper.toGetDto(trainer.get());
            return ResponseEntity.ok(trainerDt0);
        } else {
            log.warn("Trainer not found for username " + username);
            return ResponseEntity.notFound().build();
        }
    }

    // I need to check if user is already registered as trainee, they must not register however
    // while saving new trainer, I'm getting firstname and lastname. Multiple people's firstname
    // and lastname can be same even though they're not trainee.
    @PostMapping
    public ResponseEntity<PostTrainerDto> createTrainer(@RequestBody PostTrainerDto trainerDto) {
        Trainer trainer = trainerMapper.toEntity(trainerDto);
        trainerService.createTrainer(trainer);
        return ResponseEntity.ok(trainerMapper.toPostDto(trainer));
    }

    @PutMapping("/{username}")
    public ResponseEntity<PutTrainerDto> updateTrainer(@RequestBody PutTrainerDto trainerDto, @RequestHeader String authUsername, @RequestHeader String authPassword) {
        Trainer trainer = trainerService.updateTrainer(new Authentication(authUsername, authPassword), trainerDto);
        return ResponseEntity.ok(trainerMapper.toPutDto(trainer));
    }

}
