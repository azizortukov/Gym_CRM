package uz.anas.gymcrm.model.dto.post;

import com.fasterxml.jackson.annotation.JsonProperty;
import uz.anas.gymcrm.model.entity.enums.Specialization;

import java.io.Serializable;

/**
 * DTO for {@link uz.anas.gymcrm.model.entity.Trainer}
 */
public record PostTrainerDto(

        @JsonProperty(required = true, access = JsonProperty.Access.WRITE_ONLY)
        Specialization specialization,
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