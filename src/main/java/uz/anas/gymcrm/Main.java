package uz.anas.gymcrm;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import uz.anas.gymcrm.config.AppConfig;

public class Main {

    public static void main(String[] args) {
        var applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
    }
}