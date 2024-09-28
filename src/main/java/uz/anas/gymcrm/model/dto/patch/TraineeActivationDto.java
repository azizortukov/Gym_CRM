package uz.anas.gymcrm.model.dto.patch;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TraineeActivationDto(
        @JsonProperty(required = true)
        String username,
        @JsonProperty(required = true)
        Boolean isActive
) {
}
