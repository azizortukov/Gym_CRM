package uz.anas.gymcrm.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import uz.anas.gymcrm.entity.Training;
import uz.anas.gymcrm.entity.User;
import uz.anas.gymcrm.repo.TrainingRepo;
import uz.anas.gymcrm.repo.UserRepo;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TrainingService {

    private final TrainingRepo trainingRepo;
    private final UserRepo userRepo;
    private final Log log = LogFactory.getLog(TrainingService.class.getName());

    public Training createTraining(@NotNull User authentication, @Valid Training training) {
        if (!userRepo.isAuthenticated(authentication)) {
            log.warn("Request sent without authentication");
            throw new RuntimeException("User is not authenticated");
        }
        return trainingRepo.save(training);
    }

    public List<Training> getTraineeTrainings(
            @NotNull User authentication , @NotNull String traineeUsername, String trainerUsername,
            Date fromDate, Date toDate, String trainingType) {

        if (!userRepo.isAuthenticated(authentication)) {
            log.warn("Request sent without authentication");
            throw new RuntimeException("User is not authenticated");
        }
        return trainingRepo.findByTraineeAndCriteria(traineeUsername, trainerUsername, fromDate, toDate, trainingType);
    }

    public List<Training> getTrainerTrainings(
            @NotNull User authentication , @NotNull String trainerUsername, String traineeUsername,
            Date fromDate, Date toDate) {

        if (!userRepo.isAuthenticated(authentication)) {
            log.warn("Request sent without authentication");
            throw new RuntimeException("User is not authenticated");

        }
        return trainingRepo.findByTrainerAndCriteria(trainerUsername, traineeUsername, fromDate, toDate);
    }

}
