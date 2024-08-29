package uz.anas.gymcrm.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uz.anas.gymcrm.entity.Training;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TrainingDAOTest {

    private TrainingDAO trainingDAO;
    private Training training;

    @BeforeEach
    void setUp() {
        trainingDAO = new TrainingDAO();
        training = new Training();
        training.setTrainingName("Cardio");
        trainingDAO.save(training);
    }

    @Test
    void save() {
        Training newTraining = new Training();
        newTraining.setTrainingName("BodyBuilding");

        Training savedTraining = trainingDAO.save(newTraining);

        assertNotNull(savedTraining.getId());
        assertEquals("BodyBuilding", savedTraining.getTrainingName());
        assertTrue(trainingDAO.findById(savedTraining.getId()).isPresent());
    }

    @Test
    void findById() {
        Optional<Training> foundTraining = trainingDAO.findById(training.getId());

        assertTrue(foundTraining.isPresent());
        assertEquals("Cardio", foundTraining.get().getTrainingName());
    }

    @Test
    void findByIdNotFound() {
        UUID randomId = UUID.randomUUID();
        Optional<Training> foundTraining = trainingDAO.findById(randomId);

        assertFalse(foundTraining.isPresent());
    }

    @Test
    void deleteById() {
        UUID id = training.getId();
        trainingDAO.deleteById(id);

        assertFalse(trainingDAO.findById(id).isPresent());
    }

    @Test
    void deleteByIdNotFound() {
        UUID randomId = UUID.randomUUID();
        assertThrows(IllegalArgumentException.class, () -> trainingDAO.deleteById(randomId));
    }

    @Test
    void findAll() {
        Training additionalTraining = new Training();
        additionalTraining.setTrainingName("Running");
        trainingDAO.save(additionalTraining);

        List<Training> allTrainings = trainingDAO.findAll();

        assertEquals(2, allTrainings.size());
        assertTrue(allTrainings.stream().anyMatch(t -> "Cardio".equals(t.getTrainingName())));
        assertTrue(allTrainings.stream().anyMatch(t -> "Running".equals(t.getTrainingName())));
    }
}