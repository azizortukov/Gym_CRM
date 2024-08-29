package uz.anas.gymcrm.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import uz.anas.gymcrm.entity.enums.Specialization;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Data
public class Trainer extends User {

    private Specialization specialization;

}
