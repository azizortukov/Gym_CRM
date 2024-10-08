package uz.anas.gymcrm.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false, name = "first_name")
    @NotEmpty
    private String firstName;
    @Column(nullable = false, name = "last_name")
    @NotEmpty
    private String lastName;
    @Column(nullable = false)
    @NotEmpty
    private String username;
    @Column(nullable = false)
    @NotEmpty
    private String password;
    @Column(nullable = false, name = "is_active")
    private boolean isActive;

}
