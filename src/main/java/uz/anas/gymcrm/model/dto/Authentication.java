package uz.anas.gymcrm.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;

public record Authentication (
        @NotBlank(message = "username cannot be empty")
        @JsonProperty(required = true)
        String username,
        @NotBlank(message = "password cannot be empty")
        @JsonProperty(required = true)
        String password) implements Serializable {
}
