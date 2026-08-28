package hotel.services;

import hotel.models.Receptionist;
import hotel.repositories.ReceptionistRepository;

import org.springframework.security.crypto.password.PasswordEncoder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

public class ReceptionistServiceTest {

    // Attributi
    @Mock
    private ReceptionistRepository receptionistRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private ReceptionistService receptionistService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test del Metodo per la Registrazione del Receptionist
    @Test
    public void testRegisterRecepetionist() {
        Receptionist mockRecepetionist = new Receptionist();
        mockRecepetionist.setPassword("plainPassword");

        when(passwordEncoder.encode("plainPassword")).thenReturn("encodedPassword");
        when(receptionistRepository.save(mockRecepetionist)).thenReturn(mockRecepetionist);

        receptionistService.registerReceptionist(mockRecepetionist);

        assertEquals("encodedPassword", mockRecepetionist.getPassword());
        verify(passwordEncoder, times(1)).encode("plainPassword");
        verify(receptionistRepository, times(1)).save(mockRecepetionist);
    }
}
