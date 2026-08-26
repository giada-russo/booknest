package it.polimi.booknest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Punto di ingresso dell'applicazione BookNest.
 * <p>
 * L'annotazione {@code @SpringBootApplication} attiva la configurazione automatica
 * e la scansione dei componenti a partire dal package {@code it.polimi.booknest}.
 */
@SpringBootApplication
public class BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

}
