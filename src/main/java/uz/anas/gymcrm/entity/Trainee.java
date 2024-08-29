package uz.anas.gymcrm.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Data
public class Trainee extends User {

    private LocalDate dateOfBirth;
    private Address address;

}
