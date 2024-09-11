package uz.anas.gymcrm;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import uz.anas.gymcrm.config.AppConfig;
import uz.anas.gymcrm.service.TraineeService;
import uz.anas.gymcrm.service.TrainerService;

public class Main {

    public static void main(String[] args) {
        var context = new AnnotationConfigApplicationContext(AppConfig.class);
        System.out.println("Mock datas: ");
        TraineeService traineeService = context.getBean(TraineeService.class);
        TrainerService trainerService = context.getBean(TrainerService.class);
        System.out.println("Trainees");
        traineeService.getAllTrainees().forEach(System.out::println);
        System.out.println("Trainers");
        trainerService.getAllTrainers().forEach(System.out::println);
    }
}