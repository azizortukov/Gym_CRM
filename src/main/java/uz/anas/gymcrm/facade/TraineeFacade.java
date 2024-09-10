package uz.anas.gymcrm.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uz.anas.gymcrm.entity.Trainee;
import uz.anas.gymcrm.service.TraineeService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class TraineeFacade {

    private final TraineeService traineeService;

    @Autowired
    public TraineeFacade(TraineeService traineeService) {
        this.traineeService = traineeService;
    }

    public Trainee createTrainee(Trainee trainee) {
        return traineeService.createTrainee(trainee);
    }

    public List<Trainee> getAllTrainees() {
        return traineeService.getAllTrainees();
    }

    public Optional<Trainee> getTraineeById(UUID id) {
        return traineeService.getTraineeById(id);
    }

    public Optional<Trainee> getTraineeByUsername(String username) {
        return traineeService.getTraineeByUsername(username);
    }

    public Trainee updateTrainee(Trainee trainee) {
        return traineeService.updateTrainee(trainee);
    }

    public void deleteTraineeById(UUID traineeId) {
        traineeService.deleteById(traineeId);
    }
}

