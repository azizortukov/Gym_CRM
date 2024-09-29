package uz.anas.gymcrm.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import uz.anas.gymcrm.model.entity.enums.Specialization;

import java.io.Serializable;

public record TrainerBaseDto(
        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        String firstName,
        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        String lastName,
        String username,
        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        Specialization specialization
) implements Serializable {
}