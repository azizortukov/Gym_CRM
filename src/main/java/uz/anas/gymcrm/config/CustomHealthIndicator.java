package uz.anas.gymcrm.config;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class CustomHealthIndicator implements HealthIndicator {

    @Override
    public Health health() {
        boolean isHealthy = checkMyService();
        if (isHealthy) {
            return Health.up().withDetail("status", "Service is healthy").build();
        } else {
            return Health.down().withDetail("status", "Service is not healthy").build();
        }
    }

    private boolean checkMyService() {
        // I can implement any health checks however, I don't know what to implement as health check
        return true;
    }
}
