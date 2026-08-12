package it.polimi.booknest.dto;

/**
 * Data Transfer Object (DTO) che rappresenta i dati necessari per registrare un nuovo utente.
 * Questo oggetto viaggia in ingresso, dal client verso il server.
 * <p>
 * Nota: la password in questa classe viaggia in chiaro. La cifratura non è
 * responsabilità del livello di trasporto (DTO), ma è demandata esclusivamente
 * alla logica di business all'interno del Service.
 * </p>
 */
public class RegistrazioneRequest {
    private String nome;
    private String cognome;
    private String username;
    private String email;
    private String password;

    /**
     * Costruttore vuoto di default.
     * <p>
     * È strettamente necessario alla libreria Jackson, che lo utilizza per
     * istanziare l'oggetto vuoto durante la deserializzazione del JSON in ingresso,
     * per poi popolarlo utilizzando i relativi metodi setter.
     * </p>
     */
   public RegistrazioneRequest(){
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

    public String getPassword() {
        return password;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
