package uz.anas.gymcrm.model.dto;

import jakarta.validation.constraints.NotNull;
import uz.anas.gymcrm.model.entity.enums.Specialization;

import java.io.Serializable;

/**
 * DTO for {@link uz.anas.gymcrm.model.entity.Trainer}
 */
public record TrainerDto(
        @NotNull
        Specialization specialization,
        String userFirstName,
        String userLastName,
        String userUsername
) implements Serializable {
}
