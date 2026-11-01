package com.example.demo.service;

import com.example.demo.model.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AuthServiceTest {

        private final AuthService authService = new AuthService();
        private final User user = new User(
            1L,
            "etudiant@smartcampus.ma",
            "password123",
            true
        );

    @Test
    void shouldAuthenticateWithCorrectCredentials() {
        assertTrue(authService.authenticate(
                user,
                "etudiant@smartcampus.ma",
                "password123"
        ));
    }

    @Test
    void shouldRejectIncorrectPassword() {
        assertFalse(authService.authenticate(
                user,
                "etudiant@smartcampus.ma",
                "wrongpassword"
        ));
    }

    @Test
    void shouldRejectInactiveUser() {
        user.setActive(false);

        assertFalse(authService.authenticate(
                user,
                "etudiant@smartcampus.ma",
                "password123"
        ));
    }

    @Test
    void shouldRejectUnknownUser() {
        assertFalse(authService.authenticate(
                null,
                "etudiant@smartcampus.ma",
                "password123"
        ));
    }
}
