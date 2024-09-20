package uz.anas.gymcrm.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class CredentialGeneratorTest {

    @InjectMocks
    private CredentialGenerator credentialGenerator;

    @Test
    public void genPasswordLength() {
        String password = credentialGenerator.genPassword();
        assertEquals(10, password.length(), "Password length should be 10 characters");
    }

    @Test
    public void genPasswordContainsValidCharacters() {
        String password = credentialGenerator.genPassword();
        String validCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_=+<>?";

        for (char c : password.toCharArray()) {
            assertTrue(validCharacters.indexOf(c) >= 0, "Password contains invalid character: " + c);
        }
    }

}