package uz.anas.gymcrm.service;

import org.springframework.stereotype.Service;
import uz.anas.gymcrm.dao.UserDao;

import java.security.SecureRandom;
import java.util.Random;

@Service
public class CredentialGenerator {

    private final Random random = new Random();
    private final SecureRandom secureRandom = new SecureRandom();
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_=+<>?";


    public String genPassword() {
        StringBuilder password = new StringBuilder(10);
        for (int i = 0; i < 10; i++) {
            int index = random.nextInt(CHARACTERS.length());
            password.append(CHARACTERS.charAt(index));
        }
        return password.toString();
    }

    public String genUsername(String username, UserDao userDao) {
        int serialNumber = secureRandom.nextInt(10, 1000000);
        String resUsername = username + serialNumber;
        if (userDao.existsByUsername(resUsername)) {
            return genUsername(username, userDao);
        }
        return resUsername;
    }

}
