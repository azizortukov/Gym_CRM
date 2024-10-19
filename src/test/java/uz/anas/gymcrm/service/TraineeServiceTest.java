package uz.anas.gymcrm.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uz.anas.gymcrm.model.dto.Authentication;
import uz.anas.gymcrm.model.dto.ResponseDto;
import uz.anas.gymcrm.model.dto.TrainerBaseDto;
import uz.anas.gymcrm.model.dto.get.GetTraineeDto;
import uz.anas.gymcrm.model.dto.patch.TraineeActivationDto;
import uz.anas.gymcrm.model.dto.post.PostTraineeDto;
import uz.anas.gymcrm.model.dto.put.PutTraineeDto;
import uz.anas.gymcrm.model.entity.Trainee;
import uz.anas.gymcrm.model.entity.Trainer;
import uz.anas.gymcrm.model.entity.User;
import uz.anas.gymcrm.model.entity.enums.Specialization;
import uz.anas.gymcrm.model.mapper.TraineeMapper;
import uz.anas.gymcrm.repository.TraineeRepository;
import uz.anas.gymcrm.repository.TrainerRepository;
import uz.anas.gymcrm.repository.UserRepository;

import java.sql.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TraineeServiceTest {

    @Mock
    TraineeRepository traineeRepo;
    @Mock
    CredentialService credentialService;
    @Mock
    UserRepository userRepo;
    @Mock
    TrainerRepository trainerRepo;
    @Mock
    TraineeMapper traineeMapper;
    @InjectMocks
    TraineeService traineeService;
    Trainee trainee;
    User user;
    PostTraineeDto postTraineeDto;
    PutTraineeDto traineeDto;
    Authentication auth = mock(Authentication.class);

    @BeforeEach
    void setUp() {
        trainee = new Trainee();
        user = new User();
        postTraineeDto = new PostTraineeDto(new Date(System.currentTimeMillis()),
                "New york", "John", "Thompson", "John.Thompson",
                "password123");
        traineeDto = new PutTraineeDto(
                new Date(System.currentTimeMillis()),"123 Test St", "John", "Doe",
                "john.doe", true, new HashSet<>());
    }

    @Test
    void createUsernameExists() {
        trainee.setUser(user);
        user.setFirstName("john");
        user.setLastName("doe");
        String existingUsername = "john.doe";
        String newUsername = "john.doe1";
        String generatedPassword = "password123";

        when(traineeMapper.toEntity(any(PostTraineeDto.class)))
                .thenReturn(trainee);
        when(userRepo.existsByUsername(anyString()))
                .thenReturn(true);
        when(credentialService.genUsername(anyString()))
                .thenReturn(newUsername);
        when(credentialService.genPassword())
                .thenReturn(generatedPassword);

        ResponseDto<PostTraineeDto> response = traineeService.create(postTraineeDto);

        assertNotNull(response);
        assertEquals(newUsername, trainee.getUser().getUsername());
        assertEquals(generatedPassword, trainee.getUser().getPassword());
        verify(userRepo, times(1)).existsByUsername(existingUsername);
        verify(credentialService, times(1)).genUsername(existingUsername);
        verify(traineeRepo, times(1)).save(trainee);
    }

    @Test
    void create() {
        trainee.setUser(user);
        user.setFirstName("john");
        user.setLastName("doe");
        String generatedPassword = "password123";

        when(traineeMapper.toEntity(any(PostTraineeDto.class)))
                .thenReturn(trainee);
        when(userRepo.existsByUsername(anyString()))
                .thenReturn(false);
        when(credentialService.genPassword())
                .thenReturn(generatedPassword);

        ResponseDto<PostTraineeDto> response = traineeService.create(postTraineeDto);

        assertNotNull(response);
        assertEquals(generatedPassword, trainee.getUser().getPassword());
        verify(traineeRepo, times(1)).save(trainee);
    }

    @Test
    void getByUsernameNotAuthenticated() {
        String username = "testUser";

        when(userRepo.isAuthenticated(auth))
                .thenReturn(false);

        ResponseDto<GetTraineeDto> response = traineeService.getByUsername(auth, username);

        assertNotNull(response);
        assertEquals("User is not authenticated", response.getErrorMessage());
        verify(userRepo, times(1)).isAuthenticated(auth);
        verifyNoInteractions(traineeRepo);
    }

    @Test
    void getByUsernameNotFound() {
        String username = "testUser";

        when(userRepo.isAuthenticated(auth))
                .thenReturn(true);
        when(traineeRepo.findByUserUsername(username))
                .thenReturn(Optional.empty());

        ResponseDto<GetTraineeDto> response = traineeService.getByUsername(auth, username);

        assertNotNull(response);
        assertEquals("Trainee is not found", response.getErrorMessage());
        verify(userRepo, times(1)).isAuthenticated(auth);
        verify(traineeRepo, times(1)).findByUserUsername(username);
    }

    @Test
    void getByUsername() {
        String username = "testUser";
        Trainee trainee = new Trainee();
        var getTraineeDto = mock(GetTraineeDto.class);

        when(userRepo.isAuthenticated(auth))
                .thenReturn(true);
        when(traineeRepo.findByUserUsername(username))
                .thenReturn(Optional.of(trainee));
        when(traineeMapper.toGetDto(trainee))
                .thenReturn(getTraineeDto);

        ResponseDto<GetTraineeDto> response = traineeService.getByUsername(auth, username);

        assertNotNull(response);
        verify(traineeRepo, times(1)).findByUserUsername(username);
    }

    @Test
    void updateTrainerListNotAuthenticated() {

        when(userRepo.isAuthenticated(auth))
                .thenReturn(false);


        String traineeUsername = "testUser";
        Set<TrainerBaseDto> trainerList = new HashSet<>();

        ResponseDto<?> response = traineeService.updateTrainerList(auth, traineeUsername, trainerList);

        assertNotNull(response);
        assertEquals("User is not authenticated", response.getErrorMessage());
        verify(userRepo, times(1)).isAuthenticated(auth);
        verifyNoInteractions(traineeRepo);
    }

    @Test
    void updateTrainerListNotFound() {
        String traineeUsername = "testUser";
        Set<TrainerBaseDto> trainerList = new HashSet<>();

        when(userRepo.isAuthenticated(auth))
                .thenReturn(true);
        when(traineeRepo.findByUserUsername(traineeUsername))
                .thenReturn(Optional.empty());

        ResponseDto<List<TrainerBaseDto>> response = traineeService.updateTrainerList(auth, traineeUsername, trainerList);

        assertNotNull(response);
        assertEquals("Trainee with testUser username is not found", response.getErrorMessage());
        verify(traineeRepo, times(1)).findByUserUsername(traineeUsername);
    }

    @Test
    void updateTrainerList() {
        String traineeUsername = "testUser";
        Set<TrainerBaseDto> trainerList = new HashSet<>();
        var trainerDto = mock(TrainerBaseDto.class);
        trainerList.add(trainerDto);

        Trainer trainer = new Trainer();
        trainer.setUser(user);

        when(userRepo.isAuthenticated(auth))
                .thenReturn(true);
        when(traineeRepo.findByUserUsername(traineeUsername))
                .thenReturn(Optional.of(trainee));
        when(trainerRepo.findByUserUsername(trainerDto.username()))
                .thenReturn(Optional.of(trainer));

        ResponseDto<List<TrainerBaseDto>> response = traineeService.updateTrainerList(auth, traineeUsername, trainerList);

        assertNotNull(response);
        verify(traineeRepo, times(1)).findByUserUsername(traineeUsername);
    }

    @Test
    void update() {
        user.setFirstName("OldFirstName");
        user.setLastName("OldLastName");
        user.setIsActive(false);
        Trainee trainee = new Trainee();
        trainee.setUser(user);
        trainee.setAddress("Old Address");
        trainee.setDateOfBirth(Date.valueOf("1980-01-01"));

        when(userRepo.isAuthenticated(auth))
                .thenReturn(true);
        when(traineeRepo.findByUserUsername(traineeDto.username()))
                .thenReturn(Optional.of(trainee));
        when(traineeMapper.toPutDto(trainee))
                .thenReturn(new PutTraineeDto(
                traineeDto.dateOfBirth(), traineeDto.address(), traineeDto.firstName(),
                traineeDto.lastName(), traineeDto.username(), traineeDto.isActive(), traineeDto.trainersList()));

        ResponseDto<PutTraineeDto> response = traineeService.update(auth, traineeDto);

        assertNotNull(response);
        assertEquals(traineeDto.username(), response.getData().username());
        assertEquals(traineeDto.firstName(), trainee.getUser().getFirstName());
        assertEquals(traineeDto.lastName(), trainee.getUser().getLastName());
        assertEquals(traineeDto.isActive(), trainee.getUser().getIsActive());
        assertEquals(traineeDto.address(), trainee.getAddress());
        assertEquals(traineeDto.dateOfBirth(), trainee.getDateOfBirth());

        verify(traineeRepo).findByUserUsername(traineeDto.username());
        verify(traineeRepo).save(trainee);
        verify(userRepo).isAuthenticated(auth);
    }

    @Test
    void updateNotAuthenticated() {

        when(userRepo.isAuthenticated(auth))
                .thenReturn(false);

        ResponseDto<PutTraineeDto> response = traineeService.update(auth, traineeDto);

        assertNotNull(response);
        assertEquals("User is not authenticated", response.getErrorMessage());
        verify(userRepo).isAuthenticated(auth);
        verifyNoInteractions(traineeRepo);
    }

    @Test
    void updateTraineeNotFound() {

        when(userRepo.isAuthenticated(auth))
                .thenReturn(true);
        when(traineeRepo.findByUserUsername(traineeDto.username()))
                .thenReturn(Optional.empty());

        ResponseDto<PutTraineeDto> response = traineeService.update(auth, traineeDto);

        assertNotNull(response);
        assertEquals("Trainee with username %s not found".formatted(traineeDto.username()), response.getErrorMessage());
        verify(userRepo).isAuthenticated(auth);
        verify(traineeRepo).findByUserUsername(traineeDto.username());
        verifyNoMoreInteractions(traineeRepo);
    }

    @Test
    void activateTraineeByUsernameNotAuthenticated() {
        var activationDto = new TraineeActivationDto("Thompson", true);

        when(userRepo.isAuthenticated(auth))
                .thenReturn(false);

        ResponseDto<?> response = traineeService.activateTraineeByUsername(auth, activationDto);

        assertNotNull(response);
        assertEquals("User is not authenticated", response.getErrorMessage());
        verify(userRepo, times(1)).isAuthenticated(auth);
        verifyNoInteractions(traineeRepo);
    }

    @Test
    void activateTraineeByUsername() {
        var activationDto = new TraineeActivationDto("Thompson", true);
        trainee.setUser(user);

        when(userRepo.isAuthenticated(auth))
                .thenReturn(true);
        when(traineeRepo.findByUserUsername(activationDto.username()))
                .thenReturn(Optional.of(trainee));

        ResponseDto<?> response = traineeService.activateTraineeByUsername(auth, activationDto);

        assertNotNull(response);
        verify(traineeRepo, times(1)).findByUserUsername(activationDto.username());
        verify(userRepo, times(1)).save(user);
    }

    @Test
    void deleteByUsernameNotAuthenticated() {
        String username = "testUser";

        when(userRepo.isAuthenticated(auth))
                .thenReturn(false);

        ResponseDto<?> response = traineeService.deleteByUsername(auth, username);

        assertNotNull(response);
        assertEquals("User is not authenticated", response.getErrorMessage());
        verify(userRepo, times(1)).isAuthenticated(auth);
        verify(traineeRepo, never()).deleteByUserUsername(username);
    }

    @Test
    void deleteByUsername() {
        String username = "testUser";

        when(userRepo.isAuthenticated(auth))
                .thenReturn(true);

        ResponseDto<?> response = traineeService.deleteByUsername(auth, username);

        assertNotNull(response);
        verify(traineeRepo, times(1)).deleteByUserUsername(username);
    }

    @Test
    void getAllTrainees() {
        when(traineeRepo.findAll())
                .thenReturn(List.of(trainee));

        List<Trainee> allTrainees = traineeService.getAllTrainees();
        assertNotNull(allTrainees);
        assertEquals(1, allTrainees.size());
        verify(traineeRepo, times(1)).findAll();
    }

    @Test
    void getTrainersByNotAssignedNotAuthenticated() {
        String username = "testUser";
        when(userRepo.isAuthenticated(auth))
                .thenReturn(false);

        ResponseDto<?> response = traineeService.getTrainersByNotAssigned(auth, username);

        assertEquals("User is not authenticated", response.getErrorMessage());
        verify(userRepo, times(1)).isAuthenticated(auth);
    }

    @Test
    void getTrainersByNotAssigned() {
        String traineeUsername = "testUser";
        var expectedTrainers = List.of(
                new TrainerBaseDto("trainerFirstName1", "trainerLastName1", "trainerUsername1", Specialization.CARDIO),
                new TrainerBaseDto("trainerFirstName2", "trainerLastName2", "trainerUsername2", Specialization.AEROBICS)
        );

        when(userRepo.isAuthenticated(auth)).thenReturn(true);
        when(traineeRepo.findByTraineeUsernameNotAssigned(traineeUsername)).thenReturn(expectedTrainers);

        ResponseDto<List<TrainerBaseDto>> response = traineeService.getTrainersByNotAssigned(auth, traineeUsername);

        assertNotNull(response);
        assertEquals(expectedTrainers, response.getData());

        verify(userRepo).isAuthenticated(auth);
        verify(traineeRepo).findByTraineeUsernameNotAssigned(traineeUsername);
    }
}