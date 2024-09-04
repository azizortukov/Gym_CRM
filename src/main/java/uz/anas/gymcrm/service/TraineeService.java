package uz.anas.gymcrm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.anas.gymcrm.dao.TraineeDAO;
import uz.anas.gymcrm.entity.Trainee;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TraineeService {

    @Autowired
    private TraineeDAO traineeDAO;
    @Autowired
    private CredentialGenerator credentialGenerator;

    public Trainee createTrainee(Trainee trainee) {
        String username = trainee.getFirstName() + "." + trainee.getLastName();
        if (traineeDAO.existsByUsername(username)) {
            username = credentialGenerator.genUsername(username, traineeDAO);
        }
        trainee.setUsername(username);
        trainee.setPassword(credentialGenerator.genPassword());
        return traineeDAO.save(trainee);
    }

    public List<Trainee> getAllTrainees() {
        return traineeDAO.findAll();
    }

    public Optional<Trainee> getTraineeById(UUID id) {
        return traineeDAO.findById(id);
    }

    public Optional<Trainee> getTraineeByUsername(String username) {
        return traineeDAO.findByUsername(username);
    }

    public Trainee updateTrainee(Trainee trainee) {
        traineeDAO.save(trainee);
        return trainee;
    }

    public void deleteById(UUID traineeId) {
        traineeDAO.deleteById(traineeId);
    }

}
