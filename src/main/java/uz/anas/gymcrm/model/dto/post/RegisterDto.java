package uz.anas.gymcrm.model.dto.post;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDto {
    @NotNull
    @Email
    @NotEmpty
    @NotBlank
    String email;
    @NotNull
    @NotEmpty
    @NotBlank
    String firstName;
    @NotNull
    @NotEmpty
    @NotBlank
    String lastName;
    @NotNull
    @NotEmpty
    @NotBlank
    String password;
}