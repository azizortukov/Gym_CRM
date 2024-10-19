package uz.anas.gymcrm.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.anas.gymcrm.model.dto.Authentication;
import uz.anas.gymcrm.model.dto.ResponseDto;
import uz.anas.gymcrm.model.dto.put.PutLoginDetailsDto;
import uz.anas.gymcrm.repository.UserRepository;

import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
@Slf4j
public class CredentialService {

    private final SecureRandom secureRandom = new SecureRandom();
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_=+<>?";
    private final UserRepository userRepo;


    public String genPassword() {
        StringBuilder password = new StringBuilder(10);
        for (int i = 0; i < 10; i++) {
            int index = secureRandom.nextInt(CHARACTERS.length());
            password.append(CHARACTERS.charAt(index));
        }
        return password.toString();
    }

    public String genUsername(String username) {
        int serialNumber = secureRandom.nextInt(10, 1000000);
        String resUsername = username + serialNumber;
        if (userRepo.existsByUsername(resUsername)) {
            return genUsername(username);
        }
        return resUsername;
    }

    public ResponseDto<?> login(Authentication auth) {
        if (!userRepo.existsByUsernameAndPassword(auth.username(), auth.password())) {
            log.warn("User details entered wrong. username: {}   password: {}", auth.username(), auth.password());
            return new ResponseDto<>("User details are incorrect");
        }
        return new ResponseDto<>();
    }

    public ResponseDto<?> updatePassword(PutLoginDetailsDto detailsDto) {
        if (!userRepo.existsByUsernameAndPassword(detailsDto.username(), detailsDto.oldPassword())) {
            log.warn("User entered wrong details for password change. username: {}   password: {}",
                    detailsDto.username(), detailsDto.oldPassword());
            return new ResponseDto<>("User details are incorrect");
        }
        userRepo.updatePasswordByDetails(detailsDto);
        return new ResponseDto<>();
    }
}
