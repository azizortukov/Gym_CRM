package uz.anas.gymcrm.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uz.anas.gymcrm.entity.Trainer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TrainerDAOTest {

    private TrainerDAO trainerDAO;
    private Trainer trainer;

    @BeforeEach
    void setUp() {
        trainerDAO = new TrainerDAO();
        trainer = new Trainer();
        trainer.setUsername("JohnDoe");
        trainerDAO.save(trainer);
    }

    @Test
    void save() {
        Trainer trainee = new Trainer();
        trainee.setUsername("JohnDoe");

        Trainer savedTrainer = trainerDAO.save(trainee);
        assertNotNull(savedTrainer.getId());
        assertEquals("JohnDoe", savedTrainer.getUsername());
        assertTrue(trainerDAO.findById(savedTrainer.getId()).isPresent());
    }

    @Test
    void findById() {
        Optional<Trainer> foundTrainer = trainerDAO.findById(trainer.getId());
        assertTrue(foundTrainer.isPresent());
        assertEquals("JohnDoe", foundTrainer.get().getUsername());
    }

    @Test
    void findByIdNotFound() {
        UUID randomId = UUID.randomUUID();
        Optional<Trainer> foundTrainer = trainerDAO.findById(randomId);

        assertFalse(foundTrainer.isPresent());
    }

    @Test
    void findByUsername() {
        Optional<Trainer> foundTrainer = trainerDAO.findByUsername("JohnDoe");

        assertTrue(foundTrainer.isPresent());
        assertEquals(trainer.getId(), foundTrainer.get().getId());
    }

    @Test
    void findByUsernameNotFound() {
        Optional<Trainer> foundTrainer = trainerDAO.findByUsername("NonExistentUser");

        assertFalse(foundTrainer.isPresent());
    }

    @Test
    void deleteById() {
        UUID id = trainer.getId();
        trainerDAO.deleteById(id);

        assertFalse(trainerDAO.findById(id).isPresent());
    }

    @Test
    void deleteByIdNotFound() {
        UUID randomId = UUID.randomUUID();
        assertThrows(IllegalArgumentException.class, () -> trainerDAO.deleteById(randomId));
    }

    @Test
    void existsByUsername() {
        assertTrue(trainerDAO.existsByUsername("JohnDoe"));
        assertFalse(trainerDAO.existsByUsername("NonExistentUser"));
    }

    @Test
    void findAll() {
        List<Trainer> trainers = trainerDAO.findAll();
        assertNotNull(trainers);
        assertEquals(1, trainers.size());
    }
}