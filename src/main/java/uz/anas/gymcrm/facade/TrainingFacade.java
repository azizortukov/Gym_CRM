package uz.anas.gymcrm.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uz.anas.gymcrm.entity.Training;
import uz.anas.gymcrm.service.TrainingService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class TrainingFacade {

    private final TrainingService trainingService;

    @Autowired
    public TrainingFacade(TrainingService trainingService) {
        this.trainingService = trainingService;
    }

    public Training createTraining(Training training) {
        return trainingService.createTraining(training);
    }

    public List<Training> getAllTrainings() {
        return trainingService.getAllTrainings();
    }

    public Optional<Training> getTrainingById(UUID id) {
        return trainingService.getTrainingById(id);
    }

    public Training updateTraining(Training training) {
        return trainingService.updateTraining(training);
    }

    public void deleteTrainingById(UUID trainingId) {
        trainingService.deleteById(trainingId);
    }
}

