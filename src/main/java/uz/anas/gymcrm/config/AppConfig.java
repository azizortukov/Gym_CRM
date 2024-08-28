package uz.anas.gymcrm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import uz.anas.gymcrm.dao.TraineeDAO;
import uz.anas.gymcrm.dao.TrainerDAO;
import uz.anas.gymcrm.dao.TrainingDAO;
import uz.anas.gymcrm.service.TraineeService;
import uz.anas.gymcrm.service.TrainerService;
import uz.anas.gymcrm.service.TrainingService;

@Configuration
@ComponentScan(basePackages = "uz.anas.gymcrm")
public class AppConfig {

    @Bean
    public TraineeService traineeService(TraineeDAO traineeDAO) {
        return new TraineeService(traineeDAO);
    }

    @Bean
    public TrainerService trainerService(TrainerDAO trainerDAO) {
        return new TrainerService(trainerDAO);
    }

    @Bean
    public TrainingService trainingService(TrainingDAO trainingDAO) {
        return new TrainingService(trainingDAO);
    }

}
