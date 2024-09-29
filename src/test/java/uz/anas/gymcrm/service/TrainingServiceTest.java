package uz.anas.gymcrm.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uz.anas.gymcrm.model.dto.Authentication;
import uz.anas.gymcrm.model.dto.post.PostTrainingDto;
import uz.anas.gymcrm.model.entity.Training;
import uz.anas.gymcrm.model.mapper.TrainingMapper;
import uz.anas.gymcrm.repository.TrainingRepository;
import uz.anas.gymcrm.repository.UserRepository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainingServiceTest {


    @Mock
    private TrainingRepository trainingRepo;
    @Mock
    private UserRepository userRepo;
    @Mock
    private TrainingMapper trainingMapper;
    @InjectMocks
    private TrainingService trainingService;
    Authentication auth;

    @BeforeEach
    void setUp() {
        auth = new Authentication("Tom.Anderson", "password111");
    }

    @Test
    public void createTrainingSuccess() {
        Training training = new Training();
        when(userRepo.isAuthenticated(any(Authentication.class)))
                .thenReturn(true);
        when(trainingRepo.save(any(Training.class)))
                .thenReturn(training);


        trainingService.createTraining(auth,
                new PostTrainingDto("", "", "", Date.valueOf(LocalDate.of(2020, 4,23)), 20));

        verify(trainingRepo, times(1)).save(training);
    }

    @Test
    public void createTrainingUserNotAuthenticated() {
        Training training = new Training();
        when(userRepo.isAuthenticated(auth))
                .thenReturn(false);

        assertThrows(RuntimeException.class, () -> trainingService.createTraining(auth, new PostTrainingDto("", "", "", Date.valueOf(LocalDate.of(2020, 04,23)), 20)));
        verify(trainingRepo, never()).save(any(Training.class));
    }

    @Test
    public void getTraineeTrainingsSuccess() {
        String traineeUsername = "trainee1";
        List<Training> trainings = List.of(new Training(), new Training());
        when(userRepo.isAuthenticated(auth))
                .thenReturn(true);
        when(trainingRepo.findByTraineeAndCriteria(traineeUsername, null, null, null, null))
                .thenReturn(trainings);

        trainingService.getTraineeTrainings(auth, traineeUsername, null, null, null, null);

        verify(trainingRepo, times(1)).findByTraineeAndCriteria(traineeUsername, null, null, null, null);
    }

    @Test
    public void getTraineeTrainingsUserNotAuthenticated() {
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
        String trainerUsername = "trainer1";
        List<Training> trainings = List.of(new Training(), new Training());
        when(userRepo.isAuthenticated(auth))
                .thenReturn(true);
        when(trainingRepo.findByTrainerAndCriteria(trainerUsername, null, null, null))
                .thenReturn(trainings);

        trainingService.getTrainerTrainings(auth, trainerUsername, null, null, null);

        verify(trainingRepo, times(1)).findByTrainerAndCriteria(trainerUsername, null, null, null);
    }

    @Test
    public void getTrainerTrainingsUserNotAuthenticated() {
        String trainerUsername = "trainer1";
        when(userRepo.isAuthenticated(auth))
                .thenReturn(false);

        assertThrows(RuntimeException.class, () -> {
            trainingService.getTrainerTrainings(auth, trainerUsername, null, null, null);
        });

        verify(trainingRepo, never()).findByTrainerAndCriteria(anyString(), anyString(), any(Date.class), any(Date.class));
    }

}