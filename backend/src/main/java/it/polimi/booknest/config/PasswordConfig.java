package it.polimi.booknest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Dichiara l'algoritmo di cifratura utilizzato per le password degli utenti.
 * <p>
 * Le password non vengono mai conservate in chiaro: alla registrazione viene
 * salvato il solo digest, e in fase di accesso la password inserita viene
 * ricodificata e confrontata con quello memorizzato.
 */
@Configuration
public class PasswordConfig {

    /**
     * Espone l'encoder utilizzato dal livello di servizio per cifrare e
     * verificare le password.
     * <p>
     * BCrypt incorpora un salt casuale in ogni digest prodotto, così due utenti
     * con la stessa password ottengono hash diversi. Il tipo dichiarato è
     * l'interfaccia {@link PasswordEncoder} e non l'implementazione concreta,
     * in modo che le classi che lo utilizzano non dipendano dall'algoritmo scelto.
     *
     * @return l'encoder BCrypt gestito dal contesto applicativo
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
