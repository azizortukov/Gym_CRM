package uz.anas.gymcrm.model.dto;

import uz.anas.gymcrm.model.entity.enums.Specialization;

import java.io.Serializable;

public record TraineeTrainersDto(
        String firstName,
        String lastName,
        String username,
        Specialization specialization
) implements Serializable {
}