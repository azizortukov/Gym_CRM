package uz.anas.gymcrm.entity;

import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString(callSuper = true)
public class Training {

    private UUID id;
    private Trainee trainee;
    private Trainer trainer;
    private String trainingName;
    private String trainingType;
    private LocalDate trainingDate;
    private long duration;

}
