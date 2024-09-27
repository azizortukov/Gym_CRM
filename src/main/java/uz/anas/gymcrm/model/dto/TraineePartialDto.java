package uz.anas.gymcrm.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import uz.anas.gymcrm.model.entity.Trainee;

import java.io.Serializable;
import java.sql.Date;

/**
 * DTO for {@link Trainee}
 */
public record TraineePartialDto(
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        Date dateOfBirth,
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        String address,
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY, required = true)
        String firstName,
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY, required = true)
        String lastName,
        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        String username,
        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        String password
) implements Serializable {
}