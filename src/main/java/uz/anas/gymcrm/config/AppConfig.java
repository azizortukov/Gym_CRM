package uz.anas.gymcrm.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan(basePackages = "uz.anas.gymcrm")
@EnableTransactionManagement
public class AppConfig {



}
