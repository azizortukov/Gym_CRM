package uz.anas.gymcrm.entity;

import lombok.*;
import uz.anas.gymcrm.entity.enums.Specialization;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Trainer extends User {

    private Specialization specialization;

}
