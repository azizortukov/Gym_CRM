package uz.anas.gymcrm.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uz.anas.gymcrm.dao.UserDao;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CredentialGeneratorTest {

    @InjectMocks
    private CredentialGenerator credentialGenerator;

    @Mock
    private UserDao userDao;

    @Test
    void testGenPassword() {
        String password = credentialGenerator.genPassword();

        assertEquals(10, password.length(), "Password should be 10 characters long");
        assertTrue(password.chars().allMatch(c -> "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_=+<>?".indexOf(c) >= 0),
                "Password should only contain valid characters");
    }

    @Test
    void testGenUsernameUnique() {
        String baseUsername = "user";
        String existingUsername = "user123";

        when(userDao.existsByUsername(anyString()))
                .thenReturn(true)
                .thenReturn(false);

        String generatedUsername = credentialGenerator.genUsername(baseUsername, userDao);
        assertNotEquals(existingUsername, generatedUsername, "Username should be unique and not equal to existing username");
        assertTrue(generatedUsername.startsWith(baseUsername), "Generated username should start with baseUsername");
        verify(userDao, atLeast(2)).existsByUsername(anyString());
    }

}