package uz.anas.gymcrm.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uz.anas.gymcrm.entity.Trainee;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TraineeDAOTest {

    private TraineeDAO traineeDAO;
    private Trainee trainee;

    @BeforeEach
    void setUp() {
        traineeDAO = new TraineeDAO();
        trainee = new Trainee();
        trainee.setUsername("JohnDoe");
        traineeDAO.save(trainee);
    }

    @Test
    void save() {
        Trainee trainee = new Trainee();
        trainee.setUsername("JohnDoe");

        Trainee savedTrainee = traineeDAO.save(trainee);
        assertNotNull(savedTrainee.getId());
        assertEquals("JohnDoe", savedTrainee.getUsername());
        assertTrue(traineeDAO.findById(savedTrainee.getId()).isPresent());
    }

    @Test
    void findById() {
        Optional<Trainee> foundTrainee = traineeDAO.findById(trainee.getId());
        assertTrue(foundTrainee.isPresent());
        assertEquals("JohnDoe", foundTrainee.get().getUsername());
    }

    @Test
    void findByIdNotFound() {
        UUID randomId = UUID.randomUUID();
        Optional<Trainee> foundTrainee = traineeDAO.findById(randomId);

        assertFalse(foundTrainee.isPresent());
    }

    @Test
    void findByUsername() {
        Optional<Trainee> foundTrainee = traineeDAO.findByUsername("JohnDoe");

        assertTrue(foundTrainee.isPresent());
        assertEquals(trainee.getId(), foundTrainee.get().getId());
    }

    @Test
    void findByUsernameNotFound() {
        Optional<Trainee> foundTrainee = traineeDAO.findByUsername("NonExistentUser");

        assertFalse(foundTrainee.isPresent());
    }

    @Test
    void deleteById() {
        UUID id = trainee.getId();
        traineeDAO.deleteById(id);

        assertFalse(traineeDAO.findById(id).isPresent());
    }

    @Test
    void deleteByIdNotFound() {
        UUID randomId = UUID.randomUUID();
        assertThrows(IllegalArgumentException.class, () -> traineeDAO.deleteById(randomId));
    }

    @Test
    void existsByUsername() {
        assertTrue(traineeDAO.existsByUsername("JohnDoe"));
        assertFalse(traineeDAO.existsByUsername("NonExistentUser"));
    }

    @Test
    void findAll() {
        List<Trainee> trainees = traineeDAO.findAll();
        assertNotNull(trainees);
        assertEquals(1, trainees.size());
    }
}