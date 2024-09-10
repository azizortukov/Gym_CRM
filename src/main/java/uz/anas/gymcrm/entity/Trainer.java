package uz.anas.gymcrm.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import uz.anas.gymcrm.entity.enums.Specialization;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
@ToString(callSuper = true)
public class Trainer extends User {

    private Specialization specialization;

}
