package uz.anas.gymcrm.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.*;
import uz.anas.gymcrm.model.dto.Authentication;
import uz.anas.gymcrm.model.dto.ResponseDto;
import uz.anas.gymcrm.model.dto.put.PutLoginDetailsDto;
import uz.anas.gymcrm.repository.UserRepository;

@RequestMapping("/api/v1/login")
@RestController
@RequiredArgsConstructor
@Tag(name = "Login APIs", description = "For checking login details and changing password")
public class LoginController {

    private final UserRepository userRepo;
    private final Log log = LogFactory.getLog(LoginController.class);


    @Operation(summary = "Checking login details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = """
                    Provided username and password are right""",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDto.class)))
    })
    @GetMapping
    public ResponseDto<?> login(@RequestBody Authentication auth) {
        if (!userRepo.existsByUsernameAndPassword(auth.username(), auth.password())) {
            log.warn("User details entered wrong. username: %s   password: %s".formatted(auth.username(), auth.password()));
            return new ResponseDto<>("User details are incorrect");
        }
        return new ResponseDto<>();
    }

    @Operation(summary = "Updating user password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = """
                    Provided username and password are right""",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDto.class)))
    })
    @PutMapping
    public ResponseDto<?> changePassword(@RequestBody PutLoginDetailsDto detailsDto) {
        if (!userRepo.existsByUsernameAndPassword(detailsDto.username(), detailsDto.oldPassword())) {
            log.warn("User entered wrong details for password change. username: %s   password: %s"
                    .formatted(detailsDto.username(), detailsDto.oldPassword()));
            return new ResponseDto<>("User details are incorrect");
        }
        return new ResponseDto<>();
    }

}
