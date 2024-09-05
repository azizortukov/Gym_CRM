package uz.anas.gymcrm.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.anas.gymcrm.entity.Trainer;
import uz.anas.gymcrm.repo.TrainerRepo;
import uz.anas.gymcrm.repo.UserRepo;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TrainerService {

    private final TrainerRepo trainerRepo;
    private final CredentialGenerator credentialGenerator;
    private final UserRepo userRepo;

    public Trainer createTrainer(Trainer trainer) {
        String username = trainer.getUser().getFirstName() + "." + trainer.getUser().getLastName();
        if (userRepo.existsByUsername(username)) {
            username = credentialGenerator.genUsername(username);
        }
        trainer.getUser().setUsername(username);
        trainer.getUser().setPassword(credentialGenerator.genPassword());
        return trainerRepo.save(trainer);
    }

    public List<Trainer> getAllTrainers() {
        return trainerRepo.findAll();
    }

    public Optional<Trainer> getTrainerById(UUID id) {
        return trainerRepo.findById(id);
    }

    public Optional<Trainer> getTrainerByUsername(String username) {
        return trainerRepo.findByUsername(username);
    }

    public Trainer updateTrainer(Trainer trainer) {
        trainerRepo.save(trainer);
        return trainer;
    }

    public void deleteById(UUID trainerId) {
        trainerRepo.deleteById(trainerId);
    }

}
