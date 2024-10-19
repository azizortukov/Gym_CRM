package uz.anas.gymcrm.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import uz.anas.gymcrm.model.entity.enums.Specialization;

import java.util.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "trainer")
public class Trainer {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Enumerated(EnumType.STRING)
    @NotNull
    private Specialization specialization;
    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @NotNull
    private User user;
    @ManyToMany(mappedBy = "trainers")
    @ToString.Exclude
    private Set<Trainee> trainees = new HashSet<>();
    @OneToMany(mappedBy = "trainer", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Training> trainings = new ArrayList<>();

    public Trainer(User user, Specialization specialization) {
        this.user = user;
        this.specialization = specialization;
    }
}