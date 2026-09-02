# BookNest

Diario di lettura e piattaforma di condivisione tra lettori, realizzato come prova finale
del corso di Ingegneria del Software del Politecnico di Milano.

Un utente registrato tiene traccia dei libri che vuole leggere, di quelli in corso e di
quelli finiti, assegna voti, scrive recensioni, segue altri lettori e partecipa alle sfide
tra libri generate dal sistema. Un visitatore consulta catalogo, recensioni pubbliche,
voti medi e classifiche senza registrarsi.

## Struttura del progetto

- `backend/` — applicazione Spring Boot che espone le API REST
- `frontend/` — applicazione Flutter Web

## Tecnologie

Java 21, Spring Boot, Spring Data JPA con Hibernate, PostgreSQL, Flutter Web (Dart).
Test con JUnit 5 e Mockito, copertura misurata con JaCoCo.

## Avvio

**Database.** Creare un database PostgreSQL chiamato `booknest_db`. Le credenziali di
connessione sono in `backend/src/main/resources/application.properties`. Lo schema è
generato automaticamente all'avvio e popolato con i dati di esempio in `data.sql`.

**Backend.** Dalla cartella `backend`:

    ./mvnw spring-boot:run

Il server si avvia su `http://localhost:8080`.

**Frontend.** Dalla cartella `frontend`:

    flutter run -d chrome

**Utenti di prova.** Il database di esempio contiene quattro utenti; le credenziali di
accesso sono indicate nella documentazione di progetto.

## Test

Dalla cartella `backend`:

    ./mvnw test

Il report di copertura JaCoCo è generato in `backend/target/site/jacoco/index.html`.