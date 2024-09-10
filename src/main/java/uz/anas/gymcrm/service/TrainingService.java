package uz.anas.gymcrm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.anas.gymcrm.dao.TrainingDAO;
import uz.anas.gymcrm.entity.Training;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TrainingService {

    @Autowired
    private TrainingDAO trainingDAO;

    public Training createTraining(Training training) {
        return trainingDAO.save(training);
    }

    public List<Training> getAllTrainings() {
        return trainingDAO.findAll();
    }

    public Optional<Training> getTrainingById(UUID id) {
        return trainingDAO.findById(id);
    }

    public Training updateTraining(Training trainee) {
        return trainingDAO.save(trainee);
    }

    public void deleteById(UUID traineeId) {
        trainingDAO.deleteById(traineeId);
    }

}
