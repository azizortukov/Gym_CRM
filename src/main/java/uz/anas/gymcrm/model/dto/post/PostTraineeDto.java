package uz.anas.gymcrm.model.dto.post;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.sql.Date;

/**
 * DTO for {@link uz.anas.gymcrm.model.entity.Trainee}
 */
public record PostTraineeDto(
        Date dateOfBirth,
        String address,
        @JsonProperty(required = true, access = JsonProperty.Access.WRITE_ONLY)
        String firstName,
        @JsonProperty(required = true, access = JsonProperty.Access.WRITE_ONLY)
        String lastName,
        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        String username,
        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        String password
) implements Serializable {
  }