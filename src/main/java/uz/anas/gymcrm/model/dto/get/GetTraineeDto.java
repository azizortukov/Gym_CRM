package uz.anas.gymcrm.model.dto.get;

import com.fasterxml.jackson.annotation.JsonProperty;
import uz.anas.gymcrm.model.dto.TrainerBaseDto;
import uz.anas.gymcrm.model.entity.Trainee;

import java.io.Serializable;
import java.sql.Date;
import java.util.Set;

/**
 * DTO for {@link Trainee}
 */
public record GetTraineeDto(
        Date dateOfBirth,
        String address,
        String firstName,
        String lastName,
        boolean isActive,
        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        Set<TrainerBaseDto> trainersList
) implements Serializable {
}