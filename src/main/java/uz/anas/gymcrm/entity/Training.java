package uz.anas.gymcrm.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private Trainee trainee;
    @ManyToOne
    @JoinColumn(name = "trainer_id")
    private Trainer trainer;
    @Column(nullable = false, name = "training_name")
    private String trainingName;
    @ManyToOne
    @JoinColumn(name = "training_type_id")
    private TrainingType trainingType;
    @Column(nullable = false, name = "training_date")
    private Date trainingDate;
    @Column(nullable = false)
    private int duration;

}
