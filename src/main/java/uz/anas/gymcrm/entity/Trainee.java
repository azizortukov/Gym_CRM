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
public class Trainee {

    private UUID id;
    private LocalDate dateOfBirth;
    private Address address;
    private UUID userId;

}
