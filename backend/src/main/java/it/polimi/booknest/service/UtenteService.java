package it.polimi.booknest.service;

import it.polimi.booknest.exception.*;
import it.polimi.booknest.model.Utente;
import it.polimi.booknest.repository.UtenteRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

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

    /**
     * Registra il fatto che un utente inizi a seguirne un altro.
     *
     * @param idSeguace l'identificativo dell'utente che segue
     * @param idSeguito l'identificativo dell'utente da seguire
     * @throws UtenteNonTrovatoException se uno dei due utenti non esiste
     * @throws AutoFollowException       se i due identificativi coincidono
     * @throws GiaSeguitoException       se l'utente segue già la persona indicata
     */
    public void segui(Long idSeguace, Long idSeguito) {
        if (idSeguace.equals(idSeguito)) {
            throw new AutoFollowException(idSeguace);
        }

        Utente seguace = trovaPerId(idSeguace);
        Utente seguito = trovaPerId(idSeguito);

        if (!seguace.getSeguiti().add(seguito)) {
            throw new GiaSeguitoException(idSeguace, idSeguito);
        }

        utenteRepository.save(seguace);
    }

    /**
     * Interrompe la relazione di follow tra due utenti.
     * <p>
     * Se l'utente non seguiva la persona indicata, l'operazione non ha effetto:
     * lo stato finale è quello desiderato dal chiamante (idempotenza).
     *
     * @param idSeguace l'identificativo dell'utente che smette di seguire
     * @param idSeguito l'identificativo dell'utente da non seguire più
     * @throws UtenteNonTrovatoException se uno dei due utenti non esiste
     */
    public void smettiDiSeguire(Long idSeguace, Long idSeguito) {
        Utente seguace = trovaPerId(idSeguace);
        Utente seguito = trovaPerId(idSeguito);
        seguace.getSeguiti().remove(seguito);
        utenteRepository.save(seguace);
    }

    /**
     * Restituisce l'insieme degli utenti seguiti dall'utente specificato.
     * <p>
     * Sfrutta il controllo preventivo tramite ID per assicurarsi che l'utente esista;
     * in caso contrario, solleva un'eccezione anziché restituire un insieme vuoto.
     *
     * @param idUtente l'identificativo dell'utente di cui si vogliono conoscere i seguiti
     * @return un insieme di {@link Utente} seguiti
     * @throws UtenteNonTrovatoException se l'utente di riferimento non esiste
     */
    public Set<Utente> trovaSeguiti(Long idUtente) {
        return trovaPerId(idUtente).getSeguiti();
    }
}