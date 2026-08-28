package hotel.services;

import hotel.models.CleaningStaff;
import hotel.models.Customer;
import hotel.models.OperationsManager;
import hotel.models.Receptionist;
import hotel.repositories.CustomerRepository;
import hotel.repositories.ReceptionistRepository;
import hotel.repositories.OperationsManagerRepository;
import hotel.repositories.CleaningStaffRepository;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

class CustomUserDetailsServiceTest {

    // Attributi
    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private ReceptionistRepository receptionistRepository;

    @Mock
    private OperationsManagerRepository operationsManagerRepository;

    @Mock
    private CleaningStaffRepository cleaningStaffRepository;

    @InjectMocks
    private CustomUserDetailsService userDetailsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // **Test per il caricamento di un Customer**
    @Test
    void testLoadUserByUsername_Customer() {
        String email = "customer@example.com";
        Customer customer = new Customer();
        customer.setEmail(email);
        customer.setPassword("password123");
        customer.setFullName("John Doe");

        when(customerRepository.findByEmail(email)).thenReturn(customer);

        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        assertNotNull(userDetails);
        assertEquals(email, userDetails.getUsername());
        assertEquals("ROLE_CUSTOMER", userDetails.getAuthorities().iterator().next().getAuthority());
    }

    // **Test per il caricamento di un Receptionist**
    @Test
    void testLoadUserByUsername_Receptionist() {
        String email = "receptionist@example.com";
        Receptionist receptionist = new Receptionist();
        receptionist.setEmail(email);
        receptionist.setPassword("securePass");
        receptionist.setFullName("Alice Smith");

        when(receptionistRepository.findByEmail(email)).thenReturn(receptionist);

        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        assertNotNull(userDetails);
        assertEquals(email, userDetails.getUsername());
        assertEquals("ROLE_RECEPTIONIST", userDetails.getAuthorities().iterator().next().getAuthority());
    }

    // **Test per il caricamento di un Cleaning Staff**
    @Test
    void testLoadUserByUsername_CleaningStaff() {
        String email = "cleaningstaff@example.com";
        CleaningStaff cleaningStaff = new CleaningStaff();
        cleaningStaff.setEmail(email);
        cleaningStaff.setPassword("cleaningPass");
        cleaningStaff.setFullName("Robert Brown");

        when(cleaningStaffRepository.findByEmail(email)).thenReturn(cleaningStaff);

        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        assertNotNull(userDetails);
        assertEquals(email, userDetails.getUsername());
        assertEquals("ROLE_CLEANING_STAFF", userDetails.getAuthorities().iterator().next().getAuthority());
    }

    // **Test per il caricamento di un Operations Manager**
    @Test
    void testLoadUserByUsername_OperationsManager() {
        String email = "manager@example.com";
        OperationsManager operationsManager = new OperationsManager();
        operationsManager.setEmail(email);
        operationsManager.setPassword("managerPass");
        operationsManager.setFullName("Emma Wilson");

        when(operationsManagerRepository.findByEmail(email)).thenReturn(operationsManager);

        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        assertNotNull(userDetails);
        assertEquals(email, userDetails.getUsername());
        assertEquals("ROLE_OPERATIONS_MANAGER", userDetails.getAuthorities().iterator().next().getAuthority());
    }

    // **Test per il caricamento di un utente non esistente**
    @Test
    void testLoadUserByUsername_UserNotFound() {
        String email = "notfound@example.com";

        when(customerRepository.findByEmail(email)).thenReturn(null);
        when(receptionistRepository.findByEmail(email)).thenReturn(null);
        when(cleaningStaffRepository.findByEmail(email)).thenReturn(null);
        when(operationsManagerRepository.findByEmail(email)).thenReturn(null);

        Exception exception = assertThrows(UsernameNotFoundException.class, () ->
            userDetailsService.loadUserByUsername(email)
        );

        assertEquals("User with email '" + email + "' not found", exception.getMessage());
    }
}
