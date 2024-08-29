package uz.anas.gymcrm.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uz.anas.gymcrm.dao.TrainingDAO;
import uz.anas.gymcrm.entity.Training;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainingServiceTest {

    @Mock
    private TrainingDAO trainingDAO;
    @InjectMocks
    private TrainingService trainingService;
    private Training training;

    @BeforeEach
    void setUp() {
        training = Training.builder()
                .duration(120)
                .trainingDate(LocalDate.now())
                .build();
    }

    @Test
    void createTraining() {
        when(trainingDAO.save(any(Training.class)))
                .thenReturn(training);

        Training savedTraining = trainingService.createTraining(training);
        assertNotNull(savedTraining);
        verify(trainingDAO, times(1)).save(any(Training.class));
    }

    @Test
    void getAllTrainings() {
        when(trainingDAO.findAll())
                .thenReturn(List.of(training));

        List<Training> trainings = trainingService.getAllTrainings();
        assertNotNull(trainings);
        assertEquals(trainings.size(), 1);
        verify(trainingDAO, times(1)).findAll();
    }

    @Test
    void getTrainingById() {
        when(trainingDAO.findById(any(UUID.class)))
                .thenReturn(Optional.of(training));

        Optional<Training> trainingOptional = trainingService.getTrainingById(UUID.randomUUID());
        assertNotNull(trainingOptional);
        assertTrue(trainingOptional.isPresent());
        verify(trainingDAO, times(1)).findById(any(UUID.class));
    }

    @Test
    void updateTraining() {
        when(trainingDAO.save(any(Training.class)))
                .thenReturn(training);

        Training updatedTraining = trainingService.updateTraining(training);
        assertNotNull(updatedTraining);
        verify(trainingDAO, times(1)).save(any(Training.class));
    }

    @Test
    void deleteById() {
        trainingService.deleteById(UUID.randomUUID());
        verify(trainingDAO, times(1)).deleteById(any(UUID.class));
    }
}