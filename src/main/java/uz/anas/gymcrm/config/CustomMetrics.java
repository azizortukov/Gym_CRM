package uz.anas.gymcrm.config;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class CustomMetrics {

    public CustomMetrics(MeterRegistry registry) {
        registry.counter("custom_metric_counter", "type", "custom").increment();
        registry.gauge("custom_metric_gauge", 42);
    }
}

