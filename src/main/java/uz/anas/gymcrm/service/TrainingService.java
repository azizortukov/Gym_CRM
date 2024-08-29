package uz.anas.gymcrm.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.anas.gymcrm.dao.TrainingDAO;
import uz.anas.gymcrm.entity.Training;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TrainingService {

    private final TrainingDAO trainingDAO;

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
        trainingDAO.save(trainee);
        return trainee;
    }

    public void deleteTraining(Training trainee) {
        trainingDAO.deleteById(trainee.getId());
    }

    public void deleteById(UUID traineeId) {
        trainingDAO.deleteById(traineeId);
    }

}
