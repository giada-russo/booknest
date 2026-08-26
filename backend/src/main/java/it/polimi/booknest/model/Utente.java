package it.polimi.booknest.model;


import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

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
    /**
     * Utenti seguiti da questo utente.
     * <p>
     * Relazione molti-a-molti riflessiva: la tabella {@code follow} associa
     * l'utente che segue (colonna {@code seguace_id}) a quello seguito
     * (colonna {@code seguito_id}).
     */
    @ManyToMany
    @JoinTable(
            name = "follow",
            joinColumns = @JoinColumn(name = "seguace_id"),
            inverseJoinColumns = @JoinColumn(name = "seguito_id")
    )
    private Set<Utente> seguiti = new HashSet<>();


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

    public Set<Utente> getSeguiti() {
        return seguiti;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
}

