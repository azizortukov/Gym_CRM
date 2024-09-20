package uz.anas.gymcrm.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uz.anas.gymcrm.entity.Trainee;
import uz.anas.gymcrm.entity.Trainer;
import uz.anas.gymcrm.entity.User;
import uz.anas.gymcrm.repo.TraineeRepo;
import uz.anas.gymcrm.repo.UserRepo;

import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TraineeServiceTest {

    @Mock
    TraineeRepo traineeRepo;
    @Mock
    UserRepo userRepo;
    @Mock
    CredentialGenerator credentialGenerator;

    @InjectMocks
    TraineeService traineeService;
    Trainee trainee;
    User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .firstName("Tom")
                .lastName("Anderson")
                .isActive(true)
                .build();

        trainee = Trainee.builder()
                .dateOfBirth(Date.valueOf(LocalDate.of(2001, 3, 13)))
                .address("Some address")
                .user(user)
                .build();
    }

    @Test
    void createTraineeOnUserNull() {
        trainee.setUser(null);
        assertThrows(NullPointerException.class, () -> traineeService.createTrainee(trainee));
    }

    @Test
    void createTraineeIfUsernameExists() {
        trainee.setId(UUID.randomUUID());
        when(userRepo.existsByUsername(anyString()))
                .thenReturn(true);
        when(traineeRepo.save(any(Trainee.class)))
                .thenReturn(trainee);

        traineeService.createTrainee(trainee);
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

        traineeService.createTrainee(trainee);
        verify(userRepo, times(1)).existsByUsername(anyString());
        verify(credentialGenerator, times(1)).genPassword();
    }

    @Test
    void getTraineeByUsernameNotAuthenticated() {
        when(traineeRepo.isAuthenticated(any(User.class)))
                .thenReturn(false);
        assertThrows(RuntimeException.class, () -> traineeService.getTraineeByUsername(user, "jsjda"));
    }

    @Test
    void getTraineeByUsername() {
        when(traineeRepo.isAuthenticated(any(User.class)))
                .thenReturn(true);
        when(traineeRepo.findByUsername(anyString()))
                .thenReturn(Optional.of(trainee));

        Optional<Trainee> traineeOptional = traineeService.getTraineeByUsername(user, "Tom.Anderson");
        assertTrue(traineeOptional.isPresent());
        assertEquals(trainee, traineeOptional.get());
    }

    @Test
    void changePasswordByUsernameNotAuthenticated() {
        when(traineeRepo.isAuthenticated(any(User.class)))
                .thenReturn(false);

        assertThrows(RuntimeException.class, () -> traineeService.changePasswordByUsername(user, "Tom.Anderson", "Tom"));
    }

    @Test
    void changePasswordByUsername() {
        String username = "testuser";
        String newPassword = "newpassword";

        when(traineeRepo.isAuthenticated(any(User.class)))
                .thenReturn(true);
        when(traineeRepo.findByUsername(anyString()))
                .thenReturn(Optional.of(trainee));

        traineeService.changePasswordByUsername(user, username, newPassword);
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepo).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();
        assertEquals(newPassword, savedUser.getPassword());
    }

    @Test
    void updateTraineeNotAuthenticated() {
        when(traineeRepo.isAuthenticated(user))
                .thenReturn(false);
        assertThrows(RuntimeException.class, () -> traineeService.changePasswordByUsername(user, "Tom.Anderson", "Tom"));
    }

    @Test
    void updateTrainee() {
        Set<Trainer> trainers = new HashSet<>();
        trainers.add(new Trainer());

        when(traineeRepo.isAuthenticated(user)).thenReturn(true);
        when(traineeRepo.save(trainee)).thenReturn(trainee);

        Trainee updatedTrainee = traineeService.updateTrainee(user, trainee, trainers);
        verify(traineeRepo).save(trainee);
        assertEquals(trainers, updatedTrainee.getTrainers());
    }

    @Test
    void activateTraineeByUsernameNotAuthenticated() {
        when(traineeRepo.isAuthenticated(any(User.class)))
                .thenReturn(false);

        assertThrows(RuntimeException.class, () -> traineeService.activateTraineeByUsername(user, "Tom"));
        verify(traineeRepo, never()).findByUsername(anyString());
        verify(userRepo, never()).save(any(User.class));
    }

    @Test
    void activateTraineeByUsername() {
        String username = "testuser";

        when(traineeRepo.isAuthenticated(user)).thenReturn(true);
        when(traineeRepo.findByUsername(username)).thenReturn(Optional.of(trainee));

        boolean initialStatus = user.isActive();
        traineeService.activateTraineeByUsername(user, username);
        verify(userRepo).save(user);
        assertEquals(!initialStatus, user.isActive());
    }

    @Test
    void deleteByUsernameNotAuthenticated() {
        when(traineeRepo.isAuthenticated(any(User.class)))
                .thenReturn(false);

        assertThrows(RuntimeException.class, () -> traineeService.deleteByUsername(user, "Tom"));
        verify(traineeRepo, never()).deleteByUsername(anyString());
    }

    @Test
    void deleteByUsername() {
        String username = "testuser";
        when(traineeRepo.isAuthenticated(user)).thenReturn(true);

        traineeService.deleteByUsername(user, username);
        verify(traineeRepo).deleteByUsername(username);
    }

    @Test
    void getAllTrainees() {
        List<Trainee> expectedTrainees = Arrays.asList(new Trainee(), new Trainee());
        when(traineeRepo.findAll()).thenReturn(expectedTrainees);

        List<Trainee> actualTrainees = traineeService.getAllTrainees();
        assertEquals(expectedTrainees, actualTrainees);
    }
}