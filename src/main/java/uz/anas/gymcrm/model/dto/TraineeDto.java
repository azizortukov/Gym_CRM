package uz.anas.gymcrm.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

/**
 * DTO for {@link uz.anas.gymcrm.model.entity.Trainee}
 */
public record TraineeDto(
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY, required = true)
        String username,
        @JsonProperty(required = true)
        String firstName,
        @JsonProperty(required = true)
        String lastName,
        Date dateOfBirth,
        String address,
        @JsonProperty(required = true)
        Boolean isActive,
        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        List<TrainerDto> trainersList
) implements Serializable {

}