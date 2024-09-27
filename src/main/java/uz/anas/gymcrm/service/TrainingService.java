package uz.anas.gymcrm.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import uz.anas.gymcrm.model.dto.Authentication;
import uz.anas.gymcrm.model.entity.Training;
import uz.anas.gymcrm.repository.TrainingRepository;
import uz.anas.gymcrm.repository.UserRepository;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TrainingService {

    private final TrainingRepository trainingRepo;
    private final UserRepository userRepo;
    private final Log log = LogFactory.getLog(TrainingService.class.getName());

    public Training createTraining(@NotNull Authentication authentication, @Valid Training training) {
        if (!userRepo.isAuthenticated(authentication)) {
            log.warn("Request sent without authentication");
            throw new RuntimeException("User is not authenticated");
        }
        return trainingRepo.save(training);
    }

    public List<Training> getTraineeTrainings(
            @NotNull Authentication authentication , @NotNull String traineeUsername, String trainerUsername,
            Date fromDate, Date toDate, String trainingType) {

        if (!userRepo.isAuthenticated(authentication)) {
            log.warn("Request sent without authentication");
            throw new RuntimeException("User is not authenticated");
        }
        return trainingRepo.findByTraineeAndCriteria(traineeUsername, trainerUsername, fromDate, toDate, trainingType);
    }

    public List<Training> getTrainerTrainings(
            @NotNull Authentication authentication , @NotNull String trainerUsername, String traineeUsername,
            Date fromDate, Date toDate) {

        if (!userRepo.isAuthenticated(authentication)) {
            log.warn("Request sent without authentication");
            throw new RuntimeException("User is not authenticated");

        }
        return trainingRepo.findByTrainerAndCriteria(trainerUsername, traineeUsername, fromDate, toDate);
    }

}
