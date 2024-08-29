package uz.anas.gymcrm.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Training {

    private UUID id;
    private UUID traineeId;
    private UUID trainerId;
    private String trainingName;
    private String trainingType;
    private LocalDate trainingDate;
    private long duration;

}
