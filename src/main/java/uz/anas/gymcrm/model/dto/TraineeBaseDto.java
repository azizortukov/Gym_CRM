package uz.anas.gymcrm.model.dto;

import java.io.Serializable;

public record TraineeBaseDto(
        String firstName,
        String lastName,
        String username
) implements Serializable {
}