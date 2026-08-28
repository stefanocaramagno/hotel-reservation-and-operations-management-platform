package hotel.services;

import hotel.models.CleaningStaff;
import hotel.repositories.CleaningStaffRepository;

import org.springframework.security.crypto.password.PasswordEncoder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CleaningStaffServiceTest {

    // Attributi
    @Mock
    private CleaningStaffRepository cleaningStaffRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private CleaningStaffService cleaningStaffService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test del Metodo per la Registrazione del Cleaning Staff
    @Test
    public void testRegisterCleaningStaff() {
        CleaningStaff mockCleaningStaff = new CleaningStaff();
        mockCleaningStaff.setPassword("plainPassword");

        when(passwordEncoder.encode("plainPassword")).thenReturn("encodedPassword");
        when(cleaningStaffRepository.save(mockCleaningStaff)).thenReturn(mockCleaningStaff);

        cleaningStaffService.registerCleaningStaff(mockCleaningStaff);

        assertEquals("encodedPassword", mockCleaningStaff.getPassword());
        verify(passwordEncoder, times(1)).encode("plainPassword");
        verify(cleaningStaffRepository, times(1)).save(mockCleaningStaff);
    }

}
