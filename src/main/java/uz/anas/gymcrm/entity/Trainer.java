package uz.anas.gymcrm.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.anas.gymcrm.entity.enums.Specialization;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "trainer")
public class Trainer {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @NotNull
    private UUID id;
    @Enumerated(EnumType.STRING)
    @NotNull
    private Specialization specialization;
    @OneToOne(cascade = CascadeType.PERSIST)
    @NotNull
    private User user;

}
