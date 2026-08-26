package it.polimi.booknest.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Attiva l'esecuzione dei task pianificati dell'applicazione.
 * <p>
 * Lo scheduler è disattivato nel profilo {@code test}: la generazione automatica
 * degli Showdown scriverebbe sul database mentre i test sono in esecuzione,
 * rendendo non deterministico l'insieme di dati che i test osservano.
 */
@Configuration
@EnableScheduling
@Profile("!test")
public class SchedulingConfig {
}
