package it.polimi.booknest.service;

import it.polimi.booknest.dto.RisultatoShowdownDTO;
import it.polimi.booknest.model.Libro;
import it.polimi.booknest.model.LibroScelto;
import it.polimi.booknest.model.Showdown;
import it.polimi.booknest.model.Utente;
import it.polimi.booknest.repository.CatalogazioneRepository;
import it.polimi.booknest.repository.LibroRepository;
import it.polimi.booknest.repository.ShowdownRepository;
import it.polimi.booknest.repository.VotoShowdownRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ShowdownServiceTest {

    @Mock
    private ShowdownRepository showdownRepository;
    @Mock
    private VotoShowdownRepository votoShowdownRepository;
    @Mock
    private UtenteService utenteService;
    @Mock private CatalogazioneRepository catalogazioneRepository;
    @Mock private LibroRepository libroRepository;
    @InjectMocks
    private ShowdownService showdownService;

    @Test
    void votiConcorrentiNonVengonoPersi() throws InterruptedException {
        //ARRANGE

        final int NUMERO_THREAD = 50;
        final Long idShowdown = 1L;

        Libro libroA = new Libro("Titolo A", "Autore A", "978-88-06-XXX-A");
        Libro libroB = new Libro("Titolo B", "Autore B", "978-88-06-XXX-B");
        Showdown showdownFinto = new Showdown(libroA, libroB);
        Utente utenteFinto = new Utente();

        when(utenteService.trovaPerId(anyLong())).thenReturn(utenteFinto);
        when(showdownRepository.findById(anyLong())).thenReturn(Optional.of(showdownFinto));
        when(votoShowdownRepository.existsByUtenteAndShowdown(any(Utente.class), any(Showdown.class))).thenReturn(false);

        CountDownLatch via = new CountDownLatch(1);
        CountDownLatch fine = new CountDownLatch(NUMERO_THREAD);
        ExecutorService executor = Executors.newFixedThreadPool(NUMERO_THREAD);

        //ACT
        for(int i = 0; i< NUMERO_THREAD; i++){
            final long idUtente = i;

            executor.submit( () -> {
                try{
                    via.await();
                    showdownService.vota(idUtente, idShowdown, LibroScelto.A);
                }catch (InterruptedException e){
                    Thread.currentThread().interrupt();
                }finally{
                    fine.countDown();
                }
            });
        }

        via.countDown();
        fine.await();
        executor.shutdown();

        //ASSERT

        RisultatoShowdownDTO risultato = showdownService.getRisultati(idShowdown);
        assertEquals(NUMERO_THREAD, risultato.getConteggioA());
        assertEquals(0, risultato.getConteggioB());

    }
}
