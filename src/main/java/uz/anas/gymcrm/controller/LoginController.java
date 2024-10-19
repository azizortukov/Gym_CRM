package uz.anas.gymcrm.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import uz.anas.gymcrm.model.dto.Authentication;
import uz.anas.gymcrm.model.dto.ResponseDto;
import uz.anas.gymcrm.model.dto.put.PutLoginDetailsDto;
import uz.anas.gymcrm.service.CredentialService;

@RequestMapping("/api/v1/login")
@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Login APIs", description = "For checking login details and changing password")
public class LoginController {

    private final CredentialService credentialService;


    @Operation(summary = "Checking login details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = """
                    Provided username and password are right""",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDto.class)))
    })
    @GetMapping
    public ResponseDto<?> login(@RequestParam(name = "username") String username, @RequestParam(name = "password") String password) {
        var auth = new Authentication(username, password);
        return credentialService.login(auth);
    }

    @Operation(summary = "Updating user password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = """
                    Provided username and password are right""",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDto.class)))
    })
    @PutMapping
    public ResponseDto<?> changePassword(@RequestBody PutLoginDetailsDto detailsDto) {
        return credentialService.updatePassword(detailsDto);
    }

}
