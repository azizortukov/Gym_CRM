package uz.anas.gymcrm.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.anas.gymcrm.dao.TrainerDAO;
import uz.anas.gymcrm.entity.Trainer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TrainerService {

    private final TrainerDAO trainerDAO;
    private final CredentialGenerator credentialGenerator;

    public Trainer createTrainer(Trainer trainer) {
        String username = trainer.getFirstName() + "." + trainer.getLastName();
        if (trainerDAO.existsByUsername(username)) {
            username = credentialGenerator.genUsername(username, trainerDAO);
        }
        trainer.setUsername(username);
        trainer.setPassword(credentialGenerator.genPassword());
        return trainerDAO.save(trainer);
    }

    public List<Trainer> getAllTrainers() {
        return trainerDAO.findAll();
    }

    public Optional<Trainer> getTrainerById(UUID id) {
        return trainerDAO.findById(id);
    }

    public Optional<Trainer> getTrainerByUsername(String username) {
        return trainerDAO.findByUsername(username);
    }

    public Trainer updateTrainer(Trainer trainer) {
        trainerDAO.save(trainer);
        return trainer;
    }

    public void deleteById(UUID trainerId) {
        trainerDAO.deleteById(trainerId);
    }

}
