package uz.anas.gymcrm.controller;


import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.anas.gymcrm.model.dto.Authentication;
import uz.anas.gymcrm.model.dto.put.PutLoginDetailsDto;
import uz.anas.gymcrm.repository.UserRepository;

@RequestMapping("/api/v1/login")
@RestController
@RequiredArgsConstructor
public class LoginController {

    private final UserRepository userRepo;
    private final Log log = LogFactory.getLog(LoginController.class);

    @GetMapping
    public ResponseEntity<?> login(@RequestBody Authentication auth) {
        if (!userRepo.existsByUsernameAndPassword(auth.username(), auth.password())) {
            log.warn("User details entered wrong. username: %s   password: %s".formatted(auth.username(), auth.password()));
            throw new RuntimeException("User details are incorrect");
        }
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<?> changePassword(@RequestBody PutLoginDetailsDto detailsDto) {
        if (!userRepo.existsByUsernameAndPassword(detailsDto.username(), detailsDto.oldPassword())) {
            log.warn("User entered wrong details for password change. username: %s   password: %s"
                    .formatted(detailsDto.username(), detailsDto.oldPassword()));
            throw new RuntimeException("User details are incorrect");
        }
        return ResponseEntity.ok().build();
    }

}
