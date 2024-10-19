package uz.anas.gymcrm.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uz.anas.gymcrm.model.dto.Authentication;
import uz.anas.gymcrm.model.dto.ResponseDto;
import uz.anas.gymcrm.model.dto.get.GetTrainerDto;
import uz.anas.gymcrm.model.dto.patch.TraineeActivationDto;
import uz.anas.gymcrm.model.dto.post.PostTrainerDto;
import uz.anas.gymcrm.model.dto.put.PutTrainerDto;
import uz.anas.gymcrm.model.entity.Trainer;
import uz.anas.gymcrm.model.entity.User;
import uz.anas.gymcrm.model.entity.enums.Specialization;
import uz.anas.gymcrm.model.mapper.TrainerMapper;
import uz.anas.gymcrm.repository.TrainerRepository;
import uz.anas.gymcrm.repository.UserRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainerServiceTest {

    @Mock
    TrainerRepository trainerRepo;
    @Mock
    CredentialService credentialService;
    @Mock
    UserRepository userRepo;
    @Mock
    TrainerMapper trainerMapper;
    @InjectMocks
    TrainerService trainerService;
    User user;
    Authentication auth = mock(Authentication.class);

    @BeforeEach
    void setUp() {
        user = new User();
    }

    @Test
    void createTrainer() {
        var trainerDto = mock(PostTrainerDto.class);
        Trainer trainer = new Trainer();
        trainer.setUser(new User("John", "Doe", true));

        when(trainerMapper.toEntity(any(PostTrainerDto.class)))
                .thenReturn(trainer);
        when(userRepo.existsByUsername(anyString()))
                .thenReturn(true);
        when(credentialService.genUsername(anyString()))
                .thenReturn("John.Doe.1");
        when(credentialService.genPassword())
                .thenReturn("generatedPassword");
        when(trainerRepo.save(trainer))
                .thenReturn(trainer);

        ResponseDto<PostTrainerDto> response = trainerService.createTrainer(trainerDto);

        assertNotNull(response);
        assertEquals("John.Doe.1", trainer.getUser().getUsername());
        assertEquals("generatedPassword", trainer.getUser().getPassword());
        verify(trainerRepo).save(trainer);
    }

    @Test
    void getTrainerByUsername() {
        String username = "trainerUser";
        Trainer trainer = new Trainer();
        var getTrainerDto = mock(GetTrainerDto.class);
        trainer.setUser(user);

        when(userRepo.isAuthenticated(auth))
                .thenReturn(true);
        when(trainerRepo.findByUserUsername(username))
                .thenReturn(Optional.of(trainer));
        when(trainerMapper.toGetDto(trainer))
                .thenReturn(getTrainerDto);

        ResponseDto<GetTrainerDto> response = trainerService.getTrainerByUsername(auth, username);

        assertNotNull(response);
        assertNotNull(response.getData());
        verify(userRepo).isAuthenticated(auth);
        verify(trainerRepo).findByUserUsername(username);
    }

    @Test
    void getTrainerByUsernameNotAuthenticated() {
        String username = "testUser";

        when(userRepo.isAuthenticated(auth))
                .thenReturn(false);

        ResponseDto<?> response = trainerService.getTrainerByUsername(auth, username);

        assertNotNull(response);
        assertEquals("User is not authenticated", response.getErrorMessage());
        verify(userRepo, times(1)).isAuthenticated(auth);
        verifyNoInteractions(trainerRepo);
    }

    @Test
    void activateTrainerByUsername() {
        var activationDto = new TraineeActivationDto("trainerUser", true);
        Trainer trainer = new Trainer();
        trainer.setUser(user);

        when(userRepo.isAuthenticated(auth))
                .thenReturn(true);
        when(trainerRepo.findByUserUsername(activationDto.username()))
                .thenReturn(Optional.of(trainer));

        ResponseDto<?> response = trainerService.activateTrainerByUsername(auth, activationDto);

        assertNotNull(response);
        assertTrue(trainer.getUser().getIsActive());
        verify(userRepo).save(trainer.getUser());
        verify(userRepo).isAuthenticated(auth);
    }

    @Test
    void activateTrainerByUsernameNotAuthenticated() {
        when(userRepo.isAuthenticated(auth))
                .thenReturn(false);

        ResponseDto<?> response = trainerService.activateTrainerByUsername(auth, null);

        assertNotNull(response);
        assertEquals("User is not authenticated", response.getErrorMessage());
        verify(userRepo, times(1)).isAuthenticated(auth);
        verifyNoInteractions(trainerRepo);
    }

    @Test
    void updateTrainer() {
        var trainerDto = new PutTrainerDto(Specialization.CARDIO, "UpdatedFirst",
                "UpdatedLast", "trainerUser", true, new HashSet<>());

        Trainer trainer = new Trainer();
        trainer.setUser(user);

        when(userRepo.isAuthenticated(auth))
                .thenReturn(true);
        when(trainerRepo.findByUserUsername(trainerDto.username()))
                .thenReturn(Optional.of(trainer));

        ResponseDto<PutTrainerDto> response = trainerService.updateTrainer(auth, trainerDto);

        assertNotNull(response);
        assertEquals("UpdatedFirst", trainer.getUser().getFirstName());
        assertEquals("UpdatedLast", trainer.getUser().getLastName());
        verify(trainerRepo).save(trainer);
    }

    @Test
    void updateTrainerNotAuthenticated() {
        when(userRepo.isAuthenticated(auth))
                .thenReturn(false);

        ResponseDto<?> response = trainerService.updateTrainer(auth, null);

        assertNotNull(response);
        assertEquals("User is not authenticated", response.getErrorMessage());
        verify(userRepo, times(1)).isAuthenticated(auth);
        verifyNoInteractions(trainerRepo);
    }

    @Test
    void getAllTrainers() {
        when(trainerRepo.findAll())
                .thenReturn(new ArrayList<>());

        List<Trainer> resp = trainerService.getAllTrainers();
        assertNotNull(resp);
        assertEquals(0, resp.size());
    }
}