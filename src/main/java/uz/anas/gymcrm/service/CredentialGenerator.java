package uz.anas.gymcrm.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.anas.gymcrm.repository.UserRepository;

import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
public class CredentialGenerator {

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

}
