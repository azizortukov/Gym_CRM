package uz.anas.gymcrm.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class User {

    private UUID id;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private boolean isActive;

}
