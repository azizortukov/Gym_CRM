package uz.anas.gymcrm.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uz.anas.gymcrm.entity.Trainer;
import uz.anas.gymcrm.entity.User;
import uz.anas.gymcrm.entity.enums.Specialization;
import uz.anas.gymcrm.repo.TrainerRepo;
import uz.anas.gymcrm.repo.UserRepo;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainerServiceTest {

    @Mock
    TrainerRepo trainerRepo;
    @Mock
    UserRepo userRepo;
    @Mock
    CredentialGenerator credentialGenerator;

    @InjectMocks
    TrainerService trainerService;
    User user;
    Trainer trainer;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .firstName("Tom")
                .lastName("Anderson")
                .isActive(true)
                .build();
        trainer = Trainer.builder()
                .specialization(Specialization.CARDIO)
                .trainees(Set.of())
                .trainings(List.of())
                .user(user)
                .build();
    }

    @Test
    void createTrainerOnUserNull() {
        trainer.setUser(null);
        assertThrows(NullPointerException.class, () -> trainerService.createTrainer(trainer));
    }

    @Test
    void createTrainerIfUsernameExists() {
        trainer.setId(UUID.randomUUID());
        when(userRepo.existsByUsername(anyString()))
                .thenReturn(true);
        when(trainerRepo.save(any(Trainer.class)))
                .thenReturn(trainer);

        trainerService.createTrainer(trainer);
        verify(userRepo, times(1)).existsByUsername(anyString());
        verify(credentialGenerator, times(1)).genUsername(anyString());
        verify(credentialGenerator, times(1)).genPassword();
    }

    @Test
    void createTrainer() {
        trainer.setId(UUID.randomUUID());
        when(userRepo.existsByUsername(anyString()))
                .thenReturn(false);
        when(trainerRepo.save(any(Trainer.class)))
                .thenReturn(trainer);

        trainerService.createTrainer(trainer);
        verify(userRepo, times(1)).existsByUsername(anyString());
        verify(credentialGenerator, times(1)).genPassword();
    }

    @Test
    void getTrainerByUsernameNotAuthenticated() {
        when(trainerRepo.isAuthenticated(any(User.class)))
                .thenReturn(false);

        assertThrows(RuntimeException.class, () -> trainerService.getTrainerByUsername(user, "kdjf"));
    }

    @Test
    void getTrainerByUsername() {
        when(trainerRepo.isAuthenticated(any(User.class)))
                .thenReturn(true);
        when(trainerRepo.findByUsername(anyString()))
                .thenReturn(Optional.of(trainer));

        Optional<Trainer> trainerOptional = trainerService.getTrainerByUsername(user, "Tom.Anderson");
        assertTrue(trainerOptional.isPresent());
        assertEquals(trainer, trainerOptional.get());
    }

    @Test
    void changePasswordByUsernameNotAuthenticated() {
        when(trainerRepo.isAuthenticated(any(User.class)))
                .thenReturn(false);

        assertThrows(RuntimeException.class, () -> trainerService.changePasswordByUsername(user, "Tom.Anderson", "Tom"));
    }

    @Test
    void changePasswordByUsername() {
        String username = "testuser";
        String newPassword = "newpassword";
        when(trainerRepo.isAuthenticated(any(User.class)))
                .thenReturn(true);
        when(trainerRepo.findByUsername(anyString()))
                .thenReturn(Optional.of(trainer));

        trainerService.changePasswordByUsername(user, username, newPassword);
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepo).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();
        assertEquals(newPassword, savedUser.getPassword());
    }

    @Test
    public void activateTrainerByUsernameNotAuthenticated() {

        when(trainerRepo.isAuthenticated(user)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> {
            trainerService.activateTrainerByUsername(user, "trainerUsername", true);
        });

        verify(trainerRepo, never()).findByUsername(anyString());
        verify(userRepo, never()).save(any(User.class));
    }

    @Test
    public void activateTrainerByUsernameTrainerNotFound() {
        when(trainerRepo.isAuthenticated(user))
                .thenReturn(true);
        when(trainerRepo.findByUsername("trainerUsername"))
                .thenReturn(Optional.empty());

        trainerService.activateTrainerByUsername(user, "trainerUsername", true);
        verify(userRepo, never()).save(any(User.class));
    }

    @Test
    public void activateTrainerByUsernameTrainerUserNotFound() {
        trainer.setUser(null);
        when(trainerRepo.isAuthenticated(user))
                .thenReturn(true);
        when(trainerRepo.findByUsername("trainerUsername"))
                .thenReturn(Optional.of(trainer));

        trainerService.activateTrainerByUsername(user, "trainerUsername", true);

        verify(userRepo, never()).save(any(User.class));
    }

    @Test
    public void activateTrainerByUsername() {
        when(trainerRepo.isAuthenticated(user))
                .thenReturn(true);
        when(trainerRepo.findByUsername("trainerUsername"))
                .thenReturn(Optional.of(trainer));

        trainerService.activateTrainerByUsername(user, "trainerUsername", true);

        assertTrue(user.isActive());
        verify(userRepo, times(1)).save(user);
    }

    @Test
    public void getTrainersByNotAssignedNotAuthenticated() {
        when(trainerRepo.isAuthenticated(user))
                .thenReturn(false);

        assertThrows(RuntimeException.class, () -> {
            trainerService.getTrainersByNotAssigned(user, "traineeUsername");
        });

        verify(trainerRepo, never()).findByTraineeUsernameNotAssigned(anyString());
    }

    @Test
    public void getTrainersByNotAssigned() {
        List<Trainer> trainers = List.of(new Trainer(), new Trainer());

        when(trainerRepo.isAuthenticated(user)).thenReturn(true);
        when(trainerRepo.findByTraineeUsernameNotAssigned("traineeUsername")).thenReturn(trainers);

        List<Trainer> result = trainerService.getTrainersByNotAssigned(user, "traineeUsername");

        assertEquals(2, result.size());
        verify(trainerRepo, times(1)).findByTraineeUsernameNotAssigned("traineeUsername");
    }

    @Test
    public void getAllTrainers() {
        List<Trainer> trainers = List.of(new Trainer(), new Trainer());

        when(trainerRepo.findAll()).thenReturn(trainers);

        List<Trainer> result = trainerService.getAllTrainers();

        assertEquals(2, result.size());
        verify(trainerRepo, times(1)).findAll();
    }

}