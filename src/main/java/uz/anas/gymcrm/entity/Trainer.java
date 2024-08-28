package uz.anas.gymcrm.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.anas.gymcrm.entity.enums.Specialization;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Trainer {

    private UUID id;
    private Specialization specialization;
    private UUID userId;

}
