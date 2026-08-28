package hotel.services;

import hotel.models.OperationsManager;
import hotel.repositories.OperationsManagerRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.mockito.Mockito.*;

public class OperationsManagerServiceTest {

    // Attributi
    @Mock
    private OperationsManagerRepository operationsManagerRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private OperationsManagerService operationsManagerService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test del Metodo per la Registrazione dell'Operations Manager
    @Test
    public void testRegisterOperationsManager() {
        OperationsManager mockOperationsManager = new OperationsManager();
        mockOperationsManager.setPassword("plainPassword");

        when(passwordEncoder.encode("plainPassword")).thenReturn("encodedPassword");
        when(operationsManagerRepository.save(mockOperationsManager)).thenReturn(mockOperationsManager);

        operationsManagerService.registerOperationsManager(mockOperationsManager);

        assertEquals("encodedPassword", mockOperationsManager.getPassword());
        verify(passwordEncoder, times(1)).encode("plainPassword");
        verify(operationsManagerRepository, times(1)).save(mockOperationsManager);
    }

}
