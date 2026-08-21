package it.polimi.booknest.service;

import it.polimi.booknest.model.Libro;
import it.polimi.booknest.repository.LibroRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class LibroServiceTest {

    @Mock
    private LibroRepository libroRepository;
    @InjectMocks
    LibroService libroService;

    @Test
    void trovaTuttiRestituisceLibriDalRepository(){
        //Arrange
        Libro libro1 = new Libro("Cambiare l'acqua ai fiori", "Valerie Perrin", "9782226429537");
        Libro libro2 = new Libro("Tata", "Valerie Perrin", "9782226496430");
    }
}
