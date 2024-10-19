package uz.anas.gymcrm.model.dto.get;

import jakarta.validation.constraints.NotNull;
import uz.anas.gymcrm.model.dto.TraineeBaseDto;
import uz.anas.gymcrm.model.entity.enums.Specialization;

import java.io.Serializable;
import java.util.Set;

/**
 * DTO for {@link uz.anas.gymcrm.model.entity.Trainer}
 */
public record GetTrainerDto(
        @NotNull Specialization specialization,
        Set<TraineeBaseDto> traineesList
) implements Serializable {
}