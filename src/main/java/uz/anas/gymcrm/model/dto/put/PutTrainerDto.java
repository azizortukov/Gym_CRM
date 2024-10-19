package uz.anas.gymcrm.model.dto.put;

import uz.anas.gymcrm.model.dto.TraineeBaseDto;
import uz.anas.gymcrm.model.entity.enums.Specialization;

import java.io.Serializable;
import java.util.Set;

/**
 * DTO for {@link uz.anas.gymcrm.model.entity.Trainer}
 */
public record PutTrainerDto(
        Specialization specialization,
        String firstName,
        String lastName,
        String username,
        Boolean isActive,
        Set<TraineeBaseDto> traineesList) implements Serializable {
}