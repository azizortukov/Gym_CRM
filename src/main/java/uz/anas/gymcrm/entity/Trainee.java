package uz.anas.gymcrm.entity;

import lombok.*;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Trainee extends User {

    private LocalDate dateOfBirth;
    private Address address;

}
