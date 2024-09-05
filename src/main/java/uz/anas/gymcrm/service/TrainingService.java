package uz.anas.gymcrm.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.anas.gymcrm.entity.Training;
import uz.anas.gymcrm.repo.TrainingRepo;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TrainingService {


    private final TrainingRepo trainingRepo;

    public Training createTraining(Training training) {
        return trainingRepo.save(training);
    }

    public List<Training> getAllTrainings() {
        return trainingRepo.findAll();
    }

    public Optional<Training> getTrainingById(UUID id) {
        return trainingRepo.findById(id);
    }

    public Training updateTraining(Training trainee) {
        return trainingRepo.save(trainee);
    }

    public void deleteById(UUID traineeId) {
        trainingRepo.deleteById(traineeId);
    }

}
