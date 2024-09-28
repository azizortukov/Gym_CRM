package uz.anas.gymcrm.model.dto;

import java.io.Serializable;

public record TrainerTraineesDto(
        String firstName,
        String lastName,
        String username
) implements Serializable {
}