package uz.anas.gymcrm.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uz.anas.gymcrm.dao.TraineeDAO;
import uz.anas.gymcrm.dao.UserDao;
import uz.anas.gymcrm.entity.Trainee;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TraineeServiceTest {

    @Mock
    private TraineeDAO traineeDAO;
    @Mock
    private CredentialGenerator credentialGenerator;
    @InjectMocks
    private TraineeService traineeService;
    private Trainee trainee;

    @BeforeEach
    void setUp() {
        trainee = Trainee.builder()
                .firstName("John")
                .lastName("Smith")
                .build();
    }

    @Test
    void createTraineeGenNewUsername() {
        when(traineeDAO.existsByUsername(anyString()))
                .thenReturn(true);
        when(credentialGenerator.genUsername(anyString(), any(UserDao.class)))
                .thenReturn(trainee.getFirstName() + "." + trainee.getLastName() + "123");
        when(traineeDAO.save(any(Trainee.class)))
                .thenReturn(trainee);

        Trainee savedTrainee = traineeService.createTrainee(trainee);
        assertNotEquals(savedTrainee.getUsername(), trainee.getFirstName() + "." + trainee.getLastName());
        verify(traineeDAO, times(1)).existsByUsername(anyString());
        verify(credentialGenerator, times(1)).genUsername(anyString(), any(UserDao.class));
        verify(credentialGenerator, times(1)).genPassword();
    }

    @Test
    void createTrainee() {
        when(traineeDAO.save(any(Trainee.class)))
                .thenReturn(trainee);

        Trainee savedTrainee = traineeService.createTrainee(trainee);
        assertEquals(savedTrainee.getUsername(), trainee.getFirstName() + "." + trainee.getLastName());
        verify(traineeDAO, times(1)).existsByUsername(anyString());
        verify(credentialGenerator, times(1)).genPassword();
    }

    @Test
    void getAllTrainees() {
        when(traineeDAO.findAll())
                .thenReturn(List.of(trainee));

        List<Trainee> allTrainees = traineeService.getAllTrainees();

        assertEquals(allTrainees.size(), 1);
        verify(traineeDAO, times(1)).findAll();
    }

    @Test
    void getTraineeById() {
        when(traineeDAO.findById(any(UUID.class)))
                .thenReturn(Optional.of(trainee));

        Optional<Trainee> traineeOptional = traineeService.getTraineeById(UUID.randomUUID());
        assertTrue(traineeOptional.isPresent());
        assertEquals(trainee, traineeOptional.get());
        verify(traineeDAO, times(1)).findById(any(UUID.class));
    }

    @Test
    void getTraineeByUsername() {
        when(traineeDAO.findByUsername(anyString()))
                .thenReturn(Optional.of(trainee));

        Optional<Trainee> traineeOptional = traineeService.getTraineeByUsername("John.Smith");
        assertTrue(traineeOptional.isPresent());
        assertEquals(trainee, traineeOptional.get());
        verify(traineeDAO, times(1)).findByUsername(anyString());
    }

    @Test
    void updateTrainee() {
        when(traineeDAO.save(any(Trainee.class)))
                .thenReturn(trainee);

        trainee.setFirstName("Max");
        Trainee savedTrainee = traineeService.updateTrainee(trainee);
        assertNotEquals(savedTrainee.getUsername(), "Max");
        verify(traineeDAO, times(1)).save(any(Trainee.class));
    }

    @Test
    void deleteById() {
        traineeService.deleteById(UUID.randomUUID());
        verify(traineeDAO, times(1)).deleteById(any(UUID.class));
    }
}