package spagetti.tiimi.ticketguru.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import spagetti.tiimi.ticketguru.domain.AppUser;

public class AppUserTest {
    private AppUser user;

    @BeforeEach
    void setup() {
        user = new AppUser("testuser", "hashedPassword123", "John", "Doe", "USER");
    }

    @Test
    void shouldCreateAppUserWithCorrectFields() {
        assertEquals("testuser", user.getUsername());
        assertEquals("hashedPassword123", user.getPasswordHash());
        assertEquals("John", user.getFirstname());
        assertEquals("Doe", user.getLastname());
        assertEquals("USER", user.getUserRole());
    }

    @Test
    void shouldUpdatePasswordHash() {
        user.setPasswordHash("newHash456");
        assertEquals("newHash456", user.getPasswordHash());
    }

    @Test
    void shouldUpdateFirstnameAndLastname() {
        user.setFirstname("Jane");
        user.setLastname("Smith");
        assertEquals("Jane", user.getFirstname());
        assertEquals("Smith", user.getLastname());
    }

    @Test
    void shouldUpdateUserRole() {
        user.setUserRole("ADMIN");
        assertEquals("ADMIN", user.getUserRole());
    }

    @Test
    void shouldReturnFullName() {
        String fullName = user.getFirstname() + " " + user.getLastname();
        assertEquals("John Doe", fullName);
    }
}
