package uz.anas.gymcrm.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.anas.gymcrm.model.dto.Authentication;
import uz.anas.gymcrm.model.dto.TraineeDto;
import uz.anas.gymcrm.model.dto.TraineePartialDto;
import uz.anas.gymcrm.model.entity.Trainee;
import uz.anas.gymcrm.model.mapper.TraineeMapper;
import uz.anas.gymcrm.service.TraineeService;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/trainee")
@RequiredArgsConstructor
public class TraineeController {

    private final TraineeService traineeService;
    private final TraineeMapper traineeMapper;
    private final Log log = LogFactory.getLog(TraineeController.class);

    @GetMapping("/{username}")
    public ResponseEntity<TraineePartialDto> getTraineeByUsername(@PathVariable String username, @RequestHeader String authUsername, @RequestHeader String authPassword) {
        Optional<Trainee> trainee = traineeService.getByUsername(new Authentication(authUsername, authPassword), username);
        if (trainee.isPresent()) {

            TraineePartialDto resDto = traineeMapper.toPartialDto(trainee.get());
            return ResponseEntity.ok(resDto);
        } else {
            log.warn("Trainee not found for username " + username);
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<TraineePartialDto> createTrainee(@RequestBody @Valid TraineePartialDto traineePartialDto) {
        Trainee trainee = traineeMapper.toEntityPartialDto(traineePartialDto);
        traineeService.create(trainee);
        return ResponseEntity.ok(traineeMapper.toPartialDto(trainee));
    }

    @PutMapping
    public ResponseEntity<TraineeDto> updateTrainee(@RequestBody @Valid TraineeDto traineeDto, @RequestHeader String authUsername, @RequestHeader String authPassword) {
        Trainee trainee = traineeService.update(new Authentication(authUsername, authPassword), traineeDto);
        return ResponseEntity.ok(traineeMapper.toDto(trainee));
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<Object> deleteTrainee(@PathVariable String username, @RequestHeader String authUsername, @RequestHeader String authPassword) {
        traineeService.deleteByUsername(new Authentication(authUsername, authPassword), username);
        return ResponseEntity.ok().build();
    }


}
