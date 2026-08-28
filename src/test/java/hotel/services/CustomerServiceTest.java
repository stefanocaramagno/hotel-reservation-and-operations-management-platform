package hotel.services;

import hotel.models.Customer;
import hotel.repositories.CustomerRepository;

import org.springframework.security.crypto.password.PasswordEncoder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CustomerServiceTest {

    // Attributi
    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private CustomerService customerService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test del Metodo per il Metodo di Registrazione del Customer
    @Test
    public void testRegisterCustomer() {
        Customer customer = new Customer();
        customer.setEmail("test@example.com");
        customer.setPassword("plainPassword");

        when(passwordEncoder.encode("plainPassword")).thenReturn("encodedPassword");

        when(customerRepository.save(any(Customer.class))).thenAnswer(invocation -> invocation.getArgument(0));

        customerService.registerCustomer(customer);

        assertEquals("encodedPassword", customer.getPassword());

        verify(customerRepository, times(1)).save(customer);

        verify(passwordEncoder, times(1)).encode("plainPassword");
    }
}
