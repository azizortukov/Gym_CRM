package uz.anas.gymcrm.model.dto.put;

import com.fasterxml.jackson.annotation.JsonProperty;
import uz.anas.gymcrm.model.dto.TraineeTrainersDto;

import java.io.Serializable;
import java.sql.Date;
import java.util.Set;

/**
 * DTO for {@link uz.anas.gymcrm.model.entity.Trainee}
 */
public record PutTraineeDto(
        Date dateOfBirth,
        String address,
        @JsonProperty(required = true)
        String firstName,
        @JsonProperty(required = true)
        String lastName,
        @JsonProperty(required = true)
        String username,
        @JsonProperty(required = true)
        Boolean isActive,
        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        Set<TraineeTrainersDto> trainersList
) implements Serializable {
}