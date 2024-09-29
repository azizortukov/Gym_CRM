package uz.anas.gymcrm.model.dto.put;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PutLoginDetailsDto(
        @JsonProperty(required = true)
        String username,
        @JsonProperty(required = true)
        String oldPassword,
        @JsonProperty(required = true)
        String newPassword
) {
}
