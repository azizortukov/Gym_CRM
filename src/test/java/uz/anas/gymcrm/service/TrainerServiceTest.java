package uz.anas.gymcrm.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uz.anas.gymcrm.dao.TrainerDAO;
import uz.anas.gymcrm.dao.UserDao;
import uz.anas.gymcrm.entity.Trainer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainerServiceTest {

    @Mock
    private TrainerDAO trainerDAO;
    @Mock
    private CredentialGenerator credentialGenerator;
    @InjectMocks
    private TrainerService trainerService;
    private Trainer trainer;

    @BeforeEach
    void setUp() {
        trainer = Trainer.builder()
                .firstName("John")
                .lastName("Smith")
                .build();
    }

    @Test
    void createTrainerGenNewUsername() {
        when(trainerDAO.existsByUsername(anyString()))
                .thenReturn(true);
        when(credentialGenerator.genUsername(anyString(), any(UserDao.class)))
                .thenReturn(trainer.getFirstName() + "." + trainer.getLastName() + "123");
        when(trainerDAO.save(any(Trainer.class)))
                .thenReturn(trainer);

        Trainer savedTrainer = trainerService.createTrainer(trainer);
        assertNotEquals(savedTrainer.getUsername(), trainer.getFirstName() + "." + trainer.getLastName());
        verify(trainerDAO, times(1)).existsByUsername(anyString());
        verify(credentialGenerator, times(1)).genUsername(anyString(), any(UserDao.class));
        verify(credentialGenerator, times(1)).genPassword();
    }

    @Test
    void createTrainer() {
        when(trainerDAO.save(any(Trainer.class)))
                .thenReturn(trainer);

        Trainer savedTrainee = trainerService.createTrainer(trainer);
        assertEquals(savedTrainee.getUsername(), trainer.getFirstName() + "." + trainer.getLastName());
        verify(trainerDAO, times(1)).existsByUsername(anyString());
        verify(credentialGenerator, times(1)).genPassword();
    }

    @Test
    void getAllTrainers() {
        when(trainerDAO.findAll())
                .thenReturn(List.of(trainer));

        List<Trainer> allTrainees = trainerService.getAllTrainers();

        assertEquals(allTrainees.size(), 1);
        verify(trainerDAO, times(1)).findAll();
    }

    @Test
    void getTrainerById() {
        when(trainerDAO.findById(any(UUID.class)))
                .thenReturn(Optional.of(trainer));

        Optional<Trainer> traineeOptional = trainerService.getTrainerById(UUID.randomUUID());
        assertTrue(traineeOptional.isPresent());
        assertEquals(trainer, traineeOptional.get());
        verify(trainerDAO, times(1)).findById(any(UUID.class));
    }

    @Test
    void getTrainerByUsername() {
        when(trainerDAO.findByUsername(anyString()))
                .thenReturn(Optional.of(trainer));

        Optional<Trainer> traineeOptional = trainerService.getTrainerByUsername("John.Smith");
        assertTrue(traineeOptional.isPresent());
        assertEquals(trainer, traineeOptional.get());
        verify(trainerDAO, times(1)).findByUsername(anyString());
    }

    @Test
    void updateTrainer() {
        when(trainerDAO.save(any(Trainer.class)))
                .thenReturn(trainer);

        trainer.setFirstName("Max");
        Trainer savedTrainee = trainerService.updateTrainer(trainer);
        assertNotEquals(savedTrainee.getUsername(), "Max");
        verify(trainerDAO, times(1)).save(any(Trainer.class));
    }

    @Test
    void deleteById() {
        trainerService.deleteById(UUID.randomUUID());
        verify(trainerDAO, times(1)).deleteById(any(UUID.class));
    }
}