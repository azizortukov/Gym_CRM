package uz.anas.gymcrm.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.sql.Date;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "training")
public class Training {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "trainee_id")
    @NotNull
    private Trainee trainee;
    @ManyToOne
    @JoinColumn(name = "trainer_id")
    @NotNull
    private Trainer trainer;
    @Column(nullable = false, name = "training_name")
    @NotEmpty
    private String trainingName;
    @ManyToOne
    @JoinColumn(name = "training_type_id")
    @NotNull
    private TrainingType trainingType;
    @Column(nullable = false, name = "training_date")
    @NotNull
    private Date trainingDate;
    @Column(nullable = false)
    @Positive
    private int duration;

}
