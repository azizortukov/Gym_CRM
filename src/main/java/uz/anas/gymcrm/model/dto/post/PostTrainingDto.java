package uz.anas.gymcrm.model.dto.post;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.io.Serializable;
import java.sql.Date;

/**
 * DTO for {@link uz.anas.gymcrm.model.entity.Training}
 */
public record PostTrainingDto(
        @JsonProperty(required = true)
        String traineeUserUsername,
        @JsonProperty(required = true)
        String trainerUserUsername,
        @JsonProperty(required = true)
        @NotEmpty
        String trainingName,
        @JsonProperty(required = true)
        @NotNull
        Date trainingDate,
        @JsonProperty(required = true)
        @Positive
        int duration
) implements Serializable {
}