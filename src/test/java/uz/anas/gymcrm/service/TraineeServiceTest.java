package uz.anas.gymcrm.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uz.anas.gymcrm.model.dto.Authentication;
import uz.anas.gymcrm.model.dto.TrainerBaseDto;
import uz.anas.gymcrm.model.entity.Trainee;
import uz.anas.gymcrm.model.entity.User;
import uz.anas.gymcrm.model.entity.enums.Specialization;
import uz.anas.gymcrm.model.mapper.TraineeMapper;
import uz.anas.gymcrm.repository.TraineeRepository;
import uz.anas.gymcrm.repository.TrainerRepository;
import uz.anas.gymcrm.repository.UserRepository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TraineeServiceTest {

    @Mock
    TraineeRepository traineeRepo;
    @Mock
    UserRepository userRepo;
    @Mock
    CredentialGenerator credentialGenerator;
    @Mock
    TrainerRepository trainerRepo;
    @Mock
    TraineeMapper traineeMapper;

    @InjectMocks
    TraineeService traineeService;
    Trainee trainee;
    User user;
    Authentication auth;

    @BeforeEach
    void setUp() {
        user = new User("Tom", "Anderson", true);
        trainee = new Trainee(user, Date.valueOf(LocalDate.of(2001, 3, 13)),"Some address");
        auth = new Authentication("Tom.Anderson", "password111");
    }

    @Test
    void createTraineeOnUserNull() {
        trainee.setUser(null);
        assertThrows(NullPointerException.class, () -> traineeService.create(trainee));
    }

    @Test
    void createTraineeIfUsernameExists() {
        trainee.setId(UUID.randomUUID());
        when(userRepo.existsByUsername(anyString()))
                .thenReturn(true);
        when(traineeRepo.save(any(Trainee.class)))
                .thenReturn(trainee);

        traineeService.create(trainee);
        verify(userRepo, times(1)).existsByUsername(anyString());
        verify(credentialGenerator, times(1)).genUsername(anyString());
        verify(credentialGenerator, times(1)).genPassword();
    }

    @Test
    void createTrainee() {
        trainee.setId(UUID.randomUUID());
        when(userRepo.existsByUsername(anyString()))
                .thenReturn(false);
        when(traineeRepo.save(any(Trainee.class)))
                .thenReturn(trainee);

        traineeService.create(trainee);
        verify(userRepo, times(1)).existsByUsername(anyString());
        verify(credentialGenerator, times(1)).genPassword();
    }

    @Test
    void getByUsernameNotAuthenticated() {
        when(userRepo.isAuthenticated(any(Authentication.class)))
                .thenReturn(false);
        assertThrows(RuntimeException.class, () -> traineeService.getByUsername(auth, "jsjda"));
    }

    @Test
    void getByUsername() {
        when(userRepo.isAuthenticated(any(Authentication.class)))
                .thenReturn(true);
        when(traineeRepo.findByUserUsername(anyString()))
                .thenReturn(Optional.of(trainee));

        Optional<Trainee> traineeOptional = traineeService.getByUsername(auth, "Tom.Anderson");
        assertTrue(traineeOptional.isPresent());
        assertEquals(trainee, traineeOptional.get());
    }

    @Test
    void changePasswordByUsernameNotAuthenticated() {
        when(userRepo.isAuthenticated(any(Authentication.class)))
                .thenReturn(false);

        assertThrows(RuntimeException.class, () -> traineeService.changePasswordByUsername(auth, "Tom.Anderson", "Tom"));
    }

    @Test
    void changePasswordByUsername() {
        String username = "testuser";
        String newPassword = "newpassword";

        when(userRepo.isAuthenticated(any(Authentication.class)))
                .thenReturn(true);
        when(traineeRepo.findByUserUsername(anyString()))
                .thenReturn(Optional.of(trainee));

        traineeService.changePasswordByUsername(auth, username, newPassword);
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepo).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();
        assertEquals(newPassword, savedUser.getPassword());
    }

    @Test
    void updateTrainerListNotAuthenticated() {
        when(userRepo.isAuthenticated(auth))
                .thenReturn(false);
        assertThrows(RuntimeException.class, () -> traineeService.changePasswordByUsername(auth, "Tom.Anderson", "Tom"));
    }

    @Test
    void activateTraineeByUsernameNotAuthenticated() {
        when(userRepo.isAuthenticated(any(Authentication.class)))
                .thenReturn(false);

        assertThrows(RuntimeException.class, () -> traineeService.activateTraineeByUsername(auth, "Tom"));
        verify(traineeRepo, never()).findByUserUsername(anyString());
        verify(userRepo, never()).save(any(User.class));
    }

    @Test
    void activateTraineeByUsername() {
        String username = "testuser";

        when(userRepo.isAuthenticated(auth)).thenReturn(true);
        when(traineeRepo.findByUserUsername(username)).thenReturn(Optional.of(trainee));

        boolean initialStatus = user.getIsActive();
        traineeService.activateTraineeByUsername(auth, username);
        verify(userRepo).save(user);
        assertEquals(!initialStatus, user.getIsActive());
    }

    @Test
    void deleteByUsernameNotAuthenticated() {
        when(userRepo.isAuthenticated(any(Authentication.class)))
                .thenReturn(false);

        assertThrows(RuntimeException.class, () -> traineeService.deleteByUsername(auth, "Tom"));
        verify(traineeRepo, never()).deleteByUserUsername(anyString());
    }

    @Test
    void deleteByUsername() {
        String username = "testuser";
        when(userRepo.isAuthenticated(auth)).thenReturn(true);

        traineeService.deleteByUsername(auth, username);
        verify(traineeRepo).deleteByUserUsername(username);
    }

    @Test
    void getAllTrainees() {
        List<Trainee> expectedTrainees = Arrays.asList(new Trainee(), new Trainee());
        when(traineeRepo.findAll()).thenReturn(expectedTrainees);

        List<Trainee> actualTrainees = traineeService.getAllTrainees();
        assertEquals(expectedTrainees, actualTrainees);
    }

    @Test
    public void getTrainersByNotAssignedNotAuthenticated() {
        when(userRepo.isAuthenticated(auth))
                .thenReturn(false);

        assertThrows(RuntimeException.class, () -> {
            traineeService.getTrainersByNotAssigned(auth, "traineeUsername");
        });

        verify(traineeRepo, never()).findByTraineeUsernameNotAssigned(anyString());
    }

    @Test
    public void getTrainersByNotAssigned() {
        List<TrainerBaseDto> trainers = List.of(
                new TrainerBaseDto("", "", "", Specialization.CARDIO)
        );

        when(userRepo.isAuthenticated(auth)).thenReturn(true);
        when(traineeRepo.findByTraineeUsernameNotAssigned("traineeUsername")).thenReturn(trainers);

        List<TrainerBaseDto> result = traineeService.getTrainersByNotAssigned(auth, "traineeUsername");

        assertEquals(1, result.size());
        verify(traineeRepo, times(1)).findByTraineeUsernameNotAssigned("traineeUsername");
    }
}