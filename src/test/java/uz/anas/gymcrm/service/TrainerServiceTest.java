package uz.anas.gymcrm.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uz.anas.gymcrm.model.dto.Authentication;
import uz.anas.gymcrm.model.entity.Trainer;
import uz.anas.gymcrm.model.entity.User;
import uz.anas.gymcrm.model.entity.enums.Specialization;
import uz.anas.gymcrm.repository.TrainerRepository;
import uz.anas.gymcrm.repository.UserRepository;

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
    TrainerRepository trainerRepo;
    @Mock
    UserRepository userRepo;
    @Mock
    CredentialGenerator credentialGenerator;

    @InjectMocks
    TrainerService trainerService;
    User user;
    Trainer trainer;
    Authentication authentication;

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
        authentication = new Authentication("Tom.Anderson", "password111");
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
        when(userRepo.isAuthenticated(any(Authentication.class)))
                .thenReturn(false);

        assertThrows(RuntimeException.class, () -> trainerService.getTrainerByUsername(authentication, "kdjf"));
    }

    @Test
    void getTrainerByUsername() {
        when(userRepo.isAuthenticated(any(Authentication.class)))
                .thenReturn(true);
        when(trainerRepo.findByUserUsername(anyString()))
                .thenReturn(Optional.of(trainer));

        Optional<Trainer> trainerOptional = trainerService.getTrainerByUsername(authentication, "Tom.Anderson");
        assertTrue(trainerOptional.isPresent());
        assertEquals(trainer, trainerOptional.get());
    }

    @Test
    void changePasswordByUsernameNotAuthenticated() {
        when(userRepo.isAuthenticated(any(Authentication.class)))
                .thenReturn(false);

        assertThrows(RuntimeException.class, () -> trainerService.changePasswordByUsername(authentication, "Tom.Anderson", "Tom"));
    }

    @Test
    void changePasswordByUsername() {
        String username = "testuser";
        String newPassword = "newpassword";
        when(userRepo.isAuthenticated(any(Authentication.class)))
                .thenReturn(true);
        when(trainerRepo.findByUserUsername(anyString()))
                .thenReturn(Optional.of(trainer));

        trainerService.changePasswordByUsername(authentication, username, newPassword);
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepo).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();
        assertEquals(newPassword, savedUser.getPassword());
    }

    @Test
    public void activateTrainerByUsernameNotAuthenticated() {

        when(userRepo.isAuthenticated(authentication)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> {
            trainerService.activateTrainerByUsername(authentication, "trainerUsername", true);
        });

        verify(trainerRepo, never()).findByUserUsername(anyString());
        verify(userRepo, never()).save(any(User.class));
    }

    @Test
    public void activateTrainerByUsernameTrainerNotFound() {
        when(userRepo.isAuthenticated(authentication))
                .thenReturn(true);
        when(trainerRepo.findByUserUsername("trainerUsername"))
                .thenReturn(Optional.empty());

        trainerService.activateTrainerByUsername(authentication, "trainerUsername", true);
        verify(userRepo, never()).save(any(User.class));
    }

    @Test
    public void activateTrainerByUsernameTrainerUserNotFound() {
        trainer.setUser(null);
        when(userRepo.isAuthenticated(authentication))
                .thenReturn(true);
        when(trainerRepo.findByUserUsername("trainerUsername"))
                .thenReturn(Optional.of(trainer));

        trainerService.activateTrainerByUsername(authentication, "trainerUsername", true);

        verify(userRepo, never()).save(any(User.class));
    }

    @Test
    public void activateTrainerByUsername() {
        when(userRepo.isAuthenticated(authentication))
                .thenReturn(true);
        when(trainerRepo.findByUserUsername("trainerUsername"))
                .thenReturn(Optional.of(trainer));

        trainerService.activateTrainerByUsername(authentication, "trainerUsername", true);

        assertTrue(user.getIsActive());
        verify(userRepo, times(1)).save(user);
    }

    @Test
    public void getTrainersByNotAssignedNotAuthenticated() {
        when(userRepo.isAuthenticated(authentication))
                .thenReturn(false);

        assertThrows(RuntimeException.class, () -> {
            trainerService.getTrainersByNotAssigned(authentication, "traineeUsername");
        });

        verify(trainerRepo, never()).findByTraineeUsernameNotAssigned(anyString());
    }

    @Test
    public void getTrainersByNotAssigned() {
        List<Trainer> trainers = List.of(new Trainer(), new Trainer());

        when(userRepo.isAuthenticated(authentication)).thenReturn(true);
        when(trainerRepo.findByTraineeUsernameNotAssigned("traineeUsername")).thenReturn(trainers);

        List<Trainer> result = trainerService.getTrainersByNotAssigned(authentication, "traineeUsername");

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