package it.polimi.booknest.service;

import it.polimi.booknest.exception.CredenzialiNonValideException;
import it.polimi.booknest.exception.EmailGiaEsistenteException;
import it.polimi.booknest.exception.UsernameGiaEsistenteException;
import it.polimi.booknest.exception.UtenteNonTrovatoException;
import it.polimi.booknest.model.Utente;
import it.polimi.booknest.repository.UtenteRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Gestisce la registrazione, l'autenticazione e il recupero degli utenti.
 * <p>
 * È il punto in cui le regole di dominio relative all'identità vengono applicate:
 * unicità dello username, cifratura della password e verifica delle credenziali.
 * Il metodo {@link #trovaPerId(Long)} costituisce inoltre il punto unico di
 * risoluzione dell'identificativo ricevuto dal client, ed è utilizzato dagli
 * altri servizi che operano per conto di un utente.
 */
@Service
public class UtenteService {
    private final UtenteRepository utenteRepository;
    private final PasswordEncoder passwordEncoder;

    public UtenteService(UtenteRepository utenteRepository, PasswordEncoder passwordEncoder) {
        this.utenteRepository = utenteRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Registra un nuovo utente sulla piattaforma.
     * <p>
     * La password non viene mai memorizzata in chiaro: viene cifrata con BCrypt
     * prima della persistenza. L'unicità dello username e dell'email è garantita
     * anche a livello di database da vincoli di colonna; i controlli eseguiti qui
     * servono a produrre un errore di dominio leggibile.
     *
     * @param nome     nome anagrafico dell'utente
     * @param cognome  cognome anagrafico dell'utente
     * @param username identificativo pubblico scelto per l'accesso
     * @param email    indirizzo di posta elettronica
     * @param password password in chiaro, cifrata prima del salvataggio
     * @return l'utente registrato, con l'identificativo assegnato dal database
     * @throws UsernameGiaEsistenteException se lo username è già in uso
     * @throws EmailGiaEsistenteException    se l'indirizzo email è già in uso
     */
    public Utente registra(String nome, String cognome, String username, String email, String password) {
        if(utenteRepository.existsByUsername(username))
            throw new UsernameGiaEsistenteException(username);

        if(utenteRepository.existsByEmail(email))
            throw new EmailGiaEsistenteException(email);

        String passwordCifrata = passwordEncoder.encode(password);

        Utente nuovoUtente = new Utente(nome, cognome, username, email, passwordCifrata);

        return utenteRepository.save(nuovoUtente);
    }

    /**
     * Autentica un utente verificandone le credenziali.
     * <p>
     * La password memorizzata non viene decifrata: viene ricalcolato il digest
     * di quella fornita e confrontato con quello conservato.
     *
     * @param username         identificativo dell'utente che tenta l'accesso
     * @param passwordInChiaro password fornita dal client
     * @return l'utente autenticato
     * @throws UtenteNonTrovatoException     se nessun utente ha quello username
     * @throws CredenzialiNonValideException se la password non corrisponde
     */
    public Utente login(String username, String passwordInChiaro) {
        Utente utente = utenteRepository.findByUsername(username)
                .orElseThrow(() -> new UtenteNonTrovatoException(username));

        if(passwordEncoder.matches(passwordInChiaro, utente.getPasswordHash())){
            return utente;
        }else{
            throw new CredenzialiNonValideException();
        }
    }

    /**
     * Recupera un utente a partire dal suo identificativo.
     * <p>
     * È il punto unico di risoluzione dell'identificativo trasmesso dal client
     * nell'header delle richieste: garantisce che ogni operazione eseguita per
     * conto di un utente lavori su un'entità effettivamente esistente, evitando
     * il propagarsi di riferimenti non validi.
     *
     * @param id identificativo dell'utente
     * @return l'utente corrispondente
     * @throws UtenteNonTrovatoException se nessun utente ha quell'identificativo
     */
    public Utente trovaPerId(Long id){
        return utenteRepository.findById(id)
                .orElseThrow(()->new UtenteNonTrovatoException(id));
    }
}