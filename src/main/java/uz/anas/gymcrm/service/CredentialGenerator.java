package uz.anas.gymcrm.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.anas.gymcrm.repo.UserRepo;

import java.security.SecureRandom;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class CredentialGenerator {

    private final Random random = new Random();
    private final SecureRandom secureRandom = new SecureRandom();
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_=+<>?";
    private final UserRepo userRepo;


    public String genPassword() {
        StringBuilder password = new StringBuilder(10);
        for (int i = 0; i < 10; i++) {
            int index = random.nextInt(CHARACTERS.length());
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
