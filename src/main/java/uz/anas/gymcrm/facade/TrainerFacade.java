package uz.anas.gymcrm.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uz.anas.gymcrm.entity.Trainer;
import uz.anas.gymcrm.service.TrainerService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class TrainerFacade {

    private final TrainerService trainerService;

    @Autowired
    public TrainerFacade(TrainerService trainerService) {
        this.trainerService = trainerService;
    }

    public Trainer createTrainer(Trainer trainer) {
        return trainerService.createTrainer(trainer);
    }

    public List<Trainer> getAllTrainers() {
        return trainerService.getAllTrainers();
    }

    public Optional<Trainer> getTrainerById(UUID id) {
        return trainerService.getTrainerById(id);
    }

    public Optional<Trainer> getTrainerByUsername(String username) {
        return trainerService.getTrainerByUsername(username);
    }

    public Trainer updateTrainer(Trainer trainer) {
        return trainerService.updateTrainer(trainer);
    }

    public void deleteTrainerById(UUID trainerId) {
        trainerService.deleteById(trainerId);
    }

}
