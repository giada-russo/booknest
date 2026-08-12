package it.polimi.booknest.model;


import jakarta.persistence.*;

/**
 * Rappresenta un utente registrato della piattaforma BookNest.
 * <p>
 * È l'entità attorno a cui ruota l'intera attività della piattaforma: ogni
 * catalogazione, recensione, voto e relazione di follow appartiene a un utente.
 * Lo username è l'identificativo con cui l'utente accede al sistema, quindi
 * username ed email sono soggetti a vincolo di unicità a livello di database.
 * <p>
 * La password non viene mai conservata in chiaro: il campo {@code passwordHash}
 * contiene il digest prodotto dall'algoritmo BCrypt in fase di registrazione.
 */
@Entity
public class Utente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String cognome;
    @Column(unique = true, nullable = false)
    private String username;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private String passwordHash;


    public Utente(String nome, String cognome, String username, String email,  String passwordHash) {
        this.nome = nome;
        this.cognome = cognome;
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
    }

    public Utente() {}

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
}

