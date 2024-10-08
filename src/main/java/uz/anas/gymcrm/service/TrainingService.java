package uz.anas.gymcrm.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.anas.gymcrm.model.dto.Authentication;
import uz.anas.gymcrm.model.dto.ResponseDto;
import uz.anas.gymcrm.model.dto.get.GetTraineeTrainingDto;
import uz.anas.gymcrm.model.dto.get.GetTrainerTrainingDto;
import uz.anas.gymcrm.model.dto.post.PostTrainingDto;
import uz.anas.gymcrm.model.entity.Training;
import uz.anas.gymcrm.model.entity.TrainingType;
import uz.anas.gymcrm.model.mapper.TrainingMapper;
import uz.anas.gymcrm.repository.TrainingRepository;
import uz.anas.gymcrm.repository.UserRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TrainingService {

    private final TrainingRepository trainingRepo;
    private final UserRepository userRepo;
    private final TrainingMapper trainingMapper;

    public ResponseDto<?> createTraining(@NotNull Authentication authentication, @Valid PostTrainingDto postTrainingDto) {
        if (!userRepo.isAuthenticated(authentication)) {
            log.warn("Request sent without authentication");
            return new ResponseDto<>("User is not authenticated");
        }
        Training training = trainingMapper.toEntity(postTrainingDto);
        trainingRepo.save(training);
        return new ResponseDto<>();
    }

    public ResponseDto<List<GetTraineeTrainingDto>> getTraineeTrainings(
            @NotNull Authentication authentication , @NotNull String traineeUsername, String trainerUsername,
            Date fromDate, Date toDate, String trainingType
    ) {

        if (!userRepo.isAuthenticated(authentication)) {
            log.warn("Request sent without authentication");
            return new ResponseDto<>("User is not authenticated");
        }
        var trainings = trainingRepo.findByTraineeAndCriteria(traineeUsername, trainerUsername, fromDate, toDate, trainingType);
        List<GetTraineeTrainingDto> resDtos = new ArrayList<>();
        for (Training training : trainings) {
            resDtos.add(trainingMapper.toTraineeTrainingDto(training));
        }
        return new ResponseDto<>(resDtos);
    }

    public ResponseDto<List<GetTrainerTrainingDto>> getTrainerTrainings(
            @NotNull Authentication authentication , @NotNull String trainerUsername, String traineeUsername,
            Date fromDate, Date toDate
    ) {
        if (!userRepo.isAuthenticated(authentication)) {
            log.warn("Request sent without authentication");
            return new ResponseDto<>("User is not authenticated");

        }
        var trainings = trainingRepo.findByTrainerAndCriteria(trainerUsername, traineeUsername, fromDate, toDate);
        List<GetTrainerTrainingDto> resDtos = new ArrayList<>();
        for (Training training : trainings) {
            resDtos.add(trainingMapper.toTrainerTrainingDto(training));
        }
        return new ResponseDto<>(resDtos);
    }

    public ResponseDto<List<TrainingType>> getTrainingTypes(Authentication authentication) {
        if (!userRepo.isAuthenticated(authentication)) {
            log.warn("Request sent without authentication");
            return new ResponseDto<>("User is not authenticated");

        }

        return new ResponseDto<>(trainingRepo.findAllTrainingTypes());
    }

}