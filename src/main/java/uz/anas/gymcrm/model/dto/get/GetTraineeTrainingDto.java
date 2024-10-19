package uz.anas.gymcrm.model.dto.get;

import java.io.Serializable;
import java.sql.Date;

/**
 * DTO for {@link uz.anas.gymcrm.model.entity.Training}
 */
public record GetTraineeTrainingDto(
        String trainerName,
        String trainingName,
        String trainingType,
        Date trainingDate,
        int duration
) implements Serializable {
}