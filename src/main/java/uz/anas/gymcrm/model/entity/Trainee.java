package uz.anas.gymcrm.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;
import java.util.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "trainee")
public class Trainee {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(name = "date_of_birth")
    private Date dateOfBirth;
    private String address;
    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToMany
    @JoinTable(name = "trainee_trainer", joinColumns = @JoinColumn(name = "trainee_id"),
            inverseJoinColumns = @JoinColumn(name = "trainer_id"))
    @ToString.Exclude
    private Set<Trainer> trainers = new HashSet<>();
    @OneToMany(mappedBy = "trainee", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Training> trainings = new ArrayList<>();

    public Trainee(User user, Date date, String address) {
        this.user = user;
        this.dateOfBirth = date;
        this.address = address;
    }
}
