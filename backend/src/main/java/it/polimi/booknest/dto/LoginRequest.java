package it.polimi.booknest.dto;

/**
 * Data Transfer Object (DTO) utilizzato per incapsulare le credenziali di accesso.
 * Questo oggetto viaggia in ingresso, dal client verso il server, durante la fase di autenticazione.
 * <p>
 * Anche in questo caso, la password viene ricevuta e trasportata in chiaro.
 * La responsabilità di confrontare questa password in chiaro con l'hash cifrato
 * salvato nel database è delegata interamente al Service layer,
 * mantenendo il livello di trasporto puramente passivo.
 * </p>
 */
public class LoginRequest {
    private String username;
    private String password;

    /**
     * Costruttore vuoto di default.
     * <p>
     * È indispensabile per la libreria Jackson, che lo utilizza per
     * istanziare l'oggetto vuoto durante la deserializzazione del payload JSON,
     * per poi popolarlo invocando i relativi metodi setter.
     * </p>
     */
    public LoginRequest(){

    }

    public String getUsername(){
        return username;
    }

    public String getPassword(){
        return password;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public void setPassword(String password){
        this.password = password;
    }

}
