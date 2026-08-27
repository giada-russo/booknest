package it.polimi.booknest;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

class GeneraHashTest {
    @Test
    void stampaHash() {
        System.out.println(new BCryptPasswordEncoder().encode("password"));
    }
}