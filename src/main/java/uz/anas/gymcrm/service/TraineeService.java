package uz.anas.gymcrm.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.anas.gymcrm.entity.Trainee;
import uz.anas.gymcrm.repo.TraineeRepo;
import uz.anas.gymcrm.repo.UserRepo;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TraineeService {

    private final TraineeRepo traineeRepo;
    private final CredentialGenerator credentialGenerator;
    private final UserRepo userRepo;

    public Trainee createTrainee(Trainee trainee) {
        String username = trainee.getUser().getUsername() + "." + trainee.getUser().getLastName();
        if (userRepo.existsByUsername(username)) {
            username = credentialGenerator.genUsername(username);
        }
        trainee.getUser().setUsername(username);
        trainee.getUser().setPassword(credentialGenerator.genPassword());
        return traineeRepo.save(trainee);
    }

    public List<Trainee> getAllTrainees() {
        return traineeRepo.findAll();
    }

    public Optional<Trainee> getTraineeByUsername(String username) {
        return traineeRepo.findByUsername(username);
    }

    public Trainee updateTrainee(Trainee trainee) {
        traineeRepo.save(trainee);
        return trainee;
    }

    public void deleteById(UUID traineeId) {
        traineeRepo.deleteById(traineeId);
    }

}
