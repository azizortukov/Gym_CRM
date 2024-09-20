package uz.anas.gymcrm.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uz.anas.gymcrm.entity.Training;
import uz.anas.gymcrm.entity.User;
import uz.anas.gymcrm.repo.TrainingRepo;
import uz.anas.gymcrm.repo.UserRepo;

import java.sql.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainingServiceTest {


    @Mock
    private TrainingRepo trainingRepo;
    @Mock
    private UserRepo userRepo;
    @InjectMocks
    private TrainingService trainingService;

    @Test
    public void createTrainingSuccess() {
        User authUser = new User();
        Training training = new Training();
        when(userRepo.isAuthenticated(authUser))
                .thenReturn(true);
        when(trainingRepo.save(training))
                .thenReturn(training);

        Training result = trainingService.createTraining(authUser, training);

        assertEquals(training, result);
        verify(trainingRepo, times(1)).save(training);
    }

    @Test
    public void createTrainingUserNotAuthenticated() {
        User authUser = new User();
        Training training = new Training();
        when(userRepo.isAuthenticated(authUser))
                .thenReturn(false);

        assertThrows(RuntimeException.class, () -> {
            trainingService.createTraining(authUser, training);
        });
        verify(trainingRepo, never()).save(any(Training.class));
    }

    @Test
    public void getTraineeTrainingsSuccess() {
        User authUser = new User();
        String traineeUsername = "trainee1";
        List<Training> trainings = List.of(new Training(), new Training());
        when(userRepo.isAuthenticated(authUser))
                .thenReturn(true);
        when(trainingRepo.findByTraineeAndCriteria(traineeUsername, null, null, null, null))
                .thenReturn(trainings);

        List<Training> result = trainingService.getTraineeTrainings(authUser, traineeUsername, null, null, null, null);

        assertEquals(2, result.size());
        verify(trainingRepo, times(1)).findByTraineeAndCriteria(traineeUsername, null, null, null, null);
    }

    @Test
    public void getTraineeTrainingsUserNotAuthenticated() {
        User authUser = new User();
        String traineeUsername = "trainee1";
        when(userRepo.isAuthenticated(authUser))
                .thenReturn(false);
        assertThrows(RuntimeException.class, () -> {
            trainingService.getTraineeTrainings(authUser, traineeUsername, null, null, null, null);
        });
        verify(trainingRepo, never()).findByTraineeAndCriteria(anyString(), anyString(), any(Date.class), any(Date.class), anyString());
    }

    @Test
    public void getTrainerTrainingsSuccess() {
        User authUser = new User();
        String trainerUsername = "trainer1";
        List<Training> trainings = List.of(new Training(), new Training());
        when(userRepo.isAuthenticated(authUser))
                .thenReturn(true);
        when(trainingRepo.findByTrainerAndCriteria(trainerUsername, null, null, null))
                .thenReturn(trainings);

        List<Training> result = trainingService.getTrainerTrainings(authUser, trainerUsername, null, null, null);

        assertEquals(2, result.size());
        verify(trainingRepo, times(1)).findByTrainerAndCriteria(trainerUsername, null, null, null);
    }

    @Test
    public void getTrainerTrainingsUserNotAuthenticated() {
        User authUser = new User();
        String trainerUsername = "trainer1";
        when(userRepo.isAuthenticated(authUser))
                .thenReturn(false);

        assertThrows(RuntimeException.class, () -> {
            trainingService.getTrainerTrainings(authUser, trainerUsername, null, null, null);
        });

        verify(trainingRepo, never()).findByTrainerAndCriteria(anyString(), anyString(), any(Date.class), any(Date.class));
    }

}