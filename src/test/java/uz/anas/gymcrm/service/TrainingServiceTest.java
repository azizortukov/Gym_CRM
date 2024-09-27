package uz.anas.gymcrm.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uz.anas.gymcrm.model.dto.Authentication;
import uz.anas.gymcrm.model.entity.Training;
import uz.anas.gymcrm.repository.TrainingRepository;
import uz.anas.gymcrm.repository.UserRepository;

import java.sql.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainingServiceTest {


    @Mock
    private TrainingRepository trainingRepo;
    @Mock
    private UserRepository userRepo;
    @InjectMocks
    private TrainingService trainingService;

    @Test
    public void createTrainingSuccess() {
        Authentication auth = new Authentication("Tom.Anderson", "password111");
        Training training = new Training();
        when(userRepo.isAuthenticated(auth))
                .thenReturn(true);
        when(trainingRepo.save(training))
                .thenReturn(training);

        Training result = trainingService.createTraining(auth, training);

        assertEquals(training, result);
        verify(trainingRepo, times(1)).save(training);
    }

    @Test
    public void createTrainingUserNotAuthenticated() {
        Authentication auth = new Authentication("Tom.Anderson", "password111");
        Training training = new Training();
        when(userRepo.isAuthenticated(auth))
                .thenReturn(false);

        assertThrows(RuntimeException.class, () -> {
            trainingService.createTraining(auth, training);
        });
        verify(trainingRepo, never()).save(any(Training.class));
    }

    @Test
    public void getTraineeTrainingsSuccess() {
        Authentication auth = new Authentication("Tom.Anderson", "password111");
        String traineeUsername = "trainee1";
        List<Training> trainings = List.of(new Training(), new Training());
        when(userRepo.isAuthenticated(auth))
                .thenReturn(true);
        when(trainingRepo.findByTraineeAndCriteria(traineeUsername, null, null, null, null))
                .thenReturn(trainings);

        List<Training> result = trainingService.getTraineeTrainings(auth, traineeUsername, null, null, null, null);

        assertEquals(2, result.size());
        verify(trainingRepo, times(1)).findByTraineeAndCriteria(traineeUsername, null, null, null, null);
    }

    @Test
    public void getTraineeTrainingsUserNotAuthenticated() {
        Authentication auth = new Authentication("Tom.Anderson", "password111");
        String traineeUsername = "trainee1";
        when(userRepo.isAuthenticated(auth))
                .thenReturn(false);
        assertThrows(RuntimeException.class, () -> {
            trainingService.getTraineeTrainings(auth, traineeUsername, null, null, null, null);
        });
        verify(trainingRepo, never()).findByTraineeAndCriteria(anyString(), anyString(), any(Date.class), any(Date.class), anyString());
    }

    @Test
    public void getTrainerTrainingsSuccess() {
        Authentication auth = new Authentication("Tom.Anderson", "password111");
        String trainerUsername = "trainer1";
        List<Training> trainings = List.of(new Training(), new Training());
        when(userRepo.isAuthenticated(auth))
                .thenReturn(true);
        when(trainingRepo.findByTrainerAndCriteria(trainerUsername, null, null, null))
                .thenReturn(trainings);

        List<Training> result = trainingService.getTrainerTrainings(auth, trainerUsername, null, null, null);

        assertEquals(2, result.size());
        verify(trainingRepo, times(1)).findByTrainerAndCriteria(trainerUsername, null, null, null);
    }

    @Test
    public void getTrainerTrainingsUserNotAuthenticated() {
        Authentication auth = new Authentication("Tom.Anderson", "password111");
        String trainerUsername = "trainer1";
        when(userRepo.isAuthenticated(auth))
                .thenReturn(false);

        assertThrows(RuntimeException.class, () -> {
            trainingService.getTrainerTrainings(auth, trainerUsername, null, null, null);
        });

        verify(trainingRepo, never()).findByTrainerAndCriteria(anyString(), anyString(), any(Date.class), any(Date.class));
    }

}