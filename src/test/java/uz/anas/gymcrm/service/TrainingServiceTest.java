package uz.anas.gymcrm.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainingServiceTest {

    @Mock
    TrainingRepository trainingRepo;
    @Mock
    UserRepository userRepo;
    @Mock
    TrainingMapper trainingMapper;
    @InjectMocks
    TrainingService trainingService;
    Authentication auth = mock(Authentication.class);
    Date fromDate = new Date(System.currentTimeMillis() - 1000000);
    Date toDate = new Date(System.currentTimeMillis());

    @Test
    void createTraining() {
        PostTrainingDto postTrainingDto = new PostTrainingDto(
                "TrainingName", "trainerUsername", "trainingType",
                new Date(System.currentTimeMillis()), 200);
        Training training = new Training();
        when(userRepo.isAuthenticated(auth))
                .thenReturn(true);
        when(trainingMapper.toEntity(postTrainingDto))
                .thenReturn(training);
        when(trainingRepo.save(training))
                .thenReturn(training);

        ResponseDto<?> response = trainingService.createTraining(auth, postTrainingDto);

        assertNotNull(response);
        verify(trainingRepo).save(training);
    }

    @Test
    void createTrainingNotAuthenticated() {
        when(userRepo.isAuthenticated(auth))
                .thenReturn(false);

        ResponseDto<?> response = trainingService.createTraining(auth, null);

        assertNotNull(response);
        assertEquals("User is not authenticated", response.getErrorMessage());
        verify(userRepo, times(1)).isAuthenticated(auth);
        verifyNoInteractions(trainingRepo);
    }

    @Test
    void getTraineeTrainings() {
        String traineeUsername = "traineeUser";
        String trainerUsername = "trainerUser";
        String trainingType = "Cardio";
        var getTraineeTrainingDto = mock(GetTraineeTrainingDto.class);

        List<Training> trainings = new ArrayList<>();
        Training training = new Training();
        trainings.add(training);

        when(userRepo.isAuthenticated(auth))
                .thenReturn(true);
        when(trainingRepo.findByTraineeAndCriteria(traineeUsername, trainerUsername, fromDate, toDate, trainingType))
                .thenReturn(trainings);
        when(trainingMapper.toTraineeTrainingDto(training))
                .thenReturn(getTraineeTrainingDto);

        ResponseDto<List<GetTraineeTrainingDto>> response = trainingService.getTraineeTrainings(auth, traineeUsername, trainerUsername, fromDate, toDate, trainingType);

        assertNotNull(response);
        assertFalse(response.getData().isEmpty());
        verify(userRepo).isAuthenticated(auth);
        verify(trainingRepo).findByTraineeAndCriteria(traineeUsername, trainerUsername, fromDate, toDate, trainingType);
    }

    @Test
    void getTraineeTrainingsNotAuthenticated() {
        String username = "testUser";

        when(userRepo.isAuthenticated(auth))
                .thenReturn(false);

        ResponseDto<?> response = trainingService.getTraineeTrainings(auth, username, null, null, null, null);

        assertNotNull(response);
        assertEquals("User is not authenticated", response.getErrorMessage());
        verify(userRepo, times(1)).isAuthenticated(auth);
        verifyNoInteractions(trainingRepo);
    }

    @Test
    void getTrainerTrainings() {
        String trainerUsername = "trainerUser";
        String traineeUsername = "traineeUser";
        var getTrainerTrainingDto = mock(GetTrainerTrainingDto.class);

        List<Training> trainings = new ArrayList<>();
        Training training = new Training();
        trainings.add(training);

        when(userRepo.isAuthenticated(auth))
                .thenReturn(true);
        when(trainingRepo.findByTrainerAndCriteria(trainerUsername, traineeUsername, fromDate, toDate))
                .thenReturn(trainings);
        when(trainingMapper.toTrainerTrainingDto(training))
                .thenReturn(getTrainerTrainingDto);

        ResponseDto<List<GetTrainerTrainingDto>> response = trainingService.getTrainerTrainings(auth, trainerUsername, traineeUsername, fromDate, toDate);

        assertNotNull(response);
        assertFalse(response.getData().isEmpty());
        verify(userRepo).isAuthenticated(auth);
        verify(trainingRepo).findByTrainerAndCriteria(trainerUsername, traineeUsername, fromDate, toDate);
    }

    @Test
    void getTrainerTrainingsNotAuthenticated() {
        String trainerUsername = "trainerUser";

        when(userRepo.isAuthenticated(auth))
                .thenReturn(false);

        ResponseDto<List<GetTrainerTrainingDto>> response = trainingService.getTrainerTrainings(auth, trainerUsername, null, null, null);

        assertNotNull(response);
        assertEquals("User is not authenticated", response.getErrorMessage());
        verifyNoInteractions(trainingRepo);
    }

    @Test
    void getTrainingTypes() {
        var trainingType = mock(TrainingType.class);
        List<TrainingType> trainingTypes = new ArrayList<>();
        trainingTypes.add(trainingType);

        when(userRepo.isAuthenticated(auth))
                .thenReturn(true);
        when(trainingRepo.findAllTrainingTypes())
                .thenReturn(trainingTypes);

        ResponseDto<List<TrainingType>> response = trainingService.getTrainingTypes(auth);

        assertNotNull(response);
        assertFalse(response.getData().isEmpty());
        verify(userRepo).isAuthenticated(auth);
        verify(trainingRepo).findAllTrainingTypes();
    }

    @Test
    void getTrainingTypeNotAuthenticated() {
        when(userRepo.isAuthenticated(auth))
                .thenReturn(false);

        ResponseDto<List<TrainingType>> response = trainingService.getTrainingTypes(auth);

        assertNotNull(response);
        assertEquals("User is not authenticated", response.getErrorMessage());
        verifyNoInteractions(trainingRepo);
    }
}