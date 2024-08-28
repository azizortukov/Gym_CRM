package uz.anas.gymcrm;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import uz.anas.gymcrm.config.AppConfig;
import uz.anas.gymcrm.dao.TraineeDAO;
import uz.anas.gymcrm.entity.Trainee;

public class Main {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        TraineeDAO bean = context.getBean(TraineeDAO.class);
        bean.save(new Trainee());
    }
}